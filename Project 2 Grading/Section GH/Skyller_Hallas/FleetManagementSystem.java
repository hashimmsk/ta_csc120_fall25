import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class that runs the Fleet Management System.
 * Loads boats, displays the menu, and saves all data between runs.
 * @author Skyler Hallas
 * @version 1.0
 */
public class FleetManagementSystem {

    //constant for DB file name
    /**
     * The name of the file used to save and load the serialized fleet database.
     */
    private static final String DB_FILE = "FleetData.db";

    /**
     * Program entry point.
     * Loads boats (from CSV or saved DB), runs the menu, and saves changes.
     * @param args Command line arguments; if provided, the first argument is a CSV file.
     */
    public static void main(String[] args) {

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        ArrayList<Boat> fleet;

        //First run; CSV provided
        if (args.length > 0) {
            fleet = loadFromCsv(args[0]);
        } else {
            //Later runs; load saved data
            fleet = loadfromDb();
        }

        Scanner kb = new Scanner(System.in);

        runMenu(fleet, kb);

        saveToDb(fleet);

        System.out.println();
        System.out.println("Exiting the Fleet Management System");
    }//End of main

    // FILE HANDLING METHODS

    /**
     * Loads fleet data from a CSV text file.
     * Each non-empty line becomes a Boat using Boat.fromCsv().
     * @param fileName The CSV file to load.
     * @return A list of Boat objects created from the CSV file.
     * @exception IOException If the file cannot be read.
     */
    private static ArrayList<Boat> loadFromCsv(String fileName){
        ArrayList<Boat> list = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().length() > 0) {
                    Boat b = Boat.fromCsv(line);
                    list.add(b);
                }
            }// end of while loop

            br.close(); // rubric: files must be closed
        }// End of try
        catch (IOException e) {
            System.out.println("Error reading CSV file.");
        }// End of catch

        return list;
    }// End of array - load from CSV

    /**
     * Loads the fleet from the saved .db file using object deserialization.
     * @return A list of Boat objects; returns an empty list if the DB file does not exist.
     * @exception IOException If the file cannot be opened.
     * @exception ClassNotFoundException If the Boat class cannot be deserialized.
     */
    private static ArrayList<Boat> loadfromDb(){
        ArrayList<Boat> list = new ArrayList<>();

        File f = new File(DB_FILE);
        if (!f.exists()) {
            return list;  // empty fleet if first run
        }

        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(DB_FILE));

            list = (ArrayList<Boat>) in.readObject();
            in.close();
        }// End of try
        catch (Exception e) {
            System.out.println("Could not load saved fleet.");
        }// End of catch

        return list;
    }// End of array - load from Db

    /**
     * Saves the fleet to a .db file using object serialization.
     * @param list The fleet (ArrayList of Boat objects) to be saved.
     * @exception IOException If the file cannot be written.
     */
    private static void saveToDb(ArrayList<Boat> list) {
        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(DB_FILE));

            out.writeObject(list);
            out.close();
        }
        catch (IOException e) {
            System.out.println("Could not save data.");
        }
    }// End of saveToDb

//MENU SYSTEM

    /**
     * Displays the menu and handles user choices.
     * @param fleet The list of boats currently in the system.
     * @param kb The Scanner object used to collect user input.
     */
    private static void runMenu(ArrayList<Boat> fleet, Scanner kb) {

        boolean done = false;

        while (!done) {

            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String input = kb.nextLine().trim().toUpperCase();

            if (input.length() == 0) {
                continue;
            }

            char c = input.charAt(0);

            if (c=='P') {
                printFleet(fleet);
                System.out.println();

            } else if (c=='A') {
                addBoat(fleet, kb);
                System.out.println();

            } else if (c=='R') {
                removeBoat(fleet, kb);
                System.out.println();

            } else if (c=='E') {
                expenseBoat(fleet, kb);
                System.out.println();

            } else if (c=='X') {
                done = true;

            } else {
                System.out.println("Invalid menu option, try again");
            }
        }// End of while loop
    }// End of runMenu

//MENU ACTIONS

    /**
     * Prints the entire fleet along with total money paid and spent.
     * @param fleet The list of boats to print.
     */
    private static void printFleet(ArrayList<Boat> fleet) {

        System.out.println();
        System.out.println("Fleet report:");

        double totalPaid = 0;
        double totalSpent = 0;

        for (Boat b : fleet) {
            System.out.println(b.toString());
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        System.out.printf(
                "    %-44s : Paid $ %9.2f : Spent $ %9.2f\n",
                "Total", totalPaid, totalSpent);
    }//End of printFleet

    /**
     * Adds a boat to the fleet using a CSV line of input.
     * @param fleet The list to which the boat will be added.
     * @param kb Scanner used to read user input.
     */
    private static void addBoat(ArrayList<Boat> fleet, Scanner kb) {

        System.out.print("Please enter the new boat CSV data          : ");
        String line = kb.nextLine();

        try {
            Boat b = Boat.fromCsv(line);
            fleet.add(b);
        }
        catch (Exception e) {
            System.out.println("Invalid CSV format.");
        }
    }// End of addBoat

    /**
     * Removes a boat from the fleet by name (case insensitive).
     * @param fleet The list of boats.
     * @param kb Scanner used to read user input.
     */
    private static void removeBoat(ArrayList<Boat> fleet, Scanner kb) {

        System.out.print("Which boat do you want to remove?           : ");
        String name = kb.nextLine().trim();

        Boat b = findBoat(fleet, name);

        if (b == null) {
            System.out.println("Cannot find boat " + name);
        } else {
            fleet.remove(b);
        }
    }// End of removeBoat

    /**
     * Handles adding expense money to a specific boat.
     * Ensures spending does not exceed the boat's purchase price.
     * @param fleet List of boats.
     * @param kb Scanner for user input.
     */
    private static void expenseBoat(ArrayList<Boat> fleet, Scanner kb) {

        System.out.print("Which boat do you want to spend on?         : ");
        String name = kb.nextLine().trim();

        Boat b = findBoat(fleet, name);

        if (b == null) {
            System.out.println("Cannot find boat " + name);
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        double amount = Double.parseDouble(kb.nextLine());

        double newTotal = b.getExpenses() + amount;

        if (newTotal <= b.getPurchasePrice()) {
            b.addExpense(amount);
            System.out.printf("Expense authorized, $%.2f spent.\n",
                    b.getExpenses());
        } else {
            double left = b.remainingBudget();
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n", left);
        }
    }// End of expenseBoat

//HELPER METHODS

    /**
     * Finds a boat in the fleet by name (case insensitive).
     * @param fleet List of boats to search.
     * @param name The name of the boat to find.
     * @return The matching Boat, or null if none found.
     */
    private static Boat findBoat(ArrayList<Boat> fleet, String name) {

        for (Boat b : fleet) {
            if (b.getName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }// End of findBoat helper
}// End of class
