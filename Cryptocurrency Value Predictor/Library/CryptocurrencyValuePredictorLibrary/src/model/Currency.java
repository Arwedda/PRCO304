/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controllers.APIController;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import utilities.Helpers;
import utilities.JsonParser;

/**
 *
 * @author jkell
 */
public class Currency {
    private String id;
    private String name;
    private ArrayList<ExchangeRate> rates;
    private ArrayList<ExchangeRate> historicRates;
    private String lastTrade;
    private boolean calculatingGOFAI;
    private final String GDAXEndpoint;

    public Currency() {
        this.id = "unknown";
        this.name = "unknown";
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.lastTrade = "";
        this.calculatingGOFAI = false;
        this.GDAXEndpoint = "unknown";
    }
    
    public Currency(String id, String name, String GDAXEndpoint) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.lastTrade = "";
        this.calculatingGOFAI = false;
        this.GDAXEndpoint = GDAXEndpoint;
    }
    
    public Currency(String id, String name, ExchangeRate rate, String GDAXEndpoint) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.rates.add(rate);
        this.historicRates = new ArrayList<>();
        this.lastTrade = "";
        this.calculatingGOFAI = false;
        this.GDAXEndpoint = GDAXEndpoint;
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

    public ArrayList<ExchangeRate> getHistoricRates() {
        return historicRates;
    }

    public void addHistoricRate(ExchangeRate rate) {
        this.historicRates.add(rate);
    }
    
    private String getLastTrade() {
        return lastTrade;
    }

    private void setLastTrade(String lastTrade) {
        this.lastTrade = lastTrade;
    }
    
    public boolean isCalculatingGOFAI() {
        return calculatingGOFAI;
    }

    private void setCalculatingGOFAI() {
        this.calculatingGOFAI = true;
    }
    
    public void getPrice(APIController apiController, JsonParser parser){
        ExchangeRate rate;
        LocalDateTime postTime;
        try {
            postTime = getRate().getTimestamp();
        } catch (Exception e){
            postTime = Helpers.startOfMinute(LocalDateTime.now());
        }
        String json = apiController.getJSONString(apiController.getBCH_TRADES());
        Object[] locRates = parser.fromJSON(json);
        Double avgPrice = calculateAveragePrice(locRates, postTime);
        rate = new ExchangeRate(postTime, avgPrice);
        setValue(rate);
    }
    
    private Double calculateAveragePrice(Object[] trades, LocalDateTime postTime) {
        Double avgPrice = 0.0;
        int tradeNo = 0;
        boolean foundStart = false;

        for (Object trade : trades){
            if (((GDAXTrade)trade).getTime().equals(postTime.minusMinutes(1))) {
            avgPrice += ((GDAXTrade)trade).getPrice();
            tradeNo += 1;
            foundStart = true;
            } else if (foundStart) break;
        }

        /*
            POST TO API
        */
        
        return (avgPrice /= tradeNo);
    }
    
    public void calculateRecentAverages(APIController apiController, JsonParser parser) {
        ArrayList<Object> currentTrades = new ArrayList<>();
        ExchangeRate rate;
        Double avgPrice;
        boolean foundStart = false;
        String pagination = "";
        LocalDateTime tradeTime;
        String json;
        Object[] trades;
        
        if (getLastTrade().equals("")) {
            tradeTime = Helpers.startOfMinute(LocalDateTime.now());
            json = apiController.getJSONString(GDAXEndpoint);
            trades = parser.fromJSON(json);
            System.out.println("[INFO] Restarting prices at " + tradeTime.getHour() + ":" + tradeTime.getMinute());
        } else {
            pagination = "?after=" + getLastTrade();
            json = apiController.getJSONString(GDAXEndpoint + pagination);
            trades = parser.fromJSON(json);
            tradeTime = ((GDAXTrade)trades[0]).getTime();
        }
        
        if (json.equals("")) return;
        
        Object[] currTrades;
        for (Object trade : trades){
            if (((GDAXTrade)trade).getTime().equals(tradeTime.minusMinutes(1))) {
                currentTrades.add(trade);
                foundStart = true;
            } else if (foundStart) {
                setLastTrade(((GDAXTrade)currentTrades.get(currentTrades.size() - 1)).getTrade_id());
                currTrades = currentTrades.toArray();
                currentTrades.clear();
                avgPrice = calculateAveragePrice(currTrades, tradeTime);
                rate = new ExchangeRate(tradeTime, avgPrice);
                addHistoricRate(rate);
                System.out.println("[INFO] Posting trade for: " + tradeTime.getHour() + ":" + tradeTime.plusMinutes(1).getMinute());
                tradeTime = tradeTime.minusMinutes(1);
            }
        }
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
            if (this.rates.get(i).getGrowth() == 0.0) {
                this.rates.get(i).calculateGrowth(this.rates.get(i - 1).getValue());
            } else {
                break;
            }
        }
    }
    
    public int getNumberOfRatesCollected(){
        return this.rates.size() + this.historicRates.size();
    }
    
    @Override
    public String toString(){
        return "id=" + id + " name=" + name + " value=" + rates.get(rates.size() - 1).toString();
    }
}
