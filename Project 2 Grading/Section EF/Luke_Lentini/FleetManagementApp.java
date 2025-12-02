

import java.io.*;
import java.util.Scanner;

/**
 * fleetmanagement.FleetManagementApp.java
 *
 * Main program for the Fleet Management System.
 *
 * Program rules:
 * ---------------------------------------------------
 * 1. On the FIRST RUN:
 *      - User must supply a CSV filename as a command-line argument.
 *      - Fleet is initialized from that CSV.
 *      - FleetData.db is created for future runs.
 *
 * 2. On SUBSEQUENT RUNS:
 *      - No command-line arguments required.
 *      - Program loads FleetData.db automatically.
 *
 * 3. On EXIT:
 *      - FleetData.db is overwritten with updated serialized Fleet.
 *
 * IMPORTANT: All file reading/writing happens ONLY in this class.
 * Fleet, Boat, etc. contain no file I/O methods.
 *
 * @author  Luke
 *  @version 1.0
 *  @since   2025-12-1
 */
public class FleetManagementApp {

    /** Filename for serialized database. */
    private static final String DB_FILENAME = "FleetData.db";

    /**
     * Program entry.
     */
    public static void main(String[] args) {

        Fleet fleet;

        // ---------------------------------------------------
        // Determine whether this is a FIRST RUN or a NORMAL RUN
        // ---------------------------------------------------
        if (args.length > 0) {
            // FIRST RUN — CSV initialization
            String FleetData = args[0];
            try {
                File file = new File("src/FleetData.csv");
                fleet = loadFromCsv(FleetData);
                saveToDb(fleet);  // immediately create FleetData.db
                System.out.println("Fleet initialized from CSV: " + FleetData);
                System.out.println("Welcome to the Fleet Management System\n" +
                        "--------------------------------------\n");
            } catch (Exception e) {
                System.err.println("ERROR reading CSV file: " + e.getMessage());
                return;
            }

        } else {
            // SUBSEQUENT RUN — load fleet from FleetData.db

            try {
                fleet = loadFromDb(DB_FILENAME);
                System.out.println("Fleet loaded from " + fleet);
                System.out.println("Welcome to the Fleet Management System\n" +
                        "--------------------------------------\n");
            } catch (FileNotFoundException e) {
                // No DB? Create new empty fleet
                System.out.println("No " + DB_FILENAME + " found. Starting new empty fleet.");
                fleet = new Fleet();
            } catch (Exception e) {
                System.err.println("Error loading fleet database: " + e.getMessage());
                return;
            }
        }

        // ======================================================
        // Main menu loop
        // ======================================================
        Scanner in = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String input = in.nextLine().trim().toUpperCase();

            if (input.isEmpty()) {
                System.out.println("Invalid option.");
                continue;
            }

            char choice = input.charAt(0);

            switch (choice) {

                case 'P':
                    System.out.println(fleet.getReport());
                    break;

                case 'A':
                    System.out.print("Enter new boat CSV (TYPE,Name,Year,Make,Length,Price): ");
                    String csvLine = in.nextLine();
                    try {
                        Boat b = Fleet.fromCsvLine(csvLine);
                        fleet.addBoat(b);
                        System.out.println("Boat added.");
                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 'R':
                    System.out.print("Enter boat name to remove: ");
                    String removeName = in.nextLine().trim();
                    if (fleet.removeBoatByName(removeName)) {
                        System.out.println("Boat removed.");
                    } else {
                        System.out.println("Boat not found.");
                    }
                    break;

                case 'E':
                    System.out.print("Which boat do you want to spend on? ");
                    String boatName = in.nextLine().trim();
                    Boat boat = fleet.findBoatByName(boatName);
                    if (boat == null) {
                        System.out.println("Boat not found.");
                        break;
                    }

                    System.out.print("Amount to spend: ");
                    try {
                        double amt = Double.parseDouble(in.nextLine().trim());

                        if (boat.canSpend(amt)) {
                            boat.spend(amt);   // adds amt to existing expenses

                            System.out.printf(
                                    "Expense of $%.2f recorded. Total spent on %s is now $%.2f.%n",
                                    amt,
                                    boat.getName(),
                                    boat.getExpenses()
                            );

                        } else {
                            double left = boat.getPurchasePrice() - boat.getExpenses();
                            System.out.printf(
                                    "Expense denied — only $%.2f remaining budget for this boat.%n",
                                    left
                            );
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount.");
                    }
                    break;

                case 'X':
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        // ======================================================
        // SAVE TO DATABASE ON EXIT
        // ======================================================
        try {
            saveToDb(fleet);
            System.out.println("Fleet saved to " + DB_FILENAME);
        } catch (IOException ioe) {
            System.err.println("Error saving fleet database: " + ioe.getMessage());
        }

        in.close();
        System.out.println("Exiting Fleet Management System...");
    }

    // ---------------------------------------------------
    // FILE HANDLING METHODS
    // ---------------------------------------------------

    /**
     * Load a Fleet from a CSV file.
     * @param csvFile CSV filename
     * @return Fleet
     * @throws IOException on file read error
     */
    public static Fleet loadFromCsv(String csvFile) throws IOException {
        Fleet fleet = new Fleet();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    fleet.addBoat(Fleet.fromCsvLine(trimmed));
                }
            }
        }
        return fleet;
    }

    /**
     * Save a Fleet object to FleetData.db.
     * @param fleet fleet to serialize
     * @throws IOException on write error
     */
    public static void saveToDb(Fleet fleet) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DB_FILENAME))) {
            out.writeObject(fleet);
        }
    }

    /**
     * Load a Fleet object from FleetData.db.
     * @return Fleet
     * @throws IOException on read error
     * @throws ClassNotFoundException if Fleet class missing
     */
    public static Fleet loadFromDb(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DB_FILENAME))) {
            return (Fleet) in.readObject();
        }
    }

}

