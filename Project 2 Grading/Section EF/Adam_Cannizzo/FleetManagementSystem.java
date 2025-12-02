import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * main class for the entire Fleet Management System which handles loading and saving the boat data and user menu
 */
public class FleetManagementSystem {

    //-----------------------------------------------------------------------------------------------------------------
    // Constants

    private static final String DB_FILE_NAME = "FleetData.db";

    //-----------------------------------------------------------------------------------------------------------------
    // Fields

    private static Scanner keyboard = new Scanner(System.in);
    static ArrayList<Boat> fleetList = new ArrayList<>();

    //-----------------------------------------------------------------------------------------------------------------
    // Main

    /**
     * The entry point of the Fleet Management System
     * @param args - The command line arguments
     */
    public static void main(String[] args) {
        FleetManagementSystem app;
        app = new FleetManagementSystem();
        app.run(args);
    } // end of main method

    //-----------------------------------------------------------------------------------------------------------------
    // Constructor

    /**
     * This creates a new system
     */
    public FleetManagementSystem() {
        fleetList = new ArrayList<Boat>();
        keyboard = new Scanner(System.in);
    }

    //-----------------------------------------------------------------------------------------------------------------

    /**
     * This runs the application by loading the data, showing the menu and saving the data
     * @param args
     */
    public void run(String[] args) {
        boolean loadedFromDb;

        printWelcomeMessage();

        loadedFromDb = false;

        if (args.length == 1) {
            loadFromCSV(args[0]);
            // the initializing run
        } else {
            //for subsequent run
            loadedFromDb = loadFromDb();
        }

        menuLoop();

        saveToDb();

        printExitMessage();
    } // end of run

    //-----------------------------------------------------------------------------------------------------------------
    // Display

    /**
     * Prints the welcome message
     */
    private void printWelcomeMessage() {
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();
    } // end of printWelcome

    /**
     * Prints the exit message
     */
    private void printExitMessage() {
        System.out.println();
        System.out.println("Exiting the Fleet Management System");
    } // end of printExitMessage

    //-----------------------------------------------------------------------------------------------------------------
    // CSV File Handling

