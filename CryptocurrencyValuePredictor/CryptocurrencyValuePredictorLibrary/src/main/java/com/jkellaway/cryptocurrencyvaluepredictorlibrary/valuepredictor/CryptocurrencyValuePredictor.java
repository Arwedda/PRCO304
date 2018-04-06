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
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.GOFAIPredictor;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.HoldMode;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.TradeMode;
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
        priceCollector.registerObserver(this);
        trader = new Trader();
        observers = new ArrayList<>();
        holders[0] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, TradeMode.MANUAL, -1, HoldMode.BCH);
        holders[1] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, TradeMode.MANUAL, -1, HoldMode.BTC);
        holders[2] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, TradeMode.MANUAL, -1, HoldMode.ETH);
        holders[3] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, TradeMode.MANUAL, -1, HoldMode.LTC);
        best = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, TradeMode.MANUAL, -1, HoldMode.BEST);
        worst = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, TradeMode.MANUAL, -1, HoldMode.WORST);
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++){
            GOFAITradersHold[i] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, TradeMode.GOFAI, i, HoldMode.CRYPTOCURRENCY);
            GOFAITradersUSD[i] = new Trader(Globals.STARTINGUNITS, Globals.STARTINGVALUE, TradeMode.GOFAI, i, HoldMode.USD);
        }
        priceCollector.initialise(Globals.READINGSREQUIRED);
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
        return priceCollector.getCurrencies();
    }

    private void setCurrencies(Currency[] currencies) {
        this.priceCollector.setCurrencies(currencies);
    }

    public PriceCollector getPriceCollector() {
        return priceCollector;
    }

    public Trader getTrader() {
        return trader;
    }
    
    private void initialBenchmark(){
        best.tradeBenchmark(getCurrencies(), Globals.READINGSREQUIRED, Globals.NUMBEROFPREDICTIONS + 1);
        worst.tradeBenchmark(getCurrencies(), Globals.READINGSREQUIRED, Globals.NUMBEROFPREDICTIONS + 1);
        for (Trader holder : holders) {
            holder.tradeBenchmark(getCurrencies(), Globals.READINGSREQUIRED, Globals.NUMBEROFPREDICTIONS + 1);
        }
        for (int i = 0; i < GOFAITradersUSD.length; i++){
            GOFAITradersUSD[i].tradeBenchmark(getCurrencies(), Globals.READINGSREQUIRED, Globals.NUMBEROFPREDICTIONS + 1);
            GOFAITradersHold[i].tradeBenchmark(getCurrencies(), Globals.READINGSREQUIRED, Globals.NUMBEROFPREDICTIONS + 1);
        }
    }
    
    private void trade() {
        trader.autoTrade(getCurrencies());
        best.tradeBenchmark(getCurrencies());
        worst.tradeBenchmark(getCurrencies());
        for (Trader holder : holders) {
            holder.tradeBenchmark(getCurrencies());
        }
        for (int i = 0; i < GOFAITradersUSD.length; i++){
            GOFAITradersUSD[i].tradeBenchmark(getCurrencies());
            GOFAITradersHold[i].tradeBenchmark(getCurrencies());
        }
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
    
    public int getLap() {
        return priceCollector.getLap();
    }
    
    public void startTrading(boolean gofai, int tradeModeIndex, Double startValue, boolean holdUSD, String apiKey) {
        TradeMode tradeMode = (gofai ? TradeMode.GOFAI : TradeMode.NEURALNETWORK);
        HoldMode holdMode =  (holdUSD ? HoldMode.USD : HoldMode.CRYPTOCURRENCY);
        System.out.println(apiKey);
        trader = new Trader("USD", startValue, tradeMode, tradeModeIndex, holdMode);
    }
    
    public void stopTrading() {
        trader.setTradeMode(TradeMode.MANUAL);
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
        if (priceCollector.getLap() == 3) {
            setCurrencies(GOFAIPredictor.initialPredictions(getCurrencies()));
            priceCollector.benchmarkComplete(getCurrencies());
            initialBenchmark();
        } else if (priceCollector.getLap() == -1) {
            setCurrencies(GOFAIPredictor.singlePrediction(getCurrencies()));
            priceCollector.setCurrencies(getCurrencies());
            trade();
        }
        notifyObservers();
    }
}
