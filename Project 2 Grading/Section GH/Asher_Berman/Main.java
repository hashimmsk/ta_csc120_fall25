import java.io.*;
import java.util.*;

/**
 * Entry point and controller for the Fleet Management System.
 * Handles loading, saving, and all user-driven operations on the fleet.
 * @author : Asher Berman
 * @version : 2.3
 */
public class Main {

    /** Scanner used for all console input */
    private final Scanner in = new Scanner(System.in);

    /** List storing all boats currently managed */
    private final List<Boat> fleet = new ArrayList<>();

    /** File path for serialized fleet storage */
    private static final String DB_FILE = "fleet.db";

    /** File path for CSV fleet storage */
    private static final String CSV_FILE = "fleetInit.csv";

    /**
     * Program entry point. Creates a Main instance and starts execution.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        new Main().run();
    }//End of main method (Calls the run method)

    /**
     * Main execution loop of the fleet system.
     * Loads data, displays the menu, and dispatches user actions.
     */
    private void run() {
        loadFleet();

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        boolean done = false;
        while (!done) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choice = in.nextLine().trim().toUpperCase();

            switch (choice) {
                case "P": printFleet(); break;
                case "A": addBoat(); break;
                case "R": removeBoat(); break;
                case "E": expenseBoat(); break;
                case "X": done = true; break;
                default:
                    System.out.println("Invalid menu option, try again");
            }//Switch case for menu
        }//End of not finished while loop

