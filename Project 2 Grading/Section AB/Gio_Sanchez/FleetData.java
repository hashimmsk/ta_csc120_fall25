import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 *
 *fleet data contains the methods that takes user interface and presents GUI
 *  uses the BoatInfo class as a helper to interpret user input
 * @author Giovanni Sanchez
 *
 */

public class FleetData {
   private static final Scanner keyboard = new Scanner(System.in);







    /**
     * main method continually executes bulk of code and uses a for loop until user decides to terminate code
     * main method checks to see if any command line arguments are presented, if not it will attempt to read a serialized FleetData.db file
     * @param args - used to take a csv file as a command line argument if it is there
     * @throws FileNotFoundException
     */

    public static void main(String[] args) throws FileNotFoundException {
        //try to take csv file as command line argument
        String line = "";
        ArrayList<BoatInfo> myBoats = new ArrayList<>();
        String path = "";
        boolean argsExist = false;


        //attempt to populate arraylist with command line strings
        //THIS WORKS IF A FILE/COMMAND LINE ARGUMENT IS FOUND

        try {
            path = args[0];
            //used to check if command line arguments are found or not
            argsExist = true;
        }
        catch(Exception e){
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null){
                args = line.split(",");
                try {
                    int convertYear = Integer.parseInt(args[2]);
                    int convertLength = Integer.parseInt(args[4]);
                    double convertPrice = Double.parseDouble(args[5]);
                    myBoats.add(new BoatInfo(args[0], args[1], convertYear, args[3], convertLength, convertPrice, BoatInfo.INITIAL_MAINTENANCE));
                }
                catch (InputMismatchException e){
                    System.out.println("an error in input data has been detected ");
                    System.out.println("please try again making sure to separate with commas, etc... ");
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        //^^^none of this will execute if nothing is found in the command line/ argument

        //create a conditional statement that populates arraylist with serialized data if command line arguments cannot be found
        //deserialization
        //if command line arguments are not found this will instead attempt to retrieve data from the serialized file
        if (argsExist == false){
            try {

                FileInputStream fileIn = new FileInputStream("C:\\Users\\GIo\\IdeaProjects\\Project2\\FleetData.db");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                myBoats = (ArrayList<BoatInfo>) in.readObject();

            }
            catch (Exception e){
                System.out.println("serialized data could not be reserialized");
                e.printStackTrace();
            }
        }
        else{
        }
        //end of deserialization -------------------------------------------------------------------------------------------

        //welcome message and present user with option to input user data
        System.out.println();
        System.out.println("number of boats initially logged: " + myBoats.size());
        System.out.println();

        //welcome message
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("---------------------------------------------------");
        char menuSelect;
        String menuOptions = "(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it";

        //takes user input as character
        System.out.println(menuOptions);
        menuSelect = keyboard.next().charAt(0);

        //checks to see if 'x' character was entered to exit
        while(menuSelect != 'x' && menuSelect != 'X' ){
            mainMenu(menuSelect, myBoats);
            System.out.println(menuOptions);
            menuSelect = keyboard.next().charAt(0);



        }//end of while loop for menu selection--------------------

        //exit message
        System.out.println("exiting the fleet management system");

        //now serialize all object data-----------------------------------------

        try {
            FileOutputStream fileOut = new FileOutputStream("FleetData.db");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(myBoats);
            out.close();
            fileOut.close();
            System.out.println("data succesfully serialized");
        }
        catch (Exception e){
            System.out.println("object data could not be serialized");
        }

    }//end of the main method









    /**
     * creates the function that reads user input and performs the users desired fynction
     * @param menuSelect user inputted character to select a main menu option
     * @param myBoats receives arrayList of boats to further pass it to other methods
     * @throws FileNotFoundException ...
     */


    //creates function of the main menu for user selections

    public static void mainMenu(char menuSelect, ArrayList<BoatInfo> myBoats) throws FileNotFoundException {
        int i;
        switch (menuSelect){
            case 'P':
            case 'p':

                System.out.println("fleet report: ");
                for (i = 0; i < myBoats.size(); i++) {
                    System.out.println(myBoats.get(i).toString());
                }
                System.out.printf("Total:           "  + "Paid:   $" + "%.2f" + "   Spent:   $" +"%.2f", BoatInfo.calculateTotalPrice(myBoats), BoatInfo.calculateTotalMaintenance(myBoats));
                System.out.println();
                break;

            case 'A':
            case 'a':
                String newBoat;
                System.out.println("please enter new boat csv data:  ");
                newBoat = keyboard.next();
                BoatInfo.add(myBoats, newBoat);

                break;

            case 'R':
            case 'r':
                System.out.println("which boat (name) would you like to remove");
                keyboard.nextLine();
                String boatRemoval = keyboard.nextLine();
                BoatInfo.remove(myBoats, boatRemoval);


                break;

            case 'E':
            case 'e':
                String boatExpense;
                double userSpend = 0;
                System.out.println("Which boat do you want to spend on?: ");
                keyboard.nextLine();
                boatExpense = keyboard.nextLine();

                if (BoatInfo.isBoatFound(myBoats, boatExpense) == true){
                    System.out.println("how much would you like to spend: ?");
                    try {
                        userSpend = keyboard.nextDouble();
                        BoatInfo.expense(myBoats, boatExpense, userSpend);

                    } catch (InputMismatchException e){
                        System.out.println("invalid input for spend amount");

                    }
                    break;

                }//end of if statement
                else {
                    System.out.println(boatExpense + " not found");

                }//end of else statement
                break;

            default:
                System.out.println("invalid menu input please try again");

        }//end of the switch

    }//end of main Menu Method

}//end of the FleetData class
