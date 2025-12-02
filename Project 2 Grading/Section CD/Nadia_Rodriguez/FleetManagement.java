import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
   Main program for managing a fleet of boats. Handles user interface,
   file I/O (CSV and serialized data), and menu operations including
   printing inventory, adding boats, removing boats, and processing
   expenses.
*/

public class FleetManagement {
    private static final String DATABASE_FILE = "FleetData.db";
    private Scanner keyboard;
    private Fleet fleet;

    /**
     * Constructor for FleetManagementSystem
     */
    public FleetManagement() {
        keyboard = new Scanner(System.in);
        fleet = new Fleet();
    }

    /**
     * Main entry point for the program
     * @param args Command line arguments (optional CSV filename)
     */
    public static void main(String[] args) {
        FleetManagement system;

        system = new FleetManagement();
        system.run(args);
    }

    /**
     * Main program loop
     * @param args Command line arguments
     */
    public void run(String[] args) {
        File dbFile;
        boolean running;
        char choice;

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        //----Load fleet data from database or CSV
        dbFile = new File(DATABASE_FILE);
        if (dbFile.exists()) {
            fleet = loadFromDatabase(DATABASE_FILE);
        } else {
            if (args.length > 0) {
                fleet = loadFromCSV(args[0]);
            } else {
                fleet = new Fleet();
            }
        }

        //----Main menu loop
        running = true;
        while (running) {
            displayMenu();
            choice = getMenuChoice();

            if (choice == 'P') {
                handlePrint();
            } else if (choice == 'A') {
                handleAdd();
            } else if (choice == 'R') {
                handleRemove();
            } else if (choice == 'E') {
                handleExpense();
            } else if (choice == 'X') {
                running = false;
            } else {
                System.out.println("Invalid menu option, try again");
            }
        }

        //----Save and exit
        saveToDatabase(DATABASE_FILE, fleet);
        System.out.println();
        System.out.println("Exiting the Fleet Management System");
    }

    /**
     * Displays the menu options
     */
    private void displayMenu() {
        System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
    }

    /**
     * Gets and validates menu choice from user
     * @return The menu choice as uppercase character
     */
    private char getMenuChoice() {
        String input;
        char choice;

        input = keyboard.nextLine().trim();
        if (input.length() > 0) {
            choice = Character.toUpperCase(input.charAt(0));
        } else {
            choice = ' ';
        }
        return choice;
    }

    /**
     * Handles the Print menu option
     */
    private void handlePrint() {
        fleet.printInventory();
    }

    /**
     * Handles the Add menu option
     */
    private void handleAdd() {
        String csvData;
        Boat newBoat;

        System.out.print("Please enter the new boat CSV data          : ");
        csvData = keyboard.nextLine();

        newBoat = Boat.parseCSV(csvData);
        fleet.addBoat(newBoat);
        System.out.println();
    }

    /**
     * Handles the Remove menu option
     */
    private void handleRemove() {
        String boatName;
        boolean removed;

        System.out.print("Which boat do you want to remove?           : ");
        boatName = keyboard.nextLine();

        removed = fleet.removeBoat(boatName);
        if (!removed) {
            System.out.println("Cannot find boat " + boatName);
        }
        System.out.println();
    }

    /**
     * Handles the Expense menu option
     */
    private void handleExpense() {
        String boatName;
        Boat boat;
        double amount;
        double remaining;

        System.out.print("Which boat do you want to spend on?         : ");
        boatName = keyboard.nextLine();

        boat = fleet.findBoat(boatName);
        if (boat == null) {
            System.out.println("Cannot find boat " + boatName);
        } else {
            System.out.print("How much do you want to spend?              : ");
            amount = Double.parseDouble(keyboard.nextLine());

            if (boat.canSpend(amount)) {
                boat.addExpense(amount);
                System.out.printf("Expense authorized, $%.2f spent.%n",
                        boat.getExpenses());
            } else {
                remaining = boat.getRemainingBudget();
                System.out.printf(
                        "Expense not permitted, only $%.2f left to spend.%n",
                        remaining);
            }
        }
        System.out.println();
    }

    /**
     * Loads fleet data from a CSV file
     * @param filename The name of the CSV file
     * @return A Fleet object populated with boat data
     */
    private Fleet loadFromCSV(String filename) {
        Fleet newFleet;
        Scanner fileScanner;
        String line;
        Boat boat;

        newFleet = new Fleet();

        try {
            fileScanner = new Scanner(new File(filename));

            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine().trim();
                if (line.length() > 0) {
                    boat = Boat.parseCSV(line);
                    newFleet.addBoat(boat);
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find file " + filename);
        }

        return newFleet;
    }

    /**
     * Loads fleet data from a serialized database file
     * @param filename The name of the database file
     * @return A Fleet object
     */
    private Fleet loadFromDatabase(String filename) {
        Fleet loadedFleet;
        FileInputStream fileIn;
        ObjectInputStream objectIn;

        loadedFleet = new Fleet();

        try {
            fileIn = new FileInputStream(filename);
            objectIn = new ObjectInputStream(fileIn);
            loadedFleet = (Fleet) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find database file");
        } catch (IOException e) {
            System.out.println("Error: Problem reading database file");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Problem loading fleet data");
        }

        return loadedFleet;
    }

    /**
     * Saves fleet data to a serialized database file
     * @param filename The name of the database file
     * @param fleet The Fleet object to save
     */
    private void saveToDatabase(String filename, Fleet fleet) {
        FileOutputStream fileOut;
        ObjectOutputStream objectOut;

        try {
            fileOut = new FileOutputStream(filename);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(fleet);
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error: Problem saving database file");
        }
    }
}