import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * FleetManagement system for loading, saving, and modifying a fleet of boats.
 * Handles CSV input on first run and DB file input on subsequent runs.
 * It allows users to:
 * - Print the fleet
 * - Add a boat to the fleet
 * - Remove a boat from the fleet
 * - Add expenses to a boat
 *
 * @author Muhammad Ahsan Saleem
 */
public class FleetManagement {

    /** Scanner object for user input */
    private static final Scanner keyboard = new Scanner(System.in);
    /** Maximum length of boat in feet */
    private static final byte MAX_LENGTH = 100; // Not used
    /** Maximum purchase price of a boat */
    private static final double MAX_PURCHASE_PRICE = 1000000.0; // Not used

    /**
     * Main method that runs the Fleet Management System, does all the file handling,
     * fills the fleet ArrayList, gets the menu option input from the user, and calls the
     * appropriate method to perform the required task.
     *
     * @param args optional file name for CSV input (on first run)
     */
    public static void main(String[] args) {

        String fileName;
        // ArrayList to store the boats in a fleet
        ArrayList<Boat> fleet = new ArrayList<>();

        if (args.length > 0) {

            fileName = args[0];
            try {
                // Initial run: read from the CSV file
                readFromCSVFile(fleet, fileName);
            }
            catch (IOException error) {
                System.out.println("Error loading: " + error.getMessage());
            } // end of try-catch

        } // end of the if block
        else {

            fileName = "C:\\Users\\ahsan\\CSC120LAB_EF_FALL2025\\FinalProject\\FleetData.db";

            try {

                // Each subsequent run: read from the DB file
                readFromDBFile(fleet, fileName);

            } catch (IOException error) {
                System.out.println("Error loading: " + error.getMessage());
            } catch (ClassNotFoundException error) {
                System.out.println(error.getMessage());
            } // end of try-catch

        } // end of the else block

        // The fleet data is now loaded

        System.out.println("Welcome to the Fleet Management System\n" +
                "--------------------------------------");

        // Display the menu, and get the user's choice
        String menuOption = getMenuOption();

        String boatName;
        int index;
        // Performing the task requested by the user, and exiting the program if "X" is input
        while(!menuOption.equals("X")) {

            if (menuOption.equals("P")) {
                printFleet(fleet);
            }
            else if (menuOption.equals("A")) {
                addBoat(fleet);
            }
            else if (menuOption.equals("R")) {

                System.out.print("Which boat do you want to remove?           : ");
                keyboard.nextLine(); // Removing the new line character from previous input
                boatName = keyboard.nextLine();

                index = findIndexOfBoat(boatName, fleet);
                if (index != -1) {
                    fleet.remove(index);
                }

            }
            else {

                System.out.print("Which boat do you want to spend on?         : ");
                keyboard.nextLine(); // Removing the new line character from previous input
                boatName = keyboard.nextLine();

                index = findIndexOfBoat(boatName, fleet);
                if (index != -1) {
                    addExpense(index, fleet);
                }

            }

            menuOption = getMenuOption();
        } // end of the while loop

        fileName = "C:\\Users\\ahsan\\CSC120LAB_EF_FALL2025\\FinalProject\\FleetData.db";
        try {

            // Save the data to the DB file in a serialized form
            writeToDBFile(fleet, fileName);

        }
        catch (IOException error) {
            System.out.println("Error saving " + error.getMessage());
        } // end of try-catch

        System.out.println("\nExiting the Fleet Management System");

    } // end of the main method

    /**
     * Reads boat data from a CSV file and loads it into the fleet.
     *
     * @param fleet the ArrayList of boats to populate
     * @param fileName the CSV file path
     * @throws IOException if reading fails
     */
    public static void readFromCSVFile (ArrayList<Boat> fleet, String fileName) throws IOException {

        String line;
        String[] boatParts;

        BufferedReader fromBufferedReader = null;
        try {

            fromBufferedReader = new BufferedReader(new FileReader(fileName));
            line = fromBufferedReader.readLine();

            while (line != null) {

                // Split CSV line
                boatParts = line.split(",");

                // Create the Boat object
                Boat thisBoat = new Boat(
                        Boat.BoatType.valueOf(boatParts[0].toUpperCase()),
                        boatParts[1],
                        (short) Integer.parseInt(boatParts[2]),
                        boatParts[3],
                        (byte) Integer.parseInt(boatParts[4]),
                        Double.parseDouble(boatParts[5])
                );

                // Add the boat to the fleet
                fleet.add(thisBoat);
                line = fromBufferedReader.readLine();

            } // end of the while loop

        }
        finally {
            if (fromBufferedReader != null) {

                try {
                    // Closing the file
                    fromBufferedReader.close();
                } catch (IOException e) {
                    System.out.println("Error closing " + e.getMessage());
                }

            } // end of if condition
        } // end of try-finally


    } // end of the readFromCSVFile method

