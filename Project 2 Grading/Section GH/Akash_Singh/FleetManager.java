import java.io.*;
import java.util.Scanner;

/**
 * Main class containing the program entry point.
 * Handles user interaction, menu options, and file input/output.
 *
 * @see FleetOfBoats
 * @see SingleBoat
 * @see BoatType
 * @author Aakash Singh
 * @version 1.0
 */

public class FleetManager {

    // ----------------------------------------------------------------------

    private static final Scanner keyboard = new Scanner(System.in);

    // ----------------------------------------------------------------------

    /**
     * Entry point for the Fleet Management System.
     *
     * @param args Optional CSV file path for initial run
     */

    public static void main(String[] args) {

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        FleetOfBoats fleet = new FleetOfBoats();

        // Load fleet data
        if (args.length > 0) {
            loadCSVData(fleet, args[0]);
        } else {
            loadSerializedData(fleet);
        }

        // Menu loop
        char option;
        do {
            option = getMenuOption();

            if (option == 'P') {
                printFleetReport(fleet);
            } else if (option == 'A') {
                addBoatMenu(fleet);
            } else if (option == 'R') {
                removeBoatMenu(fleet);
            } else if (option == 'E') {
                spendOnBoatMenu(fleet);
            }

        } while (option != 'X');

        // Save fleet data
        saveSerializedData(fleet);

        System.out.println("\nExiting the Fleet Management System");
    } //End of main method

    // ----------------------------------------------------------------------

    /**
     * Displays the menu options to the user and validates the choice.
     *
     * @return The validated menu choice (P, A, R, E, X)
     */

    private static char getMenuOption() {

        System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
        String input = keyboard.nextLine().trim().toUpperCase();

        while (input.isEmpty() || "PAREX".indexOf(input.charAt(0)) == -1) {
            System.out.println("Invalid menu option, try again");
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            input = keyboard.nextLine().trim().toUpperCase();
        }

        return input.charAt(0);
    } //End of getMenuOption method

    // ----------------------------------------------------------------------

    /**
     * Handles the "Add Boat" menu option.
     * Prompts the user for CSV data and adds a new boat to the fleet.
     *
     * @param fleet The fleet to which the new boat will be added
     */

    private static void addBoatMenu(FleetOfBoats fleet) {

        System.out.print("Please enter the new boat CSV data          : ");
        String line = keyboard.nextLine();
        String[] parts = line.split(",");

        try {
            BoatType type = BoatType.valueOf(parts[0].toUpperCase());
            String name = parts[1];
            int year = Integer.parseInt(parts[2]);
            String makeModel = parts[3];
            double length = Double.parseDouble(parts[4]);
            double price = Double.parseDouble(parts[5]);

            SingleBoat boat = new SingleBoat(type, name, year, makeModel, length, price);
            fleet.addBoat(boat);

        } catch (Exception e) {
            System.out.println("Invalid CSV input. Boat not added.");
        }
    } //End of addBoatMenu method

    // ----------------------------------------------------------------------

    /**
     * Handles the "Remove Boat" menu option.
     * Prompts the user for a boat name and removes it from the fleet.
     *
     * @param fleet The fleet from which a boat will be removed
     */

    private static void removeBoatMenu(FleetOfBoats fleet) {

        System.out.print("Which boat do you want to remove?           : ");
        String name = keyboard.nextLine();

        // Only print a message if the boat was not found
        if (!fleet.removeBoat(name)) {
            System.out.println("Cannot find boat " + name);
        }

    } //End of removeBoatMenu method

    // ----------------------------------------------------------------------

    /**
     * Handles the "Expense" menu option.
     * Prompts the user for a boat name and expense amount,
     * and adds the expense if allowed by the Commodore's policy.
     *
     * @param fleet The fleet containing the boat to spend on
     */

    private static void spendOnBoatMenu(FleetOfBoats fleet) {

        System.out.print("Which boat do you want to spend on?         : ");
        String name = keyboard.nextLine();
        SingleBoat boat = fleet.findBoat(name);

        if (boat == null) {
            System.out.println("Cannot find boat " + name);
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        double amount = keyboard.nextDouble();
        keyboard.nextLine();

        if (boat.addExpense(amount)) {
            // Print cumulative spent
            System.out.printf("Expense authorized, $%.2f spent.\n", boat.getExpenses());
        } else {
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n",
                    boat.remainingBudget());
        }
    } //End of spendOnBoatMenu method

    // ----------------------------------------------------------------------

    /**
     * Prints a nicely formatted fleet report, including all boats
     * and the total purchase price and total expenses.
     *
     * @param fleet The fleet to report
     */
    private static void printFleetReport(FleetOfBoats fleet) {
        double totalPaid = 0;
        double totalSpent = 0;

        System.out.println("\nFleet report:");
        for (SingleBoat boat : fleet.getBoats()) {
            System.out.printf("    %-7s %-20s %4d %-10s %5.0f' : Paid $ %8.2f : Spent $ %8.2f\n",
                    boat.getType(),
                    boat.getName(),
                    boat.getYear(),
                    boat.getMakeModel(),
                    boat.getLength(),
                    boat.getPurchasePrice(),
                    boat.getExpenses());
            totalPaid += boat.getPurchasePrice();
            totalSpent += boat.getExpenses();
        }
        System.out.printf("    %-53s : Paid $ %8.2f : Spent $ %8.2f\n",
                "Total", totalPaid, totalSpent);
    }

    // ----------------------------------------------------------------------

    /**
     * Loads fleet data from a CSV file.
     * Each line in the CSV represents a boat in the format:
     * TYPE,NAME,YEAR,MAKE_MODEL,LENGTH,PURCHASE_PRICE
     *
     * @param fleet The fleet to populate
     * @param filename The path to the CSV file
     */

    private static void loadCSVData(FleetOfBoats fleet, String filename) {

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                BoatType type = BoatType.valueOf(parts[0].toUpperCase());
                String name = parts[1];
                int year = Integer.parseInt(parts[2]);
                String makeModel = parts[3];
                double length = Double.parseDouble(parts[4]);
                double price = Double.parseDouble(parts[5]);
                fleet.addBoat(new SingleBoat(type, name, year, makeModel, length, price));
            }
        } catch (Exception e) {
            System.out.println("Error reading CSV file.");
        }
    } //End of loadCSVData method

    // ----------------------------------------------------------------------

    /**
     * Loads previously saved fleet data from the serialized FleetData.db file.
     *
     * @param fleet The fleet to populate with saved data
     */

    private static void loadSerializedData(FleetOfBoats fleet) {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("FleetData.db"))) {
            FleetOfBoats savedFleet = (FleetOfBoats) in.readObject();
            for (SingleBoat boat : savedFleet.getBoats()) {
                fleet.addBoat(boat);
            }
        } catch (Exception e) {
            // File not found or error, just start a new fleet
        }
    } //End of loadSerializedData method

    // ----------------------------------------------------------------------

    /**
     * Saves the fleet data to a serialized FleetData.db file.
     *
     * @param fleet The fleet to save
     */

    private static void saveSerializedData(FleetOfBoats fleet) {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("FleetData.db"))) {
            out.writeObject(fleet);
        } catch (Exception e) {
            System.out.println("Error saving fleet data.");
        }
    } //End of saveSerializedData method
} //End of FleetManager class