/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.LocalDateTimeHelper;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.GDAXTrade;
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
public class GDAXAPIControllerTest {
    private GDAXAPIController controller;
    
    public GDAXAPIControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        controller = new GDAXAPIController();
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testGet() {
        String json = "[{\"time\":\"2016-05-18T00:14:03.60168Z\",\"trade_id\":1,\"price\":\"12.50000000\",\"size\":\"0.39900249\",\"side\":\"sell\"}]";
        assertEquals(json, controller.get("https://api.gdax.com/products/ETH-USD/trades?after=2"));
    }

    @Test
    public void testGetGDAXTrades() {
        GDAXTrade[] trades = controller.getGDAXTrades("https://api.gdax.com/products/ETH-USD/trades?after=2");
        assertTrue(1 == trades.length);
        assertTrue(trades[0].getPrice() == 12.5);
        assertTrue(trades[0].getTime().equals(LocalDateTimeHelper.localDateTimeParser("2016-05-18T00:14")));
        assertTrue(trades[0].getTrade_id() == 1);
    }
}
