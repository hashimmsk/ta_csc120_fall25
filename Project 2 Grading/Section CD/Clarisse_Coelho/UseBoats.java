import java.io.*;
import java.util.*;


/**
 * Handles user interface, menu, fleet operations, and file handling.
 * Manages a collection of Boats objects using an ArrayList<Boats>..
 * @author Clarisse Coelho
 */


public class UseBoats {


    /**
     * Max length of a boat is 100 feet
     */

    private static final int MAX_BOAT_LENGTH = 100;

    /**
     * Global Scanner object to use keyboard
     */

    private static final Scanner keyboard = new Scanner(System.in);

    //==============================Main Method======================================================

    /**
     * Main method.
     * Loads initial fleet data from a CSV file.
     * Calls the method that reads the .db file—creating it if needed,
     * or otherwise creating objects from the CSV file.
     * Displays a menu with options to Print, Add, Remove, Expense, or Exit.
     * If selected, the first 3 menu options call their respective methods for execution.
     * Exit menu option also creates/overwrites the .db file when it is called.
     *
     * @param args Command-line arguments, including the fleet CSV file (FleetData.csv).
     */
    public static void main(String[] args) {

            ArrayList<Boats> boatsArray = new ArrayList<>();

            String dbFileName = "FleetData.db";

            boatsArray = readFromDBfile(boatsArray,dbFileName, args);


        //==============================MENU======================================================


        System.out.println("Welcome to the Fleet Management System\n" + "--------------------------------------\n");

        String menuOption;
        boolean validOption = true;

       do {
                System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
                menuOption = keyboard.next();

                if (menuOption.equalsIgnoreCase("P")) {

                    printingBoats(boatsArray);

                } else if (menuOption.equalsIgnoreCase("A")) {

                    addingBoatManually(boatsArray);

                } else if (menuOption.equalsIgnoreCase("R")) {

                    removingBoat(boatsArray);

                } else if (menuOption.equalsIgnoreCase("X")) {
                    System.out.println("Exiting the Fleet Management System\n");

                    writeInDBfile(boatsArray,dbFileName);


                } else if(menuOption.equalsIgnoreCase("E")){

                    expenseMaintenanceCheck(boatsArray);


                }else{
                    System.out.println("Invalid menu option, try again");

                }
       }while(!menuOption.equalsIgnoreCase("X"));

    } // end of main method

    //==============================Fleet Operations Methods======================================================

    /**
     * Creates a new Boat object based on the input from the user.
     * @param boatsArray Array list made of Boat objects.
     */
    public static void addingBoatManually(ArrayList<Boats> boatsArray){


        //---- Variables of the method
        String boatData;

        String name;
        Boats.typeOfBoat boatType = Boats.typeOfBoat.POWER;
        int year;
        String model;
        int length;
        double purchasePrice;
        double maintenanceExpenses = 0.0;

        //=====================METHOD
        keyboard.nextLine(); // fixing the buffer reader from the menu

        System.out.print("Please enter the new boat CSV data          : "); //Format ref: SAILING,Finesse,1974,Tartan,34,9200.50

        boatData = keyboard.nextLine();

       try {
           String[] boatInformation = boatData.split(",");

           //------Assigning values

           if (boatInformation[0].equalsIgnoreCase("SAILING")) {
               boatType = Boats.typeOfBoat.SAILING;
           }

           name = boatInformation[1];
           year = Integer.parseInt(boatInformation[2]);
           model = boatInformation[3];
           length = Integer.parseInt(boatInformation[4]);
           purchasePrice = Double.parseDouble(boatInformation[5]);

           // ----- Checks if length is according

           if(length<=MAX_BOAT_LENGTH && length>0){

               //------ Creates the boat object based on the information provided in the string

               boatsArray.add(new Boats(boatType,name,year,model,length,purchasePrice, maintenanceExpenses));
               //System.out.println("Boat added");

           }else {
               System.out.println("Boat not added. Length must be positive and up to 100 feet");
           }




       } catch (ArrayIndexOutOfBoundsException e) {

           System.out.println("Invalid boat information");
       }






    } // end of addingBoat method

