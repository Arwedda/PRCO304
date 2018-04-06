/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import java.time.Clock;
import java.time.LocalDateTime;
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
public class ExchangeRateTest {
    private ExchangeRate blank;
    private ExchangeRate real;
    private LocalDateTime timeStamp;
    private static Double[] doubles;
    
    public ExchangeRateTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        doubles = new Double[20];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = (double) i;
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        timeStamp = LocalDateTimeHelper.startOfMinute(LocalDateTime.now(Clock.systemUTC()));
        blank = new ExchangeRate();
        real = new ExchangeRate(TestGlobals.IDBCH, timeStamp, 100.0, null, null, null, TestGlobals.LASTTRADE);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetCurrency_id() {
        assertEquals("Unknown", blank.getCurrency_id());
        assertEquals(TestGlobals.IDBCH, real.getCurrency_id());
    }

    @Test
    public void testSetCurrency_id() {
        blank.setCurrency_id(TestGlobals.IDBTC);
        assertEquals(TestGlobals.IDBTC, blank.getCurrency_id());
        assertNotEquals(blank.getCurrency_id(), real.getCurrency_id());
        blank.setCurrency_id("Unknown");
    }

    @Test
    public void testGetTimestamp() {
        assertEquals("Unknown", blank.getTimestamp());
        assertEquals(String.valueOf(timeStamp), real.getTimestamp());
    }

    @Test
    public void testSetTimestamp_String() {
        blank.setTimestamp(String.valueOf(timeStamp));
        assertEquals(String.valueOf(timeStamp), blank.getTimestamp());
        assertEquals(timeStamp, blank.getLDTTimestamp());
        blank.setTimestamp("Unknown");
        assertEquals("Unknown", blank.getTimestamp());
        assertNull(blank.getLDTTimestamp());
    }

    @Test
    public void testGetLDTTimestamp() {
        assertEquals(timeStamp, real.getLDTTimestamp());
        assertNull(blank.getLDTTimestamp());
    }
    
    @Test
    public void testSetTimestamp_LocalDateTime() {
        blank.setTimestamp(timeStamp);
        assertEquals(timeStamp, blank.getLDTTimestamp());
        assertEquals(String.valueOf(timeStamp), blank.getTimestamp());
        blank.setTimestamp((LocalDateTime)null);
    }

    @Test
    public void testGetValue() {
        assertEquals(0.0, blank.getValue(), TestGlobals.DELTA);
        assertEquals(100.0, real.getValue(), TestGlobals.DELTA);
    }

    @Test
    public void testSetValue() {
        real.setValue(125.0);
        assertEquals(125.0, real.getValue(), TestGlobals.DELTA);
        real.setValue(100.0);
    }

    @Test
    public void testGetGrowth() {
        assertNull(real.getGrowth());
    }

    @Test
    public void testGetLastTrade() {
        assertNull(blank.getLastTrade());
        assertEquals(TestGlobals.LASTTRADE, real.getLastTrade());
    }

    @Test
    public void testSetLastTrade() {
        blank.setLastTrade(TestGlobals.LASTTRADE);
        assertEquals(TestGlobals.LASTTRADE, blank.getLastTrade());
        blank.setLastTrade(null);
        assertNull(blank.getLastTrade());
    }

    @Test
    public void testGetGofaiNextGrowth() {
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++) {
            assertNull(blank.getGofaiNextGrowth()[i]);
            assertNull(real.getGofaiNextGrowth()[i]);
        }
    }

    @Test
    public void testSetGofaiNextGrowth() {
        blank.setGofaiNextGrowth(doubles);
        for (int i = 0; i < doubles.length; i++) {
            assertEquals(i, blank.getGofaiNextGrowth()[i], TestGlobals.DELTA);
        }
        blank = new ExchangeRate();
    }

    @Test
    public void testGetNeuralNetworkNextGrowth() {
        for (int i = 0; i < Globals.NUMBEROFPREDICTIONS; i++) {
            assertNull(blank.getNeuralNetworkNextGrowth()[i]);
            assertNull(real.getNeuralNetworkNextGrowth()[i]);
        }
    }

    @Test
    public void testSetNeuralNetworkNextGrowth() {
        blank.setNeuralNetworkNextGrowth(doubles);
        for (int i = 0; i < doubles.length; i++) {
            assertEquals(i, blank.getNeuralNetworkNextGrowth()[i], TestGlobals.DELTA);
        }
        blank = new ExchangeRate();
    }

    @Test
    public void testCalculateGrowth() {
        real.calculateGrowth(100.0);
        assertEquals(0.0, real.getGrowth(), TestGlobals.DELTA);
        real.calculateGrowth(125.0);
        assertEquals(-20.0, real.getGrowth(), TestGlobals.DELTA);
        real.calculateGrowth(0.0);
        assertEquals(Double.POSITIVE_INFINITY, real.getGrowth(), TestGlobals.DELTA);
    }

    @Test
    public void testIsMinuteAfter() {
        assertFalse(blank.isMinuteAfter(real));
        blank.setTimestamp(timeStamp.minusMinutes(1));
        assertTrue(real.isMinuteAfter(blank));
        assertFalse(blank.isMinuteAfter(real));
        blank = new ExchangeRate();
    }

    @Test
    public void testIsSameMinute() {
        blank.setTimestamp(timeStamp);
        assertTrue(real.isSameMinute(real));
        assertTrue(real.isSameMinute(blank));
        blank = new ExchangeRate();
        assertFalse(blank.isSameMinute(real));
    }

    @Test
    public void testCompareTo() {
        blank.setTimestamp(timeStamp.minusMinutes(1));
        assertEquals(-1, blank.compareTo(real));
        blank.setTimestamp(timeStamp);
        assertEquals(0, blank.compareTo(real));
        blank.setTimestamp(timeStamp.plusMinutes(1));
        assertEquals(1, blank.compareTo(real));
    }
}
