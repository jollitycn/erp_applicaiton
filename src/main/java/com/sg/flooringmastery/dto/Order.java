/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dto;

import java.math.BigDecimal;

/**
 *
 * @author Al Rahman
 */
public class Order {

    private final String orderDate;
    private final int orderNumber;
    private String customerName;
    private double area;
    private Product productData;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private Tax taxData;
    private BigDecimal totalTax;
    private BigDecimal totalAmount;

    public Order(String orderDate, int orderNumber) {
        this.productData = new Product();
        this.taxData = new Tax();
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Product getProductData() {
        return productData;
    }

    public void setProductData(Product productData) {
        this.productData = productData;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public Tax getTaxData() {
        return taxData;
    }

    public void setTaxData(Tax taxData) {
        this.taxData = taxData;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Order Date: " + orderDate + " |Order Number: " + orderNumber + " |Customer Name: " + customerName 
                + " |State: " + taxData.getStateAbbr() + " | Tax Rate: " + taxData.getTaxRate() 
                + " |Product Type: " + productData.getProductType() + " |Area: " + Double.toString(area) 
                + " |Cost per sq ft.: " + productData.getCostPerSquareFoot() 
                + " |Labor cost per sq ft.: " + productData.getLaborCostPerSquareFoot() + " |Material Cost: " + materialCost 
                + " |Labor Cost: " + laborCost + " |Total Tax: " + totalTax + " |Total Amount: " + totalTax;
    }
}
