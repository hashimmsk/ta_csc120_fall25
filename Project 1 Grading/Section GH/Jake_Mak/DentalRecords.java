import java.util.Scanner;
/**
 * The purpose of this program is to record what teeth the members of Florida families have
 * @author Jake Maksimiak
 */
public class DentalRecords {
    /**
     * Global Scanner object to use keyboard
     */
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * The maximum family size, as given in the instructions
     */
    public static final int MAXIMUM_FAMILY_SIZE = 6;

    /**
     * The maximum amount of teeth that can be recorded, as given in the instructions
     */
    public static final int MAXIMUM_AMOUNT_OF_TEETH = 8;

    /**
     * The main method, which will be used to record the family members' names, and teeth data for each family member
     * @param args Passed in from the command line
     */
    public static void main(String[] args) {

        // Declare variables for family and teeth data

        int familySize;
        int index;
        int index1;
        String uppers;
        String lowers;
        char checkinput;
        char toothAt;
        String lengthOfTeeth;

        // Get input data

        // Welcome to the user
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");


        // Get the number of family members first
        System.out.println("Please enter the number of people in the family:");
        familySize = keyboard.nextInt();


        // Verification to see that the number of people is eligible
        do {

            if (familySize > MAXIMUM_FAMILY_SIZE || familySize < 1) {
                System.out.println("Invalid number of people, try again:");
                familySize = keyboard.nextInt();
            } // end of if loop

        } while (familySize > MAXIMUM_FAMILY_SIZE || familySize < 1); // end of do while loop

        // Declaration of arrays
        String uppersArray[] = new String[familySize]; // holds the uppers data for each family member
        String lowersArray[] = new String[familySize]; // holds the lowers data for each family member
        String [] familyNames = new String[familySize]; // holds each family members name

        index = 0;
        familyNames [index] = keyboard.nextLine(); // For some reason the code only runs properly with this line


        // Do-while loop will add all the uppers, lowers, and name data to arrays

            do {

                // Names of the family members

                System.out.println("Please enter the name for family member " + (index + 1) + ":");
                familyNames [index] = keyboard.nextLine();

                // Input data for uppers

            System.out.println("Please enter the uppers for " + familyNames[index] + ":");
            uppers = keyboard.nextLine();
            uppers = uppers.toUpperCase();

            // For loop cycles through each character entered
            for (index1 = 0; index1 < uppers.length(); index1++) {

                // First if loop to check if input data matches syntax

                    checkinput = uppers.charAt(index1);
                if ((checkinput != 'B') && (checkinput != 'I') && (checkinput != 'M')) {
                    System.out.println("Invalid teeth types, try again:");
                    uppers = keyboard.nextLine();
                    uppers = uppers.toUpperCase();

                    index1 = -1; // resets the for loop

                } // end of first if loop

                // Second if loop to check if input data length is correct

                if (uppers.length() > MAXIMUM_AMOUNT_OF_TEETH) {
                    System.out.println("Too many teeth, try again:");
                    uppers = keyboard.nextLine();
                    uppers = uppers.toUpperCase();

                    index1 = -1; // resets the for loop

                } // end of second  if loop

            } // end of inner for loop

                // Add uppers data to the array for the whole family
            uppersArray [index] = uppers;


        // Input data for lowers

            System.out.println("Please enter the lowers for " + familyNames[index] + ":");
            lowers = keyboard.nextLine();
            lowers = lowers.toUpperCase();

            // For loop cycles through each character entered
            for (index1 = 0; index1 < lowers.length(); index1++) {

                // First if loop to check if input data matches syntax

                checkinput = lowers.charAt(index1);
                if ((checkinput != 'B') && (checkinput != 'I') && (checkinput != 'M')) {
                    System.out.println("Invalid teeth types, try again:");
                    lowers = keyboard.nextLine();
                    lowers = lowers.toUpperCase();

                    index1 = -1; // resets the for loop

                } // end of first if loop

                // Second if loop to check if input data length is correct

                if (lowers.length() > MAXIMUM_AMOUNT_OF_TEETH) {
                    System.out.println("Too many teeth, try again:");
                    lowers = keyboard.nextLine();
                    lowers = lowers.toUpperCase();

                    index1 = -1; // resets the for loop

                } // end of second if loop

            } // end of inner for loop

                // Add lowers data to the array for the whole family
                lowersArray [index] = lowers;

                index = index + 1;

            } while (index < familySize); // end of do while loop

        /* teethData array stores the following:
         family name in the first operator
         upper or lower in the second operator
         the data for each tooth in the third operator
         */
        String teethData [][][] = new String [familyNames.length][2][];

        // Combine the name, uppers, and lowers arrays into a single array
        // First for loop cycles through each family member

        for (index = 0; index < familySize; index++) {

            // Initialize the array lengths
            /* For teethData array:
            [][0][] corresponds to the lowers
            [][1][] corresponds to the uppers
             */

            // Lowers lengths
            lengthOfTeeth = lowersArray[index];
           teethData [index][0] = new String [lengthOfTeeth.length()];

           // Uppers lengths
           lengthOfTeeth = uppersArray[index];
           teethData [index][1] = new String [lengthOfTeeth.length()];

           // first nested for loop in order to initialize lowers
           for (index1 = 0; index1 < lowersArray[index].length(); index1++) {

               toothAt = lowersArray[index].charAt(index1);
               teethData [index][0][index1] = String.valueOf(toothAt);

           } // end of second (nested) for loop

            // second nested for loop in order to initialize uppers
            for (index1 = 0; index1 < uppersArray[index].length(); index1++) {

                toothAt = uppersArray[index].charAt(index1);
                teethData [index][1][index1] = String.valueOf(toothAt);

            } // end of second inner for loop

        } // end of outer for loop

        // End of data inputs; the teethData array contains all the family member names, and teeth data

        selectMenuOptions(familyNames, teethData);

    } // end of main method

