/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

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
            rate.calculateGrowth(Double.parseDouble(this.getRate().getValue()));
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

    public void setCalculatingGOFAI(boolean calculatingGOFAI) {
        this.calculatingGOFAI = calculatingGOFAI;
    }

    @Override
    public String toString(){
        return "id=" + id + " name=" + name + " value=" + rates.get(rates.size() - 1).toString();
    }
}
