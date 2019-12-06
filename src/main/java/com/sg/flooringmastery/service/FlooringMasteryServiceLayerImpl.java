/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.dto.Order;
import java.util.List;
import com.sg.flooringmastery.dao.FlooringMasteryAuditDao;
import com.sg.flooringmastery.dao.FlooringMasteryDao;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Al Rahman
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {

    FlooringMasteryDao dao;
    FlooringMasteryAuditDao auditDao;

    public FlooringMasteryServiceLayerImpl(FlooringMasteryDao dao, FlooringMasteryAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }

    @Override
    public void addOrder(Order order) throws FlooringMasteryDataValidationException {
        this.validateData(order);
        this.calculateCosts(order);
        if (order != null) {
            dao.addOrder(order);
        }
    }

    @Override
    public boolean editOrder(Order order, String[] orderInfo) throws FlooringMasteryDataValidationException {
        boolean orderInfoEmpty = this.validateData(order, orderInfo);
        if (order != null && !orderInfoEmpty) {
            this.calculateCosts(order);
            dao.addOrder(order);
        }
        return orderInfoEmpty;
    }

    @Override
    public void removeOrder(Order order) {
        dao.removeOrder(order);
    }

    @Override
    public Order getOrder(int orderNumber) {
        return dao.getOrder(orderNumber);
    }

    @Override
    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }

    @Override
    public List<Order> getAllOrdersByDate(String date) throws FlooringMasteryDataValidationException {
        validateData(date);
        List<Order> orderList = dao.getAllOrdersByDate(date);
        if (orderList.isEmpty()) {
            throw new FlooringMasteryDataValidationException("<Error> Order date does not exist. Order data could not be found "
                    + "for the given order date.");
        }
        return orderList;
    }

    @Override
    public Order getOrderByDate(String date, int orderNum) throws FlooringMasteryDataValidationException {
        List<Order> orderList = this.getAllOrdersByDate(date);
        Optional<Order> currentOrder = orderList.stream()
                .filter(order -> order.getOrderNumber() == orderNum).findFirst();
        Order order;
        try {
            order = currentOrder.get();
        } catch (NoSuchElementException e) {
            throw new FlooringMasteryDataValidationException("<Error> Order number does not exist. Order data could not be found "
                    + "for the given order number.", e);
        }
//        for (Order nextOrder : orderList) {
//            if (nextOrder.getOrderNumber() == orderNum) {
//                order = nextOrder;
//            }
//        }
        return order;
    }

    @Override
    public List<String> getAllOrderDates() {
        Map<String, List<Order>> orderDates = dao.getAllOrderDates();
        return new ArrayList<>(orderDates.keySet());
    }

    @Override
    public List<String> getAllFileNames() {
        return dao.getAllFileNames();
    }

    @Override
    public void saveCurrentWork() throws FlooringMasteryPersistenceException {
        dao.saveCurrentWork();
    }

    @Override
    public Order generateOrderNumAndDate() {
        List<Integer> orderNumList = dao.getAllOrders().stream().map(Order::getOrderNumber).collect(Collectors.toList());
        int orderNum = 0;
        for (int n : orderNumList) {
            if (orderNum < n) {
                orderNum = n;
            }
        }
        orderNum++;

        LocalDate ld = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String orderDate = ld.format(formatter);

        Order newOrder = new Order(orderDate, orderNum);
        return newOrder;
    }
    

    private void validateData(Order order) throws FlooringMasteryDataValidationException {
        List<String> productList = dao.getAllProducts().stream().map(Product::getProductType).collect(Collectors.toList());
        Map<String, String> taxMap = dao.getAllTaxes().stream()
                .map(e -> new HashMap.SimpleEntry<>(e.getStateFullName(), e.getStateAbbr()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<String> taxList = new ArrayList<>();

        if (order.getCustomerName() == null
                || order.getCustomerName().trim().length() == 0
                || order.getCustomerName().isEmpty()
                || order.getTaxData().getStateAbbr() == null
                || order.getTaxData().getStateAbbr().trim().length() == 0
                || order.getTaxData().getStateAbbr().isEmpty()
                || order.getProductData().getProductType() == null
                || order.getProductData().getProductType().trim().length() == 0
                || order.getProductData().getProductType().isEmpty()
                || order.getArea() == 0) {
            throw new FlooringMasteryDataValidationException(
                    "<Error> One or more fields [Customer Name, State, Product Type, Area] "
                    + "are empty. Please enter in all fields correctly.");
        }

        for (Map.Entry<String, String> entry : taxMap.entrySet()) {
            taxList.add(entry.getKey().toUpperCase());
            taxList.add(entry.getValue());
        }

        if (taxList.contains(order.getTaxData().getStateAbbr())) {
            for (Map.Entry<String, String> entry : taxMap.entrySet()) {
                if (order.getTaxData().getStateAbbr().equalsIgnoreCase(entry.getKey())) {
                    order.getTaxData().setStateFullName(entry.getKey().toLowerCase());
                    order.getTaxData().setStateAbbr(taxMap.get(order.getTaxData().getStateFullName()));
                    Tax currentTax = dao.getTax(order.getTaxData().getStateAbbr());
                    order.setTaxData(currentTax);
                } else if (order.getTaxData().getStateAbbr().equalsIgnoreCase(entry.getValue())) {
                    Tax currentTax = dao.getTax(order.getTaxData().getStateAbbr());
                    order.setTaxData(currentTax);
                }
            }
        } else {
            throw new FlooringMasteryDataValidationException(
                    "<Error> " + order.getTaxData().getStateAbbr()
                    + " is not from the selection of States. "
                    + "Please enter the correct State from the selection.");
        }

        if (productList.contains(order.getProductData().getProductType())) {
            Product currentProduct = dao.getProduct(order.getProductData().getProductType());
            order.setProductData(currentProduct);
        } else {
            throw new FlooringMasteryDataValidationException(
                    "<Error> " + order.getProductData().getProductType()
                    + " is not from the selection of Products. "
                    + "Please enter the correct Product Type from the selection.");
        }

        //Another way of validating data from product and state selection 
//        int productCount = 0;
//        int taxCount = 0;
//        productCount = productList.stream()
//                .filter(currentProductName -> (!order.getProductData().getProductType().equalsIgnoreCase(currentProductName)))
//                .map((increment) -> 1).reduce(productCount, Integer::sum);
//
//        taxCount = taxList.stream().filter((currentState) -> (!order.getTaxData().getStateAbbr().equalsIgnoreCase(currentState)))
//                .map((increment) -> 1).reduce(taxCount, Integer::sum);
//
//        if (taxCount == taxList.size()) {
//            throw new FlooringMasteryDataValidationException(
//                    "<Error> " + order.getTaxData().getStateAbbr()
//                    + " is not from the selection of States. "
//                    + "Please enter the correct State from the selection using state abbreviations.");
//        } else if (productCount == productList.size()) {
//            throw new FlooringMasteryDataValidationException(
//                    "<Error> " + order.getProductData().getProductType()
//                    + " is not from the selection of Products. "
//                    + "Please enter the correct Product Type from the selection.");
//        }
    }

    private boolean validateData(Order order, String[] orderInfo) throws FlooringMasteryDataValidationException {
        List<String> productList = dao.getAllProducts().stream()
                .map(Product::getProductType).collect(Collectors.toList());
        Map<String, String> taxMap = dao.getAllTaxes().stream()
                .map(tax -> new HashMap.SimpleEntry<>(tax.getStateFullName(), tax.getStateAbbr()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<String> taxList = new ArrayList<>();
        int orderInfoEmpty = 0;

        if (orderInfo[0] == null
                || orderInfo[1] == null
                || orderInfo[2] == null) {
            throw new FlooringMasteryDataValidationException(
                    "<Error> One or more fields [Customer Name, State, Product Type, Area] "
                    + "are not entered in correctly. Please enter in all fields correctly.");
        }

        if (orderInfo[0].trim().length() != 0
                && !orderInfo[0].isEmpty()
                && !orderInfo[0].equalsIgnoreCase(order.getCustomerName())) {
            order.setCustomerName(orderInfo[0]);
        } else {
            orderInfoEmpty++;
        }

        if (orderInfo[1].trim().length() != 0
                && !orderInfo[1].isEmpty()
                && !orderInfo[1].equalsIgnoreCase(order.getTaxData().getStateAbbr())
                && !orderInfo[1].equalsIgnoreCase(order.getTaxData().getStateFullName())) {

            for (Map.Entry<String, String> entry : taxMap.entrySet()) {
                taxList.add(entry.getKey().toUpperCase());
                taxList.add(entry.getValue());
            }

            if (taxList.contains(orderInfo[1])) {
                for (Map.Entry<String, String> entry : taxMap.entrySet()) {
                    if (orderInfo[1].equalsIgnoreCase(entry.getKey())) {
                        order.getTaxData().setStateFullName(entry.getKey().toLowerCase());
                        order.getTaxData().setStateAbbr(taxMap.get(order.getTaxData().getStateFullName()));
                        order.setTaxData(dao.getTax(order.getTaxData().getStateAbbr()));
                    } else if (orderInfo[1].equalsIgnoreCase(entry.getValue())) {
                        order.getTaxData().setStateAbbr(orderInfo[1].toUpperCase());
                        order.setTaxData(dao.getTax(order.getTaxData().getStateAbbr()));
                    }
                }
            } else {
                throw new FlooringMasteryDataValidationException(
                        "<Error> " + orderInfo[1]
                        + " is not from the selection of States. "
                        + "Please enter the correct State from the selection.");
            }

            //Another way of validating data from state selection
//            int taxCount = 0;
//            taxCount = taxList.stream().filter((currentState) -> (!orderInfo[1].equalsIgnoreCase(currentState)))
//                    .map((increment) -> 1).reduce(taxCount, Integer::sum);
//
//            if (taxCount == taxList.size()) {
//                throw new FlooringMasteryDataValidationException(
//                        "<Error> " + orderInfo[1]
//                        + " is not from the selection of States. "
//                        + "Please enter the correct State from the selection using state abbreviations.");
//            }
        } else {
            orderInfoEmpty++;
        }

        if (orderInfo[2].trim().length() != 0
                && !orderInfo[2].isEmpty()
                && !orderInfo[2].equalsIgnoreCase(order.getProductData().getProductType())) {
            if (productList.contains(orderInfo[2])) {
                order.getProductData().setProductType(orderInfo[2].toLowerCase());
                order.setProductData(dao.getProduct(order.getProductData().getProductType()));
            } else {
                throw new FlooringMasteryDataValidationException(
                        "<Error> " + orderInfo[2]
                        + " is not from the selection of Products. "
                        + "Please enter the correct Product Type from the selection.");
            }

            //Another way of validating data from product
//            int productCount = 0;
//            productCount = productList.stream()
//                    .filter(currentProductName -> (!orderInfo[2].equalsIgnoreCase(currentProductName)))
//                    .map((increment) -> 1).reduce(productCount, Integer::sum);
//
//            if (productCount == productList.size()) {
//                throw new FlooringMasteryDataValidationException(
//                        "<Error> " + orderInfo[2]
//                        + " is not from the selection of Products. "
//                        + "Please enter the correct Product Type from the selection.");
//            }
        } else {
            orderInfoEmpty++;
        }

        if (orderInfo[3].trim().length() != 0
                && !orderInfo[3].isEmpty()
                && !orderInfo[3].equalsIgnoreCase(Double.toString(order.getArea()))) {
            double area;
            try {
                area = Double.parseDouble(orderInfo[3]);
            } catch (NumberFormatException e) {
                throw new FlooringMasteryDataValidationException("<Error> Incorrect input: "
                        + orderInfo[3] + " is not a number. Please enter a number for area.", e);
            }
            if (area != 0) {
            order.setArea(area);
            } else {
                throw new FlooringMasteryDataValidationException("<Error> Area cannot be 0. "
                        + "Please enter a number greater than 0.");
            }
        } else {
            orderInfoEmpty++;
        }
        
        return orderInfoEmpty == 4;
    }

    private void validateData(String date) throws FlooringMasteryDataValidationException {
        Date orderDate;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        try {
            orderDate = formatter.parse(date);
        } catch (ParseException e) {
            throw new FlooringMasteryDataValidationException("<Error> Incorrect date. Please enter the date in MM-dd-yyyy format.", e);
        }
        if (date == null
                || date.trim().length() == 0
                || !date.equals(formatter.format(orderDate))) {
            throw new FlooringMasteryDataValidationException("<Error> Incorrect date. Please enter the date in mm-dd-yyyy format.");
        }
    }

    private void calculateCosts(Order order) {
        BigDecimal costPerSQFT = order.getProductData().getCostPerSquareFoot(),
                laborCostPerSQFT = order.getProductData().getLaborCostPerSquareFoot(),
                area = BigDecimal.valueOf(order.getArea()),
                taxRate = BigDecimal.valueOf(order.getTaxData().getTaxRate() * 0.01),
                materialCost, laborCost, totalTax, totalAmount;

        materialCost = costPerSQFT.multiply(area);
        laborCost = laborCostPerSQFT.multiply(area);
        totalTax = materialCost.multiply(taxRate)
                .add(laborCost.multiply(taxRate));
        totalAmount = materialCost.add(laborCost).add(totalTax);

        order.setMaterialCost(materialCost.setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(laborCost.setScale(2, RoundingMode.HALF_UP));
        order.setTotalTax(totalTax.setScale(2, RoundingMode.HALF_UP));
        order.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
    }

}
