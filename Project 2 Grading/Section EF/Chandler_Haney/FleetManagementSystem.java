import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The FleetManagementSystem class is the MAIN "brain" of the program.
 *
 * It is responsible for:
 * - Loading the fleet of boats from a CSV file or from a saved .db file
 * - Showing a text menu to the user
 * - Handling user choices: print, add, remove, expense, exit
 * - Enforcing the Commodore's rule that we never spend more than the purchase price
 * - Saving the fleet back to the .db file when exiting
 *
 * This class contains the main method and all file-handling methods,
 * as required by the assignment.
 */
public class FleetManagementSystem {

    /**
     * The list that holds ALL the Boat objects in the fleet.
     * This is static so there is only one shared fleet for the whole program.
     */
    private static ArrayList<Boat> fleet = new ArrayList<>();

    /**
     * The name of the database file where we store the fleet between runs.
     * This never changes, so it is declared as a constant.
     */
    private static final String DB_FILE = "FleetData.db";

    /**
     * The main entry point of the program.
     * This method:
     * 1. Prints the welcome message
     * 2. Tries to load the fleet from the .db file
     * 3. If that fails and a CSV file name is given, loads from the CSV file
     * 4. Shows the menu and keeps asking for options until the user exits
     * 5. On exit, saves the fleet into the .db file
     *
     * @param args command line arguments (first run should pass the CSV file name)
     */
    public static void main(String[] args) {

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        // Try first to load boats from the .db file (normal run)
        boolean loadedFromDb = loadFromDB();

        // If the .db file did not load AND a CSV file was given, use the CSV to initialize
        if (!loadedFromDb && args.length > 0) {
            loadFromCSV(args[0]);
        }

        // Only this class is allowed to have a Scanner, as required
        Scanner keyboard = new Scanner(System.in);
        String choice;

        // Main menu loop, keeps going until the user chooses X (exit)
        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine().trim().toUpperCase();  // make it uppercase so user can type p or P

            switch (choice) {
                case "P":
                    printFleet();
                    break;
                case "A":
                    addBoat(keyboard);
                    break;
                case "R":
                    removeBoat(keyboard);
                    break;
                case "E":
                    expenseBoat(keyboard);
                    break;
                case "X":
                    // Save before exiting
                    saveToDB();
                    System.out.println("\nExiting the Fleet Management System");
                    break;
                default:
                    // If the user types something like W or Z, show an error
                    System.out.println("Invalid menu option, try again");
            }

        } while (!choice.equals("X"));  // loop until user chooses X
    }

    /**
     * Loads boat data from a CSV file.
     * Each line of the file should look like:
     * TYPE,Name,Year,MakeModel,LengthFeet,PurchasePrice
     *
     * Example:
     * SAILING,Moon Glow,1973,Bristol,30,5500.00
     *
     * For each line, this method creates a Boat object and adds it to the fleet.
     *
     * @param filename the name of the CSV file to read
     */
    private static void loadFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;

            // Read each line until we reach the end of the file (null)
            while ((line = br.readLine()) != null) {
                // Split the line into parts around the commas
                String[] parts = line.split(",");

                BoatType type = BoatType.valueOf(parts[0].toUpperCase());
                String name = parts[1];
                int year = Integer.parseInt(parts[2]);
                String makeModel = parts[3];
                int length = Integer.parseInt(parts[4]);
                double price = Double.parseDouble(parts[5]);

                // Create a new Boat with the data from this line
                Boat b = new Boat(type, name, year, makeModel, length, price);
                fleet.add(b);
            }

        } catch (IOException e) {
            System.out.println("Could not load CSV file.");
        }
    }

    /**
     * Tries to load the fleet from the binary .db file.
     * If the file does not exist, this method returns false.
     *
     * @return true if the fleet was successfully read from the DB file; false otherwise
     */
    private static boolean loadFromDB() {
        File f = new File(DB_FILE);

        // If the file doesn't exist, we can't load from it
        if (!f.exists()) {
            return false;
        }

        // Try reading the ArrayList<Boat> from the file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {

            // We stored the whole fleet list, so we cast the object back to ArrayList<Boat>
            fleet = (ArrayList<Boat>) ois.readObject();
            return true;

        } catch (Exception e) {
            // If anything goes wrong, just return false and fall back to CSV
            return false;
        }
    }

    /**
     * Saves the current fleet (ArrayList of Boat objects) into the .db file.
     * This uses Java's built-in serialization so all boats are saved at once.
     */
    private static void saveToDB() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILE))) {

            oos.writeObject(fleet);

        } catch (IOException e) {
            System.out.println("Error saving database.");
        }
    }

    /**
     * Prints a report of all boats in the fleet, followed by a total line.
     * The report includes:
     * - Type
     * - Name
     * - Year
     * - Make/Model
     * - Length
     * - Purchase price
     * - Amount spent
     *
     * It also sums up:
     * - The total purchase price of all boats
     * - The total expenses for all boats
     */
    private static void printFleet() {
        System.out.println("\nFleet report:");

        double totalPaid = 0.0;
        double totalSpent = 0.0;

        // Go through each boat in the fleet and print it
        for (Boat b : fleet) {
            System.out.println("    " + b);  // uses Boat.toString()
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        // Print the total line, formatted to match the sample output as closely as possible
        System.out.printf(
                "            %-30s : Paid $ %9.2f : Spent $ %9.2f%n%n",
                "Total",
                totalPaid,
                totalSpent
        );
    }

    /**
     * Asks the user for CSV-style data, parses it, and adds a new boat to the fleet.
     * The user is expected to type something like:
     *
     * SAILING,Finesse,1974,Tartan,34,9200.50
     *
     * @param keyboard the Scanner used to read user input
     */
    private static void addBoat(Scanner keyboard) {
        System.out.print("Please enter the new boat CSV data          : ");
        String line = keyboard.nextLine();

        try {
            String[] parts = line.split(",");
            BoatType type = BoatType.valueOf(parts[0].toUpperCase());
            String name = parts[1];
            int year = Integer.parseInt(parts[2]);
            String makeModel = parts[3];
            int length = Integer.parseInt(parts[4]);
            double price = Double.parseDouble(parts[5]);

            Boat b = new Boat(type, name, year, makeModel, length, price);
            fleet.add(b);

        } catch (Exception e) {
            System.out.println("Invalid data format.");
        }
    }

    /**
     * Removes a boat from the fleet by its name.
     * The name check is case-insensitive, so "BIG BROTHER" and "Big Brother"
     * are treated the same.
     *
     * If the boat is found, it is removed.
     * If not, a "Cannot find boat ..." message is printed.
     *
     * @param keyboard the Scanner used to read user input
     */
    private static void removeBoat(Scanner keyboard) {
        System.out.print("Which boat do you want to remove?           : ");
        String inputName = keyboard.nextLine().trim().toLowerCase();

        // Use an index-based loop so we can safely remove an element from the list
        for (int i = 0; i < fleet.size(); i++) {
            Boat b = fleet.get(i);
            if (b.getName().toLowerCase().equals(inputName)) {
                fleet.remove(i);  // remove the matching boat
                return;           // stop after removing
            }
        }

        // If we reach here, no boat with that name was found
        System.out.println("Cannot find boat " + inputName);
    }

    /**
     * Handles a request to spend money on a particular boat.
     * Steps:
     * 1. Ask the user for the boat name (case-insensitive match).
     * 2. If the boat does not exist, print an error message.
     * 3. If the boat exists, ask how much to spend.
     * 4. Check the Commodore's rule:
     *    total spent (so far + new amount) must NOT exceed purchase price.
     * 5. If allowed, add the expense and show the new total spent.
     * 6. If not allowed, show how much money is left to spend.
     *
     * @param keyboard the Scanner used to read user input
     */
    private static void expenseBoat(Scanner keyboard) {
        System.out.print("Which boat do you want to spend on?         : ");
        String inputName = keyboard.nextLine().trim().toLowerCase();

        Boat match = null;

        // Find the boat with the given name (ignoring case)
        for (Boat b : fleet) {
            if (b.getName().toLowerCase().equals(inputName)) {
                match = b;
                break;
            }
        }

        if (match == null) {
            // No boat by that name was found
            System.out.println("Cannot find boat " + inputName);
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        double amount = Double.parseDouble(keyboard.nextLine());

        // How much more can we legally spend on this boat?
        double allowed = match.getPurchasePrice() - match.getExpenses();

        if (amount <= allowed) {
            // The amount is allowed, update the boat's expenses
            match.addExpense(amount);
            System.out.printf("Expense authorized, $%.2f spent.%n", match.getExpenses());
        } else {
            // The amount is too big, tell the user how much is left
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", allowed);
        }
    }
}



