/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jkell
 */
public class LocalDateTimeHelper {
    public static LocalDateTime startOfMinute(LocalDateTime timestamp){
        return timestamp.minusSeconds(timestamp.getSecond()).minusNanos(timestamp.getNano());
    }
    
    public static LocalDateTime localDateTimeParser(String timestamp){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, format);
        return localDateTime;
    }
}
