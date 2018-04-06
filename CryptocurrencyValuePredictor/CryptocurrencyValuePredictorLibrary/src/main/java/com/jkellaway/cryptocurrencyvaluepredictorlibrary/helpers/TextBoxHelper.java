/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.helpers;

/**
 *
 * @author jkell
 */
public class TextBoxHelper {
    
    /**
     * Prevents default action caused by any non-numeric keys (with the exception
     * of . to act as a decimal point).
     * @param evt Keyboard input.
     */
    public static void forceNumeric(java.awt.event.KeyEvent evt) {
        switch (evt.getKeyChar()) {
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '0':
            case '.':
                break;
            default:
                evt.consume();
                break;
        }
    }
}
