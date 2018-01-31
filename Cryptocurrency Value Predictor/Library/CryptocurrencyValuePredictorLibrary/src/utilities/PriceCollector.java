/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import controllers.APIController;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Currency;
import model.Trade;


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


    public PriceCollector() {
        System.out.println("[INFO] Fetching resource from: " + API_ENDPOINT + "currencies/");
        try {
            setUpCurrencies();
            getRecentPrices();
            initCollector();
        } catch (Exception e){
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
    
    private void getRecentPrices(){
        int maxReadings = 99;
        String json = apiController.getJSONString(GDAX_ENDPOINT + BCH_TRADES);
        Object[] bch = parser.fromJSON(json);
        json = apiController.getJSONString(GDAX_ENDPOINT + BTC_TRADES);
        Object[] btc = parser.fromJSON(json);
        json = apiController.getJSONString(GDAX_ENDPOINT + ETH_TRADES);
        Object[] eth = parser.fromJSON(json);
        json = apiController.getJSONString(GDAX_ENDPOINT + LTC_TRADES);
        Object[] ltc = parser.fromJSON(json);

        maxReadings = calculateRecentAverages(bch, maxReadings);
        maxReadings = calculateRecentAverages(btc, maxReadings);
        maxReadings = calculateRecentAverages(eth, maxReadings);
        calculateRecentAverages(ltc, maxReadings);
    }
    
    private int calculateRecentAverages(Object[] trades, int maxReadings) {
        String oldest = ((Trade)trades[99]).getTime();
        oldest = oldest.substring(0, Math.min(oldest.length(), 16));
        Double avgPrice = 0.0;
        int tradeNo = 0;
        
        int oldestMins = Integer.parseInt(oldest.substring(oldest.length() - 2, oldest.length()));
        System.out.println(oldestMins);
        
        oldestMins += 1;
        
        LocalDateTime tradeTime = LocalDateTime.now();
        tradeTime.minusMinutes(tradeTime.getMinute() - oldestMins);
        
        System.out.println(tradeTime);
        
        for (Object trade : trades){
            if (helper.stringsMatch(((Trade)trade).getTime(), tradeTime.minusMinutes(1).toString(), 16)) {
            avgPrice += Double.parseDouble(((Trade)trade).getPrice());
            tradeNo += 1;
            }
        }
        
        //create averages for each minute, pass back # of minutes, 
        
        return 1;
    }
    
    private void initCollector(){
        Runnable automatedCollection = new Runnable() {
            public void run() {
                priceCollection();
            }

            private void priceCollection() {
                Currency[] currencies = get(LocalDateTime.now());
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
            String json = apiController.getJSONString(GDAX_ENDPOINT + BCH_TRADES);
            Double avgPrice = calculateAveragePrice(json, postTime);
            currencies[0].setValue(avgPrice.toString());
            
            json = apiController.getJSONString(GDAX_ENDPOINT + BTC_TRADES);
            avgPrice = calculateAveragePrice(json, postTime);
            currencies[1].setValue(avgPrice.toString());
            
            json = apiController.getJSONString(GDAX_ENDPOINT + ETH_TRADES);
            avgPrice = calculateAveragePrice(json, postTime);
            currencies[2].setValue(avgPrice.toString());

            json = apiController.getJSONString(GDAX_ENDPOINT + LTC_TRADES);
            avgPrice = calculateAveragePrice(json, postTime);
            currencies[3].setValue(avgPrice.toString());
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
            if (helper.stringsMatch(((Trade)trade).getTime(), postTime.minusMinutes(1).toString(), 16)) {
            avgPrice += Double.parseDouble(((Trade)trade).getPrice());
            tradeNo += 1;
            }
        }
        
        /*
            POST TO API
        */
        
        return (avgPrice /= tradeNo);
    }
    
    public void post() {
        System.out.println("[INFO] Posting to resource: " + API_ENDPOINT + "ExchangeRate/");
    }
}
