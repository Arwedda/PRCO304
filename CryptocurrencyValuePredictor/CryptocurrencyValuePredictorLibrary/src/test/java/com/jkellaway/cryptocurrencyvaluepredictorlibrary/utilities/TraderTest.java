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
    
    public TraderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        blank = new Trader();
        realGOFAICrypto = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.GOFAI, -1, HoldMode.CRYPTOCURRENCY);
        realManualCrypto = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.CRYPTOCURRENCY);
        realNNCrypto = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.NEURALNETWORK, -1, HoldMode.CRYPTOCURRENCY);
        realGOFAIUSD = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.GOFAI, -1, HoldMode.USD);
        realManualUSD = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.USD);
        realNNUSD = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.NEURALNETWORK, -1, HoldMode.USD);
        manualBest = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.BEST);
        manualWorst = new Trader(TestGlobals.IDUSD, TestGlobals.ONEHUNDRED, TradeMode.MANUAL, -1, HoldMode.WORST);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetWallet() {
    }

    @Test
    public void testGetTradeMode() {
    }

    @Test
    public void testSetTradeMode() {
    }

    @Test
    public void testGetTradeModeIndex() {
    }

    @Test
    public void testSetTradeModeIndex() {
    }

    @Test
    public void testSetHoldMode() {
    }

    @Test
    public void testGetHoldMode() {
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
