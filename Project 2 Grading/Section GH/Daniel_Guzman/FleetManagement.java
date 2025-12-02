import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
//---------------------------------------------------------------------------------------------
/**
 * Program manages fleet data and tracks the costs associated with each boat.
 * Uses an ArrayList for boat data storage and management.
 * @author Daniel Guzman
 * @version 1.0
 */
public class FleetManagement {

    private static final Scanner keyboard = new Scanner(System.in);
    static ArrayList<Boat> fleetList = new ArrayList<>();

//---------------------------------------------------------------------------------------------
    /**
     * Entry point of the Fleet Management program.
     * Loads fleet data from CSV file or DB file.
     * @param args command line arguments that may contain the CSV filename to load
     * @throws IOException in case there is an error reading or writing files
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("-".repeat(38));

        if (args.length > 0) {
            loadFromCSV(args[0], fleetList); // First runs with CSV file
        } else {
            fleetList = loadFleet("FleetData.db"); // Later runs with db file
        }

        displayMenu();
        boolean runMenu = true;
        while (runMenu) {

            String input = keyboard.nextLine().toUpperCase();
            char userChoice = input.charAt(0);

            switch (userChoice) {
                case 'P' -> {
                    printInventory();
                    displayMenu();
                }
                case 'A' -> {
                    System.out.printf("%-45s: ", "Please enter the new boat CSV data:");
                    addBoat(keyboard.nextLine());
                    displayMenu();
                }
                case 'R' -> {
                    System.out.printf("%-45s: ", "Which boat do you want to remove?");
                    removeBoat(keyboard.nextLine());
                    displayMenu();
                }
                case 'E' -> {
                    System.out.printf("%-45s: ", "Which boat do you want to spend on?");
                    String boatName = keyboard.nextLine();
                    Boat boat = findBoat(boatName); // Finds boat from fleetList

                    if (boat != null) {
                        System.out.printf("%-45s: ", "How much do you want to spend?");
                        requestExpense(boat, keyboard.nextDouble(), args);
                        keyboard.nextLine();
                    } else {
                        System.out.println("Cannot find boat " + boatName);
                        System.out.println();
                    }

                    displayMenu();
                }
                case 'X' -> {
                    System.out.println("Exiting the Fleet Management System");
                    saveFleet("FleetData.db", fleetList); //Saves current fleeList on exit
                    runMenu = false;
                }
                default -> System.out.printf("%-45s: ", "Invalid menu option, try again");
            }
        } // End of menu loop
    } // End of main method

//---------------------------------------------------------------------------------------------
    /**
     * Reads CSV file from command line argument and loads boats onto fleetList.
     * @param filename name of file to load
     * @param fleetList Array List to fill out
     * @throws IOException in case there is an error reading files
     */
    public static void loadFromCSV(String filename, ArrayList<Boat> fleetList) throws IOException {
        BufferedReader fromBufferedReader = new BufferedReader(new FileReader(filename));
        // Opens file for reading and wraps it in BufferedReader
        String line;

        while ((line = fromBufferedReader.readLine()) != null) { // Loops through every line of file

            if (line.trim().isEmpty())
                continue; // Skips empty lines

            fleetList.add(parseBoat(line)); //Adds boat after using parse method

        } // End of while loop

        fromBufferedReader.close();
    } // End of loadFromCSV method

//---------------------------------------------------------------------------------------------

    /**
     * Prints boat inventory including all associated data.
     */
    public static void printInventory() {
        // Initializes local variables for printing total
        double totalPaid = 0;
        double totalSpent = 0;

        System.out.println();

        System.out.println("Fleet Report:");
        for (Boat boat : fleetList) { // Loops through every Boat in fleetList
            System.out.println(boat.toString());
            totalPaid += boat.getPrice();
            totalSpent += boat.getExpenses();
        }

        System.out.printf(
                "    %-51s : Paid $ %10.2f : Spent $ %10.2f%n",
                "Total", totalPaid, totalSpent
        );

        System.out.println();
    } // End of printInventory method

//---------------------------------------------------------------------------------------------

