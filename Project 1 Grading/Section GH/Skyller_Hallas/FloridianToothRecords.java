import java.util.Scanner;

/**
 * Records and manages the tooth data for one Florida family.
 * @author Skyler Hallas
 * @version 1.0 (2025-10-27)
 */
public class FloridianToothRecords { // begin class

    /** Scanner used for keyboard input. */
    private static final Scanner keyboard = new Scanner(System.in);

    /** Maximum number of people in a family. */
    private static final int MAX_PEOPLE = 6;

    /** Maximum number of teeth per row. */
    private static final int MAX_TEETH = 8;

    /** Index value for upper teeth row. */
    private static final int UPPER = 0;

    /** Index value for lower teeth row. */
    private static final int LOWER = 1;

    /**
     * Main entry point of the program.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) { // begin main
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        int familySize = getFamilySize();

        // create data structures
        String[] names = new String[familySize];
        char[][][] teeth = new char[familySize][2][MAX_TEETH];
        int[][] toothCounts = new int[familySize][2];

        // get data for each family member
        int p = 0;
        while (p < familySize) { // begin loop through family
            names[p] = getName(p);

            char[] uppers = getTeethRow(names[p], "uppers");
            storeRow(p, UPPER, uppers, teeth, toothCounts);

            char[] lowers = getTeethRow(names[p], "lowers");
            storeRow(p, LOWER, lowers, teeth, toothCounts);

            p = p + 1;
        } // end loop through family

        // start menu
        menuLoop(names, teeth, toothCounts);

        System.out.println();
        System.out.println("Exiting the Floridian Tooth Records");
    } // end main

    /**
     * Prompts for and validates the number of family members (1–6).
     * @return The valid number of family members.
     */
    private static int getFamilySize() { // begin getFamilySize
        System.out.print("Please enter number of people in the family: ");
        int n = keyboard.nextInt();

        while (n < 1 || n > MAX_PEOPLE) { // begin validation
            System.out.print("Invalid number of people, try again: ");
            n = keyboard.nextInt();
        } // end validation
        return n;
    } // end getFamilySize

    /**
     * Prompts for and returns a family member's name.
     * @param index The index number of the current family member.
     * @return The name entered by the user.
     */
    private static String getName(int index) { // begin getName
        System.out.print("Please enter the name for family member " + (index + 1) + ": ");
        return keyboard.next();
    } // end getName

    /**
     * Gets and validates a string of teeth for a given person and row.
     * @param name The name of the family member.
     * @param whichRow Either "uppers" or "lowers".
     * @return A character array representing that row of teeth.
     */
    private static char[] getTeethRow(String name, String whichRow) { // begin getTeethRow
        String s = "";
        boolean valid = false;

        while (!valid) { // begin validation loop
            System.out.print("Please enter the " + whichRow + " for " + name + ": ");
            s = keyboard.next().trim();

            if (s.length() == 0) { // empty check
                System.out.print("No teeth entered, try again: ");
            } else if (s.length() > MAX_TEETH) { // too many
                System.out.print("Too many teeth, try again: ");
            } else { // check content
                String u = s.toUpperCase();
                int invalidFound = 0;
                int i = 0;
                while (i < u.length()) { // check letters
                    char c = u.charAt(i);
                    if (c != 'I' && c != 'B' && c != 'M') {
                        invalidFound = 1;
                    }
                    i = i + 1;
                } // end letter check
                if (invalidFound == 1) {
                    System.out.print("Invalid teeth types, try again: ");
                } else {
                    valid = true;
                    s = u;
                }
            }
        } // end validation loop

        // convert to char array
        char[] row = new char[s.length()];
        int k = 0;
        while (k < s.length()) { // begin copy loop
            row[k] = s.charAt(k);
            k = k + 1;
        } // end copy loop
        return row;
    } // end getTeethRow

    /**
     * Stores one row of teeth into the 3D array.
     * @param person Index of the person.
     * @param layer 0 for upper, 1 for lower.
     * @param row The character array of teeth.
     * @param teeth 3D array of all teeth data.
     * @param counts 2D array of tooth counts per row.
     */
    private static void storeRow(int person, int layer, char[] row,
                                 char[][][] teeth, int[][] counts) { // begin storeRow
        counts[person][layer] = row.length;
        int i = 0;
        while (i < row.length) { // begin copy loop
            teeth[person][layer][i] = row[i];
            i = i + 1;
        } // end copy loop
    } // end storeRow

    /**
     * Displays a menu for the user to choose actions.
     * @param names Array of family member names.
     * @param teeth 3D array of tooth data.
     * @param counts 2D array of tooth counts.
     */
    private static void menuLoop(String[] names, char[][][] teeth, int[][] counts) { // begin menuLoop
        boolean done = false; // flag to end loop
        while (!done) { // begin loop
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it: ");
            char choice = Character.toUpperCase(keyboard.next().charAt(0));

            if (choice == 'P') {
                printFamily(names, teeth, counts);
            } else if (choice == 'E') {
                extractTooth(names, teeth, counts);
            } else if (choice == 'R') {
                reportRoots(teeth, counts);
            } else if (choice == 'X') {
                done = true;
            } else {
                System.out.print("Invalid menu option, try again: ");
            }
        } // end loop
    } // end menuLoop

