/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.valuepredictor;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.PriceCollector;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.PricePredictor;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.Trader;



/**
 *
 * @author jkell
 */
public class CryptocurrencyValuePredictor {
    private static CryptocurrencyValuePredictor instance;
    private static Currency[] currencies;
    private PriceCollector priceCollector;
    private Trader trader;
    
    /**
     * Default constructor for CryptocurrencyValuePredictor
     */
    protected CryptocurrencyValuePredictor(){
        priceCollector = new PriceCollector();
        trader = new Trader();
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
    
    public static void pricesCollected(Currency[] currencies){
        CryptocurrencyValuePredictor.currencies = currencies;
        PricePredictor.makePredictions(currencies);
        System.out.println("stop here..");
    }
}
