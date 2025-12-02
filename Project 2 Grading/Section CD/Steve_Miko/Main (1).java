import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Main class for the Fleet Management System.
 * This program manages a collection of Boat objects, allowing the user to add, remove,
 * view, and manage expenses for boats. Data is persisted via a serialized database file
 * or imported via CSV.
 *
 * @author Steve Mikolajewski
 * @version 1.0
 */
public class Main {

    /**
     * Enumeration representing the types of boats supported by the system.
     */
    public enum boatType{SAILING, POWER}

    // Standard input scanner
    private static final Scanner keyboard = new Scanner(System.in);
    // Constant for the database filename
    private static final String DATABASE_FILENAME = "FleetData.db";
    // List to hold the boat objects
    private static ArrayList<Boat> fleet;

    /**
     * The main entry point for the application.
     * Initializes the system, loads data, and handles the main menu loop.
     *
     * @param args Command line arguments. If provided, the first argument is used as a CSV filename to load data.
     */
    public static void main(String[] args) {
        boolean keepRunning;
        String choice;

        // Initialize static variables
        fleet = new ArrayList<Boat>();
        // Load Data
        if (args.length > 0) {
            // If command line arg exists, load from CSV
            System.out.println("--- Initializing from CSV file ---");
            loadFleetFromCsv(args[0]);
        } else {
            // Otherwise, load from serialized .db file
            loadFleetFromDb();
        }

        // Welcome Message
        System.out.println("\nWelcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        // Main Menu Loop
        keepRunning = true;
        while (keepRunning) {
            printMenu();
            choice = keyboard.nextLine().toUpperCase();

            switch (choice) {
                case "P":
                    printFleet();
                    break;
                case "A":
                    addBoat();
                    break;
                case "R":
                    removeBoat();
                    break;
                case "E":
                    addExpense();
                    break;
                case "X":
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
                    break;
            } // end switch
        } // end while

        // Save Data and Exit
        System.out.println("\nExiting the Fleet Management System");
        saveFleetToDb();
    } // end of main method

    /**
     * Displays the main menu options to the console.
     */
    private static void printMenu() {
        System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
    } // end of printMenu method

    /**
     * Prints a formatted report of all boats currently in the fleet.
     * Calculates and displays total purchase prices and total expenses incurred.
     */
    private static void printFleet() {
        double totalPaid = 0.0;
        double totalSpent = 0.0;
        Boat currentBoat;
        int index;


        System.out.println("\nFleet report:");

        // Iterate through the fleet to print details and accumulate totals
        for (index = 0; index < fleet.size(); index++) {
            currentBoat = fleet.get(index);
            System.out.println(currentBoat);
            totalPaid += currentBoat.getPurchasePrice();
            totalSpent += currentBoat.getExpenses();
        }
        System.out.println(String.format("    Total                                         : Paid $ %9.2f : Spent $ %9.2f",
                totalPaid, totalSpent));
    } // end of printFleet method

    /**
     * Prompts the user to enter a CSV string defining a new boat and adds it to the fleet.
     * Catches exceptions related to parsing invalid data.
     */
    private static void addBoat() {
        // Declare all local variables at the top
        String csvLine;
        Boat newBoat;

        System.out.print("Please enter the new boat CSV data       : ");
        csvLine = keyboard.nextLine();

        try {
            newBoat = parseBoatFromCsvLine(csvLine);
            fleet.add(newBoat);
        } catch (Exception e) {
            // Catches NumberFormatException, IllegalArgumentException, etc.
            System.out.println("Error adding boat. Invalid data format: " + e.getMessage());
        }
    } // end of addBoat method

    /**
     * Prompts the user for a boat name and removes that boat from the fleet if found.
     */
    private static void removeBoat() {
        // Declare all local variables at the top
        String name;
        Boat boatToRemove;

        System.out.print("Which boat do you want to remove?        : ");
        name = keyboard.nextLine();

        // Attempt to find the boat
        boatToRemove = findBoatByName(name);

        if (boatToRemove != null) {
            fleet.remove(boatToRemove);
        } else {
            System.out.println("Cannot find boat " + name);
        }
    } // end of removeBoat method

    /**
     * Prompts the user to authorize an expense for a specific boat.
     * Checks if the boat exists and if the expense is within the allowable budget.
     */
    private static void addExpense() {
        // Declare all local variables at the top
        String name;
        Boat boat;
        double amount;

        System.out.print("Which boat do you want to spend on?    : ");
        name = keyboard.nextLine();

        boat = findBoatByName(name);

        if (boat == null) {
            System.out.println("Cannot find boat " + name);
            return; // Exit method
        }

        System.out.print("How much do you want to spend?         : ");
        try {
            amount = Double.parseDouble(keyboard.nextLine());

            if (amount < 0) {
                System.out.println("Expense amount cannot be negative.");
                return;
            }

            // Check the Commodore's policy
            if (boat.canSpend(amount)) {
                boat.addExpense(amount);
                // Report the *new total* expenses for this boat
                System.out.printf("Expense authorized, $%.2f spent.\n", boat.getExpenses());
            } else {
                // Report how much is left to spend
                System.out.printf("Expense not permitted, only $%.2f left to spend.\n", boat.getRemainingBudget());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        }
    } // end of addExpense method

    /**
     * Helper method to search for a boat in the fleet list by name.
     * The search is case-insensitive.
     *
     * @param name The name of the boat to find.
     * @return The Boat object if found, or null if not found.
     */
    private static Boat findBoatByName(String name) {
        // Declare all local variables at the top
        int index;
        Boat boat;

        // Use a classic for-loop per CSC120 standards
        for (index = 0; index < fleet.size(); index++) {
            boat = fleet.get(index);
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }
        return null; // Not found
    } // end of findBoatByName method

    /**
     * Parses a single CSV line string into a Boat object.
     * Expected format: Type, Name, Year, Model, Length, Price.
     *
     * @param csvLine The comma-separated string containing boat data.
     * @return A new Boat object populated with the parsed data.
     * @throws Exception If the string format is invalid or missing fields.
     */
    private static Boat parseBoatFromCsvLine(String csvLine) throws Exception {
        // Declare all local variables at the top
        String[] parts;
        boatType type;
        String name;
        int year;
        String makeModel;
        int length;
        double price;

        parts = csvLine.split(",");

        if (parts.length < 6) {
            throw new IllegalArgumentException("Not enough data fields. Expected 6.");
        }

        // Trim whitespace just in case
        type = boatType.valueOf(parts[0].trim().toUpperCase());
        name = parts[1].trim();
        year = Integer.parseInt(parts[2].trim());
        makeModel = parts[3].trim();
        length = Integer.parseInt(parts[4].trim());
        price = Double.parseDouble(parts[5].trim());

        return new Boat(type, name, year, makeModel, length, price);
    } // end of parseBoatFromCsvLine method

    /**
     * Loads the fleet data from a serialized database file.
     * If the file does not exist or is corrupted, initializes an empty fleet.
     */
    private static void loadFleetFromDb() {
        File databaseFile;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        databaseFile = new File(DATABASE_FILENAME);

        if (!databaseFile.exists()) {
            System.out.println("No " + DATABASE_FILENAME + " found. Starting with an empty fleet.");
            return; // Exit method, fleet is already new/empty
        }

        System.out.println("Loading fleet from " + DATABASE_FILENAME + "...");

        try {
            fileInputStream = new FileInputStream(DATABASE_FILENAME);
            objectInputStream = new ObjectInputStream(fileInputStream);

            // Read the entire ArrayList object in one go
            fleet = (ArrayList<Boat>) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            // This case is handled by dbFile.exists(), but good to have
            System.out.println("No " + DATABASE_FILENAME + " found. Starting with an empty fleet.");
        } catch (IOException e) {
            System.out.println("Error reading " + DATABASE_FILENAME + ". File might be corrupt. Starting fresh.");
            fleet = new ArrayList<Boat>(); // Reset fleet
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + DATABASE_FILENAME + " contains invalid data. Starting fresh.");
            fleet = new ArrayList<Boat>(); // Reset fleet
        } finally {
            // Close streams in reverse order of creation
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing database file stream: " + e.getMessage());
            }
        }
    } // end of loadFleetFromDb method