    /**
     * Writes the fleet data to a DB file in a serialized form.
     *
     * @param fleet the ArrayList of boats to save
     * @param fileName the DB file to write to
     * @throws IOException if writing fails
     */
    public static void writeToDBFile (ArrayList<Boat> fleet, String fileName) throws IOException {

        ObjectOutputStream toStream = null;
        try {

            toStream = new ObjectOutputStream(new FileOutputStream(fileName));

            int index;
            for (index = 0; index < fleet.size(); index++) {
                toStream.writeObject(fleet.get(index));
            } // end of for loop

        }
        finally {
            if (toStream != null) {
                try {
                    // Closing the file
                    toStream.close();
                } catch (IOException e) {
                    System.out.println("Error closing " + e.getMessage());
                }
            }
        } // end of try-finally

    } // end of the writeToDBFile method

    /**
     * Reads boats from a serialized DB file and loads them into the fleet.
     *
     * @param fleet the ArrayList to populate
     * @param fileName the DB file to read from
     * @throws IOException if reading fails
     * @throws ClassNotFoundException if Boat class is not found
     */
    public static void readFromDBFile (ArrayList<Boat> fleet, String fileName) throws IOException, ClassNotFoundException {

        ObjectInputStream fromStream = null;
        try {

            fromStream = new ObjectInputStream(new FileInputStream(fileName));
            Boat thisBoat = (Boat) fromStream.readObject();

            while (thisBoat != null) {

                // Add thisBoat to the fleet
                fleet.add(thisBoat);
                thisBoat = (Boat) fromStream.readObject();

            } // end of the while loop

        }
        catch (EOFException error) {
            // Do nothing - end of file reached
        }
        finally {
            if (fromStream != null) {
                try {
                    // Closing the file
                    fromStream.close();
                } catch (IOException e) {
                    System.out.println("Error closing " + e.getMessage());
                }
            }
        } // end of try-catch-finally

    } // end of the readFromDBFile method

    /**
     * Displays the menu and gets the user's selection.
     *
     * @return the chosen menu option as an uppercase letter
     */
    public static String getMenuOption() {

        System.out.println();
        System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
        String menuOption = keyboard.next().toUpperCase();

        // Validating the menu option input by the user
        while (!menuOption.equals("P") && !menuOption.equals("A") && !menuOption.equals("R") && !menuOption.equals("E") && !menuOption.equals("X")) {

            System.out.println("Invalid menu option, try again");
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            menuOption = keyboard.next().toUpperCase();

        } // end of the while loop
        return menuOption;

    } // end of the getMenuOption method

    /**
     * Prints all boats in the fleet and the total purchase price and expenses.
     *
     * @param fleet the ArrayList of boats
     */
    public static void printFleet (ArrayList<Boat> fleet) {

        double totalPurchasePrice = 0;
        double totalExpenses = 0;

        System.out.println("\nFleet report:");

        int index;
        for (index = 0; index < fleet.size(); index++) {

            System.out.println("    " + fleet.get(index).toString());
            totalPurchasePrice += fleet.get(index).getPurchasePrice(); // Calculating total purchase prices
            totalExpenses += fleet.get(index).getExpenses(); // Calculating total expenses

        } // end of for loop

        System.out.printf("    Total                                               : Paid $ " +
                "%10.2f : Spent $ %10.2f\n", totalPurchasePrice, totalExpenses);

    } // end of the printFleet method

    /**
     * Adds a new boat to the fleet using CSV-styled input.
     *
     * @param fleet the ArrayList of boats
     */
    public static void addBoat (ArrayList<Boat> fleet) {

        System.out.print("Please enter the new boat CSV data          : ");
        String[] boatParts = keyboard.next().split(",");

        // Create the Boat object
        Boat thisBoat = new Boat(
                Boat.BoatType.valueOf(boatParts[0].toUpperCase()),
                boatParts[1],
                (short) Integer.parseInt(boatParts[2]),
                boatParts[3],
                (byte) Integer.parseInt(boatParts[4]),
                Double.parseDouble(boatParts[5])
        );

        // Add the boat to the fleet
        fleet.add(thisBoat);

    } // end of the addBoat method

    /**
     * Finds the index of a boat in the fleet by name.
     *
     * @param boatName the name of the boat to search for
     * @param fleet the ArrayList of boats
     * @return the index of the boat, or -1 if not found
     */
    public static int findIndexOfBoat (String boatName, ArrayList<Boat> fleet) {

        int index;
        for (index = 0; index < fleet.size(); index++) {
            if (fleet.get(index).getName().equalsIgnoreCase(boatName)) {
                return index;
            }
        } // end of for loop

        System.out.println("Cannot find boat " + boatName);
        return -1;
    } // end of the findIndexOfBoat method

    /**
     * Adds an expense to a selected boat, ensuring limits are not exceeded.
     *
     * @param index the index of the boat in the fleet
     * @param fleet the ArrayList of boats
     */
    public static void addExpense (int index, ArrayList<Boat> fleet) {

        System.out.print("How much do you want to spend?              : ");
        double amountToSpend = keyboard.nextDouble();
        double currentExpenses = fleet.get(index).getExpenses();
        // Cannot spend more on a boat than its purchase price
        double maxExpenses = fleet.get(index).getPurchasePrice();

        if (currentExpenses + amountToSpend > maxExpenses) {
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n", (maxExpenses - currentExpenses));
        }
        else {
            fleet.get(index).setExpenses(amountToSpend + currentExpenses);
            System.out.printf("Expense authorized, $%.2f spent.\n", (currentExpenses + amountToSpend));
        }

    } // end of the addExpense method

} // end of the FleetManagement class
