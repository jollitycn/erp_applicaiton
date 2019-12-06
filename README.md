# ERP-Business-Application
A Java console-based Enterprise Resource Planning (ERP) Business Application. 

## Overview
The goal of this mastery project is to create an application that allows for reading and writing flooring orders for SWG Corp. Your complete application must demonstrate your understanding of all the topics covered in the prior sections, including:

- N-Tier/MVC architecture including the use of a Service Layer
- Interfaces
- Spring dependency injection
- Spring aspect-oriented programming
- Unit testing

## Requirements
The application will have a configuration file to set the mode to either Training or Prod. If the mode is Training, then the application should load all existing order data but it should not persist any new or modified order data. If the mode is Prod, then the application should read and write order information from a file called Orders_MMDDYYYY.txt

An Order consists of an order number, customer name, state, tax rate, product type, area, cost per square foot, labor cost per square foot, material cost, labor cost, tax, and total.

Taxes and product type information can be found in the Data/Taxes.txt and Data/Products.txt files. The customer state and product type entered by a user must match items from these files. This information is read in from the file in both production and training mode.

Orders_06012013.txt is a sample row of data for one order.

## Architectural Guidance
1. Use nTier development and MVC principles in structuring code to handle products, taxes, and orders appropriately.  Your application should follow the MVC pattern presented in the course.
2. Use unit tests and integration tests to ensure that your data layers and business logic (service layer) code is covered.
3. If time allows, implement an audit trail using aspect-oriented programming.

## Rules of the Application
1. This is using an enterprise MVC architecture for this project so your code must be organized into reasonable classes.  You will draw a  UML class diagram and high-level flowchart before proceeding with writing code.
2. For an enterprise architecture, we must layer our code.
- The model package may only contain classes that have data members (properties).
- The dao package contains classes that are responsible for persisting data.
- The controller package contains classes that orchestrate the program.
- The view package contains classes that interact with the user.
- The service package contains the service layer components.  
- You will use your UserIO class (along with the View component) to handle all IO for the user.
3. Build this application following the process outlined in the Agile Approach Checklist for Console Applications document.

## User Stories
The UI should start with a menu to prompt the user for what they would like to do:

    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
    *  <<ERP Program>>
    * 1. Display Orders
    * 2. Add an Order
    * 3. Edit an Order
    * 4. Remove an Order
    * 5. Save Current Work
    * 6. Quit
    * 
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
### Display Orders
Display orders will ask the user for a date and then display the orders for that date. If no orders exist for that date, it will display an error message and return the user to the main menu.

### Add an Order
Add an order will ask the user for each piece of order data. At the end, it will display a summary of the data entered and ask the user to commit (Y/N). If yes, the data will be written to the orders list. If no, the data will be discarded and the user returned to the main menu.

The system should generate an order number for the user based on the next number in the file (so if there are two orders, the next order should be #3)

### Edit an Order
Edit will ask the user for a date and order number. If the order exists for that date, it will ask the user for each piece of order data but display the existing data. If the user enters something new, it will replace that data; if the user hits Enter without entering data, it will leave the existing data in place. For example:

Enter customer name (Wise):
If the user enters a new name, the name will replace Wise, otherwise it will leave it as-is.

### Remove an Order
For removing an order, the system should ask for the date and order number. If it exists, the system should display the order information and prompt the user if they are sure. If yes, it should be removed from the list.

Any time a user enters invalid data, the system should ask them again until they enter valid data. As in our previous labs, all existing order data will be read in from files when the program starts and will be written to the files when the program exits. This program also allows the user to save their current work when requested (see menu option 5 above).
