/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.GDAXTrade;



/**
 *
 * @author jkell
 */
public class SafeCastHelper {
    public static Currency[] objectsToCurrencies(Object[] objs){
        Currency[] currencies = new Currency[objs.length];
        Currency currency;
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] instanceof Currency){
                currency = new Currency(((Currency) objs[i]).getID(), ((Currency) objs[i]).getName(), ((Currency) objs[i]).getGDAXEndpoint());
                currencies[i] = currency;
            }
        }
        return currencies;
    }
    
    public static ExchangeRate[] objectsToExchangeRates(Object[] objs){
        ExchangeRate[] exchangeRates = new ExchangeRate[objs.length];
        ExchangeRate rate;
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] instanceof ExchangeRate){
                rate = new ExchangeRate(((ExchangeRate) objs[i]).getTimestamp(), ((ExchangeRate) objs[i]).getValue(), ((ExchangeRate) objs[i]).getLastTrade(), ((ExchangeRate) objs[i]).getGrowth());
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
    
    public static Double[] objectsToDoubles(Object[] objs){
        Double[] doubles = new Double[objs.length];
        Double dub;
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] instanceof Double){
                dub = (Double) objs[i];
                doubles[i] = dub;
            }
        }
        return doubles;
    }
}
