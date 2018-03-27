/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.Globals;
import com.jkellaway.cryptocurrencyvaluepredictorlibrary.model.Currency;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jkell
 */
public class CurrencyAPIControllerTest {
    private CurrencyAPIController controller;
    
    public CurrencyAPIControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        controller = new CurrencyAPIController();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetCurrencies() {
        Currency[] currencies = controller.getCurrencies(Globals.API_ENDPOINT + Globals.CURRENCY_EXTENSION);
        assertTrue(currencies[0].getID().equals("BCH"));
        assertTrue(currencies[1].getID().equals("BTC"));
        assertTrue(currencies[2].getID().equals("ETH"));
        assertTrue(currencies[3].getID().equals("LTC"));
    }
}
