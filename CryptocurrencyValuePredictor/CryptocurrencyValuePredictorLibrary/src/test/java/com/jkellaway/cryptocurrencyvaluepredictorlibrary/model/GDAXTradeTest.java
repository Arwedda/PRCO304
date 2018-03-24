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
import org.junit.Test;

/**
 *
 * @author jkell
 */
public class GDAXTradeTest {
    GDAXTrade blankTrade;
    GDAXTrade trade;
    LocalDateTime timeStamp;
    
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
        timeStamp = LocalDateTime.now();
        blankTrade = new GDAXTrade();
        trade = new GDAXTrade(timeStamp, TestGlobals.ONEHUNDRED, TestGlobals.LASTTRADE);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetTime() {
        assertNull(blankTrade.getTime());
        assertEquals(timeStamp, trade.getTime());
    }

    @Test
    public void testSetTime() {
        blankTrade.setTime(timeStamp);
        assertEquals(timeStamp, blankTrade.getTime());
        blankTrade.setTime(null);
    }

    @Test
    public void testGetPrice() {
        assertEquals(TestGlobals.ZERO, blankTrade.getPrice(), TestGlobals.DELTA);
        assertEquals(TestGlobals.ONEHUNDRED, trade.getPrice(), TestGlobals.DELTA);
    }

    @Test
    public void testSetPrice() {
        blankTrade.setPrice(TestGlobals.ONE);
        assertEquals(TestGlobals.ONE, blankTrade.getPrice(), TestGlobals.DELTA);
        blankTrade.setPrice(TestGlobals.ZERO);
    }

    @Test
    public void testGetTrade_id() {
        assertEquals(null, blankTrade.getTrade_id());
        assertEquals(TestGlobals.LASTTRADE, trade.getTrade_id());
    }

    @Test
    public void testSetTrade_id() {
        blankTrade.setTrade_id(TestGlobals.LASTTRADE);
        assertEquals(TestGlobals.LASTTRADE, blankTrade.getTrade_id());
        blankTrade.setTrade_id(0);
    }

    @Test
    public void testTradesMatch() {
        blankTrade = trade;
        assertTrue(blankTrade.tradesMatch(trade));
        blankTrade = new GDAXTrade();
    }
}
