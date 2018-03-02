/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;


import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.MathsHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.SafeCastHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author jkell
 */
public class PricePredictor {
    private static final int numberOfPredictions = 20;
    private static final int numberOfReadingsPerPrediction = 20;
    private static Double[] meanPredictedChange = new Double [numberOfPredictions];
    private static int[] predictedChangeNo = new int[numberOfPredictions];
    private static Double[] meanDeltas = new Double[numberOfPredictions];
    private static int[] deltaNo = new int[numberOfPredictions];
    private static Double[] closest = new Double[numberOfPredictions];
    private static int totalReadings = 0;
    private static Currency[] currencies;
    
    public static void makePredictions(Currency[] currencies){
        initialiseTests();
        GOFAI();
        neuralNetwork();
    }
    
    private static void initialiseTests(){
        for (int i = 0; i < numberOfPredictions; i++){
            meanPredictedChange[i] = 0.0;
            predictedChangeNo[i] = 0;
            meanDeltas[i] = 0.0;
            deltaNo[i] = 0;
        }
    }
    
    public static void GOFAI(){
        for (Currency currency: currencies){
            GOFAICalculations(currency);
        }
        GOFAIResults();
    }
    
    private static void GOFAICalculations(Currency currency){
        Double[] predictions;
        Double[] deltas;
        ExchangeRate[] rates = SafeCastHelper.objectsToExchangeRates(currency.getRates().toArray());
        ExchangeRate[] currentRates;
        int highestIndex = rates.length - 1;
        int noOfPredictions = highestIndex - numberOfReadingsPerPrediction;
        Double actualGrowth;

        for (int i = 0; i < noOfPredictions; i++) {
            currentRates = Arrays.copyOfRange(rates, noOfPredictions - i, highestIndex - i);
            predictions = predict(currentRates);
            
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
                System.out.println("[INFO] Failed to get growth for " + currency.getID() + " at " + currency.getRates().get(highestIndex - i).getLDTTimestamp().plusMinutes(1));
            }
        }
    }

    private static Double[] predict(ExchangeRate[] rates){
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
    
    private static void neuralNetwork(){
        trainNeuralNetwork();
        for (Currency currency: currencies){
            neuralNetworkCalculations(currency);
        }
        neuralNetworkResults();
    }
    
    private static void trainNeuralNetwork(){
        
    }
    
    private static void neuralNetworkCalculations(Currency currency){
        
    }
    
    private static void neuralNetworkResults(){
        
    }
}
