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
public class JsonParser {
    public JsonParser() {
    }
    
    public Object[] fromJSON(String json){
        Object[] array = new Object[0];
        
        return array;
    }
    
    public String toJSON(Object obj){
        String json = "";
        
        json = obj.toString();
        
        return json;
    }
}
