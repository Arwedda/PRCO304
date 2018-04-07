/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author jkell
 */
public class GDAXTrade {
    private LocalDateTime time;
    private Double price;
    private Integer trade_id; 

    /**
     * Default GDAXTrade constructor.
     */
    public GDAXTrade() {
        this.time = null;
        this.price = 0.0;
        this.trade_id = null;
    }
    
    /**
     * Standard GDAXTrade constructor.
     * @param time The timestamp associated with this GDAXTrade.
     * @param price The USD value of this GDAXTrade.
     * @param trade_id The GDAX identifier of this GDAXTrade.
     */
    public GDAXTrade(LocalDateTime time, Double price, Integer trade_id) {
        this.time = time;
        this.price = price;
        this.trade_id = trade_id;
    }

    /**
     * Get the time associated with this GDAXTrade.
     * @return The LocalDateTime representation of this GDAXTrade's timestamp.
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Set a new time to be associated with this GDAXTrade using a LocalDateTime.
     * @param time The new time.
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * Get the USD value of this GDAXTrade.
     * @return The Double USD value of this GDAXTrade.
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Set a new USD value for this GDAXTrade.
     * @param price The new price.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Get the GDAX identifier of this GDAXTrade.
     * @return The GDAXTrade identifier.
     */
    public Integer getTrade_id() {
        return trade_id;
    }

    /**
     * Set a new GDAX identifier for this GDAXTrade.
     * @param trade_id The new GDAX identifier.
     */
    public void setTrade_id(Integer trade_id) {
        this.trade_id = trade_id;
    }
    
    /**
     * Assesses whether this GDAXTrade is the same as the parameter using their
     * trade_id fields.
     * @param trade The GDAXTrade to compare with this one.
     * @return true - match, false - no match.
     */
    public boolean tradesMatch(GDAXTrade trade){
        return Objects.equals(this.trade_id, trade.getTrade_id());
    }
}
