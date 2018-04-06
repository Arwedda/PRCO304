/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jkellaway.cryptocurrencyvaluepredictorlibrary.controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 *
 * @author jkell
 */
public abstract class APIController implements IAPIController {
    final Gson gson;
    
    /**
     * APIController constructor. Initialises Gson to be used to parse JSON by
     * implemented APIControllers.
     */
    public APIController() {
        gson = new Gson();
    }

    /**
     * Retrieves data from the target URL.
     * @param url String form of the URL to collect data from.
     * @return JSON String from target URL.
     */
    @Override
    public String get(String url) {
        String json = "";
        try {
            try (InputStream is = new URL(url).openStream();
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))){
                        json = br.lines().collect(
                            Collectors.joining(System.lineSeparator()));
                }
        } catch (Exception e) {
            System.out.println(e);
        }
        return json;
    }

    /**
     * Posts data to the target URL.
     * @param endpoint String form of the URL endpoint to post data to.
     * @param json JSON string to post to the URL.
     */
    @Override
    public void post(String endpoint, String json) {
        //System.out.println("[INFO] Posting to: " + endpoint);
        try {        
            HttpURLConnection http = (HttpURLConnection) new URL(endpoint).openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            
            try (OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream())) {
                out.write(json);
            }

            if (http.getResponseCode() != 201){
                System.out.println(http.getResponseCode());
            }
            http.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Puts data to the target URL. Used to update/overwrite data that exists
     * there.
     * @param url String form of the URL endpoint to put data to.
     * @param json JSON string to put to the URL.
     */
    @Override
    public void put(String url, String json) {
        //System.out.println("[INFO] Putting resource to: " + url);
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setRequestMethod("PUT");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            
            try (OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream())) {
                out.write(json);
            }
            
            int response = http.getResponseCode();
            if (response != 200 && response != 204){
                System.out.println(http.getResponseCode());
            }
            http.disconnect();
        } catch (Exception e) {
            System.out.println("[ERR] " + e);
        }
    }

    /**
     * Deletes data from the target URL.
     * @param url String form of the URL endpoint to delete data from.
     */
    @Override
    public void delete(String url) {
        //System.out.println("[INFO] Deleting resource at: " + url);
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setRequestMethod("DELETE");
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            
            http.connect();
            
            System.out.println(http.getResponseCode());
            http.disconnect();
        } catch (Exception e) {
            System.out.println("[ERR] " + e);
        }
    }
}