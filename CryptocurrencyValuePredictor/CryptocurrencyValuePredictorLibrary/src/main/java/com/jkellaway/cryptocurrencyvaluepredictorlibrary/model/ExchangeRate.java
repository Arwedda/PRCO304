/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

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
    private Double gofaiNextGrowth;
    private Double neuralNetworkNextGrowth;
    private Integer lastTrade;
    private transient LocalDateTime ldtTimestamp;
    
    public Double[] gofaiGrowth = new Double[20];
    
    public ExchangeRate() {
        this.currency_id = "Unknown";
        this.timestamp = "Unknown";
        this.value = 0.0;
        this.lastTrade = null;
        this.growth = null;
        this.gofaiNextGrowth = null;
        this.neuralNetworkNextGrowth = null;
        this.ldtTimestamp = null;
    }

    public ExchangeRate(String currency_id, LocalDateTime timestamp, Double value, Double growth, Double gofaiNextGrowth, Double neuralNetworkNextGrowth, Integer lastTrade) {
        this.currency_id = currency_id;
        this.timestamp = timestamp.toString();
        this.value = value;
        this.growth = growth;
        this.gofaiNextGrowth = gofaiNextGrowth;
        this.neuralNetworkNextGrowth = neuralNetworkNextGrowth;
        this.lastTrade = lastTrade;
        this.ldtTimestamp = timestamp;
    }

    public ExchangeRate(String currency_id, String timestamp, Double value, Double growth, Double gofaiNextGrowth, Double neuralNetworkNextGrowth, Integer lastTrade) {
        this.currency_id = currency_id;
        this.timestamp = timestamp;
        this.value = value;
        this.growth = growth;
        this.gofaiNextGrowth = gofaiNextGrowth;
        this.neuralNetworkNextGrowth = neuralNetworkNextGrowth;
        this.lastTrade = lastTrade;
        this.ldtTimestamp = LocalDateTimeHelper.localDateTimeParser(timestamp);
    }
    
    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        this.ldtTimestamp = LocalDateTimeHelper.localDateTimeParser(timestamp);
    }

    public LocalDateTime getLDTTimestamp() {
        return ldtTimestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.ldtTimestamp = timestamp;
        this.timestamp = timestamp.toString();
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getGrowth() {
        return growth;
    }

    public Integer getLastTrade() {
        return lastTrade;
    }

    public void setLastTrade(Integer lastTrade) {
        this.lastTrade = lastTrade;
    }

    public Double getGofaiNextGrowth() {
        return gofaiNextGrowth;
    }

    public void setGofaiNextGrowth(Double gofaiNextGrowth) {
        this.gofaiNextGrowth = gofaiNextGrowth;
    }

    public Double getNeuralNetworkNextGrowth() {
        return neuralNetworkNextGrowth;
    }

    public void setNeuralNetworkNextGrowth(Double neuralNetworkNextGrowth) {
        this.neuralNetworkNextGrowth = neuralNetworkNextGrowth;
    }

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
    
    public boolean isMinuteAfter(ExchangeRate rate){
        return ldtTimestamp.minusMinutes(1).isEqual(rate.getLDTTimestamp());
    }
    
    public boolean isSameMinute(ExchangeRate rate){
        return ldtTimestamp.isEqual(rate.getLDTTimestamp());
    }
    
    @Override
    public String toString(){
        return "timestamp=" + timestamp + " value=" + value + " lastTrade=" + lastTrade + " growth=" + growth;
    }

    @Override
    public int compareTo(ExchangeRate rate) {
        return ldtTimestamp.compareTo(rate.getLDTTimestamp());
    }
}
