/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.SafeCastHelper;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author jkell
 */
public class Currency {
    private String id;
    private String name;
    private transient List<ExchangeRate> rates;
    private transient List<ExchangeRate> historicRates;
    private transient List<GDAXTrade> historicTrades;
    private transient List<Gap> gaps;
    private transient boolean calculatingGOFAI;
    private transient boolean calculatingNN;
    private final String GDAXEndpoint;

    public Currency() {
        this.id = "unknown";
        this.name = "unknown";
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.gaps = new ArrayList<>();
        this.calculatingGOFAI = false;
        this.calculatingNN = false;
        this.GDAXEndpoint = "unknown";
    }
    
    public Currency(String id, String name, String GDAXEndpoint) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.gaps = new ArrayList<>();
        this.calculatingGOFAI = false;
        this.calculatingNN = false;
        this.GDAXEndpoint = GDAXEndpoint;
    }
    
    public Currency(String id, String name, ExchangeRate rate, String GDAXEndpoint) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.rates.add(rate);
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.gaps = new ArrayList<>();
        this.calculatingGOFAI = false;
        this.calculatingNN = false;
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
                rate.setValue(getRate().getValue());
            }
            if (rate.isMinuteAfter(getRate())){
                rate.calculateGrowth(getRate().getValue());
            }
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
    
    public List<ExchangeRate> getRates() {
        return this.rates;
    }

    public List<ExchangeRate> getHistoricRates() {
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

    public boolean isCalculatingNN() {
        return calculatingNN;
    }

    private void setCalculatingNN() {
        this.calculatingNN = true;
    }
    
    public String getGDAXEndpoint() {
        return GDAXEndpoint;
    }

    public List<GDAXTrade> getHistoricTrades() {
        return historicTrades;
    }

    public void addHistoricTrade(GDAXTrade historicTrade) {
        this.historicTrades.add(historicTrade);
    }
    
    public List<Gap> getGaps(){
        return gaps;
    }
    
    public Gap getLastGap(){
        return gaps.get(gaps.size() - 1);
    }

    public void initialMerge(LocalDateTime firstRelevantRate){
        findGaps(firstRelevantRate);
        mergeRates();
    }
    
    public void GOFAIMerge(){
        this.calculatingGOFAI = true;
        Collections.reverse(historicRates);
        mergeRates();
    }
    
    public void NNMerge(){
        this.calculatingNN = true;
        Collections.reverse(historicRates);
        mergeRates();
    }
    
    public void merge(){
        Collections.reverse(historicRates);
        mergeRates();
        if (0 < gaps.size()){
            gaps.remove(getLastGap());
        }
    }
    
    public void dumpDuplicates(){
        List<ExchangeRate> toRemove = new ArrayList<>();
        for (ExchangeRate rate : getRates()){
            for (ExchangeRate rate2 : getHistoricRates()) {
                if (rate.isSameMinute(rate2)){
                    System.out.println("[INFO] Two prices from same time: " + rate.toString() + " & " + rate2.toString());
                    toRemove.add(rate2);
                }
            }
        }
        historicRates.removeAll(toRemove);
    }
    
    private void findGaps(LocalDateTime firstMinute) {
        int ratesRequired = 0;
        int timeBetween;
        ExchangeRate[] reqRates = new ExchangeRate[Globals.READINGSREQUIRED];
        Gap gap;

        for (ExchangeRate rate : historicRates) {
            timeBetween = (int) ChronoUnit.MINUTES.between(firstMinute, rate.getLDTTimestamp());
            reqRates[timeBetween] = rate;
        }
        
        for (ExchangeRate reqRate : reqRates) {
            if (reqRate != null && reqRate.getCurrency_id().equals(getID())) {
                if (0 < ratesRequired){
                    gap = new Gap(reqRate.getLastTrade(), ratesRequired);
                    gaps.add(gap);
                    ratesRequired = 0;
                }
            } else {
                ratesRequired++;
            }
        }
        gap = new Gap(0, ratesRequired);
        gaps.add(gap);
    }
    
    private void mergeRates(){        
        this.rates.addAll(0, historicRates);
        Collections.sort(rates);
        historicRates.clear();
    }
    
    public ExchangeRate[] calculateGrowth(){
        List<ExchangeRate> updatedRates = new ArrayList<>();
        for (int i = 1; i < rates.size(); i++){
            if (null == rates.get(i).getGrowth()) {
                if (rates.get(i).isMinuteAfter(rates.get(i - 1))){
                    rates.get(i).calculateGrowth(rates.get(i - 1).getValue());
                    updatedRates.add(rates.get(i));
                }
            }
        }
        return SafeCastHelper.objectsToExchangeRates(updatedRates.toArray());
    }
    
    public int noOfHistoricRates(){
        return this.historicRates.size();
    }
    
    @Override
    public String toString(){
        return "id=" + id + " name=" + name + " value=" + rates.get(rates.size() - 1).toString();
    }
}
