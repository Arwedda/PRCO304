/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import controllers.APIController;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Currency;
import model.ExchangeRate;
import model.GDAXTrade;

/**
 *
 * @author jkell
 */
public class PriceCollector {
    private APIController apiController = new APIController();
    private Currency[] currencies = new Currency[4];
    private JsonParser parser = new JsonParser();
    private ScheduledExecutorService current = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService historic = Executors.newSingleThreadScheduledExecutor();
    private final int startGOFAI = 21;
    private final int startNN = 1001;
    private int currentSecond;
    
    public PriceCollector() {
        //System.out.println("[INFO] Fetching resource from: " + apiController.API_ENDPOINT + "/currencies/");
        try {
            setUpCurrencies();
            /*
                Websocket listener will act as collector (rather than RESTful) if the bug is ever ironed out:
                It can't find an implementation of ContainerProvider.getWebSocketContainer();
                [Info] Error: java.lang.RuntimeException: Could not find an implementation class.
                [INFO] Error: java.lang.NullPointerException
            initWebsocketListener();
            */
            initCollector();
            getRecentPrices();
        } catch (Exception e){
            System.out.println("[INFO] Error: " + e);
        }
    }
    
    private void initWebsocketListener(){
        /*try {
            wsListener = new WebsocketListener(new URI("wss://ws-feed.gdax.com"));
        
            wsListener.addMessageHandler(new WebsocketListener.MessageHandler() {
                @Override
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            wsListener.sendMessage("{\"type\": \"subscribe\", \"product_ids\": [\"BCH-USD\", \"BTC-USD\", \"ETH-USD\", \"LTC-USD\"], \"channels\": [\"level2\", \"heartbeat\"] }");

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }*/
    }
    
    private void setUpCurrencies(){
        /*
        GET FROM API
        */
        Currency newCurrency = new Currency("BCH", "Bitcoin Cash", apiController.getBCH_TRADES());
        currencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", apiController.getBTC_TRADES());
        currencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", apiController.getETH_TRADES());
        currencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", apiController.getLTC_TRADES());
        currencies[3] = newCurrency;
    }
    
