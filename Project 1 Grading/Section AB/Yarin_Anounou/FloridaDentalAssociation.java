import java.sql.SQLOutput;
import java.util.Scanner;

public class FloridaDentalAssociation {

    private static int MAX_NUM_FAMILY = 6;
    private static int MAX_NUM_UPPER = 8;
    private static int MAX_NUM_LOWER = 8;
    private static int UPPER_TOOTH_LAYER = 0;
    private static int LOWER_TOOTH_LAYER = 1;

    private static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {


        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        int numberOfPeople; // started wrong so the while loop will continue

        System.out.print("Please enter number of people in the family: ");
        numberOfPeople = keyboard.nextInt();
        while (numberOfPeople < 0 || numberOfPeople > MAX_NUM_FAMILY) {
            System.out.print("Invalid number of people, try again : ");
            numberOfPeople = keyboard.nextInt();

        } // end of while loop

        String[] memberNamesArray = new String[numberOfPeople];

        char[][][] toothArray = new char[numberOfPeople][2][MAX_NUM_UPPER];

        fillArray(memberNamesArray, toothArray);

        char menuOption = 'A';
        while (menuOption != 'X') {
            System.out.println();
            System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it : ");
            menuOption = Character.toUpperCase(keyboard.next().charAt(0));

            if (menuOption == 'P') {
                System.out.println();
                printTeethTypes(memberNamesArray, toothArray);
            } else if (menuOption == 'E') {
                System.out.println();
                extractTooth(memberNamesArray, toothArray);
            } else if (menuOption == 'R') {
                System.out.println();
                rootMethod(memberNamesArray, toothArray);
            } else if (menuOption == 'X') {
                System.out.println();
                exitOption("Exiting the Floridian tooth Records :-)");
            } else {
                while (menuOption != 'P' && menuOption != 'E' && menuOption != 'R' && menuOption != 'X') {
                    System.out.print("Invalid menu option, try again : ");
                    menuOption = Character.toUpperCase(keyboard.next().charAt(0));
                }
                if (menuOption == 'X') {
                    System.out.println();
                    exitOption("Exiting the Floridian tooth Records :-)");
                    break;
                } else if (menuOption == 'P') {
                    System.out.println();
                    printTeethTypes(memberNamesArray, toothArray);
                } else if (menuOption == 'E') {
                    System.out.println();
                    extractTooth(memberNamesArray, toothArray);
                } else if (menuOption == 'R') {
                    System.out.println();
                    rootMethod(memberNamesArray, toothArray);
                }
            } // end of if statements

        } // end of while loop

    } // end of main method

    /**
     * This is the method that gets the information from the user to fill in the 3D array with inputs.
     * It gets the number of members in the family, the names of each family member, and the teeth types they have.
     * It ensures valid teeth types are inserted and a valid number of teeth (meaning not above the maximum amount in each
     * layer which is 8).
     * @param memberNamesArray
     * @param toothArray
     */

