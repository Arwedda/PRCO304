/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.GDAXAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Wallet;

/**
 *
 * @author jkell
 */
public class Trader {
    private GDAXAPIController gdaxAPIController;
    private Wallet wallet;
    private TradeMode tradeMode;
    private int tradeModeIndex;
    private HoldMode holdMode;
    
    /**
     * Default Trader constructor.
     */
    public Trader() {
        gdaxAPIController = new GDAXAPIController();
        wallet = new Wallet(Globals.STARTINGUNITS, Globals.STARTINGVALUE);
        this.tradeMode = TradeMode.MANUAL;
        this.tradeModeIndex = -1;
        this.holdMode = HoldMode.USD;
    }
    
    /**
     * Standard Trader constructor.
     * @param currencyID The identifier for the Currency held in the Wallet.
     * @param value The amount of currencyID Currency held in this Wallet.
     * @param tradeMode The TradeMode selected for this Trader.
     * @param tradeModeIndex The TradeMode index selected for this Trader.
     * @param holdMode The HoldMode selected for this Trader.
     */
    public Trader(String currencyID, Double value, TradeMode tradeMode, int tradeModeIndex, HoldMode holdMode) {
        wallet = new Wallet(currencyID, value);
        this.tradeMode = tradeMode;
        this.tradeModeIndex = tradeModeIndex;
        this.holdMode = holdMode;
    }

    /**
     * Gets the Wallet being used by this Trader.
     * @return The Wallet being used by this Trader.
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Gets the TradeMode strategy assigned to this Trader.
     * @return The TradeMode of this Trader.
     */
    public TradeMode getTradeMode() {
        return tradeMode;
    }

    /**
     * Sets a new TradeMode strategy for this Trader.
     * @param tradeMode The new TradeMode strategy of this Trader.
     */
    public void setTradeMode(TradeMode tradeMode) {
        this.tradeMode = tradeMode;
    }

    /**
     * Gets the TradeMode strategy index assigned to this Trader.
     * @return The TradeMode index of this Trader.
     */
    public int getTradeModeIndex() {
        return tradeModeIndex;
    }

    /**
     * Sets a new TradeMode strategy index assigned to this Trader. Takes 1 away
     * from parameter to select the index.
     * @param tradeModeNo TradeMode strategy number to switch to (-1 for index)
     */
    public void setTradeModeIndex(int tradeModeNo) {
        this.tradeModeIndex = tradeModeNo - 1;
    }

    /**
     * Sets a new HoldMode strategy for this Trader.
     * @param holdMode The new HoldMode strategy for this Trader.
     */
    public void setHoldMode(HoldMode holdMode) {
        this.holdMode = holdMode;
    }

    /**
     * Gets the HoldMode strategy assigned to this Trader.
     * @return The HoldMode strategy of this Trader.
     */
    public HoldMode getHoldMode() {
        return holdMode;
    }
    
    /**
     * The standard Trade performed by the regular Trader that will be used by
     * the end user. Calculates a desired Currency and issues a Trade order to 
     * convert the Wallet's holding Currency into the desired Currency.
     * @param currencies The Currency data to base the decision on.
     */
    public void autoTrade(Currency[] currencies) {
        ExchangeRate desired = new ExchangeRate();
        Double growth = 0.0;
        Double predictedGrowth;
        ExchangeRate rate;
        switch (tradeMode){
            case NEURALNETWORK:
                for (Currency currency : currencies) {
                    rate = currency.getRate();
                    predictedGrowth = rate.getNeuralNetworkNextGrowth()[tradeModeIndex];
                    if (growth < predictedGrowth) {
                        desired = rate;
                        growth = predictedGrowth;
                    }
                }
                if (growth == 0.0 && holdMode == HoldMode.CRYPTOCURRENCY){
                    break;
                }
                trade(desired, currencies);
                break;
            case GOFAI:
                for (Currency currency : currencies) {
                    rate = currency.getRate();
                    predictedGrowth = rate.getGofaiNextGrowth()[tradeModeIndex];
                    if (growth < predictedGrowth) {
                        desired = rate;
                        growth = predictedGrowth;
                    }
                }
                if (growth == 0.0 && holdMode == HoldMode.CRYPTOCURRENCY){
                    break;
                }
                trade(desired, currencies);
                break;
            case MANUAL:
                System.out.println("[INFO] Autotrade mode off...");
                break;
            default:
                System.out.println("[INFO] Invalid trade mode...");
        }
    }
    
