/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author jkell
 */
public class GDAXTrade {
    private String time;
    private String price;
    private String trade_id; 

    public GDAXTrade() {
        this.time = "unknown";
        this.price = "unknown";
        this.trade_id = "0";
    }
    
    public GDAXTrade(String time, String price, String trade_id) {
        this.time = time;
        this.price = price;
        this.trade_id = trade_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }
}
