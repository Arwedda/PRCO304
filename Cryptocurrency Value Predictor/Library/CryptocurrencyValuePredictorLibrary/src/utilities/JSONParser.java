/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.regex.*;
import model.GDAXTrade;

/**
 *
 * @author jkell
 */
public class JSONParser {
    
    public JSONParser() {
    }
    
    public Object[] fromJSON(String json){
        Object[] array;
        
        //GDAXTrade parser
        GDAXTrade trade;
        //String full = "time\\\":\\\"[0-9]{4}-[0-1][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,3}Z\\\",\\\"trade_id\\\":[0-9]+,\\\"price\\\":\\\"[0-9]+.[0-9]+\\\",\\\"size\\\":\\\"[0-9]+.[0-9]+\\\",\\\"side\\\":\\\"(sell|buy)";
        String datetimeRegex = "\\d{4}-[0-1]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d";//:[0-5][0-9].[0-9]{1,3}Z";
        String priceRegex = "price\\\":\\\"\\d+.\\d+";
        String tradeIDRegex = "trade_id\\\":\\d+";
        ArrayList<String> arlTimestamps = getMatches(json, datetimeRegex);
        ArrayList<String> arlPrices = getMatches(json, priceRegex);
        ArrayList<String> arlTradeIDs = getMatches(json, tradeIDRegex);
        ArrayList<GDAXTrade> trades = new ArrayList<>();
        double price;
        String tradeID;

        for (String timestamp : arlTimestamps){
            price = Double.parseDouble(arlPrices.get(0).substring(8));
            tradeID = arlTradeIDs.get(0).substring(10);
            trade = new GDAXTrade(Helpers.localDateTimeParser(timestamp), price, tradeID);
            arlPrices.remove(0);
            arlTradeIDs.remove(0);
            trades.add(trade);
        }
        
        array = trades.toArray();
        
        return array;
    }
    
    private ArrayList<String> getMatches(String json, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(json);
        ArrayList<String> arlMatches = new ArrayList<>();
        
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
