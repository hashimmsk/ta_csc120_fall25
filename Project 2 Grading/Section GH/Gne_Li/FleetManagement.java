import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Fleet Management System for Coconut Grove Sailing Club.
 * Manages a fleet of boats including tracking expenses and enforcing
 * spending policies. Supports loading from CSV and persisting to
 * serialized database files.
 *
 * @author Gen Li
 * @version 1.0
 */
public class FleetManagement {

    // Constants
    private static final String DB_FILE = "FleetData.db";

    // Instance variables
    private ArrayList<Boat> fleet;
    private final Scanner keyboard;

    /**
     * Constructs a new FleetManagement system.
     * Initializes the fleet collection and keyboard scanner.
     */
    public FleetManagement() {
        fleet = new ArrayList<Boat>();
        keyboard = new Scanner(System.in);
    }

    /**
     * Main entry point for the Fleet Management System.
     * Handles command line arguments and launches the application.
     *
     * @param args command line arguments (optional CSV filename for initial load)
     */
    public static void main(String[] args) {
        FleetManagement manager;

        manager = new FleetManagement();

        // Load data from CSV if argument provided, otherwise from DB
        if (args.length > 0) {
            manager.loadFromCSV(args[0]);
        } else {
            manager.loadFromDB();
        }

        // Run the main menu loop
        manager.run();

        // Save data and exit
        manager.saveToDB();
        System.out.println("\nExiting the Fleet Management System");
    }

    /**
     * Main application loop. Displays welcome message and processes
     * menu options until user chooses to exit.
     */
    private void run() {
        char choice;

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        do {
            choice = getMenuChoice();

            switch (choice) {
                case 'P':
                    printFleet();
                    break;
                case 'A':
                    addBoat();
                    break;
                case 'R':
                    removeBoat();
                    break;
                case 'E':
                    processExpense();
                    break;
                case 'X':
                    // Exit - handled by loop condition
                    break;
            }
        } while (choice != 'X');
    }

    /**
     * Displays the menu and gets a valid menu choice from the user.
     * Repeats until a valid option is entered.
     *
     * @return the validated menu choice (P, A, R, E, or X)
     */
    private char getMenuChoice() {
        String input;
        char choice;
        boolean valid;

        valid = false;
        choice = ' ';

        while (!valid) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            input = keyboard.nextLine().trim().toUpperCase();

            if (input.length() == 1) {
                choice = input.charAt(0);
                if (choice == 'P' || choice == 'A' || choice == 'R' ||
                        choice == 'E' || choice == 'X') {
                    valid = true;
                }
            }

            if (!valid) {
                System.out.println("Invalid menu option, try again");
            }
        }

