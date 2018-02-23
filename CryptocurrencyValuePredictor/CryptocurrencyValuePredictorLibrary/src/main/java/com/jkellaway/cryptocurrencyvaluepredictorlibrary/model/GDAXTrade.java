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

    public GDAXTrade() {
        this.time = null;
        this.price = 0.0;
        this.trade_id = null;
    }
    
    public GDAXTrade(LocalDateTime time, Double price, Integer trade_id) {
        this.time = time;
        this.price = price;
        this.trade_id = trade_id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(Integer trade_id) {
        this.trade_id = trade_id;
    }
    
    public boolean tradesMatch(GDAXTrade trade){
        return Objects.equals(this.trade_id, trade.getTrade_id());
    }
}
