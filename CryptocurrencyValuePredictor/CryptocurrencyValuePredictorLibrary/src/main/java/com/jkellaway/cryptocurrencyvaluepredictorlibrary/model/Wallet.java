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
    
    /**
     * Default Wallet constructor.
     */
    public Wallet(){
        this.holdingID = "Unknown";
        this.value = null;
        this.startingValues = new Double[4];
        this.startingUSD = null;
    }
    
    /**
     * Standard Wallet constructor.
     * @param holdingID The identifier for the Currency held in this Wallet.
     * @param value The amount of holdingID Currency held in this Wallet.
     */
    public Wallet(String holdingID, Double value){
        this.holdingID = holdingID;
        this.value = value;
        this.startingValues = new Double[4];
        this.startingUSD = (holdingID.equals("USD")) ? value : null;
    }
    
    /**
     * Get the globally recognised identifier (normally 3 characters).
     * @return String representation of identifier.
     */
    public String getHoldingID() {
        return holdingID;
    }

    /**
     * Set the new globally recognised identifier (normally 3 characters).
     * @param holdingID The new identifier String.
     */
    public void setHoldingID(String holdingID) {
        this.holdingID = holdingID;
    }

    /**
     * Get the amount of holdingID Currency held in this Wallet.
     * @return The amount of holdingID in this Wallet.
     */
    public Double getValue() {
        return value;
    }
    
    /**
     * Set the amount of holdingID Currency held in this Wallet.
     * @param value The new amount of holdingID in this Wallet.
     */
    public void setValue(Double value) {
        this.value = value;
    }
    
    /**
     * Set quantities that could be acquired at the start of trading for use
     * when benchmarking and comparing performances.
     * @param startingValues The amount of each Currency that could be purchased.
     * @param startQuantity The amount of holdingID held in Wallet.
     * @param startIndex The initial Currency held.
     */
    public void setStartingValues(Double[] startingValues, Double startQuantity, Integer startIndex){
        this.startingValues = startingValues;
        if (startQuantity != null && !holdingID.equals("USD")) {
            this.startingUSD = startingValues[startIndex] * startQuantity;
        }
    }
    
    /**
     * Get quantities that could be acquired at the start of trading for use
     * when benchmarking and comparing performances.
     * @return Quantities of each holdingID available at trading minute 0.
     */
    public Double[] getStartingValues(){
        return startingValues;
    }
    
    /**
     * Get the USD value invested at the start of trading.
     * @return The USD value invested at the start of trading.
     */
    public Double getStartingUSD(){
        return startingUSD;
    }
    
    /**
     * Calculates the current USD value of holdingID.
     * @param currencies The Currency array to extract prices from.
     * @return The USD value of holdingID currently held in the Wallet.
     */
    public Double getUSDValue(Currency[] currencies) {
        if (value != null) {
            for (Currency currency : currencies){
                if (currency.getID().equals(this.holdingID)) {
                    //if (currency.getRate().getValue() != null) {
                        return value * currency.getRate().getValue();
                    /*} else {
                        return 0.0;
                    }*/
                }
            }
        }
        return value;
    }
}
