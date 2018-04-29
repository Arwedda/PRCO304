/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.ExchangeRate;
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
public class ExchangeRateAPIControllerTest {
    private static ExchangeRateAPIController controller;
    private ExchangeRate[] rates;
    
    public ExchangeRateAPIControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        controller = new ExchangeRateAPIController();
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
    public void testGetExchangeRates() {
        assertNull(rates);
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION);
        assertNotSame(null, rates);
    }

    @Test
    public void testPost_String_ExchangeRate() {
        ExchangeRate rate = new ExchangeRate(TestGlobals.IDBCH, LocalDateTime.of(2000, 01, 01, 0, 0, 0), 1.0, null, null, null, TestGlobals.LASTTRADE);
        controller.post(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, rate);
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id());
        List<ExchangeRate> temp = new ArrayList<>();
        for (ExchangeRate er : rates) {
            if (er.getTimestamp().equals("2000-01-01T00:00:00")) {
                temp.add(er);
                er.setTimestamp(er.getTimestamp());
            }
        }
        rates = temp.toArray(new ExchangeRate[temp.size()]);
        assertEquals(1, rates.length);
        assertEquals(rate.getCurrency_id(), rates[0].getCurrency_id());
        assertEquals(rate.getGrowth(), rates[0].getGrowth());
        assertEquals(rate.getLDTTimestamp(), rates[0].getLDTTimestamp());
        assertEquals(rate.getLastTrade(), rates[0].getLastTrade());
        assertEquals(rate.getTimestamp(), rates[0].getTimestamp());
        assertEquals(rate.getValue(), rates[0].getValue());
        
        //Also testing delete
        controller.delete(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id() + "/" + rate.getLDTTimestamp());
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id());
        temp = new ArrayList<>();
        for (ExchangeRate er : rates) {
            if (er.getTimestamp().equals("2000-01-01T00:00:00")) {
                temp.add(er);
                er.setTimestamp(er.getTimestamp());
            }
        }
        rates = temp.toArray(new ExchangeRate[temp.size()]);
        assertEquals(0, rates.length);
    }

    @Test
    public void testPost_String_ExchangeRateArr() {
        ExchangeRate rate = new ExchangeRate(TestGlobals.IDBCH, LocalDateTime.of(2000, 01, 01, 0, 1, 0), 1.0, null, null, null, TestGlobals.LASTTRADE);
        ExchangeRate rate2 = new ExchangeRate(TestGlobals.IDBCH, LocalDateTime.of(2000, 01, 01, 0, 2, 0), 100.0, null, null, null, TestGlobals.LASTTRADE);
        rates = new ExchangeRate[2];
        rates[0] = rate;
        rates[1] = rate2;
        controller.post(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, rates);
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id());
        List<ExchangeRate> temp = new ArrayList<>();
        for (ExchangeRate er : rates) {
            if (er.getTimestamp().equals("2000-01-01T00:01:00") || er.getTimestamp().equals("2000-01-01T00:02:00")) {
                temp.add(er);
                er.setTimestamp(er.getTimestamp());
            }
        }
        rates = temp.toArray(new ExchangeRate[temp.size()]);
        assertEquals(2, rates.length);
        
        //Also testing delete
        controller.delete(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id() + "/" + rate.getLDTTimestamp());
        controller.delete(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate2.getCurrency_id() + "/" + rate2.getLDTTimestamp());;
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id());
        temp = new ArrayList<>();
        for (ExchangeRate er : rates) {
            if (er.getTimestamp().equals("2000-01-01T00:01:00") || er.getTimestamp().equals("2000-01-01T00:02:00")) {
                temp.add(er);
                er.setTimestamp(er.getTimestamp());
            }
        }
        rates = temp.toArray(new ExchangeRate[temp.size()]);
        assertEquals(0, rates.length);
    }

    @Test
    public void testPut_String_ExchangeRate() {
        ExchangeRate rate = new ExchangeRate(TestGlobals.IDBCH, LocalDateTime.of(2000, 01, 01, 0, 3, 0), 1.0, null, null, null, TestGlobals.LASTTRADE);
        controller.post(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, rate);
        rate.setValue(125.0);
        controller.put(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, rate);
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id());
        List<ExchangeRate> temp = new ArrayList<>();
        for (ExchangeRate er : rates) {
            if (er.getTimestamp().equals("2000-01-01T00:03:00")) {
                temp.add(er);
                er.setTimestamp(er.getTimestamp());
            }
        }
        rates = temp.toArray(new ExchangeRate[temp.size()]);
        assertEquals(1, rates.length);
        assertEquals(rate.getCurrency_id(), rates[0].getCurrency_id());
        assertEquals(rate.getGrowth(), rates[0].getGrowth());
        assertEquals(rate.getLDTTimestamp(), rates[0].getLDTTimestamp());
        assertEquals(rate.getLastTrade(), rates[0].getLastTrade());
        assertEquals(rate.getTimestamp(), rates[0].getTimestamp());
        assertEquals(rate.getValue(), rates[0].getValue());
        
        //Also testing delete
        controller.delete(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id() + "/" + rate.getLDTTimestamp());
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id());
        temp = new ArrayList<>();
        for (ExchangeRate er : rates) {
            if (er.getTimestamp().equals("2000-01-01T00:03:00")) {
                temp.add(er);
                er.setTimestamp(er.getTimestamp());
            }
        }
        rates = temp.toArray(new ExchangeRate[temp.size()]);
        assertEquals(0, rates.length);
    }
    
    @Test
    public void testPut_String_ExchangeRateArr() {
        ExchangeRate rate = new ExchangeRate(TestGlobals.IDBCH, LocalDateTime.of(2000, 01, 01, 0, 4, 0), 1.0, null, null, null, TestGlobals.LASTTRADE);
        ExchangeRate rate2 = new ExchangeRate(TestGlobals.IDBCH, LocalDateTime.of(2000, 01, 01, 0, 5, 0), 100.0, null, null, null, TestGlobals.LASTTRADE);
        rates = new ExchangeRate[2];
        rates[0] = rate;
        rates[1] = rate2;
        controller.post(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, rates);
        rates = new ExchangeRate[2];
        rate.setValue(1.0);
        rate2.setValue(125.0);
        rates[0] = rate;
        rates[1] = rate2;
        controller.put(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION, rates);
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id());
        List<ExchangeRate> temp = new ArrayList<>();
        for (ExchangeRate er : rates) {
            if (er.getTimestamp().equals("2000-01-01T00:04:00") || er.getTimestamp().equals("2000-01-01T00:05:00")) {
                temp.add(er);
                er.setTimestamp(er.getTimestamp());
            }
        }
        rates = temp.toArray(new ExchangeRate[temp.size()]);
        assertEquals(2, rates.length);
        
        //Also testing delete
        controller.delete(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id() + "/" + rate.getLDTTimestamp());
        controller.delete(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate2.getCurrency_id() + "/" + rate2.getLDTTimestamp());;
        rates = controller.getExchangeRates(Globals.API_ENDPOINT + Globals.EXCHANGERATE_EXTENSION + "/" + rate.getCurrency_id());
        temp = new ArrayList<>();
        for (ExchangeRate er : rates) {
            if (er.getTimestamp().equals("2000-01-01T00:04:00") || er.getTimestamp().equals("2000-01-01T00:05:00")) {
                temp.add(er);
                er.setTimestamp(er.getTimestamp());
            }
        }
        rates = temp.toArray(new ExchangeRate[temp.size()]);
        assertEquals(0, rates.length);
    }
}
