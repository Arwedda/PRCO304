/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import java.time.LocalDateTime;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jkell
 */
public class GDAXTradeTest extends TestCase {
    GDAXTrade blankTrade;
    GDAXTrade trade;
    LocalDateTime timeStamp;
    
    public GDAXTradeTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GDAXTradeTest.class);
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        timeStamp = LocalDateTime.now();
        blankTrade = new GDAXTrade();
        trade = new GDAXTrade(timeStamp, TestGlobals.ONEHUNDRED, TestGlobals.LASTTRADE);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
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
