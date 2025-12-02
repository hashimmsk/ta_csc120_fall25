import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main FleetManagement class holds the code that allows the user to load the data from a
 * CSV file, create an ArrayList, add/remove boats, and spend money on boats.
 */
public class FleetManagement {

    private static final String DB_FILE_NAME = "FleetData.db";

    /**
     *
     * @param args The args are set to the name of the CSV file in the initial run and empty in
     *            subsequent runs.
     * The main method contains the code for calling the methods to either load from the CSV
     * or the DB file depending on which run it is. It also prints the menu options available to the user.
     */
    public static void main(String[] args) {
        ArrayList<Boat> fleet;


        if (args.length > 0) {
            // Initializing run: read from CSV
            String csvFileName = args[0];
            fleet = loadFromCsv(csvFileName);
        } else {
            // Subsequent runs: reading from serialized DB
            fleet = loadFromDb();
        }

        Scanner keyboard = new Scanner(System.in);

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        boolean done = false;
        while (!done) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String input = keyboard.nextLine();
            if (input.isEmpty()) {
                continue;
            }
            char choice = Character.toUpperCase(input.charAt(0));

            switch (choice) {
                case 'P':
                    printFleetReport(fleet);
                    break;
                case 'A':
                    addBoat(fleet, keyboard);
                    break;
                case 'R':
                    removeBoat(fleet, keyboard);
                    break;
                case 'E':
                    handleExpense(fleet, keyboard);
                    break;
                case 'X':
                    done = true;
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
                    break;
            }
        }


        saveToDb(fleet);
        System.out.println();
        System.out.println("Exiting the Fleet Management System");
    }

    /**
     *
     * @param fileName The name of the file, from the command line.
     * @return Returns the filled ArrayList with boats.
     * This method reads the data from each line in the CSV and creates the ArrayList with boats.
     */
    private static ArrayList<Boat> loadFromCsv(String fileName) {
        ArrayList<Boat> fleet = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                Boat boat = Boat.fromCsvLine(line);
                fleet.add(boat);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            // Start with empty fleet if something goes wrong
        }
        return fleet;
    }

    /**
     * Loads data from the previously saved DB if the initial run is complete
     * @return Returns the ArrayList of boats.
     */
    private static ArrayList<Boat> loadFromDb() {
        ArrayList<Boat> fleet = new ArrayList<>();
        File dbFile = new File(DB_FILE_NAME);
        if (!dbFile.exists()) {
            return fleet; // first time, no DB yet
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                fleet = (ArrayList<Boat>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading database file: " + e.getMessage());
            // Start empty if failed
        }
        return fleet;
    }

    /**
     * Saves the ArrayList to the DB to be used in the next run.
     * @param fleet The ArrayList of boats, previously filled.
     */
    private static void saveToDb(ArrayList<Boat> fleet) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILE_NAME))) {
            oos.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Error writing database file: " + e.getMessage());
        }
    }

    /**
     * Prints the information on all the boats in the fleet, and how much money has been spent.
     * @param fleet The ArrayList of boats.
     */
    private static void printFleetReport(ArrayList<Boat> fleet) {
        System.out.println();
        System.out.println("Fleet report:");
        double totalPaid = 0.0;
        double totalSpent = 0.0;

        for (Boat b : fleet) {
            System.out.println(b);
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        System.out.printf("    %-47s : Paid $ %8.2f : Spent $ %8.2f%n",
                "Total", totalPaid, totalSpent);
        System.out.println();
    }

    /**
     * Adds a boat to the array from a user input matching the structure of the CSV file.
     * @param fleet The ArrayList that the boat will go into
     * @param keyboard Allows the user to type data
     */
    private static void addBoat(ArrayList<Boat> fleet, Scanner keyboard) {
        System.out.print("Please enter the new boat CSV data          : ");
        String csvLine = keyboard.nextLine();
        if (csvLine.isEmpty()) {
            return;
        }
        try {
            Boat b = Boat.fromCsvLine(csvLine);
            fleet.add(b);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid boat data, not added.");
        }
    }

    /**
     * Removes a boat from the ArrayList.
     * @param fleet The ArrayList to remove the boat from.
     * @param keyboard Allows the user to choose which boat to remove.
     */
    private static void removeBoat(ArrayList<Boat> fleet, Scanner keyboard) {
        System.out.print("Which boat do you want to remove?           : ");
        String name = keyboard.nextLine();
        if (name.isEmpty()) return;

        int index = findBoatIndex(fleet, name);
        if (index == -1) {
            System.out.println("Cannot find boat " + name);
        } else {
            fleet.remove(index);
        }
    }

    /**
     * Allows the user to choose a boat and spend money on it.
     * @param fleet The ArrayList of boats.
     * @param keyboard Allows the user to input their selection of boat and amount.
     */
    private static void handleExpense(ArrayList<Boat> fleet, Scanner keyboard) {
        System.out.print("Which boat do you want to spend on?         : ");
        String name = keyboard.nextLine();
        if (name.isEmpty()) return;

        int index = findBoatIndex(fleet, name);
        if (index == -1) {
            System.out.println("Cannot find boat " + name);
            return;
        }

        Boat boat = fleet.get(index);

        System.out.print("How much do you want to spend?              : ");
        String amountStr = keyboard.nextLine();
        if (amountStr.isEmpty()) return;
        double amount = Double.parseDouble(amountStr);

        double remaining = boat.remainingBudget();
        if (amount <= remaining) {
            boat.addExpense(amount);
            System.out.printf("Expense authorized, $%.2f spent.%n", boat.getExpenses());
        } else {
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", remaining);
        }
    }

    /**
     * A helper method that finds the index of a specific named boat in the ArrayList.
     * @param fleet The ArrayList of boats
     * @param name The name of the boat to search for
     * @return The index of the requested boat in the ArrayList
     */
    private static int findBoatIndex(ArrayList<Boat> fleet, String name) {
        for (int i = 0; i < fleet.size(); i++) {
            if (fleet.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }
}
