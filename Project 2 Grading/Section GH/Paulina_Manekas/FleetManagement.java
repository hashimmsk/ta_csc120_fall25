import java.util.*;
import java.io.*;

/**
 * Main class for the Fleet Management System
 * Handles menu operations, file I/O, and fleet management
 *
 * @author Your Name
 * @version 1.0
 */
public class FleetManagement {
    /** Database filename for serialized object storage */
    private static final String DB_FILENAME = "FleetData.db";
    /** Scanner for user input - only in main class as required */
    private static final Scanner keyboard = new Scanner(System.in);
    /** ArrayList to store fleet data - using ArrayList as hinted */
    private static ArrayList<Boat> fleet = new ArrayList<>();

    /**
     * Main method that controls program flow
     * @param args Command line arguments - CSV filename for initial run
     */
    public static void main(String[] args) {
        // Load data based on command line arguments as specified in requirements
        if (args.length > 0) {
            loadFromCSV(args[0]);
        } else {
            loadFromDB(DB_FILENAME);
        }

        displayWelcomeMessage();

        // Main menu loop
        boolean running = true;
        while (running) {
            displayMenu();
            String option = keyboard.nextLine().toUpperCase();

            switch (option) {
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
                    processExpense();
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
            }
        }

        // Save data and exit
        saveToDB(DB_FILENAME);
        displayExitMessage();
    }

    /**
     * Displays the welcome message to the user
     */
    private static void displayWelcomeMessage() {
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();
    }

    /**
     * Displays the main menu options to the user
     */
    private static void displayMenu() {
        System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
    }

    /**
     * Displays the exit message when program terminates
     */
    private static void displayExitMessage() {
        System.out.println();
        System.out.println("Exiting the Fleet Management System");
    }

    /**
     * Loads fleet data from CSV file - all file handling in main class as hinted
     * @param filename The CSV file to load data from
     */
    private static void loadFromCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Boat boat = parseCSVLine(line);
                if (boat != null) {
                    fleet.add(boat);
                }
            }
        } catch (IOException e) {
            // Silent fail as per requirements - start with empty fleet
        }
    }

    /**
     * Parses a single CSV line into a Boat object
     * @param line The CSV line to parse
     * @return Boat object if parsing successful, null otherwise
     */
    private static Boat parseCSVLine(String line) {
        try {
            String[] parts = line.split(",");
            // Using BoatType enum as hinted in requirements
            BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
            String name = parts[1].trim();
            int year = Integer.parseInt(parts[2].trim());
            String makeModel = parts[3].trim();
            int length = Integer.parseInt(parts[4].trim());
            double purchasePrice = Double.parseDouble(parts[5].trim());

            return new Boat(type, name, year, makeModel, length, purchasePrice);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Loads fleet data from serialized database file
     * @param filename The database file to load from
     */
    @SuppressWarnings("unchecked")
    private static void loadFromDB(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            fleet = (ArrayList<Boat>) ois.readObject();
        } catch (Exception e) {
            // Silent fail as per requirements - start with empty fleet
        }
    }

    /**
     * Saves fleet data to serialized database file
     * @param filename The database file to save to
     */
    private static void saveToDB(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(fleet);
        } catch (IOException e) {
            // Silent fail as per requirements
        }
    }

    /**
     * Prints the entire fleet inventory with totals
     */
    private static void printFleet() {
        System.out.println();
        System.out.println("Fleet report:");
        if (fleet.isEmpty()) {
            System.out.println("    No boats in fleet");
            return;
        }

        double totalPaid = 0;
        double totalSpent = 0;

        for (Boat boat : fleet) {
            System.out.println(boat);
            totalPaid += boat.getPurchasePrice();
            totalSpent += boat.getExpenses();
        }

        System.out.printf("    Total                                             : Paid $ %8.2f : Spent $ %8.2f\n",
                totalPaid, totalSpent);
        System.out.println();
    }

    /**
     * Adds a new boat to the fleet using CSV input
     */
    private static void addBoat() {
        System.out.print("Please enter the new boat CSV data          : ");
        String csvData = keyboard.nextLine();

        Boat newBoat = parseCSVLine(csvData);
        if (newBoat != null) {
            fleet.add(newBoat);
            // No success message - exactly as in example
        } else {
            // Should not happen according to spec - data is reasonable
        }
    }

    /**
     * Removes a boat from the fleet by name (case-insensitive)
     */
    private static void removeBoat() {
        System.out.print("Which boat do you want to remove?           : ");
        String boatName = keyboard.nextLine();

        Boat boat = findBoatByName(boatName);
        if (boat != null) {
            fleet.remove(boat);
            // No success message - exactly as in example
        } else {
            System.out.println("Cannot find boat " + boatName);
        }
    }

    /**
     * Processes an expense request for a boat, enforcing the Commodore's policy
     */
    private static void processExpense() {
        System.out.print("Which boat do you want to spend on?         : ");
        String boatName = keyboard.nextLine();

        Boat boat = findBoatByName(boatName);
        if (boat == null) {
            System.out.println("Cannot find boat " + boatName);
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        try {
            double amount = Double.parseDouble(keyboard.nextLine());

            if (boat.addExpense(amount)) {
                System.out.printf("Expense authorized, $%.2f spent.\n", amount);
            } else {
                System.out.printf("Expense not permitted, only $%.2f left to spend.\n",
                        boat.getRemainingSpend());
            }
            System.out.println();
        } catch (NumberFormatException e) {
            // Should not happen according to spec - numeric input is correct
        }
    }

    /**
     * Finds a boat by name (case-insensitive as required)
     * @param name The boat name to search for
     * @return The boat object if found, null otherwise
     */
    private static Boat findBoatByName(String name) {
        for (Boat boat : fleet) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }
        return null;
    }
}