        return choice;
    }

    /**
     * Displays the fleet report showing all boats and totals.
     * Format includes type, name, year, make, length, purchase price,
     * and expenses for each boat, plus summary totals.
     */
    private void printFleet() {
        double totalPaid;
        double totalSpent;

        System.out.println("\nFleet report:");

        for (Boat boat : fleet) {
            System.out.println(boat);
        }

        totalPaid = calculateTotalPurchases();
        totalSpent = calculateTotalExpenses();

        System.out.printf("    %-7s %-20s %4s %-12s %3s : Paid $ %9.2f : Spent $ %9.2f\n",
                "Total", "", "", "", "", totalPaid, totalSpent);
        System.out.println();
    }

    /**
     * Prompts user for boat data and adds a new boat to the fleet.
     * Boat data should be provided as a CSV formatted string.
     */
    private void addBoat() {
        String csvData;
        Boat newBoat;

        System.out.print("Please enter the new boat CSV data          : ");
        csvData = keyboard.nextLine().trim();

        newBoat = new Boat(csvData);
        fleet.add(newBoat);
        System.out.println();
    }

    /**
     * Prompts user for a boat name and removes it from the fleet.
     * Search is case-insensitive. Displays error if boat not found.
     */
    private void removeBoat() {
        String boatName;
        Boat boat;

        System.out.print("Which boat do you want to remove?           : ");
        boatName = keyboard.nextLine().trim();

        boat = findBoat(boatName);

        if (boat == null) {
            System.out.println("Cannot find boat " + boatName);
        } else {
            fleet.remove(boat);
        }

        System.out.println();
    }

    /**
     * Processes an expense request for a boat.
     * Prompts for boat name and amount, then checks against spending policy.
     * Updates expenses if approved, otherwise displays remaining budget.
     */
    private void processExpense() {
        String boatName;
        String amountStr;
        double amount;
        Boat boat;

        System.out.print("Which boat do you want to spend on?         : ");
        boatName = keyboard.nextLine().trim();

        boat = findBoat(boatName);

        if (boat == null) {
            System.out.println("Cannot find boat " + boatName);
        } else {
            System.out.print("How much do you want to spend?              : ");
            amountStr = keyboard.nextLine().trim();
            amount = Double.parseDouble(amountStr);

            if (boat.canSpend(amount)) {
                boat.addExpense(amount);
                System.out.printf("Expense authorized, $%.2f spent.\n",
                        boat.getExpenses());
            } else {
                System.out.printf("Expense not permitted, only $%.2f left to spend.\n",
                        boat.getRemainingBudget());
            }
        }

        System.out.println();
    }

    /**
     * Finds a boat in the fleet by name (case-insensitive search).
     *
     * @param name the name of the boat to find
     * @return the Boat object if found, null otherwise
     */
    private Boat findBoat(String name) {
        Boat foundBoat;

        foundBoat = null;

        for (Boat boat : fleet) {
            if (boat.getName().equalsIgnoreCase(name)) {
                foundBoat = boat;
                break;
            }
        }

        return foundBoat;
    }

    /**
     * Calculates the total of all boat purchase prices.
     *
     * @return the sum of all purchase prices
     */
    private double calculateTotalPurchases() {
        double total;

        total = 0.0;

        for (Boat boat : fleet) {
            total += boat.getPurchasePrice();
        }

        return total;
    }

    /**
     * Calculates the total of all boat expenses.
     *
     * @return the sum of all expenses
     */
    private double calculateTotalExpenses() {
        double total;

        total = 0.0;

        for (Boat boat : fleet) {
            total += boat.getExpenses();
        }

        return total;
    }

    /**
     * Loads fleet data from a CSV file.
     * Each line represents one boat in CSV format.
     *
     * @param filename the name of the CSV file to load
     */
    private void loadFromCSV(String filename) {
        BufferedReader reader;
        String line;
        Boat boat;

        reader = null;

        try {
            reader = new BufferedReader(new FileReader(filename));

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    boat = new Boat(line);
                    fleet.add(boat);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: CSV file not found - " + filename);
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error closing CSV file: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Loads fleet data from the serialized database file.
     * If file doesn't exist, starts with an empty fleet.
     */
    private void loadFromDB() {
        ObjectInputStream input;
        File dbFile;

        input = null;
        dbFile = new File(DB_FILE);

        // Only try to load if file exists
        if (!dbFile.exists()) {
            return;
        }

        try {
            input = new ObjectInputStream(new FileInputStream(DB_FILE));
            fleet = (ArrayList<Boat>) input.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist - start with empty fleet
            System.err.println("Database file not found, starting with empty fleet");
        } catch (IOException e) {
            System.err.println("Error reading database file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading boat data: " + e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("Error closing database file: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Saves fleet data to the serialized database file.
     * Overwrites any existing file.
     */
    private void saveToDB() {
        ObjectOutputStream output;

        output = null;

        try {
            output = new ObjectOutputStream(new FileOutputStream(DB_FILE));
            output.writeObject(fleet);
        } catch (IOException e) {
            System.err.println("Error saving database file: " + e.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.err.println("Error closing database file: " + e.getMessage());
                }
            }
        }
    }
}