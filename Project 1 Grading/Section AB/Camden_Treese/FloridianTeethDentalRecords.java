import java.util.Scanner;

/**
 * A tool to store the upper and lower rows of up to 8 teeth for up to 6 people in a family. Can also print all the
 * teeth, extract any tooth from any row of any family member, and can also find the roots based on the quadratic
 * equation with the variables as such: I(x^2)+Bx-M.
 * @author Camden Treese
 */
public class FloridianTeethDentalRecords {

//----Declaration of a scanner object named keyboard to be used for user input
    private static final Scanner keyboard = new Scanner(System.in);

//----Declaration of constant variables
    private static final int MINIMUM_FAMILY_MEMBERS = 1;
    private static final int MAXIMUM_FAMILY_MEMBERS = 6;
    private static final int MAXIMUM_NUMBER_OF_TEETH = 8;
    private static final int UPPER_TEETH_LAYER = 0;
    private static final int LOWER_TEETH_LAYER = 1;
    private static final int NUMBER_OF_TEETH_ROWS = 2;


    /**
     *Welcomes the user and prompts for them to input how many family members they have storing this information. Then
     * calls to other methods to fill the rest. Gives menu option to print, extract root, and exit
     * @param args
     */
    public static void main(String[] args) {

//----Output statement to welcome the user
        System.out.println("Welcome to the Floridian Tooth Records\n" + "--------------------------------------");

//----Declaration of two arrays to store input information
        String[] familyNames;
        char[][][] teethInformation;

//----Declaration of two variables that will store user input
        int numberOfFamilyMembers;
        String menuChoice;

//----Output statement to prompt users to input number of people in the family
        System.out.print("Please enter number of people in the family : ");

//----Stores the input value into the int variable numberOfFamilyMembers
        numberOfFamilyMembers = keyboard.nextInt();

//----While loop to make sure the number of family members is in the range of 1-6
        while(numberOfFamilyMembers < MINIMUM_FAMILY_MEMBERS || numberOfFamilyMembers > MAXIMUM_FAMILY_MEMBERS) {

//--------Output statement to tell the user the number was invalid and to try again
            System.out.print("Invalid number of people, try again : ");

//--------Stores the input into the same int variable as before, numberOfFamilyMembers
            numberOfFamilyMembers = keyboard.nextInt();

        } // end of the while loop

//----Initializes the array of family names with the number from input stored in numberOfFamilyMembers
        familyNames = new String[numberOfFamilyMembers];

//----Initializes the array of teeth information with the number of family members and number of teeth rows
        teethInformation = new char[numberOfFamilyMembers][NUMBER_OF_TEETH_ROWS][];

//---Calls to the fillFamilyToothInformation method with the arrays familyNames and teethInformation as arguments
        fillFamilyToothInformation(familyNames, teethInformation);

//----Blank print statement for the purpose of organized output formatting
        System.out.println();

//----Output statement to prompt users about which menu option they want
        System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it : ");

//----Stores the input in the string menuChoice and then auto-capitalizes it
        menuChoice = keyboard.next();
        menuChoice = menuChoice.toUpperCase();

//----Do while loop that runs until 'x' is entered by the user
        do {

//--------If statement that acts based on the menu option chosen, has an option for an invalid input
            if (menuChoice.equals("P")){

//------------Call to the printInformation method with arguments familyNames and teethInformation
                printInformation(familyNames, teethInformation);

//------------Prompts users to select another menu option
                System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it : ");

//------------Stores the input in the string menuChoice and then auto-capitalizes it
                menuChoice = keyboard.next();
                menuChoice = menuChoice.toUpperCase();

            } else if (menuChoice.equals("E")) {

//------------Call to the extractTooth method with the arguments familyNames and teethInformation
                extractTooth(familyNames, teethInformation);

//------------Prompts users to select another menu option
                System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it : ");

//------------Stores the input in the string menuChoice and then auto-capitalizes it
                menuChoice = keyboard.next();
                menuChoice = menuChoice.toUpperCase();

            } else if (menuChoice.equals("R")) {

//------------Call to the method rootLocation with the argument teethInformation
                rootLocation(teethInformation);

//------------Prompts users to select another menu option
                System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it : ");

//------------Stores the input in the string menuChoice and then auto-capitalizes it
                menuChoice = keyboard.next();
                menuChoice = menuChoice.toUpperCase();

            } else if (menuChoice.equals("X")){

//------------Empty in order to end the do while loop so the final statement after the loop can be output

            } else {

//------------Prompts users to select another menu option
                System.out.print("Invalid menu option, try again : ");

//------------Stores the input in the string menuChoice and then auto-capitalizes it
                menuChoice = keyboard.next();
                menuChoice = menuChoice.toUpperCase();

            }

        } while (!menuChoice.equals("X"));

//----Blank print statement for the purpose of organized output formatting
        System.out.println();

//----Goodbye statement
        System.out.println("Exiting the Floridian Tooth Records :)");


    } // end of the main method


