import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the Boat Club Fleet Management System.
 * <p>
 * This program lets the user:
 * • View all boats (Print)
 * • Add a new boat
 * • Remove a boat
 * • Record expenses on a boat
 * • Exit and save everything
 * </p>
 * <p>
 * On startup:
 * • If you give a CSV filename (e.g. java FleetManagement FleetData.csv), it loads from that file<br>
 * • Otherwise it tries to load saved boats from FleetData.db
 * </p>
 * <p>
 * When you exit, the current fleet is automatically saved to FleetData.db
 * </p>
 *
 * @author Juliana Geyer-Kim
 * @version 1.0
 */


public class FleetManagement {


    // The list that holds all boats in memory
    private static ArrayList<Boat> fleet = new ArrayList<>();
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * Main method – starts the program.
     *
     * @param args Command line arguments. If a filename is given, loads boats from that CSV file.
     */


    public static void main(String[] args) {
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");


        // Load data: from CSV if argument provided, else from DB
        if (args.length > 0) {
            loadFromCSV(args[0]);
        }//end of if
        else {
            loadFromDB();
        }//end of else

        // Run the menu loop
        menuLoop();

        // Save to DB on exit
        saveToDB();


    }//end of main method

    /**
     * Loads fleet from CSV file (first run).
     */

    private static void loadFromCSV(String filename) {
        fleet.clear();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                } // end of if (empty line)

                // This line handles tabs, commas, and any amount of spaces
                String[] parts = line.split("[,\t]+");  // MAGIC LINE

