import java.io.Serializable;
import java.util.ArrayList;


/**
 * BoatInfo class is used to create new BoatInfo Objects
 * this class also interprets and records user input data to edit attributes about different BoatInfo Objects
 *
 */

public class BoatInfo implements Serializable {
    //declare constants and variables that will act as object variables
    private static final double MAX_PRICE = 1000000.0;
    public static final double INITIAL_MAINTENANCE = 0.0;
    public static final int MAX_LENGTH = 100;

    String boatType;
    String boatName;
    int year;
    String model;
    int length;
    double price;
    double maintainExpenses;




    /**
     *Constructor used to build new BoatInfoObjects
     * The main class will store new Objects in the ArrayList that constantly keeps track of the boats
     * @param boatType - Type of boat
     * @param boatName - name of the boat
     * @param year - year boat was made
     * @param model - boat model
     * @param length - boat length
     * @param price - initial boat price
     * @param maintainExpenses - How much has been spent on a boat's maintenance
     */
    public BoatInfo(String boatType, String boatName, int year, String model, int length, double price, double maintainExpenses) {
        this.boatType = boatType;
        this.boatName = boatName;
        this.year = year;
        this.model = model;
        this.length = length;
        this.price = price;
        this.maintainExpenses = maintainExpenses;

    }//end of BoatInfo constructor-----



    /**
     *
     * Calculates total price of all the boats added up
     * @param myBoats receives and interprets data about the boat objects in the ArrayList
     * @return returns total price of all boats added
     */

    public static double calculateTotalPrice(ArrayList<BoatInfo> myBoats) {

        double totalPrice = 0;
        for (int i = 0; i < myBoats.size(); i++) {
            totalPrice = totalPrice +  myBoats.get(i).price;
        }

        return totalPrice;
    }// end of calculateTotal method






    /**
     * Calculates total Maintenance price of all boats added up
     * @param myBoats- boat objects arraylist
     * @return returns total maintenance cost
     */

    public static double calculateTotalMaintenance(ArrayList<BoatInfo> myBoats) {

        double totalIndividualMaintenance = 0;
        int i;
        for (i = 0; i < myBoats.size(); i++) {
            totalIndividualMaintenance = totalIndividualMaintenance +  myBoats.get(i).maintainExpenses;
        }
        return totalIndividualMaintenance;

    }// end of calculateTotalMaintenance method



    /**
     *
     * @return formatted string used to print out fleet data
     */

    @Override
    public String toString(){
        return String.format("%-10s %-15s %-5s %-15s"  + "%-2s" + "'" + ":   " +"paid $"+"%-10.2f" + ": Spent $"+"%-10.2f",boatType,boatName,year,model,length,price,maintainExpenses);

    }//end of toString




    /**
     * this method is used to add new user inputed data as a new boat object in the myBoats ArrayList
     * @param myBoats - boat objects arraylist
     * @param newBoat - receives user inputted data as a csv string
     */

    //method to add objects to the myBoats array

    public static void add(ArrayList<BoatInfo> myBoats, String newBoat){

        String[] newBoatArray = newBoat.split(",");
        try{
            int convertYear = Integer.parseInt(newBoatArray[2]);
            int convertLength = Integer.parseInt(newBoatArray[4]);
            double convertPrice = Double.parseDouble(newBoatArray[5]);

            //making sure user inputs are in range for real world values
            if (convertPrice > MAX_PRICE){
                System.out.println("price entered must be less than 1 million");
                System.out.println("boat not added");

            } else if (convertLength > MAX_LENGTH) {
                System.out.println("New boats must be less than 100 feet, boat not logged");

            } else {
                myBoats.add(new BoatInfo(newBoatArray[0], newBoatArray[1], convertYear, newBoatArray[3], convertLength, convertPrice, INITIAL_MAINTENANCE));
            }

        }//end of the try block
        catch (Exception e){
            System.out.println("an invalid data type or format has been entered");
            System.out.println("type (A) again and try again");

        }//end of catch block
    }// end of the add method-----------------------------------------------






