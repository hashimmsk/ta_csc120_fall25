package angelmartinez.dentalrecords;

import java.util.Scanner;
/**
 * Records what teeth the members of Florida families have, prints the family's teeth record,
 * extracts a tooth, and reports the family's root canal indices.
 * @author Angel Martinez
 */
public class DentalRecords {

    /**
     * Global Scanner object to use keyboard
     */
    private static final  Scanner keyboard = new Scanner(System.in);

    /**
     * Represents the upper and lower layers of teeth
     */
    private static final int UPPERS_AND_LOWERS_IN_ARRAY = 2;

    /**
     * The maximum number of teeth a layer can have, including missing teeth
     */
    private static final int MAX_AMOUNT_OF_TEETH = 8;

    /**
     * The sentinel value used for the main while loop
     */
    private static final char SENTINEL_CHAR = 'y';

    /**
     * The main method
     * @param args Passed in from the command line
     */
    public static void main(String[] args) {

        // Variables to hold user inputs, menu break and indexes
        String userInput;
        String teethString;
        char sentinelValue = 'n';
        int familySize;
        int index;
        int teethIndex;

        // Prompt user for initial number of family members
        System.out.println("Welcome to the Floridian Tooth Records\n" + "--------------------------------------");
        System.out.print("Please enter number of people in the family         : ");
        familySize = keyboard.nextInt();
        keyboard.nextLine();

        // Validate inputted number of family, maximum number is 6
        while (familySize < 1 || familySize > 6) {

            System.out.print("Invalid number of people, try again         : ");
            familySize = keyboard.nextInt();
            keyboard.nextLine();


        }// End of verification loop

        // Create array of family member names
         String[] names = new String[familySize];

        // Create array each family member's lower and upper layers of teeth
        char[][][] teeth = new char[familySize][UPPERS_AND_LOWERS_IN_ARRAY][MAX_AMOUNT_OF_TEETH];

        // Input loop for created names and teeth arrays
        for (index = 0; index < familySize; index++) {

            // Prompt user for name of family member
            System.out.print("Please enter the name for family member " + (index + 1) + "    : ");
            names[index] = keyboard.nextLine();


            // Prompt user for upper layer and validate it using the isValidTeethRow method
            System.out.print("Please enter the uppers for " + names[index] + "       :  ");
            do {

                teethString = keyboard.nextLine().toUpperCase();

            } while (!isValidTeethRow(teethString));

            // Input upper layer information
            for (teethIndex = 0; teethIndex < teethString.length() && teethIndex < MAX_AMOUNT_OF_TEETH; teethIndex++) {
                teeth[index][0][teethIndex] = teethString.charAt(teethIndex);
            }

            // Prompt user for lower layer and validate it using the isValidTeethRow method
            System.out.print("Please enter the lowers for " + names[index] + "       :  ");
            do {

                teethString = keyboard.nextLine().toUpperCase();

            } while (!isValidTeethRow(teethString));

            // Input upper layer information
            for ( teethIndex = 0; teethIndex < teethString.length() && teethIndex < MAX_AMOUNT_OF_TEETH; teethIndex++) {
                teeth[index][1][teethIndex] = teethString.charAt(teethIndex);
            }

        }// End of Input loop


        System.out.println();

        // Menu loop using sentinel value to repeat
        while (sentinelValue != SENTINEL_CHAR) {

            // Prompt user for menu option
            System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            userInput = keyboard.nextLine();

            // Conditionals for Print, Extract, Root and Exit
            if (userInput.charAt(0) == 'P' || userInput.charAt(0) == 'p'){

               displayRecords(names, teeth);

            } else if (userInput.charAt(0) == 'E' || userInput.charAt(0) == 'e') {

                extractTeeth(names, teeth);

            } else if (userInput.charAt(0) == 'R' || userInput.charAt(0) == 'r') {

                displayRootCanals(teeth);
                System.out.println();

            } else if (userInput.charAt(0) == 'X' || userInput.charAt(0) == 'x') {

                System.out.println("Exiting the Floridian Tooth Records :-)");
                sentinelValue = 'y';


            } else {
                System.out.println("Invalid menu option, try again");
                System.out.println();
            }


        }// End of menu loop


    } // End of main method

    /**
     * Displays stored teeth information of family
     * @param  names The array of inputted family member names
     * @param  teeth Multidimensional array for both layers of teeth
     */
    public static void displayRecords(String[] names, char[][][] teeth){

        // Variables for loop indexes and
        int nameIndex;
        int index;
        char tooth;

        // For loop for displaying each family members records
        for (nameIndex = 0; nameIndex < names.length; nameIndex++) {

            // Print family member as a header
            System.out.println(names[nameIndex]);

            // Print family member's upper layer of teeth, ignoring null in teeth array
            System.out.print("  Uppers: ");
            for (index = 0; index < teeth[nameIndex][0].length; index++) {
                tooth = teeth[nameIndex][0][index];
                if (tooth != '\0') {
                    System.out.print(" " + (index + 1) + ":" + tooth);
                }
            }

            // Print family member's lower layer of teeth, ignoring null in teeth array
            System.out.print("\n  Lowers: ");
            for (index = 0; index < teeth[nameIndex][1].length; index++) {
                tooth = teeth[nameIndex][1][index];
                if (tooth != '\0') {
                    System.out.print(" " + (index + 1) + ":" + tooth);
                }
            }

            // Formatting
            System.out.println("\n");

        }// End of display for loop

    }// End of displayRecords method

