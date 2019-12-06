/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.dto.Configuration;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringMasteryDataValidationException;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Al Rahman
 */
public class FlooringMasteryController {

    FlooringMasteryView view;
    FlooringMasteryServiceLayer serviceLayer;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayer serviceLayer) {
        this.view = view;
        this.serviceLayer = serviceLayer;
    }

    public void run(Configuration settings) {
        int menuSelection = 0;
        boolean keepGoing = true;
        view.displayTitleBanner();
        while (keepGoing) {
            try {
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        this.displayOrdersByDate();
                        break;
                    case 2:
                        this.displayAllOrders();
                        break;
                    case 3:
                        this.displayAllOrderDates();
                        break;
                    case 4:
                        this.addOrder();
                        break;
                    case 5:
                        this.editOrder();
                        break;
                    case 6:
                        this.removeOrder();
                        break;
                    case 7:
                        this.saveCurrentWork();
                        break;
                    case 8:
                        try {
                            this.exit();
                        } catch (FlooringMasteryPersistenceException e) {
                            return;
                        }   //this try-catch is just for training mode when persistence exception is thrown for saving work
                        keepGoing = false;
                        break;
                    default:
                        view.displayUnknownCommandBanner();
                        break;
                }
            } catch (FlooringMasteryPersistenceException | FlooringMasteryDataValidationException e) {
                view.displayErrorMessage(e.getMessage());
            } 
//            catch (Exception e) {
//                view.displayErrorMessage(e.getMessage());
//            }
        }
    }

    private int getMenuSelection() throws FlooringMasteryDataValidationException {
        int selection = 0;
        boolean hasErrors;
        do {
            try {
                selection = view.printMenuAndGetSelection();
                hasErrors = false;
            } catch (FlooringMasteryDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        return selection;
    }

    private void displayOrdersByDate() throws FlooringMasteryDataValidationException {
        boolean hasErrors = false;
        String date;
        view.displayAllOrdersByDateBanner();
        do {
            if (view.promptUserToContinue(hasErrors, "displaying orders by date")) return;
            date = view.promptUserForDate();
            try {
                view.displayOrders(serviceLayer.getAllOrdersByDate(date));
                hasErrors = false;
            } catch (FlooringMasteryDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        view.prompEnterToContinue();
    }

    private void displayAllOrders() {
        view.displayAllOrdersBanner();
        view.displayOrders(serviceLayer.getAllOrders());
        view.prompEnterToContinue();
    }

    private void displayAllOrderDates() {
        view.displayAllDatesBanner();
        view.displayOrderDates(serviceLayer.getAllOrderDates(), serviceLayer.getAllFileNames());
        view.prompEnterToContinue();
    }

    private void addOrder() throws FlooringMasteryDataValidationException {
        boolean hasErrors = false;
        List<Order> newOrderList = new ArrayList<>();
        Order newOrder = null;
        view.displayAddOrderBanner();
        do {
            if (view.promptUserToContinue(hasErrors, "adding an order")) return;
            try {
                newOrder = view.getNewOrderInfo(serviceLayer.generateOrderNumAndDate());
                serviceLayer.addOrder(newOrder);
                hasErrors = false;
            } catch (FlooringMasteryDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        newOrderList.add(newOrder);
        view.displayOrders(newOrderList);
        if (!view.promptSaveData()) {
            serviceLayer.removeOrder(newOrder);
        }
        view.displayAddOrderSuccessBanner(serviceLayer.getOrder(newOrder.getOrderNumber()));
    }

    private void editOrder() throws FlooringMasteryDataValidationException {
        boolean hasErrors = false;
        boolean orderInfoEmpty = false;
        String date;
        int orderNum;
        List<Order> orderList = new ArrayList<>();
        Order currentOrder = null;
        view.displayEditOrderBanner();
        do {
            if (view.promptUserToContinue(hasErrors, "editing an order")) return;
            date = view.promptUserForDate();
            orderNum = view.promptUserForOrderNum();
            try {
                currentOrder = serviceLayer.getOrderByDate(date, orderNum);
                hasErrors = false;
            } catch (FlooringMasteryDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        do {
            if (view.promptUserToContinue(hasErrors, "editing an order")) return;
            try {
                orderInfoEmpty = serviceLayer.editOrder(currentOrder, view.getEditOrderInfo(currentOrder));
                hasErrors = false;
            } catch (FlooringMasteryDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        orderList.add(currentOrder);
        view.displayOrders(orderList);
        view.displayEditOrderSuccessBanner(orderInfoEmpty);
    }

    private void removeOrder() throws FlooringMasteryDataValidationException {
        boolean hasErrors = false;
        String date;
        int orderNum;
        List<Order> orderList = new ArrayList<>();
        Order currentOrder = null;
        view.displayRemoveOrderBanner();
        do {
            if (view.promptUserToContinue(hasErrors, "removing an order")) return;
            date = view.promptUserForDate();
            orderNum = view.promptUserForOrderNum();
            try {
                currentOrder = serviceLayer.getOrderByDate(date, orderNum);
                if (currentOrder != null) {
                    orderList.add(currentOrder);
                }
                hasErrors = false;
            } catch (FlooringMasteryDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        view.displayOrders(orderList);
        String input = view.promptRemoveData();
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            serviceLayer.removeOrder(currentOrder);
        }
        view.displayRemoveOrderSuccessBanner(serviceLayer.getOrder(currentOrder.getOrderNumber()));
    }

    private void saveCurrentWork() throws FlooringMasteryPersistenceException {
        serviceLayer.saveCurrentWork();
        view.displaySaveSucessBanner();
    }

    private void exit() throws FlooringMasteryPersistenceException {
        serviceLayer.saveCurrentWork();
        view.displayExitBanner();
    }
}
