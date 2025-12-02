import java.io.*;

import java.util.*;

/**
 * FleetManagementSystem is the main class for managing the fleet of boats.
 * It loads fleet data from a CSV file (initializing run) or a serialized
 * database (subsequent runs), and allows the user to:
 *  - Print the fleet report
 *  - Add a new boat
 *  - Remove a boat
 *  - Record expenses for a boat
 *  - Exit and save the fleet
 * @author Zack White
 */
public class FleetManagementSystem {

    //Constant for database file name
    private static final String DATABASE_FILE = "FleetData.db";

    //ArrayList storing all boats
    private ArrayList<Boat> fleet;

    /**
     * Main method. Loads fleet data and starts the menu loop.
     *
     * @param args optional command-line arguments
     *             - If args[0] exists, treated as CSV file for initializing run
     */
    public static void main(String[] args) {
        FleetManagementSystem system;
        system = new FleetManagementSystem();
        system.run(args);
    }// end of main method

    /**
     * Starts the Fleet Management System program logic.
     *
     * This method is responsible for:
     *  - Determining whether to perform an initializing run (CSV file)
     *    or a subsequent run (load from database)
     *  - Loading the fleet data appropriately
     *  - Launching the main menu loop for user interaction
     *
     * The run method encapsulates the main workflow of the program,
     * separating it from the static main method.
     */
    private void run(String[] args) {
        Scanner keyboard;

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        fleet = new ArrayList<Boat>();

        if (args.length > 0) {
            loadFromCsv(args[0]);
            saveToDatabase();
        } else {
            loadFromDatabase();
        }

        keyboard = new Scanner(System.in);
        runMenu(keyboard);

        saveToDatabase();

        System.out.println("\nExiting the Fleet Management System");
    }// end of run method

    /**
     * Loads the fleet from the serialized database file (FleetData.db).
     * If the file does not exist, creates an empty fleet.
     */
    private void loadFromDatabase() {
        FileInputStream fileInput;
        ObjectInputStream objectInput;
        ArrayList<Boat> loadedFleet;

        try {
            fileInput = new FileInputStream(DATABASE_FILE);
            objectInput = new ObjectInputStream(fileInput);

            loadedFleet = (ArrayList<Boat>) objectInput.readObject();
            fleet = loadedFleet;

            objectInput.close();
        } catch (Exception error) {
            //----If file missing or unreadable, start empty fleet
            fleet = new ArrayList<Boat>();
        }// end of try-catch statement

    }// end of loadFromDatabase method

    /**
     * Saves the fleet to the serialized database file (FleetData.db).
     * This method should be called before exiting the program to persist data.
     */
    private void saveToDatabase() {
        FileOutputStream fileOutput;
        ObjectOutputStream objectOutput;

        try {
            fileOutput = new FileOutputStream(DATABASE_FILE);
            objectOutput = new ObjectOutputStream(fileOutput);

            objectOutput.writeObject(fleet);
            objectOutput.close();
        }catch (Exception error) {
            System.out.println("Error saving database.");

        }// end of try-catch statement

    }// end of saveToDatabase method