    /**
     * Removes a boat from the ArrayList based on the name provided by the user.
     * Uses findingBoatIndex to locate the boat and then removes it from the ArrayList.
     *
     * @param boatsArray Array list made of Boat objects.
     */
    public static void removingBoat(ArrayList<Boats> boatsArray){

        String boatName;
        int boatIndex;

        keyboard.nextLine(); // fix MENU buffer

        System.out.print("Which boat do you want to remove?           : ");
        boatName = keyboard.nextLine();

        boatIndex = findingBoatIndex(boatsArray,boatName);


        if(boatIndex==-1){

            System.out.println("Cannot find boat " + boatName);

        }else{
            boatsArray.remove(boatIndex);
        }


    } // end of the removing Boat method

    /**
     * Finds the index of a desired boat in the ArrayList
     *
     * @param boatsArray Array list made of Boat objects.
     * @param boatName Name of the boat user wants to find the index from.
     * @return index of the desired boat.
     */
    public static int findingBoatIndex(ArrayList<Boats> boatsArray, String boatName){

        int boatIndex=-1;

        for (int i = 0; i < boatsArray.size(); i++) {

            if(boatsArray.get(i).getName().equalsIgnoreCase(boatName)){

                boatIndex = i;
                break;
            }

        }
        return boatIndex;



    } // end of findingBoat method

    /**
     * Prints the information of all Boat objects in the Array List.
     *
     * @param boatsArray ArrayList of Boat objects.
     */
    public static void printingBoats(ArrayList<Boats> boatsArray){

        double totalSpent=0;
        double totalPaid =0;

        System.out.println("\nFleet report:");

        for (int i = 0; i < boatsArray.size(); i++) {

            System.out.println(boatsArray.get(i).toString());
            totalPaid += boatsArray.get(i).getPurchasePrice();
            totalSpent += boatsArray.get(i).getMaintenanceExpenses();

        }

        System.out.printf("Total %37s : Paid $ %8.2f : Spent $ %1.2f\n"," ", totalPaid,totalSpent);

    } // end of printingBoats method

    /**
     * Calculates and processes user-requested expenses for a boat.
     * Ensures the total expenses do not exceed the boat’s purchase value,
     * authorizes or denies the expenditure, and updates the selected Boat object accordingly.
     *
     * @param boatsArray Array list made of Boat objects.
     */
    public static void expenseMaintenanceCheck(ArrayList<Boats> boatsArray){


        String boatName;
        int boatIndex;

        keyboard.nextLine(); // fix menu buffer

        System.out.print("Which boat do you want to spend on?         : ");
        boatName = keyboard.nextLine();

        boatIndex = findingBoatIndex(boatsArray,boatName);


        if(boatIndex==-1){

            System.out.println("Cannot find boat " + boatName);

        }else{


            System.out.print("How much do you want to spend?              : ");

            double spendingAmount = keyboard.nextDouble();

            double purchasePrice = boatsArray.get(boatIndex).getPurchasePrice();
            double currentMaintenance = boatsArray.get(boatIndex).getMaintenanceExpenses();
            double maxSpending = purchasePrice - currentMaintenance;

            if(spendingAmount > maxSpending){


                System.out.printf("Expense not permitted, only $%1.2f left to spend.\n",maxSpending );

            } else if (spendingAmount<= maxSpending) {


                double totalSpending = spendingAmount+currentMaintenance;

                boatsArray.get(boatIndex).setMaintenanceExpenses(totalSpending);

                System.out.printf("Expense authorized, $%1.2f spent.\n", totalSpending);

            }


        } // end of else


    } // end of expenseMaintenance method


    //==============================File Handling Methods======================================================

    /**
     * Overwrites the .db file if already exists, or creates a new .db file if needed.
     *
     * @param boatsArray Array list made of Boat objects.
     * @param dbFileName Name of the .dbFile of stored information about the boats.
     */
    public static void writeInDBfile(ArrayList<Boats>boatsArray, String dbFileName){

        try{

            // Overwrites the .db file if already exists, or creates a new .db file

            FileOutputStream outFile = new FileOutputStream(dbFileName);
            ObjectOutputStream outObject = new ObjectOutputStream(outFile);

            outObject.writeObject(boatsArray);

            outObject.close();

        } catch (Exception e) {

            System.out.println("Error writing file: " + e.getMessage());
        }


    } // end of write in DB File

