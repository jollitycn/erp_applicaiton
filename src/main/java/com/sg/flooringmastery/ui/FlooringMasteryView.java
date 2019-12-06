/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringMasteryDataValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Al Rahman
 */
public class FlooringMasteryView {

    private UserIO io;

    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public void displayTitleBanner() {
        io.print("===========================================\n"
                + "||                                       ||\n"
                + "||               SWG Corp                ||\n"
                + "||           Flooring Program            ||\n"
                + "||                                       ||\n"
                + "===========================================");
    }

    public int printMenuAndGetSelection() throws FlooringMasteryDataValidationException {
        return io.readInt("<<Flooring Program Menu>>\n"
                + "1. Display Orders By Date\n"
                + "2. Display All Orders\n"
                + "3. Display All Dates\n"
                + "4. Add an Order\n"
                + "5. Edit an Order\n"
                + "6. Remove an Order\n"
                + "7. Save Current Work\n"
                + "8. Quit\n"
                + "Please select a number from the menu above:", 1, 8);
    }

    public Order getNewOrderInfo(Order newOrder) throws FlooringMasteryDataValidationException {
        String customerName = io.readString("Please enter the Customer Name:");
        String state = io.readString("Please select a State from the following selection [OH, PA, MI, IN]:");
        String productType = io.readString("Please select a Product Type from the following selection [wood, laminate, tile, carpet]:");
        double area = io.readDouble("Please enter the Area amount:");

        newOrder.setCustomerName(customerName);
        newOrder.getTaxData().setStateAbbr(state.toUpperCase());
        newOrder.getProductData().setProductType(productType.toLowerCase());
        newOrder.setArea(area);

        return newOrder;
    }

    public String[] getEditOrderInfo(Order currentOrder) throws FlooringMasteryDataValidationException {
        String customerName = "", state = "", productType = "", area = "";
        if (currentOrder != null) {
            io.readString("Instructions:\n"
                    + "* The previous order data will be shown in parentheses.\n"
                    + "* Press enter to leave a field unchanged.\n"
                    + "* Edit the data for this order by entering new information for each field. This will\n"
                    + "  overwrite the previous order data (in parentheses) with the new information entered.");

            customerName = io.readString("Please enter the Customer Name: (" + currentOrder.getCustomerName() + ")");
            if (customerName.trim().length() == 0
                    && customerName.isEmpty()
                    || customerName.equalsIgnoreCase(currentOrder.getCustomerName())) {
                io.print("* Field is left unchanged. [" + currentOrder.getCustomerName() + "] is kept as Customer Name.\n");
            } else {
                io.print("* You have edited Customer Name from: [" + currentOrder.getCustomerName() + "] to : " + customerName + ".\n");
            }
            
            String stateFullName = currentOrder.getTaxData().getStateFullName().substring(0, 1).toUpperCase()
                        + currentOrder.getTaxData().getStateFullName().substring(1);

            state = io.readString("Please select a State from the following selection [OH, PA, MI, IN]: ("
                    + stateFullName + " - " + currentOrder.getTaxData().getStateAbbr() + ")");
            if (state.trim().length() == 0
                    && state.isEmpty()
                    || state.equalsIgnoreCase(currentOrder.getTaxData().getStateAbbr())
                    || state.equalsIgnoreCase(currentOrder.getTaxData().getStateFullName())) {
                io.print("* Field is left unchanged. [" + stateFullName + " - " + currentOrder.getTaxData().getStateAbbr() 
                        + "] is kept as State.\n");
            } else {
                io.print("* You have edited State from: [" + stateFullName + " - " + currentOrder.getTaxData().getStateAbbr() 
                        + "] to : " + state.toUpperCase() + ".\n");
            }

            productType = io.readString("Please select a Product Type from the following selection "
                    + "[wood, laminate, tile, carpet]: (" + currentOrder.getProductData().getProductType() + ")");
            if (productType.trim().length() == 0
                    && productType.isEmpty()
                    || productType.equalsIgnoreCase(currentOrder.getProductData().getProductType())) {
                io.print("* Field is left unchanged. [" + currentOrder.getProductData().getProductType()
                        + "] is kept as Product Type.\n");
            } else {
                io.print("* You have edited Product Type from: [" + currentOrder.getProductData().getProductType()
                        + "] to : " + productType.toLowerCase() + ".\n");
            }

            area = io.readString("Please enter the Area amount: (" + currentOrder.getArea() + " sq ft.)");
            if (area.trim().length() == 0
                    && area.isEmpty()
                    || area.equalsIgnoreCase(Double.toString(currentOrder.getArea()))) {
                io.print("* Field is left unchanged. [" + currentOrder.getArea() + " sq ft.] is kept as Area.\n");
            } else {
                io.print("* You have edited Area from: [" + currentOrder.getArea() + " sq ft.] to : " + area + " sq ft.\n");
            }
        } else {
            io.print("No order exists for that date or order number.");
        }
        return new String[]{customerName, state.toUpperCase(), productType.toLowerCase(), area};
    }
    
