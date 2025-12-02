import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * FleetManager - main class that runs the Fleet Management System.
 *
 * Responsible for:
 * - loading initial fleet data (CSV or serialized DB)
 * - menu loop (only class with Scanner)
 * - saving fleet to FleetData.db on exit
 */
public class FleetManager {
    private static final String DB_FILENAME = "FleetData.db";

    public static void main(String[] args) {
        Fleet fleet = null;

        // If a command line argument provided, treat it as CSV file to initialize fleet
        if (args.length > 0) {
            String csvFile = args[0];
            try {
                fleet = loadFromCSV(csvFile);
                System.out.println("Welcome to the Fleet Management System");
                System.out.println("--------------------------------------");
                System.out.println();
                // After initializing from CSV, we will save to DB on exit.
            } catch (IOException e) {
                System.err.println("Failed to load CSV file: " + e.getMessage());
                return;
            }
        } else {
            // Try to read DB
            try {
                fleet = loadFromDB(DB_FILENAME);
                System.out.println("Welcome to the Fleet Management System");
                System.out.println("--------------------------------------");
                System.out.println();
            } catch (IOException | ClassNotFoundException e) {
                // No DB or read failure -> start empty fleet
                System.out.println("Welcome to the Fleet Management System");
                System.out.println("--------------------------------------");
                System.out.println();
                fleet = new Fleet();
            }
        }

        // Scanner only in main class
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choice = sc.nextLine().trim();
            if (choice.isEmpty()) {
                System.out.println("Invalid menu option, try again");
                continue;
            }
            char cmd = Character.toUpperCase(choice.charAt(0));
            switch (cmd) {
                case 'P':
                    printReport(fleet);
                    break;
                case 'A':
                    System.out.print("Please enter the new boat CSV data          : ");
                    String line = sc.nextLine();
                    try {
                        Boat b = parseBoatCSVLine(line);
                        fleet.addBoat(b);
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Failed to add boat: " + ex.getMessage());
                    }
                    break;
                case 'R':
                    System.out.print("Which boat do you want to remove?           : ");
                    String remName = sc.nextLine();
                    Boat removed = fleet.removeBoatByName(remName);
                    if (removed == null) System.out.println("Cannot find boat " + remName);
                    break;
                case 'E':
                    System.out.print("Which boat do you want to spend on?         : ");
                    String spendName = sc.nextLine();
                    System.out.print("How much do you want to spend?              : ");
                    String amtStr = sc.nextLine();
                    double amt;
                    try {
                        amt = Double.parseDouble(amtStr);
                        Fleet.AuthorizationResult res = fleet.authorizeExpense(spendName, amt);
                        if (!res.authorized) {
                            if (res.message != null) {
                                // Not found or specific message
                                if (res.message.startsWith("Cannot find")) System.out.println(res.message);
                                else System.out.printf("%s%n", res.message);
                            } else {
                                System.out.printf("Expense not permitted, only $%.2f left to spend.%n", res.amountOrRemaining);
                            }
                        } else {
                            System.out.printf("Expense authorized, $%.2f spent.%n", res.amountOrRemaining);
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid numeric input for amount.");
                    }
                    break;
                case 'X':
                    // Save DB and exit
                    try {
                        saveToDB(fleet, DB_FILENAME);
                    } catch (IOException ex) {
                        System.err.println("Failed to save fleet data: " + ex.getMessage());
                    }
                    System.out.println();
                    System.out.println("Exiting the Fleet Management System");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
            }
        }
        sc.close();
    }

    /**
     * Print a formatted fleet report similar to sample output.
     * @param fleet fleet to print
     */
    private static void printReport(Fleet fleet) {
        System.out.println();
        System.out.println("Fleet report:");
        List<Boat> boats = fleet.getBoatList();
        // Sort by insertion order (already), but print each line with aligned columns
        for (Boat b : boats) {
            // Example: "    POWER   Big Brother          2019 Mako        20' : Paid $ 12989.56 : Spent $     0.00"
            String typeStr = String.format("%-7s", b.getType().toString());
            String nameStr = String.format("%-20s", b.getName());
            String yearMake = String.format("%4d %s", b.getYear(), b.getMakeModel());
            String length = String.format("%3d'", b.getLengthFeet());
            System.out.printf("    %s %s %s %8s : Paid $ %8.2f : Spent $ %8.2f%n",
                    typeStr, nameStr, b.getYear(), String.format("%-12s", b.getMakeModel() + " " + b.getLengthFeet() + "'"),
                    b.getPurchasePrice(), b.getExpenses());
        }
        System.out.printf("    Total                                             : Paid $ %8.2f : Spent $ %8.2f%n",
                fleet.getTotalPaid(), fleet.getTotalSpent());
        System.out.println();
    }

    // -------------------------
    // File handling (CSV & DB)
    // -------------------------

    /**
     * Load fleet data from a CSV file.
     * Each line: TYPE,Name,Year,MakeModel,LengthFeet,PurchasePrice
     * @param csvFile path to CSV
     * @return Fleet populated
     * @throws IOException on IO error
     */
    public static Fleet loadFromCSV(String csvFile) throws IOException {
        Fleet fleet = new Fleet();
        Path p = Path.of(csvFile);
        if (!Files.exists(p)) throw new IOException("CSV file does not exist: " + csvFile);
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                Boat b = parseBoatCSVLine(line);
                fleet.addBoat(b);
            }
        }
        return fleet;
    }

    /**
     * Load fleet from serialized DB file.
     * @param dbFile filename
     * @return Fleet read
     * @throws IOException on read error
     * @throws ClassNotFoundException on deserialization error
     */
    public static Fleet loadFromDB(String dbFile) throws IOException, ClassNotFoundException {
        Path p = Path.of(dbFile);
        if (!Files.exists(p)) throw new IOException("DB file not present");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile))) {
            Object obj = ois.readObject();
            if (obj instanceof Fleet) {
                return (Fleet) obj;
            } else {
                throw new IOException("DB file does not contain Fleet object");
            }
        }
    }

    /**
     * Save fleet to DB (serialized).
     * @param fleet fleet to save
     * @param dbFile filename
     * @throws IOException on write error
     */
    public static void saveToDB(Fleet fleet, String dbFile) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dbFile))) {
            oos.writeObject(fleet);
        }
    }

    /**
     * Parse a CSV line describing a boat and return a Boat object.
     * Expected fields: TYPE,Name,Year,MakeModel,LengthFeet,PurchasePrice
     *
     * Example: SAILING,Moon Glow,1973,Bristol,30,5500.00
     *
     * @param line csv line
     * @return Boat
     */
    public static Boat parseBoatCSVLine(String line) {
        if (line == null) throw new IllegalArgumentException("CSV line is null");
        String[] parts = line.split(",", -1);
        if (parts.length < 6) throw new IllegalArgumentException("CSV line must have 6 fields: " + line);
        String typeStr = parts[0].trim();
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String makeModel = parts[3].trim();
        int lengthFeet = Integer.parseInt(parts[4].trim());
        double price = Double.parseDouble(parts[5].trim());
        BoatType type = BoatType.fromString(typeStr);
        // Basic bounds validation (per hints)
        if (lengthFeet < 0 || lengthFeet > 100) throw new IllegalArgumentException("Invalid length: " + lengthFeet);
        if (price < 0 || price >= 1_000_000) throw new IllegalArgumentException("Invalid purchase price: " + price);
        return new Boat(type, name, year, makeModel, lengthFeet, price);
    }
}
