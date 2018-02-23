/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.SafeCastHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.GDAXTrade;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;


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
        double price;
        Integer tradeID;
        List<GDAXTrade> trades = new ArrayList<>();
        String datetimeRegex = "\\d{4}-[0-1]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d";//:[0-5][0-9].[0-9]{1,3}Z";
        String priceRegex = "price\\\":\\\"\\d+.\\d+";
        String tradeIDRegex = "trade_id\\\":\\d+";
        List<String> timestamps = getMatches(json, datetimeRegex);
        List<String> prices = getMatches(json, priceRegex);
        List<String> tradeIDs = getMatches(json, tradeIDRegex);

        for (String timestamp : timestamps){
            price = Double.parseDouble(prices.get(0).substring(8));
            tradeID = Integer.parseInt(tradeIDs.get(0).substring(10));
            trade = new GDAXTrade(LocalDateTimeHelper.localDateTimeParser(timestamp), price, tradeID);
            prices.remove(0);
            tradeIDs.remove(0);
            trades.add(trade);
        }
        
        array = SafeCastHelper.objectsToGDAXTrades(trades.toArray());
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
    
    
    /*
    public Currency[] currencyFromJSON(String json){
        Currency[] array;
        Currency currency;
        String currencyName, GDAXEndpoint;
        List<Currency> currencies = new ArrayList<>();
        String currencyIDRegex = "CURRENCY_ID\\\":\\\"\\w+";
        String currencyNameRegex = "CURRENCY_NAME\\\":\\\"([\\w]+ ?)+";
        String GDAXEndpointRegex = "https:\\/\\/api.gdax.com\\/products\\/\\w+-USD\\/trades";
        List<String> currencyIDs = getMatches(json, currencyIDRegex);
        List<String> currencyNames = getMatches(json, currencyNameRegex);
        List<String> GDAXEndpoints = getMatches(json, GDAXEndpointRegex);
        
        for (String currencyID : currencyIDs){
            currencyName = currencyNames.get(0).substring(16);
            GDAXEndpoint = GDAXEndpoints.get(0);
            currency = new Currency(currencyID.substring(14), currencyName, GDAXEndpoint);
            currencyNames.remove(0);
            GDAXEndpoints.remove(0);
            currencies.add(currency);
        }
        
        array = SafeCastHelper.objectsToCurrencies(currencies.toArray());
        return array;
    }
    
    public ExchangeRate[] exchangeRateFromJSON(String json){
        ExchangeRate[] array;
        ExchangeRate rate;
        List<ExchangeRate> rates = new ArrayList<>();
        
        
        
        array = SafeCastHelper.objectsToExchangeRates(rates.toArray());
        return array;
    }
    
    public String currentRateToJSON(Currency currency){
        ExchangeRate rate = currency.getRate();
        String currencyID = currency.getID();
        LocalDateTime dateTime = rate.getTimestamp();
        Double dollarValue = rate.getValue();
        Double growth = rate.getGrowth();
        Double GOFAINextGrowth = null;
        Double neuralNetworkNextGrowth = null;
        String lastGDAXTrade = rate.getLastTrade();
        String json = "{\"CURRENCY_ID\":\"" + currencyID + 
                "\", \"DATETIME\": \"" + dateTime.toString() + 
                "\", \"DOLLAR_VALUE\": " + dollarValue + 
                ", \"GROWTH\\\": " + growth + 
                ", \"GOFAI_NEXT_GROWTH\": " + GOFAINextGrowth + 
                ", \"NEURALNETWORK_NEXT_GROWTH\": " + neuralNetworkNextGrowth + 
                ", \"LAST_GDAXTRADE\": \"" + lastGDAXTrade + "\\\"}";
        return json;
    }*/
}
