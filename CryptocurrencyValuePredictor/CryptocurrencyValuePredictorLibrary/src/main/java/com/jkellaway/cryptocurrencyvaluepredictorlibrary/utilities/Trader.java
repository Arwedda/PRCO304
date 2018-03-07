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
    private String tradeMode;
    private String holdMode;

    
    
    public Trader(){
        gdaxAPIController = new GDAXAPIController();
        wallet = new Wallet(Globals.STARTINGUNITS, Globals.STARTINGVALUE);
        this.tradeMode = "unknown";
        this.holdMode = "unknown";
    }
    
    public Trader(String currency, Double value, String tradeMode, String holdMode){
        wallet = new Wallet(currency, value);
        this.tradeMode = tradeMode;
        this.holdMode = holdMode;
    }
    
    public void autoTrade(Currency[] currencies){
        ExchangeRate desired = new ExchangeRate();
        Double growth = 0.0;
        Double predictedGrowth;
        ExchangeRate rate;
        switch (tradeMode){
            case "NeuralNetwork":
                for (Currency currency : currencies) {
                    rate = currency.getRate();
                    predictedGrowth = rate.getNeuralNetworkNextGrowth();
                    if (growth < predictedGrowth) {
                        desired = rate;
                        growth = predictedGrowth;
                    }
                }
                trade(desired, currencies);
                break;
            case "GOFAI":
                for (Currency currency : currencies) {
                    rate = currency.getRate();
                    predictedGrowth = rate.getGofaiNextGrowth();
                    if (growth < predictedGrowth) {
                        desired = rate;
                        growth = predictedGrowth;
                    }
                }
                trade(desired, currencies);
                break;
            case "Manual":
                System.out.println("[INFO] Autotrade mode off...");
                break;
            default:
                System.out.println("[INFO] Invalid trade mode...");
        }
    }
    
    public void trade(ExchangeRate desired, Currency[] currencies) {
        String holdingID = wallet.getHoldingID();
        if (!holdingID.equals(desired.getCurrency_id())) {
            ExchangeRate current = new ExchangeRate();
            for (Currency currency : currencies){
                String id = currency.getID();
                if (id.equals(holdingID)) {
                    current = currency.getRate();
                    break;
                }
            }
            makeTrade(current, desired);
        } else {
            Double value = wallet.getValue();
            System.out.println("[INFO] Holding desired currency - " + value + " " + holdingID);
        }
    }
    
    private void makeTrade(ExchangeRate current, ExchangeRate desired){
        Double value = wallet.getValue();
        if (!current.getCurrency_id().equals("Unknown") && desired.getCurrency_id().equals("Unknown")){
            wallet.setValue(value * current.getValue());
            wallet.setHoldingID("USD");
        } else {
            if (wallet.getHoldingID().equals("USD")){
                wallet.setValue(value / desired.getValue());
            } else {
                wallet.setValue(value * current.getValue() / desired.getValue());
            }
            wallet.setHoldingID(desired.getCurrency_id());
        }
    }
    
    public void tradeTest(Currency[] currencies, int numberOfPredictions, int futureReading, int predictionIndex){
        ExchangeRate[] rates = new ExchangeRate[4];
        ExchangeRate desired = new ExchangeRate();
        Double growth;
        Double predictedGrowth;
        ExchangeRate rate;
        
        trading:
        for (int i = numberOfPredictions + 1; i < Globals.READINGSREQUIRED - 1; i++) {
            for (int j = 0; j < 4 ; j++) {
                rates[j] = currencies[j].getRates().get(i + futureReading);
            }
            
            switch (tradeMode) {
                case "NeuralNetwork":
                    growth = 0.0;
                    desired = new ExchangeRate();
                    for (Currency currency : currencies) {
                        rate = currency.getRate();
                        predictedGrowth = rate.getNeuralNetworkNextGrowth();
                        if (growth < predictedGrowth) {
                            desired = rate;
                            growth = predictedGrowth;
                        }
                    }
                    trade(desired, currencies);
                    break;
                case "GOFAI":
                    growth = 0.0;
                    desired = new ExchangeRate();
                    for (Currency currency : currencies) {
                        rate = currency.getRate();
                        predictedGrowth = rate.gofaiGrowth[predictionIndex];
                        if (growth < predictedGrowth) {
                            desired = rate;
                            growth = predictedGrowth;
                        }
                    }
                    trade(desired, currencies);
                    break;
                case "Manual":
                    if (holdMode.equals("BEST")){
                        desired = getHighestGrowth(rates);
                        trade(desired, rates);
                    } else if (holdMode.equals("WORST")){
                        desired = getLowestGrowth(rates);
                        trade(desired, rates);
                    } else {
                        if (desired.getCurrency_id().equals("Unknown")){
                            desired = getRate(rates);
                            trade(desired, rates);
                        } else {
                            desired = new ExchangeRate();
                            trade(desired, currencies);
                            break trading;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        
        if (!wallet.getHoldingID().equals("USD")){
            System.out.println("Finished holding " + wallet.getValue() + " " + wallet.getHoldingID());
        }
        System.out.println("$" + wallet.getUSDValue(currencies));
    }
    
    private ExchangeRate getHighestGrowth(ExchangeRate[] rates){
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
    
    private ExchangeRate getLowestGrowth(ExchangeRate[] rates){
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
    
    private ExchangeRate getRate(ExchangeRate[] rates){
        ExchangeRate desired = new ExchangeRate();
        
        for (ExchangeRate rate : rates){
            if (rate.getCurrency_id().equals(holdMode)){
                desired = rate;
                break;
            }
        }
        return desired;
    }
    
    private void trade(ExchangeRate desired, ExchangeRate[] rates){
        String holdingID = wallet.getHoldingID();
        if (!holdingID.equals(desired.getCurrency_id())) {
            ExchangeRate current = new ExchangeRate();
            for (ExchangeRate rate : rates){
                String id = rate.getCurrency_id();
                if (id.equals(holdingID)) {
                    current = rate;
                    break;
                }
            }
            makeTrade(current, desired);
        } else {
            Double value = wallet.getValue();
            System.out.println("[INFO] Holding desired currency - " + value + " " + holdingID);
        }
    }
}
