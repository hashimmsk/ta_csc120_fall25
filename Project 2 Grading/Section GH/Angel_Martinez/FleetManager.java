package angelmartinez.fleetmangement;

import java.io.*;
import java.util.*;

/**
 * An enum that differentiates between Sailing and Power boats.
 */
enum BoatType {
    SAILING,
    POWER
}
/**
 * Records information about a fleet of boats, including  purchase price and expenses maintaining.
 * Can add and remove boats, which are saved to a serialized file.
 * @author Angel Martinez
 */
public class FleetManager {

    /** Name of the database file where fleet is saved */
    private static final String DB_FILENAME = "FleetData.db";

    /** Scanner object for reading user input from the console */
    private static final  Scanner keyboard = new Scanner(System.in);

    /** The Array list of all boats in the fleet */
    public static ArrayList<Boat> fleet = new ArrayList<>();


    /**
     * Main method of FleetManager Class.
     * Loads fleet data from CSV file or database.
     * Starts menu loop, then saves fleet data on exit.
     *
     * @param args Optional first argument is CSV filename to load fleet data
     */
    public static void main(String[] args) {

        String csvFilename;
        int argCount;

        argCount = args.length;



        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        if (argCount >= 1) {
            csvFilename = args[0];
            try {
                loadFromCsv(csvFilename);
            } catch (IOException e) {
                System.out.println("Error reading CSV file: " + e.getMessage());
                System.out.println("Starting with empty fleet.");
            }
        } else {
            try {
                loadFromDb();
            } catch (IOException e) {
                System.out.println("No saved fleet database found, starting with empty fleet.");
                fleet = new ArrayList<>();
            } catch (ClassNotFoundException e) {
                System.out.println("Saved data is invalid, starting with empty fleet.");
                fleet = new ArrayList<>();
            }
        }

        menuLoop();

        try {
            saveToDb();
        } catch (IOException e) {
            System.out.println("Failed to save fleet data: " + e.getMessage());
        }

        System.out.println("Exiting the Fleet Management System");
    }

