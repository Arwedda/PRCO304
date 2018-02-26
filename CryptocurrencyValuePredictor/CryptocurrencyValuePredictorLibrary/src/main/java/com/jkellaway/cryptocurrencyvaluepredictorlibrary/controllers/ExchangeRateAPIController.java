/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;

/**
 *
 * @author jkell
 */
public class ExchangeRateAPIController extends APIController {
    public ExchangeRate[] getExchangeRates(String url){
        ExchangeRate[] rates = new ExchangeRate[0];
        String json = get(url);
        rates = gson.fromJson(json, ExchangeRate[].class);
        return rates;
    }
    
    public void post(String endpoint, ExchangeRate rate){
        String json = gson.toJson(rate, ExchangeRate.class);
        post(endpoint, json);
    }
    
    public void post(String endpoint, ExchangeRate[] rates){
        //String json = gson.toJson(rates, ExchangeRate[].class);
        String json;
        for (ExchangeRate rate : rates){
            json = gson.toJson(rate, ExchangeRate.class);
            post(endpoint, json);
        }
    }
    
    public void put(String endpoint, ExchangeRate rate){
        String json = gson.toJson(rate, ExchangeRate.class);
        post(endpoint, json);
    }
    
    public void put(String endpoint, ExchangeRate[] rates){
        String json;
        for (ExchangeRate rate : rates){ 
            json = gson.toJson(rate, ExchangeRate.class);
            put(endpoint, json);
        }
    }
}
