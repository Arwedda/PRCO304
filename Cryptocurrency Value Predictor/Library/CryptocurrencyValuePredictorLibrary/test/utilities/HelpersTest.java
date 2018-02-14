/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
public class HelpersTest {
    private String s1, s2;
    private LocalDateTime timestamp;
    private String stringTimestamp;
    
    public HelpersTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        s1 = "abcdefghijklmnopqrstuvwxyz1234567890";
        s2 = "abcdefghijklmnopqrstuvwxyz9876543210";
        timestamp = LocalDateTime.of(2016, 05, 18, 00, 14, 03, 60168);
        stringTimestamp = "2016-05-18T00:14";
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testStringsMatch() {
        for (int i = 0; i < 27; i++){
            assertTrue(Helpers.stringsMatch(s1, s2, i));
        }
        for (int i = 27; i < 37; i++){
            assertFalse(Helpers.stringsMatch(s1, s2, i));
        }
    }

    @Test
    public void testStartOfMinute() {
        assertNotEquals(Helpers.startOfMinute(timestamp), timestamp);
        assertNotEquals(Helpers.startOfMinute(timestamp), timestamp.minusSeconds(timestamp.getSecond()));
        assertNotEquals(Helpers.startOfMinute(timestamp), timestamp.minusNanos(timestamp.getNano()));
        assertEquals(Helpers.startOfMinute(timestamp), timestamp.minusSeconds(timestamp.getSecond()).minusNanos(timestamp.getNano()));
    }

    @Test
    public void testLocalDateTimeParser() {
        LocalDateTime parsedTime = Helpers.localDateTimeParser(stringTimestamp);
        assertNotEquals(parsedTime, timestamp);
        assertNotEquals(parsedTime, timestamp.minusSeconds(timestamp.getSecond()));
        assertNotEquals(parsedTime, timestamp.minusNanos(timestamp.getNano()));
        assertEquals(parsedTime, timestamp.minusSeconds(timestamp.getSecond()).minusNanos(timestamp.getNano()));
    }
}
