/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import helpers.SafeCastHelper;
import java.util.Arrays;
import model.Currency;
import model.ExchangeRate;

/**
 *
 * @author jkell
 */
public class PricePredictor {
    static final int numberOfPredictions = 20;
    static Double[] meanDeltas = new Double[numberOfPredictions];
    static Double[] closest = new Double[numberOfPredictions];
    static int totalReadings = 0;
    
    public static void gofaiTest(Currency[] currencies){
        for (Currency currency: currencies){
            GOFAICalculations(currency);
        }
        for (int i = 0; i < numberOfPredictions; i++){
            meanDeltas[i] /= totalReadings;
        }
        GOFAIResults();
    }
    
    private static void GOFAICalculations(Currency currency){
        int numberOfReadingsPerPrediction = 20;
        Double[] predictions = new Double[numberOfPredictions];
        Double[] deltas = new Double[numberOfPredictions];
        ExchangeRate[] rates = SafeCastHelper.objectsToExchangeRates(currency.getRates().toArray());
        ExchangeRate[] currentRates;
        int highestIndex = rates.length - 1;
        int noOfPredictions = highestIndex - numberOfReadingsPerPrediction;
        Double actualGrowth;

        for (int i = 0; i < noOfPredictions; i++) {
            currentRates = Arrays.copyOfRange(rates, noOfPredictions - i, highestIndex - i);
            predictions = getPredictions(currentRates);
            if (i < noOfPredictions){
                actualGrowth = currency.getRates().get(highestIndex - i + 1).getGrowth();
                deltas = getDeltas(predictions, actualGrowth);
                for (int j = 0; j < numberOfPredictions; j++){
                    meanDeltas[i] += deltas[i];
                }
                totalReadings++;
            }
        }
    }

    private static Double[] getPredictions(ExchangeRate[] rates){
        Double[] predictions = new Double[numberOfPredictions];
        for (int i = 0; i < numberOfPredictions; i++){
            predictions[i] = averageGrowth(rates, i+1);
        }
        return predictions;
    }

    private static Double averageGrowth(ExchangeRate[] rates, int entriesToUse){
        Double avg = 0.0;
        int highestIndex = rates.length - 1;
        for (int i = 0; i < entriesToUse; i++){
            avg += rates[highestIndex - i].getGrowth();
        }
        return (avg / (highestIndex + 1));
    }
    
    private static Double[] getDeltas(Double[] predictions, Double actualGrowth){
        Double[] deltas = new Double[numberOfPredictions];
        for (int i = 0; i < numberOfPredictions; i++){
            deltas[i] = Math.abs(actualGrowth - predictions[i]);
        }
        return deltas;
    }
    
    private static void GOFAIResults(){
        for (Double delta : meanDeltas){
            System.out.println(delta);
        }
    }
}