    /**
     * Loads fleet data from a CSV file.
     * Each valid line in the file is converted into a Boat and added to fleet.
     *
     * @param csvFilename the path to the CSV file
     * @throws IOException if reading the file fails
     */
    public static void loadFromCsv(String csvFilename) throws IOException {
        BufferedReader reader = null;
        int lineNumber = 0;

        try {
            reader = new BufferedReader(new FileReader(csvFilename));
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    addBoatFromCsvLine(line);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Error with reading. " +
                            "Skipping invalid CSV line " + lineNumber + ": " + ex.getMessage());
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error somewhere. Closing CSV file: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Saves the current fleet data to a serialized file.
     *
     * @throws IOException if writing to the file fails
     */
    public static void saveToDb() throws IOException {
        ObjectOutputStream toStream = null;

        try {
            toStream = new ObjectOutputStream(new FileOutputStream(DB_FILENAME));
            toStream.writeObject(fleet);
        } finally {
            if (toStream != null) {
                try {
                    toStream.close();
                } catch (IOException e) {
                    System.out.println("ERROR closing output stream: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Loads fleet data from a serialized file.
     *
     * @throws IOException if reading the file fails
     * @throws ClassNotFoundException if the file contains invalid data
     */

    public static void loadFromDb() throws IOException, ClassNotFoundException {
        File dbFile = new File(DB_FILENAME);
        ObjectInputStream fromStream = null;

        try {
            fromStream = new ObjectInputStream(new FileInputStream(dbFile));
            fleet = (ArrayList<Boat>) fromStream.readObject();
        } finally {
            if (fromStream != null) {
                try {
                    fromStream.close();
                } catch (IOException e) {
                    System.out.println("ERROR closing input stream: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Displays the menu and user choices until exit.
     */
    public static void menuLoop() {
        String userInput;
        char sentinel = 'n';

        while (sentinel != 'y') {


            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");

             userInput = keyboard.nextLine();

            if (userInput.isEmpty()) {
                System.out.print("Invalid menu option, try again");
                continue;
            }

            char choice = userInput.charAt(0);

            if (choice == 'P' || choice == 'p') {

                printInventory();

            } else if (choice == 'A' || choice == 'a') {

                addBoat();

            } else if (choice == 'R' || choice == 'r') {

                removeBoat();

            } else if (choice == 'E' || choice == 'e') {

                boatExpense();

            } else if (choice == 'X' || choice == 'x') {
                sentinel = 'y';

            } else {

                System.out.println("Invalid menu option, try again");
            }

            System.out.println();

        }
    }

    /**
     * Prompts the user to add a new boat via a string input and adds it to fleet.
     */
    public static void addBoat() {
        System.out.print("Please enter the new boat CSV data: ");
        String userInput = keyboard.nextLine();

        // Split the input into a array
        String[] inputs = userInput.split(",");

        BoatType type = BoatType.valueOf(inputs[0].toUpperCase());
        String name = inputs[1];
        int year = Integer.parseInt(inputs[2]);
        String make = inputs[3];
        int length = Integer.parseInt(inputs[4]);
        double price = Double.parseDouble(inputs[5]);

        // Create and add the new Boat
        Boat newBoat = new Boat(type, name, year, make, length, price);
        fleet.add(newBoat);

    }

    /**
     * Prompts the user to remove a boat by name.
     */
    public static void removeBoat() {
        System.out.print("Which boat do you want to remove?           : ");
        String nameInput = keyboard.nextLine();

        Boat boatToRemove = findBoatByName(nameInput);

        if (boatToRemove != null) {
            fleet.remove(boatToRemove);
        } else {
            System.out.println("Cannot find boat " + nameInput);
        }
    }

    /**
     * Prompts the user to add an expense to a specific boat.
     */
    private static void boatExpense() {
        String nameInput;
        String lowerName;
        Boat selectedBoat;
        double amount;
        double left;

        System.out.print("Which boat do you want to spend on?         : ");
        nameInput = keyboard.nextLine();
        lowerName = nameInput.toLowerCase();
        selectedBoat = findBoatByName(lowerName);
        if (selectedBoat == null) {
            System.out.println("Cannot find boat " + nameInput);
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        amount = keyboard.nextDouble();
        keyboard.nextLine();

        if (selectedBoat.canSpend(amount)) {
            selectedBoat.addExpense(amount);
            System.out.printf("Expense authorized, $%.2f spent.\n", amount);
        } else {

            left = selectedBoat.getRemainingBudget();
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n", left);
        }
    }

    /**
     * Finds a boat in the fleet by its name (case-insensitive).
     *
     * @param name the name of the boat
     * @return the Boat object if found, otherwise null
     */
    public static Boat findBoatByName(String name) {
        String lowerName;
        int index;
        Boat currentBoat;

        lowerName = name.toLowerCase();
        for (index = 0; index < fleet.size(); index++) {
            currentBoat = fleet.get(index);
            if (currentBoat.getName().toLowerCase().equals(lowerName)) {
                return currentBoat;
            }
        }
        return null;
    }

    /**
     * Prints a formatted report of all boats in the fleet with totals.
     */
    public static void printInventory() {
        int index;
        double totalPaid = 0;
        double totalSpent = 0;
        System.out.println();
        System.out.println("Fleet report:");



        for ( index = 0; index < fleet.size(); index++) {
            Boat selectedBoat = fleet.get(index);
            System.out.println(selectedBoat.toString());
        totalPaid += selectedBoat.getPurchasePrice();
            totalSpent += selectedBoat.getExpenses();
        }

        System.out.printf("Total                                             : Paid $%9.2f : Spent $%9.2f%n",
                totalPaid, totalSpent
        );
        System.out.println();
    }

    /**
     * Adds a new Boat object to fleet from a line string.
     *
     * @param csvLine CSV-formatted string: type,name,year,make,length,purchasePrice
     */
    public static void addBoatFromCsvLine(String csvLine) {

        String[] data = csvLine.split(",", -1);

        BoatType type       = BoatType.valueOf(data[0].trim().toUpperCase());
        String name         = data[1].trim();
        int year            = Integer.parseInt(data[2].trim());
        String makeModel    = data[3].trim();
        int lengthFeet      = Integer.parseInt(data[4].trim());
        double purchasePrice= Double.parseDouble(data[5].trim());

        // Expenses default to 0
        Boat newBoat = new Boat(type, name, year, makeModel, lengthFeet, purchasePrice, 0.0);

        fleet.add(newBoat);
    }


}