                // Clean up any extra spaces in each part
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                } // end of for loop

                if (parts.length >= 6) {
                    BoatType type = BoatType.valueOf(parts[0].toUpperCase());
                    String name = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    String make = parts[3];
                    int length = Integer.parseInt(parts[4]);
                    double paid = Double.parseDouble(parts[5]);

                    Boat thisBoat = new Boat(type, name, year, make, length, paid);
                    fleet.add(thisBoat);
                } // end of if (parts.length >= 6)
            } // end of while loop
        } catch (FileNotFoundException e) {
            System.out.println("CSV file not found: " + filename);
        } catch (Exception e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } // end of try-catch
    } // end of loadFromCSV method

    /**
     * Loads fleet from serialized DB file.
     */
    private static void loadFromDB() {

        File file = new File("FleetData.db");

        if (file.exists()) {

            //opens the DB_FILE and uses ObjectInputStream to bring the saved list of boats back to life,
            //then puts that list into the variable called fleet so the program can use the boats again
            try (ObjectInputStream savedFleetInput = new ObjectInputStream(new FileInputStream("FleetData.db"))) {
                fleet = (ArrayList<Boat>) savedFleetInput.readObject();
                //   System.out.println("Fleet loaded from FleetData.db");

            }//end of try
            catch (IOException e) {
                System.out.println("Error loading database: " + e.getMessage());
                System.out.println("Starting with empty fleet.");
            } catch (ClassNotFoundException e) {
                System.out.println("Error loading database: " + e.getMessage());
                System.out.println("Starting with empty fleet.");
            }//end of catch
        } //end of if
        else {
            System.out.println("No database file found. Starting with empty fleet.");
        }//end of else
    }//end of loadFromDB method

    /**
     * Saves fleet to serialized DB file.
     */
    private static void saveToDB() {
        try (ObjectOutputStream saver = new ObjectOutputStream(
                new FileOutputStream("FleetData.db"))) {

            saver.writeObject(fleet);


        } catch (IOException e) {
            System.err.println("ERROR: Failed to save fleet to FleetData.db");
            System.err.println("Reason: " + e.getMessage());
            // Optional: print stack trace only in debug mode
            // e.printStackTrace();
        }
    }

    /**
     * Prints the fleet report
     */
    public static void printFleet() {


        System.out.println("\nFleet report:");

        double totalPaid = 0.0;
        double totalSpent = 0.0;

        // Loop through each boat in the fleet ArrayList
        for (int i = 0; i < fleet.size(); i++) {
            Boat boat = fleet.get(i); // get the boat at position i

            // Inside your Print (P) menu, when printing each boat:
            System.out.printf("    %-7s %-20s %4d %-12s %3d' : Paid $%9.2f : Spent $%9.2f%n",
                    boat.getType(),           // POWER or SAILING   → left-aligned 7 spaces
                    boat.getName(),           // boat name          → left-aligned 20 spaces
                    boat.getYear(),           // year               → 4 digits
                    boat.getMakeModel(),      // make               → left-aligned 12 spaces
                    boat.getLength(),         // number like 20     → adds ' after
                    boat.getPurchasePrice(),  // paid amount
                    boat.getExpenses());      // spent amount

            // Add to totals
            totalPaid += boat.getPurchasePrice();
            totalSpent += boat.getExpenses();
        }//end of for loop

        // Print the totals after the loop
        // After you calculate totalPaid and totalSpent
        // after your loop that prints all boats
        System.out.printf("    %-52s: Paid $%9.2f : Spent $%9.2f%n", "Total ", totalPaid, totalSpent);

        System.out.println();

    }//end of printFleet method

    /**
     * Adds a new boat from user input
     */
    public static void addBoat() {
        System.out.print("Please enter the new boat CSV data          : ");
        String line = keyboard.nextLine().trim();

        String[] parts = line.split(",");
        if (parts.length == 6) {

            //trims spaces from other elements and converts them to the right data types
            try {
                BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
                String name = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                String make = parts[3].trim();
                int length = Integer.parseInt(parts[4].trim());  // autoboxing
                double price = Double.parseDouble(parts[5].trim());

                //creates new boat
                Boat boat = new Boat(type, name, year, make, length, price);
                //adds boat to fleet
                fleet.add(boat);

                System.out.println();

            } catch (NumberFormatException e) {
                //exact error message not specified
                System.out.println("Invalid number format. Please check year, length, or price.");
            } catch (IllegalArgumentException e) {
                //exact error message not specified
                System.out.println("Invalid boat type or other argument error.");
            }//end of catch
        } else {
            //exact error message not specified
            System.out.println("Invalid CSV format. Make sure you have exactly 6 items separated by commas.");
        }//end of else
    }//end of addBoat method

    /**
     * Removes a boat by name.
     */
    private static void removeBoat() {
        System.out.print("Which boat do you want to remove?           : ");
        String name = keyboard.nextLine().trim();

        Boat toRemove = null;

        // Loop through each boat in the list using an index
        for (int i = 0; i < fleet.size(); i++) {
            Boat boat = fleet.get(i);  // get the boat at position i

            if (boat.getName().equalsIgnoreCase(name)) {
                toRemove = boat;
                break; // stop searching once we find a match
            }//end of if
        }//end of for loop

        // After the loop, check if a match was found
        if (toRemove != null) {
            fleet.remove(toRemove);
            System.out.println();
        } else {
            System.out.println("Cannot find boat " + name);
            System.out.println();
        }//end of else
    }//end of removeBoat method

    /**
     * Handles requesting permission to spend on a boat.
     */
    public static void handleExpense() {
        System.out.print("Which boat do you want to spend on?         : ");
        String name = keyboard.nextLine().trim();

        Boat boat = null;

        // Loop through the list of boats using an index
        for (int i = 0; i < fleet.size(); i++) {
            Boat currentBoat = fleet.get(i);  // get the boat at position i

            if (currentBoat.getName().equalsIgnoreCase(name)) {
                boat = currentBoat;
                break; // stop searching once we find a match
            }//end of if
        }//end of for loop
        if (boat == null) {
            System.out.println("Cannot find boat " + name);
            System.out.println();
            return;
        }
        System.out.print("How much do you want to spend?              : ");
        try {
            double amount = Double.parseDouble(keyboard.nextLine().trim());
            double remaining = boat.getPurchasePrice() - boat.getExpenses();
            if (amount > remaining) {
                System.out.printf("Expense not permitted, only $%.2f left to spend.\n", remaining);
            } else {
                boat.addExpense(amount);
                System.out.printf("Expense authorized, $%.2f spent.\n", boat.getExpenses());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
        System.out.println();
    }//end of handleExpenses method

    /**
     * Runs the main menu loop.
     */
    private static void menuLoop() {
        String option;
        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            option = keyboard.nextLine().trim().toUpperCase();

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
                    handleExpense();
                    break;

                case "X":
                    System.out.println("\nExiting the Fleet Management System");
                    break;

                default:
                    System.out.println("Invalid menu option, try again");
                    // no break needed for default when it's last
            }//end of switch
        } //end of do
        while (!"X".equals(option));
    }//end of menuLoop


}//end of FleetManagement class



