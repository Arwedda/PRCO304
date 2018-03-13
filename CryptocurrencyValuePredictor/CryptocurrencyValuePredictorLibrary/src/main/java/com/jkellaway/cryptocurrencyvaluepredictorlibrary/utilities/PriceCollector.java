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
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.valuepredictor.CryptocurrencyValuePredictor;
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
    private ScheduledExecutorService collector;
    private LocalDateTime firstRelevantRate;
    private boolean connectedToDatabase;
    private boolean firstLap;
    
    public PriceCollector() {
        initialise();
    }
    
    private void initialise(){
        initPriceCollector();
        initCurrencies();
        initCollector();
    }
    
    private void initPriceCollector(){
        gdaxAPIController = new GDAXAPIController();
        currencyAPIController = new CurrencyAPIController();
        exchangeRateAPIController = new ExchangeRateAPIController();
        collector = Executors.newSingleThreadScheduledExecutor();
        firstRelevantRate = LocalDateTimeHelper.startOfMinute(LocalDateTime.now().minusMinutes(Globals.READINGSREQUIRED));
        connectedToDatabase = false;
        firstLap = true;
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
        /*
            Not intended for project - only as failsafe. Once tested can use rate
            number to run GOFAI and NN mode after project
        */
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
        
        for (Currency currency : currencies){
            currency.findGaps(firstRelevantRate);
        }
    }
    
    private void sortRelevantRates(){
        ExchangeRate[] rates, updatedRates;
        for (Currency currency : currencies){
            rates = exchangeRateAPIController.getExchangeRates(Globals.API_ENDPOINT + "/exchangerate/" + currency.getID());
            for (ExchangeRate rate : rates){
                rate.setTimestamp(rate.getTimestamp());
                if (!rate.getLDTTimestamp().isBefore(firstRelevantRate)){
                    currency.addHistoricRate(rate);
                }
            }
            currency.findGaps(firstRelevantRate);
            currency.mergeRates();
            updatedRates = currency.calculateGrowth(false);
            exchangeRateAPIController.put(Globals.API_ENDPOINT + "/exchangerate", updatedRates);
        }
    }
    
    private void initCollector(){
        Runnable automatedCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                LocalDateTime currentTime = LocalDateTimeHelper.startOfMinute(LocalDateTime.now());
                LocalDateTime currentRateTime = null;
                try {
                    currentRateTime = currencies[0].getRate().getLDTTimestamp();
                } catch (Exception e){
                    //Protection from empty rate arraylist
                }
                
                if (!currentTime.equals(currentRateTime)) {
                    getCurrentPrices(currentTime);
                } else {
                    if (gapsFilled()){
                        calculateHistoricGrowth();
                    }
                    getHistoricPrices();
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
                            if (currency.getLastGap().getPaginationStart() == 0) {
                                currency.getLastGap().setPaginationStart(trades[trades.length - 1].getTrade_id());
                            }
                        }
                    }
                    CryptocurrencyValuePredictor.getInstance().setCurrencies(currencies);
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
            
            private Double calculateMeanPrice(GDAXTrade[] trades) {
                List<Double> prices = new ArrayList<>();
                Double meanPrice;

                for (GDAXTrade trade : trades){
                    prices.add(trade.getPrice());
                }

                meanPrice = MathsHelper.mean(SafeCastHelper.objectsToDoubles(prices.toArray()));
                return meanPrice;
            }
            
            private boolean gapsFilled(){
                boolean filled = true;
                int ratesRequired = 0;
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
                        filled = false;
                    }
                }
                return filled;
            }
            
            private void calculateHistoricGrowth(){
                ExchangeRate[] updatedRates;
                for (Currency currency : currencies){
                    currency.mergeRates();
                    updatedRates = currency.calculateGrowth(!firstLap);
                    if (connectedToDatabase){
                        exchangeRateAPIController.put(Globals.API_ENDPOINT + "/exchangerate", updatedRates);
                    }
                }
                if (firstLap){
                    for (Currency currency : currencies){
                        currency.findGaps(firstRelevantRate);
                    }
                    System.out.println("[INFO] First lap completed. Attempting to collect failed prices.");
                    firstLap = false;
                } else {
                    System.out.println("[INFO] Finished historic collection. Switching current price mode");
                    CryptocurrencyValuePredictor.getInstance().pricesCollected(currencies);
                    //collector.shutdownNow();
                    //initCollector();
                }
            }
            
            private void getHistoricPrices(){
                int readingsTaken = 0;
                GDAXTrade[] trades;
                while (readingsTaken < 4){
                    boolean needsMoreReadings = false;
                    for (Currency currency : currencies){
                        if (!currency.getGaps().isEmpty()){
                            Gap gap = currency.getLastGap();
                            if (!collectionCompleted(gap.getRatesRequired(), currency.noOfHistoricRates())){
                                trades = getHistoricTrades(currency, gap);
                                calculateHistoricAverages(currency, trades, gap);
                                boolean noNewData = currency.dumpDuplicates();
                                if (noNewData) {
                                    currency.getGaps().remove(gap);
                                } else {
                                    if (connectedToDatabase){
                                        exchangeRateAPIController.post(Globals.API_ENDPOINT + "/exchangerate", SafeCastHelper.objectsToExchangeRates(currency.getHistoricRates().toArray()));
                                    }
                                    currency.gradualMerge();
                                }
                                readingsTaken++;
                            }
                            if (readingsTaken == 4) {
                                System.out.println("[INFO] 4 readings taken. 1 second break for GDAX API.");
                                break;
                            } else if (!collectionCompleted(gap.getRatesRequired(), currency.noOfHistoricRates())){
                                needsMoreReadings = true;
                            }
                        }
                    }
                    if (!needsMoreReadings){
                        break;
                    }
                }
            }
            
            private boolean collectionCompleted(int required, int collected){
                return (required <= collected);
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
                        tradeTime = LocalDateTimeHelper.startOfMinute(trades[0].getTime());
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
        };
        if (firstLap) {
            collector.scheduleWithFixedDelay(automatedCollection, 1, 1, TimeUnit.SECONDS);
        } else {
            int initDelay = 60 - LocalDateTime.now().getSecond();
            if (initDelay == 60) {
                initDelay = 1;
            }
            collector.scheduleAtFixedRate(automatedCollection, initDelay, 60, TimeUnit.SECONDS);
        }
    }
}
