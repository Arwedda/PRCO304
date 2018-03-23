/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.time.LocalDateTime;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jkell
 */
public class LocalDateTimeHelperTest extends TestCase {
    private LocalDateTime timestamp;
    private String stringTimestamp;
    
    public LocalDateTimeHelperTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LocalDateTimeHelperTest.class);
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        timestamp = LocalDateTime.of(2016, 05, 18, 00, 14, 03, 60168);
        stringTimestamp = "2016-05-18T00:14";
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testStartOfMinute() {
        assertNotEquals(LocalDateTimeHelper.startOfMinute(timestamp), timestamp);
        assertNotEquals(LocalDateTimeHelper.startOfMinute(timestamp), timestamp.minusSeconds(timestamp.getSecond()));
        assertNotEquals(LocalDateTimeHelper.startOfMinute(timestamp), timestamp.minusNanos(timestamp.getNano()));
        assertEquals(LocalDateTimeHelper.startOfMinute(timestamp), timestamp.minusSeconds(timestamp.getSecond()).minusNanos(timestamp.getNano()));
    }

    public void testLocalDateTimeParser() {
        LocalDateTime parsedTime = LocalDateTimeHelper.localDateTimeParser(stringTimestamp);
        assertNotEquals(parsedTime, timestamp);
        assertNotEquals(parsedTime, timestamp.minusSeconds(timestamp.getSecond()));
        assertNotEquals(parsedTime, timestamp.minusNanos(timestamp.getNano()));
        assertEquals(parsedTime, timestamp.minusSeconds(timestamp.getSecond()).minusNanos(timestamp.getNano()));
    }

    public void testToString() {
    }
    
}