    /**
     * Prompts information on each person's name, upper teeth, and lower teeth storing them in an array. Also checks
     * to make sure the information for the teeth is valid
     * @param familyNames
     * @param teethInformation
     */
    private static void fillFamilyToothInformation (String[] familyNames, char[][][] teethInformation) {

//----Int variables used as index for the for loops and arrays
        int personIndex;
        int upperTeethIndex;
        int lowerTeethIndex;

//----Boolean variables for if the entered string is too long or has invalid characters
        boolean tooLong;
        boolean invalidCharacter;

//----String variables to hold entered teeth
        String upperTeeth;
        String lowerTeeth;

//----For loop that runs for the length of familyNames i.e. how many family members
        for (personIndex = 0; personIndex < familyNames.length; personIndex++) {

//--------Output statement to ask for the family member at the stated index
            System.out.print("Please enter the name for family member " + (personIndex + 1) + " : ");

//--------Assigns the familyNames at that index to the input
            familyNames[personIndex] =  keyboard.next();

//--------Output statement to ask for the upper rows of teeth
            System.out.print("Please enter the uppers for " + familyNames[personIndex] + " : ");

//--------Assigns the input to the string upperTeeth and auto-capitalizes it
            upperTeeth = keyboard.next();
            upperTeeth = upperTeeth.toUpperCase();

//--------Do while loop that loops until the entered teeth are valid and the proper length
            do {

//------------Initializes the boolean variables to false to test the teeth
                tooLong = false;
                invalidCharacter = false;

//------------Creates the third layer of the area in the length of the entered upper teeth
                teethInformation[personIndex][UPPER_TEETH_LAYER] = new char[upperTeeth.length()];

//------------For loop to enter the upper teeth into the array
                for (upperTeethIndex = 0; upperTeethIndex < upperTeeth.length(); upperTeethIndex++) {

//----------------Enters the teeth into the proper array index
                    teethInformation[personIndex][UPPER_TEETH_LAYER][upperTeethIndex] =
                            upperTeeth.charAt(upperTeethIndex);

//-----------------If statement that checks if the teeth entered were valid
                    if (upperTeeth.charAt(upperTeethIndex) != 'I' && upperTeeth.charAt(upperTeethIndex) != 'B'
                    && upperTeeth.charAt(upperTeethIndex) != 'M') {

//--------------------Sets invalidCharacter equal to true due to an invalid input
                        invalidCharacter = true;

//--------------------Output statement to let the user know there was an error and prompt them again
                        System.out.print("Invalid teeth types, try again : ");

//--------------------Assigns the input to the string upperTeeth and auto-capitalizes it
                        upperTeeth = keyboard.next();
                        upperTeeth = upperTeeth.toUpperCase();

//--------------------Break to ensure the program runs back through the for loop to create the proper array length
                        break;

                    }

//----------------If statement to check if the entered number of teeth was more than the max allowed
                    if (upperTeeth.length() > MAXIMUM_NUMBER_OF_TEETH) {

//--------------------Sets tooLong equal to true due to the input being too many characters
                        tooLong = true;

//--------------------Output statement to let the user know there was an error and prompt them again
                        System.out.print("Too many teeth, try again : ");

//--------------------Assigns the input to the string upperTeeth and auto-capitalizes it
                        upperTeeth = keyboard.next();
                        upperTeeth = upperTeeth.toUpperCase();

//--------------------Break to ensure the program runs back through the for loop to create the proper array length
                        break;

                    }

                } // end of inner for loop

            } while (invalidCharacter != false || tooLong != false);

//--------Output statement to ask for the lower rows of teeth
            System.out.print("Please enter the lowers for " + familyNames[personIndex] + " : ");

//--------Assigns the input to the string lowerTeeth and auto-capitalizes it
            lowerTeeth = keyboard.next();
            lowerTeeth = lowerTeeth.toUpperCase();

            do {

//------------Initializes the boolean variables to false to test the teeth
                tooLong = false;
                invalidCharacter = false;

//------------Creates the third layer of the area in the length of the entered upper teeth
                teethInformation[personIndex][LOWER_TEETH_LAYER] = new char[lowerTeeth.length()];

//------------For loop to enter the lower teeth into the array
                for (lowerTeethIndex = 0; lowerTeethIndex < lowerTeeth.length(); lowerTeethIndex++) {

//----------------Enters the teeth into the proper array index
                    teethInformation[personIndex][LOWER_TEETH_LAYER][lowerTeethIndex] =
                            lowerTeeth.charAt(lowerTeethIndex);

//-----------------If statement that checks if the teeth entered were valid
                    if (lowerTeeth.charAt(lowerTeethIndex) != 'B' && lowerTeeth.charAt(lowerTeethIndex) != 'I'
                            && lowerTeeth.charAt(lowerTeethIndex) != 'M') {

//--------------------Sets invalidCharacter equal to true due to an invalid input
                        invalidCharacter = true;

//--------------------Output statement to let the user know there was an error and prompt them again
                        System.out.print("Invalid teeth types, try again : ");

//--------------------Assigns the input to the string lowerTeeth and auto-capitalizes it
                        lowerTeeth = keyboard.next();
                        lowerTeeth = lowerTeeth.toUpperCase();

//--------------------Break to ensure the program runs back through the for loop to create the proper array length
                        break;

                    }

//----------------If statement to check if the entered number of teeth was more than the max allowed
                    if (lowerTeeth.length() > MAXIMUM_NUMBER_OF_TEETH) {

//--------------------Sets tooLong equal to true due to the input being too many characters
                        tooLong = true;

//--------------------Output statement to let the user know there was an error and prompt them again
                        System.out.print("Too many teeth, try again : ");

//--------------------Assigns the input to the string lowerTeeth and auto-capitalizes it
                        lowerTeeth = keyboard.next();
                        lowerTeeth = lowerTeeth.toUpperCase();

//--------------------Break to ensure the program runs back through the for loop to create the proper array length
                        break;

                    }

                } // end of inner for loop

            } while (invalidCharacter != false || tooLong != false);

        } // end of the outer for loop

    } // end of the fillFamilyToothInformation method

