/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.GDAXTrade;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.JSONParser;

/**
 *
 * @author jkell
 */
public class GDAXAPIController extends APIController {
    private JSONParser parser = new JSONParser();

    public GDAXTrade[] getGDAXTrades(String url){
        GDAXTrade[] gDAXTrades;
        String json = get(url);
        gDAXTrades = parser.GDAXTradeFromJSON(json);
        return gDAXTrades;
    }
}