    /**
     * Prints all family members and their upper/lower teeth.
     * @param names Array of family member names.
     * @param teeth 3D array of tooth data.
     * @param counts 2D array of tooth counts.
     */
    private static void printFamily(String[] names, char[][][] teeth, int[][] counts) { // begin printFamily
        System.out.println();
        int p = 0;
        while (p < names.length) { // begin person loop
            System.out.println(names[p]);

            System.out.print("  Uppers:");
            int i = 0;
            while (i < counts[p][UPPER]) { // print upper
                System.out.print("  " + (i + 1) + ":" + teeth[p][UPPER][i]);
                i = i + 1;
            }
            System.out.println();

            System.out.print("  Lowers:");
            int j = 0;
            while (j < counts[p][LOWER]) { // print lower
                System.out.print("  " + (j + 1) + ":" + teeth[p][LOWER][j]);
                j = j + 1;
            }
            System.out.println();

            p = p + 1;
        } // end person loop
    } // end printFamily

    /**
     * Extracts (removes) a specific tooth and marks it as missing.
     * @param names Array of family names.
     * @param teeth 3D array of tooth data.
     * @param counts 2D array of tooth counts.
     */
    private static void extractTooth(String[] names, char[][][] teeth, int[][] counts) { // begin extractTooth
        int person = findPersonIndex(names);
        int layer = selectLayer();

        int max = counts[person][layer];
        int toothNum = 0;

        while (toothNum < 1 || toothNum > max || teeth[person][layer][toothNum - 1] == 'M') { // validation
            System.out.print("Which tooth number: ");
            int t = keyboard.nextInt();

            if (t < 1 || t > max) {
                System.out.print("Invalid tooth number, try again: ");
            } else if (teeth[person][layer][t - 1] == 'M') {
                System.out.print("Missing tooth, try again: ");
            } else {
                toothNum = t;
            }
        } // end validation
        teeth[person][layer][toothNum - 1] = 'M';
    } // end extractTooth

    /**
     * Finds and returns the index of a person by name (case-insensitive).
     * @param names Array of family member names.
     * @return The index of the person found.
     */
    private static int findPersonIndex(String[] names) { // begin findPersonIndex
        int index = -1;
        while (index < 0) {
            System.out.print("Which family member: ");
            String who = keyboard.next();

            int i = 0;
            int foundIndex = -1;
            while (i < names.length) {
                if (names[i].equalsIgnoreCase(who)) {
                    foundIndex = i;
                }
                i = i + 1;
            }
            if (foundIndex >= 0) {
                index = foundIndex;
            } else {
                System.out.print("Invalid family member, try again: ");
            }
        }
        return index;
    } // end findPersonIndex

    /**
     * Prompts the user to choose either upper or lower layer.
     * @return Integer constant for upper (0) or lower (1).
     */
    private static int selectLayer() { // begin selectLayer
        int layer = -1;
        while (layer != UPPER && layer != LOWER) {
            System.out.print("Which tooth layer (U)pper or (L)ower: ");
            char ch = Character.toUpperCase(keyboard.next().charAt(0));
            if (ch == 'U') {
                layer = UPPER;
            } else if (ch == 'L') {
                layer = LOWER;
            } else {
                System.out.print("Invalid layer, try again: ");
            }
        }
        return layer;
    } // end selectLayer

    /**
     * Counts all occurrences of 'I', 'B', and 'M' teeth across the family.
     * @param teeth 3D array of tooth data.
     * @param counts 2D array of tooth counts.
     * @return An array of three integers: counts of I, B, and M.
     */
    private static int[] countIBM(char[][][] teeth, int[][] counts) { // begin countIBM
        int[] res = new int[3];
        int p = 0;
        while (p < teeth.length) {
            int l = 0;
            while (l < 2) {
                int t = 0;
                while (t < counts[p][l]) {
                    char c = teeth[p][l][t];
                    if (c == 'I') {
                        res[0]++;
                    } else if (c == 'B') {
                        res[1]++;
                    } else if (c == 'M') {
                        res[2]++;
                    }
                    t++;
                }
                l++;
            }
            p++;
        }
        return res;
    } // end countIBM

    /**
     * Computes and prints the quadratic root canal indices for the family.
     * Equation: I·x² + B·x − M = 0.
     * @param teeth 3D array of tooth data.
     * @param counts 2D array of tooth counts.
     */
    private static void reportRoots(char[][][] teeth, int[][] counts) { // begin reportRoots
        int[] ibm = countIBM(teeth, counts);
        int I = ibm[0];
        int B = ibm[1];
        int M = ibm[2];

        if (I != 0) {
            double disc = (B * B) + (4.0 * I * M);
            double root1 = (-B + Math.sqrt(disc)) / (2.0 * I);
            double root2 = (-B - Math.sqrt(disc)) / (2.0 * I);
            System.out.println("One root canal at " + String.format("%.2f", root1));
            System.out.println("Another root canal at " + String.format("%.2f", root2));
        } else if (B != 0) {
            double root = M / (double) B;
            System.out.println("One root canal at " + String.format("%.2f", root));
        } else {
            System.out.println("No root canals.");
        }
    } // end reportRoots
} // end class