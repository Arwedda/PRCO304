/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import java.time.Clock;
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

    /**
     * Default currency constructor.
     */
    public Currency() {
        this.id = "unknown";
        this.name = "unknown";
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.gaps = new ArrayList<>();
        this.GDAXEndpoint = "unknown";
    }
    
    /**
     * Standard currency constructor.
     * @param id The identifier.
     * @param name The name.
     * @param GDAXEndpoint The GDAX API endpoint.
     */
    public Currency(String id, String name, String GDAXEndpoint) {
        this.id = id;
        this.name = name;
        this.rates = new ArrayList<>();
        this.historicRates = new ArrayList<>();
        this.historicTrades = new ArrayList<>();
        this.gaps = new ArrayList<>();
        this.GDAXEndpoint = GDAXEndpoint;
    }
    
    /**
     * Currency constructor for currency with a current ExchangeRate.
     * @param id The identifier.
     * @param name The name.
     * @param rate The current ExchangeRate.
     * @param GDAXEndpoint The GDAX API endpoint.
     */
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

    /**
     * The globally recognised identifier (normally 3 characters).
     * @return The identifier.
     */
    public String getID() {
        return id;
    }

    /**
     * Adjust the globally recognised identifier.
     * @param id The new identifier.
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * The globally recognised name.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Adjust the globally recognised name.
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the last ExchangeRate added to the currency's current rates list.
     * @return The current ExchangeRate.
     */
    public ExchangeRate getRate(){
        try {
            return rates.get(rates.size() - 1);
        } catch (Exception e){
            return null;
        }
    }
    
    /**
     * Sets the new ExchangeRate value, calculating growth if possible.
     * @param rate The new ExchangeRate.
     */
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
    
    /**
     * Gets the list of current ExchangeRates stored for this Currency.
     * @return The List of current ExchangeRates stored for this Currency.
     */
    public List<ExchangeRate> getRates() {
        return this.rates;
    }

    /**
     * Gets the list of historic ExchangeRates stored for this Currency.
     * @return The List of historic ExchangeRates stored for this Currency.
     */
    public List<ExchangeRate> getHistoricRates() {
        return historicRates;
    }

    /**
     * Adds a new historic ExchangeRate to the Currency. Clears historic GDAXTrades
     * used to calculate the ExchangeRate.
     * @param rate The newly calculated historic ExchangeRate.
     */
    public void addHistoricRate(ExchangeRate rate) {
        if (rate.getValue() < 0.0000000000000001 && null != getHistoricRate()){
            rate.setValue(getHistoricRate().getValue());
        }
        this.historicRates.add(rate);
        this.historicTrades.clear();
    }
    
    /**
     * Gets the last added historic ExchangeRate.
     * @return The last added historic ExchangeRate.
     */
    private ExchangeRate getHistoricRate(){
        try {
            return historicRates.get(historicRates.size() - 1);
        } catch (Exception e){
            return null;
        }
    }
    
    /**
     * Gets the last added historic GDAXTrade.
     * @return The last added historic GDAXTrade.
     */
    public GDAXTrade getLastHistoricTrade() {
        try {
            return historicTrades.get(historicTrades.size() - 1);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Calculates whether this Currency has found where it should begin harvesting
     * historic GDAXTrades to create historic ExchangeRates.
     * @return true - has, false - has not.
     */
    public boolean hasFoundPosition() {
        return (0 < this.historicRates.size() || 0 < this.historicTrades.size());
    }
    
    /**
     * Gets the GDAX API endpoint for this Currency.
     * @return The GDAX API endpoint for this Currency.
     */
    public String getGDAXEndpoint() {
        return GDAXEndpoint;
    }

    /**
     * Gets the List of historic GDAXTrades that will be used to create the next
     * historic ExchangeRate.
     * @return The current List of historic GDAXTrades.
     */
    public List<GDAXTrade> getHistoricTrades() {
        return historicTrades;
    }

    /**
     * Adds a GDAXTrade to the List to create the next historic ExchangeRate.
     * @param historicTrade GDAXTrade to be included in the next ExchangeRate.
     */
    public void addHistoricTrade(GDAXTrade historicTrade) {
        this.historicTrades.add(historicTrade);
    }
    
    /**
     * Gets the list of all Gaps to be filled by the historic ExchangeRate
     * PriceCollector.
     * @return The list of Gaps to be filled.
     */
    public List<Gap> getGaps(){
        return gaps;
    }
    
    /**
     * The last (most recent) Gap in ExchangeRates that needs to be filled.
     * @return The current Gap to fill.
     */
    public Gap getLastGap(){
        try {
            return gaps.get(gaps.size() - 1);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Merges historic ExchangeRates into the current ExchangeRate list. They are
     * placed at the beginning of the list and not sorted. The Gap is adjusted to
     * account for the ExchangeRates being removed from the historic List.
     */
    public void gradualMerge(){
        this.rates.addAll(0, historicRates);
        Gap gap = getLastGap();
        if (gap != null) {
            gap.setRatesRequired(gap.getRatesRequired() - historicRates.size());
            if (gap.getRatesRequired() < 1){
                gaps.remove(gap);
                historicTrades.clear();
            } else {
                gap.setStartTime(gap.getStartTime().minusMinutes(historicRates.size()));
                gap.setPaginationStart(gap.getPaginationStart() - 100);
            }
        }
        historicRates.clear();
    }
    
    /**
     * Removes any historic ExchangeRates that clash with ExchangeRates that have
     * already been harvested. Pass parameter true specify that the current Gap 
     * time limit has been passed (to not waste an API call on a finished Gap).
     * @param passedGap Whether the Gap has been passed or not.
     * @return true - no more useful data to collect, false - data worth harvesting
     */
    public boolean dumpDuplicates(boolean passedGap){
        List<ExchangeRate> toRemove = new ArrayList<>();
        for (ExchangeRate rate : rates){
            for (ExchangeRate rate2 : historicRates) {
                if (rate.isSameMinute(rate2)){
                    toRemove.add(rate2);
                }
            }
        }
        historicRates.removeAll(toRemove);
        
        if (historicRates.isEmpty()) {
            if (historicTrades.isEmpty() && passedGap) {
                return true;
            }
            Gap gap = getLastGap();
            if (gap != null) {
                gap.setPaginationStart(getLastGap().getPaginationStart() - 100);
            }
        }
        return false;
    }
    
    public void findGaps(LocalDateTime firstMinute) {
        if (0 < gaps.size()) {
            gaps.clear();
        }
        int ratesRequired = 0;
        int timeBetween;
        ExchangeRate[] reqRates = new ExchangeRate[(int) ChronoUnit.MINUTES.between(firstMinute, LocalDateTime.now(Clock.systemUTC()))];
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
        gap = new Gap(0, LocalDateTimeHelper.startOfMinute(LocalDateTime.now(Clock.systemUTC())), ratesRequired);
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
        return updatedRates.toArray(new ExchangeRate[updatedRates.size()]);
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
    
    /**
     * Gets the number of historic ExchangeRates stored for this Currency.
     * @return Integer value of historic ExchangeRates stored for this Currency.
     */
    public int noOfHistoricRates(){
        return this.historicRates.size();
    }
}
