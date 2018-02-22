/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.valuepredictor;

import model.Currency;
import utilities.PriceCollector;

/**
 *
 * @author jkell
 */
public class CryptocurrencyValuePredictor {
    private static CryptocurrencyValuePredictor instance;
    private Currency[] currencies = new Currency[4];
    private PriceCollector priceCollector;
    
    /**
     * Default constructor for CryptocurrencyValuePredictor
     */
    protected CryptocurrencyValuePredictor(){
    }

    /**
     * Gets the active instance of CryptocurrencyValuePredictor. Allows the user
     * to create an instance if one doesn't exist, forcing the instance to 
     * follow the Singleton Design pattern. 
     * @return Single initiation of CryptocurrencyValuePredictor
     */
    public static CryptocurrencyValuePredictor getInstance(){
        if (instance == null){
            instance = new CryptocurrencyValuePredictor();
        }
        return instance;
    }
    
    public void initialise(){
        priceCollector = new PriceCollector();
    }
}
