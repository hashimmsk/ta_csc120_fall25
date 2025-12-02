import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * FleetManagement is the main class for the Fleet Management System.
 * This program manages a collection of boats in a fleet. It allows users
 * to load fleet data from a CSV file or a saved database file, view reports,
 * add or remove boats, and manage expenses for individual boats.
 *
 * @author sarahlaughlin
 * @version 1.0
 * @see Fleet
 * @see Boat
 */

public class FleetManagement {

    //Constants
    private static final Scanner keyboard = new Scanner(System.in);
    private static final String DB_FILENAME = "FleetData.db";

    /**
     * Main method loads fleet data and shows the menu.
     * @param args optional first argument is CSV file name
     */

    public static void main(String[] args) {

        Fleet fleet;
        File dbFile; //File where I will save the fleet and load the fleet later
        boolean csvProvided; //list of comma separated values

        dbFile = new File(DB_FILENAME);
        csvProvided = (args.length > 0);

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();


       try
        {
            if (csvProvided) {
                // Initializing run: load from CSV file given on command line
                fleet = loadFromCSV(args[0]);
            } else {
                // Normal run: load from DB file if it exists, otherwise start empty
                if (dbFile.exists()) {
                    fleet = loadFromDB(dbFile);
                } else { // First run with no DB file
                    fleet = new Fleet();
                }
            }
            // Show menu loop and let user interact
            menuLoop(fleet);

            //Save fleet to DB on exit
            saveToDB(fleet,dbFile);

            System.out.println();
            System.out.println("Exiting the Fleet Management System");

        } // end try

        catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        } // end catch
       catch (ClassNotFoundException e) {
           System.out.println("Error reading saved data: " + e.getMessage());
       }


    } // end of the main method


    /**
     * Main menu loop
     *
     * @param fleet is the fleet to work with
     */
    private static void menuLoop(Fleet fleet) {
        boolean done;
        String line;
        char choice;

        done = false; //basically while true...

        while(!done) {
            System.out.print("P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            line = FleetManagement.keyboard.nextLine();

            if (line.isEmpty()) {
                choice = ' ';
            } // end if
            else {
                choice = Character.toUpperCase(line.charAt(0));
            } // end else
            if (choice == 'P') {
                printFleetReport(fleet);
            } // end if
            else if (choice == 'A') {
                handleAddBoat(fleet);
            } // end else if
            else if (choice == 'R') {
                handleRemoveBoat(fleet);
            } // end else if
            else if (choice == 'E'){
                handleExpenses(fleet);
            } // end else if
            else if (choice == 'X'){
                done = true;
            } // end else if
            else {
                System.out.println("Invalid menu option, try again");
            } // end else
        } // end while loop

    } // end of menuLoop method


    /**
     * Handle printing the fleet report
     * @param fleet is the fleet to display to user
     */
    private static void printFleetReport(Fleet fleet) {
        String report;

        System.out.println();
        report = fleet.formattedReport(); // see what this does
        System.out.println(report);
        System.out.println();

    } // end of the printFleetReport method

    /**
     * Handle adding a boat.
     *
     * @param fleet is the fleet
     */
    private static void handleAddBoat(Fleet fleet){
        String csvLine;
        Boat boat;

        System.out.print("Please enter the new boat CSV data          : ");
        csvLine = FleetManagement.keyboard.nextLine();

        try {
            boat = parseBoatCSVLine(csvLine);
            fleet.addBoat(boat);
        } // end try
        catch (IllegalArgumentException e) {
            System.out.println("Could not add boat: " + e.getMessage());
        } // end catch
        System.out.println();

    } // end of the handleAddBoat method


    /**
     * Handle removing a boat.
     *
     * @param fleet is the fleet
     */
    private static void handleRemoveBoat(Fleet fleet){
        String name;
        boolean removed;

        System.out.print("Which boat do you want to remove?           : ");
        name = FleetManagement.keyboard.nextLine();

        removed = fleet.removeBoatByName(name);

        if (!removed) {
            System.out.println("Cannot find boat " + name);
        } // end if

        System.out.println();

    } // end of the handleRemoveBoat method


    /**
     * Handle expense request.
     *
     * @param fleet is the fleet
     */
    private static void handleExpenses(Fleet fleet){

        String name;
        String amountLine;
        double amount;
        Boat boat;
        boolean ok;
        double remainingAmount;

        System.out.print("Which boat do you want to spend on?         : ");
        name = FleetManagement.keyboard.nextLine();

        boat = fleet.findBoatByName(name);

        if (boat == null) {
            System.out.println("Cannot find boat " + name);
            System.out.println();
            return;
        } // end if

        System.out.print("How much do you want to spend?              : ");
        amountLine = FleetManagement.keyboard.nextLine();
        amount = Double.parseDouble(amountLine);

        ok = boat.additionalExpense(amount);

        if (ok){
            System.out.printf("Expense authorized, $%.2f spent.\n", boat.getExpenses());
        } // end if
        else {
            remainingAmount = boat.getPurchasePrice() - boat.getExpenses();
            System.out.printf("Expense not permitted, only $%.2f left to spend. \n", remainingAmount);
        } // end else
        System.out.println();

    } // end handleExpenses method

    /**
     * Load fleet from a CSV file.
     * @param csvFileName CSV file name
     * @return Fleet loaded form file
     * @throws IOException if I/O error
     */
    private static Fleet loadFromCSV(String csvFileName) throws IOException{
        Fleet fleet;
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line;
        Boat boat;

        fleet = new Fleet();

        // Open CSV file for reading
        fileReader = new FileReader(csvFileName);
        bufferedReader = new BufferedReader(fileReader);


        // Read one line at a time
        line = bufferedReader.readLine();
        while(line != null){
            line = line.trim();
            if (!line.isEmpty()){ // Ignore blank lines
                boat = parseBoatCSVLine(line); // Convert CSV line into Boat object
                fleet.addBoat(boat); // Add boat to fleet
            } // end if
            line = bufferedReader.readLine();
        } // end while

        bufferedReader.close();

        return fleet;

    } // end of the loadFromCSV method


    /**
     * Load fleet from serialized .db file.
     * @param dbFile is the database file
     * @return Fleet read from file
     * @throws IOException if I/O error
     * @throws ClassNotFoundException if wrong class
     */
    private static Fleet loadFromDB(File dbFile) throws IOException, ClassNotFoundException{
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        Fleet fleet;
        Object obj;

        //Open .db file and read serialized Fleet object
        fileInputStream = new FileInputStream(dbFile);
        objectInputStream = new ObjectInputStream(fileInputStream);

        obj = objectInputStream.readObject();
        fleet = (Fleet) obj;

        objectInputStream.close();

        return fleet;

    } // end of loadFromDB method

    /**
     * Save fleet to serialized .db file.
     * @param fleet is the fleet
     * @param dbFile is the database file
     * @throws IOException if I/O error
     */
    private static void saveToDB(Fleet fleet, File dbFile) throws IOException{
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;

        // Open the .db file and write the Fleet object to it
        fileOutputStream = new FileOutputStream(dbFile);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(fleet);

        objectOutputStream.close();
    } // end of saveToDB method

    /**
     * Parse one CSV line into a Boat.
     * Format: TYPE,Name,Year,Make,Length,PurchasePrice
     * @param csvLine is the CSV line
     * @return a Boat object
     */
    private static Boat parseBoatCSVLine(String csvLine){
        String[] parts;
        String typeText;
        String name;
        String yearText;
        String makeModel;
        String lengthText;
        String priceText;
        int year;
        int lengthFeet;
        double purchasePrice;
        BoatType boatType;
        Boat boat;

        parts = csvLine.split(",");

        if(parts.length != 6){
            throw new IllegalArgumentException("CSV line must have 6 fields");
        } // end if

        typeText = parts[0].trim().toUpperCase();
        name = parts[1].trim();
        yearText = parts[2].trim();
        makeModel = parts[3].trim();
        lengthText = parts[4].trim();
        priceText = parts[5].trim();

        if (typeText.equals("SAILING")){
            boatType = BoatType.SAILING;
        } // end if
        else if (typeText.equals("POWER")) {
            boatType = BoatType.POWER;
        } // end else if
        else {
            throw new IllegalArgumentException("Unknown boat type: " + typeText);
        } // end else

        year = Integer.parseInt(yearText);
        lengthFeet = Integer.parseInt(lengthText);
        purchasePrice = Double.parseDouble(priceText);

        if (lengthFeet < 0 || lengthFeet > 100){
            throw new IllegalArgumentException("Length must be between 0 and 100");
        } // end if
        if (purchasePrice < 0.0 || purchasePrice >= 1000000.0){
            throw new IllegalArgumentException("Purchase price must be less than 1,000,000");
        } // end if

        boat = new Boat(boatType, name, year, makeModel, lengthFeet, purchasePrice);

        return boat;

    } // end of the parseBoatCSVLine method

} // end of the FleetManagement class
