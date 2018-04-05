/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

/**
 *
 * @author jkell
 */
public enum TradeMode {
    GOFAI,
    MANUAL,
    NEURALNETWORK;
    
    /**
     * Default constructor for TradeMode enumeration
     */
    TradeMode(){
    }
    
    /**
     * Creates a user-friendly string to describe the TradeMode
     * @return User-friendly string to describe the trade mode
     */
    @Override
    public String toString()
    {
        String strResult = "";
        switch (this)
        {
            case GOFAI:
                strResult = "GOFAI";
                break;
            case MANUAL:
                strResult = "Manual";
                break;
            case NEURALNETWORK:
                strResult = "NeuralNetwork";
                break;
        }
        return strResult;
    }
}
