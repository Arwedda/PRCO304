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
            return null;
        }
    }

    public static Double[] deltas(Double actual, Double[] predictions){
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
}