    /**
     * Displays family's root canal indices
     * @param  teeth Multidimensional array for both layers of teeth
     */
    public static void displayRootCanals(char[][][] teeth) {

        // Variables for each tooth type, indexes, the discriminant and results
        int incisors = 0;
        int bicuspids = 0;
        int molars = 0;
        int personIndex;
        int rowIndex;
        int toothIndex;
        double discriminant;
        double result;
        double rootResult;
        double rootIndices1;
        double rootIndices2;

        // Counting every tooth in family in nested for loop
        for (personIndex = 0; personIndex < teeth.length; personIndex++) {
            for (rowIndex = 0; rowIndex < teeth[personIndex].length; rowIndex++) {
                for ( toothIndex = 0; toothIndex < teeth[personIndex][rowIndex].length; toothIndex++) {
                    char t = teeth[personIndex][rowIndex][toothIndex];

                    if (t == 'I') {
                        incisors++;
                    } else if (t == 'B') {
                        bicuspids++;
                    } else if (t == 'M') {
                        molars++;
                    }
                }
            }
        }


        // Return case if no Incisors
        if (incisors == 0) {
            System.out.println("No Incisors, cannot compute the oot canal indices                   : ");
            return;
        }

        // Discriminant is used to see how many root canal indices there are
        discriminant = (bicuspids * bicuspids) + (4 * incisors * molars);

        // Conditionals for printing different amounts of root canal indices
        if (discriminant < 0) {

            System.out.println("No real root canal indices                     : ");

        } else if (discriminant == 0) {

            result = (double) -bicuspids / (2 * incisors);
            System.out.printf("One root canal at %.2f\n", result);

        } else {
            // 2 result case, given more identifying names
            rootResult = Math.sqrt(discriminant);
            rootIndices1 = (-bicuspids + rootResult) / (2 * incisors);
            rootIndices2 = (-bicuspids - rootResult) / (2 * incisors);
            System.out.printf("One root canal at %6.2f\n", rootIndices1);
            System.out.printf("Another root canal at %6.2f\n", rootIndices2);

        }// end of root canal indices conditionals

    }// End of displayRootCanals method


    /**
     * Extracts a family member's tooth and replace it in teeth array with missing tooth
     * @param  names The array of inputted family member names
     * @param  teeth Multidimensional array for both layers of teeth
     */
    public static void extractTeeth(String[] names, char[][][] teeth) {

        // Variables for indexes, inputs of layer, tooth and a boolean to validated a tooth to be extracted
        String familyInput;
        int familyNameIndex = -1;
        int index;
        int toothNumber;
        int toothRow;
        char toothRowChoice;
        boolean validToothChosen = false;

        // Prompt user for family member and validate that member is in record
        System.out.print("Which family member                         : ");
        familyInput = keyboard.nextLine();
        while (familyNameIndex == -1) {
            for (index = 0; index < names.length; index++) {
                if (familyInput.equalsIgnoreCase(names[index])) {
                    familyNameIndex = index;
                    break;
                }
            }// End of nested for loop that checks if in record

            // Conditional that prompts user for a family member again
            if (familyNameIndex == -1) {
                System.out.print("Invalid family member, try again                          : ");
                familyInput = keyboard.nextLine();
            }
        }

        // Prompt user for which layer of teeth and validate choice
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        toothRowChoice = keyboard.nextLine().toUpperCase().charAt(0);
        while (toothRowChoice != 'U' && toothRowChoice != 'L') {
            System.out.print("Invalid layer, try again                    : ");
            toothRowChoice = keyboard.nextLine().toUpperCase().charAt(0);
        }

        // Setting toothRowChoice to either the upper layer or lower layer
        if (toothRowChoice == 'U') {

            toothRow = 0;

        } else {

            toothRow = 1;

        }

        // Prompt user for a tooth from a layer
        System.out.print("Which tooth number                          : ");
        toothNumber = keyboard.nextInt();
        keyboard.nextLine();

        // While loop to validate chosen tooth
        while (!validToothChosen) {

            // Validate tooth number range
            if (toothNumber < 1 || toothNumber > MAX_AMOUNT_OF_TEETH) {

                // Invalid tooth, prompt user again
                System.out.print("Invalid tooth number, try again             : ");
                toothNumber = keyboard.nextInt();
                keyboard.nextLine();

            } else if (teeth[familyNameIndex][toothRow][toothNumber - 1] == 'M') {

                // Tooth already missing
                System.out.print("Missing tooth, try again                    : ");
                toothNumber = keyboard.nextInt();
                keyboard.nextLine();

            } else {

                // Valid tooth, extract it
                teeth[familyNameIndex][toothRow][toothNumber - 1] = 'M';
                System.out.println();
                validToothChosen = true;

            }

        }// End of validation while loop

    } // End of extractTeeth method

    /**
     * Checks that an inputted string representing a layer of teeth contains only 'I', 'B', or 'M' and has
     * a max length of 8.
     * @param  teethRow An inputted string representing a layer of teeth
     * @return true if the string representing layer of teeth is valid, false otherwise
     */
    public static boolean isValidTeethRow(String teethRow){

        // Variables for chosen tooth and for loop index
        int index;
        char tooth;

        // Conditional that return false if the String representing a layer of teeth is too long
        if (teethRow.length() > 8) {

            System.out.print("Too many teeth, try again                   : ");
            return false;

        }

        // For loop that checks if a Character besides 'I', 'M', or 'B' (ignoring case) is in the inputted String
        for  (index = 0; index < teethRow.length(); index++) {
            tooth = Character.toUpperCase(teethRow.charAt(index));
            if (tooth != 'I' && tooth != 'M' && tooth != 'B') {

                // If Character found, return false
                System.out.print("Invalid teeth types, try again              : ");
                return false;

            }

        } // End of checking for loop

        // Otherwise layer of teeth is valid, returning true
        return true;

    }// End of isValidTeethRow method

} // end of DentalRecords class

