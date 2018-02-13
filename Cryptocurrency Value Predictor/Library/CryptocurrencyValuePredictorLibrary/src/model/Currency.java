/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author jkell
 */
public class Currency {
    private String id;
    private String name;
    private ArrayList<ExchangeRate> rates;
    private ArrayList<ExchangeRate> historicRates;
    private ArrayList<GDAXTrade> historicTrades;
    private boolean calculatingGOFAI;
    private final String GDAXEndpoint;

    public Currency() {
        this.id = "unknown";
        this.name = "unknown";
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.calculatingGOFAI = false;
        this.GDAXEndpoint = "unknown";
    }
    
    public Currency(String id, String name, String GDAXEndpoint) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.calculatingGOFAI = false;
        this.GDAXEndpoint = GDAXEndpoint;
    }
    
    public Currency(String id, String name, ExchangeRate rate, String GDAXEndpoint) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.rates.add(rate);
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.calculatingGOFAI = false;
        this.GDAXEndpoint = GDAXEndpoint;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public ExchangeRate getRate(){
        try {
            return rates.get(rates.size() - 1);
        } catch (Exception e){
            return null;
        }
    }
    
    public void setValue(ExchangeRate rate){
        try {
            if (rate.getValue() < 0.0000000000000001){
                rate.setValue(this.getRate().getValue());
            }
            rate.calculateGrowth(this.getRate().getValue());
        } catch (NullPointerException npe) {
            if (rates.isEmpty()){
                System.out.println("[INFO] Error: " + npe + " (expected - adding a value that doesn't have a predecessor.)");
            } else {
                System.out.println("[INFO] Error: " + npe + "! Does have predecessor to calculate growth! ERROR!");
            }
        } catch (Exception e){
            System.out.println("[INFO] Error: " + e);
        }
        this.rates.add(rate);
    }
    
    public ArrayList<ExchangeRate> getRates() {
        return this.rates;
    }

    public ArrayList<ExchangeRate> getHistoricRates() {
        return historicRates;
    }

    public void addHistoricRate(ExchangeRate rate) {
        if (rate.getValue() < 0.0000000000000001){
            rate.setValue(getHistoricRate().getValue());
        }
        this.historicRates.add(rate);
        this.historicTrades.clear();
    }
    
    private ExchangeRate getHistoricRate(){
        try {
            return historicRates.get(historicRates.size() - 1);
        } catch (Exception e){
            return null;
        }
    }
    
    public GDAXTrade getLastHistoricTrade() {
        try {
            return historicTrades.get(historicTrades.size() - 1);
        } catch (Exception e){
            return null;
        }
    }

    public boolean hasFoundPosition() {
        return (0 < this.historicRates.size() || 0 < this.historicTrades.size());
    }
    
    public boolean isCalculatingGOFAI() {
        return calculatingGOFAI;
    }

    private void setCalculatingGOFAI() {
        this.calculatingGOFAI = true;
    }

    public String getGDAXEndpoint() {
        return GDAXEndpoint;
    }

    public ArrayList<GDAXTrade> getHistoricTrades() {
        return historicTrades;
    }

    public void addHistoricTrade(GDAXTrade historicTrade) {
        this.historicTrades.add(historicTrade);
    }
    
    public void mergePrices(){
        Collections.reverse(historicRates);
        
        /*
        Debugging - ensure no 2 prices are entered for the same time
        */
        for (ExchangeRate rate : getRates()){
            for (ExchangeRate rate2 : getHistoricRates()) {
                if (rate.getTimestamp().equals(rate2.getTimestamp())){
                    System.out.println("[INFO] Two prices from same time: " + rate.toString() + " & " + rate2.toString());
                }
            }
        }
        
        this.rates.addAll(0, historicRates);
        calculateGrowth();
        historicRates.clear();
        if (!isCalculatingGOFAI()){
            setCalculatingGOFAI();
        }
    }
    
    private void calculateGrowth(){
        for (int i = 1; i < this.rates.size(); i++){
            if (null == this.rates.get(i).getGrowth()) {
                this.rates.get(i).calculateGrowth(this.rates.get(i - 1).getValue());
            }/* else {
                break;
            }*/
        }
    }
    
    public int getNumberOfRatesCollected(){
        return (this.rates.size() + this.historicRates.size());
    }
    
    @Override
    public String toString(){
        return "id=" + id + " name=" + name + " value=" + rates.get(rates.size() - 1).toString();
    }
}
