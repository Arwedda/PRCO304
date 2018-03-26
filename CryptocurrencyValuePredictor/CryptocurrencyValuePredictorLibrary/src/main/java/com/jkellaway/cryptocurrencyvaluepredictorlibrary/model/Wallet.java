/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

/**
 *
 * @author jkell
 */
public class Wallet {
    private String holdingID;
    private Double value;
    private Double[] startingValues;
    private Double startingUSD;
    
    public Wallet(){
        this.holdingID = "Unknown";
        this.value = null;
        this.startingValues = new Double[4];
        this.startingUSD = null;
    }
    
    public Wallet(String holdingID, Double value){
        this.holdingID = holdingID;
        this.value = value;
        this.startingValues = new Double[4];
        this.startingUSD = (holdingID.equals("USD")) ? value : null;
    }

    public String getHoldingID() {
        return holdingID;
    }

    public void setHoldingID(String holdingID) {
        this.holdingID = holdingID;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
    
    public void setStartingValues(Double[] startingValues, Double startQuantity, Integer startIndex){
        this.startingValues = startingValues;
        if (startQuantity != null && !holdingID.equals("USD")) {
            this.startingUSD = startingValues[startIndex] * startQuantity;
        }
    }
    
    public Double[] getStartingValues(){
        return startingValues;
    }
    
    public Double getStartingUSD(){
        return startingUSD;
    }
    
    public Double getUSDValue(Currency[] currencies) {
        if (value != null) {
            for (Currency currency : currencies){
                if (currency.getID().equals(this.holdingID)) {
                    return value * currency.getRate().getValue();
                }
            }
        }
        return value;
    }
}
