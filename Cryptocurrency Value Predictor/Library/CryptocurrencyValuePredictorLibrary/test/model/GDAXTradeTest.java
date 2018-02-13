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
public class GDAXTradeTest {
    GDAXTrade blankTrade;
    GDAXTrade trade;
    LocalDateTime timeStamp;
    static final double ZERO = 0.0;
    static final double ONE = 1.0;
    static final double ONEHUNDRED = 100.0;
    static final double ONEHUNDREDANDTWENTYFIVE = 125.0;
    static final double DELTA = 1e-15;
    static final String ID777 = "777";
    
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
        trade = new GDAXTrade(timeStamp, ONEHUNDRED, ID777);
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
        assertEquals(ZERO, blankTrade.getPrice(), DELTA);
        assertEquals(ONEHUNDRED, trade.getPrice(), DELTA);
    }

    @Test
    public void testSetPrice() {
        blankTrade.setPrice(ONE);
        assertEquals(ONE, blankTrade.getPrice(), DELTA);
        blankTrade.setPrice(ZERO);
    }

    @Test
    public void testGetTrade_id() {
        assertEquals("0", blankTrade.getID());
        assertEquals(ID777, trade.getID());
    }

    @Test
    public void testSetTrade_id() {
        blankTrade.setID(ID777);
        assertEquals(ID777, blankTrade.getID());
        blankTrade.setID("0");
    }

    @Test
    public void testTradesMatch() {
        blankTrade = trade;
        assertTrue(blankTrade.tradesMatch(trade));
        blankTrade = new GDAXTrade();
    }
}
