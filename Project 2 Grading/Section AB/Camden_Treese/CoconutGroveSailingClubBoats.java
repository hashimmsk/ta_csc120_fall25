import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
 * A tool for the Coconut Grove Sailing Club to keep track of their boats. Adds boats from files and then saves them
 * when the program exits. Can also add, remove, print out info, and spend money on all the boats. Gets the initial
 * information from a CSV file anf after saves it to a new db file and then uses that file every time after. Boats are
 * created as a Boat object from the Boat class
 * @author Camden Treese
 * @see Boat
 */
public class CoconutGroveSailingClubBoats {

//----Creates a new Scanner object named keyboard for user inputs
    private static final Scanner keyboard = new Scanner(System.in);

//----Creates two constant variables for the maximum and minimum boat length
    private static final int MAXIMUM_BOAT_LENGTH = 100;
    private static final int MINIMUM_BOAT_LENGTH = 0;


    /**
     * Creates the ArrayList to store the Boats. Welcomes the user and then prompts them to enter a menu choice. Calls
     * different methods based on the menu option selected. When exiting the programming it leaves a goodbye message
     * @param args
     */
    public static void main(String[] args) {
//----Creates a string variable named menuChoice
        String menuChoice;

//----Creates a string variable named csvFile
        String csvFile;

//----If statement to see if there is no command line argument
        if (args.length == 0) {

//--------Sets csvFile equal to null
            csvFile = null;

        } else {

//--------Sets csvFile equal to the command line argument
            csvFile = args[0];
        }

//----Creates a string named csvFile and sets it equal to the command line argument


//----Creates a string named dbFile and sets it equal to the name of a dbFile
        String dbFile = "FleetData.db";

//----Creates a new ArrayList filled with Boat objects
        ArrayList<Boat> boatList = new ArrayList<>();

//----Calls to the readFromFile method and sends boatList, csvFile, and dbFile as arguments
        readFromFile(boatList, csvFile, dbFile);

//----Welcome output statement
        System.out.println("Welcome to the Fleet Management System\n" + "--------------------------------------");

//----Blank output statement for the purposes of formatting
        System.out.println();

//----Do while loop that runs until the user inputs 'X'
        do {

//--------Prints out menu options for the user
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");

//--------Sets the menuChoice variable to user input and auto-capitalizes it
            menuChoice = keyboard.next();
            menuChoice = menuChoice.toUpperCase();

//--------If statement to check what the user input and select a menu option based on the input
            if (menuChoice.equals("P")) {

//------------Calls to the printBoatInformation and sends boatList as an argument
                printBoatInformation(boatList);

            } else if (menuChoice.equals("A")) {

//------------Calls to the addBoat method and sends boatList as an argument
                addBoat(boatList);

            } else if (menuChoice.equals("R")) {

//------------Calls to the removeBoat method and sends boatList as an argument
                removeBoat(boatList);

            } else if (menuChoice.equals("E")) {

//------------Calls to the spendExpensesOnBoat method and sends boatList as an argument
                spendExpensesOnBoat(boatList);

            } else if (menuChoice.equals("X")) {

//------------Calls to the writeToFile method and sends boatList and dbFile as arguments
                writeToFile(boatList, dbFile);

//------------Break statement to end the If statement block
                break;

            } else {

//------------Prints out an error message due to an invalid input
                System.out.println("Invalid menu option, try again");

            }

        } while (!menuChoice.equals("x"));

//----Blank output statement for the purposes of formatting
        System.out.println();

//----Outputs an exit message to the user
        System.out.println("Exiting the Fleet Management System");




    } // end of the main method

//=============================================Boat menu options below===============================================

