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
    private static final String GDAX_ENDPOINT = "https://api.gdax.com/";
    private static final String API_ENDPOINT = "http://xserve.uopnet.plymouth.ac.uk/modules/intproj/PRCS251A/api/";
    private static final String BCH_TRADES = "products/BCH-USD/trades/";
    private static final String BTC_TRADES = "products/BTC-USD/trades/";
    private static final String ETH_TRADES = "products/ETH-USD/trades/";
    private static final String LTC_TRADES = "products/LTC-USD/trades/";
    private Currency[] currencies = new Currency[3];
    private JsonParser parser = new JsonParser();

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
        System.out.println("[INFO] Fetching resource from: " + GDAX_ENDPOINT + "products/.../trades");
        try {
            String json = apiController.getJSONString(GDAX_ENDPOINT + BCH_TRADES);
            Double avgPrice = calculateAveragePrice(json);
            currencies[0].setValue(avgPrice.toString());
            
            json = apiController.getJSONString(GDAX_ENDPOINT + BTC_TRADES);
            avgPrice = calculateAveragePrice(json);
            currencies[1].setValue(avgPrice.toString());
            
            json = apiController.getJSONString(GDAX_ENDPOINT + ETH_TRADES);
            avgPrice = calculateAveragePrice(json);
            currencies[2].setValue(avgPrice.toString());

            json = apiController.getJSONString(GDAX_ENDPOINT + LTC_TRADES);
            avgPrice = calculateAveragePrice(json);
            currencies[3].setValue(avgPrice.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return currencies;
    }
    
    private Double calculateAveragePrice(String json) {
            Trade[] trades = (Trade[]) parser.fromJSON(json);
            Double avgPrice = 0.0;
            
            for (Trade trade : trades){
                avgPrice += Double.parseDouble(trade.getPrice());
            }
            return (avgPrice /= trades.length);
    }
    
    public void Post() {
        System.out.println();
    }
}
