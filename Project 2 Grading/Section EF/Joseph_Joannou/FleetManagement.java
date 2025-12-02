import java.util.Scanner;
import java.io.*;


/**
 * The FleetManagement class contains the main method and controls
 * the user interface, file loading/saving, and menu for the program.
 *
 * This is the only class that uses Scanner for keyboard input.
 *
 *  @author Joseph Joannou
 * @version 1.0
 * @since 2025-12-1
 */
public class FleetManagement {

    private static final String DB_FILE_NAME = "FleetData.db"; // Name of the file where the fleet will be saved and loaded.


    /**
     * Program entry point.
     *
     * @param args command line arguments.
     *             If args[0] exists, it should be the CSV file name.
     */
    public static void main(String[] args) {

        // Show the welcome message at the top of the program.
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println(); // blank line

        Fleet fleet;

        if (args.length > 0) {
            // Initializing run: load from CSV
            String csvFileName = args[0];
            fleet = loadFromCsv(csvFileName);
        } else {
            // Normal run: load from DB
            fleet = loadFromDb();
        }

        // If loading failed for some reason, start with an empty fleet
        if (fleet == null) {
            fleet = new Fleet();
        }


        // Create a Scanner to read from the keyboard.
        // Note: this is the ONLY class that uses Scanner.
        Scanner keyboard = new Scanner(System.in);

        boolean done = false; // will become true when user chooses to exit

        // Main menu loop
        while (!done) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choiceLine = keyboard.nextLine().trim();

            if (choiceLine.isEmpty()) {
                System.out.println("Invalid menu option, try again");
                continue; // go back to the top of the loop
            }

            // Take the first character and ignore upper/lower case
            char choice = Character.toUpperCase(choiceLine.charAt(0));

            switch (choice) {
                case 'P':
                    // Print the fleet inventory
                    // (we will write printFleetReport() later)
                    printFleetReport(fleet);
                    System.out.println(); // blank line after report
                    break;

                case 'A':
                    // Add a new boat from CSV-style input
                    handleAddBoat(fleet, keyboard);
                    System.out.println(); // blank line
                    break;

                case 'R':
                    // Remove a boat by name
                    handleRemoveBoat(fleet, keyboard);
                    System.out.println(); // blank line
                    break;

                case 'E':
                    // Request to spend money on a boat
                    handleExpense(fleet, keyboard);
                    System.out.println(); // blank line
                    break;

                case 'X':
                    // Exit the loop and program
                    done = true;
                    break;

                default:
                    // Any other character is invalid
                    System.out.println("Invalid menu option, try again");
                    break;
            } // end of switch on choice
        } // end of while menu loop

        // After user chooses to exit, save the fleet data
        saveToDb(fleet);

        // Print the exit message
        System.out.println();
        System.out.println("Exiting the Fleet Management System");

