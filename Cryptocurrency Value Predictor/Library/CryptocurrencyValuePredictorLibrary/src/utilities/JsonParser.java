/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.regex.*;
import model.Trade;

/**
 *
 * @author jkell
 */
public class JsonParser {
    public JsonParser() {
    }
    
    public Object[] fromJSON(String json){
        Object[] array = new Object[0];
        
        //Trade parser
        Trade trade = new Trade();
        //String full = "time\\\":\\\"[0-9]{4}-[0-1][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,3}Z\\\",\\\"trade_id\\\":[0-9]+,\\\"price\\\":\\\"[0-9]+.[0-9]+\\\",\\\"size\\\":\\\"[0-9]+.[0-9]+\\\",\\\"side\\\":\\\"(sell|buy)";
        String datetime = "[0-9]{4}-[0-1][0-9]-[0-3][0-9]T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,3}Z";
        String price = "price\\\":\\\"[0-9]+.[0-9]+";
        ArrayList<String> arlTimestamps = getMatches(json, datetime);
        ArrayList<String> arlPrices = getMatches(json, price);
        ArrayList<Trade> trades = new ArrayList<>();
        
        System.out.println(arlTimestamps.get(0) + " " + arlPrices.get(0));
        
        for (String timestamp : arlTimestamps){
            trade = new Trade(timestamp, arlPrices.get(0));
            arlPrices.remove(0);
            trades.add(trade);
            
            System.out.println(trade.getTime() + " " + trade.getPrice());
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
