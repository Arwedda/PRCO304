/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

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
public class APIControllerTest {
    APIController apiController;
    
    public APIControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        apiController = new APIController();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetGDAX_ENDPOINT() {
        assertEquals("https://api.gdax.com", apiController.getGDAX_ENDPOINT());
    }

    @Test
    public void testGetBCH_TRADES() {
        assertEquals("https://api.gdax.com/products/BCH-USD/trades", apiController.getBCH_TRADES());
    }

    @Test
    public void testGetBTC_TRADES() {
        assertEquals("https://api.gdax.com/products/BTC-USD/trades", apiController.getBTC_TRADES());
    }

    @Test
    public void testGetETH_TRADES() {
        assertEquals("https://api.gdax.com/products/ETH-USD/trades", apiController.getETH_TRADES());
    }

    @Test
    public void testGetLTC_TRADES() {
        assertEquals("https://api.gdax.com/products/LTC-USD/trades", apiController.getLTC_TRADES());
    }

    @Test
    public void testGetJSONString() {
        String json = "[{\"time\":\"2016-05-18T00:14:03.60168Z\",\"trade_id\":1,\"price\":\"12.50000000\",\"size\":\"0.39900249\",\"side\":\"sell\"}]";
        assertEquals(json, apiController.getJSONString("https://api.gdax.com/products/ETH-USD/trades?after=2"));
    }

    @Test
    public void testPost() {
        //Not yet implemented
    }

    @Test
    public void testPut() {
        //Not yet implemented
    }

    @Test
    public void testDelete() {
        //Not yet implemented
    }
}