    private void getRecentPrices(){  
        Runnable recentPriceCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                try {
                    if (0 < currentSecond && currentSecond < 59 ) {
                        int[] sizes = new int[currencies.length];
                        int readingsTaken = 0;
                        int readings = startGOFAI;
                        Object[] trades;

                        for (int i = 0; i < sizes.length; i++){
                            sizes[i] = currencies[i].getNumberOfRatesCollected();
                        }

                        if (collectionCompleted(readings, sizes)) {
                            if (!currencies[0].isCalculatingGOFAI()){
                                for (Currency currency : currencies){
                                    currency.mergePrices();
                                }
                                System.out.println("[INFO] First price list merge complete. Can now calculate GOFAI predictions.");
                            }
                            readings = startNN;
                            if (collectionCompleted(readings, sizes)) {
                                for (Currency currency : currencies){
                                    currency.mergePrices();
                                }
                                System.out.println("[INFO] Second price list merge complete. Can now calculate Neural Network predictions.");
                                System.out.println("[INFO] Shutting down historic price collection thread.");
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
        historic.scheduleWithFixedDelay(recentPriceCollection, 0, 1, TimeUnit.SECONDS);
    }
    
    private Object[] getHistoricTrades(Currency currency) {
        Object[] trades;
        String pagination;
        String json;
        
        if (currency.getLastHistoricTrade() == null) {
            json = apiController.getJSONString(currency.getGDAXEndpoint());
            trades = parser.fromJSON(json);
        } else {
            pagination = "?after=" + currency.getLastHistoricTrade().getID();
            json = apiController.getJSONString(currency.getGDAXEndpoint() + pagination);
            trades = parser.fromJSON(json);
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
                    getCurrentPrices(Helpers.startOfMinute(LocalDateTime.now()));
                    for (Currency currency : currencies){
                        System.out.println(currency.getID() + " " + currency.getName() + " " + currency.getRate());
                    }
                }
            }
        };
        currentSecond = LocalDateTime.now().getSecond();
        current.scheduleAtFixedRate(automatedCollection, 1, 1, TimeUnit.SECONDS);
    }

    public void getCurrentPrices(LocalDateTime postTime){
        System.out.println("[INFO] Fetching resource from: " + apiController.getGDAX_ENDPOINT() + "/products/.../trades");
        try {
            Object[] trades;
            Double avgPrice;
            ExchangeRate rate;
            for (Currency currency : currencies){
                trades = getTrades(currency);
                avgPrice = calculateAveragePrice(trades, postTime);
                rate = new ExchangeRate(postTime, avgPrice);
                currency.setValue(rate);
            }
        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }
    }
    
    public Object[] getTrades(Currency currency){
        Object[] trades;
        String json = apiController.getJSONString(currency.getGDAXEndpoint());
        trades = parser.fromJSON(json);
        return trades;
    }
    
    public Double calculateAveragePrice(Object[] trades, LocalDateTime postTime) {
        Double avgPrice = 0.0;
        int tradeNo = 0;
        boolean foundStart = false;

        for (Object trade : trades){
            if (((GDAXTrade)trade).getTime().equals(postTime.minusMinutes(1))) {
            avgPrice += ((GDAXTrade)trade).getPrice();
            tradeNo += 1;
            foundStart = true;
            } else if (foundStart) break;
        }

        /*
            POST TO API
        */
        
        return (avgPrice /= tradeNo);
    }
    
    public void calculateHistoricAverages(Currency currency, Object[] trades) {
        if (trades.length == 0) {
            System.out.println("[INFO] calculateHistoricAverages received no trades. GDAX endpoint may be down...");
            return;
        }
        ExchangeRate rate;
        Double avgPrice;
        LocalDateTime tradeTime;
        
        if (!currency.hasFoundPosition()) {
            tradeTime = Helpers.startOfMinute(LocalDateTime.now());
            System.out.println("[INFO] " + currency.getID() + " starting prices at " + tradeTime.getHour() + ":" + tradeTime.getMinute());
        } else {
            tradeTime = currency.getLastHistoricTrade().getTime().plusMinutes(1);
        }
        
        for (Object trade : trades){
            if (((GDAXTrade)trade).getTime().equals(tradeTime.minusMinutes(1))) {
                currency.addHistoricTrade((GDAXTrade)trade);
            } else {
                if (currency.hasFoundPosition()) {
                    avgPrice = calculateAveragePrice((Object[])currency.getHistoricTrades().toArray(), tradeTime);
                    rate = new ExchangeRate(tradeTime, avgPrice);
                    currency.addHistoricRate(rate);
                    System.out.println("[INFO] Posting " + currency.getID() + " trade for: " + tradeTime.getHour() + ":" + tradeTime.getMinute());
                    tradeTime = tradeTime.minusMinutes(1);
                    currency.addHistoricTrade((GDAXTrade)trade);
                }
            }
        }
    }

    private void gofaiTest(Currency currency, int testSize){
        double[] predictions = new double[20];
        ExchangeRate[] rates = (ExchangeRate[]) currency.getRates().toArray();
        ExchangeRate[] currentRates;

        for (int i = 0; i < testSize; i++) {
            currentRates = Arrays.copyOfRange(rates, (rates.length - 21) - i, (rates.length - 1) - i);
            predictions = getPredictions(currentRates);
            currency.getRates().get((currency.getRates().size() - 1) - i).GOFAINextGrowth = predictions;
        }

    }

    private double[] getPredictions(ExchangeRate[] rates){
        int preds = 20;
        double[] predictions = new double[preds];

        for (int i = 0; i < preds; i++){
            predictions[i] = averageGrowth(rates, i+1);
        }
        return predictions;
    }

    private double averageGrowth(ExchangeRate[] rate, int entriesToUse){
        double avg = 0.0;
        for (int i = 0; i < entriesToUse; i++){
            avg += rate[(rate.length - 1) - i].getGrowth();
        }
        return (avg / rate.length);
    }
}
