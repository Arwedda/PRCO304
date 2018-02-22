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
public class GDAXAPIControllerTest {
    GDAXAPIController controller;

    
    public GDAXAPIControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        controller = new GDAXAPIController();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGet() {
        String json = "[{\"time\":\"2016-05-18T00:14:03.60168Z\",\"trade_id\":1,\"price\":\"12.50000000\",\"size\":\"0.39900249\",\"side\":\"sell\"}]";
        assertEquals(json, controller.get("https://api.gdax.com/products/ETH-USD/trades?after=2"));
    }

    @Test
    public void testPost() {
    }

    @Test
    public void testPut() {
    }

    @Test
    public void testDelete() {
    }
}
