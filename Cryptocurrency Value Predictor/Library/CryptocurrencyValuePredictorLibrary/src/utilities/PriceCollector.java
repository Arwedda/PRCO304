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
    private static final String GDAX_ENDPOINT = "https://api.gdax.com/";
    private static final String API_ENDPOINT = "http://xserve.uopnet.plymouth.ac.uk/modules/intproj/PRCS251A/api/";
    private static final String BCH_TRADES = "products/BCH-USD/trades/";
    private static final String BTC_TRADES = "products/BTC-USD/trades/";
    private static final String ETH_TRADES = "products/ETH-USD/trades/";
    private static final String LTC_TRADES = "products/LTC-USD/trades/";
    private Currency[] currencies = new Currency[4];
    private JsonParser parser = new JsonParser();
    private Helpers helper = new Helpers();
    private ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    private WebsocketListener wsListener;

    public PriceCollector() {
        System.out.println("[INFO] Fetching resource from: " + API_ENDPOINT + "currencies/");
        try {
            initWebsocketListener();
            setUpCurrencies();
            getRecentPrices(100);
            initCollector();
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

        Currency bch = new Currency("BCH", "Bitcoin Cash");
        Currency btc = new Currency("BTC", "Bitcoin");
        Currency eth = new Currency("ETH", "Ethereum");
        Currency ltc = new Currency("LTC", "Litecoin");

        currencies[0] = bch;
        currencies[1] = btc;
        currencies[2] = eth;
        currencies[3] = ltc;
    }
    
    private void getRecentPrices(int readings){        
        String json = apiController.getJSONString(GDAX_ENDPOINT + BCH_TRADES);
        calculateRecentAverages(json, readings);
        json = apiController.getJSONString(GDAX_ENDPOINT + BTC_TRADES);
        calculateRecentAverages(json, readings);
        json = apiController.getJSONString(GDAX_ENDPOINT + ETH_TRADES);
        calculateRecentAverages(json, readings);
        json = apiController.getJSONString(GDAX_ENDPOINT + LTC_TRADES);
        calculateRecentAverages(json, readings);
    }
    
    private int calculateRecentAverages(String json, int readings) {
        /*Double avgPrice = 0.0;
        int tradeNo = 0;
        Object[] trades = parser.fromJSON(json);
        ArrayList<ExchangeRate> avgPrices = new ArrayList<>();
        
        for (int i = 0; i < readings; i++){
            trades = parser.fromJSON(json);

            GDAXTrade oldest = ((GDAXTrade)trades[99]);
            oldest = oldest.substring(0, Math.min(oldest.length(), 16));

            int oldestMins = Integer.parseInt(oldest.substring(oldest.length() - 2, oldest.length()));
            System.out.println(oldestMins);

            oldestMins += 1;

            LocalDateTime tradeTime = LocalDateTime.now();
            tradeTime.minusMinutes(tradeTime.getMinute() - oldestMins);

            System.out.println(tradeTime);

            for (Object trade : trades){
                if (helper.stringsMatch(((GDAXTrade)trade).getTime(), tradeTime.minusMinutes(1).toString(), 16)) {
                avgPrice += Double.parseDouble(((GDAXTrade)trade).getPrice());
                tradeNo += 1;
                }
            }

            //create averages for each minute, pass back # of minutes, 
        }*/
        return 1;
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
        System.out.println("[INFO] Fetching resource from: " + GDAX_ENDPOINT + "products/.../trades");
        try {
            ExchangeRate rate;
            String json = apiController.getJSONString(GDAX_ENDPOINT + BCH_TRADES);
            Double avgPrice = calculateAveragePrice(json, postTime);
            rate = new ExchangeRate(postTime, avgPrice.toString());
            currencies[0].setValue(rate);
            
            json = apiController.getJSONString(GDAX_ENDPOINT + BTC_TRADES);
            avgPrice = calculateAveragePrice(json, postTime);
            rate = new ExchangeRate(postTime, avgPrice.toString());
            currencies[1].setValue(rate);
            
            json = apiController.getJSONString(GDAX_ENDPOINT + ETH_TRADES);
            avgPrice = calculateAveragePrice(json, postTime);
            rate = new ExchangeRate(postTime, avgPrice.toString());
            currencies[2].setValue(rate);
            
            json = apiController.getJSONString(GDAX_ENDPOINT + LTC_TRADES);
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
