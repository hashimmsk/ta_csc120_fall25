import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FleetManager {

    private static final String DB_FILENAME = "FleetData.db";

    private ArrayList<Boat> fleet = new ArrayList<>();
    private final Scanner keyboard;

    public FleetManager(Scanner keyboard) {
        this.keyboard = keyboard;
    }

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        FleetManager manager = new FleetManager(kb);

        manager.loadInitialData(args);
        manager.runMenu();
        manager.saveFleet();

        System.out.println("\nExiting the Fleet Management System");
        kb.close();
    }// end of main method


    /**
     * loads intial data
     * @param args args the command-line arguments passed to the program
     */
    private void loadInitialData(String[] args) {
        if (args.length > 0) {
            loadFromCSVOrWarn(args[0]);
            saveFleet();   // prime DB for next run
        } else {
            loadFromDBOrStartEmpty();
        }
    }// end of loadInitialData method

    /**
     * Attempts to load fleet data from the specified CSV file.
     * @param csvName the path to load the CSV file
     */
    private void loadFromCSVOrWarn(String csvName) {
        File csv = new File(csvName);
        if (!csv.exists()) {
            System.out.println("CSV file not found: " + csvName);
            return;
        }
        try {
            loadFromCsv(csv);
        } catch (IOException e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }
    }// end of loadFromCSVOrWarn method

    /**
     * loads the saved data
     */
    private void loadFromDBOrStartEmpty() {
        File db = new File(DB_FILENAME);
        if (!db.exists()) return;

        try {
            loadFromDb(db);
        } catch (Exception e) {
            System.out.println("Could not read DB file; starting empty.");
        }
    }// end of loadFromDBOrStartEmpty method


    /**
     * Loads fleet data from a CSV file
     * @param csvFile  the CSV file containing boat definitions
     * @throws IOException if the file cannot be read
     */
    public void loadFromCsv(File csvFile) throws IOException {
        fleet.clear();
        for (String line : Files.readAllLines(csvFile.toPath())) {
            if (!line.isBlank()) {
                try { fleet.add(parseBoatCsvLine(line)); }
                catch (Exception e) { System.out.println("Bad CSV line: " + line); }
            }
        }
    } // end of loadFromCsv method

    /**
     *
     * @param line reads the boats paramters from command line and creats objetct
     * @return boat object
     */
    private Boat parseBoatCsvLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 6) throw new IllegalArgumentException("Too few fields");

        return new Boat(
                BoatType.fromString(p[0]),
                p[1].trim(),
                Short.parseShort(p[2].trim()),
                p[3].trim(),
                Byte.parseByte(p[4].trim()),
                Double.parseDouble(p[5].trim())
        );
    }// end of parseBoatCsvLine method

    /**
     * Loads the fleet data
     * @param f the file containing the fleet data
     * @throws IOException if there is an error reading the file or the file that cannot be opened
     * @throws ClassNotFoundException if the class doesn't match
     */
    @SuppressWarnings("unchecked")
    public void loadFromDb(File f) throws IOException, ClassNotFoundException {
        try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(f))) {
            fleet = (ArrayList<Boat>) o.readObject();
        }
    } //end of loadFromDb method

    /**
     * saves the fleet to the file
     */
    public void saveFleet() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DB_FILENAME))) {
            out.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Failed to save fleet: " + e.getMessage());
        }
    }// end of saveFleet method


    /**
     * runs the menu loop for the data
     */
    public void runMenu() {
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        while (true) {
            System.out.print("\n(P)rint (A)dd (R)emove (E)xpense e(X)it: ");
            char c = Character.toUpperCase(keyboard.nextLine().trim().charAt(0));

            switch (c) {
                case 'P':
                    printFleet();
                    break;
                case 'A':
                    handleAdd();
                    break;
                case 'R' :
                    handleRemove();
                    break;
                case 'E' :
                    handleExpense();
                    break;
                case 'X' :
                { return; }
                default :
                    System.out.println("Invalid option.");
            }
        }
    }// end of runMenu method


    /**
     * adds a boat to the arraylist
     */
    private void handleAdd() {
        System.out.print("Enter boat CSV: ");
        try {
            fleet.add(parseBoatCsvLine(keyboard.nextLine()));
        } catch (Exception e) {
            System.out.println("Invalid CSV: " + e.getMessage());
        }
    } // end of handleAdd method

    /**
     * Removes the boat
     */
    private void handleRemove() {
        System.out.print("Which boat do you want to remove? ");
        String name = keyboard.nextLine();
        Boat b = findBoat(name);
        if (b == null) System.out.println("Cannot find boat " + name);
        else fleet.remove(b);
    }// end of handleRemove method

    /**
     * checks if the boat can be spent on and if can adds it to totalSpent
     */
    private void handleExpense() {
        System.out.print("Which boat do you want to spend on? ");
        String name = keyboard.nextLine();
        Boat b = findBoat(name);
        if (b == null) { System.out.println("Cannot find boat "+ name); return; }

        System.out.print("How much do you wanna spend: ");
        double amt = Double.parseDouble(keyboard.nextLine().trim());


        if (b.addExpense(amt)) {
            System.out.printf("Expense authorized, you have spent: $%.2f%n ", b.getExpenses());
        }
        else {
            System.out.printf("Not permitted, $%.2f left.%n", b.remainingBudget());
        }
    } // end of handleExpense method


    /**
     *
     * @param name gets the input of the boat
     * @return finds if name is in the arraylist and returns null if not
     */
    private Boat findBoat(String name) {
        String target = name.trim().toLowerCase();
        return fleet.stream()
                .filter(b -> b.getName().trim().toLowerCase().equals(target))
                .findFirst()
                .orElse(null);
    }// end of findBoat method



    /**
     * prints the array list of fleet data
     */
    public void printFleet() {
        System.out.println("\nFleet report:");
        double totalPaid = 0, totalSpent = 0;


        for (Boat b : fleet) {
            System.out.println(b.toReportString());
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        System.out.printf("    Total : Paid $ %8.2f : Spent $ %9.2f%n",
                totalPaid, totalSpent);
    }// end of printFleet


}// end of FleetManager class
