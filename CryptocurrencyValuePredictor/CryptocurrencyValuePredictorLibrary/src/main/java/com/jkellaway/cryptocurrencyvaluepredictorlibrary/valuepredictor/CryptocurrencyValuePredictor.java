/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.valuepredictor;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.IObserver;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.ISubject;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.PriceCollector;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.PricePredictor;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.Trader;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author jkell
 */
public class CryptocurrencyValuePredictor implements IObserver, ISubject {
    private static CryptocurrencyValuePredictor instance;
    private transient List<IObserver> observers;
    private Currency[] currencies;
    private PriceCollector priceCollector;
    private Trader trader;
    private Trader best;
    private Trader worst;
    private Trader[] holders = new Trader[4];
    private Trader[] GOFAITradersUSD = new Trader[20];
    private Trader[] GOFAITradersHold = new Trader[20];
    
    /**
     * Default constructor for CryptocurrencyValuePredictor
     */
    protected CryptocurrencyValuePredictor(){
        priceCollector = new PriceCollector();
        trader = new Trader();
        observers = new ArrayList<>();
        holders[0] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, "Manual", "BCH");
        holders[1] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, "Manual", "BTC");
        holders[2] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, "Manual", "ETH");
        holders[3] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, "Manual", "LTC");
        best = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, "Manual", "BEST");
        worst = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, "Manual", "WORST");
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++){
            GOFAITradersUSD[i] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, "GOFAI", "USD");
        }
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
        notifyObservers();
    }

    public PriceCollector getPriceCollector() {
        return priceCollector;
    }

    public Trader getTrader() {
        return trader;
    }
    
    public void pricesCollected(Currency[] currencies){
        setCurrencies(currencies);
        PricePredictor.makePredictions(currencies);
        tradeTest();
        System.out.println("stop here..");
    }
    
    private void tradeTest(){
        best.tradeTest(currencies, Globals.NUMBEROFPREDICTIONS, 0);
        worst.tradeTest(currencies, Globals.NUMBEROFPREDICTIONS, 0);
        for (Trader holder : holders) {
            holder.tradeTest(currencies, Globals.NUMBEROFPREDICTIONS, 0);
        }
        notifyObservers();
        for (int i = 0; i < GOFAITradersUSD.length; i++){
            GOFAITradersUSD[i].tradeTest(currencies, Globals.NUMBEROFPREDICTIONS, i);
            GOFAITradersHold[i].tradeTest(currencies, Globals.NUMBEROFPREDICTIONS, i);
        }
        notifyObservers();
    }

    public Trader getBest() {
        return best;
    }

    public Trader getWorst() {
        return worst;
    }

    public Trader[] getHolders() {
        return holders;
    }

    public Trader[] getGOFAITradersUSD() {
        return GOFAITradersUSD;
    }

    public Trader[] getGOFAITradersHold() {
        return GOFAITradersHold;
    }

    @Override
    public Boolean registerObserver(IObserver o) {
        Boolean blnAdded = false;
        if (o != null) {
            if (this.observers == null) {
                this.observers = new ArrayList<>();
            }
            if (!this.observers.contains(o)) {
                blnAdded = this.observers.add(o);
            }
        }
        return blnAdded;
    }

    @Override
    public Boolean removeObserver(IObserver o) {
        Boolean blnRemoved = false;
        if (o != null) {
            if (this.observers != null && 0 < this.observers.size()) {
                blnRemoved = this.observers.remove(o);
            }
        }
        return blnRemoved;
    }

    @Override
    public void notifyObservers() {
        if (this.observers != null && 0 < this.observers.size()) {
            for (IObserver currentObserver : this.observers) {
                currentObserver.update();
            }
        }    
    }

    @Override
    public void update() {
        //Nothing to update here.
    }
}
