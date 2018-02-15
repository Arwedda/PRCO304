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
    public static void gofaiTest(Currency currency, int testSize){
        testSize = testSize - 20; //Because 20 readings are required to predict first
        Double[] predictions = new Double[20];
        ExchangeRate[] rates = SafeCastHelper.objectsToExchangeRates(currency.getRates().toArray());
        ExchangeRate[] currentRates;

        for (int i = 0; i < testSize; i++) {
            currentRates = Arrays.copyOfRange(rates, (rates.length - 21) - i, (rates.length - 1) - i);
            predictions = getPredictions(currentRates);
            currency.getRates().get((currency.getRates().size() - 1) - i).GOFAINextGrowth = predictions;
        }
    }

    private static Double[] getPredictions(ExchangeRate[] rates){
        int preds = 20;
        Double[] predictions = new Double[preds];

        for (int i = 0; i < preds; i++){
            predictions[i] = averageGrowth(rates, i+1);
        }
        return predictions;
    }

    private static Double averageGrowth(ExchangeRate[] rates, int entriesToUse){
        Double avg = 0.0;
        for (int i = 0; i < entriesToUse; i++){
            avg += rates[(rates.length - 1) - i].getGrowth();
        }
        return (avg / rates.length);
    }
}
