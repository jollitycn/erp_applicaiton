/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.dto.Order;
import java.util.List;

/**
 *
 * @author Al Rahman
 */
public interface FlooringMasteryServiceLayer {
    void addOrder(Order order) throws FlooringMasteryDataValidationException;
    boolean editOrder(Order order, String[] orderInfo) throws FlooringMasteryDataValidationException;
    void removeOrder(Order order);
    void saveCurrentWork() throws FlooringMasteryPersistenceException;
    List<Order> getAllOrders();
    List<Order> getAllOrdersByDate(String date) throws FlooringMasteryDataValidationException;
    List<String> getAllOrderDates();
    List<String> getAllFileNames();
    Order getOrderByDate(String date, int orderNum) throws FlooringMasteryDataValidationException;
    Order getOrder(int orderNumber);
    Order generateOrderNumAndDate();
}
