import java.util.Scanner;
/**
 * This is a class to store tooth data in a 3D array and manipulate the teeth data/print the teeth data
 * @author Kush Kumar
 */
public class DentalRecords {
    /**
     * The number of possible tooth layers
     */
    private static final int NUM_TOOTH_LAYERS = 2;

    /**
     * Global scanner object to user keyboard
     */

    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * The main method
     * @param args Passed in from the command line
     */

    public static void main(String[] args) {
        int numPeopleFamily;
        String userInput;
        char command;

        numPeopleFamily = getNumPeopleFamily();

        String [] names = new String[numPeopleFamily];
        char [][][] teethData = new char[numPeopleFamily][NUM_TOOTH_LAYERS][];

        inputFamily(names, teethData);

        System.out.println(" ");

        do {
            System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it: ");
            userInput = keyboard.nextLine();

            if (!userInput.isEmpty()) {
                command = Character.toLowerCase(userInput.charAt(0));
            } else {
                command = ' ';
            }

            while (command != 'p' && command != 'e' && command != 'r' && command != 'x') {
                System.out.print("Invalid menu option, try again: ");
                userInput = keyboard.nextLine();

                if (!userInput.isEmpty()) {
                    command = Character.toLowerCase(userInput.charAt(0));
                } else {
                    command = ' ';
                }
            }

            if (command == 'p') {
                printFamily(names, teethData);
                System.out.println(" ");
            } else if (command == 'e') {
                extractTooth(names, teethData);
                System.out.println(" ");
            } else if (command == 'r') {
                calculateRoots(names, teethData);
                System.out.println(" ");
            } else if (command == 'x') {
                System.out.println(" ");
                System.out.println("Exiting the Floridian Tooth Records :-)");
            }

        } while (command != 'x');


    } // end of the main method

    /**
     * This is a method used to get the number of people in the family
     * @return The number of people in the family
     */

    public static int getNumPeopleFamily() {
        int numPeopleFamily;
        String errorMessage;

        System.out.println("Welcome to the Floridan Tooth Records");

        System.out.println("-------------------------------------");

        System.out.print("Please enter number of people in the family: ");

        do {
            numPeopleFamily = keyboard.nextInt();
            errorMessage = checkNumPeopleFamily(numPeopleFamily);

            if (errorMessage.equals("Invalid")) {
                System.out.print("Invalid number of people, try again: ");
            } // end of the if statement
        } // end of the do-while loop
        while (errorMessage.equals("Invalid"));

        return(numPeopleFamily);
    } // end of getNumPeopleFamily method

    /**
     * This is a helper method to ensure the number of people in the family is correct
     * @param numPeopleFamily
     * @return Returns a String ("Invalid" or "Success") which is used in the getNumPeopleFamily method
     */

    public static String checkNumPeopleFamily (int numPeopleFamily) {
        if (numPeopleFamily > 6 || numPeopleFamily < 2) {
            return ("Invalid");
        } // end of the if statement
        return("Success");
    } // end of checkNumPeopleFamily method

    /**
     * This is a method to allow the user to input their family teeth data into a 3D array
     * @param names
     * @param teethData
     */

    private static void inputFamily(String[] names, char[][][] teethData) {
        String upperTeeth;
        String lowerTeeth;
        int plane;
        int row;
        int column;

        keyboard.nextLine();

        for (plane = 0; plane < names.length; plane++) {
            System.out.print("Please enter the name for family member " + (plane + 1) + ": ");
            names[plane] = keyboard.nextLine();

            upperTeeth = "";

            System.out.print("Please enter the uppers for " + names[plane] + ": ");

            do {
                upperTeeth = keyboard.nextLine();

                if (upperTeeth.length() > 8) {
                    System.out.print("Too many teeth, try again: ");
                }

                else if (upperTeeth.length() < 1) {
                    System.out.print("Too few teeth, try again: ");
                }

                else if (!upperTeeth.matches("[IBMibm]+")) {
                    System.out.print("Invalid teeth types, try again: ");
                }

            } while (upperTeeth.length() < 1 || upperTeeth.length() > 8 || !upperTeeth.matches("[IBMibm]+"));

            upperTeeth = upperTeeth.toUpperCase();

            teethData[plane][0] = new char[upperTeeth.length()];

            for (column = 0; column < upperTeeth.length(); column++){
                teethData[plane][0][column] = upperTeeth.charAt(column);
            } // end of the 1st inner for loop for inputting upper Teeth into the 3D array

            lowerTeeth = "";

            System.out.print("Please enter the lowers for " + names[plane] + ": ");

            do {
                lowerTeeth = keyboard.nextLine();

                if (lowerTeeth.length() > 8) {
                    System.out.print("Too many teeth, try again: ");
                }

                else if (lowerTeeth.length() < 1) {
                    System.out.print("Too many teeth, try again: ");
                }

                else if (!lowerTeeth.matches("[IBMibm]+")) {
                    System.out.print("Invalid teeth types, try again: ");
                }

            } while (lowerTeeth.length() < 1 || lowerTeeth.length() > 8 || !lowerTeeth.matches("[IBMibm]+"));

            lowerTeeth = lowerTeeth.toUpperCase();

            teethData[plane][1] = new char[lowerTeeth.length()];

            for (column = 0; column < lowerTeeth.length(); column++) {
                teethData[plane][1][column] = lowerTeeth.charAt(column);
            } // end of 2nd inner for loop for inputting lower Teeth into the 3D array

        } // end of the outer for loop

    } // end of the inputFamily method

