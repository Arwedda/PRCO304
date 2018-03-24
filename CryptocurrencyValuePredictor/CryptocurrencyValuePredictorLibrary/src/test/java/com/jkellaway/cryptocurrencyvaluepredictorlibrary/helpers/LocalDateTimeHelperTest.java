/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.time.LocalDateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jkell
 */
public class LocalDateTimeHelperTest {
    private LocalDateTime timestamp;
    private String stringTimestamp;
    
    public LocalDateTimeHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        timestamp = LocalDateTime.of(2016, 05, 18, 00, 14, 03, 60168);
        stringTimestamp = "2016-05-18T00:14";
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testStartOfMinute() {
        assertNotEquals(LocalDateTimeHelper.startOfMinute(timestamp), timestamp);
        assertNotEquals(LocalDateTimeHelper.startOfMinute(timestamp), timestamp.minusSeconds(timestamp.getSecond()));
        assertNotEquals(LocalDateTimeHelper.startOfMinute(timestamp), timestamp.minusNanos(timestamp.getNano()));
        assertEquals(LocalDateTimeHelper.startOfMinute(timestamp), timestamp.minusSeconds(timestamp.getSecond()).minusNanos(timestamp.getNano()));
    }
    
    @Test
    public void testLocalDateTimeParser() {
        LocalDateTime parsedTime = LocalDateTimeHelper.localDateTimeParser(stringTimestamp);
        assertNotEquals(parsedTime, timestamp);
        assertNotEquals(parsedTime, timestamp.minusSeconds(timestamp.getSecond()));
        assertNotEquals(parsedTime, timestamp.minusNanos(timestamp.getNano()));
        assertEquals(parsedTime, timestamp.minusSeconds(timestamp.getSecond()).minusNanos(timestamp.getNano()));
    }

    @Test
    public void testToString() {
    }
}