        // Close the Scanner
        keyboard.close();

    } // end of main method


    /**
     * Loads fleet data from a CSV file.
     * Each line in the file should look like:
     * TYPE,Name,Year,MakeModel,Length,PurchasePrice
     *
     * Example:
     * POWER,Big Brother,2019,Mako,20,12989.56
     *
     * @param fileName the name of the CSV file
     * @return a Fleet object with all boats from the file,
     *         or null if there was an error
     */
    private static Fleet loadFromCsv(String fileName) {
        Fleet fleet = new Fleet(); // start with an empty fleet

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines just in case
                if (line.isEmpty()) {
                    continue;
                }

                // Split the line by commas
                String[] parts = line.split(",");

                // Expect exactly 6 parts: type, name, year, make, length, price
                if (parts.length != 6) {
                    // If the line is not in the correct format, skip it
                    continue;
                }

                // Extract and clean each part
                String typeText = parts[0].trim();
                String name = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                String makeModel = parts[3].trim();
                int lengthFeet = Integer.parseInt(parts[4].trim());
                double purchasePrice = Double.parseDouble(parts[5].trim());

                // Convert type text to BoatType (SAILING or POWER)
                BoatType type = BoatType.fromString(typeText);

                // Create a new Boat (expenses start at 0.0)
                Boat boat = new Boat(type, name, year, makeModel, lengthFeet, purchasePrice);

                // Add the boat to the fleet
                fleet.addBoat(boat);
            }

            return fleet; // successfully loaded
        } catch (IOException e) {
            // If there is any problem reading the file, show a message and return null
            System.out.println("Error reading CSV file: " + e.getMessage());
            return null;
        }
    } // end of loadFromCsv method


    /**
     * Loads the fleet from the saved data file (FleetData.db).
     *
     * @return a Fleet object if the file exists and is valid,
     *         or null if the file does not exist or an error occurs
     */
    private static Fleet loadFromDb() {
        File dbFile = new File(DB_FILE_NAME);

        // If the file does not exist yet (first run), just return null
        if (!dbFile.exists()) {
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dbFile))) {

            // Read an object from the file
            Object obj = in.readObject();

            // Make sure the object is actually a Fleet
            if (obj instanceof Fleet) {
                return (Fleet) obj;
            } else {
                System.out.println("Saved data file is not a Fleet object.");
                return null;
            }

        } catch (IOException | ClassNotFoundException e) {
            // If something goes wrong loading the file, show a message and return null
            System.out.println("Error loading saved fleet data: " + e.getMessage());
            return null;
        }
    } // end of loadFromDb method


    /**
     * Saves the fleet to the data file (FleetData.db) in serialized form.
     *
     * @param fleet the Fleet object to save
     */
    private static void saveToDb(Fleet fleet) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(DB_FILE_NAME))) {

            // Write the Fleet object to the file
            out.writeObject(fleet);

        } catch (IOException e) {
            System.out.println("Error saving fleet data: " + e.getMessage());
        }
    } // end of saveToDb method


    /**
     * Prints the full fleet report:
     * - Each boat, formatted nicely
     * - Followed by a Total line
     *
     * @param fleet the Fleet object to display
     */
    private static void printFleetReport(Fleet fleet) {
        System.out.println();
        System.out.println("Fleet report:");

        // Print each boat using its formatted report line
        for (Boat b : fleet.getBoats()) {
            System.out.println(b.getReportLine());
        }

        // Print totals
        System.out.printf(
                "    Total                                             : Paid $ %8.2f : Spent $ %8.2f%n",
                fleet.getTotalPurchasePrice(),
                fleet.getTotalExpenses()
        );
    } // end of printFleetReport method


    /**
     * Handles adding a new boat to the fleet.
     * The user is asked to enter CSV-style data on one line:
     * TYPE,Name,Year,MakeModel,Length,PurchasePrice
     *
     * Example input:
     * SAILING,Finesse,1974,Tartan,34,9200.50
     *
     * @param fleet    the Fleet to add the new boat to
     * @param keyboard the Scanner used to read user input
     */
    private static void handleAddBoat(Fleet fleet, Scanner keyboard) {
        System.out.print("Please enter the new boat CSV data          : ");
        String line = keyboard.nextLine().trim();

        // If the user just presses Enter, do nothing
        if (line.isEmpty()) {
            return;
        }

        // Split the input into parts
        String[] parts = line.split(",");

        // Expect exactly 6 parts
        if (parts.length != 6) {
            System.out.println("Invalid CSV format, boat not added.");
            return;
        }

        try {
            String typeText = parts[0].trim();
            String name = parts[1].trim();
            int year = Integer.parseInt(parts[2].trim());
            String makeModel = parts[3].trim();
            int lengthFeet = Integer.parseInt(parts[4].trim());
            double purchasePrice = Double.parseDouble(parts[5].trim());

            BoatType type = BoatType.fromString(typeText);

            // Create the new boat (expenses start at 0.0)
            Boat boat = new Boat(type, name, year, makeModel, lengthFeet, purchasePrice);

            // Add it to the fleet
            fleet.addBoat(boat);

        } catch (Exception e) {
            // If anything goes wrong (like bad numbers), don't crash the program
            System.out.println("Error adding boat. Please check your input.");
        }
    } // end of handleAddBoat method


    /**
     * Handles removing a boat from the fleet.
     * Asks the user for the boat name, then removes it if found.
     *
     * @param fleet    the Fleet to remove the boat from
     * @param keyboard the Scanner used to read user input
     */
    private static void handleRemoveBoat(Fleet fleet, Scanner keyboard) {
        System.out.print("Which boat do you want to remove?           : ");
        String name = keyboard.nextLine().trim();

        // Try to remove the boat (case-insensitive inside Fleet)
        boolean removed = fleet.removeBoatByName(name);

        if (!removed) {
            System.out.println("Cannot find boat " + name);
        }
    } // end of handleRemoveBoat method


    /**
     * Handles a request to spend money on a boat.
     * Checks the Commodore's rule: total expenses cannot exceed purchase price.
     *
     * @param fleet    the Fleet that contains the boats
     * @param keyboard the Scanner used to read user input
     */
    private static void handleExpense(Fleet fleet, Scanner keyboard) {
        System.out.print("Which boat do you want to spend on?         : ");
        String name = keyboard.nextLine().trim();

        // Try to find the boat (case-insensitive)
        Boat boat = fleet.findBoatByName(name);

        if (boat == null) {
            System.out.println("Cannot find boat " + name);
            return; // nothing more to do
        }

        System.out.print("How much do you want to spend?              : ");
        String amountText = keyboard.nextLine().trim();

        try {
            double amount = Double.parseDouble(amountText);

            // How much money is left before hitting the purchase price
            double remaining = boat.getRemainingBudget();

            if (amount <= remaining) {
                // Allowed: update expenses
                boat.addExpense(amount);

                // Show the new total amount spent on this boat
                System.out.printf("Expense authorized, $%.2f spent.%n", boat.getExpenses());
            } else {
                // Not allowed: show how much is left
                System.out.printf("Expense not permitted, only $%.2f left to spend.%n", remaining);
            }

        } catch (NumberFormatException e) {
            // If the user somehow typed a non-number
            System.out.println("Invalid amount entered.");
        }
    } // end of handleExpense method






} // end of FleetManagement class

