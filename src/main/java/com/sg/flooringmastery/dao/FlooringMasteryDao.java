/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Al Rahman
 */
public interface FlooringMasteryDao {
    void addOrder(Order order);
    void removeOrder(Order order);
    void saveCurrentWork() throws FlooringMasteryPersistenceException;
    Order getOrder(int orderNumber);
    List<Order> getAllOrders();
//    List<Integer> getAllOrderNums();
    List<Order> getAllOrdersByDate(String date);
    Map<String, List<Order>> getAllOrderDates();
    List<String> getAllFileNames();
    Product getProduct(String productType);
    Tax getTax(String state);
    List<Product> getAllProducts();
    List<Tax> getAllTaxes();
}
