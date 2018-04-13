/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author jkell
 */
public class MathsHelper {
    
    /**
     * Calculates the mean of an array of Doubles.
     * @param values The array of Doubles.
     * @return The mean.
     */
    public static Double mean(Double[] values) {
        try {
            Double mean = 0.0;
                for (Double value : values){
                    mean += value;
                }
            return (mean /= values.length);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Calculates the difference between an actual value and various predicted values.
     * @param actual The actual value.
     * @param predictions Array of predicted values.
     * @return The differences between the predictions and the actual value.
     */
    public static Double[] deltas(Double actual, Double[] predictions) {
        Double[] deltas = new Double[predictions.length];
        for (int i = 0; i < predictions.length; i++){
            try {
                deltas[i] = Math.abs(actual - predictions[i]);
            } catch (NullPointerException e) {
                return deltas;
            }
        }
        return deltas;
    }
    
    /**
     * Calculates the highest value in an array.
     * @param array The array of doubles.
     * @return The highest value in the array.
     */
    public static Double max(Double[] array) {
        if (0 < array.length) {
            Double max = array[0];
            for (Double dbl : array) {
                try {
                    max = (max < dbl) ? dbl : max;
                } catch (NullPointerException e) {
                    return null;
                }
            }
            return max;
        }
        return null;
    }
    
    /**
     * Calculates the lowest value in an array.
     * @param array The array of doubles.
     * @return The lowest value in the array.
     */
    public static Double min(Double[] array) {
        if (0 < array.length) {
            Double min = array[0];
            for (Double dbl : array) {
                try {
                    min = (min < dbl) ? min : dbl;
                } catch (NullPointerException e) {
                    return null;
                }
            }
            return min;
        }
        return null;
    }
    
    /**
     * Rounds a parameter Double value to numberOfDP decimal places.
     * @param toRound The number to round.
     * @param numberOfDP The number of decimal places to round to.
     * @return The rounded number.
     */
    public static Double roundDP(Double toRound, int numberOfDP) {
        String format = (0 < numberOfDP) ? "0." : "0";
        if (toRound != null) {
            for (int i = 0; i < numberOfDP; i++) {
                format += "0";
            }
            DecimalFormat df = new DecimalFormat(format);
            df.setRoundingMode(RoundingMode.HALF_UP);
            return Double.parseDouble(df.format(toRound));
        }
        return null;
    }
}
