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
    private String holdPreference;
    
    public Trader(){
        gdaxAPIController = new GDAXAPIController();
        wallet = new Wallet(Globals.STARTINGUNITS, Globals.STARTINGVALUE);
    }
    
    public Trader(String currency, Double value, String tradeMode, String holdPreference){
        wallet = new Wallet(currency, value);
        this.tradeMode = tradeMode;
        this.holdPreference = holdPreference;
    }
    
    public void autoTrade(Currency[] currencies){
        String desiredID = holdPreference;
        Double growth = 0.0;
        Double predictedGrowth;
        ExchangeRate rate;
        switch (tradeMode){
            case "NeuralNetwork":
                for (Currency currency : currencies) {
                    rate = currency.getRate();
                    predictedGrowth = rate.getNeuralNetworkNextGrowth();
                    if (growth < predictedGrowth) {
                        desiredID = rate.getCurrency_id();
                        growth = predictedGrowth;
                    }
                }
                trade(desiredID, currencies);
                break;
            case "GOFAI":
                for (Currency currency : currencies) {
                    rate = currency.getRate();
                    predictedGrowth = rate.getGofaiNextGrowth();
                    if (growth < predictedGrowth) {
                        desiredID = rate.getCurrency_id();
                        growth = predictedGrowth;
                    }
                }
                trade(desiredID, currencies);
                break;
            case "Manual":
                System.out.println("[INFO] Autotrade mode off...");
                break;
            default:
                System.out.println("[INFO] Invalid trade mode...");
        }
    }
    
    
    public void trade(String desiredID, Currency[] currencies) {
        String holdingID = wallet.getHoldingID();
        Double value = wallet.getValue();
        if (!holdingID.equals(holdingID)) {
            ExchangeRate current = new ExchangeRate();
            ExchangeRate desired = new ExchangeRate();
            for (Currency currency : currencies){
                String id = currency.getID();
                if (id.equals(holdingID)) {
                    current = currency.getRate();
                } else if (id.equals(desiredID)) {
                    desired = currency.getRate();
                }
            }
            
            if (desiredID.equals("USD")){
                wallet.setValue(value * current.getValue());
                wallet.setHoldingID("USD");
            } else {
                if (holdingID.equals("USD")){
                    wallet.setValue(value / desired.getValue());
                } else {
                    wallet.setValue(value * current.getValue() / desired.getValue());
                }
                wallet.setHoldingID(desired.getCurrency_id());
            }
        } else {
            System.out.println("[INFO] Holding desired currency - " + value + " " + holdingID);
        }
    }
}
