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
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jkell
 */
public class GOFAIPredictorTest {
    private static Currency[] decliningCurrencies;
    private static Currency[] growingCurrencies;
    
    public GOFAIPredictorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());
        ExchangeRate rate;
        decliningCurrencies = new Currency[4];
        growingCurrencies = new Currency[4];
        Currency newCurrency = new Currency("BCH", "Bitcoin Cash", Globals.BCH_TRADES);
        decliningCurrencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", Globals.BTC_TRADES);
        decliningCurrencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", Globals.ETH_TRADES);
        decliningCurrencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", Globals.LTC_TRADES);
        decliningCurrencies[3] = newCurrency;
        
        newCurrency = new Currency("BCH", "Bitcoin Cash", Globals.BCH_TRADES);
        growingCurrencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", Globals.BTC_TRADES);
        growingCurrencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", Globals.ETH_TRADES);
        growingCurrencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", Globals.LTC_TRADES);
        growingCurrencies[3] = newCurrency;
        
        for (int i = 0; i < TestGlobals.REQUIREDRATES + 10; i++) {
            rate = new ExchangeRate(TestGlobals.IDBCH, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            decliningCurrencies[0].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDBTC, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            decliningCurrencies[1].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDETH, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            decliningCurrencies[2].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDLTC, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            decliningCurrencies[3].addHistoricRate(rate);
            
            rate = new ExchangeRate(TestGlobals.IDBCH, timestamp.minusMinutes(i), Double.valueOf(TestGlobals.REQUIREDRATES + 10 - i), null, null, null, TestGlobals.LASTTRADE);
            growingCurrencies[0].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDBTC, timestamp.minusMinutes(i), Double.valueOf(TestGlobals.REQUIREDRATES + 10 - i), null, null, null, TestGlobals.LASTTRADE);
            growingCurrencies[1].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDETH, timestamp.minusMinutes(i), Double.valueOf(TestGlobals.REQUIREDRATES + 10 - i), null, null, null, TestGlobals.LASTTRADE);
            growingCurrencies[2].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDLTC, timestamp.minusMinutes(i), Double.valueOf(TestGlobals.REQUIREDRATES + 10 - i), null, null, null, TestGlobals.LASTTRADE);
            growingCurrencies[3].addHistoricRate(rate);
        }
        
        for (int i = 0; i < decliningCurrencies.length; i++) {
            decliningCurrencies[i].mergeRates();
            decliningCurrencies[i].calculateGrowth(true);
            growingCurrencies[i].mergeRates();
            growingCurrencies[i].calculateGrowth(true);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testInitialPredictions() {
        List<ExchangeRate> decRates;
        List<ExchangeRate> groRates;
        Double decGrowth;
        Double groGrowth;
        decliningCurrencies = GOFAIPredictor.initialPredictions(decliningCurrencies);
        growingCurrencies = GOFAIPredictor.initialPredictions(growingCurrencies);
        
        for (int i = 0; i < decliningCurrencies.length; i++) {
            decRates = decliningCurrencies[i].getRates();
            groRates = growingCurrencies[i].getRates();
            for (int j = 0; j < decliningCurrencies[i].getRates().size(); j++) {
                if (j <= 20) {
                    for (int k = 0; k < Globals.NUMBEROFPREDICTIONS; k++) {
                        assertNull(decRates.get(j).getGofaiNextGrowth()[k]);
                        assertNull(groRates.get(j).getGofaiNextGrowth()[k]);
                    }
                } else {
                    decGrowth = 0.0;
                    groGrowth = 0.0;
                    for (int k = 0; k < Globals.NUMBEROFPREDICTIONS; k++) {
                        decGrowth += decRates.get(j - (k + 1)).getGrowth();
                        groGrowth += groRates.get(j - (k + 1)).getGrowth();
                        assertEquals(MathsHelper.roundDP(decGrowth / (k + 1), 4), decRates.get(j).getGofaiNextGrowth()[k], TestGlobals.DELTA);
                        assertEquals(MathsHelper.roundDP(groGrowth / (k + 1), 4), groRates.get(j).getGofaiNextGrowth()[k], TestGlobals.DELTA);
                    }
                }
            }
        }
    }

    @Test
    public void testSinglePrediction() {
        List<ExchangeRate> decRates;
        List<ExchangeRate> groRates;
        Double decGrowth;
        Double groGrowth;
        decliningCurrencies = GOFAIPredictor.singlePrediction(decliningCurrencies);
        growingCurrencies = GOFAIPredictor.singlePrediction(growingCurrencies);
        
        for (int i = 0; i < decliningCurrencies.length; i++) {
            decRates = decliningCurrencies[i].getRates();
            groRates = growingCurrencies[i].getRates();
            decGrowth = 0.0;
            groGrowth = 0.0;
            for (int j = 0; j < Globals.NUMBEROFPREDICTIONS; j++) {
                decGrowth += decRates.get(decRates.size() - (j + 1)).getGrowth();
                groGrowth += groRates.get(groRates.size() - (j + 1)).getGrowth();
                assertEquals(MathsHelper.roundDP(decGrowth / (j + 1), 4), decRates.get(decRates.size() - 1).getGofaiNextGrowth()[j], TestGlobals.DELTA);
                assertEquals(MathsHelper.roundDP(groGrowth / (j + 1), 4), groRates.get(groRates.size() - 1).getGofaiNextGrowth()[j], TestGlobals.DELTA);
            }
        }
    }
}
