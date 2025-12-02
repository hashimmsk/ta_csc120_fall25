import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for Fleet Management System.
 * Handles loading CSV, saving/loading DB, menus, and user commands.
 * @author meganfoster
 * @version 1.0
 * @FleetManager
 */
public class FleetManager {

    private static final String DB_FILE = "FleetData.db";
    private static final String WELCOME =
            "Welcome to the Fleet Management System\n" +
                    "--------------------------------------\n";

    private ArrayList<Boat> fleet = new ArrayList<>();

    public static void main(String[] args) {
        new FleetManager().run(args);
    }

    /**
     * Runs the application.
     */
    private void run(String[] args) {
        System.out.print(WELCOME);

        // First run: load CSV
        if (args.length == 1) {
            loadCSV(args[0]);
        } else {
            // Later runs: load serialized DB file
            loadDB();
        }

        menuLoop();
        saveDB();

        System.out.println("\nExiting the Fleet Management System");
    }

    /**
     * Loads fleet data from a CSV file.
     */
    private void loadCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            while ((line = br.readLine()) != null) {
                Boat b = Boat.fromCSV(line);
                fleet.add(b);
            }

        } catch (Exception e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
    }

    /**
     * Loads serialized fleet data from DB file.
     */
    @SuppressWarnings("unchecked")
    private void loadDB() {
        File f = new File(DB_FILE);

        if (!f.exists()) return; // No DB file = no loading

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            fleet = (ArrayList<Boat>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error loading DB.");
        }
    }

    /**
     * Saves fleet data to serialized DB file.
     */
    private void saveDB() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILE))) {
            oos.writeObject(fleet);
        } catch (Exception e) {
            System.out.println("Error saving DB.");
        }
    }

    /**
     * Main menu loop for user interaction.
     */
    private void menuLoop() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choice = sc.nextLine().trim().toUpperCase();

            switch (choice) {
                case "P": printFleet(); break;
                case "A": addBoat(sc); break;
                case "R": removeBoat(sc); break;
                case "E": addExpense(sc); break;
                case "X": return;
                default:
                    System.out.println("Invalid menu option, try again");
            }
        }
    }

    /**
     * Prints the fleet in formatted report style.
     */
    private void printFleet() {
        System.out.println("\nFleet report:");

        double totalPaid = 0, totalSpent = 0;

        for (Boat b : fleet) {
            System.out.println(b);
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        System.out.printf(
                "    Total                                             : Paid $ %10.2f : Spent $ %10.2f\n",
                totalPaid, totalSpent
        );
    }

    /**
     * Adds a new boat using a CSV-formatted line entered by the user.
     */
    private void addBoat(Scanner sc) {
        System.out.print("Please enter the new boat CSV data          : ");
        String line = sc.nextLine();
        fleet.add(Boat.fromCSV(line));
    }

    /**
     * Removes a boat based on name.
     */
    private void removeBoat(Scanner sc) {
        System.out.print("Which boat do you want to remove?           : ");
        String name = sc.nextLine().trim();

        Boat found = null;

        for (Boat b : fleet) {
            if (b.getName().equalsIgnoreCase(name)) {
                found = b;
                break;
            }
        }

        if (found == null) {
            System.out.println("Cannot find boat " + name);
        } else {
            fleet.remove(found);
        }
    }

    /**
     * Handles adding an expense to a boat.
     * FIXED: Now prints TOTAL SPENT, matching the sample rubric output.
     */
    private void addExpense(Scanner sc) {

        System.out.print("Which boat do you want to spend on?         : ");
        String name = sc.nextLine().trim();

        Boat found = null;

        for (Boat b : fleet) {
            if (b.getName().equalsIgnoreCase(name)) {
                found = b;
                break;
            }
        }

        if (found == null) {
            System.out.println("Cannot find boat " + name);
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        double amount = Double.parseDouble(sc.nextLine());

        double remaining = found.getPurchasePrice() - found.getExpenses();

        // Allowed
        if (amount <= remaining) {
            found.addExpense(amount);

            // FIXED: print total spent, NOT amount spent this time
            System.out.printf("Expense authorized, $%.2f spent.\n", found.getExpenses());

        } else {
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n", remaining);
        }
    }
} // end of FleetManager class

