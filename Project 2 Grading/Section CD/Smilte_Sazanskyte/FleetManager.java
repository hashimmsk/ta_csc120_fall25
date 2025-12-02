import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.List;

public class FleetManager {

    /**
     * Scanner for user input
     */
    private static Scanner keyboard = new Scanner(System.in);

    /**
     * File to store serialized fleet data
     */
    private static final String DATA_FILE = "FleetData.db";

    /**
     * The main method
     *
     * @param args passed in from command line
     */
    public static void main(String[] args) {

        // The fleet list that will store all Boat objects in the system
        ArrayList<Boat> fleet = new ArrayList<>();

        /*
        If the program was started with a command-line argument, that argument should be the name of a CSV file.
        Example:  java FleetManager FleetData.csv
        In that case → initialize the fleet by loading boats from CSV.
        If the program was started with NO arguments, then this is
        considered a "normal" run, so the program loads previously-saved data
        from FleetData.db (the serialized file).
        */

        if (args.length > 0) {
            // First run: load boats from a CSV file
            loadFromCSV(args[0], fleet);
        } else {
            // Later runs: load saved fleet data from FleetData.db
            loadFromSerializedFile(fleet);
        }

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        // Show menu
        menuInput(fleet);
    }


    /**
     * Loads boat data from a CSV file and adds each boat to the fleet.
     * @param fileName the name of the CSV file to read
     * @param fleet ArrayList to store Boat objects
     */
    public static void loadFromCSV(String fileName, ArrayList<Boat> fleet) {

        try (Scanner fileScanner = new Scanner(new File(fileName))) {

            // Read the file line-by-line
            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();

                // CSV files is separated by commas
                String[] parts = line.split(",");

                // ---- CONVERT THE CSV TEXT INTO REAL DATA TYPES ----

                // Boat type is an enum (POWER or SAILING)
                Boat.BoatType type = Boat.BoatType.valueOf(parts[0].trim().toUpperCase());

                // Basic text fields
                String name = parts[1].trim();
                String make = parts[3].trim();

                // Convert Strings → numbers
                int year = Integer.parseInt(parts[2].trim());
                int length = Integer.parseInt(parts[4].trim());
                double price = Double.parseDouble(parts[5].trim());

                // ---- CREATE THE BOAT OBJECT ----
                // Expenses start at 0.0 when loaded from CSV
                Boat boat = new Boat(type, name, year, make, length, price, 0.0);

                // Add it to the fleet list
                fleet.add(boat);
            }

        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    } // End of loadFromCSV method

