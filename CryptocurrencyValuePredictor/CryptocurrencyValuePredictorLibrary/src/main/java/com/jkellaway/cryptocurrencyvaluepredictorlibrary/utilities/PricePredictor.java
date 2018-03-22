/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;


import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
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
    private static Double[] meanPredictedChange = new Double [Globals.NUMBEROFPREDICTIONS];
    private static int[] predictedChangeNo = new int[Globals.NUMBEROFPREDICTIONS];
    private static Double[] meanDeltas = new Double[Globals.NUMBEROFPREDICTIONS];
    private static int[] deltaNo = new int[Globals.NUMBEROFPREDICTIONS];
    private static Double[] closest = new Double[Globals.NUMBEROFPREDICTIONS];
    private static int totalReadings = 0;

    
    public static Currency[] initialPredictions(Currency[] currencies){
        initialiseTests();
        GOFAI(currencies);
        neuralNetwork(currencies);
        return currencies;
    }

    private static void initialiseTests(){
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++){
            meanPredictedChange[i] = 0.0;
            predictedChangeNo[i] = 0;
            meanDeltas[i] = 0.0;
            deltaNo[i] = 0;
        }
    }
    
    public static void GOFAI(Currency[] currencies){
        for (Currency currency : currencies){
            GOFAICalculations(currency);
        }
        GOFAIResults();
    }
    
    private static void GOFAICalculations(Currency currency){
        Double[] predictions;
        Double[] deltas;
        ExchangeRate[] allRates = SafeCastHelper.objectsToExchangeRates(currency.getRates().toArray());
        ExchangeRate[] relevantRates;
        int highestIndex = allRates.length - 1;
        int noOfPredictions = highestIndex - Globals.NUMBEROFPREDICTIONS;
        Double actualGrowth;

        for (int i = 0; i <= noOfPredictions; i++) {
            relevantRates = Arrays.copyOfRange(allRates, noOfPredictions - i, highestIndex - i);
            predictions = predict(relevantRates);
            currency.getRates().get(highestIndex - i).setGofaiNextGrowth(predictions);
            try {
                actualGrowth = currency.getRates().get(highestIndex - i + 1).getGrowth();
                deltas = MathsHelper.deltas(actualGrowth, predictions);
                for (int j = 0; j < Globals.NUMBEROFPREDICTIONS; j++){
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
        Double[] predictions = new Double[Globals.NUMBEROFPREDICTIONS];
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++){
            values.add(rates[i].getGrowth());
            predictions[i] = MathsHelper.mean(SafeCastHelper.objectsToDoubles(values.toArray()));
        }
        return predictions;
    }
    
    private static void GOFAIResults(){
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++){
            meanPredictedChange[i] /= predictedChangeNo[i];
            meanDeltas[i] /= deltaNo[i];
            
            System.out.println("Mean predicted change using " + (i+1) + " historic prices: " + meanPredictedChange[i]);
            System.out.println("Mean prediction error using " + (i+1) + " historic prices: " + meanDeltas[i]);
        }
    }
    
    private static void neuralNetwork(Currency[] currencies){
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
    
    public static Currency[] singlePrediction(Currency[] currencies) {
        for (Currency currency : currencies){
            GOFAICalculation(currency);
            neuralNetworkCalculation(currency);
        }
        return currencies;
    }
    
    private static void GOFAICalculation(Currency currency) {
        Double[] predictions;
        ExchangeRate[] allRates = SafeCastHelper.objectsToExchangeRates(currency.getRates().toArray());
        int highestIndex = allRates.length - 1;
        ExchangeRate[] relevantRates = Arrays.copyOfRange(allRates, highestIndex - Globals.NUMBEROFPREDICTIONS, highestIndex);
        predictions = predict(relevantRates);
        currency.getRates().get(highestIndex).setGofaiNextGrowth(predictions);
    }
    
    private static void neuralNetworkCalculation(Currency currency) {
        
    }
}
