/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import controllers.APIController;
import java.time.LocalDateTime;
import model.GDAXTrade;
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
public class JSONParserTest {
    private JSONParser parser;
    private String json;
    static final double DELTA = 1e-15;
    
    public JSONParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        APIController apiController = new APIController();
        parser = new JSONParser();
        json = apiController.getJSONString("https://api.gdax.com/products/ETH-USD/trades?after=2");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testFromJSON() {
        LocalDateTime expectedTime = Helpers.localDateTimeParser("2016-05-18T00:14");
        Object[] objArray = parser.fromJSON(json);
        assertEquals(1, objArray.length);
        assertEquals("1", ((GDAXTrade)objArray[0]).getID());
        assertEquals(12.5, ((GDAXTrade)objArray[0]).getPrice(), DELTA);
        assertEquals(expectedTime, ((GDAXTrade)objArray[0]).getTime());
    }

    @Test
    public void testToJSON() {
        //Not yet implemented
    }
}
