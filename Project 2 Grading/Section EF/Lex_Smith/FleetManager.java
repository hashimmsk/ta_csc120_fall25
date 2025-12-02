import java.util.Scanner;

/**
 * Main class for the Fleet Management System.
 */

public class FleetManager {

    private static final String DEFAULT_FILE = "FleetData.csv";
    private static Scanner keyboard = new Scanner(System.in);
    private static Fleet fleet = new Fleet();

    public static void main(String[] args) {

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        loadData(args);
        menuLoop();
        saveData();

        System.out.println("\nExiting the Fleet Management System");
    }

    /**
     * Loads data either from CSV (first run) or serialized DB (later runs).
     * @param args
     */

    private static void loadData(String[] args) {
        if (args.length > 0){
            fleet.loadFromCSV(args[0]);
        }
        else {
            fleet.loadFromCSV(DEFAULT_FILE);
        }
    }

    /**
     * Saves fleet to file.
     */

    private static void saveData() {
        fleet.saveToCSV(DEFAULT_FILE);
    }

    /**
     * Menu loop.
     */

    private static void menuLoop() {
        while (true) {
            System.out.println("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choice = keyboard.nextLine().trim().toUpperCase();

            switch (choice) {
                case "P":
                    fleet.printReport();;
                    break;

                case "A":
                    System.out.print("Please enter the new boat CSV data          : ");
                    addBoatFromCSV(keyboard.nextLine());
                    break;

                case "R":
                    System.out.print("Which boat do you want to remove?           : ");
                    if (!fleet.removeBoatByName(keyboard.nextLine())) {
                        System.out.println("Cannot find boat.");
                    }
                    break;

                case "E":
                    handleExpense();
                    break;

                case "X":
                    return;

                default:
                    System.out.println("Invalid menu option, try again");
            }
        }
    }

    /**
     * Adds a boat from CSV line.
     * @param csv
     */

    private static void addBoatFromCSV(String csv) {
        try {
            String[] p = csv.split(",");
            BoatType type = BoatType.valueOf(p[0].trim().toUpperCase());

            Boat b = new Boat(type, p[1].trim(), Integer.parseInt(p[2].trim()), p[3].trim(), Integer.parseInt(p[4].trim()), Double.parseDouble(p[5].trim()));
            fleet.addBoat(b);
        }
        catch (Exception e) {
            System.out.println("Invalid CSV format.");
        }
    }

    /**
     * Handles expense request.
     */

    private static void handleExpense() {
        System.out.print("Which boat do you want to spend on?          : ");
        Boat b = fleet.findBoat(keyboard.nextLine());

        if (b == null) {
            System.out.println("Cannot find boat.");
            return;
        }

        System.out.print("How much do you want to spend?               : ");
        double amt = Double.parseDouble(keyboard.nextLine());

        if (b.addExpense(amt)) {
            System.out.printf("Expense authorized, $%.2f spent.\n", b.getExpenses());
        }
        else {
            double left = b.getPurchasePrice() - b.getExpenses();
            System.out.printf("Expense not permitted, only $.2f left to spend.\n");
        }
    }
}
