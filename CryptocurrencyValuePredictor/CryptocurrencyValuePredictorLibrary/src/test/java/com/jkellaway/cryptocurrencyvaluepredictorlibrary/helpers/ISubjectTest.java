/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author jkell
 */
public class ISubjectTest {
    
    public ISubjectTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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

    public void testRegisterObserver() {
    }

    public void testRemoveObserver() {
    }

    public void testNotifyObservers() {
    }

    public class ISubjectImpl implements ISubject {

        public Boolean registerObserver(IObserver o) {
            return null;
        }

        public Boolean removeObserver(IObserver o) {
            return null;
        }

        public void notifyObservers() {
        }
    }    
}
