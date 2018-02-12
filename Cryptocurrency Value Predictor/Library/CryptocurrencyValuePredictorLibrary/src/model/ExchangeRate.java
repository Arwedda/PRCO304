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
    private double value;
    private double growth;
    public double[] GOFAINextGrowth = new double[20];

    public ExchangeRate() {
        this.timestamp = null;
        this.value = 0.0;
        this.growth = 0.0;
    }

    public ExchangeRate(LocalDateTime timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
        this.growth = 0.0;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Double getGrowth() {
        return growth;
    }

    public void calculateGrowth(double previousValue) { 
        this.growth = ((this.value - previousValue) / previousValue) * 100;
    }
    
    @Override
    public String toString(){
        return "timestamp=" + timestamp + " value=" + value + " growth=" + growth;
    }
}
