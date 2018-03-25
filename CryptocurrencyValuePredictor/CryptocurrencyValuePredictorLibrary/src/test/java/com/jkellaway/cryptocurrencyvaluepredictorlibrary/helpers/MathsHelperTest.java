/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
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
public class MathsHelperTest {
    private Double[] doubles;
    
    public MathsHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        doubles = new Double[] {9.9, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 1.1};
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMean() {
        assertEquals(5.5, MathsHelper.mean(doubles), TestGlobals.DELTA);
    }

    @Test
    public void testDeltas() {
        Double[] manualDeltas = new Double[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            manualDeltas[i] = doubles[i] - TestGlobals.ONE;
        }
        Assert.assertArrayEquals(manualDeltas, MathsHelper.deltas(TestGlobals.ONE, doubles));
    }

    @Test
    public void testMax() {
        assertEquals(9.9, MathsHelper.max(doubles), TestGlobals.DELTA);
    }

    @Test
    public void testMin() {
        assertEquals(1.1, MathsHelper.min(doubles), TestGlobals.DELTA);
    }
}
