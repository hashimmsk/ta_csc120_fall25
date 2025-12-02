import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

/**
 * This is our driver class. This contains our menu, and our menu options for saving boat data.
 * This program manages boats by addding, removing and tracking expences.
 */
public class FleetManagementSystem {
    private static final Scanner keyboard = new Scanner(System.in);
    private static final String BOAT_DB_FILE = "FleetData.db";

    /**
     *This where the program excutes.
     * This is where our menu is, and calling of each method.
     * This is where the file error handing occurs.
     * @param args
     */
    public static void main(String[] args) {

        // Variables:
        ArrayList<Boat> fleetOfBoat = new ArrayList<>();
        String user_choice;

        //HERE LOAD FILE

        if (args.length > 0) {
            fleetOfBoat = readingTheCSVFile("FleetData.csv");
        } else {
            fleetOfBoat = readingDBFile();
        }// when the file is greater is zero it will run the file, if not


        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it :  ");
        user_choice = keyboard.next().substring(0, 1).toUpperCase();

        while (!user_choice.equals("X")) {

            if (user_choice.equals("P")) {

                int index;
                double totalPrice = 0.0;
                double totalExpenses = 0.0;

                System.out.println("\nFleet report:");

                for (index = 0; index < fleetOfBoat.size(); index++) {

                    System.out.println("	" + fleetOfBoat.get(index));
                    totalPrice += fleetOfBoat.get(index).getPurchasePrice();
                    totalExpenses += fleetOfBoat.get(index).getMaintainCost();
                }
                System.out.printf("	%-55s : Paid $%9.2f : Spent $%9.2f\n", "Total", totalPrice, totalExpenses);


            } else if (user_choice.equals("A")) {
                addBoat(fleetOfBoat);

            } else if (user_choice.equals("R")) {

                removeBoat(fleetOfBoat);

            } else if (user_choice.equals("E")) {
                expense(fleetOfBoat);
            } else {
                System.out.println("Invalid menu option, try again");
            }

            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it :  ");
            user_choice = keyboard.next().substring(0, 1).toUpperCase();

        }// end of while

        writeDataToDB(fleetOfBoat);
        System.out.println("Exiting the Fleet Management System");

    }// end of main


    // to read Db file

    /**
     * A function to allow the reading and handling errors
     * @return boat Information is returned with handled errors
     */
    private static ArrayList<Boat> readingDBFile() {
        //variables
        ObjectInputStream fromStream;
        ArrayList<Boat> boatData;
        boatData = null;
        fromStream = null;

        try {
            fromStream = new ObjectInputStream(new FileInputStream(BOAT_DB_FILE));

            boatData = (ArrayList<Boat>) fromStream.readObject();

        } catch (EOFException e) {

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());

        } finally {

            if (fromStream != null) {
                try {
                    fromStream.close();
                } catch (IOException e) {
                    System.out.println("ERROR: Closing file: " + e.getMessage());
                }
            }
        }

