/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import java.time.Clock;
import java.time.LocalDateTime;
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
    private static Currency[] currencies;
    
    public GOFAIPredictorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());
        ExchangeRate rate;
        currencies = new Currency[4];
        Currency newCurrency = new Currency("BCH", "Bitcoin Cash", Globals.BCH_TRADES);
        currencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", Globals.BTC_TRADES);
        currencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", Globals.ETH_TRADES);
        currencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", Globals.LTC_TRADES);
        currencies[3] = newCurrency;
        for (int i = 0; i < TestGlobals.REQUIREDRATES + 10; i++) {
            rate = new ExchangeRate(TestGlobals.IDBCH, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            currencies[0].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDBTC, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            currencies[1].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDETH, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            currencies[2].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDLTC, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            currencies[3].addHistoricRate(rate);
        }
        
        for (Currency currency : currencies) {
            currency.mergeRates();
            currency.calculateGrowth(true);
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
        
    }

    @Test
    public void testSinglePrediction() {
        
    }
}