    /**
     * Prints out all family member names and their upper teeth and lower teeth
     * @param familyNames
     * @param teethInformation
     */
    private static void printInformation (String[] familyNames, char[][][] teethInformation) {

//----Creates int variables to be used as index in the loops and arrays
        int familyMemberIndex;
        int toothLayerIndex;
        int toothIndex;

//----Blank output statement for the purposes of output organization
        System.out.println();

//----For loop to print out the family members names and their upper and lower teeth
        for (familyMemberIndex = 0; familyMemberIndex < familyNames.length; familyMemberIndex ++) {

//--------Outputs the name at the familyMemberIndex
            System.out.println(familyNames[familyMemberIndex]);

//--------Middle for loop to print out which layer of teeth
            for (toothLayerIndex = 0; toothLayerIndex < teethInformation[familyMemberIndex].length;
                 toothLayerIndex++) {

//------------If statement to determine which tooth layer, upper or lower
                if (toothLayerIndex == UPPER_TEETH_LAYER) {

//----------------Output statement that outputs the tooth layer and has extra spaces at the start for formatting
                    System.out.print("    Uppers:  ");

                } else {

//----------------Output statement that outputs the tooth layer and has extra spaces at the start for formatting
                    System.out.print("    Lowers:  ");

                }

//------------Inner for loop to print out the teeth at each index
                for (toothIndex = 0; toothIndex < teethInformation[familyMemberIndex][toothLayerIndex].length;
                     toothIndex++) {

//----------------Output statement that outputs the tooth number and tooth
                    System.out.print((toothIndex +1 ) + ": " +
                            teethInformation[familyMemberIndex][toothLayerIndex][toothIndex] + "  ");

                } // end of the inner for loop

//------------Blank output statement for the purposes of output organization
                System.out.println();

            } // end of the middle for loop

        } // end of the outer for loop

//----Blank output statement for the purposes of output organization
        System.out.println();

    } // end of the printInformation method