    /**
     * The void method which gives the user 4 options: print the family's dental record, extract a tooth, view the root canal indices, or exit the program
     * @param familyNames the array which contains all of the names of the family members
     * @param teethData the array which contains the teeth data for all of the family members
     */
        public static void selectMenuOptions(String[] familyNames, String[][][] teethData) {

        // Declare local variables, to be used with the menu options

            // Variables for the switch and loops
            String menuLetter;
            boolean repeatSwitch;
            int index, index1;

            // Variables for the Extract option
            String familyMemberName;
            int familyMemberNumber = 0;
            boolean familyMemberFound;
            String upperOrLower;
            int toothNumber;

            // Variables for the Root option
            int sumOfFamilyI;
            int sumOfFamilyB;
            int sumOfFamilyM;
            double a;
            double b;
            double c;
            double rootCanalOne;
            double rootCanalTwo;

            // Give the user the menu options
            System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it:");
            menuLetter = keyboard.nextLine();
            menuLetter = menuLetter.toUpperCase();

            // Do-while loop will cycle through the menu options until the user exits
            do {

                // Switch statement to run the option the user selected
                switch (menuLetter) {
                    case ("P"): // Print family's dental record

                        // For loop to cycle through the family's dental record, one person at a time
                        for (index = 0; index < familyNames.length; index++) {

                            System.out.println(familyNames[index]);

                            // Print uppers for the family member first
                            System.out.print("  Uppers:  ");
                            for (index1 = 0; index1 < teethData[index][1].length; index1++) {
                                System.out.print((index1 + 1) + ":" + teethData[index][1][index1] + "  ");
                            } // end of first nested for loop

                            // Print lowers for the family member second
                            System.out.println();
                            System.out.print("  Lowers:  ");
                            for (index1 = 0; index1 < teethData[index][0].length; index1++) {
                                System.out.print((index1 + 1) + ":" + teethData[index][0][index1] + "  ");
                            } // end of second nested for loop
                            System.out.println();
                        } // end of outer for loop

                        repeatSwitch = true; // To repeat the switch

                        // Give the user the menu options again
                        System.out.println();
                        System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it:");
                        menuLetter = keyboard.nextLine();
                        menuLetter = menuLetter.toUpperCase();

                        break;

                    case ("E"): // Extract a tooth

                        // Ask for which family member
                        System.out.println("Which family member:");
                        familyMemberName = keyboard.nextLine();
                        familyMemberName = familyMemberName.toUpperCase();

                        // Do-while loop to check if the family member name is valid

                        familyMemberFound = false;
                        index = 0;

                        do {

                                // Checks if the family member name is valid
                                if (familyMemberName.equals(familyNames[index].toUpperCase())) {

                                    familyMemberNumber = index;
                                    familyMemberFound = true;

                                } // end of first if loop

                                // If the family name isn't valid, the user is prompted to enter again
                                if ((index == (familyNames.length - 1)) && !familyMemberFound) {

                                    System.out.println("Invalid family member, try again:");
                                    familyMemberName = keyboard.nextLine();
                                    familyMemberName = familyMemberName.toUpperCase();

                                    index = -1; // restarts the do-while loop

                                } // end of second if loop

                            index++;

                        } while (!familyMemberFound);// end of do while loop

                        // Ask the user to extract an upper or lower tooth

                        System.out.println("Which tooth layer (U)pper or (L)ower:");
                        upperOrLower = keyboard.nextLine();
                        upperOrLower = upperOrLower.toUpperCase();

                        // Verify that an eligible character was entered

                        do if (!upperOrLower.equals("U") && !upperOrLower.equals("L")) {

                            System.out.println("Invalid layer, try again:");
                            upperOrLower = keyboard.nextLine();
                            upperOrLower = upperOrLower.toUpperCase();

                        } while (!upperOrLower.equals("U") && !upperOrLower.equals("L")); // end of do-if loop

                        // Switch statement in order to separate the extraction of an upper or lower tooth

                        switch (upperOrLower) {
                            case ("U"):

                                // Asking for a tooth number and verifying that the tooth exists

                                System.out.println("Which tooth number: ");
                                toothNumber = keyboard.nextInt();

                                do {

                                    // Checks if tooth is in range
                                    if ((toothNumber > teethData[familyMemberNumber][1].length) || (toothNumber < 1)) {

                                        System.out.println("Invalid tooth number, try again:");
                                        toothNumber = keyboard.nextInt();
                                    } // end of first inner if loop

                                    // Checks if tooth is present and not missing
                                    if (teethData[familyMemberNumber][1][toothNumber - 1].equals("M")) {
                                        System.out.println("Missing tooth, try again:");
                                        toothNumber = keyboard.nextInt();
                                    } // end of second inner if loop

                                } while ((toothNumber > teethData[familyMemberNumber][1].length) || (toothNumber < 1) || teethData[familyMemberNumber][1][toothNumber - 1].equals("M"));
                                // end of do while loop

                                // Changing the tooth data
                                teethData[familyMemberNumber][1][toothNumber - 1] = "M";
                                break;

                            case ("L"):

                                // Asking for a tooth number and verifying that the tooth exists

                                System.out.println("Which tooth number: ");
                                toothNumber = keyboard.nextInt();

                                do {

                                    // Checks if tooth is in range
                                    if ((toothNumber > teethData[familyMemberNumber][0].length) || (toothNumber < 1)) {

                                        System.out.println("Invalid tooth number, try again:");
                                        toothNumber = keyboard.nextInt();

                                    } // end of first inner if loop

                                    // Checks if tooth is present and not missing
                                    if (teethData[familyMemberNumber][0][toothNumber - 1].equals("M")) {
                                        System.out.println("Missing tooth, try again:");
                                        toothNumber = keyboard.nextInt();

                                    } // end of second inner if loop

                                } while ((toothNumber > teethData[familyMemberNumber][0].length) || (toothNumber < 1) || teethData[familyMemberNumber][0][toothNumber - 1].equals("M"));
                                // end of do while loop

                                // Changing the tooth data
                                teethData[familyMemberNumber][0][toothNumber - 1] = "M";
                                break;
                        } // end of Upper or Lower switch statement

                            repeatSwitch = true; // To repeat the switch

                            // Prompt the user with menu options again
                            System.out.println();
                            System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it:");
                            menuLetter = keyboard.nextLine();
                            menuLetter = keyboard.nextLine(); // for some reason the code only runs properly with this line
                            menuLetter = menuLetter.toUpperCase();

                            break;

                            case ("R"): // Calculate the family's root canal indices

                                // Set the sums to 0
                                // This is so that if a tooth is extracted, the root canals will be accurate

                                sumOfFamilyI = 0;
                                sumOfFamilyB = 0;
                                sumOfFamilyM = 0;

                                // Add up the family's I's, B's, and M's
                                for (index = 0; index < familyNames.length; index++) {

                                    // Add up the family member's lowers first

                                    // For loop cycles through family members
                                    for (index1 = 0; index1 < teethData[index][0].length; index1++) {

                                        // Switch statement deciphers which tooth
                                        switch (teethData[index][0][index1].toUpperCase()) {

                                            case ("I"):
                                                sumOfFamilyI++;
                                                break;

                                            case ("B"):
                                                sumOfFamilyB++;
                                                break;

                                            case ("M"):
                                                sumOfFamilyM++;
                                                break;

                                        } // end of first switch statement

                                    } // end of first nested for loop

                                    // Next, add up the family member's uppers

                                    // For loop cycles through family members
                                    for (index1 = 0; index1 < teethData[index][1].length; index1++) {

                                        // Switch statement deciphers which tooth
                                        switch (teethData[index][1][index1].toUpperCase()) {

                                            case ("I"):
                                                sumOfFamilyI++;
                                                break;

                                            case ("B"):
                                                sumOfFamilyB++;
                                                break;

                                            case ("M"):
                                                sumOfFamilyM++;
                                                break;

                                        } // end of second switch statement

                                    } // end of second nested for loop

                                } // end of outer for loop

                                /* The family's root canal indices will be solved using the quadratic formula
                                where the sum of I's = A, sum of B's = B, and the negative sum of M's = C
                                x = (-b + sqrt(b^2 - 4ac)) / 2a
                                x = (-b - sqrt(b^2 - 4ac)) / 2a
                                There is no need to verify that roots exist, because c is always equal to or less than 0
                                Thus, sqrt(b^2-4ac) is always 0 or positive, meaning root(s) exist
                                 */
                                a = sumOfFamilyI;
                                b = sumOfFamilyB;
                                c = -sumOfFamilyM;

                                rootCanalOne = (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
                                rootCanalTwo = (-b - Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);

                                // It was never specified in the directions what to round the roots to, so the roots are rounded to 2 decimal places

                                System.out.println("One root canal at " + Math.round(rootCanalOne * 100.0) / 100.0);
                                System.out.println("Another root canal at " + Math.round(rootCanalTwo * 100.0) / 100.0);

                                repeatSwitch = true; // To repeat the switch

                                // Prompt the user with menu options again
                                System.out.println();
                                System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it:");
                                menuLetter = keyboard.nextLine();
                                menuLetter = menuLetter.toUpperCase();

                                break;

                            case ("X"): // The program ends

                                System.out.println("Exiting the Floridian Tooth Records :-)");

                                repeatSwitch = false; // ends the switch
                                break;

                            default: // If the input is not a menu option

                                // Prompt the user with menu options again
                                System.out.println("Invalid menu option, try again");
                                menuLetter = keyboard.nextLine();
                                menuLetter = menuLetter.toUpperCase();

                                repeatSwitch = true; // To repeat the switch

                        } // end of switch statement

            } while (repeatSwitch); // makes the switch re-run unless the user wants to exit

        } // end of menuOptions method

} // end of DentalRecords class
