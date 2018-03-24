/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;


import static com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers.ArrayHelper.merge;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author jkell
 */
public class ArrayHelperTest {
    private Integer[] a;
    private Integer[] b;
    private Integer[] c;
    private String[] d;
    private String[] e;
    private String[] f;
    
    public ArrayHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        a = new Integer[] {1, 2};
        b = new Integer[] {3, 4};
        c = new Integer[] {1, 2, 3, 4};
        d = new String[] {"One", "Two"};
        e = new String[] {"Three", "Four"};
        f = new String[] {"One", "Two", "Three", "Four"};
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMerge() {
        Integer[] ab = merge(a, b);
        String[] de = merge (d, e);
        
        Assert.assertArrayEquals(ab, c);
        Assert.assertArrayEquals(de, f);
    }
}
