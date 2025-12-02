import java.util.Scanner;

/**
 * FloridianToothRecords
 *
 * This program records and manages the tooth information for a Florida family.
 * Each family member can have up to 8 upper and 8 lower teeth.
 * Tooth types:
 * I - Incisor
 * B - Bicuspid
 * M - Missing
 *
 * The program allows the user to:
 * 1. Print the family's tooth records.
 * 2. Extract (remove) a tooth.
 * 3. Calculate and report the family's root canal indices (quadratic roots).
 * 4. Exit the system.
 *
 *  @author Mohammad Ayaan Parvez
 */
public class FloridianToothRecords {

    // Maximum number of people allowed in the family
    private static final int MAX_PEOPLE = 6;

    // Maximum number of teeth per upper or lower row
    private static final int MAX_TEETH = 8;

    // Scanner object for reading user input
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * Main method - entry point for the program.
     * It handles the main loop and menu system for interacting with the user.
     */
    public static void main(String[] args) {
        // Display welcome message
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        // Ask for the number of family members and validate
        int numPeople = getNumPeople();
        int i;

        // Arrays for storing names and teeth data
        String[] names = new String[numPeople];                   // Names of family members
        char[][][] teeth = new char[numPeople][2][MAX_TEETH];     // Teeth data: person, row (upper/lower), tooth slot
        int[][] teethCounts = new int[numPeople][2];              // Number of teeth per row for each person

        // Collect family member names and teeth data
        for (i = 0; i < numPeople; i++) {
            System.out.printf("%-50s: ", "Please enter the name for family member " + (i + 1));
            names[i] = keyboard.nextLine().trim(); // Read and store the member's name

            // Get teeth for both upper and lower rows
            teethCounts[i][0] = getTeeth("uppers for " + names[i], teeth[i][0]);
            teethCounts[i][1] = getTeeth("lowers for " + names[i], teeth[i][1]);
        }

        boolean running = true;

        // Display initial menu
        System.out.printf("\n%-50s: ", "(P)rint, (E)xtract, (R)oot, e(X)it");
        do {
            // Read user input and normalize case
            String choice = keyboard.nextLine().trim().toUpperCase();

            switch (choice) {
                case "P":
                    // Print entire family record
                    printFamily(names, teeth, teethCounts, numPeople);
                    System.out.printf("\n%-50s: ", "(P)rint, (E)xtract, (R)oot, e(X)it");
                    break;

                case "E":
                    // Extract a specific tooth
                    extractTooth(names, teeth, numPeople);
                    System.out.printf("\n%-50s: ", "(P)rint, (E)xtract, (R)oot, e(X)it");
                    break;

                case "R":
                    // Report root canal indices (quadratic roots)
                    reportRoots(teeth, numPeople);
                    System.out.printf("\n%-50s: ", "(P)rint, (E)xtract, (R)oot, e(X)it");
                    break;

                case "X":
                    // Exit the system
                    System.out.println("\nExiting the Floridian Tooth Records :-)");
                    running = false;
                    break;

                default:
                    // Handle invalid menu option
                    System.out.printf("%-50s: ", "Invalid menu option, try again");
            }
        } while (running); // Continue until user exits

        keyboard.close(); // Close scanner resource
    } // end of main method

    /**
     * getNumPeople
     * Prompts the user to enter how many people are in the family.
     * Validates that the input is between 1 and MAX_PEOPLE.
     *
     * @return valid number of family members
     */
    public static int getNumPeople() {
        int numberOfPeople;

        System.out.printf("%-50s: ", "Please enter number of people in the family");
        do {
            numberOfPeople = keyboard.nextInt();
            keyboard.nextLine(); // Clear leftover newline

            // Ensure number is valid
            if (numberOfPeople < 1 || numberOfPeople > MAX_PEOPLE)
                System.out.printf("%-50s: ", "Invalid number of people, try again");
        } while (numberOfPeople < 1 || numberOfPeople > MAX_PEOPLE);

        return numberOfPeople;
    } // end of getNumPeople method

    /**
     * getTeeth
     * Requests and validates a string of teeth characters (I, B, M) from the user.
     * Converts all characters to uppercase and ensures count is less than or equal MAX_TEETH.
     * Stores the valid tooth types into the provided character array.
     *
     * @param prompt descriptive message (e.g., "uppers for Henrietta")
     * @param storage character array to store teeth data
     * @return number of valid teeth entered
     */
    public static int getTeeth(String prompt, char[] storage) {
        String input;
        boolean valid;
        int i;

        System.out.printf("%-50s: ", "Please enter the " + prompt);
        do {
            input = keyboard.nextLine().trim().toUpperCase(); // Convert input to uppercase
            valid = true;

            // Too many teeth entered
            if (input.length() > MAX_TEETH) {
                System.out.printf("%-50s: ", "Too many teeth, try again");
                valid = false;
            } else {
                // Validate that each character is I, B, or M
                for (char c : input.toCharArray()) {
                    if (c != 'I' && c != 'B' && c != 'M') {
                        System.out.printf("%-50s: ", "Invalid teeth types, try again");
                        valid = false;
                        break;
                    }
                }
            }
        } while (!valid); // Repeat until input is valid

        // Store characters into array
        for (i = 0; i < input.length(); i++) {
            storage[i] = input.charAt(i);
        }

        // Fill remaining positions with space for consistency
        for (i = input.length(); i < MAX_TEETH; i++) {
            storage[i] = ' ';
        }

        return input.length(); // Return count of entered teeth
    } // end of getTeeth method