    /**
     * Loads fleet data from serialized DB file.
     * This method is used on NORMAL runs of the program — when the user does NOT provide a CSV file.
     * @param fleet ArrayList to store Boat objects after loading
     */
    public static void loadFromSerializedFile(ArrayList<Boat> fleet) {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {

            // Read an object from the file and cast it back into a List<Boat>.
            List<Boat> loadedFleet = (List<Boat>) ois.readObject();
            // Add all boats from the saved file into a fleet list
            fleet.addAll(loadedFleet);

        } catch (FileNotFoundException e) {
            // This happens the first time the program is run, because FleetData.db does not exist yet.
            System.out.println("No previous data found. Starting fresh.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    } // End of loadFromSerializedFile method

    /**
     * Saves fleet data to a serialized file (FleetData.db)
     * This converts the entire ArrayList of Boat objects into bytes
     * @param fleet ArrayList of Boat objects
     */
    public static void saveToSerializedFile(ArrayList<Boat> fleet) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            // Write the data to the file
            oos.writeObject(fleet);

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    } // End of saveToSerializedFile method

    /**
     * Displays the menu and handles user input.
     * @param fleet ArrayList of Boat objects
     */
    public static void menuInput(ArrayList<Boat> fleet) {
        char userInput;

        // Loop forever until the user chooses to exit
        while (true) {
            // Display menu prompt
            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            // Read user input, remove spaces, convert to lowercase, use the first character
            userInput = keyboard.nextLine().trim().toLowerCase().charAt(0);

            switch (userInput) {
                case 'p': // Show a formatted fleet report
                    printFleet(fleet);
                    break;
                case 'a': // Add a new boat to the fleet
                    addBoat(fleet);
                    break;
                case 'r': // Remove a boat from the fleet
                    removeBoat(fleet);
                    break;
                case 'e':  // Add an expense to a boat
                    addExpense(fleet);
                    break;
                case 'x': // Save the fleet to the serialized file before exiting
                    saveToSerializedFile(fleet);
                    System.out.println("\nExiting the Fleet Management System");
                    return; // Leave the menuInput() method entirely and the program ends
                default:
                    System.out.println("Invalid menu option, try again.");
            }
        }
    } // End of menuInput method

    /**
     * Prints the fleet report.
     * @param fleet ArrayList of Boat objects
     */
    public static void printFleet(ArrayList<Boat> fleet) {

        // Running totals for the entire fleet
        double totalPaid = 0;
        double totalSpent = 0;

        System.out.println("\nFleet report:");

        // Loop through each boat in the fleet and print its info
        for (Boat boat : fleet) {

            // Print the boat using its toString() formatting
            System.out.println("\t" + boat);

            // Add this boat's purchase price to the fleet total
            totalPaid += boat.getPurchasePrice();

            // Add this boat's expenses to the fleet total
            totalSpent += boat.getExpenses();
        }

        // Print the combined totals for all boats, formatted to line up with the others
        System.out.printf("\tTotal                                         : Paid $%10.2f : Spent $%10.2f%n",
                totalPaid, totalSpent);

    } // End of printFleet method

    /**
     * Prompts the user to add a new boat to the fleet.
     * The user must enter the boat information in CSV format:
     * TYPE,Name,Year,Make,Length,Price
     * @param fleet ArrayList of Boat objects
     */
    public static void addBoat(ArrayList<Boat> fleet) {

        // Ask the user to type CSV data for a new boat
        System.out.print("Please enter the new boat CSV data: ");

        String line = keyboard.nextLine(); // Read the entire line of input

        try {

            // Split the input by commas to extract each field
            String[] parts = line.split(",");

            // Convert the first part to an enum (must match SAILING or POWER)
            Boat.BoatType type = Boat.BoatType.valueOf(parts[0].toUpperCase());

            // Extract each field from the CSV input
            String name = parts[1];
            int year = Integer.parseInt(parts[2]);
            String make = parts[3];
            int length = Integer.parseInt(parts[4]);
            double price = Double.parseDouble(parts[5]);

            // Create the new boat and add it to the fleet
            // Expenses start at 0.0 for a new boat
            fleet.add(new Boat(type, name, year, make, length, price, 0.0));

        } catch (Exception e) {
            System.out.println("Invalid input. Boat not added.");
        }
    } // End of addBoat method

    /**
     * Prompts the user to remove a boat from the fleet based on its name.
     * @param fleet ArrayList of Boat objects
     */
    public static void removeBoat(ArrayList<Boat> fleet) {

        // Ask the user which boat name should be removed
        System.out.print("Which boat do you want to remove? ");
        String name = keyboard.nextLine().trim();

        // Search the fleet for a boat with the given name
        Boat boat = findBoatByName(fleet, name);

        // If a matching boat exists, remove it
        if (boat != null) {
            fleet.remove(boat);
            System.out.println("Boat removed.");

        } else {
            System.out.println("Cannot find boat " + name);
        }
    } // End of removeBoat method

    /**
     * Prompts the user to add an expense to a specific boat.
     * The program checks whether the amount is allowed (cannot exceed purchase price).
     * @param fleet ArrayList of Boat objects
     */
    public static void addExpense(ArrayList<Boat> fleet) {

        // Ask the user which boat the expense should be applied to
        System.out.print("Which boat do you want to spend on? ");
        String name = keyboard.nextLine().trim();

        // Look for the boat in the fleet
        Boat boat = findBoatByName(fleet, name);

        // If the boat exists, continue with the expense input process
        if (boat != null) {

            System.out.print("How much do you want to spend? ");
            try {

                // Convert the user's amount to a double
                double amount = Double.parseDouble(keyboard.nextLine());

                // A boat cannot spend more than its original purchase price
                double remaining = boat.getPurchasePrice() - boat.getExpenses();

                // Check if the expense is within the allowed remaining budget
                if (amount <= remaining) {

                    // If allowed, add the expense to the boat
                    boat.addExpense(amount);
                    System.out.printf("Expense authorized, $%.2f spent.%n", amount);

                } else {

                    // Not enough remaining funds
                    System.out.printf("Expense not permitted, only $%.2f left to spend.%n",
                            boat.getPurchasePrice() - boat.getExpenses());
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid number input.");
            }
        } else { // No boat with the given name
            System.out.println("Cannot find boat " + name);
        }
    } // End of addExpense method

    /**
     * Finds a boat by name (case-insensitive).
     * @param fleet ArrayList of Boat objects to search through
     * @param name Name of the boat to find
     * @return Boat object if found, otherwise null
     */
    public static Boat findBoatByName(ArrayList<Boat> fleet, String name) {

        // Loop through each boat in the fleet
        for (Boat boat : fleet) {

            // Compare the boat's name to the input, ignoring upper/lower case differences
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }
        // No matching boat found, return null
        return null;
    } // End of findBoatByName method

} // End of FleetManager