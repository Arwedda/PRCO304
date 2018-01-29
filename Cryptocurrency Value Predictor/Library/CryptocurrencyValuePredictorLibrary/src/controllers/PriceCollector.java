/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import model.Currency;
import com.google.gson.*;
import model.Trade;

/**
 *
 * @author jkell
 */
public class PriceCollector {
    private static String GDAX_ENDPOINT = "https://api.gdax.com/";
    private static String API_ENDPOINT = "http://xserve.uopnet.plymouth.ac.uk/modules/intproj/PRCS251A/api/";
    private Gson gson = new Gson();
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
        try {
            bchTrades = gson.fromJson(new URL(GDAX_ENDPOINT + "products/BCH-USD/trades/").getContent().toString(), Trade[].class);
            
            for (Trade trade : bchTrades){
                avgPrice += Double.parseDouble(trade.getPrice());
            }
            avgPrice /= bchTrades.length;

            /*btcTrades = gson.fromJson(new URL(GDAX_ENDPOINT + "products/BTC-USD/trades/").getContent().toString(), Trade[].class);
            ethTrades = gson.fromJson(new URL(GDAX_ENDPOINT + "products/ETH-USD/trades/").getContent().toString(), Trade[].class);
            ltcTrades = gson.fromJson(new URL(GDAX_ENDPOINT + "products/LTC-USD/trades/").getContent().toString(), Trade[].class);*/
            
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return currentPrices;
    }
}
