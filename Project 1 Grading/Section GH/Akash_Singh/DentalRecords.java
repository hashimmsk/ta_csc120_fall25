import java.util.Scanner;

/**
 * This program manages a family's dental records, including input, storage, and operations such as
 * printing records, extracting teeth, and calculating root canal indices.
 *
 * Also, this program allows up to 6 family members, each with up to 8 teeth per jaw.
 * Teeth are represented by the characters:
 * I - Intact
 * B - Broken
 * M - Missing
 *
 * @author Aakash Singh
 * @version 1.0
 */

public class DentalRecords {

    // ----------------------------------------------------------------------

    /**
     * Scanner object used to read user input from the console.
     * @see DentalRecords
     */
    private static final Scanner keyboard = new Scanner(System.in);

    // Static constants

    /**
     * Maximum number of family members allowed.
     * @see DentalRecords
     */
    private static final int MAX_PEOPLE = 6;

    /**
     * Maximum number of teeth per jaw.
     * @see DentalRecords
     */
    private static final int MAX_TEETH = 8;

    // ----------------------------------------------------------------------

    /**
     * Entry point for the DentalRecords program.
     * Displays a menu and performs user-selected operations until exit is chosen.
     *
     * @param args Command-line arguments (not used)
     */

    public static void main(String[] args) {

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        // Initialize arrays
        int familyCount = getFamilyCount();
        String[] names = new String[familyCount];
        char[][][] teeth = new char[familyCount][2][MAX_TEETH];

        // Input family data
        inputFamilyData(names, teeth);

        // Menu control loop
        char option;
        do {
            option = getMenuOption();

            if (option == 'P') {
                printRecords(names, teeth);
            } //end of if statement
            else if (option == 'E') {
                extractTooth(names, teeth);
            } //end of if statement
            else if (option == 'R') {
                showRootCanalIndices(teeth);
            } //end of if statement

        } while (option != 'X');

        // Exit message
        System.out.println("\nExiting the Floridian Tooth Records :-)");
    } //End of main method

    // ----------------------------------------------------------------------

    /**
     * Prompts the user for the number of family members and validates the input.
     *
     * @return The number of people in the family (between 1 and 6)
     */

    private static int getFamilyCount() {
        int count;

        System.out.print("Please enter number of people in the family : ");
        count = keyboard.nextInt();
        keyboard.nextLine();

        while (count < 1 || count > MAX_PEOPLE) {
            System.out.print("Invalid number of people, try again         : ");
            count = keyboard.nextInt();
            keyboard.nextLine();
        } //end of while loop

        return count;
    } //End of getFamilyCount method

    // ----------------------------------------------------------------------

    /**
     * Collects and stores each family member's name and corresponding tooth data.
     *
     * @param names Array for storing each family member's name
     * @param teeth 3D array for storing each member’s upper and lower teeth data
     */

