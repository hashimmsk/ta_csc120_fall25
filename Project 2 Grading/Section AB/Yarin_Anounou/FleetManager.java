import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FleetManager {

    private static final String DB_FILENAME = "FleetData.db";

    private static final Scanner keyboard = new Scanner(System.in);

    private List<MyBoat> fleet = new ArrayList<>();

    /**
     * This is the main method that welcomes the user to the fleet management system
     * It then loads the correct data from the CSV, and then it asks the user for input from the menu options.
     * @param args
     */
    public static void main(String[] args) {
        FleetManager manager = new FleetManager();

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");


        if (args.length > 0) {
            String csvFileName = args[0];
            manager.loadFleetFromCSV(csvFileName);
        } else {
            manager.loadFromDB();
        }// end of if statement

        manager.menuOptions();

        keyboard.close();
    } // end of main method

    /**
     * This is the method that loads the fleet information from the original CSV file.
     * @param filename
     */
    private void loadFleetFromCSV(String filename) {
        Path path = Path.of(filename);
        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                MyBoat newBoat = MyBoat.fromCSV(line);
                this.fleet.add(newBoat);
            } // end of the for loop
        } catch (IOException e) {
            System.err.println("Error reading CSV file " + filename + ": " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing data in CSV: " + e.getMessage());
        } // end of the try and catch statement
    } // end of loadFleetFromCSV method

    /**
     * This is the method that loads the information from the edited DB file after the program is run.
     * @return
     */
    private boolean loadFromDB() {
        if (!Files.exists(Path.of(DB_FILENAME))) {
            return false;
        } // end of if statement

        try (
                FileInputStream fileIn = new FileInputStream(DB_FILENAME);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn)
        ) {
            @SuppressWarnings("unchecked")
            List<MyBoat> loadedFleet = (List<MyBoat>) objectIn.readObject();
            this.fleet = loadedFleet;
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        } // end of try and catch
    } // end of loadFromDB method

    /**
     * This method saves the information from the first program run into the DB file instead of the original CSV file.
     */
    private void saveToDB() {
        try (
                FileOutputStream fileOut = new FileOutputStream(DB_FILENAME);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)
        ) {
            objectOut.writeObject(this.fleet);
        } catch (IOException e) {
            System.err.println("Error saving fleet data: " + e.getMessage());
        } // end of try and catch
    } // end of saveToDB method

    /**
     * This is the method that asks the user the menu options like P, A, R, E, X.
     */
    private void menuOptions() {
        String option;
        boolean exit = false;

        while (!exit) {
            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String inputLine = keyboard.nextLine().trim();

            if (inputLine.isEmpty()) {
                System.out.println("Invalid menu option, try again");
                continue;
            } // end of if statement

            char choice = Character.toUpperCase(inputLine.charAt(0));

            switch (choice) {
                case 'P':
                    printReport();
                    break;
                case 'A':
                    addBoat();
                    break;
                case 'R':
                    removeBoat();
                    break;
                case 'E':
                    requestExpense();
                    break;
                case 'X':
                    saveToDB();
                    System.out.println("Exiting the Fleet Management System");
                    exit = true;
                    break;
                default:
                    System.out.print("Invalid menu option, try again");
            } // end of switch and case
        } // end of while loop
    } // end of menuOptions method

    /**
     * This is the method that prints the fleet report when the user inputs the letter P.
     */
    private void printReport() {
        System.out.println("\nFleet report:");
        double totalPaid = 0.0;
        double totalSpent = 0.0;

        for (MyBoat myBoat : fleet) {
            System.out.println("    " + myBoat.toString());
            totalPaid += myBoat.getPurchasePrice();
            totalSpent += myBoat.getExpenses();
        } // end of for loop

        System.out.printf("    Total                                            : Paid $ %10.2f : Spent $ %10.2f\n", totalPaid, totalSpent);
    } // end of printReport method

    /**
     * This is the method that adds a boat to the fleet when the user inputs the letter A.
     */
    private void addBoat() {
        System.out.print("Please enter the new boat CSV data : ");
        String csvLine = keyboard.nextLine();

        try {
            MyBoat newBoat = MyBoat.fromCSV(csvLine);
            this.fleet.add(newBoat);
        } catch (Exception e) {
            System.out.println("Invalid CSV data. Please check the format.");
        } // end of try and catch
    } // end of addBoat method

    /**
     * This is the method that removes a boat from the fleet when the user inputs the letter R
     */
    private void removeBoat() {
        System.out.print("Which boat do you want to remove? : ");
        String removeName = keyboard.nextLine();

        MyBoat myBoat = findBoatByName(removeName);

        if (myBoat == null) {
            System.out.println("Cannot find boat " + removeName);
        } else {
            this.fleet.remove(myBoat);
            System.out.println("Boat " + myBoat.getName() + " removed successfully.");
        } // end of if and else statement
    } // end of removeBoat method

    /**
     * This is the method that asks the user what they want to spend on after they input the letter E.
     */
    private void requestExpense() {
        System.out.print("Which boat do you want to spend on?         : ");
        String spendName = keyboard.nextLine();

        MyBoat target = findBoatByName(spendName);

        if (target == null) {
            System.out.println("Cannot find boat " + spendName);
            return;
        } // end of if statement

        System.out.print("How much do you want to spend?              : ");
        double amount;

        try {
            amount = Double.parseDouble(keyboard.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid amount. Please enter a number.");
            return;
        } // end of try and catch

        if (target.ifCanSpend(amount)) {
            target.addExpenses(amount);
            System.out.printf("Expense authorized, $%.2f spent.\n", target.getExpenses());
        } else {
            double left = target.getRemainingBudget();
            System.out.printf("Expense not permitted, only $%.2f left to spend.\n", left);
        } // end of if and else statement
    } // end of requestExpense method

    /**
     * This method searches the boat by name and figures out if the fleet contains the boat or not.
     * @param name
     * @return
     */
    private MyBoat findBoatByName(String name) {

        MyBoat possibleBoat = new MyBoat(BoatType.SAILING, name, 0, "", 0, 0.0);

        for (MyBoat myBoat: this.fleet) {
            if (myBoat.equals(possibleBoat)) {
                return myBoat;
            } // end of if statement
        } // end of for loop
        return null;
    } // end of findBoatByName method
} // end of FleetManager class