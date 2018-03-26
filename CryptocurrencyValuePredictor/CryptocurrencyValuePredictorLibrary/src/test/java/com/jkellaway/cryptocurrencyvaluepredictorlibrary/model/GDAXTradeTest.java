/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

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
public class GDAXTradeTest {
    private GDAXTrade blank;
    private GDAXTrade real;
    private LocalDateTime timeStamp;
    
    public GDAXTradeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        timeStamp = LocalDateTime.now(Clock.systemUTC());
        blank = new GDAXTrade();
        real = new GDAXTrade(timeStamp, TestGlobals.ONEHUNDRED, TestGlobals.LASTTRADE);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetTime() {
        assertNull(blank.getTime());
        assertEquals(timeStamp, real.getTime());
    }

    @Test
    public void testSetTime() {
        blank.setTime(timeStamp);
        assertEquals(timeStamp, blank.getTime());
        blank.setTime(null);
    }

    @Test
    public void testGetPrice() {
        assertEquals(TestGlobals.ZERO, blank.getPrice(), TestGlobals.DELTA);
        assertEquals(TestGlobals.ONEHUNDRED, real.getPrice(), TestGlobals.DELTA);
    }

    @Test
    public void testSetPrice() {
        blank.setPrice(TestGlobals.ONE);
        assertEquals(TestGlobals.ONE, blank.getPrice(), TestGlobals.DELTA);
        blank.setPrice(TestGlobals.ZERO);
    }

    @Test
    public void testGetTrade_id() {
        assertEquals(null, blank.getTrade_id());
        assertEquals(TestGlobals.LASTTRADE, real.getTrade_id());
    }

    @Test
    public void testSetTrade_id() {
        blank.setTrade_id(TestGlobals.LASTTRADE);
        assertEquals(TestGlobals.LASTTRADE, blank.getTrade_id());
        blank.setTrade_id(0);
    }

    @Test
    public void testTradesMatch() {
        blank = real;
        assertTrue(blank.tradesMatch(real));
        blank = new GDAXTrade();
        assertFalse(blank.tradesMatch(real));
    }
}
