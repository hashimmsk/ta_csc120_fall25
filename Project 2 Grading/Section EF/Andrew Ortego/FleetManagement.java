// import classes to read the CSV file
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// import classes to save the file
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// import the Scanner and ArrayList class for functionality
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Main class to handle fleets of boats
 * @author Andrew Ortego
 */
public class FleetManagement {

    /**
     * Global Scanner object to use keyboard
     */
    private static final Scanner keyboard = new Scanner(System.in);
    /**
     * ArrayList that contains Boat objects to make up a fleet
     */
    private ArrayList<Boat> fleet;

    /**
     * String to hold the name of the .db file
     */
    private static final String DB_FILE = "FleetData.db";

//--------------------------------------------------------------------------------------------------------------------

    /**
     * The main method
     * @param args CSV file passed in from the command line
     */
    public static void main(String[] args) {

        // Greet the user & instantiate a new fleet
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();
        FleetManagement myFleet = new FleetManagement();

        // Check the command line argument
        if (args.length > 0) {
            // Store the given argument, read the CSV file, and extract necessary information
            String fileName = args[0];
            myFleet.readCSVFile(fileName);
        }
        else {
            // Attempt to load the fleet from the .db file
            boolean isLoaded = myFleet.getFleetFromFile(DB_FILE);

            // Let the user know if there is no serialized fleet
            if (!isLoaded) {
                System.out.println("No serialized fleet found!");
                System.out.println("Empty fleet");
            }
        }

        // Provide the user with menu options
        myFleet.giveMenuOptions();

        // Before program termination, save fleet to DB_FILE
        myFleet.saveFleetToFile(DB_FILE);

    } // end of the main method

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Create a new fleet to store Boat objects
     */
    public FleetManagement() {
        fleet = new ArrayList<Boat>();
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Read a CSV file passed in from the command line, extract information, and instantiate Boat objects
     * @param fileName String to hold the command line argument
     */
    private void readCSVFile(String fileName) {
        // Declare a null BufferedReader object
        BufferedReader myBufferedReader = null;

        try {
            // Open the CSV file to be read
            // Wrap the FileReader object in a BufferedReader
            myBufferedReader = new BufferedReader(new FileReader(fileName));

            // Variable to hold a line from the CSV
            String oneLine;

            // Run the loop while lines are being read
            while ( (oneLine = myBufferedReader.readLine()) != null) {

                // String array to hold each boat's data points split by commas
                String [] dataValues = oneLine.split(",");

                // Break down the CSV values and store into the appropriate variables
                Boat.BoatType boat_type = Boat.BoatType.valueOf(dataValues[0]);
                String name = dataValues[1];
                int yearOfManufacture = Integer.parseInt(dataValues[2]);
                String model = dataValues[3];
                int length = Integer.parseInt(dataValues[4]);
                double price = Double.parseDouble(dataValues[5]);

                // instantiate a new Boat object from the given data
                Boat nextBoat = new Boat(name, yearOfManufacture, model, length, price, 0.0, boat_type);
                // add the new Boat to the fleet
                fleet.add(nextBoat);
            } // end of the while loop
        } catch (FileNotFoundException error) {
            // Handle a file not found error
            System.out.println("ERROR: File not found!");
        } catch (IOException error) {
            // Handle an error reading the file
            System.out.println("ERROR: Unable to read file!");
        } finally {
            // Attempt to close the file after the program terminates
            if(myBufferedReader != null) {
                try {
                    myBufferedReader.close();
                } catch (IOException error) {
                    System.out.println("ERROR: Could not close file!");
                } // end of the try/catch statement
            } // end of the if statement
        } // end of the try/catch(2)/finally statement

    } // end of the readCSVFile method

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Save fleet data to a .db file at program termination
     * @param fileName String to hold the command line argument
     */
    private void saveFleetToFile(String fileName) {
        // try with resources to create a FileOutputStream and wrap in an ObjectOutputStream
        try (ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Write the fleet data to the ObjectOutputStream
            myObjectOutputStream.writeObject(fleet);
        }
        catch (IOException error) {
            // Handle an error saving the file
            System.out.println("Error saving fleet to file!");
        } // end of the try/catch block
    } // end of the saveFleetToFile method

//--------------------------------------------------------------------------------------------------------------------

    /**
     * On a second program run, load the seralized fleet data
     * @param fileName String to hold the command line argument
     * @return True if the fleet is loaded from file, false if otherwise
     */
    private boolean getFleetFromFile(String fileName) {
        // File object to hold the contents of fileName
        File myFile = new File(fileName);

        // Check if the file exists before proceeding
        if(!myFile.exists()) {
            return false;
        }

        // Attempt to load the file
        // try with resources to create a FileInputStream and wrap in an ObjectInputStream
        try (ObjectInputStream myObjectInputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            // Read the contents of the ObjectInputStream
            Object myObject = myObjectInputStream.readObject();

            // type cast the contents myObject to a fleet
            fleet = (ArrayList<Boat>) myObject;
            return true;
        }
        catch (FileNotFoundException error){
            // Handle a file not found error
            System.out.println("ERROR: File not found!");
            return false;
        }
        catch (IOException error){
            // Handle an error loading the file
            System.out.println("Error loading fleet from file!");
            return false;
        }
        catch (ClassNotFoundException error){
            // Handle an error in casting myObject to the correct class
            System.out.println("Error loading fleet from file!");
            return false;
        }

    } // end of the getFleetFromFile method

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Prove user with 5 options to print fleet report, add or remove a boat, spend on a boat, and terminate program
     */
    public void giveMenuOptions() {

        // flag variable to control the while loop
        boolean active = true;

        while(active) {
            // Provide the user with 5 options and check for input
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            char userInput = keyboard.nextLine().charAt(0);

            // If exit option is chosen, terminate the program
            // If print option is chosen, display a fleet report
            // If add option is chosen, allow the user to add a boat to the fleet
            // If remove option is chosen, allow the user to remove a boat from the fleet
            // If expense option is chosen, allow the user to spend money on a boat in the fleet
            if (userInput == 'X' || userInput == 'x') {
                System.out.println("");
                System.out.println("Exiting the Fleet Management System");
                active = false;
            }
            else if (userInput == 'P' || userInput == 'p') {
                System.out.println("");
                System.out.println("Fleet report:");

                // Variables to track total price and total spent on boats
                double totalPaid = 0;
                double totalSpent = 0;

                // Loop through the fleet ArrayList
                for(int index = 0; index < fleet.size(); index++) {
                    // Store each Boat object for analysis
                    Boat thisBoat = fleet.get(index);

                    // Call the toString() method for the boat to print required info
                    System.out.print(thisBoat);
                    System.out.println();

                    // Compute the total amount paid and total amount spent on each boat
                    totalPaid += thisBoat.getPrice();
                    totalSpent += thisBoat.getExpense();
                } // end of the for loop

                // NEED TO FIX FORMATTING
                // Display the total price and total expense
                String totalInformation = String.format("    %-53s : Paid $ %8.2f : Spent $ %10.2f", "Total", totalPaid, totalSpent);
                System.out.println(totalInformation);
                System.out.println();
            }
            else if (userInput == 'A' || userInput == 'a') {
                // Prompt the user to enter CSV data and read to keyboard
                System.out.print("Please enter the new boat CSV data          : " );
                String userInputtedData = keyboard.nextLine();

                // String array to hold the contents of the CSV data split by commas
                String[] newCSV_data = userInputtedData.split(",");

                // Extract each data point
                Boat.BoatType boat_type = Boat.BoatType.valueOf(newCSV_data[0]);
                String name = newCSV_data[1];
                int yearOfManufacture = Integer.parseInt(newCSV_data[2]);
                String model = newCSV_data[3];
                int length = Integer.parseInt(newCSV_data[4]);
                double price = Double.parseDouble(newCSV_data[5]);

                // Instantiate a new Boat object from the given data
                Boat nextBoat = new Boat(name, yearOfManufacture, model, length, price, 0.0, boat_type);

                // add the new Boat to the fleet
                fleet.add(nextBoat);
                System.out.println();
            }
            else if (userInput == 'R' || userInput == 'r') {
                // Ask for a boat name & read to keyboard
                System.out.print("Which boat do you want to remove?           : ");
                String givenBoat = keyboard.nextLine();

                // integer to hold the index of the boat to remove
                int testValue = validateBoat(givenBoat);

                // validate the boat index
                if (testValue == -1) {
                    System.out.println("Cannot find boat " + givenBoat);
                    System.out.println();
                }
                else {
                    // remove the boat from the fleet
                    fleet.remove(testValue);
                    System.out.println();
                }
            }
            else if (userInput == 'E' || userInput == 'e') {
                // Ask for a boat name and read to keyboard
                System.out.print("Which boat do you want to spend on?         : " );
                String givenBoat = keyboard.nextLine();

                // integer to hold the index of the boat to spend on
                int testValue = validateBoat(givenBoat);

                // validate the boat index
                if (testValue == -1) {
                    System.out.println("Cannot find boat " + givenBoat);
                    System.out.println();
                }
                else {
                    // validate the amount the user wants to spend on the boat
                    validateExpense(testValue);
                }
            } // end of elif
            else {
                System.out.println("Invalid menu option, try again");
            }
        } // end of the outer while loop

    } // end of the giveMenuOptions method

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Determine if the boat requested by the user exists and return the index
     * @param givenBoat String name of the boat inputted by the user
     * @return integer index of the particular boat to handle
     */
    private int validateBoat(String givenBoat) {
        // variable to hold index of boat name when name matches
        int indexOfBoat = -1;

        // Attempt to find a matching boat in the fleet
        for (int index = 0; index < fleet.size(); index++) {
            String boatName = fleet.get(index).getName();
            if (boatName.equalsIgnoreCase(givenBoat)) {
                indexOfBoat = index;
                break;
            }
        }

        // Validate or deny the requested boat name
        if (indexOfBoat != -1) {
            return indexOfBoat;
        }
        else {
            return -1;
        }

    } // end of the validateBoat method

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Determine how much the user wants to spend and authorize the payment
     * @param boatIndex integer index of the boat the user wants to spend money on
     */
    private void validateExpense(int boatIndex) {
        // Prompt the user for an amount to spend and read to keyboard
        System.out.print("How much do you want to spend?              : ");
        double requestedExpense = keyboard.nextDouble();;

        // Clear empty input from console
        keyboard.nextLine();

        // Store the amount already spent on the boat
        double alreadyPaid = fleet.get(boatIndex).getExpense();

        // variables to compute expense authorization
        double comparison = alreadyPaid + requestedExpense;
        double priceOfBoat = fleet.get(boatIndex).getPrice();

        // Determine if the expense can be authorized
        if (comparison > priceOfBoat) {
            double amountLeftToSpend = priceOfBoat - alreadyPaid;
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", amountLeftToSpend);
            System.out.println();
        }
        else {
            System.out.printf("Expense authorized, $%.2f spent.%n", comparison);
            System.out.println();

            // Update the expense attribute of the boat
            double newExpense = alreadyPaid + requestedExpense;
            fleet.get(boatIndex).setExpense(newExpense);
        }

    } // end of the validateExpense method

} // end of the FleetManagement class