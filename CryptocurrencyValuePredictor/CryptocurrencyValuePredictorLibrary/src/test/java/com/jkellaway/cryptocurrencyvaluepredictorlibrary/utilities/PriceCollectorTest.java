/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.IObserver;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
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
public class PriceCollectorTest implements IObserver {
    private static PriceCollector pc;
    private static boolean observerWorks;
    private static LocalDateTime expectedTime;
    
    public PriceCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        pc = new PriceCollector();
        pc.initialise(TestGlobals.REQUIREDRATES);
        observerWorks = false;
        expectedTime = LocalDateTimeHelper.startOfMinute(LocalDateTime.now(Clock.systemUTC()).minusMinutes(TestGlobals.REQUIREDRATES));
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        if (pc.getLap() != 1) {
            pc = new PriceCollector();
            pc.initialise(TestGlobals.REQUIREDRATES);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetFirstRelevantRate() {
        assertEquals(expectedTime, pc.getFirstRelevantRate());
    }

    @Test
    public void testBenchmarkComplete() {
        pc.benchmarkComplete(pc.getCurrencies());
        assertEquals(-1, pc.getLap());
        pc.initialise(TestGlobals.REQUIREDRATES);
    }

    @Test
    public void testGetLap() {
        assertEquals(1, pc.getLap());
    }

    @Test
    public void testGetCurrencies() {
        Currency[] currencies = pc.getCurrencies();
        assertEquals(4, currencies.length);
        assertEquals(TestGlobals.IDBCH, currencies[0].getID());
        assertEquals(TestGlobals.IDBTC, currencies[1].getID());
        assertEquals(TestGlobals.IDETH, currencies[2].getID());
        assertEquals(TestGlobals.IDLTC, currencies[3].getID());
    }

    @Test
    public void testSetCurrencies() {
        Currency[] currencies = pc.getCurrencies();
        pc.setCurrencies(null);
        assertNull(pc.getCurrencies());
        pc.setCurrencies(currencies);
        assertArrayEquals(currencies, pc.getCurrencies());
    }

    @Test
    public void testObservers() {
        pc.registerObserver(this);
        pc.notifyObservers();
        assertTrue(observerWorks);
        pc.removeObserver(this);
        pc.notifyObservers();
        assertTrue(observerWorks);
    }
    
    public void update() {
        observerWorks = !observerWorks;
    }
}
