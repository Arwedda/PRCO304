/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

import java.util.Arrays;

/**
 *
 * @author jkell
 */
public class ArrayHelper {
    
    /**
     * Merges two arrays into a single, larger array.
     * @param a The first array.
     * @param b The second array.
     * @return The merged array.
     */
    public static <T> T[] merge(T[] a, T[] b) {
      T[] result = Arrays.copyOf(a, a.length + b.length);
      System.arraycopy(b, 0, result, a.length, b.length);
      return result;
    }
}
