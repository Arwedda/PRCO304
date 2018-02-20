/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

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
public class APIController implements IAPIController {
    public APIController() {
    }

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
            System.out.println(e.getMessage());
        }
        return json;
    }

    @Override
    public void post(String url, String json) {
        System.out.println("[INFO] Posting resource to: " + url);
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");

            OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
            out.write(json);
            out.close();

            System.out.println(http.getResponseCode());
            
            http.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void put(String url, String json) {
        System.out.println("[INFO] Putting resource to: " + url);
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setRequestMethod("PUT");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            
            try (OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream())) {
                out.write(json);
            }
            
            System.out.println(http.getResponseCode());
            http.disconnect();
        } catch (Exception e) {
            System.out.println("[ERR] " + e.getMessage());
        }
    }

    @Override
    public void delete(String url) {
        System.out.println("[INFO] Deleting resource at: " + url);
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setRequestMethod("DELETE");
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            
            http.connect();
            
            System.out.println(http.getResponseCode());
            http.disconnect();
        } catch (Exception e) {
            System.out.println("[ERR] " + e.getMessage());
        }

    }
}
