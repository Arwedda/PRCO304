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

    public void testGetTime() {
        assertNull(blankTrade.getTime());
        assertEquals(timeStamp, trade.getTime());
    }

    public void testSetTime() {
        blankTrade.setTime(timeStamp);
        assertEquals(timeStamp, blankTrade.getTime());
        blankTrade.setTime(null);
    }

    public void testGetPrice() {
        assertEquals(TestGlobals.ZERO, blankTrade.getPrice(), TestGlobals.DELTA);
        assertEquals(TestGlobals.ONEHUNDRED, trade.getPrice(), TestGlobals.DELTA);
    }

    public void testSetPrice() {
        blankTrade.setPrice(TestGlobals.ONE);
        assertEquals(TestGlobals.ONE, blankTrade.getPrice(), TestGlobals.DELTA);
        blankTrade.setPrice(TestGlobals.ZERO);
    }

    public void testGetTrade_id() {
        assertEquals(Integer.valueOf(0), blankTrade.getTrade_id());
        assertEquals(TestGlobals.LASTTRADE, trade.getTrade_id());
    }

    public void testSetTrade_id() {
        blankTrade.setTrade_id(TestGlobals.LASTTRADE);
        assertEquals(TestGlobals.LASTTRADE, blankTrade.getTrade_id());
        blankTrade.setTrade_id(0);
    }

    public void testTradesMatch() {
        blankTrade = trade;
        assertTrue(blankTrade.tradesMatch(trade));
        blankTrade = new GDAXTrade();
    }
}
