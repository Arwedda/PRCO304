/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;
import utilities.Helpers;

/**
 *
 * @author jkell
 */
public class GDAXTrade {
    private LocalDateTime time;
    private Double price;
    private String trade_id; 

    public GDAXTrade() {
        this.time = null;
        this.price = 0.0;
        this.trade_id = "0";
    }
    
    public GDAXTrade(LocalDateTime time, Double price, String trade_id) {
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

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }
    
    public boolean tradesMatch(GDAXTrade trade){
        return Helpers.stringsMatch(this.trade_id, trade.getTrade_id(), this.trade_id.length());
    }
}