    /**
     * Checks to see if the boat name inputted by user (boatRemoval) matches with a boat name that exists in the myBoats Arraylist
     * @param myBoats - boat objects arraylist
     * @param boatRemoval - Boat name inputted by user to be removed
     * @return returns a boolean value of whether or not a boat could be recognized
     */

    //checks to see if the boat entered by the user is found in the ArrayList
    public static boolean isBoatFound(ArrayList<BoatInfo> myBoats, String boatRemoval){
        boolean boatFound = false;
        int i;
        for (i = 0; i < myBoats.size(); i++) {

            //updates condition of boat found or not found
            if ((myBoats.get(i).boatName).equalsIgnoreCase(boatRemoval)) {
                boatFound = true;
                break;

            }//end of if statement
            else if (!(myBoats.get(i).boatName).equalsIgnoreCase(boatRemoval)) {
                boatFound = false;
            }

        }//end of for loop
        return boatFound;
    }//end of isBoatFoundMethod







    /**
     * Allows user to remove a boat object from the myBoats ArrayList
     * @param myBoats - boat objects arraylist
     * @param boatRemoval - user inputted boat name they wish to remove from the fleet
     */

    public static void remove(ArrayList<BoatInfo> myBoats, String boatRemoval){
        boolean boatFound = false;
        int i;
        for (i = 0; i < myBoats.size(); i++) {
            //updates condition of boat found or not found

                
                if ((myBoats.get(i).boatName).equalsIgnoreCase(boatRemoval)) {
                    boatFound = true;
                    myBoats.remove(i);
                    break;

                }//end of if statement
                else if (!(myBoats.get(i).boatName).equalsIgnoreCase(boatRemoval)) {
                    boatFound = false;
                }

        }//end of for loop
        //display whether or not the boat was found

        if (boatFound == true){
            System.out.println("boat " + boatRemoval + " was succesfully removed");

        }//end of if statement
        else {
            System.out.println("boat " + boatRemoval + " not found");

        }//end of else statement


    }//end of remove method









    /**
     * Used to calculate whether or not more money can be spent on boat
     * @param myBoats - boat objects arraylist
     * @param boatExpense - boat that user wishes to spend on
     * @param userSpend - Amount a user is trying to spend on boat maintenance
     */

    public static void expense(ArrayList<BoatInfo> myBoats, String boatExpense, double userSpend){
        boolean boatFound = false;
        int foundBoatindex = 0;
        for (int i = 0; i < myBoats.size(); i++) {
            //updates condition of boat found or not found


            if ((myBoats.get(i).boatName).equalsIgnoreCase(boatExpense)) {
                boatFound = true;
                foundBoatindex = i;
                break;

            }//end of if statement
            else if (!(myBoats.get(i).boatName).equalsIgnoreCase(boatExpense)) {
                boatFound = false;
            }

        }//end of for loop
        //display and confirm whether or not the boat was found

        if (boatFound == true){

            if ((myBoats.get(foundBoatindex).maintainExpenses + userSpend) < myBoats.get(foundBoatindex).price){
                System.out.println("expense authorized");
                System.out.printf("$" + "%.2f" + " spent%n", (myBoats.get(foundBoatindex).maintainExpenses + userSpend));
                myBoats.get(foundBoatindex).maintainExpenses += userSpend;

            }//end of if statement
            else {
                double moneyLeft = (myBoats.get(foundBoatindex).price) - (myBoats.get(foundBoatindex).maintainExpenses);
                System.out.printf("expense not permitted, only " + "%.2f" + " can be spent%n", moneyLeft);

            }

        }//end of if statement
        else {
            System.out.println("boat " + boatExpense + " not found");


        }//end of else statement
    }//end of expense method

//create the print action of the main method in the menu select

}//end of BoatInfo class
