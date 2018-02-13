/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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
public class ExchangeRateTest {
    ExchangeRate blankRate;
    ExchangeRate rate;
    LocalDateTime timeStamp;
    static final double ZERO = 0.0;
    static final double ONE = 1.0;
    static final double ONEHUNDRED = 100.0;
    static final double ONEHUNDREDANDTWENTYFIVE = 125.0;
    static final double DELTA = 1e-15;
    
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
        rate = new ExchangeRate(timeStamp, ONEHUNDRED);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetTimestamp() {
        assertEquals(timeStamp, rate.getTimestamp());
        assertNull(blankRate.getTimestamp());
    }

    @Test
    public void testSetTimestamp() {
        blankRate.setTimestamp(timeStamp);
        assertEquals(timeStamp, blankRate.getTimestamp());
        blankRate.setTimestamp(null);
    }

    @Test
    public void testGetValue() {
        assertEquals(ZERO, blankRate.getValue(), DELTA);
        assertEquals(ONEHUNDRED, rate.getValue(), DELTA);
        
    }

    @Test
    public void testSetValue() {
        rate.setValue(ONEHUNDREDANDTWENTYFIVE);
        assertEquals(ONEHUNDREDANDTWENTYFIVE, rate.getValue(), DELTA);
        rate.setValue(ONEHUNDRED);
    }

    @Test
    public void testGetGrowth() {
        assertNull(rate.getGrowth());
    }

    @Test
    public void testCalculateGrowth() {
        rate.calculateGrowth(ONEHUNDRED);
        assertEquals(ZERO, rate.getGrowth(), DELTA);
        rate.calculateGrowth(ZERO);
        assertEquals(Double.POSITIVE_INFINITY, rate.getGrowth(), DELTA);
    }
}
