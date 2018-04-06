/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;

/**
 *
 * @author jkell
 */
public class CurrencyAPIController extends APIController {
    /**
     * Uses APIController to get a JSON string from the URL and then Gson to 
     * parse the JSON into an array of Currency objects.
     * @param url String form of the URL to collect data from.
     * @return Array of Currency objects parsed from received JSON.
     */
    public Currency[] getCurrencies(String url){
        Currency[] currencies = new Currency[0];
        String json = get(url);
        currencies = gson.fromJson(json, Currency[].class);
        return currencies;
    }
}
