/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

/**
 *
 * @author jkell
 */
public class Globals {
    public static final String API_ENDPOINT = "http://localhost:63783/api";
    public static final String GDAX_ENDPOINT = "https://api.gdax.com";
    public static final String BCH_TRADES = "https://api.gdax.com/products/BCH-USD/trades";
    public static final String BTC_TRADES = "https://api.gdax.com/products/BTC-USD/trades";
    public static final String ETH_TRADES = "https://api.gdax.com/products/ETH-USD/trades";
    public static final String LTC_TRADES = "https://api.gdax.com/products/LTC-USD/trades";
    public static final Double BTC_TAKER_FEE = 0.25;
    public static final Double TAKER_FEE = 0.30;
    
    public static final int NUMBEROFPREDICTIONS = 20;
    public static final int READINGSREQUIRED = 15000 + NUMBEROFPREDICTIONS;
    public static final String STARTINGUNITS = "USD";
    public static final Double STARTINGVALUE = 100.00;
}
