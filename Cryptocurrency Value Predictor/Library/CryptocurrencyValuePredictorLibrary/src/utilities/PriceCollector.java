/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import controllers.APIController;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final int readings = 100;
    private int currentSecond;
    private ArrayList<ExchangeRate> bch = new ArrayList<>();
    private ArrayList<ExchangeRate> btc = new ArrayList<>();
    private ArrayList<ExchangeRate> eth = new ArrayList<>();
    private ArrayList<ExchangeRate> ltc = new ArrayList<>();
    private ArrayList<Object> bchTrades = new ArrayList<>();
    private ArrayList<Object> btcTrades = new ArrayList<>();
    private ArrayList<Object> ethTrades = new ArrayList<>();
    private ArrayList<Object> ltcTrades = new ArrayList<>();

    
    
    public PriceCollector() {
        //System.out.println("[INFO] Fetching resource from: " + apiController.API_ENDPOINT + "currencies/");
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
        Currency newCurrency = new Currency("BCH", "Bitcoin Cash");
        currencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin");
        currencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum");
        currencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin");
        currencies[3] = newCurrency;
    }
    
    private void getRecentPrices(){  
        Runnable recentPriceCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                if (currentSecond != 0) {
                    int bchSize = bch.size() + currencies[0].getRates().size();
                    int btcSize = btc.size() + currencies[1].getRates().size();
                    int ethSize = eth.size() + currencies[2].getRates().size();
                    int ltcSize = ltc.size() + currencies[3].getRates().size();
                    
                    if (bchSize < readings) {
                        calculateRecentAverages(bch, apiController.getBCH_TRADES(), bchTrades);
                    }
                    if (btcSize < readings) {
                        calculateRecentAverages(btc, apiController.getBTC_TRADES(), btcTrades);
                    }
                    if (ethSize < readings) {
                        calculateRecentAverages(eth, apiController.getETH_TRADES(), ethTrades);
                    }
                    if (ltcSize < readings) {
                        calculateRecentAverages(ltc, apiController.getLTC_TRADES(), ltcTrades);
                    }
                    
                    if (readings <= bchSize && readings <= btcSize && readings <= ethSize && readings <=  ltcSize) {
                        /*
                        MERGE LISTS
                        Remove historic readings from ScheduledExecutorService
                        */
                    }
                }

                currentSecond++;
                if (currentSecond == 60) {
                    currentSecond = 0;
                }
            }
        };

        historic.scheduleAtFixedRate(recentPriceCollection, 0, 1, TimeUnit.SECONDS);
    }
    
    private void calculateRecentAverages(ArrayList<ExchangeRate> gatheredPrices, String endpoint, ArrayList<Object> oldTrades) {
        ArrayList<Object> currentTrades = oldTrades;
        ExchangeRate rate;
        Double avgPrice;
        boolean foundStart = false;
        String pagination = "";
        
        LocalDateTime tradeTime;
        if (oldTrades.isEmpty()) {
            tradeTime = Helpers.startOfMinute(LocalDateTime.now());
        } else {
            tradeTime = Helpers.localDateTimeParser(((GDAXTrade)oldTrades.get(oldTrades.size() - 1)).getTime());
            tradeTime = tradeTime.plusMinutes(1);
            pagination = "?after=" + ((GDAXTrade)oldTrades.get(oldTrades.size() - 1)).getTrade_id();
            currentTrades.addAll(oldTrades);
            oldTrades.clear();
        }
        
        String json = apiController.getJSONString(endpoint + pagination);
        
        Object[] trades = parser.fromJSON(json);
        
        Object[] currTrades;
        for (Object trade : trades){
            if (Helpers.stringsMatch(((GDAXTrade)trade).getTime(), String.valueOf(tradeTime.minusMinutes(1)), 16)) {
                currentTrades.add(trade);
                foundStart = true;
                if (((GDAXTrade)trade).tradesMatch((GDAXTrade)trades[trades.length - 1])){
                    oldTrades = currentTrades;
                    System.out.println(((GDAXTrade)oldTrades.get(oldTrades.size() - 1)).getTrade_id());
                }
            } else if (foundStart) {
                currTrades = currentTrades.toArray();
                currentTrades.clear();
                avgPrice = calculateAveragePrice(currTrades, tradeTime);
                rate = new ExchangeRate(Helpers.startOfMinute(tradeTime), String.valueOf(avgPrice));
                gatheredPrices.add(rate);
                tradeTime = tradeTime.minusMinutes(1);
            }
        }
    }

    private void initCollector(){
        Runnable automatedCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                Currency[] currencies = get(Helpers.startOfMinute(LocalDateTime.now()));
                System.out.println(currencies[0].getId() + " " + currencies[0].getName() + " " + currencies[0].getValue());
                System.out.println(currencies[1].getId() + " " + currencies[1].getName() + " " + currencies[1].getValue());
                System.out.println(currencies[2].getId() + " " + currencies[2].getName() + " " + currencies[2].getValue());
                System.out.println(currencies[3].getId() + " " + currencies[3].getName() + " " + currencies[3].getValue());
            }
        };
        currentSecond = LocalDateTime.now().getSecond();
        current.scheduleAtFixedRate(automatedCollection, (59 - currentSecond), 60, TimeUnit.SECONDS);
    }

    public Currency[] get(LocalDateTime postTime){
        System.out.println("[INFO] Fetching resource from: " + apiController.getGDAX_ENDPOINT() + "products/.../trades");
        try {
            ExchangeRate rate;
            String json = apiController.getJSONString(apiController.getBCH_TRADES());
            Double avgPrice = calculateAveragePrice(parser.fromJSON(json), postTime);
            rate = new ExchangeRate(postTime, String.valueOf(avgPrice));
            currencies[0].setValue(rate);
            
            json = apiController.getJSONString(apiController.getBTC_TRADES());
            avgPrice = calculateAveragePrice(parser.fromJSON(json), postTime);
            rate = new ExchangeRate(postTime, String.valueOf(avgPrice));
            currencies[1].setValue(rate);
            
            json = apiController.getJSONString(apiController.getETH_TRADES());
            avgPrice = calculateAveragePrice(parser.fromJSON(json), postTime);
            rate = new ExchangeRate(postTime, String.valueOf(avgPrice));
            currencies[2].setValue(rate);
            
            json = apiController.getJSONString(apiController.getLTC_TRADES());
            avgPrice = calculateAveragePrice(parser.fromJSON(json), postTime);
            rate = new ExchangeRate(postTime, String.valueOf(avgPrice));
            currencies[3].setValue(rate);
        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }
        return currencies;
    }
    
    private Double calculateAveragePrice(Object[] trades, LocalDateTime postTime) {
        Double avgPrice = 0.0;
        int tradeNo = 0;
        boolean foundStart = false;

        for (Object trade : trades){
            if (Helpers.stringsMatch(((GDAXTrade)trade).getTime(), String.valueOf(postTime.minusMinutes(1)), 16)) {
            avgPrice += Double.parseDouble(((GDAXTrade)trade).getPrice());
            tradeNo += 1;
            foundStart = true;
            } else if (foundStart) break;
        }

        /*
            POST TO API
        */
        
        return (avgPrice /= tradeNo);
    }
}