    /**
     * Loads fleet data from the CSV file
     * Each line of the file contains type, name, year, make/model, length, and price
     * @param fileName the CSV file name
     */
    private void loadFromCSV(String fileName) {
        File file;
        Scanner fileScanner;
        String line;
        Boat boat;

        file = new File(fileName);

        try {
            fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine();

                if (line.length() > 0) {
                    boat = parseBoatFromCsvLine(line);
                    if (boat != null) {
                        fleetList.add(boat); // adds boat to list from original csv file
                    } // end of inner if

                } // end of if

            } // end of while

            fileScanner.close();
        } /* end of try */ catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } // end of catch

    } // end of loadFromCSV

    /**
     * Parses a single CSV line into the boat object
     * @param line CSV line
     * @return returns the boat object, or null if the parsing fails
     */
    private Boat parseBoatFromCsvLine(String line) {
        Boat boat;
        String[] parts;
        String typeString;
        String boatName;
        String makeModel;
        BoatType type;
        int manufactureYear;
        double lengthFeet;
        double purchasePrice;
        double expenses;

        parts = line.split(",");

        if (parts.length < 6) {
            return null;
        }

        typeString = parts[0].toUpperCase();
        boatName = parts[1];
        manufactureYear = Integer.parseInt(parts[2]);
        makeModel = parts[3];
        lengthFeet = Double.parseDouble(parts[4]);
        purchasePrice = Double.parseDouble(parts[5]);
        expenses = 0.0;

        type = BoatType.valueOf(typeString);

        boat = new Boat(type, boatName, manufactureYear, makeModel, lengthFeet, purchasePrice, expenses);

        return boat;
    } // end of parseBoatFromCsvLine

    //-----------------------------------------------------------------------------------------------------------------
    // DB File

    /**
     * This loads fleet data from the serialized DB file provided if it exists
     * @return it returns true if loaded successfully, and false if not
     */
    private boolean loadFromDb() {
        File file;
        FileInputStream fileIn;
        ObjectInputStream objectIn;
        ArrayList<Boat> loadedFleet;

        file = new File(DB_FILE_NAME);

        if (!file.exists()) {
            return false;
        } // end of if statement

        try {
            fileIn = new FileInputStream(file);
            objectIn = new ObjectInputStream(fileIn);

            loadedFleet = (ArrayList<Boat>) objectIn.readObject();
            fleetList = loadedFleet;

            objectIn.close();
            fileIn.close();

            return true;
        } /* end of try */ catch (IOException e) {
            System.out.println("Error reading DB file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found while reading DB file: " + e.getMessage());
        }

        return false;
    } // end of loadFromDb

    /**
     * This saves the current fleet data to the DB file
     */
    private void saveToDb() {
        FileOutputStream fileOut;
        ObjectOutputStream objectOut;

        try {
            fileOut = new FileOutputStream(DB_FILE_NAME);
            objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(fleetList);

            objectOut.close();
            fileOut.close();
        } /* end of try */ catch (IOException e) {
            System.out.println("Error writing DB file: " + e.getMessage());
        }
    } // end of saveToDb

    //-----------------------------------------------------------------------------------------------------------------
    // Menu

    /**
     * This Displays the menu option for the user to answer until they select exit
     */
    private void menuLoop() {
        String choiceLine;
        char choice;
        boolean done;

        done = false; // this will end the loop when true

        while (!done) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choiceLine = keyboard.nextLine();

            if (choiceLine.length() == 0) {
                choice = ' ';
            } /* end of if */ else {
                choice = Character.toUpperCase(choiceLine.charAt(0));
            } // end of else

            if (choice == 'P') {
                optionPrint();
            } else if (choice == 'A') {
                optionAddBoat();
            } else if (choice == 'R') {
                optionRemoveBoat();
            } else if (choice == 'E') {
                optionExpense();
            } else if (choice == 'X') {
                done = true;
            } else {
                System.out.println("Invalid menu option, try again");
            } // end of if
        } // end of while loop
    } // end of menuLoop

    //-----------------------------------------------------------------------------------------------------------------
    // Options for the menu

    /**
     * handles the print option and its functions which prints the fleet report
     */
    private void optionPrint() {
        int index;
        Boat boat;
        double totalPaid;
        double totalSpent;

        System.out.println();
        System.out.println("Fleet report:");

        totalPaid = 0.0;
        totalSpent = 0.0;

        for (index = 0; index < fleetList.size(); index++) {
            boat = fleetList.get(index);
            System.out.println(boat.toString());
            totalPaid = totalPaid + boat.getPurchasePrice();
            totalSpent = totalSpent + boat.getExpenses();
        } // end of for

        System.out.printf("    %-47s   : Paid $ %8.2f : Spent $ %8.2f%n", "Total", totalPaid, totalSpent); /* prints
        total line */
        System.out.println();
    } // end of optionPrint

    /**
     * Handles the operations of adding a boat which prompts for a CSV line and then adds the new boat
     */
    private void optionAddBoat() {
        String line;
        Boat boat;

        System.out.print("Please enter the new boat CSV data : ");
        line = keyboard.nextLine();

        if (line.length() > 0) {
            boat = parseBoatFromCsvLine(line); // adds boat to csv
            if (boat != null) {
                fleetList.add(boat);
            } // end of inner if
        } // end of if

        System.out.println();

    } // end of optionAddBoat

    /**
     * This handles the operation of the Remove option which removes a boat by name form the list
     */
    private void optionRemoveBoat() {
        String boatName;
        Boat boat;
        boolean removed;
        int index;

        System.out.print("Which boat do you want to remove? : ");
        boatName = keyboard.nextLine();

        removed = false;

        for (index = 0; index < fleetList.size() && !removed; index++) {
            boat = fleetList.get(index);
            if (boat.getBoatName().equalsIgnoreCase(boatName)) {
                fleetList.remove(index);
                removed = true;
            } // end of if
        } // end of for

        if (!removed) {
            System.out.println("Cannot find boat " + boatName);
        } // end of if

        System.out.println();

    } // end of optionRemoveBoat

    private void optionExpense() {
        String boatName;
        String amountLine;
        double amount;
        Boat boat;
        boolean found;
        int index;

        System.out.print("Which boat do you want to spend on? : ");
        boatName = keyboard.nextLine();

        boat = null;
        found = false;

        for (index = 0; index < fleetList.size() && !found; index++) {
            boat = fleetList.get(index);
            if (boat.getBoatName().equalsIgnoreCase(boatName)) {
                found = true;
            } // end of if
        } // end of for

        if (!found) {
            System.out.println("Cannot find boat " + boatName);
            System.out.println();
            return;
        } // end of if

        System.out.print("How much do you want to spend? : ");
        amountLine = keyboard.nextLine();
        amount = Double.parseDouble(amountLine);

        if (boat.canSpend(amount)) {
            boat.addExpense(amount);
            System.out.printf("Expense authorized, $%.2f spent.%n", boat.getExpenses());
        } /* end of if */ else {
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", boat.getRemainingBudget());
        } // end of else

        System.out.println();

    } // end of optionExpense
} // end of Fleet Management System class
