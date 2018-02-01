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
    private ArrayList<ExchangeRate> arlValues;

    public Currency() {
        this.id = "unknown";
        this.name = "unknown";
        this.arlValues = new ArrayList<>();
    }
    
    public Currency(String id, String name) {
        this.id = id;
        this.name = name;
        this.arlValues = new ArrayList<>();
    }
    
    public Currency(String id, String name, ExchangeRate rate) {
        this.id = id;
        this.name = name;
        this.arlValues = new ArrayList<>();
        this.arlValues.add(rate);
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
    
    public ExchangeRate getValue(){
        return arlValues.get(arlValues.size() - 1);
    }
    
    public void setValue(ExchangeRate rate){
        this.arlValues.add(rate);
    }
    
    public ArrayList<ExchangeRate> getRates() {
        return this.arlValues;
    }
    
    @Override
    public String toString(){
        return "id=" + id + " name=" + name + " value=" + arlValues.get(arlValues.size() - 1).toString();
    }
}
