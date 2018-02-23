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
    private LocalDateTime firstRelevantReading;
    private int currentSecond;
    private boolean connectedToDatabase;
    
    public PriceCollector() {
        //System.out.println("[INFO] Fetching resource from: " + gdaxAPIController.API_ENDPOINT + "/currencies/");
        try {
            initialise();

        } catch (Exception e){
            System.out.println("[INFO] Error: " + e);
        }
    }
    
    private void initialise(){
        initClass();
        initCurrencies();
        initCollector();
        initHistoricCollector();
    }
    
    private void initClass(){
        gdaxAPIController = new GDAXAPIController();
        currencyAPIController = new CurrencyAPIController();
        exchangeRateAPIController = new ExchangeRateAPIController();
        current = Executors.newSingleThreadScheduledExecutor();
        historic = Executors.newSingleThreadScheduledExecutor();
        connectedToDatabase = false;
        firstRelevantReading = LocalDateTimeHelper.startOfMinute(LocalDateTime.now().minusMinutes(10000));
    }
    
    private void initCurrencies(){
            currencies = currencyAPIController.getCurrencies(Globals.API_ENDPOINT + "/currency");
            if (currencies.length == 0){
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
            } else {
                System.out.println("[INFO] Successfully connected to Oracle database. Initialising catch up.");
                connectedToDatabase = true;
            }
            
    }
    
    private void initHistoricCollector(){  
        Runnable historicPriceCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                try {
                    if (0 < currentSecond && currentSecond < 59 ) {
                        int[] sizes = new int[currencies.length];
                        int readingsTaken = 0;
                        int readings = Globals.STARTGOFAI;
                        GDAXTrade[] trades;

                        for (int i = 0; i < sizes.length; i++){
                            sizes[i] = currencies[i].getNumberOfRatesCollected();
                        }

                        if (collectionCompleted(readings, sizes)) {
                            if (!currencies[0].isCalculatingGOFAI()){
                                for (Currency currency : currencies){
                                    if (connectedToDatabase){
                                        exchangeRateAPIController.post(Globals.API_ENDPOINT + "/exchangerate", SafeCastHelper.objectsToExchangeRates(currency.getHistoricRates().toArray()));
                                    }
                                    currency.mergeRates();
                                }
                                System.out.println("[INFO] First price list merge complete. Can now calculate GOFAI predictions.");
                            }
                            
                            readings = Globals.STARTNN;
                            if (collectionCompleted(readings, sizes)) {
                                for (Currency currency : currencies){
                                    if (connectedToDatabase){
                                        exchangeRateAPIController.post(Globals.API_ENDPOINT + "/exchangerate", SafeCastHelper.objectsToExchangeRates(currency.getHistoricRates().toArray()));
                                    }
                                    currency.mergeRates();
                                }
                                System.out.println("[INFO] Second price list merge complete. Can now calculate Neural Network predictions.");
                                System.out.println("[INFO] Shutting down historic price collection thread.");
                                PricePredictor.gofaiTest(currencies);
                                historic.shutdownNow();
                            }
                        }

                        while (readingsTaken < 4){
                            for (Currency currency : currencies){
                                if (currency.getNumberOfRatesCollected() < readings){
                                    trades = getHistoricTrades(currency);
                                    calculateHistoricAverages(currency, trades);
                                    readingsTaken++;
                                }
                                if (readingsTaken == 4) {
                                    System.out.println("[INFO] 4 readings taken. 1 second break for GDAX API.");
                                    break;
                                }
                            }
                            for (int i = 0; i < sizes.length; i++){
                                sizes[i] = currencies[i].getNumberOfRatesCollected();
                            }
                            if (collectionCompleted(readings, sizes)){
                                break;
                            }
                        }
                    }
                } catch (Exception e){
                    System.out.println("[INFO] Error: " + e);
                }
            }
        };
        historic.scheduleWithFixedDelay(historicPriceCollection, 0, 1, TimeUnit.SECONDS);
    }
    
    private GDAXTrade[] getHistoricTrades(Currency currency) {
        GDAXTrade[] trades;
        String pagination;
        String json;
        
        if (currency.getLastHistoricTrade() == null) {
            trades = gdaxAPIController.getGDAXTrades(currency.getGDAXEndpoint());
        } else {
            pagination = "?after=" + currency.getLastHistoricTrade().getTrade_id();
            trades = gdaxAPIController.getGDAXTrades(currency.getGDAXEndpoint() + pagination);
        }
        return trades;
    }
    
    private boolean collectionCompleted(int readings, int[] sizes){
        boolean completed = true;
        for (int i : sizes){
            if (i < readings){
                completed = false;
                break;
            }
        }
        return completed;
    }

    private void initCollector(){
        Runnable automatedCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                currentSecond++;
                if (currentSecond == 60) {
                    currentSecond = 0;
                    getCurrentPrices(LocalDateTimeHelper.startOfMinute(LocalDateTime.now()));
                }
            }
        };
        currentSecond = LocalDateTime.now().getSecond();
        current.scheduleAtFixedRate(automatedCollection, 1, 1, TimeUnit.SECONDS);
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
                }
                System.out.println(currency.getID() + " " + currency.getName() + " " + currency.getRate());
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
    
    private Double calculateMeanPrice(GDAXTrade[] trades) {
        List<Double> prices = new ArrayList<>();
        Double meanPrice;
        
        for (GDAXTrade trade : trades){
            prices.add(trade.getPrice());
        }
        
        meanPrice = MathsHelper.mean(SafeCastHelper.objectsToDoubles(prices.toArray()));
        return meanPrice;
    }
    
    private void calculateHistoricAverages(Currency currency, GDAXTrade[] trades) {
        if (trades.length == 0) {
            System.out.println("[INFO] calculateHistoricAverages received no trades. GDAX endpoint may be down...");
            return;
        }
        ExchangeRate rate;
        Double meanPrice;
        LocalDateTime tradeTime;
        
        if (!currency.hasFoundPosition()) {
            tradeTime = LocalDateTimeHelper.startOfMinute(LocalDateTime.now());
            System.out.println("[INFO] " + currency.getID() + " starting prices at " + tradeTime.getHour() + ":" + tradeTime.getMinute());
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
                    System.out.println("[INFO] Posting " + currency.getID() + " trade for: " + tradeTime.getHour() + ":" + tradeTime.getMinute());
                    tradeTime = tradeTime.minusMinutes(1);
                    currency.addHistoricTrade((GDAXTrade)trade);
                }
            }
        }
    }
}
