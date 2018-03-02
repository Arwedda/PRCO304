/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;

/**
 *
 * @author jkell
 */
public class TradeBot {
    private String holdingID;
    private Double value;
    
    public TradeBot(){
        this.holdingID = "Unknown";
        this.value = null;
    }
    
    public TradeBot(String holdingID, Double value){
        this.holdingID = holdingID;
        this.value = value;
    }

    public String getHoldingID() {
        return holdingID;
    }

    public void trade(String holdingID, Currency[] currencies) {
        if (!this.holdingID.equals(holdingID)) {
            ExchangeRate current = new ExchangeRate();
            ExchangeRate desired = new ExchangeRate();
            for (Currency currency : currencies){
                String id = currency.getID();
                if (id.equals(this.holdingID)) {
                    current = currency.getRate();
                } else if (id.equals(holdingID)) {
                    desired = currency.getRate();
                }
            }
            
            if (holdingID.equals("USD")){
                this.value *= current.getValue();
                this.holdingID = "USD";
            } else {
                if (this.holdingID.equals("USD")){
                    this.value /= desired.getValue();
                } else {
                    this.value = this.value * current.getValue() / desired.getValue();
                }
                this.holdingID = desired.getCurrency_id();
            }
        } else {
            System.out.println("[INFO] Holding desired currency - " + this.holdingID);
        }
    }

    public Double getValue() {
        return value;
    }
    
    public Double getUSDValue(Currency[] currencies) {
        for (Currency currency : currencies){
            if (currency.getID().equals(this.holdingID)) {
                return this.value * currency.getRate().getValue();
            }
        }
        return this.value;
    }
}
