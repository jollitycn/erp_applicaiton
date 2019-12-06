/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

/**
 *
 * @author Al Rahman
 */
public class FlooringMasteryTrainingDaoFileImpl extends FlooringMasteryProdDaoFileImpl {

    public FlooringMasteryTrainingDaoFileImpl() throws FlooringMasteryPersistenceException {
    }

    @Override
    public void saveCurrentWork() throws FlooringMasteryPersistenceException {
        throw new FlooringMasteryPersistenceException("You are in training mode. You are not allowed to save data in file.");
    }
}