    /**
     * printFamily
     * Displays the complete tooth records for all family members.
     *
     * @param names array of family member names
     * @param teeth 3D array storing tooth data
     * @param counts 2D array with number of teeth per row
     * @param numPeople number of family members
     */
    public static void printFamily(String[] names, char[][][] teeth, int[][] counts, int numPeople) {
        int i, j;
        for (i = 0; i < numPeople; i++) {
            System.out.println("\n" + names[i]);
            System.out.print("  Uppers:");
            for (j = 0; j < counts[i][0]; j++) {
                System.out.print("  " + (j + 1) + ":" + teeth[i][0][j]);
            }
            System.out.println();
            System.out.print("  Lowers:");
            for (j = 0; j < counts[i][1]; j++) {
                System.out.print("  " + (j + 1) + ":" + teeth[i][1][j]);
            }
            System.out.println();
        }
    } // end of printFamily method

    /**
     * extractTooth
     * Allows user to remove a tooth from a chosen family member.
     * Prompts for member name, upper/lower layer, and tooth number.
     * Validates all input and ensures the selected tooth exists.
     *
     * @param names array of family member names
     * @param teeth 3D array containing tooth data
     * @param numPeople total number of family members
     */
    public static void extractTooth(String[] names, char[][][] teeth, int numPeople) {
        int personIndex = -1;

        // Ask for family member name
        System.out.printf("%-50s: ", "Which family member");
        do {
            String input = keyboard.nextLine().trim();
            int i;
            for (i = 0; i < numPeople; i++) {
                if (names[i].equalsIgnoreCase(input)) {
                    personIndex = i;
                    break;
                }
            }
            if (personIndex == -1)
                System.out.printf("%-50s: ", "Invalid family member, try again");
        } while (personIndex == -1);

        // Ask for upper or lower layer
        int layer = -1;
        System.out.printf("%-50s: ", "Which tooth layer (U)pper or (L)ower");
        do {
            String input = keyboard.nextLine().trim().toUpperCase();
            if (input.equals("U")) layer = 0;
            else if (input.equals("L")) layer = 1;
            else System.out.printf("%-50s: ", "Invalid layer, try again");
        } while (layer == -1);

        int toothNum;

        // Ask for tooth number
        System.out.printf("%-50s: ", "Which tooth number");
        do {
            toothNum = keyboard.nextInt();
            keyboard.nextLine(); // Clear newline

            // Validate number
            if (toothNum < 1 || toothNum > MAX_TEETH) {
                System.out.printf("%-50s: ", "Invalid tooth number, try again");
                continue;
            }

            // Check if already missing
            if (teeth[personIndex][layer][toothNum - 1] == 'M') {
                System.out.printf("%-50s: ", "Missing tooth, try again");
                toothNum = -1; // Force retry
            }
        } while (toothNum < 1 || toothNum > MAX_TEETH);

        // Mark tooth as missing
        teeth[personIndex][layer][toothNum - 1] = 'M';
    } // end of extractTooth method

    /**
     * reportRoots
     * Calculates and displays the roots of the quadratic equation:
     * I*x^2 + B*x - M = 0
     * where I, B, and M represent total counts of Incisors, Bicuspids, and Missing teeth.
     *
     * @param teeth 3D array containing all teeth data
     * @param numPeople number of family members
     */
    public static void reportRoots(char[][][] teeth, int numPeople) {
        int I = 0, B = 0, M = 0; // Counters for each tooth type
        int i, j, k;

        // Count total number of each tooth type
        for (i = 0; i < numPeople; i++) {
            for (j = 0; j < 2; j++) {
                for (k = 0; k < MAX_TEETH; k++) {
                    char c = teeth[i][j][k];
                    if (c == 'I') {
                        I++;
                    }
                    else if (c == 'B') {
                        B++;
                    }
                    else if (c == 'M') {
                        M++;
                    }
                }
            }
        }

        // Avoid division by zero if there are no incisors
        if (I == 0) {
            System.out.println("Cannot compute roots (no incisors recorded).");
            return;
        }

        // Compute discriminant and quadratic roots
        double disc = Math.sqrt(B * B + 4 * I * M);
        double root1 = (-B + disc) / (2 * I);
        double root2 = (-B - disc) / (2 * I);

        // Display roots
        System.out.printf("One root canal at     %.2f\n", root1);
        System.out.printf("Another root canal at %.2f\n", root2);
    } // end of reportRoots method
} // End of FloridianToothRecords Class
