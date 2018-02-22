/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import helpers.MathsHelper;
import helpers.SafeCastHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Currency;
import model.ExchangeRate;

/**
 *
 * @author jkell
 */
public class PricePredictor {
    static final int numberOfPredictions = 20;
    static final int numberOfReadingsPerPrediction = 20;
    static Double[] meanPredictedChange = new Double [numberOfPredictions];
    static int[] predictedChangeNo = new int[numberOfPredictions];
    static Double[] meanDeltas = new Double[numberOfPredictions];
    static int[] deltaNo = new int[numberOfPredictions];
    static Double[] closest = new Double[numberOfPredictions];
    static int totalReadings = 0;
    
    public static void gofaiTest(Currency[] currencies){
        initialise();
        for (Currency currency: currencies){
            GOFAICalculations(currency);
        }
        GOFAIResults();
    }
    
    private static void initialise(){
        for (int i = 0; i < numberOfPredictions; i++){
            meanPredictedChange[i] = 0.0;
            predictedChangeNo[i] = 0;
            meanDeltas[i] = 0.0;
            deltaNo[i] = 0;
        }
    }
    
    private static void GOFAICalculations(Currency currency){
        Double[] predictions = new Double[numberOfPredictions];
        Double[] deltas = new Double[numberOfPredictions];
        ExchangeRate[] rates = SafeCastHelper.objectsToExchangeRates(currency.getRates().toArray());
        ExchangeRate[] currentRates;
        int highestIndex = rates.length - 1;
        int noOfPredictions = highestIndex - numberOfReadingsPerPrediction;
        Double actualGrowth;

        for (int i = 0; i < noOfPredictions; i++) {
            currentRates = Arrays.copyOfRange(rates, noOfPredictions - i, highestIndex - i);
            predictions = makePredictions(currentRates);
            
            /*
                NEED TO ENSURE THAT NEXT GROWTH EXISTS~~
            */
            try {
                actualGrowth = currency.getRates().get(highestIndex - i + 1).getGrowth();
                deltas = MathsHelper.deltas(actualGrowth, predictions);
                for (int j = 0; j < numberOfPredictions; j++){
                    meanPredictedChange[j] += Math.abs(predictions[j]);
                    predictedChangeNo[j]++;
                    meanDeltas[j] += deltas[j];
                    deltaNo[j]++;
                }
            } catch (Exception e){
                System.out.println("[INFO] Error: " + e);
                System.out.println("[INFO] Failed to get growth for " + currency.getID() + " at " + currency.getRates().get(highestIndex - i).getTimestamp().plusMinutes(1));
            }
        }
    }

    private static Double[] makePredictions(ExchangeRate[] rates){
        Double[] predictions = new Double[numberOfPredictions];
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < numberOfPredictions; i++){
            values.add(rates[i].getGrowth());
            predictions[i] = MathsHelper.mean(SafeCastHelper.objectsToDoubles(values.toArray()));
        }
        return predictions;
    }
    
    private static void GOFAIResults(){
        for (int i = 0; i < numberOfPredictions; i++){
            meanPredictedChange[i] /= predictedChangeNo[i];
            meanDeltas[i] /= deltaNo[i];
            
            System.out.println("Mean predicted change using " + (i+1) + " historic prices: " + meanPredictedChange[i]);
            System.out.println("Mean prediction error using " + (i+1) + " historic prices: " + meanDeltas[i]);
        }
    }
}
