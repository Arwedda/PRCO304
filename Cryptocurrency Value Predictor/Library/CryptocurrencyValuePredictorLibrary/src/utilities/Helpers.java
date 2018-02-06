/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jkell
 */
public class Helpers {
    public static boolean stringsMatch(String string1, String string2, int length){
        string1 = string1.substring(0, Math.min(string1.length(), length));
        string2 = string2.substring(0, Math.min(string2.length(), length));
        return string1.equals(string2);
    }
    
    public static LocalDateTime startOfMinute(LocalDateTime time){
        return time.minusSeconds(time.getSecond()).minusNanos(time.getNano());
    }
    
    public static LocalDateTime localDateTimeParser(String date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date, format);
        return localDateTime;
    }
}
