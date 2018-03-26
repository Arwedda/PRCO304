/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.GDAXAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class CurrencyTest {
    private GDAXAPIController controller;
    private Currency blank;
    private Currency rateless;
    private Currency real;
    private ExchangeRate rate;
    private Gap gap;
    private GDAXTrade blankHistTrade;
    private GDAXTrade realHistTrade;
    private LocalDateTime timeStamp;
    
    public CurrencyTest() {
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
        controller = new GDAXAPIController();
        blank = new Currency();
        rateless = new Currency(TestGlobals.IDLTC, TestGlobals.NAMELITECOIN, Globals.LTC_TRADES);
        real = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
        rate = new ExchangeRate(TestGlobals.IDBCH, timeStamp, TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
        gap = new Gap(TestGlobals.LASTTRADE, timeStamp, 1);
        blankHistTrade = new GDAXTrade();
        realHistTrade = (controller.getGDAXTrades(real.getGDAXEndpoint())[0]);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetID() {/*
        assertEquals("unknown", blank.getID());
        assertEquals(TestGlobals.IDETH, real.getID());
    */}

    @Test
    public void testSetID() {/*
        rateless.setID(TestGlobals.IDETH);
        assertEquals(TestGlobals.IDETH, rateless.getID());
        rateless.setID(TestGlobals.IDLTC);*/
    }

    @Test
    public void testGetName() {/*
        assertEquals("unknown", blank.getName());
        assertEquals("LITECOIN", rateless.getName());*/
    }

    @Test
    public void testSetName() {/*
        blank.setName(TestGlobals.NAMEETHEREUM);
        assertEquals(TestGlobals.NAMEETHEREUM, blank.getName());
        blank.setName("unknown");*/
    }

    @Test
    public void testGetRate() {/*
        assertNull(blank.getRate());
        assertNull(rateless.getRate());
        assertEquals(rate, real.getRate());*/
    }

    @Test
    public void testSetValue() {/*
        blank.setValue(rate);
        
        assertEquals(rate, blank.getRate());
        assertEquals(TestGlobals.ONEHUNDRED, blank.getRate().getValue(), TestGlobals.DELTA);
        
        rate.setValue(TestGlobals.ONEHUNDREDANDTWENTYFIVE);
        real.setValue(rate);
        assertEquals(rate, real.getRate());
        assertEquals(TestGlobals.ONEHUNDREDANDTWENTYFIVE, real.getRate().getValue(), TestGlobals.DELTA);
        
        rate.setValue(TestGlobals.ONEHUNDRED);
        blank = new Currency();
        real = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);*/
    }

    @Test
    public void testGetRates() {/*
        List<ExchangeRate> rates = new ArrayList<>();
        assertArrayEquals(rates.toArray(), blank.getRates().toArray());
        assertArrayEquals(rates.toArray(), rateless.getRates().toArray());
        
        rates.add(rate);
        assertArrayEquals(rates.toArray(), real.getRates().toArray());*/
    }

    @Test
    public void testGetHistoricRates() {/*
        ArrayList<ExchangeRate> rates = new ArrayList<>();
        assertArrayEquals(rates.toArray(), blank.getHistoricRates().toArray());
        assertArrayEquals(rates.toArray(), rateless.getHistoricRates().toArray());
        assertArrayEquals(rates.toArray(), real.getHistoricRates().toArray());*/
    }

    @Test
    public void testAddHistoricRate() {/*
        List<ExchangeRate> rates = new ArrayList<>();
        rates.add(rate);
        real.addHistoricRate(rate);
        assertArrayEquals(rates.toArray(), real.getHistoricRates().toArray());
        assertEquals(rate, real.getHistoricRates().get(0));
        
        real = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);*/
    }

    @Test
    public void testGetLastHistoricTrade() {/*
        assertNull(blank.getLastHistoricTrade());
        assertNull(rateless.getLastHistoricTrade());
        assertNull(real.getLastHistoricTrade());*/
    }

    @Test
    public void testHasFoundPosition() {/*
        assertFalse(blank.hasFoundPosition());
        assertFalse(rateless.hasFoundPosition());
        assertFalse(real.hasFoundPosition());
        
        blank.addHistoricTrade(new GDAXTrade());
        assertTrue(blank.hasFoundPosition());
        blank = new Currency();
        
        assertFalse(blank.hasFoundPosition());
        blank.addHistoricRate(rate);
        assertTrue(blank.hasFoundPosition());
        blank = new Currency();*/
    }

    @Test
    public void testGetGDAXEndpoint() {/*
        assertEquals("unknown", blank.getGDAXEndpoint());
        assertEquals(Globals.LTC_TRADES, rateless.getGDAXEndpoint());
        assertEquals(Globals.ETH_TRADES, real.getGDAXEndpoint());*/
    }

    @Test
    public void testGetHistoricTrades() {/*
        List<GDAXTrade> historicTrades = new ArrayList<>();
        assertArrayEquals(historicTrades.toArray(), blank.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), rateless.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), real.getHistoricTrades().toArray());*/
    }

    @Test
    public void testAddHistoricTrade() {/*
        List<GDAXTrade> trades = new ArrayList<>();
        GDAXTrade trade = new GDAXTrade();
        trades.add(trade);
        real.addHistoricTrade(trade);
        assertArrayEquals(trades.toArray(), real.getHistoricTrades().toArray());
        assertEquals(trade, real.getHistoricTrades().get(0));
        
        real = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);*/
    }

    @Test
    public void testGetGaps() {/*
        List<Gap> gaps = new ArrayList<>();
        assertEquals(gaps, blank.getGaps());
        assertEquals(gaps, rateless.getGaps());
        real.getGaps().add(gap);
        assertNotEquals(gaps, real.getGaps());
        gaps.add(gap);
        assertEquals(gaps, real.getGaps());
        gaps.remove(gap);
        real.getGaps().remove(gap);*/
    }

    @Test
    public void testGetLastGap() {/*
        real.getGaps().add(gap);
        assertNull(blank.getLastGap());
        assertNull(rateless.getLastGap());
        assertEquals(gap, real.getLastGap());
        real.getGaps().remove(gap);*/
    }

    @Test
    public void testGradualMerge() {
        
    }

    @Test
    public void testDumpDuplicates() {
        
    }

    @Test
    public void testFindGaps() {
        
    }

    @Test
    public void testMergeRates() {/*
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC()).plusSeconds(1);
        blank.addHistoricRate(rate);
        ExchangeRate newRate = new ExchangeRate(TestGlobals.IDBCH, now, TestGlobals.ONEHUNDREDANDTWENTYFIVE, null, null, null, TestGlobals.LASTTRADE);
        blank.setValue(newRate);
        blank.mergeRates();
        
        assertEquals(rate, blank.getRates().get(0));
        assertEquals(newRate, blank.getRates().get(1));
        assertEquals(25.0, blank.getRates().get(1).getGrowth(), TestGlobals.DELTA);*/
    }

    @Test
    public void testCalculateGrowth() {
        
    }

    @Test
    public void testNoOfHistoricRates() {
        
    }
}
