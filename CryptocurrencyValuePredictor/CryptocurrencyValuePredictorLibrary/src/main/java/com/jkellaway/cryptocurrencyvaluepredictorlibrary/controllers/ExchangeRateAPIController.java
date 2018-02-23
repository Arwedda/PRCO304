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
    public void postExchangeRate(String endpoint, ExchangeRate rate){
        String json = gson.toJson(rate, ExchangeRate.class);
        post(endpoint, json);
    }
}
