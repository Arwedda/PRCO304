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
    private boolean calculatingGOFAI;

    public Currency() {
        this.id = "unknown";
        this.name = "unknown";
        this.rates = new ArrayList<>();
        this.calculatingGOFAI = false;
    }
    
    public Currency(String id, String name) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.calculatingGOFAI = false;
    }
    
    public Currency(String id, String name, ExchangeRate rate) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.rates.add(rate);
        this.calculatingGOFAI = false;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public ExchangeRate getRate(){
        return rates.get(rates.size() - 1);
    }
    
    public void setValue(ExchangeRate rate){
        try {
            rate.calculateGrowth(this.getRate().getValue());
        } catch (Exception e) {
            System.out.println("[INFO] Error: " + e);
        }
        this.rates.add(rate);
    }
    
    public ArrayList<ExchangeRate> getRates() {
        return this.rates;
    }

    public boolean isCalculatingGOFAI() {
        return calculatingGOFAI;
    }

    private void setCalculatingGOFAI() {
        this.calculatingGOFAI = true;
    }

    public void mergePrices(ArrayList<ExchangeRate> historicPrices){
        Collections.reverse(historicPrices);
        
        /*
        Debugging - ensure no 2 prices are entered for the same time
        */
        for (ExchangeRate rate : getRates()){
            for (ExchangeRate rate2 : historicPrices) {
                if (rate.getTimestamp().equals(rate2.getTimestamp())){
                    System.out.println("[INFO] Two prices from same time: " + rate.toString() + " & " + rate2.toString());
                }
            }
        }
        
        this.rates.addAll(0, historicPrices);
        calculateGrowth();
        historicPrices.clear();
        if (!isCalculatingGOFAI()){
            setCalculatingGOFAI();
        }
    }
    
    private void calculateGrowth(){
        for (int i = 1; i < this.rates.size(); i++){
            if (this.rates.get(i).getGrowth() == 0.0) {
                this.rates.get(i).calculateGrowth(this.rates.get(i - 1).getValue());
            } else {
                break;
            }
        }
    }
    
    @Override
    public String toString(){
        return "id=" + id + " name=" + name + " value=" + rates.get(rates.size() - 1).toString();
    }
}