    /**
     * Loads fleet data from a CSV text file.
     * Intended for initial setup or importing data via command line arguments.
     *
     * @param filename The path to the CSV file to read.
     */
    private static void loadFleetFromCsv(String filename) {
        // Declare all local variables at the top
        Scanner fileScanner = null;
        String line;
        Boat boat;

        System.out.println("Loading fleet from " + filename + "...");
        try {
            fileScanner = new Scanner(new File(filename));
            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                try {
                    boat = parseBoatFromCsvLine(line);
                    fleet.add(boat);
                } catch (Exception e) {
                    System.out.println("Skipping invalid CSV line: " + line + " - " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Initial CSV file not found: " + filename);
            System.out.println("Starting with an empty fleet.");
        } finally {
            // Always close the scanner
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
    } // end of loadFleetFromCsv method

    /**
     * Serializes the current fleet ArrayList and saves it to the database file.
     * Called before the program exits.
     */
    private static void saveFleetToDb() {
        // Declare all local variables at the top
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        System.out.println("Saving fleet to " + DATABASE_FILENAME + "...");
        try {
            fileOutputStream = new FileOutputStream(DATABASE_FILENAME);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write the entire ArrayList object
            objectOutputStream.writeObject(fleet);

        } catch (IOException e) {
            System.out.println("Error saving fleet data: " + e.getMessage());
        } finally {
            // Close streams in reverse order
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing database file stream on save: " + e.getMessage());
            }
        }
    } // end of saveFleetToDb method

} // end of Main class