    /**
     * Loads fleet data from a CSV file.
     * Each line in the CSV is converted into a Boat object.
     *
     * @param filename the path to the CSV file
     */
    private void loadFromCsv(String filename) {
        File file;
        Scanner fileScanner;
        String line;

        try {
            file = new File(filename);
            fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine().trim();
                if (!line.equals("")) {
                    fleet.add(Boat.fromCsv(line));
                }//end of if statement

            }//end of while loop

            fileScanner.close();
        }catch (Exception error) {
            System.out.println("Error reading CSV file.");

        }// end of try-catch statement

    }// end of loadFromCsv

    /**
     * Runs the main menu loop, prompting the user for actions.
     * Accepts commands: Print, Add, Remove, Expense, Exit.
     */
    private void runMenu(Scanner keyboard) {
        String choice;
        boolean finished;

        finished = false;

        while (!finished) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine().trim().toUpperCase();

            if (choice.equals("P")) {
                printFleet();
            } else if (choice.equals("A")) {
                addBoat(keyboard);
            } else if (choice.equals("R")) {
                removeBoat(keyboard);
            } else if (choice.equals("E")) {
                recordExpense(keyboard);
            } else if (choice.equals("X")) {
                finished = true;
            } else {
                System.out.println("Invalid menu option, try again");
            }//end of if-else statement

        }// end of while loop

    }// end of runMenu method

    /**
     * Prints a report of all boats in the fleet, including:
     *  - Boat type
     *  - Name
     *  - Year and make/model
     *  - Length
     *  - Purchase price
     *  - Total expenses
     * Also prints totals for all boats at the end.
     */
    private void printFleet() {
        int i;
        Boat currentBoat;
        double totalPaid;
        double totalSpent;

        System.out.println("\nFleet report:");

        totalPaid = 0.0;
        totalSpent = 0.0;

        i = 0;
        while (i < fleet.size()) {
            currentBoat = fleet.get(i);

            System.out.println(currentBoat);
            totalPaid = totalPaid + currentBoat.getPurchasePrice();
            totalSpent = totalSpent + currentBoat.getTotalExpenses();

            i = i + 1;
        }// end of while-loop

        System.out.printf(
                "    %-45s : Paid $ %9.2f : Spent $ %9.2f\n\n",
                "Total", totalPaid, totalSpent
        );
    }// end of printFleet method

    /**
     * Adds a new boat to the fleet from a CSV-style string.
     *
     *@param keyboard representing a new boat by user
     */
    private void addBoat(Scanner keyboard) {
        String csvLine;
        Boat newBoat;

        System.out.print("Please enter the new boat CSV data          : ");
        csvLine = keyboard.nextLine().trim();

        newBoat = Boat.fromCsv(csvLine);
        fleet.add(newBoat);
    }// end of addBoat method

    /**
     * Removes a boat from the fleet by name (case-insensitive).
     *
     * @param keyboard the name of the boat to remove
     * @return true if the boat was found and removed, false otherwise
     */
    private void removeBoat(Scanner keyboard) {
        String boatName;
        Boat targetBoat;

        System.out.print("Which boat do you want to remove?           : ");
        boatName = keyboard.nextLine().trim();

        targetBoat = findBoat(boatName);

        if (targetBoat == null) {
            System.out.println("Cannot find boat " + boatName);
        } else {
            fleet.remove(targetBoat);
        }// end of if-else statement

    }// end of removeBoat method

    /**
     * Records an expense for a specific boat.
     * If the expense exceeds the purchase price minus existing expenses,
     * the expense is denied.
     *
     * @param keyboard the name of the boat entered by the user
     * @param keyboard the expense amount entered by the user
     * @return true if the expense was authorized, false otherwise
     */
    private void recordExpense(Scanner keyboard) {
        String boatName;
        Boat targetBoat;
        String amountText;
        double amount;
        double remaining;

        System.out.print("Which boat do you want to spend on?         : ");
        boatName = keyboard.nextLine().trim();

        targetBoat = findBoat(boatName);

        if (targetBoat == null) {
            System.out.println("Cannot find boat " + boatName);
            return;
        }// end of if statement

        System.out.print("How much do you want to spend?              : ");
        amountText = keyboard.nextLine().trim();
        amount = Double.parseDouble(amountText);

        if (targetBoat.authorizeExpense(amount)) {
            System.out.printf("Expense authorized, $%.2f spent.\n",
                    targetBoat.getTotalExpenses());
        } else {
            remaining = targetBoat.getPurchasePrice() -
                    targetBoat.getTotalExpenses();
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n",
                    remaining);
        }//end of if-else

    }// end of recordExpense method

    /**
     * Finds a boat in the fleet by name (case-insensitive).
     *
     * @param name the name of the boat to find
     * @return the Boat object if found, null otherwise
     */
    private Boat findBoat(String name) {
        int i;
        Boat currentBoat;

        i = 0;
        while (i < fleet.size()) {
            currentBoat = fleet.get(i);

            if (currentBoat.getName().equalsIgnoreCase(name)) {
                return currentBoat;
            }

            i = i + 1;
        }// end of while loop

        return null;
    }// end of findBoat method

}// end of FleetManagementSystem method