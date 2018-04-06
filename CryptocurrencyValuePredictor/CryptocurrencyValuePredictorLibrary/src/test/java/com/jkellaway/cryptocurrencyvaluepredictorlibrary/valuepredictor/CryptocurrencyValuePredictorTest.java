/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.valuepredictor;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.HoldMode;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.PriceCollector;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.TradeMode;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities.Trader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author jkell
 */
public class CryptocurrencyValuePredictorTest {
    private static CryptocurrencyValuePredictor cryptocurrencyValuePredictor;
    
    public CryptocurrencyValuePredictorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        cryptocurrencyValuePredictor = CryptocurrencyValuePredictor.getInstance();
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
    public void testGetInstance() {
        assertNotNull(CryptocurrencyValuePredictor.getInstance());
    }

    @Test
    public void testGetCurrencies() {
        Currency[] currencies = cryptocurrencyValuePredictor.getCurrencies();
        assertEquals(4, currencies.length);
        assertEquals(TestGlobals.IDBCH, currencies[0].getID());
        assertEquals(TestGlobals.IDBTC, currencies[1].getID());
        assertEquals(TestGlobals.IDETH, currencies[2].getID());
        assertEquals(TestGlobals.IDLTC, currencies[3].getID());
    }

    @Test
    public void testGetPriceCollector() {
        PriceCollector pc = cryptocurrencyValuePredictor.getPriceCollector();
        assertArrayEquals(pc.getCurrencies(), cryptocurrencyValuePredictor.getCurrencies());
    }

    @Test
    public void testGetTrader() {
        Trader trader = cryptocurrencyValuePredictor.getTrader();
        assertEquals(trader, cryptocurrencyValuePredictor.getTrader());
        assertEquals(HoldMode.USD, trader.getHoldMode());
        assertEquals(TradeMode.MANUAL, trader.getTradeMode());
    }

    @Test
    public void testGetBest() {
        Trader trader = cryptocurrencyValuePredictor.getBest();
        assertEquals(trader, cryptocurrencyValuePredictor.getBest());
        assertEquals(HoldMode.BEST, trader.getHoldMode());
        assertEquals(TradeMode.MANUAL, trader.getTradeMode());
    }

    @Test
    public void testGetWorst() {
        Trader trader = cryptocurrencyValuePredictor.getWorst();
        assertEquals(trader, cryptocurrencyValuePredictor.getWorst());
        assertEquals(HoldMode.WORST, trader.getHoldMode());
        assertEquals(TradeMode.MANUAL, trader.getTradeMode());
    }

    @Test
    public void testGetHolders() {
        Trader[] traders = cryptocurrencyValuePredictor.getHolders();
        assertEquals(4, traders.length);
        for (int i = 0; i < traders.length; i++) {
            assertEquals(traders[i], cryptocurrencyValuePredictor.getHolders()[i]);
            assertEquals(TradeMode.MANUAL, traders[i].getTradeMode());
        }
        assertEquals(HoldMode.BCH, traders[0].getHoldMode());
        assertEquals(HoldMode.BTC, traders[1].getHoldMode());
        assertEquals(HoldMode.ETH, traders[2].getHoldMode());
        assertEquals(HoldMode.LTC, traders[3].getHoldMode());
    }

    @Test
    public void testGetGOFAITradersUSD() {
        Trader[] traders = cryptocurrencyValuePredictor.getGOFAITradersUSD();
        assertEquals(20, traders.length);
        for (int i = 0; i < traders.length; i++) {
            assertEquals(traders[i], cryptocurrencyValuePredictor.getGOFAITradersUSD()[i]);
            assertEquals(TradeMode.GOFAI, traders[i].getTradeMode());
            assertEquals(HoldMode.USD, traders[i].getHoldMode());
        }
    }

    @Test
    public void testGetGOFAITradersHold() {
        Trader[] traders = cryptocurrencyValuePredictor.getGOFAITradersHold();
        assertEquals(20, traders.length);
        for (int i = 0; i < traders.length; i++) {
            assertEquals(traders[i], cryptocurrencyValuePredictor.getGOFAITradersHold()[i]);
            assertEquals(TradeMode.GOFAI, traders[i].getTradeMode());
            assertEquals(HoldMode.CRYPTOCURRENCY, traders[i].getHoldMode());
        }
    }

    @Test
    public void testGetLap() {
        PriceCollector pc = cryptocurrencyValuePredictor.getPriceCollector();
        assertEquals(pc.getLap(), cryptocurrencyValuePredictor.getLap());
    }

    @Test
    public void testStartAndStopTrading() {
        Trader trader;
        cryptocurrencyValuePredictor.startTrading(true, 0, 100.0, true, "unknown");
        trader = cryptocurrencyValuePredictor.getTrader();
        assertEquals(HoldMode.USD, trader.getHoldMode());
        assertEquals(TradeMode.GOFAI, trader.getTradeMode());
        assertEquals(0, trader.getTradeModeIndex());
        assertEquals(100.0, trader.getWallet().getValue(), TestGlobals.DELTA);
        cryptocurrencyValuePredictor.stopTrading();
        assertEquals(HoldMode.USD, trader.getHoldMode());
        assertEquals(TradeMode.MANUAL, trader.getTradeMode());
        assertEquals(0, trader.getTradeModeIndex());
        assertEquals(100.0, trader.getWallet().getValue(), TestGlobals.DELTA);
    }
}
