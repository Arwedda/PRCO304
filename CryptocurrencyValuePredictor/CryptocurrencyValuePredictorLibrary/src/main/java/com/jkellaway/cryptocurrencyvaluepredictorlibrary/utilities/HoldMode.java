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
public enum HoldMode {
    CRYPTOCURRENCY,
    USD,
    BCH,
    BEST,
    BTC,
    ETH,
    LTC,
    WORST;
    
    /**
     * Default constructor for HoldMode enumeration
     */
    HoldMode(){
    }
    
    /**
     * Creates a user-friendly string to describe the HoldMode
     * @return User-friendly string to describe the hold mode
     */
    @Override
    public String toString()
    {
        String strResult = "";
        switch (this)
        {
            case CRYPTOCURRENCY:
                strResult = "Cryptocurrency";
                break;
            case USD:
                strResult = "USD";
                break;
            case BCH:
                strResult = "BCH";
                break;
            case BEST:
                strResult = "Best";
                break;
            case BTC:
                strResult = "BTC";
                break;
            case ETH:
                strResult = "ETH";
                break;
            case LTC:
                strResult = "LTC";
                break;
            case WORST:
                strResult = "Worst";
                break;
        }
        return strResult;
    }
}
