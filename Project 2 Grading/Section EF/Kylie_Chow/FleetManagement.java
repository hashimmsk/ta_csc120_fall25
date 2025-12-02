import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * FleetManagement
 * ------------------------------------------
 * This program manages the Coconut Grove Sailing Club fleet.
 * It loads boat data from a CSV file on the first run, stores
 * it in a serialized database file, and gives the user a clean,
 * aligned interface for printing, adding, removing, and recording
 * maintenance expenses on boats.
 *
 * All prompts use aligned colons and same-line input for readability.
 */
public class FleetManagement {

    /** Scanner for all keyboard input */
    private static final Scanner keyboard = new Scanner(System.in);

    /** Name of the serialized database file */
    private static final String DB_FILENAME = "FleetData.db";

    /** Column where colons should align for prompts */
    private static final int COLON_POS = 52;

    /**
     * Prints a user prompt with the colon aligned at COLON_POS,
     * keeping the cursor on the same line for user input.
     */
    private static void prompt(String text) {
        int spaces = Math.max(1, COLON_POS - text.length());
        System.out.print(text + " ".repeat(spaces) + ": ");
    }

    public static void main(String[] args) {

        ArrayList<Boat> fleet = new ArrayList<>();

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        // First run = CSV provided
        if (args.length > 0) {
            loadFromCsv(args[0], fleet);
        } else {
            loadFromDb(fleet);
        }

        menuLoop(fleet);

        System.out.println();
        System.out.println("Exiting the Fleet Management System");

        saveToDb(fleet);
    }

    /* ---------------------------------------------------------
       File Loading / Saving
       --------------------------------------------------------- */

    private static void loadFromCsv(String filename, ArrayList<Boat> fleet) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("CSV file not found: " + filename);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String first = line.split(",")[0].trim().toUpperCase(Locale.ROOT);

                // Skip header rows like "POWER  Big Brother  2019 ..."
                if (!first.equals("POWER") && !first.equals("SAILING")) continue;

                Boat b = parseCsvLine(line);
                if (b != null) fleet.add(b);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
    }

    private static void loadFromDb(ArrayList<Boat> fleet) {
        File file = new File(DB_FILENAME);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList) {
                fleet.clear();
                fleet.addAll((ArrayList<Boat>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading database file.");
        }
    }

    private static void saveToDb(ArrayList<Boat> fleet) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DB_FILENAME))) {
            out.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Error saving database file.");
        }
    }

    /* ---------------------------------------------------------
       CSV Parsing
       --------------------------------------------------------- */

    private static Boat parseCsvLine(String line) {
        String[] p = line.split(",");
        if (p.length < 6) return null;

        BoatType type = BoatType.fromString(p[0].trim());
        String name = p[1].trim();
        int year = Integer.parseInt(p[2].trim());
        String make = p[3].trim();
        int length = Integer.parseInt(p[4].trim());
        double price = Double.parseDouble(p[5].trim());

        return new Boat(type, name, year, make, length, price, 0.0);
    }

    /* ---------------------------------------------------------
       Menu Loop
       --------------------------------------------------------- */

    private static void menuLoop(ArrayList<Boat> fleet) {

        while (true) {
            prompt("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it");
            String input = keyboard.nextLine().trim();

            if (input.isEmpty()) {
                prompt("Invalid menu option, try again");
                System.out.println();
                continue;
            }

            char ch = Character.toUpperCase(input.charAt(0));

            switch (ch) {
                case 'P' -> printFleet(fleet);
                case 'A' -> addBoat(fleet);
                case 'R' -> removeBoat(fleet);
                case 'E' -> expenseBoat(fleet);
                case 'X' -> { return; }
                default -> {
                    prompt("Invalid menu option, try again");
                    System.out.println();
                }
            }
        }
    }

    /* ---------------------------------------------------------
       Menu Handlers
       --------------------------------------------------------- */

    private static void printFleet(ArrayList<Boat> fleet) {
        System.out.println();
        System.out.println("Fleet report:");

        double totalPaid = 0;
        double totalSpent = 0;

        for (Boat b : fleet) {
            System.out.println(b.toReportLine());
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        System.out.printf(
                "    Total                                             : Paid $ %8.2f : Spent $ %8.2f%n",
                totalPaid, totalSpent
        );

        System.out.println();
    }

    private static void addBoat(ArrayList<Boat> fleet) {
        prompt("Please enter the new boat CSV data");
        String line = keyboard.nextLine().trim();

        Boat b = parseCsvLine(line);
        if (b != null) fleet.add(b);

        System.out.println();
    }

    private static void removeBoat(ArrayList<Boat> fleet) {
        prompt("Which boat do you want to remove?");
        String name = keyboard.nextLine().trim();

        Boat found = findBoat(fleet, name);

        if (found == null) {
            System.out.println("Cannot find boat " + name);
        } else {
            fleet.remove(found);
        }

        System.out.println();
    }

    private static void expenseBoat(ArrayList<Boat> fleet) {
        prompt("Which boat do you want to spend on?");
        String name = keyboard.nextLine().trim();

        Boat b = findBoat(fleet, name);
        if (b == null) {
            System.out.println("Cannot find boat " + name);
            System.out.println();
            return;
        }

        prompt("How much do you want to spend?");
        double amount = Double.parseDouble(keyboard.nextLine());

        double newTotal = b.getExpenses() + amount;

        if (newTotal <= b.getPurchasePrice()) {
            b.setExpenses(newTotal);
            System.out.printf("Expense authorized, $%.2f spent.%n", newTotal);
        } else {
            double remaining = b.getPurchasePrice() - b.getExpenses();
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", remaining);
        }

        System.out.println();
    }

    /* ---------------------------------------------------------
       Helpers
       --------------------------------------------------------- */

    private static Boat findBoat(ArrayList<Boat> fleet, String name) {
        for (Boat b : fleet) {
            if (b.getName().equalsIgnoreCase(name)) return b;
        }
        return null;
    }
} // end of the FleetManagement class

