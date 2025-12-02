import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class FleetManagementSystem {

    // Constants
    private static final String DB_FILENAME = "FleetData.db";

    // Main entry point
    public static void main(String[] args) {
        // Initial greeting
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        // Step 1: Load the fleet either from CSV (first run) or from DB (later runs).
        Fleet fleet = loadFleet(args);

        // Step 2: Create the single Scanner for user input and run the menu loop.
        try (Scanner keyboard = new Scanner(System.in)) {
            runMenu(fleet, keyboard);
        }

        // Step 3: Save the updated fleet to the DB file before exiting.
        saveToDb(fleet, DB_FILENAME);

        // Final message
        System.out.println();
        System.out.println("Exiting the Fleet Management System");
    }

    // File Handling Methods

    private static Fleet loadFleet(String[] args) {
        if (args != null && args.length > 0) {
            // Initializing run: load from CSV file specified on the command line.
            String csvFileName = args[0];
            return loadFromCsv(csvFileName);
        } else {
            // Normal run: load from serialized database file.
            File dbFile = new File(DB_FILENAME);
            if (!dbFile.exists()) {
                // If the DB file does not exist, we cannot continue (per assignment spec).
                System.out.println("No existing fleet database found (" + DB_FILENAME + ").");
                System.out.println("Please run once with the CSV filename as a command-line argument.");
                System.exit(1);
            }
            return loadFromDb(DB_FILENAME);
        }
    }

    private static Fleet loadFromCsv(String csvFileName) {
        Fleet fleet = new Fleet();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFileName))) {
            String line;

            // Read line by line until the end-of-file.
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    // Skip blank lines just in case.
                    continue;
                }

                // Split the CSV line into fields.
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    // According to the assignment, CSV will be correctly formatted,
                    // but this guard keeps the code robust.
                    continue;
                }

                // Parse each field into the correct data type.
                BoatType type = BoatType.fromString(parts[0]);
                String name = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                String make = parts[3].trim();
                int lengthFeet = Integer.parseInt(parts[4].trim());
                double purchasePrice = Double.parseDouble(parts[5].trim());

                // Create a new Boat and add it to the fleet.
                Boat boat = new Boat(type, name, year, make, lengthFeet, purchasePrice);
                fleet.addBoat(boat);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            System.exit(1);
        }

        return fleet;
    }

    private static Fleet loadFromDb(String dbFileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFileName))) {
            Object obj = ois.readObject();
            return (Fleet) obj;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading fleet database: " + e.getMessage());
            System.exit(1);
        }
        // Control never actually reaches here, but the compiler requires a return.
        return null;
    }

    private static void saveToDb(Fleet fleet, String dbFileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dbFileName))) {
            oos.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Error saving fleet database: " + e.getMessage());
        }
    }

    // Menu/UI Methods

    private static void runMenu(Fleet fleet, Scanner keyboard) {
        String choice;

        do {
            // Display menu prompt exactly as in the sample.
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine().trim();

            if (choice.isEmpty()) {
                // Empty input: just reprompt.
                continue;
            }

            // We only care about the first character, case-insensitive.
            char option = Character.toUpperCase(choice.charAt(0));

            switch (option) {
                case 'P':
                    handlePrint(fleet);
                    break;
                case 'A':
                    handleAdd(fleet, keyboard);
                    break;
                case 'R':
                    handleRemove(fleet, keyboard);
                    break;
                case 'E':
                    handleExpense(fleet, keyboard);
                    break;
                case 'X':
                    // Exit handled after the loop; do nothing here.
                    break;
                default:
                    // Any other character is an invalid menu choice.
                    System.out.println("Invalid menu option, try again");
            }

        } while (!choice.isEmpty() && Character.toUpperCase(choice.charAt(0)) != 'X');
    }

    private static void handlePrint(Fleet fleet) {
        fleet.printReport();
    }

    private static void handleAdd(Fleet fleet, Scanner keyboard) {
        System.out.print("Please enter the new boat CSV data          : ");
        String line = keyboard.nextLine().trim();

        if (line.isEmpty()) {
            // Nothing entered; simply return to the menu.
            return;
        }

        String[] parts = line.split(",");
        if (parts.length < 6) {
            // According to spec, we can assume correct format,
            // but we still protect against bad data.
            System.out.println("Invalid CSV data for new boat.");
            return;
        }

        try {
            // Parse the new boat's data.
            BoatType type = BoatType.fromString(parts[0]);
            String name = parts[1].trim();
            int year = Integer.parseInt(parts[2].trim());
            String make = parts[3].trim();
            int lengthFeet = Integer.parseInt(parts[4].trim());
            double purchasePrice = Double.parseDouble(parts[5].trim());

            // Create and add the new boat.
            Boat boat = new Boat(type, name, year, make, lengthFeet, purchasePrice);
            fleet.addBoat(boat);
        } catch (Exception e) {
            // Any parsing problem ends up here.
            System.out.println("Error adding boat: " + e.getMessage());
        }
    }

    private static void handleRemove(Fleet fleet, Scanner keyboard) {
        System.out.print("Which boat do you want to remove?           : ");
        String name = keyboard.nextLine().trim();

        if (!fleet.removeBoatByName(name)) {
            // Name not found (case-insensitive search).
            System.out.println("Cannot find boat " + name);
        }
    }

    private static void handleExpense(Fleet fleet, Scanner keyboard) {
        System.out.print("Which boat do you want to spend on?         : ");
        String name = keyboard.nextLine().trim();

        Boat boat = fleet.findBoatByName(name);
        if (boat == null) {
            // Boat not found.
            System.out.println("Cannot find boat " + name);
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        String amountStr = keyboard.nextLine().trim();

        try {
            double amount = Double.parseDouble(amountStr);

            // Current and hypothetical new total expenses.
            double currentExpenses = boat.getExpenses();
            double newTotal = currentExpenses + amount;

            if (newTotal <= boat.getPurchasePrice()) {
                // Policy allows this expense: update the boat.
                boat.addExpense(amount);
                System.out.printf(Locale.US,
                        "Expense authorized, $%.2f spent.%n",
                        boat.getExpenses());
            } else {
                // Policy violation: compute how much is left and deny.
                double remaining = boat.getPurchasePrice() - currentExpenses;
                System.out.printf(Locale.US,
                        "Expense not permitted, only $%.2f left to spend.%n",
                        remaining);
            }
        } catch (NumberFormatException e) {
            // According to the assignment, numeric input is syntactically correct,
            // but this guard doesn't hurt.
            System.out.println("Invalid amount.");
        }
    }
} // End of FleetManagementSystem class
