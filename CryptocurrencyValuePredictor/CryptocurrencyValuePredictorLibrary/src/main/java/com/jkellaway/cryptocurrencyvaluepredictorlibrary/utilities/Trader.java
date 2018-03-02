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
    
    public Trader(){
        gdaxAPIController = new GDAXAPIController();
        wallet = new Wallet(Globals.STARTINGUNITS, Globals.STARTINGVALUE);
    }
    
    public Trader(String currency, Double value){
        wallet = new Wallet(currency, value);
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
