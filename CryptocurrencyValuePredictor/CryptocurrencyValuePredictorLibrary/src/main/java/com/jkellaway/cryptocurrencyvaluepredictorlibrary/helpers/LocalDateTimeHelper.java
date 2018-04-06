/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author jkell
 */
public class LocalDateTimeHelper {
    
    /**
     * Crops a timestamp of any seconds and nanoseconds (to make timestamps the 
     * start of each minute).
     * @param timestamp LocalDateTime timestamp to crop.
     * @return The cropped LocalDateTime timestamp.
     */
    public static LocalDateTime startOfMinute(LocalDateTime timestamp){
        return timestamp.minusSeconds(timestamp.getSecond()).minusNanos(timestamp.getNano());
    }
    
    /**
     * Handles parsing various Strings into LocalDateTime timestamps.
     * @param timestamp The string form of the timestamp.
     * @return The LocalDateTime timestamp.
     */
    public static LocalDateTime localDateTimeParser(String timestamp){
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(timestamp, format);
            return localDateTime;
        } catch (DateTimeParseException dtpe) {
            try {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(timestamp, format);
                return localDateTime;
            } catch (Exception e) {
                System.out.println("[Info] Error : " + e);
                return null;
            }
        } catch (Exception e){
            System.out.println("[Info] Error : " + e);
            return null;
        }
    }
    
    /**
     * Converts a LocalDateTime timestamp to a user-friendly String for the GUI.
     * Adds preceeding 0s if hours or minutes are less than 10.
     * @param datetime The LocalDateTime timestamp to convert.
     * @return The user-friendly String.
     */
    public static String toString(LocalDateTime datetime){
        String mins = ":";
        String hours = String.valueOf(datetime.getHour());
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        if (datetime.getMinute() < 10) {
            mins += "0";
        }
        String timestamp = String.valueOf(datetime.getDayOfMonth()) + "/" + datetime.getMonthValue() 
                + "/" + datetime.getYear() + " " + hours + mins + datetime.getMinute();
        return timestamp;
    }
}