        System.out.println("\nExiting the Fleet Management System");
        saveFleet();
    }//End of run method

    // ----------LOADING LOGIC (MODIFIED)----------

    /**
     * Attempts to load the fleet from CSV first, then DB, else loads defaults.
     */
    private void loadFleet() {
        fleet.clear(); //Ensure the list is empty before loading

        //Try to load from CSV (Prioritized)
        if (loadCSV()) {
            //Success message is not printed to match the desired output's clean startup
        }
        //Fallback to DB if CSV load fails
        else if (loadDB()) {
        }
        //Fallback to Default if both fail
        else {
            loadDefaultFleet();
        }
    }//End of loadFleet method

    /**
     * Loads fleet data from the serialized DB file.
     * @return true if load successful, false otherwise
     */
    private boolean loadDB() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DB_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                List<?> raw = (List<?>) obj;
                for (Object o : raw) {
                    // Check instance before casting
                    if (o instanceof Boat) fleet.add((Boat) o);
                }//End of add loop
                return true;
            }//End of instance check condition
        } catch (Exception ignored) {}//End of exception handling
        return false;
    }//End of loadDB method

    /**
     * Loads fleet data from a CSV file, handling both 6-field (initial) and
     * 7-field (saved) formats.
     * @return true if fleet successfully loaded, false otherwise
     */
    private boolean loadCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Boat b = null;

                // Case 1: Saved CSV data (7 fields)
                if (parts.length == 7) {
                    // Reconstruct the 6-field string for the Boat.fromCSV parser
                    String coreCsv = String.join(",", Arrays.copyOfRange(parts, 0, 6));
                    b = Boat.fromCSV(coreCsv);
                    if (b != null) {
                        // Manually set the saved expense amount
                        double savedExpense = Double.parseDouble(parts[6].trim());
                        b.setTotalExpensesForLoad(savedExpense);
                    }//End of if not null check
                }//End of if length == 7 check
                // Case 2: Initial CSV data (6 fields)
                else if (parts.length == 6) {
                    b = Boat.fromCSV(line);
                }//End of else if check

                if (b != null) fleet.add(b);
            }//End of while loop
            return !fleet.isEmpty();
        } catch (Exception ignored) {}//End of exception handling
        return false;
    }//End of loadCSV method

    /**
     * Loads a default fleet when no stored data can be read.
     */
    private void loadDefaultFleet() {
        fleet.add(new Boat(Boat.BoatType.POWER, "Big Brother", 2019, "Mako", 20, 12989.56));
        fleet.add(new Boat(Boat.BoatType.SAILING, "Moon Glow", 1973, "Bristol", 30, 5500.00));
        fleet.add(new Boat(Boat.BoatType.SAILING, "Osita", 1988, "Tartan", 40, 11500.07));
        fleet.add(new Boat(Boat.BoatType.POWER, "Rescue II", 2016, "Zodiac", 12, 8900.00));
    }//End of loadDefaultFleet method

    /**
     * Saves the fleet to both DB and CSV representations.
     */
    private void saveFleet() {
        saveDB();
        saveCSV();
    }//End of saveFleet method

    /**
     * Writes the fleet to the serialized DB file.
     */
    private void saveDB() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILE))) {
            oos.writeObject(fleet);
        } catch (Exception ignored) {}//End of exception handling
    }//End of saveDB method

    /**
     * Writes the fleet to CSV format.
     */
    private void saveCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE))) {
            for (Boat b : fleet) pw.println(b.toCSV());
        } catch (Exception ignored) {}//End of exception handling
    }//End of saveCSV method

    // ----------MENU FUNCTIONS----------

    /**
     * Prints the fleet report, including totals paid and spent.
     */
    private void printFleet() {
        System.out.println("\nFleet report:");
        double totalPaid = 0.0;
        double totalSpent = 0.0;

        for (Boat b : fleet) {
            System.out.println(b);
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getTotalExpenses();
        }//End of print loop

        // Use printf with correct formatting and locale for predictable output
        System.out.printf(Locale.ROOT,
                "    Total                                             : Paid $ %10.2f : Spent $ %10.2f\n\n",
                totalPaid, totalSpent
        );
    }//End of printFleet method

    /**
     * Prompts user for CSV boat data and adds a new boat to the fleet.
     */
    private void addBoat() {
        System.out.print("Please enter the new boat CSV data          : ");
        String line = in.nextLine().trim();

        Boat b = Boat.fromCSV(line);
        if (b == null) {
            System.out.println("Invalid CSV format.\n");
            return;
        }//End of bad CSV response

        fleet.add(b);
        saveFleet();
    }//End of addBoat method

    /**
     * Removes a boat from the fleet by name.
     */
    private void removeBoat() {
        System.out.print("Which boat do you want to remove?           : ");
        String name = in.nextLine().trim();

        Boat found = findBoat(name);
        if (found == null) {
            System.out.println("Cannot find boat " + name + "\n");
            return;
        }//End of boat existence checking

        fleet.remove(found);
        saveFleet();
    }//End of removeBoat method

    /**
     * Adds an expense to a specified boat if the budget allows.
     */
    private void expenseBoat() {
        System.out.print("Which boat do you want to spend on?         : ");
        String name = in.nextLine().trim();

        Boat b = findBoat(name);
        if (b == null) {
            System.out.println("Cannot find boat " + name + "\n");
            return;
        }//End of boat existence check

        System.out.print("How much do you want to spend?              : ");
        double amt;
        try {
            amt = Double.parseDouble(in.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid amount.\n");
            return;
        }//End of exception handling

        if (b.addExpense(amt)) {
            // Changed output format to match required text
            System.out.printf(Locale.ROOT, "Expense authorized, $%.2f spent.\n\n", b.getTotalExpenses());
            saveFleet();
        } else {
            System.out.printf(Locale.ROOT,
                    "Expense not permitted, only $%.2f left to spend.\n\n",
                    b.getRemainingBudget()
            );
        }//End of else segment
    }//End of expenseBoat method

    /**
     * Finds a boat in the fleet by its name.
     * @param name the name to search for
     * @return the matching Boat, or null if not found
     */
    private Boat findBoat(String name) {
        String target = name.trim().toLowerCase();//target definition
        for (Boat b : fleet) {
            if (b.getName().toLowerCase().equals(target))
                return b;
        }//End of for loop
        return null;
    }//End of findBoat method
}//End of Main class