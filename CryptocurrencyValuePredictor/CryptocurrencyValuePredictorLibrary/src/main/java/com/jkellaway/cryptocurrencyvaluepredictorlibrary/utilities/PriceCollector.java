/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;


import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.CurrencyAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.ExchangeRateAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.GDAXAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.MathsHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.SafeCastHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.GDAXTrade;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Gap;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author jkell
 */
public class PriceCollector {
    private GDAXAPIController gdaxAPIController;
    private CurrencyAPIController currencyAPIController;
    private ExchangeRateAPIController exchangeRateAPIController;
    private Currency[] currencies;
    private ScheduledExecutorService current;
    private ScheduledExecutorService historic;
    private LocalDateTime firstRelevantRate;
    private int liveReadings;
    private int currentSecond;
    private boolean connectedToDatabase;
    
    public PriceCollector() {
        try {
            initialise();
        } catch (Exception e){
            System.out.println("[INFO] Error: " + e);
        }
    }
    
    private void initialise(){
        initPriceCollector();
        initCurrencies();
        initCurrentCollector();
        initHistoricCollector();
    }
    
    private void initPriceCollector(){
        gdaxAPIController = new GDAXAPIController();
        currencyAPIController = new CurrencyAPIController();
        exchangeRateAPIController = new ExchangeRateAPIController();
        current = Executors.newSingleThreadScheduledExecutor();
        historic = Executors.newSingleThreadScheduledExecutor();
        connectedToDatabase = false;
        firstRelevantRate = LocalDateTimeHelper.startOfMinute(LocalDateTime.now().minusMinutes(Globals.READINGSREQUIRED));
    }
    
    private void initCurrencies(){
        try {    
            currencies = currencyAPIController.getCurrencies(Globals.API_ENDPOINT + "/currency");
            if (currencies.length == 0){
                storageFreeMode();
            } else {
                System.out.println("[INFO] Successfully connected to Oracle database. Initialising catch up.");
                sortRelevantRates();
                connectedToDatabase = true;
            }
        } catch (Exception e){
            storageFreeMode();
        }
    }
    
    private void storageFreeMode(){
        System.out.println("[INFO] Failed to connect to Oracle database. Initialising local mode.");
        currencies = new Currency[4];
        Currency newCurrency = new Currency("BCH", "Bitcoin Cash", Globals.BCH_TRADES);
        currencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", Globals.BTC_TRADES);
        currencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", Globals.ETH_TRADES);
        currencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", Globals.LTC_TRADES);
        currencies[3] = newCurrency;
    }
    
    private void sortRelevantRates(){
        ExchangeRate[] rates;
        for (Currency currency : currencies){
            rates = exchangeRateAPIController.getExchangeRates(Globals.API_ENDPOINT + "/exchangerate/" + currency.getID());
            for (ExchangeRate rate : rates){
                rate.setTimestamp(rate.getTimestamp());
                if (!rate.getLDTTimestamp().isBefore(firstRelevantRate)){
                    currency.addHistoricRate(rate);
                }
            }
            currency.initialMerge(firstRelevantRate);
        }
    }
    
    private void initCurrentCollector(){
        Runnable automatedCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                currentSecond++;
                if (currentSecond == 60) {
                    currentSecond = 0;
                    liveReadings++;
                    getCurrentPrices(LocalDateTimeHelper.startOfMinute(LocalDateTime.now()));
                }
            }
            
