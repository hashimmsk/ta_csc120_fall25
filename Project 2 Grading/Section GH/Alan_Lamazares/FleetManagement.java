import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
 * Fleet Management System for tracking boats and expenses.
 */
public class FleetManagement {

    // constants and shared state
    private static final Scanner KEYBOARD = new Scanner(System.in);
    private static final String DB_FILE = "FleetData.db";
    private static ArrayList<Boat> fleet;

    /**
     * Entry point. Loads fleet data and runs the menu loop.
     * @param args optional CSV filename for initialization
     */
    public static void main(String[] args) {

        // intro
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        // load fleet data
        if (args.length > 0) {
            fleet = loadFleetFromCSV(args[0]);
        } else {
            fleet = loadFleetFromDB();
        }

        // menu loop
        boolean running = true;
        while (running) {

            String s;
            char choice;

            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            s = KEYBOARD.nextLine();
            while (s.length() == 0 || "PAREXparex".indexOf(s.charAt(0)) == -1) {
                System.out.println("Invalid menu option, try again");
                System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
                s = KEYBOARD.nextLine();
            }
            choice = Character.toUpperCase(s.charAt(0));

            if (choice == 'P') {
                printFleet();
            } else if (choice == 'A') {
                addBoat();
            } else if (choice == 'R') {
                removeBoat();
            } else if (choice == 'E') {
                addExpense();
            } else if (choice == 'X') {
                saveFleetToDB();
                System.out.println("\nExiting the Fleet Management System");
                running = false;
            }
        }
    } // end of main method

    /**
     * Loads fleet data from a CSV file.
     * @param filename the CSV filename
     * @return ArrayList of boats
     */
    private static ArrayList<Boat> loadFleetFromCSV(String filename) {
        ArrayList<Boat> boats = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                Boat boat = parseCSVLine(line);
                if (boat != null) {
                    boats.add(boat);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }

        return boats;
    } // end of loadFleetFromCSV

    /**
     * Loads fleet data from the database file.
     * @return ArrayList of boats
     */
    private static ArrayList<Boat> loadFleetFromDB() {
        ArrayList<Boat> boats = new ArrayList<>();

        try {
            FileInputStream fileIn = new FileInputStream(DB_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            boats = (ArrayList<Boat>) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            // file does not exist yet
        } catch (IOException e) {
            System.out.println("Error reading database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading fleet data: " + e.getMessage());
        }

        return boats;
    } // end of loadFleetFromDB

    /**
     * Saves fleet data to the database file.
     */
    private static void saveFleetToDB() {
        try {
            FileOutputStream fileOut = new FileOutputStream(DB_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(fleet);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error saving database: " + e.getMessage());
        }
    } // end of saveFleetToDB

    /**
     * Parses a CSV line into a Boat object.
     * @param line the CSV line
     * @return Boat object or null if invalid
     */
    private static Boat parseCSVLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 6) {
            try {
                BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
                String name = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                String makeModel = parts[3].trim();
                int length = Integer.parseInt(parts[4].trim());
                double price = Double.parseDouble(parts[5].trim());
                return new Boat(type, name, year, makeModel, length, price);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    } // end of parseCSVLine

    /**
     * Prints the fleet inventory with totals.
     */
    private static void printFleet() {
        System.out.println();
        System.out.println("Fleet report:");

        double totalPaid = 0.0;
        double totalSpent = 0.0;

        int i = 0;
        while (i < fleet.size()) {
            Boat boat = fleet.get(i);
            System.out.printf("    %-7s %-20s %4d %-12s %3d' : Paid $ %7.2f : Spent $ %8.2f%n",
                    boat.getType().toString(),
                    boat.getName(),
                    boat.getYear(),
                    boat.getMakeModel(),
                    boat.getLength(),
                    boat.getPurchasePrice(),
                    boat.getExpenses());

            totalPaid += boat.getPurchasePrice();
            totalSpent += boat.getExpenses();
            i++;
        }

        System.out.printf("    %-7s %-37s : Paid $ %7.2f : Spent $ %8.2f%n",
                "Total", "", totalPaid, totalSpent);
    } // end of printFleet

    /**
     * Adds a boat to the fleet from CSV input.
     */
    private static void addBoat() {
        System.out.print("Please enter the new boat CSV data          : ");
        String csvData = KEYBOARD.nextLine();

        Boat boat = parseCSVLine(csvData);
        if (boat != null) {
            fleet.add(boat);
        }
    } // end of addBoat

    /**
     * Removes a boat from the fleet by name.
     */
    private static void removeBoat() {
        System.out.print("Which boat do you want to remove?           : ");
        String boatName = KEYBOARD.nextLine();

        int index = findBoatByName(boatName);
        if (index == -1) {
            System.out.println("Cannot find boat " + boatName);
        } else {
            fleet.remove(index);
        }
    } // end of removeBoat

    /**
     * Adds an expense to a boat if authorized.
     */
    private static void addExpense() {
        System.out.print("Which boat do you want to spend on?         : ");
        String boatName = KEYBOARD.nextLine();

        int index = findBoatByName(boatName);
        if (index == -1) {
            System.out.println("Cannot find boat " + boatName);
        } else {
            System.out.print("How much do you want to spend?              : ");
            double amount = KEYBOARD.nextDouble();
            KEYBOARD.nextLine();

            Boat boat = fleet.get(index);
            boolean authorized = boat.addExpense(amount);

            if (authorized) {
                System.out.printf("Expense authorized, $%.2f spent.%n", boat.getExpenses());
            } else {
                System.out.printf("Expense not permitted, only $%.2f left to spend.%n",
                        boat.getAmountLeftToSpend());
            }
        }
    } // end of addExpense

    /**
     * Finds a boat by name (case-insensitive).
     * @param name the boat name
     * @return the index or -1 if not found
     */
    private static int findBoatByName(String name) {
        int i = 0;
        while (i < fleet.size()) {
            if (fleet.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
            i++;
        }
        return -1;
    } // end of findBoatByName
}