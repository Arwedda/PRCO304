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
public interface ISubject {
    public Boolean registerObserver(IObserver o);
    public Boolean removeObserver(IObserver o);
    public void notifyObservers();
}
