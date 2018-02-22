/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controllers.GDAXAPIController;
import helpers.Globals;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class CurrencyTest {
    GDAXAPIController apiController;
    Currency blankCurrency;
    Currency ratelessCurrency;
    Currency currency;
    ExchangeRate rate;
    LocalDateTime timeStamp;
    static final String IDETH = "ETH";
    static final String IDLTC = "LTC";
    static final String NAMEETHEREUM = "ETHEREUM";
    static final String NAMELITECOIN = "LITECOIN";
    static final String LASTTRADE = "DOESNOTMATTER";
    static final double ONEHUNDRED = 100.0;
    static final double ONEHUNDREDANDTWENTYFIVE = 125.0;
    static final double DELTA = 1e-15;
    
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
        apiController = new GDAXAPIController();
        rate = new ExchangeRate(timeStamp, ONEHUNDRED, LASTTRADE);
        blankCurrency = new Currency();
        ratelessCurrency = new Currency(IDLTC, NAMELITECOIN, Globals.LTC_TRADES);
        currency = new Currency(IDETH, NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetID() {
        assertEquals("unknown", blankCurrency.getID());
        assertEquals(IDETH, currency.getID());
    }

    @Test
    public void testSetID() {
        ratelessCurrency.setID(IDETH);
        assertEquals(IDETH, ratelessCurrency.getID());
        ratelessCurrency.setID(IDLTC);
    }

    @Test
    public void testGetName() {
        assertEquals("unknown", blankCurrency.getName());
        assertEquals("LITECOIN", ratelessCurrency.getName());
    }

    @Test
    public void testSetName() {
        blankCurrency.setName(NAMEETHEREUM);
        assertEquals(NAMEETHEREUM, blankCurrency.getName());
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
        assertEquals(ONEHUNDRED, blankCurrency.getRate().getValue(), DELTA);
        
        rate.setValue(ONEHUNDREDANDTWENTYFIVE);
        currency.setValue(rate);
        assertEquals(rate, currency.getRate());
        assertEquals(ONEHUNDREDANDTWENTYFIVE, currency.getRate().getValue(), DELTA);
        
        rate.setValue(ONEHUNDRED);
        blankCurrency = new Currency();
        currency = new Currency(IDETH, NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    @Test
    public void testGetRates() {
        ArrayList<ExchangeRate> rates = new ArrayList<>();
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
        ArrayList<ExchangeRate> rates = new ArrayList<>();
        rates.add(rate);
        currency.addHistoricRate(rate);
        assertArrayEquals(rates.toArray(), currency.getHistoricRates().toArray());
        assertEquals(rate, currency.getHistoricRates().get(0));
        
        currency = new Currency(IDETH, NAMEETHEREUM, rate, Globals.ETH_TRADES);
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
    public void testIsCalculatingGOFAI() {
        assertFalse(blankCurrency.isCalculatingGOFAI());
        assertFalse(ratelessCurrency.isCalculatingGOFAI());
        assertFalse(currency.isCalculatingGOFAI());
        
        blankCurrency.mergeRates();
        assertTrue(blankCurrency.isCalculatingGOFAI());
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
        ArrayList<GDAXTrade> historicTrades = new ArrayList<>();
        assertArrayEquals(historicTrades.toArray(), blankCurrency.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), ratelessCurrency.getHistoricTrades().toArray());
        assertArrayEquals(historicTrades.toArray(), currency.getHistoricTrades().toArray());
    }

    @Test
    public void testAddHistoricTrade() {
        ArrayList<GDAXTrade> trades = new ArrayList<>();
        GDAXTrade trade = new GDAXTrade();
        trades.add(trade);
        currency.addHistoricTrade(trade);
        assertArrayEquals(trades.toArray(), currency.getHistoricTrades().toArray());
        assertEquals(trade, currency.getHistoricTrades().get(0));
        
        currency = new Currency(IDETH, NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    @Test
    public void testMergePrices() {
        LocalDateTime now = LocalDateTime.now().plusSeconds(1);
        blankCurrency.addHistoricRate(rate);
        ExchangeRate newRate = new ExchangeRate(now, ONEHUNDREDANDTWENTYFIVE, LASTTRADE);
        blankCurrency.setValue(newRate);
        blankCurrency.mergeRates();
        
        assertEquals(rate, blankCurrency.getRates().get(0));
        assertEquals(newRate, blankCurrency.getRates().get(1));
        assertEquals(25.0, blankCurrency.getRates().get(1).getGrowth(), DELTA);
    }

    @Test
    public void testGetNumberOfRatesCollected() {
        assertEquals(0, blankCurrency.getNumberOfRatesCollected());
        assertEquals(0, ratelessCurrency.getNumberOfRatesCollected());
        assertEquals(1, currency.getNumberOfRatesCollected());
        currency.addHistoricRate(rate);
        assertEquals(2, currency.getNumberOfRatesCollected());
        currency = new Currency(IDETH, NAMEETHEREUM, rate, Globals.ETH_TRADES);
    }

    @Test
    public void testMergeRates() {
    }

    @Test
    public void testToString() {
    }
}