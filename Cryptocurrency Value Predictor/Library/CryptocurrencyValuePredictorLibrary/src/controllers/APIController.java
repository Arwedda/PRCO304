/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 *
 * @author jkell
 */
public class APIController {
    private final String GDAX_ENDPOINT = "https://api.gdax.com";
    private final String BCH_TRADES = "/products/BCH-USD/trades";
    private final String BTC_TRADES = "/products/BTC-USD/trades";
    private final String ETH_TRADES = "/products/ETH-USD/trades";
    private final String LTC_TRADES = "/products/LTC-USD/trades";
//    private static final String API_ENDPOINT = "http://xserve.uopnet.plymouth.ac.uk/modules/intproj/PRCS251A/api";
    
    public APIController() {
    }

    public String getGDAX_ENDPOINT() {
        return GDAX_ENDPOINT;
    }

    public String getBCH_TRADES() {
        return GDAX_ENDPOINT + BCH_TRADES;
    }

    public String getBTC_TRADES() {
        return GDAX_ENDPOINT + BTC_TRADES;
    }

    public String getETH_TRADES() {
        return GDAX_ENDPOINT + ETH_TRADES;
    }

    public String getLTC_TRADES() {
        return GDAX_ENDPOINT + LTC_TRADES;
    }
    
    public String getJSONString(String url){
        String json = "";
        try {
            try (InputStream is = new URL(url).openStream();
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))){
                        json = br.lines().collect(
                            Collectors.joining(System.lineSeparator()));
                }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return json;
    }
    
    public void Post(String url){
        
    }
    
    public void Put(String url){
        
    }
    
    public void Delete(String url){
        
    }
}
