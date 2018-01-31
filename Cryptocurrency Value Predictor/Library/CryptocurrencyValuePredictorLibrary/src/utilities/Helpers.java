/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author jkell
 */
public class Helpers {

    public Helpers() {
    }
    
    public boolean stringsMatch(String string1, String string2, int length){
        string1 = string1.substring(0, Math.min(string1.length(), length));
        string2 = string2.substring(0, Math.min(string2.length(), length));
        return string1.equals(string2);
    }
}