    /**
     * Extracts teeth based on user input information about which family member, which row, and which tooth. Also checks
     * to make sure tht family member, row, and tooth exists as well as making sure the tooth is not already missing
     * @param familyNames
     * @param teethInformation
     */
    private static void extractTooth (String[] familyNames, char[][][] teethInformation) {

//----Declaration of two string variables to be used to store user input
       String enteredFamilyMember;
       String enteredLayer;

//----Declaration and initialization of boolean variables to test if the entered information is valid
       boolean familyMemberExists = true;
       boolean validToothLayer = true;
       boolean validToothNumber = true;

//----Declaration and initialization of int variables to be used as index for the loops and arrays
       int familyMemberIndex = 0;
       int toothLayerIndex = 0;
       int toothIndex = 0;
       int chosenMemberIndex = 0;

//----Output statement that prompts the user to enter the desired family member
        System.out.print("Which family member : ");

//----Stores the input in the string enteredFamilyMember
        enteredFamilyMember = keyboard.next();

//----For loop to check if the input from the user exists
        for (familyMemberIndex = 0; familyMemberIndex < familyNames.length; familyMemberIndex ++) {

//--------If statement to check if the family member name exists no matter the case
            if (enteredFamilyMember.equals(familyNames[familyMemberIndex])
                    || enteredFamilyMember.equalsIgnoreCase(familyNames[familyMemberIndex])) {

//------------Sets the chosenMemberIndex equal to the familyMemberIndex where that name was found
                chosenMemberIndex = familyMemberIndex;

//------------Sets the familyMemberExists variable to true due to the name existing
                familyMemberExists = true;

//------------Break statement to end the loop and make sure there are no processing errors
                break;


            } else {

//------------Sets the familyMemberExists variable to false due to the name not existing
                familyMemberExists = false;

            }

        } // end of the for loop

//----While loop that runs if the family member entered was invalid, runs until valid input received
        while (familyMemberExists == false) {

//--------Outputs an error message and prompts the user to try again
            System.out.print("Invalid family member, try again : ");

//----Stores the input in the string enteredFamilyMember
            enteredFamilyMember = keyboard.next();

//--------For loop to check if the input from the user exists
            for (familyMemberIndex = 0; familyMemberIndex < familyNames.length; familyMemberIndex ++) {

//------------If statement to check if the family member name exists no matter the case
                if (enteredFamilyMember.equals(familyNames[familyMemberIndex])
                        || enteredFamilyMember.equalsIgnoreCase(familyNames[familyMemberIndex])) {

//------------Sets the chosenMemberIndex equal to the familyMemberIndex where that name was found
                    chosenMemberIndex = familyMemberIndex;

//----------------Sets the familyMemberExists variable to true due to the name existing
                    familyMemberExists = true;

//----------------Break statement to end the loop and make sure there are no processing errors
                    break;


                } else {

//----------------Sets the familyMemberExists variable to false due to the name not existing
                    familyMemberExists = false;

                }

            } // end of the for loop

        } // end of the while loop

//----Output statement that prompts the user to pick a tooth layer
        System.out.print("Which tooth layer (U)pper or (L)ower : ");

//----Assigns the input to the string enteredLayer and auto-capitalizes it
        enteredLayer = keyboard.next();
        enteredLayer = enteredLayer.toUpperCase();

//----Do while loop that continues to run if an invalid input is entered
        do {

//--------If statement to check if a valid input was entered and if so which one
            if(enteredLayer.equals("U")) {

//------------Sets toothLayerIndex equal to the UPPER_TEETH_LAYER constant
                toothLayerIndex = UPPER_TEETH_LAYER;

//------------Sets validToothLayer to true due to a valid input being entered
                validToothLayer = true;

            } else if (enteredLayer.equals("L")) {

//------------Sets toothLayerIndex equal to the LOWER_TEETH_LAYER constant
                toothLayerIndex = LOWER_TEETH_LAYER;

//------------Sets validToothLayer to true due to a valid input being entered
                validToothLayer = true;

            } else {

//------------Sets validToothLayer to false due to an invalid input being entered
                validToothLayer = false;

//------------Outputs an error message and prompts the user to try again
                System.out.print("Invalid layer, try again : ");

//----Assigns the input to the string enteredLayer and auto-capitalizes it
                enteredLayer = keyboard.next();
                enteredLayer = enteredLayer.toUpperCase();

            }


        } while (validToothLayer == false);

//----Output statement that prompts the user to enter which tooth number they want
        System.out.print("Which tooth number : ");

//----Sets toothIndex equal to the input and subtracts 1 due to index starting at 0
        toothIndex = keyboard.nextInt();
        toothIndex -= 1;

//----Do while loop that continues to run while validToothNumber is set to false from an invalid input
        do {

//--------If statement that checks if the tooth exists and that it is not already missing
            if (toothIndex < 0 || toothIndex > ((teethInformation[chosenMemberIndex][toothLayerIndex].length) - 1)) {

//------------Outputs an error statement and prompts the user to try again
                System.out.print("Invalid tooth number, try again : ");

//------------Sets toothIndex equal to the input and subtracts 1 due to index starting at 0
                toothIndex = keyboard.nextInt();
                toothIndex -= 1;

//------------Sets validToothNumber to false due to an invalid input
                validToothNumber = false;

            } else if (teethInformation[chosenMemberIndex][toothLayerIndex][toothIndex] == 'M'){

//------------Outputs an error statement and prompts the user to try again
                System.out.print("Missing tooth, try again : ");

//------------Sets toothIndex equal to the input and subtracts 1 due to index starting at 0
                toothIndex = keyboard.nextInt();
                toothIndex -= 1;

//------------Sets validToothNumber to false due to an invalid input
                validToothNumber = false;

            } else {

//------------Sets the tooth at the chosen tooth number to missing as it has now been extracted
                teethInformation[chosenMemberIndex][toothLayerIndex][toothIndex] = 'M';

//------------Sets validToothNumber to true due to a valid input
                validToothNumber = true;

//------------Blank output statement for the purposes of output organization
                System.out.println();

            }


        } while (validToothNumber == false);


    } // end of the extractTooth method