    /**
     * Prints out the information from the boatList ArrayList in a well formatted manner
     * @param boatList
     */
    private static void printBoatInformation (ArrayList<Boat> boatList) {

//----Declares two double variables and initializes them to 0
        double totalPaid = 0;
        double totalExpenses = 0;

//----Declares an int variable named index
        int index;

//----Blank output statement for the purposes of formatting
        System.out.println();

//----Output statement that outputs "Fleet report:" followed by a new line
        System.out.println("Fleet report:");

//----For loop that loops for the size of the boatList ArrayList
        for (index = 0; index < boatList.size(); index++) {

//--------Outputs the information from each boat object in boatList by calling the toString method
            System.out.println(boatList.get(index).toString(false));

//--------Sets totalPaid equal to purchase price at each index added up and rounds it to two decimal places
            totalPaid += boatList.get(index).getPurchasePrice();
            totalPaid = Math.round(totalPaid * 100.00) / 100.00;

//--------Sets totalExpenses equal to the expenses at each index added up and rounds it to two decimal places
            totalExpenses += boatList.get(index).getBoatExpenses();
            totalExpenses = Math.round(totalExpenses * 100.00) / 100.00;

        } // end of the for loop

//----Outputs the totalPaid and totalExpenses
        System.out.println("    Total : Paid $ " + totalPaid + " : Spent $ " + totalExpenses);

//----Blank output statement for the purposes of formatting
        System.out.println();

    } // end of the printBoatInformation method

    /**
     * Adds a boat to the boatList ArrayList by prompting the user to enter all the information necessary then it
     * creates a new object with those arguments and adds it boatList
     * @param boatList
     */
    private static void addBoat (ArrayList<Boat> boatList) {

//----Declares variables for all the boat values
        Boat.AcceptableBoatType boatType = null;
        String boatName;
        int manufactureYear;
        String boatMakeAndModel;
        int boatLength;
        double purchasePrice;

//----Declares a String variable named userInput
        String userInput;

//----Prompts the user to input the boat data
        System.out.print("Please enter the new boat CSV data          : ");

//----keyboard.nextLine() to skip a line and properly get the input as the value
        keyboard.nextLine();

//----Assigns the user input to the String userInput
        userInput = keyboard.nextLine();

//----Creates a String array with the values being the userInput split by commas
        String[] userInputSeparated = userInput.split(",");

//----If statement to check the boat type entered
        if (userInputSeparated[0].equals("POWER")) {

//--------Sets boatType equal to POWER from the enumerator in the Boat class
            boatType = Boat.AcceptableBoatType.POWER;

        } else if (userInputSeparated[0].equals("SAILING")) {

//--------Sets boatType equal to SAILING from the enumerator in the Boat class
            boatType = Boat.AcceptableBoatType.SAILING;

        }

//----Sets the name entered to the variable boatName
        boatName = userInputSeparated[1];

//----Sets the manufacture year entered to the variable manufactureYear
        manufactureYear = Integer.parseInt(userInputSeparated[2]);

//----Sets the make and model entered to the variable boatMakeAndModel
        boatMakeAndModel = userInputSeparated[3];

//----If statement to check and make sure the boat length entered is within the acceptable range
        if (Integer.parseInt(userInputSeparated[4]) < MAXIMUM_BOAT_LENGTH &&
                Integer.parseInt(userInputSeparated[4]) > MINIMUM_BOAT_LENGTH) {

//--------Sets the boat length entered to the variable boatLength
            boatLength = Integer.parseInt(userInputSeparated[4]);

        } else {

//--------Outputs an error message and returns to the main method
            System.out.println("Invalid boat length");
            return;

        }

//----Sets the purchase price equal to the purchasePrice entered
        purchasePrice = Double.parseDouble(userInputSeparated[5]);

/*----Creates a new Boat object and adds it to boatList based on the entered values (expenses is set to 0 because the
      boat has just been added and thus no expenses has been spent)*/
        boatList.add(new Boat(boatType, boatName, manufactureYear, boatMakeAndModel, boatLength,
                purchasePrice, 0));

//----Blank output statement for the purposes of formatting
        System.out.println();

    } // end of the addBoat method

