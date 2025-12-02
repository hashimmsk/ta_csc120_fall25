import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

/**
 * Fleet Management System for the Coconut Grove Sailing Club.
 * Manages a fleet of boats, tracks expenses, and enforces spending policies.
 * Data can be loaded from CSV files initially and persists in a database file.
 *
 * @author Mohammad Ayaan Parvez
 */
public class FleetManager {
    /** The filename for the serialized database */
    private static final String DB_FILE = "FleetData.db";
    /** The collection of boats in the fleet */
    private ArrayList<Boat> fleet;

    /**
     * Constructs a new FleetManager with an empty fleet.
     */
    public FleetManager() {
        fleet = new ArrayList<>();
    }

    /**
     * Main entry point for the Fleet Management System.
     * Loads data from CSV (if command line argument provided) or database file,
     * runs the interactive menu, and saves data on exit.
     *
     * @param args command line arguments - optional CSV filename for initial run
     */
    public static void main(String[] args) {
        FleetManager manager = new FleetManager();

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        // Load data
        if (args.length > 0) {
            // Initial run - load from CSV
            manager.loadFromCSV(args[0]);
        } else {
            // Subsequent run - load from DB
            manager.loadFromDB();
        }

        // Run menu
        manager.runMenu();

        // Save data on exit
        manager.saveToDB();
        System.out.println("\nExiting the Fleet Management System");
    }

    /**
     * Loads boat data from a CSV file.
     * CSV format: TYPE,NAME,YEAR,MAKEMODEL,LENGTH,PRICE (no expenses).
     *
     * @param filename the name of the CSV file to load
     */
    private void loadFromCSV(String filename) {
        try {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                fleet.add(Boat.parseCSV(line));
            }

            fileScanner.close();
        } catch (Exception e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    /**
     * Loads boat data from the database file.
     * Database format: TYPE,NAME,YEAR,MAKEMODEL,LENGTH,PRICE,EXPENSES.
     */
    private void loadFromDB() {
        try {
            File file = new File(DB_FILE);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                fleet.add(Boat.fromCSV(line));
            }

            fileScanner.close();
        } catch (Exception e) {
            System.err.println("Error reading database file: " + e.getMessage());
        }
    }

    /**
     * Saves all boat data to the database file in CSV format.
     * Includes current expense data for each boat.
     */
    private void saveToDB() {
        try {
            PrintWriter writer = new PrintWriter(DB_FILE);

            for (Boat boat : fleet) {
                writer.println(boat.toCSV());
            }

            writer.close();
        } catch (Exception e) {
            System.err.println("Error writing database file: " + e.getMessage());
        }
    }

    /**
     * Runs the interactive menu loop.
     * Offers options to print, add, remove boats, record expenses, or exit.
     */
    private void runMenu() {
        Scanner keyboard = new Scanner(System.in);
        String choice;

        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine().trim().toUpperCase();

            switch (choice) {
                case "P":
                    printFleet();
                    break;
                case "A":
                    addBoat(keyboard);
                    break;
                case "R":
                    removeBoat(keyboard);
                    break;
                case "E":
                    addExpense(keyboard);
                    break;
                case "X":
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
            }

        } while (!choice.equals("X"));
    }

    /**
     * Prints a formatted report of all boats in the fleet.
     * Shows boat details, purchase prices, expenses, and totals.
     */
    private void printFleet() {
        System.out.println("\nFleet report:");
        double totalPaid = 0;
        double totalSpent = 0;

        for (Boat boat : fleet) {
            System.out.println(boat);
            totalPaid += boat.getPurchasePrice();
            totalSpent += boat.getExpenses();
        }

        System.out.printf("    %-51s : Paid $ %8.2f : Spent $ %8.2f\n\n",
                "Total", totalPaid, totalSpent);
    }

    /**
     * Prompts the user for boat data and adds a new boat to the fleet.
     *
     * @param keyboard the Scanner object for reading user input
     */
    private void addBoat(Scanner keyboard) {
        System.out.print("Please enter the new boat CSV data          : ");
        String csvData = keyboard.nextLine();
        fleet.add(Boat.parseCSV(csvData));
        System.out.println();
    }

    /**
     * Prompts the user for a boat name and removes it from the fleet.
     * Boat name matching is case-insensitive.
     *
     * @param keyboard the Scanner object for reading user input
     */
    private void removeBoat(Scanner keyboard) {
        System.out.print("Which boat do you want to remove?           : ");
        String boatName = keyboard.nextLine();

        Boat toRemove = findBoat(boatName);
        if (toRemove != null) {
            fleet.remove(toRemove);
        } else {
            System.out.println("Cannot find boat " + boatName);
        }
        System.out.println();
    }

    /**
     * Prompts the user for a boat and expense amount, then attempts to record the expense.
     * Enforces the Commodore's policy that expenses cannot exceed purchase price.
     * Boat name matching is case-insensitive.
     *
     * @param keyboard the Scanner object for reading user input
     */
    private void addExpense(Scanner keyboard) {
        System.out.print("Which boat do you want to spend on?         : ");
        String boatName = keyboard.nextLine();

        Boat boat = findBoat(boatName);
        if (boat == null) {
            System.out.println("Cannot find boat " + boatName);
        } else {
            System.out.print("How much do you want to spend?              : ");
            double amount = Double.parseDouble(keyboard.nextLine());

            if (boat.addExpense(amount)) {
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
     * Searches for a boat by name (case-insensitive).
     *
     * @param name the name of the boat to find
     * @return the Boat object if found, null otherwise
     */
    private Boat findBoat(String name) {
        for (Boat boat : fleet) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }
        return null;
    }
}