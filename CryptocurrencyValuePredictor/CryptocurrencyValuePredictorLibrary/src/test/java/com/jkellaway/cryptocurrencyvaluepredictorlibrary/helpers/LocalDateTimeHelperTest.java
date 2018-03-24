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
        timestamp = LocalDateTime.of(2000, 1, 1, 0, 0, 7, 77777);
        stringTimestamp = "2000-01-01T00:00";
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
        LocalDateTime singleHours = LocalDateTime.of(2000, 1, 1, 1, 11, 7, 77777);
        LocalDateTime singleMinutes = LocalDateTime.of(2000, 1, 1, 11, 1, 7, 77777);
        assertEquals("1/1/2000 01:11", LocalDateTimeHelper.toString(singleHours));
        assertEquals("1/1/2000 11:01", LocalDateTimeHelper.toString(singleMinutes));
    }
}
