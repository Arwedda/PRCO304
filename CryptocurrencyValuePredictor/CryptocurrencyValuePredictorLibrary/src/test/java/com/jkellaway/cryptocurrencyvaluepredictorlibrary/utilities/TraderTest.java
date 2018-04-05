/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
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
public class TraderTest {
    private static Trader blank;
    private static Trader realGOFAICrypto;
    private static Trader realManualCrypto;
    private static Trader realNNCrypto;
    private static Trader realGOFAIUSD;
    private static Trader realManualUSD;
    private static Trader realNNUSD;
    private static Trader manualBest;
    private static Trader manualWorst;
    private static Trader[] holders;
    
    public TraderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        blank = new Trader();
        realGOFAICrypto = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.GOFAI, 1, HoldMode.CRYPTOCURRENCY);
        realManualCrypto = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.CRYPTOCURRENCY);
        realNNCrypto = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.NEURALNETWORK, 1, HoldMode.CRYPTOCURRENCY);
        realGOFAIUSD = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.GOFAI, 1, HoldMode.USD);
        realManualUSD = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.USD);
        realNNUSD = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.NEURALNETWORK, 1, HoldMode.USD);
        manualBest = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.BEST);
        manualWorst = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.WORST);
        holders = new Trader[4];
        holders[0] = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.BCH);
        holders[1] = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.BTC);
        holders[2] = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.ETH);
        holders[3] = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.LTC);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        blank = new Trader();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetWallet() {
        assertNotNull(blank.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, blank.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realGOFAICrypto.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, realGOFAICrypto.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realManualCrypto.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, realManualCrypto.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realNNCrypto.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, realNNCrypto.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realGOFAIUSD.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, realGOFAIUSD.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realManualUSD.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, realManualUSD.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realNNUSD.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, realNNUSD.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(manualBest.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, manualBest.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(manualWorst.getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, manualWorst.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(holders[0].getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, holders[0].getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(holders[1].getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, holders[1].getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(holders[2].getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, holders[2].getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(holders[3].getWallet());
        assertEquals(TestGlobals.ONEHUNDRED, holders[3].getWallet().getStartingUSD(), TestGlobals.DELTA);
    }

    @Test
    public void testGetTradeMode() {
        assertEquals(TradeMode.MANUAL, blank.getTradeMode());
        assertEquals(TradeMode.GOFAI, realGOFAICrypto.getTradeMode());
        assertEquals(TradeMode.MANUAL, realManualCrypto.getTradeMode());
        assertEquals(TradeMode.NEURALNETWORK, realNNCrypto.getTradeMode());
        assertEquals(TradeMode.GOFAI, realGOFAIUSD.getTradeMode());
        assertEquals(TradeMode.MANUAL, realManualUSD.getTradeMode());
        assertEquals(TradeMode.NEURALNETWORK, realNNUSD.getTradeMode());
        assertEquals(TradeMode.MANUAL, manualBest.getTradeMode());
        assertEquals(TradeMode.MANUAL, manualWorst.getTradeMode());
        assertEquals(TradeMode.MANUAL, holders[0].getTradeMode());
        assertEquals(TradeMode.MANUAL, holders[1].getTradeMode());
        assertEquals(TradeMode.MANUAL, holders[2].getTradeMode());
        assertEquals(TradeMode.MANUAL, holders[3].getTradeMode());
    }

    @Test
    public void testSetTradeMode() {
        blank.setTradeMode(TradeMode.GOFAI);
        assertEquals(TradeMode.GOFAI, blank.getTradeMode());
        blank.setTradeMode(TradeMode.NEURALNETWORK);
        assertEquals(TradeMode.NEURALNETWORK, blank.getTradeMode());
        blank.setTradeMode(TradeMode.MANUAL);
        assertEquals(TradeMode.MANUAL, blank.getTradeMode());
    }

    @Test
    public void testGetTradeModeIndex() {
        assertEquals(-1, blank.getTradeModeIndex());
        assertEquals(1, realGOFAICrypto.getTradeModeIndex());
        assertEquals(-1, realManualCrypto.getTradeModeIndex());
        assertEquals(1, realNNCrypto.getTradeModeIndex());
        assertEquals(1, realGOFAIUSD.getTradeModeIndex());
        assertEquals(-1, realManualUSD.getTradeModeIndex());
        assertEquals(1, realNNUSD.getTradeModeIndex());
        assertEquals(-1, manualBest.getTradeModeIndex());
        assertEquals(-1, manualWorst.getTradeModeIndex());
        assertEquals(-1, holders[0].getTradeModeIndex());
        assertEquals(-1, holders[1].getTradeModeIndex());
        assertEquals(-1, holders[2].getTradeModeIndex());
        assertEquals(-1, holders[3].getTradeModeIndex());
    }

    @Test
    public void testSetTradeModeIndex() {
         blank.setTradeModeIndex(1);
         assertEquals(0, blank.getTradeModeIndex());
         blank.setTradeModeIndex(10);
         assertEquals(9, blank.getTradeModeIndex());
    }

    @Test
    public void testSetHoldMode() {
        blank.setHoldMode(HoldMode.BCH);
        assertEquals(HoldMode.BCH, blank.getHoldMode());
        blank.setHoldMode(HoldMode.BEST);
        assertEquals(HoldMode.BEST, blank.getHoldMode());
        blank.setHoldMode(HoldMode.BTC);
        assertEquals(HoldMode.BTC, blank.getHoldMode());
        blank.setHoldMode(HoldMode.CRYPTOCURRENCY);
        assertEquals(HoldMode.CRYPTOCURRENCY, blank.getHoldMode());
        blank.setHoldMode(HoldMode.ETH);
        assertEquals(HoldMode.ETH, blank.getHoldMode());
        blank.setHoldMode(HoldMode.LTC);
        assertEquals(HoldMode.LTC, blank.getHoldMode());
        blank.setHoldMode(HoldMode.USD);
        assertEquals(HoldMode.USD, blank.getHoldMode());
        blank.setHoldMode(HoldMode.WORST);
        assertEquals(HoldMode.WORST, blank.getHoldMode());
    }

    @Test
    public void testGetHoldMode() {
        assertEquals(HoldMode.USD, blank.getHoldMode());
        assertEquals(HoldMode.CRYPTOCURRENCY, realGOFAICrypto.getHoldMode());
        assertEquals(HoldMode.CRYPTOCURRENCY, realManualCrypto.getHoldMode());
        assertEquals(HoldMode.CRYPTOCURRENCY, realNNCrypto.getHoldMode());
        assertEquals(HoldMode.USD, realGOFAIUSD.getHoldMode());
        assertEquals(HoldMode.USD, realManualUSD.getHoldMode());
        assertEquals(HoldMode.USD, realNNUSD.getHoldMode());
        assertEquals(HoldMode.BEST, manualBest.getHoldMode());
        assertEquals(HoldMode.WORST, manualWorst.getHoldMode());
        assertEquals(HoldMode.BCH, holders[0].getHoldMode());
        assertEquals(HoldMode.BTC, holders[1].getHoldMode());
        assertEquals(HoldMode.ETH, holders[2].getHoldMode());
        assertEquals(HoldMode.LTC, holders[3].getHoldMode());
    }

    @Test
    public void testAutoTrade() {
    }

    @Test
    public void testTradeBenchmark_CurrencyArr_int() {
    }

    @Test
    public void testTradeBenchmark_CurrencyArr() {
    }

    @Test
    public void testTrade() {
    }
}
