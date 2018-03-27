/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

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
public class StringHelperTest {
    private static String s1, s2;
    private static Double d1, d2, d3;
    
    public StringHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        s1 = "abcdefghijklmnopqrstuvwxyz1234567890";
        s2 = "abcdefghijklmnopqrstuvwxyz9876543210";
        d1 = 07.77;
        d2 = 70.07;
        d3 = 77.70;
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
    public void testStringsMatch() {
        for (int i = 0; i < 27; i++){
            assertTrue(StringHelper.stringsMatch(s1, s2, i));
        }
        for (int i = 27; i < 37; i++){
            assertFalse(StringHelper.stringsMatch(s1, s2, i));
        }
    }

    @Test
    public void testDoubleToCurrencyString() {
        assertEquals("7.77", StringHelper.doubleToCurrencyString(d1));
        assertEquals("70.07", StringHelper.doubleToCurrencyString(d2));
        assertEquals("77.70", StringHelper.doubleToCurrencyString(d3));
    }
}
