/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import model.ExchangeRate;
import model.GDAXTrade;

/**
 *
 * @author jkell
 */
public class SafeCastHelper {
    public static ExchangeRate[] objectsToExchangeRates(Object[] objs){
        ExchangeRate[] exchangeRates = new ExchangeRate[objs.length];
        ExchangeRate rate;
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] instanceof ExchangeRate){
                rate = new ExchangeRate(((ExchangeRate) objs[i]).getTimestamp(), ((ExchangeRate) objs[i]).getValue());
                exchangeRates[i] = rate;
            }
        }
        return exchangeRates;
    }
    
    public static GDAXTrade[] objectsToGDAXTrades(Object[] objs){
        GDAXTrade[] trades = new GDAXTrade[objs.length];
        GDAXTrade trade;
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] instanceof GDAXTrade){
                trade = new GDAXTrade(((GDAXTrade) objs[i]).getTime(), ((GDAXTrade) objs[i]).getPrice(),((GDAXTrade)objs[i]).getID());
                trades[i] = trade;
            }
        }
        return trades;
    }
}
