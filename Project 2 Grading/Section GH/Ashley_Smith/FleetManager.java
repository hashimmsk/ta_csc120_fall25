import java.io.*;
import java.util.*;

/**
 * The FleetManager class allows the user to manage and make changes to a fleet of boats.
 * The first time the program runs, the program accepts the FleetData.csv file.
 * After the program runs, the fleet data is saved in the serialized file FleetData.db
 * On subsequent runs, the program automatically loads from the FleetData.db
 *
 * @author Ashley Howe-Smith
 * @version 1.0
 */
public class FleetManager {
    private static final String DATA_FILE = "FleetData.db";
    private static final String CSV_FILE = "FleetData.csv";
    private Fleet fleet;
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * Start of the Fleet Management System.
     * @param args optional command line arguments.
     * If provided and the serialized database file does not exist,
     * the argument should be FleetData.csv to initialize the fleet.
     */
    public static void main(String[] args) {
        FleetManager manager = new FleetManager();
        manager.run(args);
    }//end of main method

    /**
     * Executes the main program loop for the Fleet Management System.
     * @param args command line arguments
     */
    private void run(String[] args) {
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        File dbFile = new File(DATA_FILE);
        if (args.length > 0 && !dbFile.exists()) {
            fleet = loadFromCSV(args[0]);
            saveFleet();
        } else {
            fleet = loadFromDB();
        }//end of if else statement


        boolean running = true;
        while (running) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choice = keyboard.nextLine().trim().toUpperCase();
            switch (choice) {
                case "P":
                    fleet.printFleet();
                    break;
                case "A":
                    addBoat();
                    break;
                case "R":
                    removeBoat();
                    break;
                case "E":
                    handleExpense();
                    break;
                case "X":
                    System.out.println("\nExiting the Fleet Management System");
                    saveFleet();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
                    break;
            }//end of switch statement
        }//end of while loop
    }//end of run method

    /**
     * Loads fleet data from a CSV file.
     * @param filename the name of the CSV file to read.
     */
    private Fleet loadFromCSV(String filename) {
        Fleet newFleet = new Fleet();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) == '\uFEFF') {
                    line = line.substring(1);
                }//end of if statement
                line = line.trim();
                if (line.length() > 0) {
                    if (line.charAt(0) < 'A' || line.charAt(0) > 'Z') {
                        if (line.length() > 1) {
                            line = line.substring(1);
                        }//end of inner if statement
                    }//end of outer if statement
                    Boat boat = Boat.fromCSV(line);
                    if (boat != null) {
                        newFleet.addBoat(boat);
                    }//end of if statement
                }//end of if statement
            }//end of while loop
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }//end of try catch statement
        return newFleet;
    }//end of loadFromCSV method

/**
 * Loads fleet data from the serialized database file.
 */
    private Fleet loadFromDB() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (Fleet) ois.readObject();
        } catch (Exception e) {
            System.out.println("No existing database found, starting new fleet.");
            return new Fleet();
        }//end of try catch statement
    }//end of loadFromDB

    /**
     * Saves the current fleet to the serialized database file.
     */
    private void saveFleet() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Error saving fleet data: " + e.getMessage());
        }//end of try catch statement
    }//end of saveFleet method

/**
 * Prompts the user to add a new boat
 */
    private void addBoat() {
        System.out.print("Please enter the new boat CSV data          : ");
        String csv = keyboard.nextLine();
        Boat boat = Boat.fromCSV(csv);
        if (boat != null) {
            fleet.addBoat(boat);
        } else {
            System.out.println("Invalid CSV format.");
        }//end of if else statement
    }//end of addBoat method

    /**
     * Prompts the user to remove a boat by name.
     */
    private void removeBoat() {
        System.out.print("Which boat do you want to remove?           : ");
        String name = keyboard.nextLine();
        boolean removed = fleet.removeBoat(name);
        if (!removed) {
            System.out.println("Cannot find boat " + name);
        }//end of if statement
    }//end of removeBoat method

    /**
     * Handles recording an expense for a specific boat.
     * Ensures total expenses never exceed the boat's purchase price.
     */
    private void handleExpense() {
        System.out.print("Which boat do you want to spend on?         : ");
        String name = keyboard.nextLine();
        Boat boat = fleet.findBoat(name);
        if (boat == null) {
            System.out.println("Cannot find boat " + name);
            return;
        }//end of if statement
        System.out.print("How much do you want to spend?              : ");
        double amt = Double.parseDouble(keyboard.nextLine());
        double newTotal = boat.getExpenses() + amt;
        if (newTotal > boat.getPurchasePrice()) {
            double left = boat.getPurchasePrice() - boat.getExpenses();
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", left);
        } else {
            boat.addExpense(amt);
            System.out.printf("Expense authorized, $%.2f spent.%n", boat.getExpenses());
        }//end of if else statement
    }//end of handleExpense method

}//end of FleetManager class
