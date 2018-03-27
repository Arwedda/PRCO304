/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.model;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
import java.time.Clock;
import java.time.LocalDateTime;
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
public class GapTest {
    private Gap blank;
    private Gap real;
    private static LocalDateTime timeStamp;
    
    public GapTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        timeStamp = LocalDateTime.now(Clock.systemUTC());
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        blank = new Gap();
        real = new Gap(TestGlobals.LASTTRADE, timeStamp, 1);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetPaginationStart() {
        assertNull(blank.getPaginationStart());
        assertEquals(TestGlobals.LASTTRADE, real.getPaginationStart());
        assertNotEquals((long)1, (long)real.getPaginationStart());
    }

    @Test
    public void testSetPaginationStart() {
        blank.setPaginationStart(TestGlobals.LASTTRADE);
        assertEquals(TestGlobals.LASTTRADE, blank.getPaginationStart());
        blank.setPaginationStart(null);
    }
    
    @Test
    public void testGetStartTime() {
        assertNull(blank.getStartTime());
        assertEquals(timeStamp, real.getStartTime());
    }

    @Test
    public void testSetStartTime() {
        LocalDateTime newTimeStamp = LocalDateTime.now(Clock.systemUTC()).minusDays(1);
        assertNotEquals(newTimeStamp, real.getStartTime());
        real.setStartTime(newTimeStamp);
        assertEquals(newTimeStamp, real.getStartTime());
    }

    @Test
    public void testGetRatesRequired() {
        assertNull(blank.getRatesRequired());
        assertEquals((long) 1, (long)real.getRatesRequired());
    }

    @Test
    public void testSetRatesRequired() {
        blank.setRatesRequired(7);
        assertEquals((long) 7, (long) blank.getRatesRequired());
        blank.setRatesRequired(null);
    }
}
