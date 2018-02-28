/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

/**
 *
 * @author jkell
 */
public class Gap {
    private Integer paginationStart;
    private int ratesRequired;

    public Gap() {
    }
    
    public Gap(Integer paginationStart, int ratesRequired) {
        this.paginationStart = paginationStart;
        this.ratesRequired = ratesRequired;
    }

    public Integer getPaginationStart() {
        return paginationStart;
    }

    public void setPaginationStart(Integer paginationStart) {
        this.paginationStart = paginationStart;
    }

    public int getRatesRequired() {
        return ratesRequired;
    }

    public void setRatesRequired(int ratesRequired) {
        this.ratesRequired = ratesRequired;
    }
}
