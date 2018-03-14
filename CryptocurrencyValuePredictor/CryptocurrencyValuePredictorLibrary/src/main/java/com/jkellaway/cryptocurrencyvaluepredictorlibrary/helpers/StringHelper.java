/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

/**
 *
 * @author jkell
 */
public class StringHelper {
    public static boolean stringsMatch(String string1, String string2, int length){
        string1 = string1.substring(0, Math.min(string1.length(), length));
        string2 = string2.substring(0, Math.min(string2.length(), length));
        return string1.equals(string2);
    }
    
    public static String doubleToCurrencyString(Double dbl){
        String value = String.valueOf(dbl);
        String[] splitString = value.split("\\.");
        if (1 < splitString[1].length()) {
            return splitString[0] + "." + splitString[1].substring(0, 2);
        }
        return splitString[0] + "." + splitString[1] + "0";
    }
}
