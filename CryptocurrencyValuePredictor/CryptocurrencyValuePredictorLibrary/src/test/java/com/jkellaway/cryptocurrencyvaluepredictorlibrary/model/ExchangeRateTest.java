/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author jkell
 */
public class ExchangeRateTest {
    ExchangeRate blankRate;
    ExchangeRate rate;
    LocalDateTime timeStamp;
    
    public ExchangeRateTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        timeStamp = LocalDateTime.now();
        blankRate = new ExchangeRate();
        rate = new ExchangeRate(TestGlobals.IDBCH, timeStamp, TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
    }

    @After
    public void tearDown() {
    }

    public void testGetCurrency_id() {
    }

    public void testSetCurrency_id() {
    }

    public void testGetTimestamp() {

    }

    public void testSetTimestamp_String() {
        
    }

    public void testGetLDTTimestamp() {
        assertEquals(timeStamp, rate.getLDTTimestamp());
        assertNull(blankRate.getLDTTimestamp());
    }

    public void testSetTimestamp_LocalDateTime() {
        blankRate.setTimestamp(timeStamp);
        assertEquals(timeStamp, blankRate.getTimestamp());
        blankRate.setTimestamp("");
    }

    public void testGetValue() {
        assertEquals(TestGlobals.ZERO, blankRate.getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.ONEHUNDRED, rate.getValue(), TestGlobals.DELTA);
    }

    public void testSetValue() {
        rate.setValue(TestGlobals.ONEHUNDREDANDTWENTYFIVE);
        assertEquals(TestGlobals.ONEHUNDREDANDTWENTYFIVE, rate.getValue(), TestGlobals.DELTA);
        rate.setValue(TestGlobals.ONEHUNDRED);
    }

    public void testGetGrowth() {
        assertNull(rate.getGrowth());
    }

    public void testGetLastTrade() {
    }

    public void testSetLastTrade() {
    }

    public void testGetGofaiNextGrowth() {
    }

    public void testSetGofaiNextGrowth() {
    }

    public void testGetNeuralNetworkNextGrowth() {
    }

    public void testSetNeuralNetworkNextGrowth() {
    }

    public void testCalculateGrowth() {
        rate.calculateGrowth(TestGlobals.ONEHUNDRED);
        assertEquals(TestGlobals.ZERO, rate.getGrowth(), TestGlobals.DELTA);
        rate.calculateGrowth(TestGlobals.ZERO);
        assertEquals(Double.POSITIVE_INFINITY, rate.getGrowth(), TestGlobals.DELTA);
    }

    public void testIsMinuteAfter() {
    }

    public void testIsSameMinute() {
    }

    public void testToString() {
    }

    public void testCompareTo() {
    }
    
}
