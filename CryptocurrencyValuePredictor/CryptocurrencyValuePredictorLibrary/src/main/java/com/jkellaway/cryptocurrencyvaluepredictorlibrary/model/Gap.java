/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import java.time.LocalDateTime;

/**
 *
 * @author jkell
 */
public class Gap {
    private Integer paginationStart;
    private LocalDateTime startTime;
    private Integer ratesRequired;

    /**
     * Default Gap constructor.
     */
    public Gap() {
        paginationStart = null;
        startTime = null;
        ratesRequired = null;
    }
    
    /**
     * Standard Gap constructor.
     * @param paginationStart The most recent GDAX identifier adjacent to this Gap.
     * @param startTime The time associated with the most recent missing ExchangeRate.
     * @param ratesRequired The number of ExchangeRates required to fill this Gap.
     */
    public Gap(Integer paginationStart, LocalDateTime startTime, Integer ratesRequired) {
        this.paginationStart = paginationStart;
        this.startTime = startTime;
        this.ratesRequired = ratesRequired;
    }

    /**
     * Get the most recent GDAX identifier adjacent to this Gap.
     * @return The GDAX identifier associated with the start of this Gap.
     */
    public Integer getPaginationStart() {
        return paginationStart;
    }

    /**
     * Set a new most recent GDAX identifier adjacent to this Gap.
     * @param paginationStart The new GDAX identifier to be associated with the start of this Gap.
     */
    public void setPaginationStart(Integer paginationStart) {
        this.paginationStart = paginationStart;
    }

    /**
     * Get the time associated with the most recent missing ExchangeRate.
     * @return The LocalDateTime representation of the most recent missing ExchangeRate.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Set a new time to be associated with the most recent missing ExchangeRate.
     * @param startTime The new LocalDateTime timestamp.
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    /**
     * Get the number of ExchangeRates required to fill this Gap.
     * @return The quantity of ExchangeRates required to fill this Gap.
     */
    public Integer getRatesRequired() {
        return ratesRequired;
    }

    /**
     * Set the new number of ExchangeRates required to fill this Gap.
     * @param ratesRequired The new quantity of ExchangeRates to fill this Gap.
     */
    public void setRatesRequired(Integer ratesRequired) {
        this.ratesRequired = ratesRequired;
    }
}
