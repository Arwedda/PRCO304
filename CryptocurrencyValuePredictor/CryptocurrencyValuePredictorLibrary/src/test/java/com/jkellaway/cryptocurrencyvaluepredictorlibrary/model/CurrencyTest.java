/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers.GDAXAPIController;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
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
    GDAXAPIController controller;
    Currency blankCurrency;
    Currency ratelessCurrency;
    Currency currency;
    ExchangeRate rate;
    LocalDateTime timeStamp;
    
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
        timeStamp = LocalDateTime.now();
        controller = new GDAXAPIController();
        rate = new ExchangeRate(TestGlobals.IDBCH, timeStamp, TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
        blankCurrency = new Currency();
        ratelessCurrency = new Currency(TestGlobals.IDLTC, TestGlobals.NAMELITECOIN, Globals.LTC_TRADES);
        currency = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetID() {
        assertEquals("unknown", blankCurrency.getID());
        assertEquals(TestGlobals.IDETH, currency.getID());
    }

    @Test
    public void testSetID() {
        ratelessCurrency.setID(TestGlobals.IDETH);
        assertEquals(TestGlobals.IDETH, ratelessCurrency.getID());
        ratelessCurrency.setID(TestGlobals.IDLTC);
    }

    @Test
    public void testGetName() {
        assertEquals("unknown", blankCurrency.getName());
        assertEquals("LITECOIN", ratelessCurrency.getName());
    }

    @Test
    public void testSetName() {
        blankCurrency.setName(TestGlobals.NAMEETHEREUM);
        assertEquals(TestGlobals.NAMEETHEREUM, blankCurrency.getName());
        blankCurrency.setName("unknown");
    }

    @Test
    public void testGetRate() {
        assertNull(blankCurrency.getRate());
        assertNull(ratelessCurrency.getRate());
        assertEquals(rate, currency.getRate());
    }

    @Test
    public void testSetValue() {
        blankCurrency.setValue(rate);
        
        assertEquals(rate, blankCurrency.getRate());
        assertEquals(TestGlobals.ONEHUNDRED, blankCurrency.getRate().getValue(), TestGlobals.DELTA);
        
        rate.setValue(TestGlobals.ONEHUNDREDANDTWENTYFIVE);
        currency.setValue(rate);
        assertEquals(rate, currency.getRate());
        assertEquals(TestGlobals.ONEHUNDREDANDTWENTYFIVE, currency.getRate().getValue(), TestGlobals.DELTA);
        
        rate.setValue(TestGlobals.ONEHUNDRED);
        blankCurrency = new Currency();
        currency = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    @Test
    public void testGetRates() {
        List<ExchangeRate> rates = new ArrayList<>();
        assertArrayEquals(rates.toArray(), blankCurrency.getRates().toArray());
        assertArrayEquals(rates.toArray(), ratelessCurrency.getRates().toArray());
        
        rates.add(rate);
        assertArrayEquals(rates.toArray(), currency.getRates().toArray());
    }

    @Test
    public void testGetHistoricRates() {
        ArrayList<ExchangeRate> rates = new ArrayList<>();
        assertArrayEquals(rates.toArray(), blankCurrency.getHistoricRates().toArray());
        assertArrayEquals(rates.toArray(), ratelessCurrency.getHistoricRates().toArray());
        assertArrayEquals(rates.toArray(), currency.getHistoricRates().toArray());
    }

    @Test
    public void testAddHistoricRate() {
        List<ExchangeRate> rates = new ArrayList<>();
        rates.add(rate);
        currency.addHistoricRate(rate);
        assertArrayEquals(rates.toArray(), currency.getHistoricRates().toArray());
        assertEquals(rate, currency.getHistoricRates().get(0));
        
        currency = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    @Test
    public void testGetLastHistoricTrade() {
        assertNull(blankCurrency.getLastHistoricTrade());
        assertNull(ratelessCurrency.getLastHistoricTrade());
        assertNull(currency.getLastHistoricTrade());
    }

    @Test
    public void testHasFoundPosition() {
        assertFalse(blankCurrency.hasFoundPosition());
        assertFalse(ratelessCurrency.hasFoundPosition());
        assertFalse(currency.hasFoundPosition());
        
        blankCurrency.addHistoricTrade(new GDAXTrade());
        assertTrue(blankCurrency.hasFoundPosition());
        blankCurrency = new Currency();
        
        assertFalse(blankCurrency.hasFoundPosition());
        blankCurrency.addHistoricRate(rate);
        assertTrue(blankCurrency.hasFoundPosition());
        blankCurrency = new Currency();
    }

    @Test
    public void testGetGDAXEndpoint() {
        assertEquals("unknown", blankCurrency.getGDAXEndpoint());
        assertEquals(Globals.LTC_TRADES, ratelessCurrency.getGDAXEndpoint());
        assertEquals(Globals.ETH_TRADES, currency.getGDAXEndpoint());
    }

    @Test
    public void testGetHistoricTrades() {
        List<GDAXTrade> historicTrades = new ArrayList<>();
        assertArrayEquals(historicTrades.toArray(), blankCurrency.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), ratelessCurrency.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), currency.getHistoricTrades().toArray());
    }

    @Test
    public void testAddHistoricTrade() {
        List<GDAXTrade> trades = new ArrayList<>();
        GDAXTrade trade = new GDAXTrade();
        trades.add(trade);
        currency.addHistoricTrade(trade);
        assertArrayEquals(trades.toArray(), currency.getHistoricTrades().toArray());
        assertEquals(trade, currency.getHistoricTrades().get(0));
        
        currency = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    @Test
    public void testGetGaps() {
    }

    @Test
    public void testGetLastGap() {
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
    public void testMergeRates() {
        LocalDateTime now = LocalDateTime.now().plusSeconds(1);
        blankCurrency.addHistoricRate(rate);
        ExchangeRate newRate = new ExchangeRate(TestGlobals.IDBCH, now, TestGlobals.ONEHUNDREDANDTWENTYFIVE, null, null, null, TestGlobals.LASTTRADE);
        blankCurrency.setValue(newRate);
        blankCurrency.mergeRates();
        
        assertEquals(rate, blankCurrency.getRates().get(0));
        assertEquals(newRate, blankCurrency.getRates().get(1));
        assertEquals(25.0, blankCurrency.getRates().get(1).getGrowth(), TestGlobals.DELTA);
    }

    @Test
    public void testCalculateGrowth() {
    }

    @Test
    public void testNoOfHistoricRates() {
    }
}
