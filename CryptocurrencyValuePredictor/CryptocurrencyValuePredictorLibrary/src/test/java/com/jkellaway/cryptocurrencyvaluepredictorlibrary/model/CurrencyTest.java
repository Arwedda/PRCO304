/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.GDAXAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
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
    private static GDAXAPIController controller;
    private static Currency blank;
    private static Currency rateless;
    private static Currency real;
    private static ExchangeRate rate;
    private static Gap gap;
    private static GDAXTrade[] blankHistTrades;
    private static GDAXTrade[] realHistTrades;
    private static LocalDateTime timeStamp;
    
    public CurrencyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        timeStamp = LocalDateTime.now(Clock.systemUTC()).minusMinutes(1);
        controller = new GDAXAPIController();
        rate = new ExchangeRate(TestGlobals.IDETH, timeStamp, TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
        blank = new Currency();
        rateless = new Currency(TestGlobals.IDLTC, TestGlobals.NAMELITECOIN, Globals.LTC_TRADES);
        real = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
        gap = new Gap(TestGlobals.LASTTRADE, timeStamp, 1);
        blankHistTrades = new GDAXTrade[100];
        realHistTrades = controller.getGDAXTrades(real.getGDAXEndpoint());
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        blank = new Currency();
        rateless = new Currency(TestGlobals.IDLTC, TestGlobals.NAMELITECOIN, Globals.LTC_TRADES);
        real = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    @Test
    public void testGetID() {
        assertEquals("unknown", blank.getID());
        assertEquals(TestGlobals.IDETH, real.getID());
    }

    @Test
    public void testSetID() {
        rateless.setID(TestGlobals.IDETH);
        assertEquals(TestGlobals.IDETH, rateless.getID());
        rateless.setID(TestGlobals.IDLTC);
    }

    @Test
    public void testGetName() {
        assertEquals("unknown", blank.getName());
        assertEquals("Litecoin", rateless.getName());
    }

    @Test
    public void testSetName() {
        blank.setName(TestGlobals.NAMEETHEREUM);
        assertEquals(TestGlobals.NAMEETHEREUM, blank.getName());
        blank.setName("unknown");
    }

    @Test
    public void testGetRate() {
        assertNull(blank.getRate());
        assertNull(rateless.getRate());
        assertEquals(rate, real.getRate());
    }

    @Test
    public void testSetValue() {
        blank.setValue(rate);
        
        assertEquals(rate, blank.getRate());
        assertEquals(TestGlobals.ONEHUNDRED, blank.getRate().getValue(), TestGlobals.DELTA);
        
        rate.setValue(TestGlobals.ONEHUNDREDANDTWENTYFIVE);
        real.setValue(rate);
        assertEquals(rate, real.getRate());
        assertEquals(TestGlobals.ONEHUNDREDANDTWENTYFIVE, real.getRate().getValue(), TestGlobals.DELTA);
        
        rate.setValue(TestGlobals.ONEHUNDRED);
    }

    @Test
    public void testGetRates() {
        List<ExchangeRate> rates = new ArrayList<>();
        assertArrayEquals(rates.toArray(), blank.getRates().toArray());
        assertArrayEquals(rates.toArray(), rateless.getRates().toArray());
        
        rates.add(rate);
        assertArrayEquals(rates.toArray(), real.getRates().toArray());
    }

    @Test
    public void testGetHistoricRates() {
        ArrayList<ExchangeRate> rates = new ArrayList<>();
        assertArrayEquals(rates.toArray(), blank.getHistoricRates().toArray());
        assertArrayEquals(rates.toArray(), rateless.getHistoricRates().toArray());
        assertArrayEquals(rates.toArray(), real.getHistoricRates().toArray());
    }

    @Test
    public void testAddHistoricRate() {
        List<ExchangeRate> rates = new ArrayList<>();
        rates.add(rate);
        real.addHistoricRate(rate);
        assertArrayEquals(rates.toArray(), real.getHistoricRates().toArray());
        assertEquals(rate, real.getHistoricRates().get(0));
    }

    @Test
    public void testGetLastHistoricTrade() {
        assertNull(blank.getLastHistoricTrade());
        assertNull(rateless.getLastHistoricTrade());
        assertNull(real.getLastHistoricTrade());
    }

    @Test
    public void testHasFoundPosition() {
        assertFalse(blank.hasFoundPosition());
        assertFalse(rateless.hasFoundPosition());
        assertFalse(real.hasFoundPosition());
        
        blank.addHistoricTrade(new GDAXTrade());
        assertTrue(blank.hasFoundPosition());
        blank = new Currency();
        
        assertFalse(blank.hasFoundPosition());
        blank.addHistoricRate(rate);
        assertTrue(blank.hasFoundPosition());
    }

    @Test
    public void testGetGDAXEndpoint() {
        assertEquals("unknown", blank.getGDAXEndpoint());
        assertEquals(Globals.LTC_TRADES, rateless.getGDAXEndpoint());
        assertEquals(Globals.ETH_TRADES, real.getGDAXEndpoint());
    }

    @Test
    public void testGetHistoricTrades() {
        List<GDAXTrade> historicTrades = new ArrayList<>();
        assertArrayEquals(historicTrades.toArray(), blank.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), rateless.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), real.getHistoricTrades().toArray());
    }

    @Test
    public void testAddHistoricTrade() {
        List<GDAXTrade> trades = new ArrayList<>();
        trades.add(blankHistTrades[0]);
        real.addHistoricTrade(blankHistTrades[0]);
        assertArrayEquals(trades.toArray(), real.getHistoricTrades().toArray());
        assertEquals(blankHistTrades[0], real.getHistoricTrades().get(0));
        trades.add(realHistTrades[0]);
        real.addHistoricTrade(realHistTrades[0]);
        assertArrayEquals(trades.toArray(), real.getHistoricTrades().toArray());
        assertEquals(realHistTrades[0], real.getHistoricTrades().get(1));
    }

    @Test
    public void testGetGaps() {
        List<Gap> gaps = new ArrayList<>();
        assertEquals(gaps, blank.getGaps());
        assertEquals(gaps, rateless.getGaps());
        real.getGaps().add(gap);
        assertNotEquals(gaps, real.getGaps());
        gaps.add(gap);
        assertEquals(gaps, real.getGaps());
        gaps.remove(gap);
        real.getGaps().remove(gap);
    }

    @Test
    public void testGetLastGap() {
        real.getGaps().add(gap);
        assertNull(blank.getLastGap());
        assertNull(rateless.getLastGap());
        assertEquals(gap, real.getLastGap());
        real.getGaps().remove(gap);
    }

    @Test
    public void testGradualMerge() {
        List<ExchangeRate> rates = new ArrayList<>();
        blank.gradualMerge();
        assertNull(blank.getLastGap());
        rates.add(rate);
        rates.add(rate);
        real.addHistoricRate(rate);
        assertNotEquals(rates, real.getHistoricRates());
        assertNotEquals(rates, real.getRates());
        real.gradualMerge();
        assertEquals(rates, real.getRates());
        assertTrue(real.getHistoricRates().isEmpty());
    }

    @Test
    public void testDumpDuplicates() {
        ExchangeRate newRate = new ExchangeRate(TestGlobals.IDETH, timeStamp.plusMinutes(1), TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
        ExchangeRate newRate2 = new ExchangeRate(TestGlobals.IDETH, timeStamp.plusMinutes(2), TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
        assertFalse(blank.dumpDuplicates(false));
        assertFalse(rateless.dumpDuplicates(false));
        assertFalse(real.dumpDuplicates(false));
        assertTrue(blank.dumpDuplicates(true));
        assertTrue(rateless.dumpDuplicates(true));
        assertTrue(real.dumpDuplicates(true));
        
        real.addHistoricRate(rate);
        assertTrue(real.dumpDuplicates(true));
        real.gradualMerge();
        assertEquals(1, real.getRates().size());
        
        real.addHistoricRate(rate);
        assertFalse(real.dumpDuplicates(false));
        real.gradualMerge();
        assertEquals(1, real.getRates().size());
        
        real.addHistoricRate(newRate);
        assertFalse(real.dumpDuplicates(true));
        real.gradualMerge();
        assertEquals(2, real.getRates().size());
        
        real.addHistoricRate(newRate2);
        assertFalse(real.dumpDuplicates(false));
        real.gradualMerge();
        assertEquals(3, real.getRates().size());
    }

    @Test
    public void testFindGaps() {
        blank.findGaps(timeStamp.minusMinutes(10));
        rateless.findGaps(timeStamp.minusMinutes(10));
        real.findGaps(timeStamp.minusMinutes(10));
        
        assertEquals((long) 11, (long) blank.getLastGap().getRatesRequired());
        assertEquals((long) 11, (long) rateless.getLastGap().getRatesRequired());
        assertEquals((long) 0, (long) real.getLastGap().getRatesRequired());
        assertEquals((long) 10, (long)real.getGaps().get(0).getRatesRequired());
    }

    @Test
    public void testMergeRates() {
        ExchangeRate newRate = new ExchangeRate(TestGlobals.IDETH, timeStamp.plusMinutes(1), TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
        ExchangeRate newRate2 = new ExchangeRate(TestGlobals.IDETH, timeStamp.plusMinutes(2), TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
        real.addHistoricRate(newRate2);
        real.addHistoricRate(newRate);
        real.mergeRates();
        assertTrue(real.getRates().get(1).isMinuteAfter(real.getRates().get(0)));
        assertTrue(real.getRates().get(2).isMinuteAfter(real.getRates().get(1)));
        assertTrue(real.getHistoricRates().isEmpty());
        assertEquals(3, real.getRates().size());
    }

    @Test
    public void testCalculateGrowth() {
        blank.setValue(rate);
        ExchangeRate newRate = new ExchangeRate(TestGlobals.IDBCH, rate.getLDTTimestamp().plusMinutes(1), TestGlobals.ONEHUNDREDANDTWENTYFIVE, null, null, null, TestGlobals.LASTTRADE);
        blank.setValue(newRate);
        blank.mergeRates();
        
        assertEquals(rate, blank.getRates().get(0));
        assertEquals(newRate, blank.getRates().get(1));
        assertEquals(25.0, blank.getRates().get(1).getGrowth(), TestGlobals.DELTA);
    }

    @Test
    public void testNoOfHistoricRates() {
        assertEquals(0, blank.noOfHistoricRates());
        assertEquals(0, rateless.noOfHistoricRates());
        real.addHistoricRate(rate);
        assertEquals(1, real.noOfHistoricRates());
    }
}
