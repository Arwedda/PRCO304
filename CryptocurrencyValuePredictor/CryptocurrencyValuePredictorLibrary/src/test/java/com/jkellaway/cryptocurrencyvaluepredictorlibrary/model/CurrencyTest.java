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
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jkell
 */
public class CurrencyTest extends TestCase {
    GDAXAPIController controller;
    Currency blankCurrency;
    Currency ratelessCurrency;
    Currency currency;
    ExchangeRate rate;
    LocalDateTime timeStamp;
    
    public CurrencyTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CurrencyTest.class);
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        timeStamp = LocalDateTime.now();
        controller = new GDAXAPIController();
        rate = new ExchangeRate(TestGlobals.IDBCH, timeStamp, TestGlobals.ONEHUNDRED, null, null, null, TestGlobals.LASTTRADE);
        blankCurrency = new Currency();
        ratelessCurrency = new Currency(TestGlobals.IDLTC, TestGlobals.NAMELITECOIN, Globals.LTC_TRADES);
        currency = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetID() {
        assertEquals("unknown", blankCurrency.getID());
        assertEquals(TestGlobals.IDETH, currency.getID());
    }

    public void testSetID() {
        ratelessCurrency.setID(TestGlobals.IDETH);
        assertEquals(TestGlobals.IDETH, ratelessCurrency.getID());
        ratelessCurrency.setID(TestGlobals.IDLTC);
    }

    public void testGetName() {
        assertEquals("unknown", blankCurrency.getName());
        assertEquals("LITECOIN", ratelessCurrency.getName());
    }

    public void testSetName() {
        blankCurrency.setName(TestGlobals.NAMEETHEREUM);
        assertEquals(TestGlobals.NAMEETHEREUM, blankCurrency.getName());
        blankCurrency.setName("unknown");
    }

    public void testGetRate() {
        assertNull(blankCurrency.getRate());
        assertNull(ratelessCurrency.getRate());
        assertEquals(rate, currency.getRate());
    }

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

    public void testGetRates() {
        List<ExchangeRate> rates = new ArrayList<>();
        assertArrayEquals(rates.toArray(), blankCurrency.getRates().toArray());
        assertArrayEquals(rates.toArray(), ratelessCurrency.getRates().toArray());
        
        rates.add(rate);
        assertArrayEquals(rates.toArray(), currency.getRates().toArray());
    }

    public void testGetHistoricRates() {
        ArrayList<ExchangeRate> rates = new ArrayList<>();
        assertArrayEquals(rates.toArray(), blankCurrency.getHistoricRates().toArray());
        assertArrayEquals(rates.toArray(), ratelessCurrency.getHistoricRates().toArray());
        assertArrayEquals(rates.toArray(), currency.getHistoricRates().toArray());
    }

    public void testAddHistoricRate() {
        List<ExchangeRate> rates = new ArrayList<>();
        rates.add(rate);
        currency.addHistoricRate(rate);
        assertArrayEquals(rates.toArray(), currency.getHistoricRates().toArray());
        assertEquals(rate, currency.getHistoricRates().get(0));
        
        currency = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    public void testGetLastHistoricTrade() {
        assertNull(blankCurrency.getLastHistoricTrade());
        assertNull(ratelessCurrency.getLastHistoricTrade());
        assertNull(currency.getLastHistoricTrade());
    }

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

    public void testGetGDAXEndpoint() {
        assertEquals("unknown", blankCurrency.getGDAXEndpoint());
        assertEquals(Globals.LTC_TRADES, ratelessCurrency.getGDAXEndpoint());
        assertEquals(Globals.ETH_TRADES, currency.getGDAXEndpoint());
    }

    public void testGetHistoricTrades() {
        List<GDAXTrade> historicTrades = new ArrayList<>();
        assertArrayEquals(historicTrades.toArray(), blankCurrency.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), ratelessCurrency.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), currency.getHistoricTrades().toArray());
    }

    public void testAddHistoricTrade() {
        List<GDAXTrade> trades = new ArrayList<>();
        GDAXTrade trade = new GDAXTrade();
        trades.add(trade);
        currency.addHistoricTrade(trade);
        assertArrayEquals(trades.toArray(), currency.getHistoricTrades().toArray());
        assertEquals(trade, currency.getHistoricTrades().get(0));
        
        currency = new Currency(TestGlobals.IDETH, TestGlobals.NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    public void testGetGaps() {
    }

    public void testGetLastGap() {
    }

    public void testGradualMerge() {
    }

    public void testDumpDuplicates() {
    }

    public void testFindGaps() {
    }

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

    public void testCalculateGrowth() {
    }

    public void testNoOfHistoricRates() {
    }
    
}
