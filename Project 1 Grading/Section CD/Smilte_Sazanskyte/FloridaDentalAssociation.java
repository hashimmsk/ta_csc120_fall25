import java.util.Scanner;

/**
 * The Florida Dental Association program records teeth information
 * for a family of up to six members.
 * Teeth data is stored in a three-dimensional array:
 * - First dimension: family member index.
 * - Second dimension: 0 for uppers, 1 for lowers.
 * - Third dimension: tooth positions (max 8 per row).
 *
 * @author Smilte Sazanskyte
 */
public class FloridaDentalAssociation {

    // declare constants
    private static final int MAX_FAMILY_SIZE = 6; // Maximum number of family members allowed
    private static final int MAX_TEETH = 8; // Maximum number of teeth per upper or lower row
    private static final char INCISOR = 'I';
    private static final char BICUSPID = 'B';
    private static final char MISSING = 'M';
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * The main method. Initializes the program, collects family information,
     * and displays the menu for printing, extracting teeth, calculating root canals,
     * or exiting the program.
     */
    public static void main(String[] args) {

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        int numPeople = getFamilySize();

        String[] familyNames = new String[numPeople];
        char[][][] teethRecords = new char[numPeople][2][MAX_TEETH]; // 3D array

        getFamilyInformation(numPeople, familyNames, teethRecords);

        boolean exit = false;
        while (!exit) {
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it: ");
            String choice = keyboard.nextLine().toUpperCase(); //covert any input letter to uppercase

            switch (choice) {
                case "P":
                    printFamilyRecords(numPeople, familyNames, teethRecords);
                    break;
                case "E":
                    extractTooth(numPeople, familyNames, teethRecords);
                    break;
                case "R":
                    calculateRootCanals(numPeople, teethRecords);
                    break;
                case "X":
                    System.out.println("Exiting the Floridian Tooth Records :-)");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid menu option, try again.");
                    break;
            } // end of switch statement
        } // end of while loop
    } // end of main method

// -----------------------------------------------------------------------------------------------------------------
    /**
     * Prompts the user to enter the number of people in the family.
     * Ensures that the input is within the allowed range (1 to MAX_FAMILY_SIZE).
     * @return The validated number of family members
     */
    private static int getFamilySize() {
        int numPeople = 0;
        System.out.print("Please enter number of people in the family: ");

        while (numPeople < 1 || numPeople > MAX_FAMILY_SIZE) {
            numPeople = keyboard.nextInt();
            keyboard.nextLine();
            if (numPeople < 1 || numPeople > MAX_FAMILY_SIZE) {
                System.out.print("Invalid number of people, try again. ");
            } // End of if statement
        } // End of while loop

        return numPeople;

    } // End of getFamilySize method

// -----------------------------------------------------------------------------------------------------------------
    /**
     * Collects family member names and their upper and lower teeth.
     * Uses the getValidatedTeeth method to ensure teeth input is valid.
     *
     * @param numPeople Number of people in the family
     * @param familyNames Array to store family member names
     * @param teethRecords Three-dimensional array to store teeth data
     */
    private static void getFamilyInformation(int numPeople, String[] familyNames, char[][][] teethRecords) {

        for (int i = 0; i < numPeople; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + " : ");
            familyNames[i] = keyboard.nextLine();

            teethRecords[i][0] = getValidatedTeeth("uppers", familyNames[i]);
            teethRecords[i][1] = getValidatedTeeth("lowers", familyNames[i]);
        } // End of for loop

    } // End of getFamilyInformation method

// -----------------------------------------------------------------------------------------------------------------
    /**
     * Prompts the user to enter teeth for a specific layer (upper or lower) of a family member.
     * Validates that all teeth are either I, B, or M and that the number of teeth does not exceed MAX_TEETH.
     *
     * @param layer The tooth layer ("uppers" or "lowers")
     * @param name The family member's name
     * @return A char array representing the teeth for the given layer
     */
    private static char[] getValidatedTeeth(String layer, String name) {
        char[] teeth = new char[MAX_TEETH];
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Please enter the " + layer + " for " + name + ": ");
            String input = keyboard.nextLine().toUpperCase();

            // Check if too many teeth
            if (input.length() > MAX_TEETH) {
                System.out.println("Too many teeth, try again.");
                continue;
            } // end of if statement

            validInput = true;
            for (int i = 0; i < input.length(); i++) {
                char tooth = input.charAt(i);
                if (tooth != INCISOR && tooth != BICUSPID && tooth != MISSING) {
                    System.out.println("Invalid teeth types, try again.");
                    validInput = false;
                    break;
                } // end of if statement

                teeth[i] = tooth;
            } // end of for loop
        } // end of while loop

        return teeth;

    } // End of getValidatedTeeth method

