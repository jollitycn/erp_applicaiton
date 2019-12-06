/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dto;

/**
 *
 * @author Al Rahman
 */
public class Tax {
    private String stateAbbr;
    private String stateFullName;
    private double taxRate;

    public String getStateAbbr() {
        return stateAbbr;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public String getStateFullName() {
        return stateFullName;
    }

    public void setStateFullName(String stateFullName) {
        this.stateFullName = stateFullName;
    }
    

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }
}
