/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;

/**
 *
 * @author jkell
 */
public class NeuralNetwork {
    private Double[] weights;
    
    public NeuralNetwork() {
        
    }
    
    public NeuralNetwork(Currency[] currencies, Integer inputSize) {
        initialiseWeights(inputSize);
        trainNeuralNetwork();
        for (Currency currency: currencies){
            neuralNetworkCalculations(currency);
        }
        neuralNetworkResults();
    }
    
    private void initialiseWeights(Integer inputSize) {
        weights = new Double[inputSize];
        
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
