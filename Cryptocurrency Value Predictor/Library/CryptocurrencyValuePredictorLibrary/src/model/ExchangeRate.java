/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author jkell
 */
public class ExchangeRate {
    private LocalDateTime timestamp;
    private String value;

    public ExchangeRate() {
        this.timestamp = null;
        this.value = "unknown";
    }

    public ExchangeRate(LocalDateTime timestamp, String value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString(){
        return "timestamp=" + timestamp + " value=" + value;
    }
}
