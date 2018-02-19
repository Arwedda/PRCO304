/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import helpers.LocalDateTimeHelper;
import helpers.SafeCastHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import model.Currency;
import model.ExchangeRate;
import model.GDAXTrade;

/**
 *
 * @author jkell
 */
public class JSONParser {
    
    public JSONParser() {
    }
    
    public GDAXTrade[] GDAXTradeFromJSON(String json){
        GDAXTrade[] array;
        
        GDAXTrade trade;
        //String full = "time\\\":\\\"[0-9]{4}-[0-1][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,3}Z\\\",\\\"trade_id\\\":[0-9]+,\\\"price\\\":\\\"[0-9]+.[0-9]+\\\",\\\"size\\\":\\\"[0-9]+.[0-9]+\\\",\\\"side\\\":\\\"(sell|buy)";
        String datetimeRegex = "\\d{4}-[0-1]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d";//:[0-5][0-9].[0-9]{1,3}Z";
        String priceRegex = "price\\\":\\\"\\d+.\\d+";
        String tradeIDRegex = "trade_id\\\":\\d+";
        List<String> arlTimestamps = getMatches(json, datetimeRegex);
        List<String> arlPrices = getMatches(json, priceRegex);
        List<String> arlTradeIDs = getMatches(json, tradeIDRegex);
        List<GDAXTrade> trades = new ArrayList<>();
        double price;
        String tradeID;

        for (String timestamp : arlTimestamps){
            price = Double.parseDouble(arlPrices.get(0).substring(8));
            tradeID = arlTradeIDs.get(0).substring(10);
            trade = new GDAXTrade(LocalDateTimeHelper.localDateTimeParser(timestamp), price, tradeID);
            arlPrices.remove(0);
            arlTradeIDs.remove(0);
            trades.add(trade);
        }
        
        array = SafeCastHelper.objectsToGDAXTrades(trades.toArray());
        return array;
    }
    
    public Currency[] CurrencyFromJSON(String json){
        Currency[] array;
        Currency currency;
        List<Currency> currencies = new ArrayList<>();
        
        
        
        array = SafeCastHelper.objectsToCurrencies(currencies.toArray());
        return array;
    }
    
    public ExchangeRate[] ExchangeRateFromJSON(String json){
        ExchangeRate[] array;
        ExchangeRate rate;
        List<ExchangeRate> rates = new ArrayList<>();
        
        
        
        array = SafeCastHelper.objectsToExchangeRates(rates.toArray());
        return array;
    }
    
    private List<String> getMatches(String json, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(json);
        List<String> arlMatches = new ArrayList<>();
        
        while (match.find()){
            arlMatches.add(match.group());
        }
        
        return arlMatches;
    }
    
    public String toJSON(Object obj){
        String json = "";
        
        json = obj.toString();
        
        return json;
    }
}
