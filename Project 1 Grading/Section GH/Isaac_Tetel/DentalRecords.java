import java.util.Scanner;

/**
 * Manages dental records for a family, allowing tracking of tooth types and dental procedures.
 * @author Isaac Tetel
 * @version 1
 */
public class DentalRecords {

    /**
     * Maximum number of family members allowed in the system.
     */
    public static final int MAXIMUM_FAMILY = 6;

    /**
     * Maximum number of teeth per layer (upper or lower).
     */
    public static final int MAXIMUM_TEETH = 8;

    /**
     * Number of tooth layers (upper and lower).
     */
    public static final int NUMBER_LAYERS = 2;

    /**
     * Main method that runs the dental records program.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);


        int totalMembers;
        String[] memberNames ;
        char[][][] teeth;
        boolean done;
        String userInput;


        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        totalMembers = getFamilySize(input);

        memberNames = new String[totalMembers];
        teeth = new char[totalMembers][NUMBER_LAYERS][MAXIMUM_TEETH];

        int memberIndex;
        for (memberIndex = 0; memberIndex < totalMembers; memberIndex++) {
            memberNames[memberIndex] =
                    getFamilyName(input, memberIndex + 1);
            teeth[memberIndex][0] =
                    getTeeth(input, memberNames[memberIndex], "uppers");
            teeth[memberIndex][1] =
                    getTeeth(input, memberNames[memberIndex], "lowers");
        }

        done = false;

        while (!done) {
            System.out.println();
            userInput = menuInput(input);

            String upperMenu = userInput.toUpperCase();

            if (upperMenu.equals("P")) {
                System.out.println();
                printFamily(memberNames, teeth, totalMembers);
            }
            if (upperMenu.equals("E")) {
                extractTooth(input, memberNames, teeth, totalMembers);
            }
            if (upperMenu.equals("R")) {
                rootCanal(teeth, totalMembers);
            }
            if (upperMenu.equals("X")) {
                System.out.println();
                System.out.println(
                        "Exiting the Floridian Tooth Records :-)");
                done = true;
            }
        }
    }//End of main method

    /**
     * Gets the number of family members from the user input.
     * @param input Scanner object for reading user input
     * @return The number of family members (between 1 - MAXIMUM_FAMILY)
     */
    public static int getFamilySize(Scanner input) {
        int num;
        System.out.print(
                "Please enter number of people in the family : ");
        num = input.nextInt();

        while (num < 1 || num > MAXIMUM_FAMILY) {
            System.out.print(
                    "Invalid number of people, try again         : ");
            num = input.nextInt();
        }

        input.nextLine();
        return num;
    }//End of getFamilySize method

    /**
     * Gets the name of a family member from the user input.
     * @param input Scanner object for reading user input
     * @param personNumber The position number of the family member
     * @return The name of the family member
     */
    public static String getFamilyName(Scanner input, int personNumber) {
        String name;
        System.out.print(
                "Please enter the name for family member " +
                        personNumber + "   : ");
        name = input.nextLine();
        return name;
    }//End of getFamilyName method

    /**
     * Gets tooth data for upper or lower layer of a person.
     * Validates that the input contains only valid tooth characters and does not exceed MAXIMUM_TEETH.
     * @param input Scanner object for reading user input
     * @param name Name of the family member
     * @param layer Either "uppers" or "lowers" to indicate which tooth layer
     * @return Array of characters representing the teeth (I, B, or M)
     */
    public static char[] getTeeth(Scanner input, String name,
                                  String layer) {
        char[] row = new char[MAXIMUM_TEETH];
        String line;
        boolean valid;
        int i;

        valid = false;

        System.out.print(
                "Please enter the " + layer + " for " + name);

        int spacesNeeded = 39 -
                ("Please enter the " + layer + " for " + name).length();
        for (i = 0; i < spacesNeeded; i++) {
            System.out.print(" ");
        }
        System.out.print(": ");

        line = input.nextLine();
        line = line.toUpperCase();

        while (!valid) {
            if (line.length() > MAXIMUM_TEETH) {
                System.out.print(
                        "Too many teeth, try again" +
                                "                   : ");
                line = input.nextLine();
                line = line.toUpperCase();
            }
            else {
                if (!isValidTeeth(line)) {
                    System.out.print(
                            "Invalid teeth types, try again" +
                                    "              : ");
                    line = input.nextLine();
                    line = line.toUpperCase();
                }
                else {
                    for (i = 0; i < line.length(); i++) {
                        row[i] = line.charAt(i);
                    }
                    for (i = line.length(); i < MAXIMUM_TEETH; i++) {
                        row[i] = ' ';
                    }
                    valid = true;
                }
            }
        }
        return row;
    }//End of getTeeth method

    /**
     * Validates that a string contains only valid tooth characters.
     * The only valid characters are "I" for Incisor, "B" for Bicuspid and "M" for Missing.
     * @param line The string to validate
     * @return true if all characters are valid tooth types, false otherwise
     */
    public static boolean isValidTeeth(String line) {
        int i;
        char ch;
        for (i = 0; i < line.length(); i++) {
            ch = line.charAt(i);
            if (ch != 'I' && ch != 'B' && ch != 'M') {
                return false;
            }
        }
        return true;
    }//End of isValidTeeth

    /**
     * Shows the menu of input options and gets a valid input from the user.
     * Valid choices are P, E, R, or X and can be upper or lower case.
     * @param input Scanner object for reading user input
     * @return The user's menu choice
     */
    public static String menuInput(Scanner input) {
        String choice;
        System.out.print(
                "(P)rint, (E)xtract, (R)oot, e(X)it          : ");
        choice = input.nextLine();

        String upperChoice = choice.toUpperCase();

        while (!upperChoice.equals("P") &&
                !upperChoice.equals("E") &&
                !upperChoice.equals("R") &&
                !upperChoice.equals("X")) {
            System.out.print(
                    "Invalid menu option, try again              : ");
            choice = input.nextLine();
            upperChoice = choice.toUpperCase();
        }
        return choice;
    }//End of menuInput method