    private static void inputFamilyData(String[] names, char[][][] teeth) {

        for (int i = 0; i < names.length; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + "   : ");
            names[i] = keyboard.nextLine().trim();

            for (int j = 0; j < 2; j++) {
                String jaw = (j == 0) ? "uppers" : "lowers";
                String input = getTeethString(names[i], jaw);

                // Store valid teeth data
                for (int k = 0; k < input.length(); k++) {
                    teeth[i][j][k] = input.charAt(k);
                } //end of for loop

                for (int k = input.length(); k < MAX_TEETH; k++) {
                    teeth[i][j][k] = ' ';
                } //end of for loop
            } //end of for loop
        } //end of for loop
    } //End of inputFamilyData method

    // ----------------------------------------------------------------------

    /**
     * Prompts the user to enter a valid string of teeth for a given jaw.
     *
     * @param name The name of the family member
     * @param jaw The jaw being entered (uppers or lowers)
     * @return A validated string representing the teeth status (I, B, or M)
     */

    private static String getTeethString(String name, String jaw) {
        String input;
        boolean valid = false;

        System.out.print("Please enter the " + jaw + " for " + name + "       : ");
        input = keyboard.nextLine().toUpperCase();

        while (!valid) {
            if (input.length() > MAX_TEETH) {
                System.out.print("Too many teeth, try again                   : ");
                input = keyboard.nextLine().toUpperCase();
            } //end of if statement
            else if (!input.matches("[IBM]+")) {
                System.out.print("Invalid teeth types, try again              : ");
                input = keyboard.nextLine().toUpperCase();
            } //end of if statement
            else {
                valid = true;
            } //end of if statement
        } //end of while loop

        return input;
    } //End of getTeethString method

    // ----------------------------------------------------------------------

    /**
     * Displays the menu options and validates the user’s selection.
     *
     * @return The validated menu choice (P, E, R, or X)
     */

    private static char getMenuOption() {
        System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
        char choice = keyboard.next().toUpperCase().charAt(0);
        keyboard.nextLine();

        while ("PERX".indexOf(choice) == -1) {
            System.out.print("Invalid menu option, try again              : ");
            choice = keyboard.next().toUpperCase().charAt(0);
            keyboard.nextLine();
        } //end of while loop

        return choice;
    } //End of getMenuOption method

    // ----------------------------------------------------------------------

    /**
     * Displays the stored dental records for each family member.
     *
     * @param names Array containing the family members' names
     * @param teeth 3D array containing the family members' teeth data
     */

    private static void printRecords(String[] names, char[][][] teeth) {
        for (int i = 0; i < names.length; i++) {
            System.out.println("\n" + names[i]);

            System.out.print("  Uppers:");
            for (int j = 0; j < MAX_TEETH; j++) {
                if (teeth[i][0][j] != ' ') {
                    System.out.print("  " + (j + 1) + ":" + teeth[i][0][j]);
                } //end of if statement
            } //end of for loop

            System.out.print("\n  Lowers:");
            for (int j = 0; j < MAX_TEETH; j++) {
                if (teeth[i][1][j] != ' ') {
                    System.out.print("  " + (j + 1) + ":" + teeth[i][1][j]);
                } //end of if statement
            } //end of for loop

            System.out.println();
        } //end of for loop
    } //End of printRecords method

    // ----------------------------------------------------------------------

    /**
     * Allows the user to extract a specific tooth from a selected family member.
     *
     * @param names Array containing family member names
     * @param teeth 3D array containing each member’s teeth data
     */

    private static void extractTooth(String[] names, char[][][] teeth) {
        System.out.print("Which family member                         : ");
        String target = keyboard.nextLine().trim();
        int personIndex = findPersonIndex(names, target);

        // Validate member name
        while (personIndex == -1) {
            System.out.print("Invalid family member, try again            : ");
            target = keyboard.nextLine().trim();
            personIndex = findPersonIndex(names, target);
        } //end of while loop

        // Select jaw layer
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        char layer = keyboard.next().toUpperCase().charAt(0);
        keyboard.nextLine();

        while (layer != 'U' && layer != 'L') {
            System.out.print("Invalid layer, try again                    : ");
            layer = keyboard.next().toUpperCase().charAt(0);
            keyboard.nextLine();
        } //end of while loop

        // Select specific tooth
        System.out.print("Which tooth number                          : ");
        int toothNum = keyboard.nextInt();
        keyboard.nextLine();

        while (toothNum < 1 || toothNum > MAX_TEETH ||
                teeth[personIndex][layer == 'U' ? 0 : 1][toothNum - 1] == 'M') {
            System.out.print("Invalid or missing tooth, try again         : ");
            toothNum = keyboard.nextInt();
            keyboard.nextLine();
        } //end of while loop

        // Mark as missing
        teeth[personIndex][layer == 'U' ? 0 : 1][toothNum - 1] = 'M';
    } //End of extractTooth method

    // ----------------------------------------------------------------------

    /**
     * Finds the index of a family member by name.
     *
     * @param names Array containing family member names
     * @param target The name to search for
     * @return The index of the family member, or -1 if not found
     */

    private static int findPersonIndex(String[] names, String target) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(target)) {
                return i;
            } //end of if statement
        } //end of for loop
        return -1;
    } //End of findPersonIndex method

    // ----------------------------------------------------------------------

    /**
     * Calculates and displays the root canal indices based on current tooth data.
     * Uses the quadratic formula based on the counts of Intact, Broken, and Missing teeth.
     *
     * @param teeth 3D array containing the teeth data for all family members
     */

    private static void showRootCanalIndices(char[][][] teeth) {
        int I = 0, B = 0, M = 0;

        for (char[][] tooth : teeth) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < MAX_TEETH; k++) {
                    char t = tooth[j][k];
                    if (t == 'I') I++;
                    else if (t == 'B') B++;
                    else if (t == 'M') M++;
                } //end of for loop
            } //end of for loop
        } //end of for loop

        double discriminant = (B * B) + (4.0 * I * M);
        double root1 = (-B + Math.sqrt(discriminant)) / (2 * I);
        double root2 = (-B - Math.sqrt(discriminant)) / (2 * I);

        System.out.printf("One root canal at     %.2f\n", root1);
        System.out.printf("Another root canal at %.2f\n", root2);
    } //End of showRootCanalIndices method
} //End of DentalRecords class