    /**
     * This is a method used to print the family tooth data
     * @param names
     * @param teethData
     */

    public static void printFamily (String[] names, char [][][] teethData) {
        int plane;
        int row;
        int column;

        for (plane = 0; plane < names.length; plane++) {
            System.out.println(names[plane]);

            System.out.print("  Uppers: ");

            for (column = 0; column < teethData[plane][0].length; column++) {
                System.out.print((column + 1) + ":" + teethData[plane][0][column] + " ");
            } // end of the first inner for loop to print the uppers

            System.out.println(" ");

            System.out.print("  Lowers: ");

            for (column = 0; column < teethData[plane][1].length; column++) {
                System.out.print((column + 1) + ":" + teethData[plane][1][column] + " ");
            } // end of the second inner for loop to print the lowers

            System.out.println(" ");

        } // end of the outer for loop

    } // end of the printFamily method

    /**
     * This is a method used to extract a tooth, changing it to missing in the 3D array
     * @param names
     * @param teethData
     */

    public static void extractTooth (String[] names, char[][][] teethData) {
        String error;
        String name;
        int personIndex;
        String layerInput;
        int layer;
        int plane;
        int toothNumber;

        // Ask for which family member

        System.out.print("Which family member: ");

        personIndex = -1;

        error = "Invalid";

        do {
            name = keyboard.nextLine();

            for (plane = 0; plane < names.length; plane++) {
                if (names[plane].equalsIgnoreCase(name)) {
                    personIndex = plane;
                    error = "Valid";

                } // end of inner if statement

            } // end of first for loop to check if name provided is valid

            if (error.equals("Invalid")) {
                System.out.print("Invalid family member, try again: ");
            } // end of outer if statement

        } while (error.equals("Invalid"));

        //Ask for which layer of teeth

        System.out.print("Which tooth layer (U)pper or (L)ower: ");

        error = "";
        layerInput = "";
        layer = -1;

        do {
            layerInput = keyboard.nextLine();

            if (layerInput.equals("U") || layerInput.equals("u")) {
                layer = 0;
                error = "Valid";
            } // end of first if statement
            else if (layerInput.equals("L") || layerInput.equals("l")) {
                layer = 1;
                error = "Valid";
            } // end of second if-else statement
            else {
                System.out.print("Invalid layer, try again: ");
                error = "Invalid";
            } // end of third & final if-else statement

        } while (error.equals("Invalid"));

        //Ask for which tooth and extract if tooth is not already missing

        error = "";

        System.out.print("Which tooth number: ");

        do {
            toothNumber = keyboard.nextInt();

            keyboard.nextLine();

            toothNumber = toothNumber -1;

            if (toothNumber >=0 && toothNumber < teethData[personIndex][layer].length) {
                if (teethData[personIndex][layer][toothNumber] == 'M') {
                    System.out.print("Missing tooth, try again: ");
                    error = "Invalid";
                } // end of first inner if statement

                else {
                    teethData[personIndex][layer][toothNumber] = 'M';
                    error = "Valid";
                } // end of second inner if statement

            } // end of first outer if statement
            else {
                System.out.print("Invalid tooth number, try again: ");
                error = "Invalid";

            } // end of second out if-else statement

        } while (error.equals("Invalid"));

    } // end of the extractTooth method

    /**
     * This is a method used to calculate the family roots based on the 3D array tooth data
     * @param names
     * @param teethData
     */

    public static void calculateRoots (String [] names, char [][][] teethData) {
        int incisors;
        int bicuspids;
        int missingTeeth;
        int plane;
        int row;
        int column;
        double discriminant;
        double squareRootDiscriminant;
        double root1;
        double root2;

        incisors = 0;
        bicuspids = 0;
        missingTeeth = 0;

        for (plane = 0; plane < names.length; plane++) {
            for (row = 0; row < teethData[plane].length; row ++) {
                for (column = 0; column < teethData[plane][row].length; column++) {

                    if (teethData[plane][row][column] == 'I' || teethData[plane][row][column] == 'i') {
                        incisors = incisors + 1;
                    } // end of first if statement

                    else if (teethData[plane][row][column] == 'B' || teethData[plane][row][column] == 'b') {
                        bicuspids = bicuspids + 1;
                    } // end of second if statement

                    else if (teethData[plane][row][column] == 'M' || teethData[plane][row][column] == 'm') {
                        missingTeeth = missingTeeth + 1;
                    } // end of third and final if statement

                } // end of inner for loop

            } // end of middle for loop

        } // end of the outer for loop

        discriminant = Math.pow(bicuspids, 2) - (4.00 * incisors * (-1 * missingTeeth));

        if (discriminant >= 0) {

            squareRootDiscriminant = Math.sqrt(discriminant);

            root1 = ((-1.00 * bicuspids) + squareRootDiscriminant) / (2.00 * incisors);

            root2 = ((-1.00 * bicuspids) - squareRootDiscriminant) / (2.00 * incisors);

            System.out.printf("One root canal at %.2f\n", root1);

            System.out.printf("Another root canal at %.2f\n", root2);

        }
        else {
            System.out.println("No real roots as discriminant is negative");
        }

    } // end of the calculateRoots method

} // end of the class DentalRecords