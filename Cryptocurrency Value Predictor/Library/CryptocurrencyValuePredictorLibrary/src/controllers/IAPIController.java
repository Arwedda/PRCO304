/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author jkell
 */
public interface IAPIController {
    public String get(String url);
    public void post(String url, String json);
    public void put(String url, String json);
    public void delete(String url);
}