    public void displayOrder(List<Order> orderList) {
        if (!orderList.isEmpty()) {
            io.print("\n*******************************"
                    + "Order Summary"
                    + "*******************************");
            for (Order currentOrder : orderList) {
                String state = currentOrder.getTaxData().getStateFullName().substring(0, 1).toUpperCase()
                        + currentOrder.getTaxData().getStateFullName().substring(1);
                io.print(displayLineSeparators() + "\n");
                io.print("[Date]: " + currentOrder.getOrderDate() + " || "
                        + "[Order Number]: " + currentOrder.getOrderNumber() + " || "
                        + "[Customer Name]: " + currentOrder.getCustomerName() + " || "
                        + "[State]: " + state + " (" + currentOrder.getTaxData().getStateAbbr() + ") || "
                        + "[State Tax Rate]: " + currentOrder.getTaxData().getTaxRate() + "% || "
                        + "[Product Type]: " + currentOrder.getProductData().getProductType() + " || "
                        + "[Area]: " + currentOrder.getArea() + " sq ft. || "
                        + "[Cost Per Square Foot]: $" + currentOrder.getProductData().getCostPerSquareFoot() + " || "
                        + "[Labor Cost Per Square Foot]: $" + currentOrder.getProductData().getLaborCostPerSquareFoot() + " || "
                        + "[Material Cost]: $" + currentOrder.getMaterialCost() + " || "
                        + "[Labor Cost]: $" + currentOrder.getLaborCost() + " || "
                        + "[Total Tax]: $" + currentOrder.getTotalTax() + " || "
                        + "[Total Amount]: $" + currentOrder.getTotalAmount() + " || ");
            }
            io.print(displayLineSeparators() + "\n");
        } else {
            io.print("No order exists for that date or order number.");
        }
    }

    public void displayOrders(List<Order> orderList) {
        if (!orderList.isEmpty()) {
            io.print("\n*******************************"
                    + "Order Summary"
                    + "*******************************");
            for (Order currentOrder : orderList) {
                String state = currentOrder.getTaxData().getStateFullName().substring(0, 1).toUpperCase()
                        + currentOrder.getTaxData().getStateFullName().substring(1);
                io.print(displayLineSeparators() + "\n");
                io.print("[Date]: " + currentOrder.getOrderDate() + " || "
                        + "[Order Number]: " + currentOrder.getOrderNumber() + " || "
                        + "[Customer Name]: " + currentOrder.getCustomerName() + " || "
                        + "[State]: " + state + " (" + currentOrder.getTaxData().getStateAbbr() + ") || "
                        + "[State Tax Rate]: " + currentOrder.getTaxData().getTaxRate() + "% || "
                        + "[Product Type]: " + currentOrder.getProductData().getProductType() + " || "
                        + "[Area]: " + currentOrder.getArea() + " sq ft. || "
                        + "[Cost Per Square Foot]: $" + currentOrder.getProductData().getCostPerSquareFoot() + " || "
                        + "[Labor Cost Per Square Foot]: $" + currentOrder.getProductData().getLaborCostPerSquareFoot() + " || "
                        + "[Material Cost]: $" + currentOrder.getMaterialCost() + " || "
                        + "[Labor Cost]: $" + currentOrder.getLaborCost() + " || "
                        + "[Total Tax]: $" + currentOrder.getTotalTax() + " || "
                        + "[Total Amount]: $" + currentOrder.getTotalAmount() + " || ");
            }
            io.print(displayLineSeparators() + "\n");
        } else {
            io.print("No order exists for that date or order number.");
        }
    }