    /**
     * Prompts the user to enter a boat name. If that name is found in boatList it removes it from the ArrayList. If it
     * is not found it lets the user know and returns back to the main method
     * @param boatList
     */
    private static void removeBoat (ArrayList<Boat> boatList) {
//----Declares and initializes two int variables for index
        int inputBoatIndex = 0;
        int index = 0;

//----Declares and initializes a boolean variable to check if the boat entered exists
        boolean boatExists = true;

//----Declares a String variable named inputBoatName
        String inputBoatName;

//----Output statement that prompts the user to enter a boat name
        System.out.print("Which boat do you want to remove?           : ");

//----keyboard.nextLine() to skip a line and properly get the input as the value
        keyboard.nextLine();

//----Assigns the input to inputBoatName
        inputBoatName = keyboard.nextLine();

//----For statement that runs for the length of the boatSize ArrayList
        for (index = 0; index < boatList.size(); index++) {

//--------If statement to check if the boat named entered exists regardless of case
            if (inputBoatName.equals(boatList.get(index).getBoatName()) ||
                    inputBoatName.equalsIgnoreCase(boatList.get(index).getBoatName())) {

//------------Sets boatExists equal to true
                boatExists = true;

//------------Sets inputBoatIndex equal to the index where the boat was found
                inputBoatIndex = index;

//------------Break statement to exit the for loop
                break;

            } else {

//------------Sets boatExists equal to false
                boatExists = false;

            } // end of the if-else statement

        } // end of the for loop

//----If statement that removes the boat if boatExists is equal to true
        if (boatExists == true) {

//--------Removes the boat at the index where the boat name was found
            boatList.remove(inputBoatIndex);

        } else {

//--------Output statement to let the user know they cannot find a boat with the input name
            System.out.println("Cannot find boat " + inputBoatName);

        } // end of the if-else statement

//----Blank output statement for the purposes of formatting
        System.out.println();

    } // end of the removeBoat method

    /**
     * Prompts the user to enter a boat name. If that name is found in boatList it then prompts them to enter how much
     * money they would like to spend. It checks to make sure that the amount spent is not more than the boat is worth.
     * If it is not then it spends it, if it is then it does not spend it and it lets the user know. If the boat is not
     * found then it lets the user know and returns to the main method.
     * @param boatList
     */
    private static void spendExpensesOnBoat (ArrayList<Boat> boatList) {
//----Declares and initializes two int variables for index
        int index = 0;
        int inputBoatIndex = 0;

//----Declares three double variables to be used to calculate boat expenses
        double inputExpenses;
        double totalInputExpenses;
        double expensesLeft;

//----Declares a String variable named inputBoatName
        String inputBoatName;

//---Declares a boolean named boatExists and initializes it to true
        boolean boatExists = true;

//----Output statement to prompt the user to enter a boat name to spend money on
        System.out.print("Which boat do you want to spend on?         : ");

//----keyboard.nextLine() to skip a line and properly get the input as the value
        keyboard.nextLine();

//----Sets the input to inputBoatName
        inputBoatName = keyboard.nextLine();

//----For loop that runs through all of boatList
        for (index = 0; index < boatList.size(); index++) {

//--------If statement to check if the input boat exists regardless of case
            if (inputBoatName.equals(boatList.get(index).getBoatName()) ||
                    inputBoatName.equalsIgnoreCase(boatList.get(index).getBoatName())) {

//------------Sets boatExists equal to true
                boatExists = true;

//------------Sets inputBoatIndex equal to the index where the boat name was found
                inputBoatIndex = index;

//------------Break statement to exit the for loop
                break;

            } else {

//------------Sets boatExists equal to false
                boatExists = false;

            } // end of the if-else statement

        } // end of the for loop

//----If statement that checks if boatExists is equal to true
        if (boatExists == true) {

//--------Output statement that prompts the user to enter the money they want to spend
            System.out.print("How much do you want to spend?              : ");

//--------Sets the input to inputExpenses and rounds it to two decimal places
            inputExpenses = keyboard.nextDouble();
            inputExpenses = Math.round(inputExpenses * 100.00) / 100.00;

//--------Sets the total expenses equal to the new expenses plus the existing expenses
            totalInputExpenses = inputExpenses + boatList.get(inputBoatIndex).getBoatExpenses();

//--------If statement to check if the purchase price of the boat is greater than the expenses spent
            if (boatList.get(inputBoatIndex).getPurchasePrice() > totalInputExpenses) {

//------------Calls to the spendMoneyOnBoat method on the Boat object at the input index
                boatList.get(inputBoatIndex).spendMoneyOnBoat(inputExpenses);

//------------Outputs a statement to let the user know the expenses were authorized and spent
                System.out.println("Expense authorized, $" + inputExpenses + " spent.");

            } else {

//------------Sets expenses left equal to the purchase price - expenses spent and rounds it to two decimal places
                expensesLeft = boatList.get(inputBoatIndex).getPurchasePrice() -
                        boatList.get(inputBoatIndex).getBoatExpenses();
                expensesLeft = Math.round(expensesLeft * 100.00) / 100.00;

//------------Output statement to let the user know the expenses were not permitted
                System.out.println("Expense not permitted, only $" + expensesLeft + " left to spend.");

            } // end of the inner if-else statement

        } else {

//--------Outputs statement to let the user know they could not find the input boat name
            System.out.println("Cannot find boat " + inputBoatName);

        } // end of the outer if-else statement

//----Blank output statement for the purposes of formatting
        System.out.println();


    } // end of the spendExpensesOnBoat method

//============================================File Handling Methods Below============================================