    private static void fillArray(String[] memberNamesArray, char[][][] toothArray){
        for (int i = 0; i < memberNamesArray.length; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + " : ");
            memberNamesArray[i] = keyboard.next();

            // Upper teeth

            boolean validUpperTeeth = false;
            String upperTeethTypes = "";

            while (!validUpperTeeth) {
                System.out.print("Please enter the uppers for " + memberNamesArray[i] + " : ");
                upperTeethTypes = keyboard.next().toUpperCase();

                // check for invalid characters
                boolean invalidType = false;
                for (int j = 0; j < upperTeethTypes.length(); j++) {
                    char t = upperTeethTypes.charAt(j);
                    if (t != 'I' && t != 'B' && t != 'M') {
                        System.out.print("Invalid teeth types, try again : ");
                        upperTeethTypes = keyboard.next().toUpperCase();
                        invalidType = true;
                    }
                    // check for too many teeth
                    else if (upperTeethTypes.length() > MAX_NUM_UPPER) {
                        System.out.print("Too many teeth, try again : ");
                        upperTeethTypes = keyboard.next().toUpperCase();
                    }
                }

                validUpperTeeth = true;
            } // end of while loop

                for (int j = 0; j < upperTeethTypes.length(); j++) {
                    toothArray[i][UPPER_TOOTH_LAYER][j] = upperTeethTypes.charAt(j);
                }

            // Checking Lowers
            boolean validLowerTeeth = false;
            String lowerTeethTypes = "";

            while (!validLowerTeeth) {
                System.out.print("Please enter the lowers for " + memberNamesArray[i] + " : ");
                lowerTeethTypes = keyboard.next();

                validLowerTeeth = true;

                if (lowerTeethTypes.length() > MAX_NUM_LOWER) {
                    System.out.print("Too many teeth, try again : ");
                    validLowerTeeth = false;
                    break;
                } else {
                    for (int index = 0; index < lowerTeethTypes.length(); index++) {
                        char teethType = Character.toUpperCase(lowerTeethTypes.charAt(index));
                        if (teethType != 'I' && teethType != 'B' && teethType != 'M') {
                            System.out.print("Invalid teeth types, try again : ");
                            validLowerTeeth = false;
                            break;
                        }

                    } // end of for loop to check for invalid char

                } // end of else statement

                if (validLowerTeeth) break;

            } // end of while loop

                for (int j = 0; j < lowerTeethTypes.length(); j++) {
                    toothArray[i][LOWER_TOOTH_LAYER][j] = lowerTeethTypes.charAt(j);
                }

            } // end of for loop

    } // end of fillArray method

    /**
     * This is the method that prints all the data that the user gave about the 3D array. It lists each person's name in
     * the family with each tooth type, along with the number of each tooth.
     * @param memberNamesArray
     * @param toothArray
     */

    private static void printTeethTypes(String [] memberNamesArray, char[][][] toothArray){
        for (int i = 0; i < memberNamesArray.length; i++) {
            System.out.println(memberNamesArray[i]);
            System.out.print("  Uppers: ");
            for (int j = 0; j < toothArray[i][0].length; j++){
                if(toothArray[i][UPPER_TOOTH_LAYER][j] == '\0'){
                    break;
                }
                System.out.print((j + 1) + ":" + toothArray[i][UPPER_TOOTH_LAYER][j] + " ");
            }// end of inner for loop for uppers
            System.out.println();
            System.out.print("  Lowers: ");
            for (int k = 0; k < toothArray[i][LOWER_TOOTH_LAYER].length; k++){
                if(toothArray[i][LOWER_TOOTH_LAYER][k] == '\0') {
                    break;
                }
                System.out.print((k + 1) + ":" + toothArray[i][LOWER_TOOTH_LAYER][k] + " ");
            } // end of inner for loop for lowers
            System.out.println();
        } // end of outer for loop

    } // end printTeethTypes

    /**
     * This method is used to extract one of the teeth in the family. It changes one of the teeth to be represented by
     * the character 'M' to stand for missing. (it removes a tooth from a family member) It checks the family member
     * exists, the tooth layer exists, and the tooth number exists.
     * @param memberNamesArray
     * @param toothArray
     */

    private static void extractTooth(String [] memberNamesArray, char [][][] toothArray){

        int memberIndex = -1;
        String comment  = "Which family member : ";
        while (memberIndex == -1) {
            System.out.print(comment);
            String familyMember = keyboard.next();

            for (int i = 0; i < memberNamesArray.length; i++) {
                if (memberNamesArray[i].equalsIgnoreCase(familyMember)) {
                    memberIndex = i;
                    break;
                } // end of if statement
            } // end of for loop

            if (memberIndex == -1) {
                comment = "Invalid family member, try again : ";
            } // end of if statement

        } // end of while loop

        System.out.print("Which tooth layer (U)pper or (L)ower : ");
        char toothLayer = Character.toUpperCase(keyboard.next().charAt(0));

        while (toothLayer != 'U' && toothLayer != 'L') {
            System.out.print("Invalid layer, try again : ");
            toothLayer = Character.toUpperCase(keyboard.next().charAt(0));
        } // end of while loop

        int typeOfLayer;
        if (toothLayer == 'U') {
            typeOfLayer = UPPER_TOOTH_LAYER;
        } else {
            typeOfLayer = LOWER_TOOTH_LAYER;
        } // end of if statements

        int numberOfTooth = -1;
        System.out.print("Which tooth number : ");

        while (true) {

            numberOfTooth = keyboard.nextInt();

            if (numberOfTooth < 1 || numberOfTooth > MAX_NUM_UPPER) {
                System.out.print("Invalid tooth number, try again : ");
                continue;
            } // end of if statement

            char possibleMissingTooth = toothArray[memberIndex][typeOfLayer][numberOfTooth - 1];

            if (possibleMissingTooth == 'M') {
                System.out.print("Missing tooth, try again : ");
                continue;
            }

            break;
        } // end fo while loop

        toothArray[memberIndex][typeOfLayer][numberOfTooth - 1] = 'M';

    } // end of extractTooth method

    /**
     * This method computes the calculations for the root canals in the family. It first calculates the amount of I
     * characters, B characters, and M characters there are in the 3D array and then computes the root canals using
     * the quadratic formula to find the zeros.
     * @param memberNamesArray
     * @param toothArray
     */

    private static void rootMethod(String [] memberNamesArray, char [][][] toothArray){
        double amountOfI = 0.0;
        double amountOfB = 0.0;
        double amountOfM = 0.0;

        for (int i = 0; i < memberNamesArray.length; i++){
            for (int j = 0; j < 2; j++) { // 0 = upper, 1 = lower
                for (int k = 0; k < toothArray[i][j].length; k++) {
                    char tooth = toothArray[i][j][k];
                    if (tooth == 'I' || tooth == 'i') amountOfI++;
                    else if (tooth == 'B' || tooth == 'b') amountOfB++;
                    else if (tooth == 'M' || tooth == 'm') amountOfM++;
                } // end of inner for loop
            } // end of middle for loop
        } // end of outer for loop

        double discriminant = Math.sqrt(((double)amountOfB * (double)amountOfB) - (4.0 * (double)amountOfI * -(double)amountOfM));
        double root1 = (-(double)amountOfB + discriminant) / (2.0 * (double)amountOfI);
        double root2 = (-(double)amountOfB - discriminant) / (2.0 * (double)amountOfI);

        // Print results
        System.out.printf("One root canal at     %.2f%n", root1);
        System.out.printf("Another root canal at %.2f%n", root2);
    } // end of rootMethod method

    /**
     * This method simply prints out the exit statement for this code.
     * @param exitStatement
     * @return
     */

    private static String exitOption(String exitStatement){

        System.out.println(exitStatement);

        return exitStatement;

    } // end of exitOption

} // end of FloridaDentalAssociation class
