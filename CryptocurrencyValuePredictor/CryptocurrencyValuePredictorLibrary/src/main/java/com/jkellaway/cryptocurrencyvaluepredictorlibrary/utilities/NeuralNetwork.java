/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import java.util.List;

/**
 *
 * @author jkell
 */
public class NeuralNetwork {
    private List<Double[]> weights;
    
    public NeuralNetwork() {
        
    }
    
    public NeuralNetwork(Currency[] currencies, Integer[] inputSizes) {
        initialiseWeights(inputSizes);
        trainNeuralNetwork();
        for (Currency currency: currencies){
            neuralNetworkCalculations(currency);
        }
        neuralNetworkResults();
    }
    
    private void initialiseWeights(Integer[] inputSizes) {
        Double[] locWeights;
        for (int i = 0; i < inputSizes.length; i++) {
            locWeights = new Double[inputSizes[i]];
            for (Double dbl : locWeights) {
                dbl = Math.random() * 0.001;
            }
            weights.add(locWeights);
        }
        
    }
    
    private void trainNeuralNetwork() {
        
    }
    
    private void neuralNetworkCalculations(Currency currency) {
        
    }
    
    private void neuralNetworkResults() {
        
    }

    private static void neuralNetworkCalculation(Currency currency) {
        
    }
}
