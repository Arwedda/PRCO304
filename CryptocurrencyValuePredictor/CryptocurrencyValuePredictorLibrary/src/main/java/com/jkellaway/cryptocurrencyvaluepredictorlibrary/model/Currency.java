/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
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
    private final String GDAXEndpoint;

    public Currency() {
        this.id = "unknown";
        this.name = "unknown";
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.gaps = new ArrayList<>();
        this.GDAXEndpoint = "unknown";
    }
    
    public Currency(String id, String name, String GDAXEndpoint) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.gaps = new ArrayList<>();
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
    
    public void gradualMerge(){
        this.rates.addAll(0, historicRates);
        Gap gap = getLastGap();
        gap.setRatesRequired(gap.getRatesRequired() - historicRates.size());
        if (gap.getRatesRequired() < 1){
            gaps.remove(gap);
            historicTrades.clear();
        } else {
            gap.setStartTime(gap.getStartTime().minusMinutes(historicRates.size()));
            gap.setPaginationStart(gap.getPaginationStart() - 100);
        }
        historicRates.clear();
    }
    
    public boolean dumpDuplicates(){
        List<ExchangeRate> toRemove = new ArrayList<>();
        for (ExchangeRate rate : rates){
            for (ExchangeRate rate2 : historicRates) {
                if (rate.isSameMinute(rate2)){
                    toRemove.add(rate2);
                }
            }
        }
        historicRates.removeAll(toRemove);
        if (historicRates.isEmpty()){
            if (historicTrades.isEmpty()){
                getLastGap().setPaginationStart(getLastGap().getPaginationStart() - 100);
                return false;
            }
            if (historicTrades.get(0).getTime().isBefore(getLastGap().getStartTime().minusMinutes(historicRates.size() + 1))){
                historicTrades.clear();
                return true;
            }
            getLastGap().setPaginationStart(getLastHistoricTrade().getTrade_id());
        }
        return false;
    }
    
    public void findGaps(LocalDateTime firstMinute) {
        if (0 < gaps.size()) {
            gaps.clear();
        }
        int ratesRequired = 0;
        int timeBetween;
        ExchangeRate[] reqRates = new ExchangeRate[Globals.READINGSREQUIRED];
        Gap gap;
        
        List<ExchangeRate> gatheredRates = new ArrayList<>();
        gatheredRates.addAll(historicRates);
        gatheredRates.addAll(rates);

        for (ExchangeRate rate : gatheredRates) {
            try {
                timeBetween = (int) ChronoUnit.MINUTES.between(firstMinute, rate.getLDTTimestamp());
                reqRates[timeBetween] = rate;
            } catch (Exception e) {
                /*
                Prices outside of relevant price range
                System.out.println(e);
                */
            }
        }
        
        for (ExchangeRate reqRate : reqRates) {
            if (reqRate != null && reqRate.getCurrency_id().equals(getID())) {
                if (0 < ratesRequired){
                    gap = new Gap(reqRate.getLastTrade(), reqRate.getLDTTimestamp().minusMinutes(1), ratesRequired);
                    gaps.add(gap);
                    ratesRequired = 0;
                }
            } else {
                ratesRequired++;
            }
        }
        gap = new Gap(0, LocalDateTimeHelper.startOfMinute(LocalDateTime.now()), ratesRequired);
        gaps.add(gap);
    }
    
    public void mergeRates(){        
        this.rates.addAll(0, historicRates);
        Collections.sort(rates);
        historicRates.clear();
    }
    
    public ExchangeRate[] calculateGrowth(boolean calculateChange){
        List<ExchangeRate> updatedRates = new ArrayList<>();
        for (int i = 1; i < rates.size(); i++){
            if (rates.get(i).isMinuteAfter(rates.get(i - 1))){
                if (rates.get(i).getGrowth() == null) {
                    rates.get(i).calculateGrowth(rates.get(i - 1).getValue());
                    updatedRates.add(rates.get(i));
                }
            } else if (calculateChange) {
                fillMissingData(i);
            }
        }
        return SafeCastHelper.objectsToExchangeRates(updatedRates.toArray());
    }
    
    private void fillMissingData(int i) {
        ExchangeRate previous = rates.get(i - 1);
        ExchangeRate next = rates.get(i);
        ExchangeRate rate = null;
        int gapsSize = (int) ChronoUnit.MINUTES.between(previous.getLDTTimestamp(), next.getLDTTimestamp()) - 1;
        double change = 0.0;
        
        if (next.getValue() != 0.0 && previous.getValue() != 0.0){
            change = next.getValue() - previous.getValue();
        }
        
        change /= (gapsSize + 1);
        
        for (int j = 1; j <= gapsSize; j++){
            rate = new ExchangeRate(id, previous.getLDTTimestamp().plusMinutes(j), previous.getValue() + change, null, null, null, next.getLastTrade());
            historicRates.add(rate);
        }
    }
    
    public int noOfHistoricRates(){
        return this.historicRates.size();
    }
}
