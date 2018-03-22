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
public class MathsHelper {
    public static Double mean(Double[] values){
        try {
            Double mean = 0.0;
                for (Double value : values){
                    mean += value;
                }
            return (mean /= values.length);
        } catch (Exception e){
            System.out.println("[INFO] Error: " + e);
            return null;
        }
    }

    public static Double[] deltas(Double actual, Double[] predictions){
        Double[] deltas = new Double[predictions.length];
        for (int i = 0; i < predictions.length; i++){
            deltas[i] = Math.abs(actual - predictions[i]);
        }
        return deltas;
    }
    
    public static Double max(Double[] array) {
        if (0 < array.length) {
            Double max = array[0];
            for (Double dbl : array) {
                max = (max < dbl) ? dbl : max;
            }
            return max;
        }
        return null;
    }
    
    public static Double min(Double[] array) {
        if (0 < array.length) {
            Double min = array[0];
            for (Double dbl : array) {
                min = (min < dbl) ? min : dbl;
            }
            return min;
        }
        return null;
    }
}
