/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jkell
 */
public class PriceCollectorTest extends TestCase {
    
    public PriceCollectorTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PriceCollectorTest.class);
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

    public void testGetFirstRelevantRate() {
    }

    public void testBenchmarkComplete() {
    }

    public void testGetLap() {
    }

    public void testGetCurrencies() {
    }

    public void testSetCurrencies() {
    }

    public void testRegisterObserver() {
    }

    public void testRemoveObserver() {
    }

    public void testNotifyObservers() {
    }
    
}
