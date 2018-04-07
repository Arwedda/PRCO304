/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import java.time.LocalDateTime;

/**
 *
 * @author jkell
 */
public class ExchangeRate implements Comparable<ExchangeRate> {
    private String currency_id;
    private String timestamp;
    private Double value;
    private Double growth;
    private transient Double[] gofaiNextGrowth;
    private transient Double[] neuralNetworkNextGrowth;
    private Integer lastTrade;
    private transient LocalDateTime ldtTimestamp;
    
    /**
     * Default ExchangeRate constructor.
     */
    public ExchangeRate() {
        this.currency_id = "Unknown";
        this.timestamp = "Unknown";
        this.value = 0.0;
        this.lastTrade = null;
        this.growth = null;
        this.gofaiNextGrowth = new Double[Globals.NUMBEROFPREDICTIONS];
        this.neuralNetworkNextGrowth = new Double[Globals.NUMBEROFPREDICTIONS];
        this.ldtTimestamp = null;
    }

    /**
     * Standard ExchangeRate constructor with timestamp as a LocalDateTime.
     * Both constructors calculate the alternative timestamp representation.
     * @param currency_id The identifier.
     * @param timestamp The LocalDateTime representation of the ExchangeRate's time.
     * @param value The ExchangeRate USD value.
     * @param growth The percentage growth from the previous ExchangeRate.
     * @param gofaiNextGrowth The array of GOFAI predictions for the next growth.
     * @param neuralNetworkNextGrowth The array of Neural Network predictions for the next growth.
     * @param lastTrade The trade_id of the last (oldest) GDAXTrade collected for this ExchangeRate.
     */
    public ExchangeRate(String currency_id, LocalDateTime timestamp, Double value, Double growth, Double[] gofaiNextGrowth, Double[] neuralNetworkNextGrowth, Integer lastTrade) {
        this.currency_id = currency_id;
        this.timestamp = LocalDateTimeHelper.startOfMinute(timestamp).toString();
        this.value = value;
        this.growth = growth;
        try {
            this.gofaiNextGrowth = (gofaiNextGrowth.length < 1) ? new Double[Globals.NUMBEROFPREDICTIONS] : gofaiNextGrowth;
        } catch (Exception e) {
            this.gofaiNextGrowth = new Double[Globals.NUMBEROFPREDICTIONS];
        }
        try {
            this.neuralNetworkNextGrowth = (neuralNetworkNextGrowth.length < 1) ? new Double[Globals.NUMBEROFPREDICTIONS] : neuralNetworkNextGrowth;
        } catch (Exception e) {
            this.neuralNetworkNextGrowth = new Double[Globals.NUMBEROFPREDICTIONS];
        }
        this.lastTrade = lastTrade;
        this.ldtTimestamp = timestamp;
    }

    /**
     * Standard ExchangeRate constructor with timestamp as a String.
     * Both constructors calculate the alternative timestamp representation.
     * @param currency_id The identifier.
     * @param timestamp The String representation of the ExchangeRate's time.
     * @param value The ExchangeRate USD value.
     * @param growth The percentage growth from the previous ExchangeRate.
     * @param gofaiNextGrowth The array of GOFAI predictions for the next growth.
     * @param neuralNetworkNextGrowth The array of Neural Network predictions for the next growth.
     * @param lastTrade The trade_id of the last (oldest) GDAXTrade collected for this ExchangeRate.
     */
    public ExchangeRate(String currency_id, String timestamp, Double value, Double growth, Double[] gofaiNextGrowth, Double[] neuralNetworkNextGrowth, Integer lastTrade) {
        this.currency_id = currency_id;
        this.timestamp = timestamp;
        this.value = value;
        this.growth = growth;
        try {
            this.gofaiNextGrowth = (gofaiNextGrowth.length < 1) ? new Double[Globals.NUMBEROFPREDICTIONS] : gofaiNextGrowth;
        } catch (Exception e) {
            this.gofaiNextGrowth = new Double[Globals.NUMBEROFPREDICTIONS];
        }
        try {
            this.neuralNetworkNextGrowth = (neuralNetworkNextGrowth.length < 1) ? new Double[Globals.NUMBEROFPREDICTIONS] : neuralNetworkNextGrowth;
        } catch (Exception e) {
            this.neuralNetworkNextGrowth = new Double[Globals.NUMBEROFPREDICTIONS];
        }
        this.lastTrade = lastTrade;
        this.ldtTimestamp = LocalDateTimeHelper.localDateTimeParser(timestamp);
    }
    
    /**
     * The globally recognised identifier (normally 3 characters).
     * @return The identifier.
     */
    public String getCurrency_id() {
        return currency_id;
    }

