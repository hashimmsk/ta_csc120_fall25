// Imports
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
 * Class to gather input and perform operations on the selected fleet
 * @author alexhooper
 * @version 1.0
 */
public class FleetManager {


// DATA ATTRIBUTES

    /**
     * Keyboard input for program
     * @see FleetManager
     */
    private static final Scanner keyboard = new Scanner(System.in); // Keyboard input


// PROGRAM ENTRYPOINT

    /**
     * Primary method for menu selection
     * @param args (Optional), csv file for importing boat data
     * @see FleetManager
     */
    public static void main(String[] args) {
        // Greeting
        System.out.println("\tWelcome to the Fleet Management System");
        System.out.println("-----------------------------------------------");

        FleetDatabase currentFleet;

        ArrayList<Boat> fleetFromFile = retrieveSavedFleet();

        // Import existing file
        if (args.length > 0) {
            try {
                currentFleet = new FleetDatabase(fleetFromFile, csvInputProcessor(args[0])); // create database
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            currentFleet = new FleetDatabase(fleetFromFile);
        }

        char user_menu_input = '#'; // initialize user input to start the loop

        // Repeat until user exits
        while (user_menu_input != 'X') {
            // Give user options
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it: ");
            // Get selection and format to validate
            user_menu_input = keyboard.nextLine().toUpperCase().charAt(0);

            switch (user_menu_input) {


                case 'P': // Print fleet
                    currentFleet.printFleetReport();
                    break;


                case 'A': // Add boat
                    // Get input
                    System.out.println("Input format for new boats: Kind, Name, Manufacture Year, Model, Length, Purchase Price");
                    System.out.print("Please enter data for the new boat using the above format: ");
                    String newBoatInput = keyboard.nextLine();

                    // Add boat string, and set expenses to $0.00
                    currentFleet.addBoat(newBoatInput + ",0.00");
                    break;


                case 'R': // Remove boat
                    // Get input
                    System.out.print("Please enter the name of the boat you'd like to remove: ");
                    String removeBoatInput = keyboard.nextLine();

                    // Execute Removal
                    currentFleet.removeBoat(removeBoatInput);
                    break;


                case 'E': // Request to expense
                    // Get input for boat name
                    System.out.print("Please enter the name of the boat you'd like to expense: ");
                    String expenseBoatNameInput = keyboard.nextLine();

                    // Validate input
                    short searchResultIndex = currentFleet.searchBoatDatabase(expenseBoatNameInput);

                    // If input valid, proceed
                    if (searchResultIndex > -1) {
                        // Get input for expense
                        System.out.print("How much would you like to spend on the " + expenseBoatNameInput + "?: ");
                        double expenseAmount = keyboard.nextDouble();
                        keyboard.nextLine(); // polish line break

                        // Execute Expense
                        currentFleet.expenseBoat(searchResultIndex, expenseAmount);

                    // Return error for invalid input
                    } else {
                        System.out.println("Cannot find boat " + expenseBoatNameInput);
                    }
                    break;


                    default: // filter invalid inputs
                        if (user_menu_input != 'X') {
                            System.out.println("That's not a valid input. Please try again.");
                        }
                        break;

            } // End menu switch case

        } // User input loop

        // Once user exits, save the fleet to the .db file
        saveFleet(currentFleet);
        System.out.println("\nExiting the Fleet Management System");

    } // End of main method


// SETTERS

    /**
     * Save the fleet to a serialized file
     * @param fleetImport Pass the fleet that will need to go into that file.
     */
    private static void saveFleet(FleetDatabase fleetImport) {

        // Declare variables
        ObjectOutputStream outputStream = null;
        int boatIterator;
        ArrayList<Boat> importedBoats = fleetImport.getBoats();

        // Attempt to save file
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream("ApplicationFleetData.db"));

            for (boatIterator = 0; boatIterator < importedBoats.size(); boatIterator++) {
                // Add each boat to data stream
                outputStream.writeObject(importedBoats.get(boatIterator));
            } // End loop for each boat

        } catch (IOException e) {
            System.out.println("Error saving database.");

        } // End try/catch
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } // end try catch
        } // end null check
    } // End of saveFleet method


// GETTERS

    /**
     * Process data incoming from CSV file
     * @param fileName Set filename of CSV. Typically inherited from program argument
     * @return Return all the lines for the caller to parse
     * @throws IOException Error if the file reading fails
     */
    private static ArrayList<String> csvInputProcessor(String fileName) throws IOException {

        // Declare variables
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader fromBufferedReader;
        String oneLine;

        // Process Lines
        fromBufferedReader = new BufferedReader(new FileReader(fileName));


        // Read each line
        oneLine = fromBufferedReader.readLine();
        // Continue until EOF
        while (oneLine != null) {
            lines.add(oneLine);
            oneLine = fromBufferedReader.readLine();
        } // Stop loop to find in

        // Close out reader
        fromBufferedReader.close();

        // Give caller the lines
        return lines;

    } // End of csvInputProcessor method

    /**
     * Bring fleet from .db file back into the program.
     * @return Give back the retrieved fleet to the caller.
     */
    private static ArrayList<Boat> retrieveSavedFleet() {

        // Setup reader and data
        ObjectInputStream fromStream = null;
        ArrayList<Boat> boatsImportedFromFile = new ArrayList<>();
        Boat currentImportBoat;

        try {
            // Get data from file
            fromStream = new ObjectInputStream(new FileInputStream("ApplicationFleetData.db"));
            currentImportBoat = (Boat) fromStream.readObject();
            // Keep iterating until the next object doesn't exist
            while (currentImportBoat != null) {
                boatsImportedFromFile.add(currentImportBoat);
                currentImportBoat = (Boat) fromStream.readObject();
            }
        // error checking
        } catch (EOFException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (fromStream != null) {
                try {
                    fromStream.close();
                } catch (IOException e) {
                    System.out.println("Error retrieving boats from file");
                } // end try catch
            } // end null check
        } // end finally
        // give all the boats back to caller
        return boatsImportedFromFile;
    } // End of retrieveSavedFleet method


} // End of FleetManager class
