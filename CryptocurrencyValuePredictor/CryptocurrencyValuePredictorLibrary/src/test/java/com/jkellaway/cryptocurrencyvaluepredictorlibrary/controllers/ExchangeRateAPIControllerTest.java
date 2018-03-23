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
public class ExchangeRateAPIControllerTest extends TestCase {
    
    public ExchangeRateAPIControllerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ExchangeRateAPIControllerTest.class);
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetExchangeRates() {
    }

    public void testPost_String_ExchangeRate() {
    }

    public void testPost_String_ExchangeRateArr() {
    }

    public void testPut_String_ExchangeRate() {
    }

    public void testPut_String_ExchangeRateArr() {
    }
    
}