    /**
     * Adjust the globally recognised identifier.
     * @param currency_id The new identifier.
     */
    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    /**
     * The String representation of the ExchangeRate's time.
     * @return The String timestamp.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Adjust the ExchangeRate's time with a String.
     * @param timestamp The new String timestamp.
     */
    public void setTimestamp(String timestamp) {
        this.ldtTimestamp = LocalDateTimeHelper.localDateTimeParser(timestamp);
        try {
            this.timestamp = this.ldtTimestamp.toString();
        } catch (Exception e) {
            this.timestamp = "Unknown";
        }
    }

    /**
     * The LocalDateTime representation of the ExchangeRate's time.
     * @return The LocalDateTime timestamp.
     */
    public LocalDateTime getLDTTimestamp() {
        return ldtTimestamp;
    }

    /**
     * Adjust the ExchangeRate's time with a LocalDateTime.
     * @param timestamp The new LocalDateTime timestamp.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.ldtTimestamp = timestamp;
        if (timestamp != null) {
            this.timestamp = timestamp.toString();
        } else {
            this.timestamp = "Unknown";
        }
    }

    
    /**
     * Get the ExchangeRate USD value.
     * @return The ExchangeRate USD value as a Double.
     */
    public Double getValue() {
        return value;
    }

    /**
     * Set the ExchangeRate new USD value.
     * @param value USD value.
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Get the percentage growth from the previous ExchangeRate.
     * @return The percentage growth from the previous ExchangeRate as a Double.
     */
    public Double getGrowth() {
        return growth;
    }

    /**
     * Get the trade_id of the last (oldest) GDAXTrade collected for this ExchangeRate.
     * @return Integer trade_id of the last (oldest) GDAXTrade collected for this ExchangeRate.
     */
    public Integer getLastTrade() {
        return lastTrade;
    }

    /**
     * Set the trade_id of the last (oldest) GDAXTrade collected for this ExchangeRate.
     * @param lastTrade The new trade_id of the last (oldest) GDAXTrade collected for this ExchangeRate.
     */
    public void setLastTrade(Integer lastTrade) {
        this.lastTrade = lastTrade;
    }

    /**
     * Get the array of GOFAI predictions for the next growth.
     * @return The array of GOFAI predictions for the next growth.
     */
    public Double[] getGofaiNextGrowth() {
        return gofaiNextGrowth;
    }

    /**
     * Set the array of GOFAI predictions for the next growth.
     * @param gofaiNextGrowth The new array of GOFAI predictions for the next growth.
     */
    public void setGofaiNextGrowth(Double[] gofaiNextGrowth) {
        this.gofaiNextGrowth = gofaiNextGrowth;
    }

    /**
     * Get the array of Neural Network predictions for the next growth.
     * @return The array of Neural Network predictions for the next growth.
     */
    public Double[] getNeuralNetworkNextGrowth() {
        return neuralNetworkNextGrowth;
    }

    /**
     * Set the array of Neural Network predictions for the next growth.
     * @param neuralNetworkNextGrowth The new array of Neural Network predictions for the next growth.
     */
    public void setNeuralNetworkNextGrowth(Double[] neuralNetworkNextGrowth) {
        this.neuralNetworkNextGrowth = neuralNetworkNextGrowth;
    }

    /**
     * Calculates the growth made to reach this ExchangeRate's value from the parameter value.
     * @param previousValue The value to compare with this value to calculate the growth.
     */
    public void calculateGrowth(Double previousValue) {
        try {
            this.growth = ((this.value - previousValue) / previousValue) * 100;
        } catch (ArithmeticException e){
            System.out.println("[INFO] Growth from 0 to " + getValue() + " can't be calculated.");
            this.growth = null;
        } catch (Exception e){
            System.out.println("[INFO] Error: " + e);
        }
    }
    
    /**
     * Assesses whether this ExchangeRate is the minute after the parameter value.
     * @param rate The value to compare with this ExchangeRate.
     * @return true - this is the minute after parameter, false - it is not.
     */
    public boolean isMinuteAfter(ExchangeRate rate){
        if (ldtTimestamp != null && rate != null && rate.getLDTTimestamp() != null) {
            return ldtTimestamp.minusMinutes(1).isEqual(rate.getLDTTimestamp());
        }
        return false;
    }
    
    /**
     * Assesses whether this ExchangeRate is the same minute as the parameter value.
     * @param rate The value to compare with this ExchangeRate.
     * @return true - this is the same minute as parameter, false - it is not.
     */
    public boolean isSameMinute(ExchangeRate rate){
        if (ldtTimestamp != null && rate != null && rate.getLDTTimestamp() != null) {
            return ldtTimestamp.isEqual(rate.getLDTTimestamp());
        }
        return false;
    }

    /**
     * Used to compare ExchangeRates with each other for sorting. Not called 
     * except to sort ExchangeRates chronologically.
     * @param rate The rate to compare with this ExchangeRate.
     * @return The difference between this rate and the parameter ExchangeRate.
     */
    @Override
    public int compareTo(ExchangeRate rate) {
        return ldtTimestamp.compareTo(rate.getLDTTimestamp());
    }
}
