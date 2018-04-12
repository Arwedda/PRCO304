/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author jkell
 */
public class StringHelper {
    
    /**
     * Calculates whether the first x values of two Strings are the same.
     * @param string1 The first String.
     * @param string2 The second String.
     * @param length The number of characters to compare.
     * @return true = match, false = mismatch.
     */
    public static boolean stringsMatch(String string1, String string2, int length){
        string1 = string1.substring(0, Math.min(string1.length(), length));
        string2 = string2.substring(0, Math.min(string2.length(), length));
        return string1.equals(string2);
    }
    
    /**
     * Converts a Double representing a currency value into a user-friendly String.
     * @param dbl The double.
     * @param decimals The number of decimals to show
     * @return A user-friendly String (i.e. Adds succeeding 0s if there are no pence/cents).
     */
    public static String doubleToCurrencyString(Double dbl, Integer decimals){
        if (dbl != null && decimals != null) {
            String format = (0 < decimals) ? "0." : "0";
            for (int i = 0; i < decimals; i++) {
                format += "0";
            }
            DecimalFormat df = new DecimalFormat(format, DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(dbl);
        }
        return null;
    }
    
    /**
     * Compares two String representations of Integers or Doubles in a way that
     * is more natural to humans (i.e not 1, 11, 111, 2, 22, 222) by padding the
     * beginning of the String with 0s.
     * @param s1 The first String.
     * @param s2 The second String.
     * @return Result of comparing the Strings with .compareTo (lexicographically).
     */
    public static int naturalNumberCompare(String s1, String s2) {
        if (s1 != null && s2 != null) {
            int diff = s1.length() - s2.length();
            String str1 = (0 < diff) ? s1 : padLeftWithZeroes(s1, -diff);
            String str2 = (0 < diff) ? padLeftWithZeroes(s2, diff) : s2;
            return str1.toUpperCase().compareTo(str2.toUpperCase());
        }
        return 0;
    }
    
    /**
     * Adds 0s to the beginning of a String to make the two Strings equal in 
     * size. This allows for a more human sorting of numbers.
     * @param str The String to pad.
     * @param padSize The number of 0s to add.
     * @return The new String.
     */
    private static String padLeftWithZeroes(String str, int padSize) {
        String padded = "";
        for (int i = 0; i < padSize; i++) {
            padded += "0";
        }
        return padded + str;
    }
}