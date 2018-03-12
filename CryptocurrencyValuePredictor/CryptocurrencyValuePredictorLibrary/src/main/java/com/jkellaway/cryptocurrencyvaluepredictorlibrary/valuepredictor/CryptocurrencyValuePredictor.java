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
    private Currency[] currencies;
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

    public Currency[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currency[] currencies) {
        this.currencies = currencies;
    }

    public PriceCollector getPriceCollector() {
        return priceCollector;
    }

    public void setPriceCollector(PriceCollector priceCollector) {
        this.priceCollector = priceCollector;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }
    
    public void pricesCollected(Currency[] currencies){
        setCurrencies(currencies);
        PricePredictor.makePredictions(currencies);
        
        System.out.println("stop here..");
    }
}
