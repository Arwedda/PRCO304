/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.utilities;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.GDAXTrade;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.testglobals.TestGlobals;
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
public class JSONParserTest {
    private static JSONParser parser;
    
    public JSONParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        parser = new JSONParser();
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
    public void testGDAXTradeFromJSON() {
        String json = "[{\"time\":\"2016-05-18T00:14:03.60168Z\",\"trade_id\":1,\"price\":\"12.50000000\",\"size\":\"0.39900249\",\"side\":\"sell\"}]";
        GDAXTrade[] trades = parser.GDAXTradeFromJSON(json);
        assertEquals(1, trades.length);
        assertEquals("2016-05-18T00:14", trades[0].getTime().toString());
        assertEquals(12.5, trades[0].getPrice(), TestGlobals.DELTA);
        assertEquals((long) 1, (long) trades[0].getTrade_id());
    }
}