    /**
     * Loads boat data into boatsArray by trying to read from the .db file.
     * If the .db file exists, its data is loaded. If not, the method checks for a
     * provided CSV file; if found, it loads the CSV data and a new .db file will be
     * created when the program exits.
     *
     * @param boatsArray Array list made of Boat objects.
     * @param dbFileName Name of the .dbFile of stored information about the boats.
     * @param args Command-line arguments, including the fleet CSV file (FleetData.csv).
     */
    public static ArrayList<Boats> readFromDBfile(ArrayList<Boats>boatsArray, String dbFileName, String[] args){


        try{

            FileInputStream fileIN = new FileInputStream(dbFileName);
            ObjectInputStream objectIN = new ObjectInputStream(fileIN);

            // here you are loading it with the information you are retrieving from the existing db file

            boatsArray = (ArrayList<Boats>) objectIN.readObject();

            objectIN.close();


        } catch (FileNotFoundException e) {

            //If file is not found, it checks if a CSV was provided or not.
            // If provided, it reads from it. If not provided, nothing happens.

            if(args.length>0) {

                //System.out.println("DB File not found. Read info from CSV file. New .db file created when exit =)");

                String CSVFilepath = args[0];

                creatingBoatsFromCSVFile(boatsArray, CSVFilepath);
            }else{
                //System.out.println("DB File not found. No CVS file provided. New .db file created when exit =)");
            }


        } catch (Exception e) {

           // System.out.println("DB file exists but is empty/corrupted. Starting with empty list.");
            throw new RuntimeException(e);
        }

        return boatsArray;

    } // end of Read From DB File

    /**
     * Reads boat data from the specified .db file, parses the comma-separated values,
     * and uses them to create and add Boat objects to boatsArray.
     *
     * @param boatsArray Array list made of Boat objects.
     * @param dbFileName Name of the .dbFile of stored information about the boats.
     */
    public static void creatingBoatsFromCSVFile(ArrayList<Boats> boatsArray, String dbFileName){


        //-------Variables Needed to create a boat

        String name;
        Boats.typeOfBoat boatType = Boats.typeOfBoat.POWER;
        int year;
        String model;
        int length;
        double purchasePrice;
        double maintenanceExpenses = 0.0;


        try {

            //You find the file what has the data of the boats and READ the info in it (separated by commas)
            // Then you assign each value between commas to a String array. Accessing it by index now.
            //Create and object with each of the info

            FileReader fileReaderIn = new FileReader(dbFileName);

            BufferedReader bufferReader = new BufferedReader(fileReaderIn);


            String lineWithBoatInfo;
            String[] boatInformation;

            do{
                lineWithBoatInfo = bufferReader.readLine();

                if(!(lineWithBoatInfo==null)){

                    boatInformation = lineWithBoatInfo.split(",");

                    //Assigning Values based on the String read and separated into the array.

                    if (boatInformation[0].equalsIgnoreCase("SAILING")) {
                        boatType = Boats.typeOfBoat.SAILING;

                    }else{

                        boatType = Boats.typeOfBoat.POWER;
                    }

                    name = boatInformation[1];
                    year = Integer.parseInt(boatInformation[2]);
                    model = boatInformation[3];
                    length = Integer.parseInt(boatInformation[4]);
                    purchasePrice = Double.parseDouble(boatInformation[5]);

                    //-----Creating a new Boat object, and adding it to our array

                    Boats newBoat = new Boats(boatType,name,year,model,length,purchasePrice,maintenanceExpenses);

                    boatsArray.add(newBoat);



                }


            }while(!(lineWithBoatInfo==null));

            bufferReader.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Printing the ARRAY

        /*for (int i = 0; i < boatsArray.size(); i++) {

            System.out.println(boatsArray.get(i));

        }
        System.out.println("\n");

        */




    } // end of Creating Boats from CSV File


} // end of Use Boats class
