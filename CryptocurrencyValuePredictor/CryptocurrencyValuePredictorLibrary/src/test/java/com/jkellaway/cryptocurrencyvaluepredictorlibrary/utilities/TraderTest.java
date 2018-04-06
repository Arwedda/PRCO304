/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import java.time.Clock;
import java.time.LocalDateTime;
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
    private static Trader realNNCrypto;
    private static Trader realGOFAIUSD;
    private static Trader realManualUSD;
    private static Trader realNNUSD;
    private static Trader manualBest;
    private static Trader manualWorst;
    private static Trader[] holders;
    private static Currency[] decliningCurrencies;
    private static Currency[] growingCurrencies;
    
    public TraderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());
        ExchangeRate rate;
        decliningCurrencies = new Currency[4];
        growingCurrencies = new Currency[4];
        Currency newCurrency = new Currency("BCH", "Bitcoin Cash", Globals.BCH_TRADES);
        decliningCurrencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", Globals.BTC_TRADES);
        decliningCurrencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", Globals.ETH_TRADES);
        decliningCurrencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", Globals.LTC_TRADES);
        decliningCurrencies[3] = newCurrency;
        
        newCurrency = new Currency("BCH", "Bitcoin Cash", Globals.BCH_TRADES);
        growingCurrencies[0] = newCurrency;
        newCurrency = new Currency("BTC", "Bitcoin", Globals.BTC_TRADES);
        growingCurrencies[1] = newCurrency;
        newCurrency = new Currency("ETH", "Ethereum", Globals.ETH_TRADES);
        growingCurrencies[2] = newCurrency;
        newCurrency = new Currency("LTC", "Litecoin", Globals.LTC_TRADES);
        growingCurrencies[3] = newCurrency;
        
        for (int i = 0; i < TestGlobals.REQUIREDRATES + 10; i++) {
            rate = new ExchangeRate(TestGlobals.IDBCH, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            decliningCurrencies[0].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDBTC, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            decliningCurrencies[1].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDETH, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            decliningCurrencies[2].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDLTC, timestamp.minusMinutes(i), Double.valueOf(i), null, null, null, TestGlobals.LASTTRADE);
            decliningCurrencies[3].addHistoricRate(rate);
            
            rate = new ExchangeRate(TestGlobals.IDBCH, timestamp.minusMinutes(i), Double.valueOf(TestGlobals.REQUIREDRATES + 10 - i), null, null, null, TestGlobals.LASTTRADE);
            growingCurrencies[0].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDBTC, timestamp.minusMinutes(i), Double.valueOf(TestGlobals.REQUIREDRATES + 10 - i), null, null, null, TestGlobals.LASTTRADE);
            growingCurrencies[1].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDETH, timestamp.minusMinutes(i), Double.valueOf(TestGlobals.REQUIREDRATES + 10 - i), null, null, null, TestGlobals.LASTTRADE);
            growingCurrencies[2].addHistoricRate(rate);
            rate = new ExchangeRate(TestGlobals.IDLTC, timestamp.minusMinutes(i), Double.valueOf(TestGlobals.REQUIREDRATES + 10 - i), null, null, null, TestGlobals.LASTTRADE);
            growingCurrencies[3].addHistoricRate(rate);
        }
        
        for (int i = 0; i < decliningCurrencies.length; i++) {
            decliningCurrencies[i].mergeRates();
            decliningCurrencies[i].calculateGrowth(true);
            growingCurrencies[i].mergeRates();
            growingCurrencies[i].calculateGrowth(true);
        }
        
        decliningCurrencies = GOFAIPredictor.initialPredictions(decliningCurrencies);
        growingCurrencies = GOFAIPredictor.initialPredictions(growingCurrencies);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        blank = new Trader();
        realGOFAICrypto = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.GOFAI, 1, HoldMode.CRYPTOCURRENCY);
        realNNCrypto = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.NEURALNETWORK, 1, HoldMode.CRYPTOCURRENCY);
        realGOFAIUSD = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.GOFAI, 1, HoldMode.USD);
        realManualUSD = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.USD);
        realNNUSD = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.NEURALNETWORK, 1, HoldMode.USD);
        manualBest = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.BEST);
        manualWorst = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.WORST);
        holders = new Trader[4];
        holders[0] = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.BCH);
        holders[1] = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.BTC);
        holders[2] = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.ETH);
        holders[3] = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.LTC);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetWallet() {
        assertNotNull(blank.getWallet());
        assertEquals(100.0, blank.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realGOFAICrypto.getWallet());
        assertEquals(100.0, realGOFAICrypto.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realNNCrypto.getWallet());
        assertEquals(100.0, realNNCrypto.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realGOFAIUSD.getWallet());
        assertEquals(100.0, realGOFAIUSD.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realManualUSD.getWallet());
        assertEquals(100.0, realManualUSD.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(realNNUSD.getWallet());
        assertEquals(100.0, realNNUSD.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(manualBest.getWallet());
        assertEquals(100.0, manualBest.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(manualWorst.getWallet());
        assertEquals(100.0, manualWorst.getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(holders[0].getWallet());
        assertEquals(100.0, holders[0].getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(holders[1].getWallet());
        assertEquals(100.0, holders[1].getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(holders[2].getWallet());
        assertEquals(100.0, holders[2].getWallet().getStartingUSD(), TestGlobals.DELTA);
        assertNotNull(holders[3].getWallet());
        assertEquals(100.0, holders[3].getWallet().getStartingUSD(), TestGlobals.DELTA);
    }

    @Test
    public void testGetTradeMode() {
        assertEquals(TradeMode.MANUAL, blank.getTradeMode());
        assertEquals(TradeMode.GOFAI, realGOFAICrypto.getTradeMode());
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
        realGOFAICrypto.autoTrade(decliningCurrencies);
        assertEquals(100.0, realGOFAICrypto.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, realGOFAICrypto.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, realGOFAICrypto.getWallet().getHoldingID());
        realGOFAIUSD.autoTrade(decliningCurrencies);
        assertEquals(100.0, realGOFAIUSD.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, realGOFAIUSD.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, realGOFAIUSD.getWallet().getHoldingID());
        
        realGOFAICrypto = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.GOFAI, 1, HoldMode.CRYPTOCURRENCY);
        realGOFAIUSD = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.GOFAI, 1, HoldMode.USD);
        
        realGOFAICrypto.autoTrade(growingCurrencies);
        assertEquals(100.0, realGOFAICrypto.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(3.225806451612903, realGOFAICrypto.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, realGOFAICrypto.getWallet().getHoldingID());
        realGOFAIUSD.autoTrade(growingCurrencies);
        assertEquals(100.0, realGOFAIUSD.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(3.225806451612903, realGOFAIUSD.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, realGOFAIUSD.getWallet().getHoldingID());
    }

    @Test
    public void testTradeBenchmark_CurrencyArr_int() {
        Integer numberOfReadings = decliningCurrencies[0].getRates().size();
        blank.tradeBenchmark(decliningCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(100.0, blank.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, blank.getWallet().getHoldingID());
        blank.tradeBenchmark(growingCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(100.0, blank.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, blank.getWallet().getHoldingID());
        
        for (Trader trader : holders) {
            assertEquals(TestGlobals.IDUSD, trader.getWallet().getHoldingID());
            assertEquals(100.0, trader.getWallet().getValue(), TestGlobals.DELTA);
            trader.tradeBenchmark(decliningCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
            assertEquals(trader.getHoldMode().toString(), trader.getWallet().getHoldingID());
            assertEquals(11.11111111111111, trader.getWallet().getValue(), TestGlobals.DELTA);
            assertEquals(0.0, trader.getWallet().getUSDValue(decliningCurrencies), TestGlobals.DELTA);
            trader.tradeBenchmark(growingCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
            assertEquals(trader.getHoldMode().toString(), trader.getWallet().getHoldingID());
            assertEquals(11.11111111111111, trader.getWallet().getValue(), TestGlobals.DELTA);
            assertEquals(344.44444444444446, trader.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        }
        manualBest.tradeBenchmark(decliningCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        manualWorst.tradeBenchmark(decliningCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(100.0, manualBest.getWallet().getUSDValue(decliningCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualBest.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualBest.getWallet().getHoldingID());
        assertEquals(0.0, manualWorst.getWallet().getUSDValue(decliningCurrencies), TestGlobals.DELTA);
        assertEquals(12.5, manualWorst.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, manualWorst.getWallet().getHoldingID());
        
        manualBest = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.BEST);
        manualWorst = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.WORST);
        
        manualBest.tradeBenchmark(growingCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        manualWorst.tradeBenchmark(growingCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(134.78260869565216, manualBest.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(4.3478260869565215, manualBest.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, manualBest.getWallet().getHoldingID());
        assertEquals(100.0, manualWorst.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualWorst.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualWorst.getWallet().getHoldingID());
        
        realManualUSD.tradeBenchmark(decliningCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(100.0, manualWorst.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualWorst.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualWorst.getWallet().getHoldingID());
        realManualUSD.tradeBenchmark(decliningCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS);
        assertEquals(100.0, manualWorst.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualWorst.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualWorst.getWallet().getHoldingID());
        
        realGOFAICrypto.tradeBenchmark(decliningCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(100.0, realGOFAICrypto.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, realGOFAICrypto.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, realGOFAICrypto.getWallet().getHoldingID());
        realGOFAIUSD.tradeBenchmark(decliningCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(100.0, realGOFAIUSD.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, realGOFAIUSD.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, realGOFAIUSD.getWallet().getHoldingID());
        
        realGOFAICrypto = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.GOFAI, 1, HoldMode.CRYPTOCURRENCY);
        realGOFAIUSD = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.GOFAI, 1, HoldMode.USD);
        
        realGOFAICrypto.tradeBenchmark(growingCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(140.90909090909093, realGOFAICrypto.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(4.545454545454546, realGOFAICrypto.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, realGOFAICrypto.getWallet().getHoldingID());
        realGOFAIUSD.tradeBenchmark(growingCurrencies, numberOfReadings, Globals.NUMBEROFPREDICTIONS + 1);
        assertEquals(140.90909090909093, realGOFAIUSD.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(4.545454545454546, realGOFAIUSD.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, realGOFAIUSD.getWallet().getHoldingID());
    }

    @Test
    public void testTradeBenchmark_CurrencyArr() {
        blank.tradeBenchmark(decliningCurrencies);
        assertEquals(100.0, blank.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, blank.getWallet().getHoldingID());
        blank.tradeBenchmark(growingCurrencies);
        assertEquals(100.0, blank.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, blank.getWallet().getHoldingID());
        
        for (Trader trader : holders) {
            assertEquals(TestGlobals.IDUSD, trader.getWallet().getHoldingID());
            assertEquals(100.0, trader.getWallet().getValue(), TestGlobals.DELTA);
            trader.tradeBenchmark(decliningCurrencies);
            assertEquals(trader.getHoldMode().toString(), trader.getWallet().getHoldingID());
            assertEquals(100.0, trader.getWallet().getValue(), TestGlobals.DELTA);
            assertEquals(0.0, trader.getWallet().getUSDValue(decliningCurrencies), TestGlobals.DELTA);
            trader.tradeBenchmark(growingCurrencies);
            assertEquals(trader.getHoldMode().toString(), trader.getWallet().getHoldingID());
            assertEquals(100.0, trader.getWallet().getValue(), TestGlobals.DELTA);
            assertEquals(3100.0, trader.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        }
        manualBest.tradeBenchmark(decliningCurrencies);
        manualWorst.tradeBenchmark(decliningCurrencies);
        assertEquals(100.0, manualBest.getWallet().getUSDValue(decliningCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualBest.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualBest.getWallet().getHoldingID());
        assertEquals(100.0, manualWorst.getWallet().getUSDValue(decliningCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualWorst.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualWorst.getWallet().getHoldingID());
        
        manualBest = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.BEST);
        manualWorst = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.MANUAL, -1, HoldMode.WORST);
        
        manualBest.tradeBenchmark(growingCurrencies);
        manualWorst.tradeBenchmark(growingCurrencies);
        assertEquals(100.0, manualBest.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(3.225806451612903, manualBest.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, manualBest.getWallet().getHoldingID());
        assertEquals(100.0, manualWorst.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualWorst.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualWorst.getWallet().getHoldingID());
        
        realManualUSD.tradeBenchmark(decliningCurrencies);
        assertEquals(100.0, manualWorst.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualWorst.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualWorst.getWallet().getHoldingID());
        realManualUSD.tradeBenchmark(decliningCurrencies);
        assertEquals(100.0, manualWorst.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, manualWorst.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, manualWorst.getWallet().getHoldingID());
        
        realGOFAICrypto.tradeBenchmark(decliningCurrencies);
        assertEquals(100.0, realGOFAICrypto.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, realGOFAICrypto.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, realGOFAICrypto.getWallet().getHoldingID());
        realGOFAIUSD.tradeBenchmark(decliningCurrencies);
        assertEquals(100.0, realGOFAIUSD.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(100.0, realGOFAIUSD.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDUSD, realGOFAIUSD.getWallet().getHoldingID());
        
        realGOFAICrypto = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.GOFAI, 1, HoldMode.CRYPTOCURRENCY);
        realGOFAIUSD = new Trader(TestGlobals.IDUSD, 100.0, TradeMode.GOFAI, 1, HoldMode.USD);
        
        realGOFAICrypto.tradeBenchmark(growingCurrencies);
        assertEquals(103.33333333333334, realGOFAICrypto.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(3.3333333333333335, realGOFAICrypto.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, realGOFAICrypto.getWallet().getHoldingID());
        realGOFAIUSD.tradeBenchmark(growingCurrencies);
        assertEquals(103.33333333333334, realGOFAIUSD.getWallet().getUSDValue(growingCurrencies), TestGlobals.DELTA);
        assertEquals(3.3333333333333335, realGOFAIUSD.getWallet().getValue(), TestGlobals.DELTA);
        assertEquals(TestGlobals.IDBCH, realGOFAIUSD.getWallet().getHoldingID());
    }
}
