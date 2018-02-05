/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import controllers.APIController;
import java.net.URI;
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
    private Helpers helper = new Helpers();
    private ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    private final int readings = 100;
    
    private WebsocketListener wsListener;

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
        try {
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
        }
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
        /*Runnable recentPriceCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                String json = apiController.getJSONString(GDAX_ENDPOINT + BCH_TRADES);
                calculateRecentAverages(json, readings);
                json = apiController.getJSONString(GDAX_ENDPOINT + BTC_TRADES);
                calculateRecentAverages(json, readings);
                json = apiController.getJSONString(GDAX_ENDPOINT + ETH_TRADES);
                calculateRecentAverages(json, readings);
                json = apiController.getJSONString(GDAX_ENDPOINT + LTC_TRADES);
                calculateRecentAverages(json, readings);
            }
        };

        exec.scheduleAtFixedRate(recentPriceCollection, 0, 1, TimeUnit.SECONDS);
        */
        String json;
        
        json = apiController.getJSONString(apiController.getBCH_TRADES());
        ArrayList<ExchangeRate> bch = calculateRecentAverages(json);

        /*json = apiController.getJSONString(apiController.getBTC_TRADES());
        ArrayList<ExchangeRate> btc = calculateRecentAverages(json);

        json = apiController.getJSONString(apiController.getETH_TRADES());
        ArrayList<ExchangeRate> eth = calculateRecentAverages(json);

        json = apiController.getJSONString(apiController.getLTC_TRADES());
        ArrayList<ExchangeRate> ltc = calculateRecentAverages(json);

        */
    }
    
    private ArrayList<ExchangeRate> calculateRecentAverages(String json) {
        ArrayList<ExchangeRate> avgPrices = new ArrayList<>();
        Double avgPrice = 0.0;
        int tradeNo = 0;
        Object[] trades = parser.fromJSON(json);
        GDAXTrade oldestTrade;
        String oldestTime;
        int oldestMins;
        int oldestHours;
        LocalDateTime tradeTime;
        
        while (avgPrices.size() < readings){
            //The oldest trade will probably be spread from this page to the next,
            //so taking its time allows the prices to be merged
            oldestTrade = ((GDAXTrade)trades[trades.length - 1]);
            oldestTime = oldestTrade.getTime().substring(0, Math.min(oldestTrade.getTime().length(), 16));
            oldestMins = Integer.parseInt(oldestTime.substring(oldestTime.length() - 2, oldestTime.length()));
            oldestHours = Integer.parseInt(oldestTime.substring(oldestTime.length() - 5, oldestTime.length() - 3));
            System.out.println(oldestMins);
            System.out.println(oldestHours);

            tradeTime = LocalDateTime.now().minusMinutes(1);
            
            //Should work to here
            while (oldestMins < tradeTime.getMinute() || oldestHours < tradeTime.getHour()){
                
                for (Object trade : trades){
                    if (helper.stringsMatch(((GDAXTrade)trade).getTime(), tradeTime.toString(), 16)) {
                    avgPrice += Double.parseDouble(((GDAXTrade)trade).getPrice());
                    tradeNo += 1;
                    }
                    avgPrice /= tradeNo;
                }
                tradeTime = tradeTime.minusMinutes(1);
            }

            //add to arrayslist + make new endpoint request
            
            trades = parser.fromJSON(json);
        }
        return avgPrices;
    }
    
    private void initCollector(){
        Runnable automatedCollection = new Runnable() {
            @Override
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                Currency[] currencies = get(LocalDateTime.now().minusSeconds(LocalDateTime.now().getSecond()).minusNanos(LocalDateTime.now().getNano()));
                System.out.println(currencies[0].getId() + " " + currencies[0].getName() + " " + currencies[0].getValue());
                System.out.println(currencies[1].getId() + " " + currencies[1].getName() + " " + currencies[1].getValue());
                System.out.println(currencies[2].getId() + " " + currencies[2].getName() + " " + currencies[2].getValue());
                System.out.println(currencies[3].getId() + " " + currencies[3].getName() + " " + currencies[3].getValue());
            }
        };

        exec.scheduleAtFixedRate(automatedCollection, (60 - LocalDateTime.now().getSecond()), 60, TimeUnit.SECONDS);
    }
    
    public Currency[] get(LocalDateTime postTime){
        System.out.println("[INFO] Fetching resource from: " + apiController.getGDAX_ENDPOINT() + "products/.../trades");
        try {
            ExchangeRate rate;
            String json = apiController.getJSONString(apiController.getBCH_TRADES());
            Double avgPrice = calculateAveragePrice(json, postTime);
            rate = new ExchangeRate(postTime, avgPrice.toString());
            currencies[0].setValue(rate);
            
            json = apiController.getJSONString(apiController.getBTC_TRADES());
            avgPrice = calculateAveragePrice(json, postTime);
            rate = new ExchangeRate(postTime, avgPrice.toString());
            currencies[1].setValue(rate);
            
            json = apiController.getJSONString(apiController.getETH_TRADES());
            avgPrice = calculateAveragePrice(json, postTime);
            rate = new ExchangeRate(postTime, avgPrice.toString());
            currencies[2].setValue(rate);
            
            json = apiController.getJSONString(apiController.getLTC_TRADES());
            avgPrice = calculateAveragePrice(json, postTime);
            rate = new ExchangeRate(postTime, avgPrice.toString());
            currencies[3].setValue(rate);
        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }
        return currencies;
    }
    
    private Double calculateAveragePrice(String json, LocalDateTime postTime) {
        Object[] trades = parser.fromJSON(json);
        Double avgPrice = 0.0;
        int tradeNo = 0;

        for (Object trade : trades){
            if (helper.stringsMatch(((GDAXTrade)trade).getTime(), postTime.minusMinutes(1).toString(), 16)) {
            avgPrice += Double.parseDouble(((GDAXTrade)trade).getPrice());
            tradeNo += 1;
            }
        }
        System.out.println(avgPrice);
        System.out.println(tradeNo);
        /*
            POST TO API
        */
        
        return (avgPrice /= tradeNo);
    }
}
