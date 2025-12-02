import java.util.Scanner;
import java.util.*;
import java.io.*;

/**
 * Read boat data from file, manage boat data, and re-write boat data when finished
 * @author Kush Kumar
 */
public class BoatManager implements Serializable {
    /**
     * Global Scanner object to use keyboard
     */
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * The main method
     * @param args The file name is passed in from the command line
     */

    public static void main(String[] args) {
        //Variable to hold userInput and assist with program
        ArrayList<Boat> boatArrayList = new ArrayList<>();
        String fileName;
        boolean exitRequested;
        String userInput;
        char command;
        boolean checkUserInput;

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        //Loading boat data from file
        try {
            if (args.length > 0) {
                fileName = args[0];
                readBoatFileFromCSV(fileName, boatArrayList);
            } else {
                readBoatFileSerialized("FleetData.db", boatArrayList);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        exitRequested = false;

        //Menu of options for user to use to manipulate boat data

        while (!exitRequested) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it: ");
            userInput = keyboard.nextLine().toLowerCase();

            if (userInput.isEmpty()) {
                System.out.println("No input detected, try again.");
            }

            command = userInput.charAt(0);

            switch (command) {
                case 'p':
                    printBoatList(boatArrayList);
                    System.out.println();
                    break;
                case 'a':
                    addBoatToList(boatArrayList);
                    System.out.println();
                    break;
                case 'r':
                    removeBoatFromList(boatArrayList);
                    System.out.println();
                    break;
                case 'e':
                    expenseBoat(boatArrayList);
                    System.out.println();
                    break;
                case 'x':
                    try {
                        saveBoatData(boatArrayList);
                    } catch (IOException e) {
                        System.out.println("Error saving data: " + e.getMessage());
                    }
                    System.out.println("Exiting the Fleet Management System");
                    exitRequested = true;
                    break;
                default:
                    System.out.println("Invalid menu option, try again.");
            }
        }
    } // end of the main method

    /**
     * Loads boat data from CSV file specified in command line
     * @param fileName The fileName from the command line
     * @param boatArrayList The boat array list created in main
     * @throws IOException Throws an IO Exception if file is not read correctly
     */

    public static void readBoatFileFromCSV (String fileName, ArrayList<Boat> boatArrayList) throws IOException {
        //Local variables for reading file and to temporarily store file data
        BufferedReader fromBufferedReader;
        String oneLine;
        String fields [];

        fromBufferedReader = new BufferedReader(new FileReader(fileName));

        oneLine = fromBufferedReader.readLine();

        //Read lines from file and create a boat object from each line
        while (oneLine != null) {
            fields = oneLine.split(",");

            Boat.BoatType type = Boat.BoatType.valueOf(fields[0].toUpperCase());

            Boat boat = new Boat(type, fields[1], Integer.parseInt(fields[2]), fields [3], Integer.parseInt(fields [4]), Double.parseDouble(fields [5]), 0.00);

            boatArrayList.add(boat);

            oneLine = fromBufferedReader.readLine();
        } // end of the while loop
        fromBufferedReader.close();

    } // end of the readBoatFile method

    /**
     * Reloads serialized boat data saved from a previous run
     * @param fileName fileName is "FleetData.db"
     * @param boatArrayList The boat array list created in main
     * @throws IOException Throws an IO exception if file is not read correctly
     * @throws ClassNotFoundException Throws a class not found exception if boat array list objects aren't read sp
     */

    public static void readBoatFileSerialized (String fileName, ArrayList<Boat> boatArrayList) throws IOException, ClassNotFoundException{
        //Local variables for reading file and to temporarily hold boat data
        ObjectInputStream readFile = null;
        ArrayList<Boat> loadedList;

        //Load boat data and cast as boats and then add to ArrayList
        try {
            readFile = new ObjectInputStream(new FileInputStream(fileName));
            loadedList = (ArrayList<Boat>) readFile.readObject();

            boatArrayList.addAll(loadedList);
        }
        finally {
            if (readFile != null) {
                readFile.close();
            }
        }
    } // end of the readBoatFileSerialized Object

    /**
     * Prints boat data
     * @param boatArrayList The boat array list created in main
     */

    public static void printBoatList (ArrayList<Boat> boatArrayList) {
        //Local variables to allow for proper printing of boats
        int index;
        double totalBoatPayment;
        double totalBoatExpenses;

        totalBoatPayment = 0;
        totalBoatExpenses = 0;

        System.out.println("Fleet report: ");

        //Print out each boat using toString method in Boat class
        for (index = 0; index < boatArrayList.size(); index ++) {
            System.out.println(boatArrayList.get(index));
            totalBoatPayment = totalBoatPayment + boatArrayList.get(index).getBoatPurchasePrice();
            totalBoatExpenses = totalBoatExpenses + boatArrayList.get(index).getBoatExpenses();
        }

        //Print out total boat payments and boat expenses
        System.out.printf(
                "%-48s : Paid $ %8.2f : Spent $ %8.2f",
                "Total",
                totalBoatPayment,
                totalBoatExpenses
        );
    } // end of the printBoatList method

    /**
     * Adds a boat, given as a line of a CSV file, to the boat menu
     * @param boatArrayList The boat array list created in main
     */

    public static void addBoatToList (ArrayList<Boat> boatArrayList) {
        //Local variables to hold temporarily hold new boat information
        String newBoat;
        String fields [];

        //Prompts user to enter boat data
        System.out.print("Please enter the new boat CSV data         : ");

        newBoat = keyboard.nextLine();

        fields = newBoat.split(",");

        //Separates user input into proper fields, creates boat objects, and adds boat to ArrayList
        Boat.BoatType type = Boat.BoatType.valueOf(fields[0].toUpperCase());

        Boat boat = new Boat(type, fields[1], Integer.parseInt(fields[2]), fields [3], Integer.parseInt(fields [4]), Double.parseDouble(fields [5]), 0.00);

        boatArrayList.add(boat);

    } // end of the AddBoatToList method

    /**
     * Removes a specified boat from the boat menu
     * @param boatArrayList The boat array list created in main
     */

    public static void removeBoatFromList (ArrayList<Boat> boatArrayList) {
        //Local variables to hold information about boat to be removed
        String boatToRemove;
        int index;
        boolean checkRemoval;

        //Prompts user to enter boat to be removed
        System.out.print("Which boat do you want to remove?          : ");

        boatToRemove = keyboard.nextLine();

        checkRemoval = false;

        //Loops through ArrayList to find boat to remove
        for (index = 0; index < boatArrayList.size(); index++) {
            if (boatToRemove.equalsIgnoreCase(boatArrayList.get(index).getBoatName())) {
                boatArrayList.remove(index);
                checkRemoval = true;
            } // end of the if statement

        } // end of the for loop

        //If boat is not found, lets the user know
        if (!checkRemoval) {
            System.out.println("Cannot find boat " + boatToRemove);
        }
    } // end of the removeBoatFromList method

    /**
     * Adds to boat expenses from a boat in the menu
     * @param boatArrayList The boat array list created in main
     */

    public static void expenseBoat (ArrayList<Boat> boatArrayList) {
        //Local variables to hold information about boat to expense and amount to expense
        String boatToExpense;
        int index;
        double amountToExpense;
        boolean checkExpense;
        boolean checkBoatName;
        double remainingPurchaseAmount;

        //Prompts user to enter information about boat to expense
        System.out.print("Which boat do you want to spend on?        : ");

        boatToExpense = keyboard.nextLine();

        checkBoatName = false;

        //Loops through ArrayList to find boat to expense
        for (index = 0; index < boatArrayList.size(); index++) {
            if (boatToExpense.equalsIgnoreCase(boatArrayList.get(index).getBoatName())) {
                System.out.print("How much do you want to spend?             : ");

                //Gets the amount to expense
                amountToExpense = keyboard.nextDouble();
                keyboard.nextLine();

                //Check if amount to expense is valid
                checkExpense = boatArrayList.get(index).changeBoatExpenses(amountToExpense);

                //If amount to expense is not valid, do not allow
                if (!checkExpense) {
                    remainingPurchaseAmount= boatArrayList.get(index).getBoatPurchasePrice()
                            - boatArrayList.get(index).getBoatExpenses();

                    System.out.println("Expense not permitted, only " + String.format("%.2f", remainingPurchaseAmount) + " left to spend");
                }
                //If amount fo to expense is valid, let the user know
                else {
                    System.out.println("Expense authorized, " + String.format("%.2f", boatArrayList.get(index).getBoatExpenses()) + " spent.");
                }
                checkBoatName = true;
            } // end of the if statement

        } // end of the for loop

        //If boat was not found in ArrayList, let user know
        if (!checkBoatName) {
            System.out.println("Cannot find boat " + boatToExpense);
        }
    } // end of the expenseBoat method

    /**
     * Save boat data as a serialized .db file when user exits program
     * @param boatArrayList The boat array list created in main
     * @throws IOException Throws an IO exception if file is not saved correctly
     */

    public static void saveBoatData (ArrayList <Boat> boatArrayList) throws IOException {
        //Local file handling variables
        ObjectOutputStream saveBoatData = null;

        //Save file in a serialized format
        try {

            saveBoatData = new ObjectOutputStream(new FileOutputStream("FleetData.db"));

            saveBoatData.writeObject(boatArrayList);

        } // end of try block

        finally {
            if (saveBoatData != null) {
                saveBoatData.close();
            }
        } // end of finally block

    } // end of the saveBoatData method

} // end of the BoatManager class

