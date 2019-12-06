/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.service.FlooringMasteryDataValidationException;

/**
 *
 * @author Al Rahman
 */
public interface UserIO {
    void print(String message);

    String readString(String prompt);

    double readDouble(String prompt) throws FlooringMasteryDataValidationException;

    double readDouble(String prompt, double min, double max) throws FlooringMasteryDataValidationException;

    float readFloat(String prompt) throws FlooringMasteryDataValidationException;

    float readFloat(String prompt, float min, float max) throws FlooringMasteryDataValidationException;

    int readInt(String prompt) throws FlooringMasteryDataValidationException;

    int readInt(String prompt, int min, int max) throws FlooringMasteryDataValidationException;

    long readLong(String prompt) throws FlooringMasteryDataValidationException;

    long readLong(String prompt, long min, long max) throws FlooringMasteryDataValidationException;
}
