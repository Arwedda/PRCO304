/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author jkell
 */
public class ISubjectTest extends TestCase {
    
    public ISubjectTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ISubjectTest.class);
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
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
