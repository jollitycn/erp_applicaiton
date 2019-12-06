/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author Al Rahman
 */
public class FlooringMasteryProdDaoFileImpl implements FlooringMasteryDao {

    protected final Map<Integer, Order> orders = new LinkedHashMap<>();
    protected final Map<String, Product> products = new HashMap<>();
    protected final Map<String, Tax> taxes = new HashMap<>();
    protected final List<String> fileNamesList = new ArrayList<>();

    protected static final String PRODUCT_FILE = "products.txt";
    protected static final String TAXES_FILE = "taxes.txt";
    protected static final String ORDER_FILES = "order_file_names.txt";
    public static final String DELIMITER = "::";

    public FlooringMasteryProdDaoFileImpl() throws FlooringMasteryPersistenceException {
            loadOrders();
            loadProducts();
            loadTaxes();
    }

    @Override
    public void addOrder(Order order) {
        orders.put(order.getOrderNumber(), order);
    }

    @Override
    public void removeOrder(Order order) {
        orders.remove(order.getOrderNumber());
    }

    @Override
    public Order getOrder(int orderNumber) {
        return orders.get(orderNumber);
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
//    @Override
//    public List<Integer> getAllOrderNums() {
//        return new ArrayList<>(orders.keySet());
//    }

    @Override
    public List<Order> getAllOrdersByDate(String date) {
        return new ArrayList<>(orders.values()
                .stream()
                .filter(order -> order.getOrderDate().equalsIgnoreCase(date))
                .collect(Collectors.toList()));
    }

    @Override
    public Map<String, List<Order>> getAllOrderDates() {
        return new HashMap<>(orders.values()
                .stream()
                .collect(Collectors.groupingBy(Order::getOrderDate)));
    }

    @Override
    public List<String> getAllFileNames() {
        return new ArrayList<>(fileNamesList);
    }

    @Override
    public Product getProduct(String productType) {
        return products.get(productType.toLowerCase());
    }

    @Override
    public Tax getTax(String state) {
        return taxes.get(state.toUpperCase());
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    @Override
    public List<Tax> getAllTaxes() {
        return new ArrayList<>(taxes.values());
    }

    private void createOrderFileNames() {
        Map<String, List<Order>> orderDates = this.getAllOrderDates();
        List<String> listOrderDates = new ArrayList<>(orderDates.keySet());

        for (String currentDate : listOrderDates) {
            currentDate = currentDate.replaceAll("[\\s\\-]", "");
            currentDate = "ORDERS_" + currentDate + ".txt";
            if (!fileNamesList.contains(currentDate)) {
                fileNamesList.add(currentDate);
            }
        }
    }

    @Override
    public void saveCurrentWork() throws FlooringMasteryPersistenceException {
        this.writeOrders();
    }

    private void writeOrders() throws FlooringMasteryPersistenceException {
        this.createOrderFileNames();

        PrintWriter writeOrders, writeFileNames;

        try {
            writeFileNames = new PrintWriter(new FileWriter(ORDER_FILES));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not save order file names.", e);
        }

        for (String currentFile : fileNamesList) {

            try {
                writeOrders = new PrintWriter(new FileWriter(currentFile));
            } catch (IOException e) {
                throw new FlooringMasteryPersistenceException("Could not save order data.", e);
            }

            writeFileNames.println(currentFile);
            writeFileNames.flush();

            String currentDate = currentFile.substring(7, 15);
            currentDate = currentDate.substring(0, 2) + "-"
                    + currentDate.substring(2, 4) + "-"
                    + currentDate.substring(4);

            List<Order> orderList = getAllOrdersByDate(currentDate);
            for (Order currentOrder : orderList) {
                writeOrders.println(currentOrder.getOrderDate() + DELIMITER
                        + currentOrder.getOrderNumber() + DELIMITER
                        + currentOrder.getCustomerName() + DELIMITER
                        + currentOrder.getTaxData().getStateAbbr() + DELIMITER
                        + currentOrder.getTaxData().getStateFullName() + DELIMITER
                        + currentOrder.getTaxData().getTaxRate() + DELIMITER
                        + currentOrder.getProductData().getProductType() + DELIMITER
                        + currentOrder.getArea() + DELIMITER
                        + currentOrder.getProductData().getCostPerSquareFoot() + DELIMITER
                        + currentOrder.getProductData().getLaborCostPerSquareFoot() + DELIMITER
                        + currentOrder.getMaterialCost() + DELIMITER
                        + currentOrder.getLaborCost() + DELIMITER
                        + currentOrder.getTotalTax() + DELIMITER
                        + currentOrder.getTotalAmount());
                writeOrders.flush();
            }
            writeOrders.close();
        }
        writeFileNames.close();
    }

    private void loadOrders() throws FlooringMasteryPersistenceException {
        boolean fileExists;
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FILES)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Could not load order file names into memory.", e);
        }
        String currentFileNameLine;
        while (scanner.hasNextLine()) {
            currentFileNameLine = scanner.nextLine();
            if (!currentFileNameLine.equals("")) {
                fileNamesList.add(currentFileNameLine);
            }
        }
        scanner.close();

        for (String currentFile : fileNamesList) {
            fileExists = new File(currentFile).exists();
            if (fileExists) {
                try {
                    scanner = new Scanner(new BufferedReader(new FileReader(currentFile)));
                } catch (FileNotFoundException e) {
                    throw new FlooringMasteryPersistenceException("Could not load order data into memory.", e);
                }

                String currentOrderLine;
                String[] currentOrderToken;
                while (scanner.hasNextLine()) {
                    currentOrderLine = scanner.nextLine();
                    currentOrderToken = currentOrderLine.split(DELIMITER);
                    Order currentOrder = new Order(currentOrderToken[0], Integer.parseInt(currentOrderToken[1]));
                    currentOrder.setCustomerName(currentOrderToken[2]);
                    currentOrder.getTaxData().setStateAbbr(currentOrderToken[3]);
                    currentOrder.getTaxData().setStateFullName(currentOrderToken[4]);
                    currentOrder.getTaxData().setTaxRate(Double.parseDouble(currentOrderToken[5]));
                    currentOrder.getProductData().setProductType(currentOrderToken[6]);
                    currentOrder.setArea(Double.parseDouble(currentOrderToken[7]));
                    currentOrder.getProductData().setCostPerSquareFoot(new BigDecimal(currentOrderToken[8]));
                    currentOrder.getProductData().setLaborCostPerSquareFoot(new BigDecimal(currentOrderToken[9]));
                    currentOrder.setMaterialCost(new BigDecimal(currentOrderToken[10]));
                    currentOrder.setLaborCost(new BigDecimal(currentOrderToken[11]));
                    currentOrder.setTotalTax(new BigDecimal(currentOrderToken[12]));
                    currentOrder.setTotalAmount(new BigDecimal(currentOrderToken[13]));
                    orders.put(currentOrder.getOrderNumber(), currentOrder);
                }
                scanner.close();
            }
        }
    }

    private void loadProducts() throws FlooringMasteryPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Could not load product data into memory.", e);
        }

        String currentLine;
        String[] currentTokens;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            Product currentProduct = new Product();
            currentProduct.setProductType(currentTokens[0]);
            currentProduct.setCostPerSquareFoot(new BigDecimal(currentTokens[1]));
            currentProduct.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[2]));
            products.put(currentTokens[0].toLowerCase(), currentProduct);
        }
    }

    private void loadTaxes() throws FlooringMasteryPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException("Could not load tax data into memory.", e);
        }

        String currentLine;
        String[] currentTokens;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            Tax currentTax = new Tax();
            currentTax.setStateAbbr(currentTokens[0]);
            currentTax.setStateFullName(currentTokens[1]);
            currentTax.setTaxRate(Double.parseDouble(currentTokens[2]));
            taxes.put(currentTokens[0].toUpperCase(), currentTax);
        }
    }
}
