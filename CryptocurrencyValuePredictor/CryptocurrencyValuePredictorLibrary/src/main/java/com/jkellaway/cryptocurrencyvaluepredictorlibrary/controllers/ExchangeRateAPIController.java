/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.StringHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import java.net.URLEncoder;

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
    
    public void put(String endpoint, ExchangeRate[] rates) {
        String json;
        String query;
        System.out.println("[INFO] Starting PUT for " + rates[0].getCurrency_id());
        for (ExchangeRate rate : rates){
            try {
                json = gson.toJson(rate, ExchangeRate.class);
                query = "/" + rate.getCurrency_id() + "/" + rate.getTimestamp();
                put(endpoint + query, json);
            } catch (Exception e) {
                System.out.println("[INFO] Error: " + e);
            }
        }
        System.out.println("[INFO] Finished PUT for " + rates[0].getCurrency_id());
    }
}
