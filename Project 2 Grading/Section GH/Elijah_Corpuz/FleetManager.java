import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Elijah Corpuz
 * The FleetManager class manages a collection of Boat objects.
 * It allows the user to load boats from a CSV file or database,
 * and provides menu options to print, add, remove, and add expenses
 * to boats. The fleet is saved automatically when quitting.
 */
public class FleetManager {

    /** File used to store serialized fleet data */
    private static final String DB_FILE = "FleetData.db";

    /**
     *
     * Loads data, displays the menu, and controls program execution.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        ArrayList<Boat> fleet = new ArrayList<>();
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        if (args.length > 0) {

            String csvFile = args[0];


            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;

                while ((line = br.readLine()) != null) {
                    fleet.add(Boat.fromCSV(line));
                }



            } catch (Exception e) {
                System.out.println("ERROR: Could not read CSV file.");
                System.out.println(e.getMessage());
                return;
            }

        } else {

            File db = new File(DB_FILE);

            if (db.exists()) {
                try (ObjectInputStream ois =
                             new ObjectInputStream(new FileInputStream(db))) {

                    fleet = (ArrayList<Boat>) ois.readObject();


                } catch (Exception e) {
                    System.out.println("ERROR: Database exists but is corrupted.");
                    System.out.println("Delete FleetData.db and rerun with CSV.");
                    return;
                }

            } else {
                System.out.println("ERROR: No database found.");
                System.out.println("You must run once with a CSV file first!");
                System.out.println("Example:");
                System.out.println("java FleetManager FleetData.csv");
                return;
            }
        }

        String option;

        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            option = keyboard.nextLine().trim().toUpperCase();

            switch (option) {
                case "P":
                    printFleet(fleet);
                    break;

                case "A":
                    addBoat(fleet, keyboard);
                    break;

                case "R":
                    removeBoat(fleet, keyboard);
                    break;

                case "E":
                    addExpense(fleet, keyboard);
                    break;

                case "X":
                    try (ObjectOutputStream oos =
                                 new ObjectOutputStream(new FileOutputStream(DB_FILE))) {

                        oos.writeObject(fleet);

                    } catch (Exception e) {
                        System.out.println("Error saving fleet.");
                    }

                    System.out.println("\nExiting the Fleet Management System");
                    break;

                default:
                    System.out.println("Invalid menu option.\n");
            }

        } while (!option.equals("X"));

    } // end of main

    /**
     * Prints the data of the fleet
     * @param fleet
     */
    private static void printFleet(ArrayList<Boat> fleet) {

        System.out.println("\nFleet report:");

        double totalPaid = 0.0;
        double totalSpent = 0.0;

        for (Boat b : fleet) {
            totalPaid += b.getPrice();
            totalSpent += b.getExpenses();

            System.out.printf(
                    "    %-7s %-20s %-4d %-12s %2d' : Paid $ %10.2f : Spent $ %10.2f%n",
                    b.getType(),
                    b.getName(),
                    b.getYear(),
                    b.getModel(),
                    (int) b.getLengthInFeet(),
                    b.getPrice(),
                    b.getExpenses()
            );
        }

        System.out.printf(
                "    %-44s : Paid $ %10.2f : Spent $ %10.2f%n%n",
                "Total",
                totalPaid,
                totalSpent
        );

    } // end of printFleet


    /**
     * Allows user to add a boat to the fleet list
     * @param fleet
     * @param keyboard
     */
    private static void addBoat(ArrayList<Boat> fleet, Scanner keyboard) {

        System.out.print("Enter new boat CSV data : ");
        String line = keyboard.nextLine();

        try {
            Boat b = Boat.fromCSV(line);
            fleet.add(b);
        } catch (Exception e) {
            System.out.println("Invalid CSV format.");
        }

    } // end of addBoat


    /**
     * Searches for boat the user enters in the fleet list
     * Returns null if boat is not found
     * @param fleet
     * @param name
     * @return
     */
    private static Boat findBoat(ArrayList<Boat> fleet, String name) {

        for (Boat b : fleet) {
            if (b.getName().equalsIgnoreCase(name))
                return b;
        }

        return null;

    } // end of findBoat


    /**
     * Allows user to remove a boat from the fleet list
     * Validates if a boat is not in the list
     * @param fleet
     * @param keyboard
     */
    private static void removeBoat(ArrayList<Boat> fleet, Scanner keyboard) {

        System.out.print("Which boat do you want to remove? : ");
        String name = keyboard.nextLine();

        Boat b = findBoat(fleet, name);

        if (b == null) {
            System.out.println("Cannot find boat " + name);
            return;
        }

        fleet.remove(b);
        System.out.println("Boat removed.");

    } // end of removeBoat


    /**
     * Adds expenses onto the boat the user selects
     * Validates if boat is not in list
     * @param fleet
     * @param keyboard
     */
    private static void addExpense(ArrayList<Boat> fleet, Scanner keyboard) {

        System.out.print("Which boat do you want to spend on? : ");
        String name = keyboard.nextLine();

        Boat b = findBoat(fleet, name);

        if (b == null) {
            System.out.println("Cannot find boat " + name);
            return;
        }

        System.out.print("How much do you want to spend? : ");
        double amount = Double.parseDouble(keyboard.nextLine());

        if (amount + b.getExpenses() > b.getPrice()) {
            System.out.printf(
                    "Expense not permitted, only $%.2f left to spend.%n",
                    b.remainingBudget()
            );
            return;
        }

        b.addExpense(amount);
        System.out.printf(
                "Expense authorized, $%.2f spent.%n",
                b.getExpenses()
        );

    } // end of addExpense

} // end of FleetManager class
