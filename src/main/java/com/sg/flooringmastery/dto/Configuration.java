/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dto;

import com.sg.flooringmastery.dao.FlooringMasteryPersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Al Rahman
 */
public class Configuration {
    private List<String> configSettings = new ArrayList<>();
    private static final String CONFIG_FILE = "Config.txt";
    private static final String DELIMITER = "::";
    
    public Configuration() {
        try {
            loadSetting();
        } catch (FlooringMasteryPersistenceException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String getSettings() {
        return configSettings.get(0);
    }
    
    public void loadSetting() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(CONFIG_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Could not load configuration file to memory.", e);
        }
        
        String currentLine;
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            configSettings.add(currentLine);
        }
    }
}
