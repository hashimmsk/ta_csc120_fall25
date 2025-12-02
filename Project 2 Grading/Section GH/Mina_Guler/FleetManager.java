import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Import and edit boat information, add expense information
 * @author Mina Guler
 * @version 1
 */

public class FleetManager {
    /**
     * Boat's name location in file line
     */
    private static final int NAME_LOCATION = 1;
    /**
     * Boat's year location in file line
     */
    private static final int YEAR_LOCATION = 2;
    /**
     * Boat's make location in file line
     */
    private static final int MAKE_LOCATION = 3;
    /**
     * Boat's length location in file line
     */
    private static final int LENGTH_LOCATION = 4;
    /**
     * Boat's price location in file line
     */
    private static final int PRICE_LOCATION = 5;
    /**
     * Database name is FleetData.db
     */
    private static final String DB_FILENAME = "FleetData.db";
    /**
     * List of boats
     */
    private ArrayList<Boat> fleet = new ArrayList<>();
    /**
     * Default constructor
     * @param args Passed in from the command line
     */
    public static void main(String[] args) {
        FleetManager manager = new FleetManager();
        manager.runFile(args);
    } // end of main method
    /**
     * Run file
     * @param args Passed in from main
     */
    private void runFile(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        if (args != null && args.length > 0) {
            String csvFile = args[0];
            System.out.println("Welcome to the Fleet Management System");
            System.out.println("--------------------------------------");
            System.out.println("Initializing from CSV: " + csvFile);
            loadFromCSV(csvFile);
            saveToDB();
        } else {
            System.out.println("Welcome to the Fleet Management System");
            System.out.println("--------------------------------------");
            if (!loadFromDB()) {
                System.out.println("No existing fleet database found; starting empty.");
            } // end of if statement
        } // end of if-else statement

        String choice;

        do { //menu options
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine();

            if (choice.isEmpty()) {
                System.out.println("Invalid menu option, try again");
                continue;
            } // end of if statement

            char option = Character.toUpperCase(choice.charAt(0));

            if (option == 'P') {
                printFleet();
            } else if (option == 'A') {
                System.out.print("Please enter the new boat CSV data          : ");
                String csvLine = keyboard.nextLine();
                addBoatFromCSVLine(csvLine);
            } else if (option == 'R') {
                System.out.print("Which boat do you want to remove?           : ");
                String name = keyboard.nextLine();
                removeBoatByName(name);
            } else if (option == 'E') {
                System.out.print("Which boat do you want to spend on?         : ");
                String name = keyboard.nextLine();
                Boat boat = findBoatByName(name);

                if (boat == null) {
                    System.out.println("Cannot find boat " + name);
                } else {
                    System.out.print("How much do you want to spend?              : ");
                    double amount = Double.parseDouble(keyboard.nextLine());

                    if (boat.spend(amount)) {
                        System.out.printf("Expense authorized, $%.2f spent.%n", amount);
                    } else {
                        double left = boat.getRemaining();
                        System.out.printf("Expense not permitted, only $%.2f left to spend.%n", left);
                    } // end of inner else statement
                } // end of outer else statement
            } else if (option == 'X') {
                // exit
            } else {
                System.out.println("Invalid menu option, try again");
            } // end of if-else statement

        } while (!choice.equalsIgnoreCase("x"));

        saveToDB();
        System.out.println("Exiting the Fleet Management System");
        keyboard.close();
    } // end of runFile method
    /**
     * Load data from initial csv file (FleetData)
     * @param filename file to load data from
     */
    private void loadFromCSV(String filename) {
        int index;
        Boat boat;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            fleet.clear();
            for (index = 0; index < lines.size(); index++) {
                String line = lines.get(index).trim();

                if (!line.isEmpty()) {
                    boat = parseBoatFromCSVLine(line);
                    fleet.add(boat);
                } // end of if statement
            } // end of for loop
        } catch (Exception e) {
            System.out.println("Problem reading CSV file.");
        } // end of exception catch if CSV file cannot be read
    } // end of loadFromCSV method
    /**
     * Save boat information to FleetData.db
     */
    private void saveToDB() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DB_FILENAME));
            out.writeObject(fleet);
            out.close();
        } catch (Exception e) {
            System.out.println("Problem saving database.");
        } // end of try-catch
    } // end of saveToDB method
    /**
     * Load information from database
     * @return whether information has been successfully loaded
     */
    @SuppressWarnings("unchecked")
    private boolean loadFromDB() {
        File file = new File(DB_FILENAME);
        if (!file.exists()) {
            return false;
        } // end of if statement

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            fleet = (ArrayList<Boat>) in.readObject();
            in.close();
            return true;
        } catch (Exception e) {
            System.out.println("Problem loading database.");
            return false;
        } // end of try-catch
    } // end of loadFromDB method
    /**
     * Print fleet information
     */
    private void printFleet() {
        System.out.println("Fleet report:");
        double totalPaid = 0;
        double totalSpent = 0;
        int index;

        for (index = 0; index < fleet.size(); index++) {
            Boat b = fleet.get(index);  // get the boat at position i
            System.out.println(b);
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        } // end of for loop

        System.out.printf("    Total%45s : Paid $%9.2f : Spent $%9.2f%n", "", totalPaid, totalSpent);

    } // end of printFleet method

    /**
     * Add boat information from CSV
     * @param line add boat information according to its line location
     */
    private void addBoatFromCSVLine(String line) {
        Boat boat = parseBoatFromCSVLine(line);
        fleet.add(boat);
    } //end of addBoatFromCSVLine method
    /**
     * Remove a boat by its name
     * @param name boat's name
     */
    private void removeBoatByName(String name) {
        Boat b = findBoatByName(name);
        if (b == null) {
            System.out.println("Cannot find boat " + name);
        } else {
            fleet.remove(b);
        } // end of if-else statement
    } // end of removeBoatByName method
    /**
     * Find a boat by its name
     * @param name boat's name
     * @return boat information
     */
    private Boat findBoatByName(String name) {
        String search = name.toLowerCase();
        int index;
        Boat boat;
        for (index = 0; index < fleet.size(); index++) {
            boat = fleet.get(index);  // get the boat at position i
            if (boat.getName().toLowerCase().equals(search)) {
                return boat;
            } // end of if statement
        } // end of for loop

        return null;
    } // end of findBoatByName method
    /**
     * Turn csv file lines into boat objects
     * @param line line from csv line
     * @return boat object
     */
    private Boat parseBoatFromCSVLine(String line) {
        String[] parts = line.split(",", -1);
        String boatType = parts[0].toUpperCase();
        Boat.Type type;

        if (boatType.equals("POWER")) {
            type = Boat.Type.POWER;
        } else {
            type = Boat.Type.SAILING;
        } // end of if statement

        String name = parts[NAME_LOCATION];
        int year = Integer.parseInt(parts[YEAR_LOCATION]);
        String makeModel = parts[MAKE_LOCATION];
        int length = Integer.parseInt(parts[LENGTH_LOCATION]);
        double price = Double.parseDouble(parts[PRICE_LOCATION]);

        return new Boat(type, name, year, makeModel, length, price);
    } // end of parseBoatFromCSVFile method
} // end of FleetManager class
