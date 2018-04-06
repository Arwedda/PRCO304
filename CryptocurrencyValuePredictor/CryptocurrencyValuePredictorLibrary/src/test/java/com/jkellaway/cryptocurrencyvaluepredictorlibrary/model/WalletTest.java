/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author jkell
 */
public class WalletTest {
    private Wallet blank;
    private Wallet real;
    
    public WalletTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        blank = new Wallet();
        real = new Wallet(TestGlobals.IDUSD, 100.0);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetHoldingID() {
        assertEquals("Unknown", blank.getHoldingID());
        assertEquals(TestGlobals.IDUSD, real.getHoldingID());
    }

    @Test
    public void testSetHoldingID() {
        blank.setHoldingID(TestGlobals.IDBCH);
        assertEquals(TestGlobals.IDBCH, blank.getHoldingID());
        blank.setHoldingID(null);
    }

    @Test
    public void testSetValue() {
        blank.setValue(1.0);
        assertEquals(1.0, blank.getValue(), TestGlobals.DELTA);
        blank.setValue(null);
    }

    @Test
    public void testGetValue() {
        assertNull(blank.getValue());
        assertEquals(100.0, real.getValue(), TestGlobals.DELTA);
    }

    @Test
    public void testSetStartingValues() {
        Double[] startVals = new Double[4];
        for (int i = 0; i < startVals.length; i++) {
            startVals[i] = real.getStartingUSD() / (i + 1);
        }
        real.setStartingValues(startVals, 2.0, 1);
        blank.setStartingValues(startVals, 2.0, 1);
        
        assertArrayEquals(real.getStartingValues(), blank.getStartingValues());
        assertEquals(real.getStartingUSD(), blank.getStartingUSD(), TestGlobals.DELTA);
    }

    @Test
    public void testGetStartingValues() {
        for (int i = 0; i < 4; i++) {
            assertNull(blank.getStartingValues()[i]);
            assertNull(real.getStartingValues()[i]);
        }
    }

    @Test
    public void testGetStartingUSD() {
        assertNull(blank.getStartingUSD());
        assertEquals(100.0, real.getStartingUSD(), TestGlobals.DELTA);
    }

    @Test
    public void testGetUSDValue() {
        Currency[] currencies = new Currency[2];
        Currency currency = new Currency(TestGlobals.IDBCH, TestGlobals.NAMEBITCOINCASH, Globals.BCH_TRADES);
        currencies[0] = currency;
        currency = new Currency(TestGlobals.IDBTC, TestGlobals.NAMEBITCOIN, Globals.BTC_TRADES);
        currencies[1] = currency;
        ExchangeRate rate = new ExchangeRate();
        rate.setValue(100.0);
        currencies[0].setValue(rate);
        rate = new ExchangeRate();
        rate.setValue(125.0);
        currencies[1].setValue(rate);
        assertNull(blank.getUSDValue(currencies));
        blank.setHoldingID(TestGlobals.IDBCH);
        assertNull(blank.getUSDValue(currencies));
        blank.setValue(1.0);
        assertEquals(100.0, blank.getUSDValue(currencies), TestGlobals.DELTA);
        assertEquals(100.0, real.getUSDValue(currencies), TestGlobals.DELTA);
        blank = new Wallet();
    }
}