        return (boatData);

    }//end of method readingDBFile

    /**
     * A function for allowing Data to be Written to a new file
     * @param boatData there is the data about each boat, the type, name, model, length, price, and cost
     */
    private static void writeDataToDB(ArrayList<Boat> boatData) {
        //variables
        ObjectOutputStream toStream;
        toStream = null;

        try {
            toStream = new ObjectOutputStream(new FileOutputStream(BOAT_DB_FILE));
            toStream.writeObject(boatData);


        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());

        } finally {
            if (toStream != null) {
                try {
                    toStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }// end of if-catch statement
        }// end of finally statements

    }// end of writeDataToBD method



    /**
     * a function to read from SVC file and return the ArrayList of boat
     * @param fileName is a file with boat information that needs to be split
     * @return will return the data of the boat
     */
    private static ArrayList<Boat> readingTheCSVFile(String fileName) {
        //variables
        String oneLine;

        BufferedReader frombufferReader; //the BufferReader is pre made in Java and it is used to read a file.(one way).
        ArrayList<Boat> boatData = new ArrayList<>(); //an empty arrayList. we declare an empty list of boat

        try {

            frombufferReader = new BufferedReader(new FileReader(fileName));
            oneLine = frombufferReader.readLine();

            while (oneLine != null) {

                String[] boatDataParts;

                //Split the Array
                boatDataParts = oneLine.split(","); //it will split by the commas

                String boatType = boatDataParts[0];
                Boat.BoatType type = Boat.BoatType.valueOf(boatType);

                String name = boatDataParts[1];
                int year = Integer.parseInt(boatDataParts[2]);
                String model = boatDataParts[3];
                double length = Double.parseDouble(boatDataParts[4]);
                double price = Double.parseDouble(boatDataParts[5]);

                Boat aBoat = new Boat(type, name, year, model, length, price);

                boatData.add(aBoat);

                oneLine = frombufferReader.readLine();


            }// end of while

        } catch (IOException e) {
            System.out.println("Error reading file" + fileName);
        }

        return boatData;
        //when we call this method this has to be inside a try block

    } // end of readingTheSCVFile



    /**
     * purpose of the function is to get a boat information from user, and add it to fleet
     * @param fleet is a arrayList of objects
     */
    public static void addBoat(ArrayList<Boat> fleet) {

        // variables
        Boat.BoatType type;
        int boatYear;
        String boatName;
        String model;
        String newBoatInfo;
        double boatLength;
        double purchasePrice;


        keyboard.nextLine();
        System.out.print("Please enter the new boat CSV data: ");

        // Split into newBoatParts
        newBoatInfo = keyboard.nextLine();
        String[] newBoatParts = newBoatInfo.split(",");


        // Convert type string to enum because scanner get only obtain string. Enum limits options.
        if (newBoatParts[0].equalsIgnoreCase("POWER")) {
            type = Boat.BoatType.POWER;
        } else if (newBoatParts[0].equalsIgnoreCase("SAILING")) {
            type = Boat.BoatType.SAILING;
        } else {
            type = Boat.BoatType.UNKNOWN;
        }

        boatName = newBoatParts[1];
        boatYear = Integer.parseInt(newBoatParts[2]);
        model = newBoatParts[3];
        boatLength = Double.parseDouble(newBoatParts[4]);
        purchasePrice = Double.parseDouble(newBoatParts[5]);

        // create the new boat
        Boat aBoat = new Boat(type, boatName, boatYear, model, boatLength, purchasePrice);

        // Add to fleet
        fleet.add(aBoat);

    }// end of addBoat method.

    /**
     *This removes a boat from our file.
     * @param fleet is a arrayList of objects
     */
    public static void removeBoat(ArrayList<Boat> fleet) {

        //variable
        String boatNameRemoved;
        int boatIndex;
        keyboard.nextLine();

        System.out.print("Which boat do you want to remove?           :");
        boatNameRemoved = keyboard.nextLine();


        boatIndex = findBoatIndex(fleet, boatNameRemoved);

        if (boatIndex >= 0) {
            fleet.remove(boatIndex);
            //boat was removed
        }// end of if statement


    }// end of removeBoat method


    /**
     * A funciton for finding the index of a Boat
     * @param fleet is a arrayList of objects
     * @param boatNameRemoved
     * @return {@code BoatIndex} if true, and {@code -1} if boat cannot be found
     */
    public static int findBoatIndex(ArrayList<Boat> fleet, String boatNameRemoved) {
        //variables
        int boatIndex;

        for (boatIndex = 0; boatIndex < fleet.size(); boatIndex++) {

            if (fleet.get(boatIndex).getBoatName().equalsIgnoreCase(boatNameRemoved)) {
                return (boatIndex);
            }

        }// end of for loop
        System.out.println("Cannot find boat " + boatNameRemoved);
        return (-1);

    }// end of findBoatIndex


    /**
     * A function for testing the Expenses of a boat
     * @param fleet is a arrayList of objects
     */
    public static void expense(ArrayList<Boat> fleet) {
        //Variables
        String boatName;
        int boatIndex;
        double tempAmount;

        keyboard.nextLine();
        System.out.print("Which boat do you want to spend on?         :");
        boatName = keyboard.nextLine();

        boatIndex = findBoatIndex(fleet, boatName);

        if (boatIndex >= 0) {
            Boat currentBoat = fleet.get(boatIndex);

            double boatExpense;
            System.out.print("How much do you want to spend?              : ");
            boatExpense = keyboard.nextDouble();

            if (currentBoat.isApprovedCost(boatExpense)) {
                System.out.printf("Expense authorized, " + "%.2f" + " spent.\n", currentBoat.getMaintainCost());
            } else {
                tempAmount = currentBoat.getPurchasePrice() - currentBoat.getMaintainCost();
                System.out.printf("Expense not permitted, only  %.2f", tempAmount);
                System.out.println(" left to spend.");
            }// end of if-else statements


        }// end of if statement


    }// end of expense method


}// end of Fleet Management System Class




