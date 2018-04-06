/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import java.net.URLEncoder;

/**
 *
 * @author jkell
 */
public class ExchangeRateAPIController extends APIController {
    
    /**
     * Uses APIController to get a JSON string from the URL and then Gson to 
     * parse the JSON into an array of ExchangeRate objects.
     * @param url String form of the URL to collect data from.
     * @return Array of ExchangeRate objects parsed from received JSON.
     */
    public ExchangeRate[] getExchangeRates(String url){
        ExchangeRate[] rates;
        String json = get(url);
        rates = gson.fromJson(json, ExchangeRate[].class);
        return rates;
    }
    
    /**
     * Parses a single ExchangeRate into a JSON string and then posts it to the 
     * specified URL string.
     * @param endpoint String form of the URL endpoint to post data to.
     * @param rate ExchangeRate to convert to a JSON string to post to the URL.
     */
    public void post(String endpoint, ExchangeRate rate){
        String json = gson.toJson(rate, ExchangeRate.class);
        post(endpoint, json);
    }
    
    /**
     * Parses an array of ExchangeRates into a JSON strings and then posts them
     * to the specified URL string one by one.
     * @param endpoint String form of the URL endpoint to post data to.
     * @param rates ExchangeRate array to convert to JSON strings to post to the URL.
     */
    public void post(String endpoint, ExchangeRate[] rates){
        //String json = gson.toJson(rates, ExchangeRate[].class);
        String json;
        if (0 < rates.length){
            for (ExchangeRate rate : rates){
                json = gson.toJson(rate, ExchangeRate.class);
                post(endpoint, json);
            }
            System.out.println("[INFO] Posted prices for " + rates[0].getCurrency_id());
        }
    }
    
    /**
     * Parses a single ExchangeRate into a JSON string and then puts it to the 
     * specified URL string.
     * @param endpoint String form of the URL endpoint to put data to.
     * @param rate ExchangeRate to convert to a JSON string to put to the URL.
     */
    public void put(String endpoint, ExchangeRate rate) {
        try {
            String json = gson.toJson(rate, ExchangeRate.class);
            String query = "/" + rate.getCurrency_id() + "/" + rate.getTimestamp();
            put(endpoint + URLEncoder.encode(query, "UTF-8"), json);
        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }
    }
    
    /**
     * Parses an array of ExchangeRates into a JSON strings and then puts them
     * to the specified URL string one by one.
     * @param endpoint String form of the URL endpoint to put data to.
     * @param rates ExchangeRate array to convert to JSON strings to put to the URL.
     */
    public void put(String endpoint, ExchangeRate[] rates) {
        if (0 < rates.length){
            String json;
            String query;
            System.out.println("[INFO] Starting PUT for " + rates[0].getCurrency_id());
            for (ExchangeRate rate : rates){
                try {
                    json = gson.toJson(rate, ExchangeRate.class);
                    query = "/" + rate.getCurrency_id() + "/" + rate.getTimestamp();
                    put(endpoint + URLEncoder.encode(query, "UTF-8"), json);
                } catch (Exception e) {
                    System.out.println("[INFO] Error: " + e);
                }
            }
            System.out.println("[INFO] Finished PUT for " + rates[0].getCurrency_id());
        }
    }
}
