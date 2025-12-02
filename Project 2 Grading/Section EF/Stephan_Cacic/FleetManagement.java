import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
/**
 * This Program was designed to function like a console application for managing a fleet of boats.
 * <p>
 * It loads boat data from a CSV file (FleetData.csv) or a database and lets the user either
 * print the fleet files, add and remove boats, or record boat expenses, saving
 * updated fleet info back to disk when exiting
 *
 * @author Stephan Cacic
 */
public class FleetManagement {

    /** File name utilized to store serialized fleet between runs */
    private static final String DB_FILENAME = "FleetData.db";

    /** Shared Scanner for reading all keyboard input */
    private static final Scanner keyboard = new Scanner(System.in);

    /** In-memory collection of all boats in the fleet */
    private List<Boat> fleet = new ArrayList<>();

    /**
     * Program entry point. Creates {@code FleetManagement} object , starting the application.
     * @param args optional command-line arguments; if present, {@code args[0]} is treated as a CSV file
     */
    public static void main(String[] args) {
        FleetManagement app = new FleetManagement();
        app.run(args);
    }

    /**
     * Runs main application (loads the fleet, displays welcome message,
     * processes menu commands, and saves fleet on exit)
     * @param args command-line arguments passed from {@link #main(String[])}
     */
    private void run(String[] args) {
        try {
            loadFleet(args);
        } catch (Exception e) {
            fleet = new ArrayList<>();
        }

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        boolean done = false;
        while (!done) {
            char choice = promptMenu();
            switch (Character.toUpperCase(choice)) {
                case 'P':
                    System.out.println();
                    printFleet();
                    break;
                case 'A':
                    addBoat();
                    break;
                case 'R':
                    removeBoat();
                    break;
                case 'E':
                    expenseBoat();
                    break;
                case 'X':
                    done = true;
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
                    break;
            }
        }

        System.out.println("Exiting the Fleet Management System");

        try {
            saveFleet();
        } catch (IOException e) {
            System.err.println("Error saving fleet data: " + e.getMessage());
        }
    }

    /**
     * Loads fleet data at beginning of program.
     * <ul>
     *     <li>If a CSV filename is passed in {@code args[0]}, loads boats
     *     from that CSV file</li>
     *     <li>Otherwise, if {@code FleetData.db} exists, loads a serialized
     *     {@code List<Boat>} from that file</li>
     *     <li>Otherwise, starts with an empty fleet</li>
     * </ul>
     *
     * @param args command-line arguments, possibly containing a CSV filename
     * @throws IOException            if there is a problem reading either file
     * @throws ClassNotFoundException if the serialized object in the DB file
     *                                cannot be deserialized as {@code List<Boat>}
     */
    private void loadFleet(String[] args) throws IOException, ClassNotFoundException {
        if (args.length > 0) {
            String csvFile = args[0];
            fleet = loadFromCsv(csvFile);
        } else {
            File dbFile = new File(DB_FILENAME);
            if (dbFile.exists()) {
                try (ObjectInputStream in =
                             new ObjectInputStream(new FileInputStream(dbFile))) {
                    @SuppressWarnings("unchecked")
                    List<Boat> loaded = (List<Boat>) in.readObject();
                    fleet = loaded;
                }
            } else {
                fleet = new ArrayList<>();
            }
        }
    }

    /**
     * Reads boat data from CSV file and builds a list of {@link Boat} the given objects
     * all non-empty lines are to have said format:
     * {@code TYPE,Name,Year,MakeModel,LengthFeet,PurchasePrice}
     *
     * @param csvFile name of the CSV file to read
     * @return a newly created {@code List<Boat>} containing all boats from the file
     * @throws IOException if I/O error occurs whilst reading file
     */
    private List<Boat> loadFromCsv(String csvFile) throws IOException {
        List<Boat> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    continue;
                }
                BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
                String name = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                String makeModel = parts[3].trim();
                int lengthFeet = Integer.parseInt(parts[4].trim());
                double purchasePrice = Double.parseDouble(parts[5].trim());
                double expenses = 0.0;

