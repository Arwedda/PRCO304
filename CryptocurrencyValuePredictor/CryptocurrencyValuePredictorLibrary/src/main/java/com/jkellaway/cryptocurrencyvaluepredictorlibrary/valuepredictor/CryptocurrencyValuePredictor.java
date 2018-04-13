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
    protected CryptocurrencyValuePredictor() {
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
     * @return Single initiation of CryptocurrencyValuePredictor.
     */
    public static CryptocurrencyValuePredictor getInstance() {
        if (instance == null){
            instance = new CryptocurrencyValuePredictor();
        }
        return instance;
    }

    /**
     * Gets the current Currency data stored in the PriceCollector class.
     * @return The current Currency data.
     */
    public Currency[] getCurrencies() {
        return priceCollector.getCurrencies();
    }

    /**
     * Sets the Currency data stored in the PriceCollector class.
     * @param currencies The new Currency data.
     */
    private void setCurrencies(Currency[] currencies) {
        this.priceCollector.setCurrencies(currencies);
    }

    /**
     * Gets the PriceCollector.
     * @return The PriceCollector.
     */
    public PriceCollector getPriceCollector() {
        return priceCollector;
    }

    /**
     * Gets the user's Trader.
     * @return The user's Trader.
     */
    public Trader getTrader() {
        return trader;
    }
    
    /**
     * Performs initial benchmarking of each Trader strategy. This allows the 
     * end user to see how strategies are performing in the current market.
     */
    private void initialBenchmark() {
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
    
    /**
     * Updates benchmarking of each Trader strategy and performs actions for the 
     * user's Trader.
     */
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
    
    /**
     * Gets the Trader with the best possible performance. (Requires knowledge 
     * of future prices).
     * @return Trader with the best possible performance.
     */
    public Trader getBest() {
        return best;
    }

    /**
     * Gets the Trader with the worst possible performance. (Requires knowledge 
     * of future prices).
     * @return Trader with the worst possible performance.
     */
    public Trader getWorst() {
        return worst;
    }
    
    /**
     * Gets a List of Traders with the hold strategies. (One trade to their
     * desired Currency).
     * @return Traders with hold strategy.
     */
    public Trader[] getHolders() {
        return holders;
    }

    /**
     * Gets a List of Traders with GOFAI strategies with a HoldMode of USD.
     * @return Traders using GOFAI + HoldMode USD.
     */
    public Trader[] getGOFAITradersUSD() {
        return GOFAITradersUSD;
    }

    /**
     * Gets a List of Traders with GOFAI strategies with a HoldMode of 
     * Cryptocurrency.
     * @return Traders using GOFAI + HoldMode Cryptocurrency.
     */
    public Trader[] getGOFAITradersHold() {
        return GOFAITradersHold;
    }
    
    /**
     * Gets the current historic price collection lap.
     * @return The current historic price collection lap.
     */
    public int getLap() {
        return priceCollector.getLap();
    }
    
    /**
     * Initialises the user's Trader.
     * @param gofai true - GOFAI, false - NeuralNetwork
     * @param tradeModeNum TradeMode strategy number (array index + 1)
     * @param startValue The initial Wallet value in USD.
     * @param holdUSD true - HoldMode.USD, false - HoldMode.CRYPTOCURRENCY
     * @param apiKey User's GDAX API key to allow REAL trading.
     */
    public void startTrading(boolean gofai, int tradeModeNum, Double startValue, boolean holdUSD, String apiKey) {
        TradeMode tradeMode = (gofai ? TradeMode.GOFAI : TradeMode.NEURALNETWORK);
        HoldMode holdMode =  (holdUSD ? HoldMode.USD : HoldMode.CRYPTOCURRENCY);
        //System.out.println(apiKey);
        trader = new Trader("USD", startValue, tradeMode, tradeModeNum, holdMode);
    }
    
    /**
     * Stops the user's Trader from trading.
     */
    public void stopTrading() {
        trader.setTradeMode(TradeMode.MANUAL);
    }

    /**
     * Adds the parameter Observer to the Observer List.
     * @param o Observer to register.
     * @return true = registered correctly, false = failed to register.
     */
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

    /**
     * Remove parameter Observer from the Observer List.
     * @param o Observer to be removed.
     * @return true = Observer successfully removed, false = failed to remove.
     */
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

    /**
     * Notify each Observer to update itself due to a change of situation.
     */
    @Override
    public void notifyObservers() {
        if (this.observers != null && 0 < this.observers.size()) {
            for (IObserver currentObserver : this.observers) {
                currentObserver.update();
            }
        }    
    }

    /**
     * Updates the CryptocurrencyValuePredictor based on ExchangeRates being 
     * updated by the PriceCollector. Also notifies the GUI so that it is also
     * updated.
     */
    @Override
    public void update() {
        if (priceCollector.getLap() == 3) {
            priceCollector.benchmarkComplete(GOFAIPredictor.initialPredictions(getCurrencies()));
            initialBenchmark();
        } else if (priceCollector.getLap() == -1) {
            setCurrencies(GOFAIPredictor.singlePrediction(getCurrencies()));
            trade();
        }
        notifyObservers();
    }
}
