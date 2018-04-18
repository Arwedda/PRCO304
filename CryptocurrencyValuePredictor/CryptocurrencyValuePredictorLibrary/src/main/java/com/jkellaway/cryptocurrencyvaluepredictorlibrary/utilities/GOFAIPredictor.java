/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;


import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.MathsHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author jkell
 */
public class GOFAIPredictor {
    private static Double[] meanPredictedChange = new Double [Globals.NUMBEROFPREDICTIONS];
    private static int[] predictedChangeNo = new int[Globals.NUMBEROFPREDICTIONS];
    private static Double[] meanDeltas = new Double[Globals.NUMBEROFPREDICTIONS];
    private static int[] deltaNo = new int[Globals.NUMBEROFPREDICTIONS];
    private static Double[] closest = new Double[Globals.NUMBEROFPREDICTIONS];
    private static int totalReadings = 0;
    
    /**
     * Initial predictions that would have been across the ExchangeRates as they 
     * collected live - used for benchmarking.
     * @param currencies The Currency array to make predictions for.
     * @return The Currency array with newly calculated predictions.
     */
    public static Currency[] initialPredictions(Currency[] currencies) {
        initialise();
        predict(currencies);
        return currencies;
    }

    /**
     * Initialises variables used to compare each algorithm's prediction accuracy.
     */
    private static void initialise() {
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++){
            meanPredictedChange[i] = 0.0;
            predictedChangeNo[i] = 0;
            meanDeltas[i] = 0.0;
            deltaNo[i] = 0;
        }
    }
    
    /**
     * Cycles each Currency in parameter and makes GOFAI predictions across it.
     * @param currencies The Currency array to make GOFAI predictions for.
     */
    private static void predict(Currency[] currencies) {
        for (Currency currency : currencies){
            GOFAIPredictions(currency);
        }
        GOFAIResults();
    }
    
    /**
     * Creates an array of 20 growth predictions for each ExchangeRate in Currency.
     * Starts at price 22 (index 21) because each prediction requires up to 20 minutes
     * of previous prices. Prediction Index 0 = 1 minute, Prediction Index 19 = 20 minutes.
     * It creates relevant 20 ExchangeRate arrays to calculate each growth array.
     * Also calculates the difference between the prediction and the result to
     * assess algorithm accuracy.
     * @param currency The Currency to make GOFAI predictions for.
     */
    private static void GOFAIPredictions(Currency currency) {
        Double[] predictions;
        Double[] deltas;
        ExchangeRate[] allRates = currency.getRates().toArray(new ExchangeRate[currency.getRates().size()]);
        ExchangeRate[] relevantRates;
        int highestIndex = allRates.length - 1;
        int noOfPredictions = highestIndex - (Globals.NUMBEROFPREDICTIONS + 1);
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
            } catch (IndexOutOfBoundsException ie){
                System.out.println("[INFO] Error: " + ie);
                System.out.println("[INFO] If Index = Size then expected.");
            } catch (Exception e) {
                
            }
        }
    }

    /**
     * Constructs the prediction array from the relevant ExchangeRates by creating
     * a mean of the growths used in each prediction.
     * @param rates The ExchangeRates relevant to this prediction array.
     * @return The prediction array.
     */
    private static Double[] predict(ExchangeRate[] rates) {
        Integer highestIndex = rates.length - 1;
        Double[] predictions = new Double[Globals.NUMBEROFPREDICTIONS];
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++){
            values.add(rates[highestIndex - i].getGrowth());
            predictions[i] = MathsHelper.mean(values.toArray(new Double[values.size()]));
            predictions[i] = MathsHelper.roundDP(predictions[i], 4);
        }
        return predictions;
    }
    
    /**
     * Calculates and outputs the mean predictions of each algorithm and then the
     * difference between the prediction and the actual result (error).
     */
    private static void GOFAIResults() {
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++){
            meanPredictedChange[i] /= predictedChangeNo[i];
            meanDeltas[i] /= deltaNo[i];
            
            System.out.println("Mean predicted change using " + (i+1) + " historic prices: " + meanPredictedChange[i]
            + " Mean prediction error: " + meanDeltas[i]);
        }
    }
    
    /**
     * Creates a set of predictions for the next ExchangeRate values based on the 
     * growth between the previous 20 prices. Updates benchmarking and used for
     * live trading.
     * @param currencies The Currency array to make predictions for.
     * @return The Currency array with newly calculated predictions.
     */
    public static Currency[] singlePrediction(Currency[] currencies) {
        for (Currency currency : currencies){
            GOFAICalculation(currency);
        }
        return currencies;
    }
    
    /**
     * Creates an array of 20 growth predictions for the next ExchangeRate of Currency.
     * Starts at price 22 (index 21) because each prediction requires up to 20 minutes
     * of previous prices. Prediction Index 0 = 1 minute, Prediction Index 19 = 20 minutes.
     * It creates relevant 20 ExchangeRate arrays to calculate each growth array.
     * @param currency The Currency to make GOFAI predictions for.
     */
    private static void GOFAICalculation(Currency currency) {
        Double[] predictions;
        ExchangeRate[] allRates = currency.getRates().toArray(new ExchangeRate[currency.getRates().size()]);
        int length = allRates.length;
        ExchangeRate[] relevantRates = Arrays.copyOfRange(allRates, length - Globals.NUMBEROFPREDICTIONS, length);
        predictions = predict(relevantRates);
        currency.getRates().get(length - 1).setGofaiNextGrowth(predictions);
    }
}