    /**
     * Uses multiple try-catch blocks to determine what file to read from. If it is the first run of the program the
     * db file will not exist so it will pull from the initial CSV file. Creates Boat object and adds them to boatList
     * based on the information read from the files.
     * @param boatList
     * @param csvFile
     * @param dbFile
     */
    private static void readFromFile (ArrayList<Boat> boatList, String csvFile, String dbFile){
//----Creates a BufferedReader object named fromBufferedReader to get input from a file
        BufferedReader fromBufferedReader;

//----Declares a String variable named lineReadFromFile
        String lineReadFromFile;

//----Declares a String array named boatInformation
        String[] boatInformation;

//----Declares variables for all the boat values
        Boat.AcceptableBoatType boatType = null;
        String boatName;
        int manufactureYear;
        String boatMakeAndModel;
        int boatLength;
        double purchasePrice;
        double boatExpenses;

/*----Try-catch block to open files. If the db file doesn't exist (i.e. on the first run) it will catch the FileNotFound
      exception and then attempt to open the CSV file from the command line argument. If it can't find that it throws
      another exception and tells the user to call the programmer*/
        try {

//--------Creates an input stream from the file with the file name from dbFile
            fromBufferedReader = new BufferedReader(new FileReader(dbFile));

//--------Sets lineReadFromFile equal to the line read from the file
            lineReadFromFile = fromBufferedReader.readLine();

//--------Do while loop that runs through the whole file until there is nothing left
            do {

//------------Sets the String array boatInformation with the values being the file info split by commas
                boatInformation = lineReadFromFile.split(",");

//------------If statement to check the boat type
                if (boatInformation[0].equals("POWER")) {

//----------------Sets boatType equal to POWER from the enumerator in the Boat class
                    boatType = Boat.AcceptableBoatType.POWER;

                } else if (boatInformation[0].equals("SAILING")) {

//----------------Sets boatType equal to SAILING from the enumerator in the Boat class
                    boatType = Boat.AcceptableBoatType.SAILING;

                }

//------------Sets boatName equal to the name found in the file that is stored in the array
                boatName = boatInformation[1];

//------------Sets the manufactureYear equal to the value found in the file that is stored in the array
                manufactureYear = Integer.parseInt(boatInformation[2]);

//------------Sets the boatMakeAndModel equal to the value found in the file that is stored in the array
                boatMakeAndModel = boatInformation[3];

/*------------Sets the boatLength equal to the value found in the file that is stored in the array (There is no need to
              check the bounds because if it is coming from a file it has already been checked and approved*/
                boatLength = Integer.parseInt(boatInformation[4]);

//------------Sets the purchasePrice equal to the value found in the file that is stored in the array
                purchasePrice = Double.parseDouble(boatInformation[5]);

//------------Sets the boatExpenses equal to the value found in the file that is stored in the array
                boatExpenses = Double.parseDouble(boatInformation[6]);

//------------Adds a new Boat object to the boatList with the specified arguments from the file
                boatList.add(new Boat(boatType, boatName, manufactureYear, boatMakeAndModel, boatLength,
                        purchasePrice, boatExpenses));

//------------Reads the next line from the file
                lineReadFromFile = fromBufferedReader.readLine();

            } while (lineReadFromFile != null);

//--------Closes the input stream
            fromBufferedReader.close();


        } catch (FileNotFoundException e) {

            try {

//------------Creates an input stream from the file with the file name from csvFile
                fromBufferedReader = new BufferedReader(new FileReader(csvFile));

//------------Sets lineReadFromFile equal to the line read from the file
                lineReadFromFile = fromBufferedReader.readLine();

//------------Do while loop that runs through the whole file until there is nothing left
                do {

//----------------Sets the String array boatInformation with the values being the file info split by commas
                    boatInformation = lineReadFromFile.split(",");

//----------------If statement to check the boat type
                    if (boatInformation[0].equals("POWER")) {

//--------------------Sets boatType equal to POWER from the enumerator in the Boat class
                        boatType = Boat.AcceptableBoatType.POWER;

                    } else if (boatInformation[0].equals("SAILING")) {

//--------------------Sets boatType equal to SAILING from the enumerator in the Boat class
                        boatType = Boat.AcceptableBoatType.SAILING;

                    }

//----------------Sets boatName equal to the name found in the file that is stored in the array
                    boatName = boatInformation[1];

//----------------Sets the manufactureYear equal to the value found in the file that is stored in the array
                    manufactureYear = Integer.parseInt(boatInformation[2]);

//----------------Sets the boatMakeAndModel equal to the value found in the file that is stored in the array
                    boatMakeAndModel = boatInformation[3];

/*----------------Sets the boatLength equal to the value found in the file that is stored in the array (There is no need
                  to check the bounds because if it is coming from the initial file*/
                    boatLength = Integer.parseInt(boatInformation[4]);

//----------------Sets the purchasePrice equal to the value found in the file that is stored in the array
                    purchasePrice = Double.parseDouble(boatInformation[5]);

/*----------------Adds a new Boat object to the boatList with the specified arguments from the file (boat expenses is 0
                  because it is coming from the initial value where no expenses have been spent*/
                    boatList.add(new Boat(boatType, boatName, manufactureYear, boatMakeAndModel, boatLength,
                            purchasePrice, 0));

//----------------Reads the next line from the file
                    lineReadFromFile = fromBufferedReader.readLine();

                } while (lineReadFromFile != null);

//------------Closes the input stream
                fromBufferedReader.close();


            } catch (FileNotFoundException exception) {

//------------Output statement to let the user know no files are working, they need to call the programmer
                System.out.println("ERROR! NO FILES WORKING! CALL THE PROGRAMMER!");

            } catch (IOException exception) {

//------------Error statement is output
                System.out.println("ERROR!");

            } // end of the inner try-catch block


        } catch (IOException e) {

//--------Error statement is output
            System.out.println("ERROR!");

        } // end of the outer try-catch block

    } // end of the readFromFile method

    /**
     * Saves the information from boatList to a db file. If it is the first run and does not exist the file will be
     * created for the user and then the information will be saved.
     * @param boatList
     * @param dbFile
     */
    private static void writeToFile (ArrayList<Boat> boatList, String dbFile) {

//----Declares a PrintWriter object named toPrintWriter to be able to save to a file
        PrintWriter toPrintWriter;

//----Declares an int variable named lineNumber
        int lineNumber;

//----Try-catch block to try and save to the file name from the variable dbFile
        try {

//--------Opens an output stream to the file from dbFile
            toPrintWriter = new PrintWriter(new FileOutputStream(dbFile));

//--------for statement to save the info from boatList to the file
            for (lineNumber = 0; lineNumber < boatList.size(); lineNumber++) {

//------------Saves the info gotten from the boatList toString method when closing a file at each index
                toPrintWriter.println(boatList.get(lineNumber).toString(true));

            }

//--------Closes the file stream
            toPrintWriter.close();

        } catch (FileNotFoundException e){

//--------Lets the user know there has been an error and the file has not been found
            System.out.println("FILE NOT FOUND!");
        }

    } // end of the writeToFile method


} // end of the CoconutGroveSailingClubBoats class
