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
    static List<List<Double>> arlDeltas = new ArrayList<>();
    static Double[] meanDeltas = new Double[numberOfPredictions];
    static Double[] closest = new Double[numberOfPredictions];
    static int totalReadings = 0;
    
    public static void gofaiTest(Currency[] currencies){
        
        for (Currency currency: currencies){
            GOFAICalculations(currency);
        }
        for (int i = 0; i < numberOfPredictions; i++){
            Double[] deltas = SafeCastHelper.objectsToDoubles(arlDeltas.get(i).toArray());
            meanDeltas[i] = MathsHelper.mean(deltas);
        }
        GOFAIResults();
    }
    
    private static void GOFAICalculations(Currency currency){
        int numberOfReadingsPerPrediction = 20;
        Double[] predictions = new Double[numberOfPredictions];
        for (int i = 0; i < numberOfPredictions; i++){
            List<Double> deltas = new ArrayList<>();
            arlDeltas.add(deltas);
        }

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
            if (i < noOfPredictions){
                actualGrowth = currency.getRates().get(highestIndex - i + 1).getGrowth();
                deltas = MathsHelper.deltas(actualGrowth, predictions);
                for (int j = 0; j < numberOfPredictions; j++){
                    arlDeltas.get(j).add(deltas[j]);
                }
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
        for (Double delta : meanDeltas){
            System.out.println(delta);
        }
    }
}