            private void getCurrentPrices(LocalDateTime postTime){
                System.out.println("[INFO] Fetching resource from: " + Globals.GDAX_ENDPOINT + "/products/.../trades");
                try {
                    GDAXTrade[] trades;
                    Double meanPrice;
                    ExchangeRate rate;
                    for (Currency currency : currencies){
                        trades = gdaxAPIController.getGDAXTrades(currency.getGDAXEndpoint());
                        trades = getRelevantTrades(trades, postTime);
                        meanPrice = calculateMeanPrice(trades);
                        if (meanPrice == null){
                            meanPrice = currency.getRate().getValue();
                        }
                        rate = new ExchangeRate(currency.getID(), postTime, meanPrice, null, null, null, trades[trades.length - 1].getTrade_id());
                        currency.setValue(rate);

                        if (connectedToDatabase){
                            exchangeRateAPIController.post(Globals.API_ENDPOINT + "/exchangerate", rate);
                            if (currency.getLastGap().getPaginationStart() == 0){
                                currency.getLastGap().setPaginationStart(trades[trades.length - 1].getTrade_id());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("[INFO] Error: " + e);
                }
            }
            
            private GDAXTrade[] getRelevantTrades(GDAXTrade[] trades, LocalDateTime postTime){
                ArrayList<GDAXTrade> relevantTrades = new ArrayList<>();
                boolean foundStart = false;
                for (GDAXTrade trade : trades){
                    if (trade.getTime().equals(postTime.minusMinutes(1))) {
                    relevantTrades.add(trade);
                    foundStart = true;
                    } else if (foundStart) break;
                }
                return SafeCastHelper.objectsToGDAXTrades(relevantTrades.toArray());
            }
        };
        currentSecond = LocalDateTime.now().getSecond();
        liveReadings = 0;
        current.scheduleAtFixedRate(automatedCollection, 1, 1, TimeUnit.SECONDS);
    }

    private void initHistoricCollector(){
        Runnable historicPriceCollection;
        historicPriceCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection(currencies[0].getRates().isEmpty());
            }

            private void priceCollection(boolean noRelevantData) {
                try {
                    if (permittedAPIAccess()){
                        if (noRelevantData){
                            freshHarvest();
                        } else {
                            collectMissingData();
                        }
                    }
                } catch (Exception e){
                    System.out.println("[INFO] Error: " + e);
                }
            }
            
            private void collectMissingData(){
                int readingsTaken = 0;
                GDAXTrade[] trades;
                int ratesRequired = 0;
                boolean gapsFilled = true;
                List<Gap> gaps, toRemove;
                
                for (Currency currency : currencies){
                    gaps = currency.getGaps();
                    toRemove = new ArrayList<>();
                    for (Gap gap : gaps){
                        ratesRequired = gap.getRatesRequired();
                        if (ratesRequired == 0){
                            toRemove.add(gap);
                        }
                    }
                    if (!toRemove.isEmpty()){
                        gaps.removeAll(toRemove);
                    }
                    if (!gaps.isEmpty()) {
                        gapsFilled = false;
                    }
                }
                
                if (gapsFilled){
                    for (Currency currency : currencies){
                        currency.mergeRates();
                    }
                    System.out.println("[INFO] Shutting down historic price collection thread.");
                    historic.shutdownNow();
                }
                
                while (readingsTaken < 4){
                    boolean needsMoreReadings = false;
                    for (Currency currency : currencies){
                        try {
                            Gap gap = currency.getLastGap();
                            if (!collectionCompleted(gap.getRatesRequired(), currency.noOfHistoricRates())){
                                trades = getHistoricTrades(currency, gap);
                                calculateHistoricAverages(currency, trades, gap);
                                currency.dumpDuplicates();
                                exchangeRateAPIController.post(Globals.API_ENDPOINT + "/exchangerate", SafeCastHelper.objectsToExchangeRates(currency.getHistoricRates().toArray()));
                                currency.gradualMerge();
                                readingsTaken++;
                            }
                            if (readingsTaken == 4) {
                                System.out.println("[INFO] 4 readings taken. 1 second break for GDAX API.");
                                break;
                            } else if (!collectionCompleted(gap.getRatesRequired(), currency.noOfHistoricRates())){
                                needsMoreReadings = true;
                            }
                        } catch (Exception e) {
                            System.out.println("[INFO] Error: " + e + " on " + currency.getID() + ". Filled required data.");
                        }
                    }
                    if (!needsMoreReadings){
                        break;
                    }
                }
            }
            
            private void freshHarvest(){
                try {
                    int[] sizes = new int[currencies.length];
                    int readingsTaken = 0;
                    int historicPricesRequired = Globals.STARTGOFAI - liveReadings;
                    GDAXTrade[] trades;

                    for (int i = 0; i < sizes.length; i++){
                        sizes[i] = currencies[i].noOfHistoricRates();
                    }

                    if (collectionCompleted(historicPricesRequired, sizes)) {
                        if (!currencies[0].isCalculatingGOFAI()){
                            for (Currency currency : currencies){
                                handleMerge(currency, "GOFAIMerge");
                            }
                            liveReadings = 0;
                            System.out.println("[INFO] First price list merge complete. Can now calculate GOFAI predictions.");
                        }

                        historicPricesRequired = Globals.STARTNN - (liveReadings + Globals.STARTGOFAI);
                        if (collectionCompleted(historicPricesRequired, sizes)) {
                            if (!currencies[0].isCalculatingNN()){
                                for (Currency currency : currencies){
                                    handleMerge(currency, "NNMerge");
                                }
                                liveReadings = 0;
                                System.out.println("[INFO] Second price list merge complete. Can now calculate Neural Network predictions.");
                            }

                            historicPricesRequired = Globals.READINGSREQUIRED - (liveReadings + Globals.STARTNN);
                            if (collectionCompleted(historicPricesRequired, sizes)) {
                                for (Currency currency : currencies){
                                    handleMerge(currency, "merge");
                                }
                                System.out.println("[INFO] Shutting down historic price collection thread.");
                                PricePredictor.gofaiTest(currencies);
                                historic.shutdownNow();
                            }
                        }
                    }

                    while (readingsTaken < 4){
                        for (Currency currency : currencies){
                            if (currency.noOfHistoricRates() < historicPricesRequired){
                                trades = getHistoricTrades(currency, null);
                                calculateHistoricAverages(currency, trades, null);
                                readingsTaken++;
                            }
                            if (readingsTaken == 4) {
                                System.out.println("[INFO] 4 readings taken. 1 second break for GDAX API.");
                                break;
                            }
                        }
                        for (int i = 0; i < sizes.length; i++){
                            sizes[i] = currencies[i].noOfHistoricRates();
                        }
                        if (collectionCompleted(historicPricesRequired, sizes)){
                            break;
                        }
                    }
                } catch (Exception e){
                    System.out.println("[INFO] Error: " + e);
                }
            }
            
            private boolean permittedAPIAccess(){
                return (0 < currentSecond && currentSecond < 59);
            }
            
            private GDAXTrade[] getHistoricTrades(Currency currency, Gap gap) {
                String pagination = "";
                if (currency.getLastHistoricTrade() != null) {
                    pagination = "?after=" + currency.getLastHistoricTrade().getTrade_id();
                } else if (gap != null) {
                    Integer lastTrade = gap.getPaginationStart();
                    if (0 < lastTrade){
                        pagination = "?after=" + lastTrade;
                    }
                }
                return gdaxAPIController.getGDAXTrades(currency.getGDAXEndpoint() + pagination);
            }

            private boolean collectionCompleted(int required, int[] collected){
                boolean completed = true;
                for (int gathered : collected){
                    if (!collectionCompleted(required, gathered)){
                        completed = false;
                        break;
                    }
                }
                return completed;
            }
            
            private boolean collectionCompleted(int required, int collected){
                return (required <= collected);
            }
            
            private void calculateHistoricAverages(Currency currency, GDAXTrade[] trades, Gap gap) {
                if (trades.length == 0) {
                    System.out.println("[INFO] calculateHistoricAverages received no trades. GDAX endpoint may be down...");
                    return;
                }
                ExchangeRate rate;
                Double meanPrice;
                LocalDateTime tradeTime;

                if (!currency.hasFoundPosition()) {
                    if (gap == null){
                        tradeTime = LocalDateTimeHelper.startOfMinute(LocalDateTime.now());
                        System.out.println("[INFO] " + currency.getID() + " starting prices at " + tradeTime.getHour() + ":" + tradeTime.getMinute());
                    } else {
                        tradeTime = LocalDateTimeHelper.startOfMinute(trades[0].getTime()).plusMinutes(1);
                    }
                } else {
                    tradeTime = currency.getLastHistoricTrade().getTime().plusMinutes(1);
                }

                for (GDAXTrade trade : trades){
                    if (trade.getTime().equals(tradeTime.minusMinutes(1))) {
                        currency.addHistoricTrade(trade);
                    } else {
                        if (currency.hasFoundPosition()) {
                            meanPrice = calculateMeanPrice(SafeCastHelper.objectsToGDAXTrades(currency.getHistoricTrades().toArray()));
                            rate = new ExchangeRate(currency.getID(), tradeTime.toString(), meanPrice, null, null, null, trade.getTrade_id());
                            currency.addHistoricRate(rate);
                            tradeTime = tradeTime.minusMinutes(1);
                            currency.addHistoricTrade(trade);
                        }
                    }
                }
            }
            
            private void handleMerge(Currency currency, String mergeType){
                currency.dumpDuplicates();
                if (connectedToDatabase){
                    exchangeRateAPIController.post(Globals.API_ENDPOINT + "/exchangerate", SafeCastHelper.objectsToExchangeRates(currency.getHistoricRates().toArray()));
                }
                try {
                    Method method = Currency.class.getMethod(mergeType);
                    method.invoke(currency);
                } catch (Exception e){
                    System.out.println("[INFO] Error: " + e);
                }
                
                ExchangeRate[] updatedRates = currency.calculateGrowth();
                if (connectedToDatabase){
                    exchangeRateAPIController.put(Globals.API_ENDPOINT + "/exchangerate", updatedRates);
                }
            }
        };
        historic.scheduleWithFixedDelay(historicPriceCollection, 0, 1, TimeUnit.SECONDS);
    }
    
    private Double calculateMeanPrice(GDAXTrade[] trades) {
        List<Double> prices = new ArrayList<>();
        Double meanPrice;
        
        for (GDAXTrade trade : trades){
            prices.add(trade.getPrice());
        }
        
        meanPrice = MathsHelper.mean(SafeCastHelper.objectsToDoubles(prices.toArray()));
        return meanPrice;
    }
}