    /**
     * Runs all trading strategies available to the end user as well as a the
     * best, worst and hold strategies possible. Best and worst strategies would
     * require knowledge of future prices, but hold strategies simply keep one
     * Currency. This algorithm trades through each collected historic minute to
     * give a sense of which strategy is performing well in the current market.
     * @param currencies Array of Currency data to perform trading with.
     * @param numberOfReadings The number of prices to iterate over (and number of trades to perform)
     * @param numberOfPredictions The number of predictions made per strategy per price.
     */
    public void tradeBenchmark(Currency[] currencies, Integer numberOfReadings, Integer numberOfPredictions) {
        int noOfCurrencies = currencies.length;
        ExchangeRate[] rates = new ExchangeRate[noOfCurrencies];
        ExchangeRate desired = new ExchangeRate();
        Double growth;
        Double predictedGrowth;
        Double starting[] = new Double[noOfCurrencies];
        for (int i = 0; i < noOfCurrencies; i++) {
            starting[i] = Globals.STARTINGVALUE / currencies[i].getRates().get(numberOfPredictions).getValue();
        }
        wallet.setStartingValues(starting, currencies[0].getRate().getValue(), 0);

        trading:
        for (int i = numberOfPredictions; i < numberOfReadings - 1; i++) {
            for (int j = 0; j < noOfCurrencies ; j++) {
                rates[j] = currencies[j].getRates().get(i);
            }
            
            switch (tradeMode) {
                case NEURALNETWORK:
                    growth = 0.0;
                    desired = new ExchangeRate();
                    for (ExchangeRate rate : rates) {
                        predictedGrowth = rate.getNeuralNetworkNextGrowth()[tradeModeIndex];
                        if (growth < predictedGrowth) {
                            desired = rate;
                            growth = predictedGrowth;
                        }
                    }
                    if (growth == 0.0 && holdMode == HoldMode.CRYPTOCURRENCY){
                        break;
                    }
                    trade(desired, rates);
                    break;
                case GOFAI:
                    growth = 0.0;
                    desired = new ExchangeRate();
                    for (ExchangeRate rate : rates) {
                        predictedGrowth = rate.getGofaiNextGrowth()[tradeModeIndex];
                        if (growth < predictedGrowth) {
                            desired = rate;
                            growth = predictedGrowth;
                        }
                    }
                    if (growth == 0.0 && holdMode == HoldMode.CRYPTOCURRENCY){
                        break;
                    }
                    trade(desired, rates);
                    break;
                case MANUAL:
                    ExchangeRate[] nextRates = new ExchangeRate[4];
                    for (int j = 0; j < noOfCurrencies ; j++) {
                        nextRates[j] = currencies[j].getRates().get(i+1);
                    }
                    switch (holdMode) {
                        case BEST:
                            desired = getHighestGrowth(nextRates);
                            trade(desired, rates);
                            break;
                        case WORST:
                            desired = getLowestGrowth(nextRates);
                            trade(desired, rates);
                            break;
                        default:
                            //HOLD - Manual never gets USD or CRYPTOCURRENCY
                            if (!holdMode.toString().equals(wallet.getHoldingID())) {
                                desired = getRate(rates);
                                trade(desired, rates);
                            } else {
                                break trading;
                            }
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
        
        if (!wallet.getHoldingID().equals(HoldMode.USD.toString())){
            System.out.println("Finished holding " + wallet.getValue() + " " + wallet.getHoldingID());
        }
        System.out.println("$" + wallet.getUSDValue(currencies));
    }
    
    /**
     * Runs all trading strategies available to the end user as well as a the
     * best, worst and hold strategies possible. Best and worst strategies would
     * require knowledge of future prices, but hold strategies simply keep one
     * Currency. This algorithm trades through a single minute of data to give a
     * continued sense of which strategy is performing well in the current market.
     * @param currencies Array of Currency data to perform trading with.
     */
    public void tradeBenchmark(Currency[] currencies) {
        int noOfCurrencies = currencies.length;
        ExchangeRate[] rates = new ExchangeRate[noOfCurrencies];
        ExchangeRate desired = new ExchangeRate();
        Double growth;
        Double predictedGrowth;
        
        for (int i = 0; i < noOfCurrencies; i++) {
            int index = currencies[i].getRates().size() - 2;
            rates[i] = currencies[i].getRates().get(index);
        }
        
        switch (tradeMode) {
            case NEURALNETWORK:
                growth = 0.0;
                desired = new ExchangeRate();
                for (ExchangeRate rate : rates) {
                    predictedGrowth = rate.getNeuralNetworkNextGrowth()[tradeModeIndex];
                    if (growth < predictedGrowth) {
                        desired = rate;
                        growth = predictedGrowth;
                    }
                }
                if (growth == 0.0 && holdMode == HoldMode.CRYPTOCURRENCY){
                    break;
                }
                trade(desired, rates);
                break;
            case GOFAI:
                growth = 0.0;
                desired = new ExchangeRate();
                for (ExchangeRate rate : rates) {
                    predictedGrowth = rate.getGofaiNextGrowth()[tradeModeIndex];
                    if (growth < predictedGrowth) {
                        desired = rate;
                        growth = predictedGrowth;
                    }
                }
                if (growth == 0.0 && holdMode == HoldMode.CRYPTOCURRENCY){
                    break;
                }
                trade(desired, rates);
                break;
            case MANUAL:
                ExchangeRate[] nextRates = new ExchangeRate[4];
                for (int j = 0; j < noOfCurrencies ; j++) {
                    nextRates[j] = currencies[j].getRate();
                }
                switch (holdMode) {
                    case BEST:
                        desired = getHighestGrowth(nextRates);
                        trade(desired, rates);
                        break;
                    case WORST:
                        desired = getLowestGrowth(nextRates);
                        trade(desired, rates);
                        break;
                    default:
                        //HOLD - Manual never gets USD or CRYPTOCURRENCY
                        if (!holdMode.toString().equals(wallet.getHoldingID())) {
                            desired = getRate(rates);
                            trade(desired, rates);
                        }
                        break;
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * Selects the ExchangeRate with the highest growth in the parameter.
     * @param rates The available ExchangeRates at the time.
     * @return The ExchangeRate with the highest growth available.
     */
    private ExchangeRate getHighestGrowth(ExchangeRate[] rates) {
        ExchangeRate best = new ExchangeRate();
        Double growth = 0.0;
        
        for (ExchangeRate rate : rates){
            if (growth < rate.getGrowth()) {
                best = rate;
                growth = rate.getGrowth();
            }
        }
        return best;
    }
    
    /**
     * Selects the ExchangeRate with the lowest growth in the parameter.
     * @param rates The available ExchangeRates at the time.
     * @return The ExchangeRate with the lowest growth available.
     */
    private ExchangeRate getLowestGrowth(ExchangeRate[] rates) {
        ExchangeRate worst = new ExchangeRate();
        Double growth = 0.0;
        
        for (ExchangeRate rate : rates){
            if (rate.getGrowth() < growth){
                worst = rate;
                growth = rate.getGrowth();
            }
        }
        return worst;
    }
    
    /**
     * Gets the ExchangeRate that has an currency_id that matches the HoldMode.
     * For use with HoldMode benchmarking only.
     * @param rates The available ExchangeRates at the time.
     * @return The ExchangeRate of the Currency desired by HoldMode.
     */
    private ExchangeRate getRate(ExchangeRate[] rates) {
        ExchangeRate desired = new ExchangeRate();
        
        for (ExchangeRate rate : rates){
            if (rate.getCurrency_id().equals(holdMode.toString())){
                desired = rate;
                break;
            }
        }
        return desired;
    }
    
    /**
     * Sets up the trade to the desired Currency from the one currently being
     * held in the Wallet. If the wallet is holding the desired Currency then 
     * the trade does not need to be made. If the desired currency is USD and 
     * the HoldMode is Cryptocurrency then the trade does not need to be made.
     * @param desired The desired Currency's current ExchangeRate.
     * @param rates The current ExchangeRates for each Currency.
     */
    private void trade(ExchangeRate desired, ExchangeRate[] rates) {
        if (holdMode.equals(HoldMode.CRYPTOCURRENCY) && desired.getCurrency_id().equals("Unknown")) {
            return;
        }
        ExchangeRate current = new ExchangeRate();
        String holdingID = wallet.getHoldingID();
        if (!holdingID.equals(desired.getCurrency_id()) && 
                !((holdingID.equals(HoldMode.USD.toString()) && desired.getCurrency_id().equals("Unknown")))) {
            for (ExchangeRate rate : rates){
                String id = rate.getCurrency_id();
                if (id.equals(holdingID)) {
                    current = rate;
                    break;
                }
            }
            //Avoid issues linked with buying or selling Cryptocurrency for 0.0
            if (current.getCurrency_id().equals("Unknown")) {
                if (0.0 < desired.getValue()) {
                    makeTrade(current, desired);
                }
            } else if (desired.getCurrency_id().equals("Unknown")) {
                if (0.0 < current.getValue()) {
                    makeTrade(current, desired);
                }
            } else { //Crypto -> Crypto
                if (0.0 < current.getValue() && 0.0 < desired.getValue()) {
                    makeTrade(current, desired);
                }
            }
        }
        //System.out.println("[INFO] Holding desired currency - " + wallet.getValue() + " " + wallet.getHoldingID());
    }
    
    /**
     * Sets up the trade to the desired Currency from the one currently being
     * held in the Wallet. If the wallet is holding the desired Currency then 
     * the trade does not need to be made. If the desired currency is USD and 
     * the HoldMode is Cryptocurrency then the trade does not need to be made.
     * @param desired The desired Currency's current ExchangeRate.
     * @param currencies The current Currency data.
     */
    private void trade(ExchangeRate desired, Currency[] currencies) {
        if (holdMode.equals(HoldMode.CRYPTOCURRENCY) && desired.getCurrency_id().equals("Unknown")) {
            return;
        }
        String holdingID = wallet.getHoldingID();
        if (!holdingID.equals(desired.getCurrency_id()) && 
                !(holdingID.equals(HoldMode.USD.toString()) && desired.getCurrency_id().equals("Unknown"))) {
            ExchangeRate current = new ExchangeRate();
            for (Currency currency : currencies){
                String id = currency.getID();
                if (id.equals(holdingID)) {
                    current = currency.getRate();
                    break;
                }
            }
            //Avoid issues linked with buying or selling Cryptocurrency for 0.0
            if (current.getCurrency_id().equals("Unknown")) {
                if (0.0 < desired.getValue()) {
                    makeTrade(current, desired);
                }
            } else if (desired.getCurrency_id().equals("Unknown")) {
                if (0.0 < current.getValue()) {
                    makeTrade(current, desired);
                }
            } else { //Crypto -> Crypto
                if (0.0 < current.getValue() && 0.0 < desired.getValue()) {
                    makeTrade(current, desired);
                }
            }
        }
        //System.out.println("[INFO] Holding desired currency - " + wallet.getValue() + " " + wallet.getHoldingID());
    }
    
    /**
     * Makes the trade from the current ExchangeRate to the desired ExchangeRate.
     * If current Currency and desired Currency are not the same then:
     * If the desired Currency is not "Unknown" (USD) then do a generic swap 
     * from current to desired. If the desired Currency is "Unknown" (USD) then
     * swap to USD (different setHoldingID conventions).
     * @param current The ExchangeRate of the current Currency held in the Wallet.
     * @param desired The ExchangeRate of the desired Currency.
     */
    private void makeTrade(ExchangeRate current, ExchangeRate desired) {
        Double value = wallet.getValue();
        if (!current.getCurrency_id().equals(desired.getCurrency_id())) {
            if (!desired.getCurrency_id().equals("Unknown")) {
                if (wallet.getHoldingID().equals(HoldMode.USD.toString())){
                    wallet.setValue(value / desired.getValue());
                } else {
                    wallet.setValue(value * current.getValue() / desired.getValue());
                }
                wallet.setHoldingID(desired.getCurrency_id());
            } else {
                wallet.setValue(value * current.getValue());
                wallet.setHoldingID(HoldMode.USD.toString());
            }
        }
    }
}
