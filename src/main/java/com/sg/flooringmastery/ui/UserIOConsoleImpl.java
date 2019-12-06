/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.service.FlooringMasteryDataValidationException;
import java.util.Scanner;

/**
 *
 * @author Al Rahman
 */
public class UserIOConsoleImpl implements UserIO {
    private final Scanner myScanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        print(prompt);
        String input = myScanner.nextLine();
        return input.trim();
    }

    @Override
    public double readDouble(String prompt) throws FlooringMasteryDataValidationException {
        String input = readString(prompt);
        double result;
        try {
            result = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new FlooringMasteryDataValidationException("<Error> Incorrect input: "
                    + "Number not detected. Please enter a number within the range specified.", e);
        }
        return result;
    }

    @Override
    public double readDouble(String prompt, double min, double max) throws FlooringMasteryDataValidationException {
        double result = readDouble(prompt);
        while (result < min || result > max) {
            print("<Error> Out of Range. Please enter a number between " + min + " and " + max + ".");
            result = readDouble(prompt);
        }
        return result;
    }

    @Override
    public float readFloat(String prompt) throws FlooringMasteryDataValidationException {
        String input = readString(prompt);
        float result;
        try {
            result = Float.parseFloat(input);
        } catch (NumberFormatException e) {
            throw new FlooringMasteryDataValidationException("<Error> Incorrect input: "
                    + "Number not detected. Please enter a number within the range specified.", e);
        }
        return result;
    }

    @Override
    public float readFloat(String prompt, float min, float max) throws FlooringMasteryDataValidationException {
        float result = readFloat(prompt);
        while (result < min || result > max) {
            print("<Error> Out of Range. Please enter a number between " + min + " and " + max + ".");
            result = readFloat(prompt);
        }
        return result;
    }

    @Override
    public int readInt(String prompt) throws FlooringMasteryDataValidationException {
        String input = readString(prompt);
        int result = 0;
        try {
            result = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new FlooringMasteryDataValidationException("<Error> Incorrect input: "
                    + "Number not detected. Please enter a number within the range specified.", e);
        }
        return result;
    }

    @Override
    public int readInt(String prompt, int min, int max) throws FlooringMasteryDataValidationException {
        int result = readInt(prompt);
        while (result < min || result > max) {
            print("<Error> Out of Range. Please enter a number between " + min + " and " + max + ".");
            result = readInt(prompt);
        }
        return result;
    }

    @Override
    public long readLong(String prompt) throws FlooringMasteryDataValidationException {
        String input = readString(prompt);
        long result;
        try {
            result = Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new FlooringMasteryDataValidationException("<Error> Incorrect input: "
                    + "Number not detected. Please enter a number within the range specified.", e);
        }
        return result;
    }

    @Override
    public long readLong(String prompt, long min, long max) throws FlooringMasteryDataValidationException {
        long result = readLong(prompt);
        while (result < min || result > max) {
            print("<Error> Out of Range. Please enter a number between " + min + " and " + max + ".");
            result = readLong(prompt);
        }
        return result;
    }
}