    /**
     * Calculates where the roots are based on the quadratic formula with the variables as such: I(x^2)+Bx-M by counting
     * number of I teeth, B teeth, and M teeth. Also checks to make sure roots exist and if one or two exists
     * @param teethInformation
     */
    private static void rootLocation (char[][][] teethInformation) {

//----Declares int variables to be used as index for the loops and arrays
        int familyMemberIndex;
        int familyLayerIndex;
        int toothIndex;

//----Declares and initializes three double variables that will be used to count up the total types of teeth
        double totalM = 0;
        double totalI = 0;
        double totalB = 0;

//----Declares double variables that will be used to store the calculations
        double rootDiscriminant;
        double totalRootOne;
        double totalRootTwo;

//----Large for loop to add up all the types of teeth for each family member
        for (familyMemberIndex = 0; familyMemberIndex < teethInformation.length; familyMemberIndex ++) {

            for (familyLayerIndex = 0; familyLayerIndex < teethInformation[familyMemberIndex].length;
                 familyLayerIndex ++) {

                for (toothIndex = 0; toothIndex < teethInformation[familyMemberIndex][familyLayerIndex].length;
                     toothIndex++) {

//----------------If statement to check what the type of tooth is and increment that total
                    if (teethInformation[familyMemberIndex][familyLayerIndex][toothIndex] == 'M') {

                        totalM += 1;

                    } else if (teethInformation[familyMemberIndex][familyLayerIndex][toothIndex] == 'B') {

                        totalB += 1;

                    } else {

                        totalI += 1;

                    }

                } // end of the inner for loop

            } // end of the middle for loop

        } // end of the outer for loop

//----Calculates the root discriminate with the formula (B^2) - (4 * I * -M) based on the formula (B^2) - (4 * A * C)
        rootDiscriminant = (totalB * totalB) - (4 * totalI * -totalM);

//----If statement to determine how many roots to calculate based on the root discriminate
        if (rootDiscriminant == 0) {

//--------Calculates one root (due to the root discriminate being 0) based on the quadratic equation
            totalRootOne = (-totalB) / (2 * totalI);

//--------Outputs calculation results
            System.out.println("One root canal at : " + totalRootOne);

        } else if (rootDiscriminant < 0 ) {

//--------Outputs that there are no real roots due to the discriminate being a negative number
            System.out.println("There is no real roots");

        } else {

//--------Calculates the two roots based on the quadratic equation
            totalRootOne = ((-totalB) + Math.sqrt(rootDiscriminant)) / (2 * totalI);
            totalRootTwo = ((-totalB) - Math.sqrt(rootDiscriminant)) / (2 * totalI);

//--------Blank output statement for the purposes of output organization
            System.out.println();

//--------Two output statements that output the location of the two roots
            System.out.println("One root canal at : " + totalRootOne);
            System.out.println("Another root canal at : " + totalRootTwo);

//--------Blank output statement for the purposes of output organization
            System.out.println();
            
        }

    } // end of the rootLocation method


} // end of the FloridianTeethDentalRecords class
