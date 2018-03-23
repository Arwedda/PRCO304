/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jkell
 */
public class GDAXAPIControllerTest extends TestCase {
    GDAXAPIController controller;
    
    public GDAXAPIControllerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GDAXAPIControllerTest.class);
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new GDAXAPIController();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetGDAXTrades() {
        String json = "[{\"time\":\"2016-05-18T00:14:03.60168Z\",\"trade_id\":1,\"price\":\"12.50000000\",\"size\":\"0.39900249\",\"side\":\"sell\"}]";
        assertEquals(json, controller.get("https://api.gdax.com/products/ETH-USD/trades?after=2"));
    }
}
