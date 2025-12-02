import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FleetManager {
    private static final Scanner keyboard = new Scanner(System.in);
    private static ArrayList<Boat> fleet = new ArrayList<>();

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args.length > 0) {
            loadFromCSV(args[0]);
            saveToDB();
        } else {
            loadFromDB();
        }

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        menuLoop();
        saveToDB();


    } //end of the main method


    /**
     *
     * @param arg load from the CSV file
     */
    private static void loadFromCSV(String arg) {
        try {
            Scanner fileScanner = new Scanner(new File("FleetData.csv"));

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");


                Boat.BoatType type = Boat.BoatType.valueOf(parts[0].toUpperCase());
                String name = parts[1];
                int year = Integer.parseInt(parts[2].trim());
                String model = parts[3].trim();
                double length = Double.parseDouble(parts[4].trim());
                double purchasePrice = Double.parseDouble(parts[5].trim());
                double expense = 0.0;

                fleet.add(new Boat(type, name, year, model, length, purchasePrice, expense));

            }
            fileScanner.close();


        } catch (Exception e) {
            System.out.println("Error opening or reading file");
        }


    }//end of the loadFromCSV method

    /**
     * Save to DB after the first run
     */
    private static void saveToDB(){
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("FleetData.db"));
            out.writeObject(fleet);
            out.close();

        }catch (Exception e){

        }


    }//end of the SaveToDB method

    /**
     * Load from DB after the first run
     */
    private static void loadFromDB() {
        System.out.println("Successes");
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("FleetData.db"));
            fleet = (ArrayList<Boat>) in.readObject();
            in.close();
            System.out.println("Loaded fleet data from DB.");
        } catch (Exception e) {
            System.out.println("No existing DB found. Starting with empty fleet.");
        }

    }//end of the loadFromDB method

    /**
     * Display the main menu and process user input
     */
    private static void menuLoop() {
        String choice;

        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine().toUpperCase();

            if (choice.equals("P")) {
                printInventory();
            } else if (choice.equals("A")) {
                addBoat();
            } else if (choice.equals("R")) {
                removeBoat();
            } else if (choice.equals("E")) {
                requestSpending();
            } else if (choice.equals("X")) {
                System.out.println("Exiting the Fleet Management System");
            } else {
                System.out.println("Invalid menu option, try again");
            }

        } while (!choice.equals("X"));

    } //end of the menuLoop method

    /**
     * Print a report for all the boats in fleet
     */
    private static void printInventory() {
        System.out.println("\nFleet report:");

        double totalPrice = 0.0;
        double totalExpenses = 0.0;

        for (int i = 0; i < fleet.size(); i++) {
            Boat theBoat = fleet.get(i);

            totalPrice = totalPrice + theBoat.getPurchasePrice();
            totalExpenses = totalExpenses + theBoat.getExpenses();

            System.out.printf("    %-8s %-14s %4d %-10s %3.0f' : Paid $ %8.2f : Spent $ %8.2f\n", theBoat.getType(), theBoat.getName(), theBoat.getYear(), theBoat.getModel(), theBoat.getLength(), theBoat.getPurchasePrice(), theBoat.getExpenses());

        }
        System.out.printf("    %-8s %-14s %4s %-10s %3s  : Paid $ %8.2f : Spent $ %8.2f\n",
                "Total", "", "", "", "", totalPrice, totalExpenses);


    } //end of the printInventory method

    /**
     * Prompt the user to enter the information and add a boat to the fleet
     */
    private static void addBoat() {
        System.out.print("Please enter the new boat CSV data          : ");
        String userInput = keyboard.nextLine();

        String[] parts = userInput.split(",");

        Boat.BoatType type = Boat.BoatType.valueOf(parts[0].toUpperCase());
        String name = parts[1];
        int year = Integer.parseInt(parts[2].trim());
        String model = parts[3].trim();
        double length = Double.parseDouble(parts[4].trim());
        double purchasePrice = Double.parseDouble(parts[5].trim());

        double expense = 0.0;

        Boat theBoat = new Boat(type, name, year, model, length, purchasePrice, expense);
        fleet.add(theBoat);

    }//end of the addBoat method

    /**
     * Prompt the user to enter the name of a boat and removes the boat from the fleet
     */
    private static void removeBoat() {

        System.out.print("Which boat do you want to remove?           : ");
        String userInput = keyboard.nextLine();

        boolean find = false;

        for (int i = 0; i < fleet.size(); i++) {

            Boat theBoat = fleet.get(i);

            if (theBoat.getName().equalsIgnoreCase(userInput)) {
                fleet.remove(i);
                find = true;
                break;

            }

        }

        if (!find) {
            System.out.println("Cannot find boat " + userInput);

        }
    } //end of the removeBoat method

    /**
     * Ask user to select a boat and an amount of money to spend
     * Spending is allowed only if the total expenses do not exceed the boat's purchase price
     *
     */
    private static void requestSpending() {

        System.out.print("Which boat do you want to spend on?      : ");
        String userInput = keyboard.nextLine().trim();

        Boat targetBoat = null;
        for (int i = 0; i < fleet.size(); i++) {
            Boat theBoat = fleet.get(i);
            if (theBoat.getName().equalsIgnoreCase(userInput)) {
                targetBoat = theBoat;
                break;
            }
        }
        if (targetBoat == null) {
            System.out.println("Cannot find boat " + userInput);
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        double amount = Double.parseDouble(keyboard.nextLine().trim());
        double expenses = targetBoat.getExpenses();
        double purchasePrice = targetBoat.getPurchasePrice();

        if (expenses + amount <= purchasePrice) {
            targetBoat.setExpenses(expenses + amount);
            System.out.println("Expense authorized, $" + amount + " spent.");

        } else {
            double left = purchasePrice - expenses;
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", left);


        }
    } //end of the requestSpending method


} //end of the FleetManager class