    /**
     * Adds Boat to fleetList after using parse method.
     * @param boatInput string of user boat CSV data
     */
    public static void addBoat(String boatInput) {
        // Parses CSV data then adds to fleetList
        fleetList.add(parseBoat(boatInput));
        System.out.println();
    } // End of addBoat method

//---------------------------------------------------------------------------------------------

    /**
     * Removes Boat from fleetList after searching for it.
     * @param boatName string of user input name to remove
     */
    public static void removeBoat(String boatName) {
        // Get boat object using find method
        Boat boat = findBoat(boatName);

        if (boat != null) { // Checks for if boat exists
            fleetList.remove(boat);
            System.out.println("Removed " + boat.getName());
            System.out.println();
        } else {
            System.out.println("Cannot find boat " + boatName);
            System.out.println();
        }
    } // End of removeBoat method

//---------------------------------------------------------------------------------------------

    /**
     * Validates permission to spend money according to Commodore's policy.
     * @param boat object to access data
     * @param amount money user wants to spend
     */
    public static void requestExpense(Boat boat, double amount, String[] args){
        double totalSpent = 0;

        //Checks to see if safe to spend
        if (boat.getExpenses() + amount <= boat.getPrice()) {
            boat.addExpense(amount);

            if (args.length != 0) {
                for (Boat boats : fleetList) { // Loops through every Boat in fleetList
                    totalSpent += boats.getExpenses();
                }
            }
            else
                totalSpent += amount;

            System.out.printf("Expense authorized, $%.2f spent.%n", totalSpent);

        } else {
            double remaining = boat.getPrice() - boat.getExpenses();
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", remaining);
        }
        System.out.println();
    } // End of requestExpense method

//---------------------------------------------------------------------------------------------

    /**
     * Parses line of CSV data to create a Boat object.
     * @param csvData string of boat data to parse
     * @return Boat object with input data
     */
    public static Boat parseBoat(String csvData) {
        String[] parts = csvData.split(","); // Splits string into array separated by commas

        // Parses each part for new boat data
        Boat.BoatKind type = Boat.BoatKind.valueOf(parts[0].trim().toUpperCase());
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String model = parts[3].trim();
        int length = Integer.parseInt(parts[4].trim());
        double price = Double.parseDouble(parts[5].trim());

        // Creates boat object to add to fleetList
        return new Boat(type, name, year, model, length, price, 0.0);
    } // End of parseBoat method

//---------------------------------------------------------------------------------------------

    /**
     * Finds if boat exists in current fleetList.
     * @param boatName string from user
     * @return whether boat exists
     */
    public static Boat findBoat(String boatName) {
        for (Boat boat : fleetList) {
            if (boatName.equalsIgnoreCase(boat.getName())) { //Finds boat while ignoring case
                return boat;
            }
        } // End of for loop to search for boat

        return null;  // If no boat is found
    } // End of findBoat method

//---------------------------------------------------------------------------------------------

    /**
     * Saves fleet data into DB file.
     * @param filename string to name file
     * @param fleetList to write data from
     * @throws IOException in case there is an error writing file
     */
    public static void saveFleet(String filename, ArrayList<Boat> fleetList) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(fleetList); // Writes data from current fleetList
            System.out.println("Fleet saved to " + filename);
        }
    } // End of saveFleet method

//---------------------------------------------------------------------------------------------

    /**
     * Loads fleet data from file.
     * @param filename string to load file
     * @return ArrayList of fleet
     */
    public static ArrayList<Boat> loadFleet(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (ArrayList<Boat>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();  // Returns empty list if not found
        }
    } // End of loadFleet method

//---------------------------------------------------------------------------------------------

    /**
     * Displays menu options.
     */
    public static void displayMenu(){
        System.out.printf("%-45s: ", "(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it");
    } // End of displayMenu method

//---------------------------------------------------------------------------------------------
} //end of FleetManagement class