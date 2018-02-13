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
    private String id; 

    public GDAXTrade() {
        this.time = null;
        this.price = 0.0;
        this.id = "0";
    }
    
    public GDAXTrade(LocalDateTime time, Double price, String trade_id) {
        this.time = time;
        this.price = price;
        this.id = trade_id;
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

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
    
    public boolean tradesMatch(GDAXTrade trade){
        return Helpers.stringsMatch(this.id, trade.getID(), this.id.length());
    }
}
