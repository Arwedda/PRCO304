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

    public Gap() {
        paginationStart = null;
        startTime = null;
        ratesRequired = null;
    }
    
    public Gap(Integer paginationStart, LocalDateTime startTime, Integer ratesRequired) {
        this.paginationStart = paginationStart;
        this.startTime = startTime;
        this.ratesRequired = ratesRequired;
    }

    public Integer getPaginationStart() {
        return paginationStart;
    }

    public void setPaginationStart(Integer paginationStart) {
        this.paginationStart = paginationStart;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getRatesRequired() {
        return ratesRequired;
    }

    public void setRatesRequired(Integer ratesRequired) {
        this.ratesRequired = ratesRequired;
    }
}