                Boat boat = new Boat(type, name, year, makeModel,
                        lengthFeet, purchasePrice, expenses);
                list.add(boat);
            }
        }
        return list;
    }

    /**
     * Saves current fleet to the binary database file
     * {@link #DB_FILENAME} using object serialization
     * @throws IOException if an error occurs while writing the file
     */
    private void saveFleet() throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(DB_FILENAME))) {
            out.writeObject(fleet);
        }
    }

    /**
     * Displays main menu prompt and reads the user's choice.
     * @return the first character of the user's input or a space character if said user just presses Enter
     */
    private char promptMenu() {
        System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
        String line = keyboard.nextLine();
        if (line.isEmpty()) {
            return ' ';
        }
        return line.charAt(0);
    }

    /**
     * Prints formatted report of all the boats in the fleet, including a
     * final row showing the total amount paid and total expenses
     */
    private void printFleet() {
        System.out.println("Fleet report:");
        double totalPaid = 0.0;
        double totalSpent = 0.0;

        final String ROW_FORMAT =
                "    %-7s %-15s %-4s %-12s %3s : Paid $ %10.2f : Spent $ %10.2f%n";

        for (Boat boat : fleet) {
            System.out.printf(ROW_FORMAT,
                    boat.getType().name(),
                    boat.getName(),
                    boat.getYear(),
                    boat.getMakeModel(),
                    boat.getLengthFeet() + "'",
                    boat.getPurchasePrice(),
                    boat.getExpenses());
            totalPaid += boat.getPurchasePrice();
            totalSpent += boat.getExpenses();
        }

        System.out.printf(ROW_FORMAT,
                "Total", "", "", "", "",
                totalPaid, totalSpent);

        System.out.println();
    }

    /**
     * Prompts user to enter a new boat as a CSV-formatted line,
     * parses the line, creates a {@link Boat}, and adds it to the fleet
     * If the input is empty or invalid, the method simply returns
     */
    private void addBoat() {
        System.out.printf("%-44s: ", "Please enter the new boat CSV data");

        String line = keyboard.nextLine().trim();
        System.out.println();
        if (line.isEmpty()) {
            return;
        }

        String[] parts = line.split(",");
        if (parts.length < 6) {
            return;
        }

        try {
            BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
            String name = parts[1].trim();
            int year = Integer.parseInt(parts[2].trim());
            String makeModel = parts[3].trim();
            int lengthFeet = Integer.parseInt(parts[4].trim());
            double purchasePrice = Double.parseDouble(parts[5].trim());
            double expenses = 0.0;

            Boat boat = new Boat(type, name, year, makeModel,
                    lengthFeet, purchasePrice, expenses);
            fleet.add(boat);
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Prompts user for a boat name and removes the first matching
     * {@link Boat} from the fleet, if any
     * Prints error message if no boat with provided name is identified
     */
    private void removeBoat() {
        System.out.print("Which boat do you want to remove?           : ");
        String name = keyboard.nextLine().trim();
        if (name.isEmpty()) {
            return;
        }
        Iterator<Boat> it = fleet.iterator();
        while (it.hasNext()) {
            Boat boat = it.next();
            if (boat.getName().equalsIgnoreCase(name)) {
                it.remove();
                System.out.println();
                return;
            }
        }
        System.out.println("Cannot find boat " + name);
        System.out.println();
    }

    /**
     * Prompts the user for a boat name and an expense amount
     * If the named boat exists and the new total expenses would not exceed
     * the boat's purchase price, the expense is recorded
     * Otherwise, the expense is rejected and the amount left to spend is reported
     */
    private void expenseBoat() {
        System.out.print("Which boat do you want to spend on?         : ");
        String name = keyboard.nextLine().trim();
        if (name.isEmpty()) {
            return;
        }
        Boat target = null;
        for (Boat boat : fleet) {
            if (boat.getName().equalsIgnoreCase(name)) {
                target = boat;
                break;
            }
        }
        if (target == null) {
            System.out.println("Cannot find boat " + name);
            System.out.println();
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        String line = keyboard.nextLine().trim();
        double amount;
        try {
            amount = Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return;
        }

        double current = target.getExpenses();
        double limit = target.getPurchasePrice();
        double newTotal = current + amount;

        if (newTotal <= limit) {
            target.addExpense(amount);
            System.out.printf("Expense authorized, $%.2f spent.%n",
                    target.getExpenses());
            System.out.println();
        } else {
            double remaining = limit - current;
            if (remaining < 0) {
                remaining = 0;
            }
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n",
                    remaining);
            System.out.println();
        }
    }
}