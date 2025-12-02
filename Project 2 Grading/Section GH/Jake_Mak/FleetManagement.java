import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * The purpose of this program will be to track the costs associated with owning various sailing and power boats
 *
 * @author Jake Maksimiak
 * @see Boat
 */
public class FleetManagement {

    /**
     * The main method, which will be responsible for creating boat objects based on the fleet data, and for
     * putting the boats into an arraylist.
     * Afterwards, the main method will direct the user towards the 5 menu options
     *
     * @param args Depending on the configuration, args will serve different purposes
     *             In the configuration where args contains "FleetData.csv" passed in from the command line parameters,
     *             the args will provide the program with the name of the file containing the fleet data.
     *             In the configuration where args is empty, the program will read the fleet data from
     *             FleetData.db, which is the fleet data stored in serialized form from the previous execution
     *             of the program.
     *
     */
    public static void main(String[] args) {

        // declare variables
        final Scanner keyboard = new Scanner(System.in);

        boolean readCSVFile;
        File dataFile;
        BufferedReader fileReader;
        String lineOfData;
        String[] boatAttributes;
        Boat.TypeOfBoat currentTypeOfBoat;
        String nameOfBoat;
        int yearOfManufacture;
        String makeAndModel;
        byte lengthOfBoat;
        double moneyPaid;
        Boat sampleBoat;
        ArrayList<Boat> listOfBoats = new ArrayList<>();

        String menuSelection;
        char menuSelect;
        boolean repeatSwitch;
        String newData;
        String boatSelection;
        int savedNumber;
        double spendingAmount;
        double totalSpent;

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");


        // If the boolean is true, data will be read from the csv file
        // Otherwise, data will be read from FleetData.db
        readCSVFile = args.length > 0;

        // Try-catch-finally will catch any errors when reading the files
        try {

            if (readCSVFile) { // Fleet data will be obtained from the csv file

                dataFile = new File(args[0]);

            } else { // Fleet data will be obtained from the serialized file

                dataFile = new File("FleetData.db");

            } // end of if else loop

            // Create the BufferedReader
            fileReader = new BufferedReader(new FileReader(dataFile));

            // Attempt to read the file
            lineOfData = fileReader.readLine();

            // While loop to initialize the attributes of the boat
            while (lineOfData != null) {

                // Split each line of the CSV into strings
                boatAttributes = lineOfData.split(",");

                // Initialize all the data from the CSV
                currentTypeOfBoat = Boat.TypeOfBoat.valueOf(boatAttributes[0]); // type of boat
                nameOfBoat = boatAttributes[1]; // name of boat
                yearOfManufacture = Integer.parseInt(boatAttributes[2]); // year of manufacture
                makeAndModel = boatAttributes[3]; // make and model of boat
                lengthOfBoat = Byte.parseByte(boatAttributes[4]); // length of boat
                moneyPaid = Double.parseDouble(boatAttributes[5]); // money paid for boat


                // Create the boat object
                sampleBoat = new Boat(currentTypeOfBoat, nameOfBoat, yearOfManufacture, makeAndModel, lengthOfBoat, moneyPaid);

                // If data is not being read from the csv file, then the money spent must be initialized
                // Because the money spent can be nonzero
                if (!readCSVFile) {
                    sampleBoat.setMoneySpent(Double.parseDouble(boatAttributes[6]));
                }

                // add the boat to the arraylist
                listOfBoats.add(sampleBoat);

                // cycle through the next line
                lineOfData = fileReader.readLine();

                // While loop will repeat until all the lines in the file have been read
            } // end of while loop

            // Close the file
            fileReader.close();

            // Catch FileNotFound and IO Exceptions
        } catch (FileNotFoundException e) {

            System.out.println("File wasn't found");

        } catch (IOException e) {

            System.out.println("IO Error");

        } catch (NullPointerException e) {

            System.out.println("Error closing the file");

        } // end of try-catch-finally loop

/* At this point the data of the fleet has been entered in to the arraylist
Now the user will be prompted with 5 menu options */


// Switch statement will give the user the 5 menu options
        repeatSwitch = true;

        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            menuSelection = keyboard.nextLine();
            menuSelect = Character.toUpperCase(menuSelection.charAt(0));

            // Every menu selection is converted to uppercase to avoid invalid inputs

            switch (menuSelect) {

                case ('P'): // Print the data of the fleet
                    printBoats(listOfBoats);

                    System.out.println();
                    break;

                case ('A'): // Add a boat to the fleet
                    System.out.print("Please enter the new boat CSV data          : ");
                    newData = keyboard.nextLine();

                    listOfBoats.add(addBoat(newData));

                    System.out.println();
                    break;

                case ('R'): // Remove a boat from the fleet
                    System.out.print("Which boat do you want to remove?           : ");
                    boatSelection = keyboard.nextLine();

                    savedNumber = findBoatIndex(boatSelection, listOfBoats);

                    if (savedNumber == -1) { // savedNumber = -1 indicates the boat was never found in the arraylist
                        System.out.println("Cannot find boat " + boatSelection);

                    } else {

                        listOfBoats.remove(savedNumber);
                    }
                    System.out.println();
                    break;
                case ('E'):
                    System.out.print("Which boat do you want to spend on?         : ");
                    boatSelection = keyboard.nextLine();

                    savedNumber = findBoatIndex(boatSelection, listOfBoats);

                    if (savedNumber == -1) { // savedNumber = -1 indicates the boat was never found in the arraylist
                        System.out.println("Cannot find boat " + boatSelection);

                    } else {

                        System.out.print("How much do you want to spend?              : ");
                        spendingAmount = keyboard.nextDouble();

                        // The following if condition verifies that the amount the user wants to spend
                        // Plus the amount already spent on the boat
                        // Is less than or equal to the amount paid for the boat

                        totalSpent = spendingAmount + listOfBoats.get(savedNumber).getMoneySpent();
                        if (totalSpent <= listOfBoats.get(savedNumber).getMoneyPaid()) {

                            // The expense is authorized
                            System.out.println("Expense authorized, $" + String.format("%.2f", totalSpent) + " spent.");
                            listOfBoats.get(savedNumber).setMoneySpent(totalSpent);

                        } else {

                            // The expense is not authorized
                            System.out.print("Expense not permitted, only $");
                            System.out.printf("%.2f", listOfBoats.get(savedNumber).getAvailableToSpend());
                            System.out.println(" left to spend.");

                        } // end of inner if loop

                        menuSelection = keyboard.nextLine(); // for some reason the code only runs properly with this line

                    } // end of outer if loop

                    System.out.println();
                    break;

                case ('X'): // Exit the program
                    System.out.println("Exiting the Fleet Management System");

                    // The fleet data is saved to FleetData.db, then the program ends
                    saveDataToSerializedForm(listOfBoats);

                    repeatSwitch = false;
                    break;

                default: // If the user enters an invalid menu option
                    System.out.println("Invalid menu option, try again");

            } // end of switch statement

        } while (repeatSwitch); // Repeat the switch until the user wants to exit

    } // end of main method

    /**
     * The method is responsible for printing the fleet data, as well as a sum of the amount paid for each
     * boat and the amount spent on each boat
     *
     * @param listOfBoats The arraylist which contains all of the boat objects
     */
    public static void printBoats(ArrayList<Boat> listOfBoats) {

        int index;
        double sumOfPaid = 0;
        double sumOfSpent = 0;

        // For loop will output each element of the arraylist of boat objects

        for (index = 0; index < listOfBoats.size(); index++) {

            // Output the boat's data using the toString() method
            System.out.println(listOfBoats.get(index).toString());

            // Add up the amount paid on the boat
            sumOfPaid = sumOfPaid + listOfBoats.get(index).getMoneyPaid();

            // Add up the amount spent on the boat
            sumOfSpent = sumOfSpent + listOfBoats.get(index).getMoneySpent();

        } // end of for loop

        // Print the sums of the amount paid and spent for each boat
        System.out.print("    " + String.format("%-50s", "Total") + ": ");
        System.out.print("Paid $" + String.format("%9s", String.format("%.2f", sumOfPaid)) + " : ");
        System.out.println("Spent $" + String.format("%9s", String.format("%.2f", sumOfSpent)));

    } // end of printBoats method

    /**
     * The method will create a boat, based on the user's input, and return the boat.
     *
     * @param newData The boat that the user wants to add, formatted the same as the csv data
     * @return The boat object that the user wants to add to the fleet
     */
    public static Boat addBoat(String newData) {

        String[] boatData;
        Boat.TypeOfBoat currentTypeOfBoat;
        String nameOfBoat;
        int yearOfManufacture;
        String makeAndModel;
        byte lengthOfBoat;
        double moneyPaid;
        Boat sampleBoat;

        // Split the csv input
        boatData = newData.split(",");

        // Initialize all the data from the CSV
        currentTypeOfBoat = Boat.TypeOfBoat.valueOf(boatData[0]); // type of boat
        nameOfBoat = boatData[1]; // name of boat
        yearOfManufacture = Integer.parseInt(boatData[2]); // year of manufacture
        makeAndModel = boatData[3]; // make and model
        lengthOfBoat = Byte.parseByte(boatData[4]); // length of boat
        moneyPaid = Double.parseDouble(boatData[5]); // money paid for boat

        // Create the new boat object
        sampleBoat = new Boat(currentTypeOfBoat, nameOfBoat, yearOfManufacture, makeAndModel, lengthOfBoat, moneyPaid);

        return sampleBoat;
    } // end of addBoat method

    /**
     * The method will determine if the user's input, the name of a boat, matches any of the boats in the arraylist
     *
     * @param boatSelection The name of a boat given by the user's input
     * @param listOfBoats   The arraylist with all of the boat objects
     * @return The boat object which was created based off the user's input
     */
    public static int findBoatIndex(String boatSelection, ArrayList<Boat> listOfBoats) {

        int index;
        int savedNumber = -1;

        // For loop will check if the inputted boat name matches any boat in the arraylist
        for (index = 0; index < listOfBoats.size(); index++) {

            // If loop determines if the boat name has a match in the arraylist
            if (boatSelection.equalsIgnoreCase(listOfBoats.get(index).getNameOfBoat())) {

                savedNumber = index;

            } // end of inner if loop

        } // end of for loop

        // Returns the index at which the name was found
        // If the name wasn't found, -1 is returned

        return savedNumber;

    } // end of removeBoat method

    /**
     * The method will save the fleet data in serialized form to the file FleetData.db
     *
     * @param listOfBoats The arraylist of boat objects
     */
    public static void saveDataToSerializedForm(ArrayList<Boat> listOfBoats) {

        int index;
        File serialOutputDataFile;
        BufferedWriter serialOutput;

        try {

            serialOutputDataFile = new File("FleetData.db");
            serialOutput = new BufferedWriter(new FileWriter(serialOutputDataFile));

            // The for loop will write each boat to the FleetData.db file
            for (index = 0; index < listOfBoats.size(); index++) {

                serialOutput.write(listOfBoats.get(index).toSerializedForm());
                serialOutput.newLine();

            } // end of for loop

            // attempt to close the file
            serialOutput.close();

            // Catch any exceptions
        } catch (FileNotFoundException e) {

            System.out.println("Serial output file was not found");

        } catch (IOException e) {

            System.out.println("Serial output file IO error");

        } // end of try-catch

    } // end of saveDataToSerializedForm method

} // end of FleetManagement Class
