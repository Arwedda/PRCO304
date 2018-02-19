/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author jkell
 */
public class ExchangeRate {
    private LocalDateTime timestamp;
    private Double value;
    private Double growth;
    private String lastTrade;

    //private Double neuralNetworkNextGrowth;
    
    public ExchangeRate() {
        this.timestamp = null;
        this.value = 0.0;
        this.growth = null;
        this.lastTrade = null;
    }
    
    public ExchangeRate(LocalDateTime timestamp, Double value, String lastTrade) {
        this.timestamp = timestamp;
        this.value = value;
        this.lastTrade = lastTrade;
        this.growth = null;
    }

    public ExchangeRate(LocalDateTime timestamp, Double value, String lastTrade, Double growth) {
        this.timestamp = timestamp;
        this.value = value;
        this.lastTrade = lastTrade;
        this.growth = growth;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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

    public String getLastTrade() {
        return lastTrade;
    }

    public void setLastTrade(String lastTrade) {
        this.lastTrade = lastTrade;
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
    
    @Override
    public String toString(){
        return "timestamp=" + timestamp + " value=" + value + " lastTrade=" + lastTrade + " growth=" + growth;
    }
}