    public void displayOrderDates(List<String> listOfDates, List<String> listOfFileNames) {
        if (!listOfDates.isEmpty()) {
            io.print("---------- List of Order Dates in File ----------");
            listOfDates.forEach(currentDate -> io.print(currentDate));
        } else {
            io.print("There are no dates listed in file yet.");
        }
        if (!listOfFileNames.isEmpty()) {
            io.print("---------- List of File Names ----------");
            listOfFileNames.forEach(currentFile -> io.print(currentFile));
        } else {
            io.print("There are no file names yet.");
        }
        io.print("\n");
    }

    public boolean promptSaveData() {
        String input = "";
        do {
            input = io.readString("Would you like to keep this data? [Y/N]");
            if (this.readYesOrNo(input)) {
                this.displayYesNoInputError();
            } else if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                return true;
            }
        } while (this.readYesOrNo(input));
        return false;
    }

    public String promptRemoveData() {
        String input = "";
        do {
            input = io.readString("Would you like to remove this order? [Y/N]");
            if (this.readYesOrNo(input)) {
                this.displayYesNoInputError();
            } else if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                input = io.readString("Are you sure?");
            }
        } while (this.readYesOrNo(input));
        return input;
    }

    public boolean promptUserToContinue(boolean hasErrors, String message) {
        String input;
        if (hasErrors) {
            do {
                input = io.readString("Would you like to continue " + message + "? Press [Y] "
                        + "to contine or [N] to return to the main menu.");
                if (this.readYesOrNo(input)) {
                    this.displayYesNoInputError();
                } else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
                    return true;
                }
            } while (this.readYesOrNo(input));
        }
        return false;
    }

    public String promptUserForDate() {
        return io.readString("Please enter the date in MM-dd-yyyy format:");
    }

    public int promptUserForOrderNum() throws FlooringMasteryDataValidationException {
        return io.readInt("Please enter the order number:");
    }

    public void prompEnterToContinue() {
        io.readString("Please hit enter to continue.");
    }

    public void displayAllOrdersByDateBanner() {
        io.print("============ Display All Orders By Date ============");
    }

    public void displayAllOrdersBanner() {
        io.print("============= Display All Orders =============");
    }

    public void displayAllDatesBanner() {
        io.print("============= Display All Dates =============");
    }

    public void displayAddOrderBanner() {
        io.print("================ Add Order ================");
    }

    public void displayRemoveOrderBanner() {
        io.print("================ Remove Order ================");
    }

    public void displayEditOrderBanner() {
        io.print("================ Edit Order ================");
    }

    public void displayAddOrderSuccessBanner(Order order) {
        if (order != null) {
            io.readString("Order successfully added. Please hit enter to continue.");
        } else {
            io.readString("New order data discarded. Please hit enter to continue.");
        }
    }

    public void displayEditOrderSuccessBanner(boolean orderInfoEmpty) {
//        List<String> CheckOrderInfo = Arrays.asList(orderInfo).stream()
//                        .filter(s -> !s.isEmpty()).collect(Collectors.toList());
//        !CheckOrderInfo.isEmpty()
        if (!orderInfoEmpty) {
            io.readString("Order successfully edited. Please hit enter to continue.");
        } else {
            io.readString("Order data was left unchanged. Please hit enter to continue.");
        }
    }

    public void displayRemoveOrderSuccessBanner(Order order) {
        if (order == null) {
            io.readString("Order successfully removed from file. Please hit enter to continue.");
        } else {
            io.readString("Order was not removed from file. Please hit enter to continue.");
        }
    }

    public void displaySaveSucessBanner() {
        io.readString("All order data was successfully saved in file. Please hit enter to continue.");
    }

    public void displayExitBanner() {
        io.print("Current data saved.\n"
                + "Thank you for using SWG Corp Flooring Program.");
    }

    public String displayLineSeparators() {
        String line = "";
        for (int i = 0; i < 8; i++) {
            line += "-------------------------------------------";
        }
        return line;
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String errorMessage) {
        io.print("\n=============== ERROR ===============\n"
                + errorMessage);
        io.readString("Hit enter to continue.");
    }

    public void displayYesNoInputError() {
        io.print("<Error> Please enter [Y/N].");
        io.readString("Hit enter to continue.");
    }

    public boolean readYesOrNo(String input) {
        return !(input.equalsIgnoreCase("y"))
                && !(input.equalsIgnoreCase("n"))
                && !(input.equalsIgnoreCase("yes"))
                && !(input.equalsIgnoreCase("no"));
    }
}
