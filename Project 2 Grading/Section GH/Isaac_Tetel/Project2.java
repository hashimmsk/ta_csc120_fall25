import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the Fleet Management System. Handles loading data,
 * user interaction, menu processing, and saving data on exit.
 *
 * This class must contain the only Scanner object in the program.
 *
 * @author Isaac Tetel
 * @version 1
 * @see FleetManager
 * @see Boat
 */
public class Project2 {

    /**
     * Program entry point. Loads fleet data either from a CSV file
     * (initial run) or from a serialized database file (subsequent runs),
     * then provides a menu for interacting with the fleet.
     *
     * @param args Command line arguments (CSV filename optional)
     */
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        FleetManager manager = new FleetManager();

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        if (args.length == 1) {
            try {
                loadFromCSV(args[0], manager);
                saveToDB(manager);
            } catch (Exception e) {
                System.out.println("Error loading CSV file.");
                return;
            }
        } else {
            File dbFile = new File("FleetData.db");
            if (dbFile.exists()) {
                try {
                    loadFromDB(manager);
                } catch (Exception e) {
                    System.out.println("Error reading database, exiting.");
                    return;
                }
            } else {
                System.out.println("No saved data found. Exiting.");
                return;
            }
        }

        String choice = "";
        while (!choice.equalsIgnoreCase("X")) {

            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine();

            if (choice.equalsIgnoreCase("P")) {
                manager.printFleetReport();
            }

            else if (choice.equalsIgnoreCase("A")) {
                System.out.print("Please enter the new boat CSV data          : ");
                String line = keyboard.nextLine();
                manager.addBoatFromCSV(line);
                System.out.println();
            }

            else if (choice.equalsIgnoreCase("R")) {
                System.out.print("Which boat do you want to remove?           : ");
                String name = keyboard.nextLine().trim();
                boolean removed = manager.removeBoat(name);
                if (!removed) {
                    System.out.println("Cannot find boat " + name);
                    System.out.println();
                }
            }

            else if (choice.equalsIgnoreCase("E")) {
                System.out.print("Which boat do you want to spend on?         : ");
                String name = keyboard.nextLine().trim();
                Boat b = manager.findBoat(name);

                if (b == null) {
                    System.out.println("Cannot find boat " + name);
                    System.out.println();
                } else {
                    System.out.print("How much do you want to spend?              : ");
                    double amt = Double.parseDouble(keyboard.nextLine());

                    boolean ok = b.addExpense(amt);

                    if (ok) {
                        System.out.printf("Expense authorized, $%.2f spent.%n%n", b.getExpenses());
                    } else {
                        double left = b.getPurchasePrice() - b.getExpenses();
                        System.out.printf("Expense not permitted, only $%.2f left to spend.%n%n", left);
                    }
                }
            }

            else if (!choice.equalsIgnoreCase("X")) {
                System.out.println("Invalid menu option, try again");
            }
        }

        try {
            saveToDB(manager);
        } catch (IOException e) {
            System.out.println("Error saving fleet data.");
        }

        System.out.println();
        System.out.println("Exiting the Fleet Management System");
    }

    /**
     * Loads fleet data from a CSV file.
     *
     * @param filename Name of the CSV file
     * @param manager FleetManager to populate
     * @exception IOException If the file cannot be read
     */
    public static void loadFromCSV(String filename, FleetManager manager) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            manager.addBoatFromCSV(line);
        }

        br.close();
    }

    /**
     * Saves fleet data to a serialized database file.
     *
     * @param manager FleetManager whose fleet will be saved
     * @exception IOException If the file cannot be written
     */
    public static void saveToDB(FleetManager manager) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("FleetData.db"));
        out.writeObject(manager.getFleet());
        out.close();
    }

    /**
     * Loads fleet data from a serialized database file.
     *
     * @param manager FleetManager to receive the loaded fleet
     * @exception IOException If reading fails
     * @exception ClassNotFoundException If deserialization fails
     */
    public static void loadFromDB(FleetManager manager)
            throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("FleetData.db"));
        ArrayList<Boat> fleet = (ArrayList<Boat>) in.readObject();
        manager.setFleet(fleet);
        in.close();
    }
}
