/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

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
    
    public static Integer mode(int[] values){
        try {
            Integer maxValue = null;
            int count;
            int maxCount = 0;
            for (int i = 0; i < values.length; ++i) {
                count = 0;
                for (int j = 0; j < values.length; ++j) {
                    if (values[j] == values[i]) {
                        count++;
                    }
                }
                if (count > maxCount) {
                    maxCount = count;
                    maxValue = i;
                }
            }
            return maxValue;
        } catch (Exception e){
            System.out.println("[INFO] Error: " + e);
            return null;
        }
    }
    
    public static Integer[] modes(Double[] values){
        Integer[] modeOrder = new Integer[values.length];
        
        return modeOrder;
    }
    
    public static Double[] deltas(Double actual, Double[] predictions){
        Double[] deltas = new Double[predictions.length];
        for (int i = 0; i < predictions.length; i++){
            deltas[i] = Math.abs(actual - predictions[i]);
        }
        return deltas;
    }
}
