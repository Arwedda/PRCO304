/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 *
 * @author jkell
 */
public class APIController {
    public APIController() {
    }
    
    public String getJSONString(String url){
        String json = "";
        try {
            try (InputStream is = new URL(url).openStream();
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))){
                        json = br.lines().collect(
                            Collectors.joining(System.lineSeparator()));
                }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return json;
    }
    
    public void Post(String url){
        
    }
    
    public void Put(String url){
        
    }
    
    public void Delete(String url){
        
    }
}