// -----------------------------------------------------------------------------------------------------------------
    /**
     * Prints the teeth records for all family members.
     * Only prints teeth that were entered by the user. Missing teeth ('M') are included.
     * Uninitialized positions in the array are skipped.
     *
     * @param numPeople Number of family members
     * @param familyNames Array of family member names
     * @param teethRecords Three-dimensional array of teeth data
     */
    private static void printFamilyRecords(int numPeople, String[] familyNames, char[][][] teethRecords) {
        for (int i = 0; i < numPeople; i++) {
            System.out.println(familyNames[i]);
            System.out.print("Uppers: ");
            for (int j = 0; j < MAX_TEETH; j++) {
                char upper = teethRecords[i][0][j];
                if (upper != '\u0000') {
                    System.out.print((j + 1) + ":" + teethRecords[i][0][j] + "  ");
                } // end of if statement
            } // end of inner for loop

            System.out.println();

            System.out.print("Lowers: ");
            for (int j = 0; j < MAX_TEETH; j++) {
                char lower = teethRecords[i][1][j];
                if (lower != '\u0000') {
                    System.out.print((j + 1) + ":" + teethRecords[i][1][j] + "  ");
                } // end of if statement
            } // end of inner for loop

            System.out.println();

        } // end of for loop

    } //end of printFamilyRecords method

// -----------------------------------------------------------------------------------------------------------------
    /**
     * Allows the user to extract a specific tooth from a family member.
     * Replaces the selected tooth with 'M' (missing) after validating input.
     * @param numPeople Number of family members
     * @param familyNames Array of family member names
     * @param teethRecords Three-dimensional array of teeth data
     */
    private static void extractTooth(int numPeople, String[] familyNames, char[][][] teethRecords) {

        int memberIndex = -1;

        while (memberIndex == -1) {
            System.out.print("Which family member: ");
            String nameInput = keyboard.nextLine();
            for (int i = 0; i < numPeople; i++) {
                if (familyNames[i].equalsIgnoreCase(nameInput)) {
                    memberIndex = i;
                    break;
                } // end of if statement
            } // end of for loop
            if (memberIndex == -1) {
                System.out.println("Invalid family member, try again.");
            } // end of if statement
        } // end of while loop

        int layerIndex = -1;
        while (layerIndex == -1) {
            System.out.print("Which tooth layer (U)pper or (L)ower: ");
            String layerInput = keyboard.nextLine().toUpperCase();
            if (layerInput.equals("U")) {
                layerIndex = 0;
            }
            else if (layerInput.equals("L")) {
                layerIndex = 1;
            }
            else {
                System.out.println("Invalid layer, try again.");
            }
        } // end of while loop

        int toothNumber = 0;
        boolean validTooth = false;

        while (!validTooth) {
            System.out.print("Which tooth number: ");
            toothNumber = keyboard.nextInt();
            keyboard.nextLine();
            try {

                if (toothNumber < 1 || toothNumber > MAX_TEETH) {
                    System.out.println("Invalid tooth number, try again.");
                }

                else if (teethRecords[memberIndex][layerIndex][toothNumber - 1] == MISSING) {
                    System.out.println("Missing tooth, try again.");
                }

                else {
                    validTooth = true;
                }
            }

            catch (NumberFormatException e) {
                System.out.println("Invalid input, enter a number.");
            }
        } // end of while loop

        teethRecords[memberIndex][layerIndex][toothNumber - 1] = MISSING;
        System.out.println("Tooth removed.");

    } // End of extractTooth method

// -----------------------------------------------------------------------------------------------------------------
    /**
     * Calculates the root canal indices for the family based on the quadratic formula:
     * Ix^2 + Bx - M = 0, where I, B, and M are the total number of incisors, bicuspids,
     * and missing teeth in the family.
     * Prints both roots to the console.
     *
     * @param numPeople Number of family members
     * @param teethRecords Three-dimensional array of teeth data
     */
    private static void calculateRootCanals(int numPeople, char[][][] teethRecords) {
        int totalI = 0;
        int totalB = 0;
        int totalM = 0;

        for (int i = 0; i < numPeople; i++) {
            for (int layer = 0; layer < 2; layer++) {
                for (int j = 0; j < MAX_TEETH; j++) {
                    char c = teethRecords[i][layer][j];
                    if (c == INCISOR) {
                        totalI++;
                    }
                    else if (c == BICUSPID) {
                        totalB++;
                    }
                    else if (c == MISSING) {
                        totalM++;
                    }
                }  // end of inner for loop
            }  // end of middle for loop
        } // end of outer for loop

        if (totalI == 0) {
            System.out.println("Cannot calculate roots, no incisors (division by zero).");
            return;
        } // end of if statement

        double discriminant = totalB * totalB + 4 * totalI * totalM;
        double sqrtDisc = Math.sqrt(discriminant);

        double root1 = (-totalB + sqrtDisc) / (2.0 * totalI);
        double root2 = (-totalB - sqrtDisc) / (2.0 * totalI);

        System.out.printf("One root canal at %.2f%n", root1);
        System.out.printf("Another root canal at %.2f%n", root2);

    } // End of calculateRootCanals method

// -----------------------------------------------------------------------------------------------------------------

} // End of FloridaDentalAssociation