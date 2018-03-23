/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jkell
 */
public class StringHelperTest extends TestCase {
    private String s1, s2;
    
    public StringHelperTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(StringHelperTest.class);
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        s1 = "abcdefghijklmnopqrstuvwxyz1234567890";
        s2 = "abcdefghijklmnopqrstuvwxyz9876543210";
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testStringsMatch() {
        for (int i = 0; i < 27; i++){
            assertTrue(StringHelper.stringsMatch(s1, s2, i));
        }
        for (int i = 27; i < 37; i++){
            assertFalse(StringHelper.stringsMatch(s1, s2, i));
        }
    }

    public void testDoubleToCurrencyString() {
    }
}
