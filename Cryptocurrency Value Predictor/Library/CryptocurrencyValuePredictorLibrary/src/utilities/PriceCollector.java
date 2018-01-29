/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import controllers.APIController;
import model.Currency;
import model.Trade;

/**
 *
 * @author jkell
 */
public class PriceCollector {
    private APIController apiController = new APIController();
    private static String GDAX_ENDPOINT = "https://api.gdax.com/";
    private static String API_ENDPOINT = "http://xserve.uopnet.plymouth.ac.uk/modules/intproj/PRCS251A/api/";
    private static String BCH_TRADES = "products/BCH-USD/trades/";
    private static String BTC_TRADES = "products/BTC-USD/trades/";
    private static String ETH_TRADES = "products/ETH-USD/trades/";
    private static String LTC_TRADES = "products/LTC-USD/trades/";
    private Currency[] currencies = new Currency[3];
    public Currency[] currentPrices;

    public PriceCollector() {
        System.out.println("[INFO] Fetching resource from: " + API_ENDPOINT + "currencies/");
        try {
            /*  Commented out because endpoint not up yet.
                It will cycle through each currency in the database and create
                an array to get prices for.
            
            URL url = new URL(API_ENDPOINT + "currencies/");
            String json = url.openStream().toString();
            Currency currencies = gson.fromJson(json, Currency.class);
            */
            
            Currency bch = new Currency("BCH", "Bitcoin Cash");
            Currency btc = new Currency("BTC", "Bitcoin");
            Currency eth = new Currency("ETH", "Ethereum");
            Currency ltc = new Currency("LTC", "Litecoin");
            
            currencies[0] = bch;
            currencies[1] = btc;
            currencies[2] = eth;
            currencies[3] = ltc;
            
        } catch (Exception e){
            System.out.println("[INFO] Error: " + e.getMessage());
        }
    }
    
    public Currency[] Get(){
        System.out.println("[INFO] Fetching resource from: " + GDAX_ENDPOINT + "products/BTC-USD/trades/");
        Trade[] bchTrades = new Trade[0];
        Trade[] btcTrades = new Trade[0];
        Trade[] ethTrades = new Trade[0];
        Trade[] ltcTrades = new Trade[0];
        Double avgPrice = 0.0;
        String json;
        try {
            json = apiController.getJSONString(GDAX_ENDPOINT + BCH_TRADES);
            //PARSE JSON INTO TRADES
            
            for (Trade trade : bchTrades){
                avgPrice += Double.parseDouble(trade.getPrice());
            }
            avgPrice /= bchTrades.length;

            
            
            json = apiController.getJSONString(GDAX_ENDPOINT + BTC_TRADES);
            //PARSE JSON INTO TRADES
            
            for (Trade trade : btcTrades){
                avgPrice += Double.parseDouble(trade.getPrice());
            }
            avgPrice /= btcTrades.length;
            
            
            
            json = apiController.getJSONString(GDAX_ENDPOINT + ETH_TRADES);
            //PARSE JSON INTO TRADES
            
            for (Trade trade : ethTrades){
                avgPrice += Double.parseDouble(trade.getPrice());
            }
            avgPrice /= ethTrades.length;
            
            
            
            json = apiController.getJSONString(GDAX_ENDPOINT + LTC_TRADES);
            //PARSE JSON INTO TRADES
            
            for (Trade trade : ltcTrades){
                avgPrice += Double.parseDouble(trade.getPrice());
            }
            avgPrice /= ltcTrades.length;
            
            
            
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return currentPrices;
    }
}