    /**
     * Prints dental records for the family members.
     * Displays each member's name and their upper and lower teeth.
     * @param names Array of family member names
     * @param teeth 3D array containing tooth data for each family member
     * @param numPeople Number of family members
     */
    public static void printFamily(String[] names, char[][][] teeth,
                                   int numPeople) {
        int i;
        for (i = 0; i < numPeople; i++) {
            System.out.println(names[i]);
            System.out.print("  Uppers: ");
            printRow(teeth[i][0]);
            System.out.print("  Lowers: ");
            printRow(teeth[i][1]);
        }
    }//End of printFamily method

    /**
     * Prints a row of teeth.
     * @param row Array of characters representing teeth to print
     */
    public static void printRow(char[] row) {
        int i;
        for (i = 0; i < MAXIMUM_TEETH && row[i] != ' '; i++) {
            System.out.print(" " + (i + 1) + ":" + row[i] + " ");
        }
        System.out.println();
    }//End of printRow method

    /**
     * Extracts tooth from a family member's dental record.
     * Prompts user for family member's name, tooth layer, and tooth number.
     * @param input Scanner object for reading user input
     * @param names Array of family member names
     * @param teeth 3D array containing tooth data for each family member
     * @param numPeople Number of family members
     */
    public static void extractTooth(Scanner input, String[] names,
                                    char[][][] teeth, int numPeople) {
        String name,layer;
        int personIndex,layerIndex,numTooth;

        System.out.print(
                "Which family member                         : ");
        name = input.nextLine();
        personIndex = findPerson(names, numPeople, name);

        while (personIndex == -1) {
            System.out.print(
                    "Invalid family member, try again            : ");
            name = input.nextLine();
            personIndex = findPerson(names, numPeople, name);
        }

        System.out.print(
                "Which tooth layer (U)pper or (L)ower        : ");
        layer = input.nextLine();
        String upperLayer = layer.toUpperCase();

        while (!upperLayer.equals("U") && !upperLayer.equals("L")) {
            System.out.print(
                    "Invalid layer, try again                    : ");
            layer = input.nextLine();
            upperLayer = layer.toUpperCase();
        }

        if (upperLayer.equals("U")) {
            layerIndex = 0;
        }
        else {
            layerIndex = 1;
        }

        System.out.print(
                "Which tooth number                          : ");
        numTooth = input.nextInt();
        input.nextLine();

        while (numTooth < 1 || numTooth > MAXIMUM_TEETH ||
                teeth[personIndex][layerIndex][numTooth - 1] == ' ' ||
                teeth[personIndex][layerIndex][numTooth - 1] == 'M') {
            if (numTooth < 1 || numTooth > MAXIMUM_TEETH) {
                System.out.print(
                        "Invalid tooth number, try again             : ");
            }
            else {
                System.out.print(
                        "Missing tooth, try again                    : ");
            }
            numTooth = input.nextInt();
            input.nextLine();
        }

        teeth[personIndex][layerIndex][numTooth - 1] = 'M';
    }//End of extractTooth method

    /**
     * Finds the index of a person in the names array.
     * @param names Array of family member names
     * @param numPeople Number of family members
     * @param searchName Name to search for
     * @return Index of the person if found, -1 otherwise
     */
    public static int findPerson(String[] names, int numPeople,
                                 String searchName) {
        int i;
        for (i = 0; i < numPeople; i++) {
            String nameUpper = names[i].toUpperCase();
            String searchNameUpper = searchName.toUpperCase();
            if (nameUpper.equals(searchNameUpper)) {
                return i;
            }
        }
        return -1;
    }//End of findPerson method

    /**
     * Calculates and shows root canal using the quadratic formula.
     * Uses the equation Ix² + Bx - M where I is incisors, B is bicuspids, and M is missing teeth.
     * @param teeth 3D array containing tooth data for each family member
     * @param numPeople Number of family members
     */
    public static void rootCanal(char[][][] teeth, int numPeople) {
        int i,j,x;
        int incisors,bicuspids,missing;
        double squareRootValue,positiveRoot,negativeRoot;
        char toothType;

        incisors = 0;
        bicuspids = 0;
        missing = 0;

        for (i = 0; i < numPeople; i++) {
            for (j = 0; j < NUMBER_LAYERS; j++) {
                for (x = 0; x < MAXIMUM_TEETH; x++) {
                    toothType = teeth[i][j][x];
                    if (toothType == 'I') {
                        incisors++;
                    }
                    if (toothType == 'B') {
                        bicuspids++;
                    }
                    if (toothType == 'M') {
                        missing++;
                    }
                }
            }
        }

        squareRootValue =
                (bicuspids * bicuspids) + (4.0 * incisors * missing);

        if (incisors == 0) {
            System.out.println("No incisors.");
        }
        else {
            positiveRoot = (-bicuspids + Math.sqrt(squareRootValue)) /
                    (2 * incisors);
            negativeRoot = (-bicuspids - Math.sqrt(squareRootValue)) /
                    (2 * incisors);

            double finalValue1 = ((int)(positiveRoot * 100)) / 100.0;
            double finalValue2 = ((int)(negativeRoot * 100)) / 100.0;

            System.out.printf("One root canal at %9.2f%n", finalValue1);
            System.out.printf("Another root canal at %8.2f%n", finalValue2);
        }
    }//End of rootCanal method
}//End of DentalRecords method