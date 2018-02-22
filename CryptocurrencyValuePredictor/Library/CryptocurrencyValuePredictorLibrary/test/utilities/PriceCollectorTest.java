/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import model.Currency;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jkell
 */
public class PriceCollectorTest {
    PriceCollector pc;
    Currency firstTwoLitecoinTrades;
    
    public PriceCollectorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        pc = new PriceCollector();
        firstTwoLitecoinTrades = new Currency("LTC", "Litecoin", "https://api.gdax.com/products/LTC-USD/trades?after=3");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetCurrentPrices() {
        
    }

    @Test
    public void testGetTrades() {
        
    }

    @Test
    public void testCalculateAveragePrice() {
        
    }

    @Test
    public void testCalculateHistoricAverages() {
        
    }
    
}
