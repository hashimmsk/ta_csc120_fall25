import java.io.*;
import java.util.Scanner;

/**
 * Main class that runs the Fleet Management System.
 */
public class FleetManagementSystem {

    private static final String DB_FILE = "FleetData.db";

    /**
     * Loads Fleet object from .db or initializes from CSV.
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Fleet fleet;

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        // If argument provided → initialize from CSV
        if (args.length == 1) {
            fleet = loadFromCSV(args[0]);
            saveDB(fleet);
        }
        // Otherwise → load from .db
        else {
            fleet = loadDB();
            if (fleet == null) {
                System.out.println("No saved fleet found. Exiting.");
                return;
            }
        }

        char option;
        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            option = keyboard.nextLine().trim().toUpperCase().charAt(0);

            switch (option) {

                case 'P':
                    System.out.println(fleet.generateReport());
                    break;

                case 'A':
                    System.out.print("Please enter the new boat CSV data          : ");
                    String line = keyboard.nextLine();
                    Boat newBoat = parseCSVBoat(line);
                    fleet.addBoat(newBoat);
                    break;

                case 'R':
                    System.out.print("Which boat do you want to remove?           : ");
                    String toRemove = keyboard.nextLine();
                    if (!fleet.removeBoat(toRemove)) {
                        System.out.println("Cannot find boat " + toRemove);
                    }
                    break;

                case 'E':
                    System.out.print("Which boat do you want to spend on?         : ");
                    String name = keyboard.nextLine();
                    Boat b = fleet.findBoat(name);

                    if (b == null) {
                        System.out.println("Cannot find boat " + name);
                        break;
                    }

                    System.out.print("How much do you want to spend?              : ");
                    double amt = Double.parseDouble(keyboard.nextLine());

                    double remaining = b.remainingBudget();
                    if (amt <= remaining) {
                        b.addExpense(amt);
                        System.out.printf("Expense authorized, $%.2f spent.\n", b.getExpenses());
                    } else {
                        System.out.printf("Expense not permitted, only $%.2f left to spend.\n", remaining);
                    }
                    break;

                case 'X':
                    System.out.println("\nExiting the Fleet Management System");
                    saveDB(fleet);
                    break;

                default:
                    System.out.println("Invalid menu option, try again");
            }

        } while (option != 'X');
    }

    /** Parse CSV for adding a boat */
    private static Boat parseCSVBoat(String csv) {
        String[] parts = csv.split(",");
        BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String make = parts[3].trim();
        int length = Integer.parseInt(parts[4].trim());
        double price = Double.parseDouble(parts[5].trim());
        return new Boat(type, name, year, make, length, price);
    }

    /** Load fleet from .csv file */
    private static Fleet loadFromCSV(String FleetData) {
        Fleet fleet = new Fleet();

        try (BufferedReader br = new BufferedReader(new FileReader(FleetData))) {
            String line;

            while ((line = br.readLine()) != null) {
                fleet.addBoat(parseCSVBoat(line));
            }

        } catch (IOException e) {
            System.out.println("Error reading CSV file.");
        }

        return fleet;
    }

    /** Save fleet to .db file */
    private static void saveDB(Fleet fleet) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DB_FILE))) {
            out.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Error saving database.");
        }
    }

    /** Load fleet from .db file */
    private static Fleet loadDB() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DB_FILE))) {
            return (Fleet) in.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
