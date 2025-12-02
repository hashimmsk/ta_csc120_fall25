import java.util.*;

/** Main class for Floridian Tooth Records program */
public class FloridianToothRecords {
    /** Main method */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final int MAX_FAMILY = 6;
        final int MAX_TEETH = 8;

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        int familyCount = getFamilySize(sc, MAX_FAMILY);
        String[] names = new String[familyCount];
        char[][][] teeth = new char[familyCount][2][MAX_TEETH];
        int[] upperCount = new int[familyCount];
        int[] lowerCount = new int[familyCount];

        inputFamilyData(sc, names, teeth, upperCount, lowerCount, familyCount, MAX_TEETH);
        menuLoop(sc, names, teeth, upperCount, lowerCount, familyCount);
    } // end of main
    /** Gets a valid family size between 1 and MAX_FAMILY */
    public static int getFamilySize(Scanner sc, int max) {
        int familySize;
        System.out.print("Please enter number of people in the family : ");
        familySize = sc.nextInt();
        sc.nextLine();
        while (familySize < 1 || familySize > max) {
            System.out.print("Invalid number of people, try again         : ");
            familySize = sc.nextInt();
            sc.nextLine();
        }
        return familySize;
    } // end of getFamilySize
    /** Inputs all names and the teeth for each family member */
    public static void inputFamilyData(Scanner sc, String[] names, char[][][] teeth,
                                       int[] upperCount, int[] lowerCount,
                                       int count, int maxTeeth) {
        for (int i = 0; i < count; i++) { // loop for each family member
            System.out.print("Please enter the name for family member " + (i + 1) + "   : ");
            names[i] = sc.nextLine();

            upperCount[i] = getTeethRow(sc, "uppers", names[i], teeth[i][0], maxTeeth);
            lowerCount[i] = getTeethRow(sc, "lowers", names[i], teeth[i][1], maxTeeth);
        } // end of family loop
    } // end of inputFamilyData
    /** Reads and validates one row of teeth for someone */
    public static int getTeethRow(Scanner sc, String layer, String name, char[] row, int max) {
        String input;
        boolean valid = false;
        int count = 0;

        System.out.print("Please enter the " + layer + " for " + name + "       : ");
        input = sc.nextLine().trim().toUpperCase();

        while (!valid) { // loop until valid input
            if (input.length() > max) {
                System.out.print("Too many teeth, try again                   : ");
                input = sc.nextLine().trim().toUpperCase();
            } else if (!input.matches("[IBM]+")) {
                System.out.print("Invalid teeth types, try again              : ");
                input = sc.nextLine().trim().toUpperCase();
            } else {
                valid = true;
            }
        } // end of validation loop

        count = input.length();
        for (int i = 0; i < count; i++) {
            row[i] = input.charAt(i);
        } // end of copy loop
        return count;
    } // end of getTeethRow
    /** Displays the main menu loop */
    public static void menuLoop(Scanner sc, String[] names, char[][][] teeth,
                                int[] upperCount, int[] lowerCount, int count) {
        char choice;
        String line;

        choice = ' ';
        while (choice != 'X') { // menu runs until user exits
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            line = sc.nextLine().trim().toUpperCase();

            if (line.length() == 0) {
                System.out.print("Invalid menu option, try again              : ");
                continue;
            }
            choice = line.charAt(0);

            if (choice == 'P') {
                printRecords(names, teeth, upperCount, lowerCount, count);
            } else if (choice == 'E') {
                extractTooth(sc, names, teeth, upperCount, lowerCount, count);
            } else if (choice == 'R') {
                reportRoots(teeth, upperCount, lowerCount, count);
            } else if (choice == 'X') {
                System.out.println("\nExiting the Floridian Tooth Records :-)");
            } else {
                System.out.print("Invalid menu option, try again              : ");
            }
        } // end of menu loop
    } // end of menuLoop
    /** Prints all of the familys tooth records */
    public static void printRecords(String[] names, char[][][] teeth,
                                    int[] upperCount, int[] lowerCount, int count) {
        for (int i = 0; i < count; i++) { // loop for each family member
            System.out.println("\n" + names[i]);
            System.out.print("  Uppers:");
            for (int t = 0; t < upperCount[i]; t++) {
                System.out.print("  " + (t + 1) + ":" + teeth[i][0][t]);
            } // end of upper loop
            System.out.print("\n  Lowers:");
            for (int t = 0; t < lowerCount[i]; t++) {
                System.out.print("  " + (t + 1) + ":" + teeth[i][1][t]);
            } // end of lower loop
            System.out.println();
        } // end of member loop
    } // end of printRecords
    /** Extracts a single tooth (changes to 'M') */
    public static void extractTooth(Scanner sc, String[] names, char[][][] teeth,
                                    int[] upperCount, int[] lowerCount, int count) {

        // ask for family member
        String person;
        int memberIndex = -1;
        System.out.print("Which family member                         : ");
        person = sc.nextLine().trim();

        while (memberIndex == -1) { // loop until valid member name
            for (int i = 0; i < count; i++) {
                if (names[i].equalsIgnoreCase(person)) {
                    memberIndex = i;
                }
            }
            if (memberIndex == -1) {
                System.out.print("Invalid family member, try again            : ");
                person = sc.nextLine().trim();
            }
        } // end of member validation loop

        // ask for layer
        int layer = -1;
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        String layerInput = sc.nextLine().trim().toUpperCase();
        while (layer == -1) { // loop until valid layer
            if (layerInput.equals("U")) {
                layer = 0;
            } else if (layerInput.equals("L")) {
                layer = 1;
            } else {
                System.out.print("Invalid layer, try again                    : ");
                layerInput = sc.nextLine().trim().toUpperCase();
            }
        } // end of layer validation loop

        // ask for tooth number
        int maxTeeth = (layer == 0) ? upperCount[memberIndex] : lowerCount[memberIndex];
        int toothNum;
        System.out.print("Which tooth number                          : ");
        toothNum = sc.nextInt();
        sc.nextLine();
        boolean valid = false;

        while (!valid) { // loop until valid tooth number
            if (toothNum < 1 || toothNum > maxTeeth) {
                System.out.print("Invalid tooth number, try again             : ");
                toothNum = sc.nextInt();
                sc.nextLine();
            } else if (teeth[memberIndex][layer][toothNum - 1] == 'M') {
                System.out.print("Missing tooth, try again                    : ");
                toothNum = sc.nextInt();
                sc.nextLine();
            } else {
                valid = true;
            }
        } // end of tooth validation loop

        teeth[memberIndex][layer][toothNum - 1] = 'M'; // extract tooth
    } // end of extractTooth
    /** Calculates and prints the family's root canal indices */
    public static void reportRoots(char[][][] teeth,
                                   int[] upperCount, int[] lowerCount, int count) {
        int incisors = 0, bicuspids = 0, missing = 0;

        for (int i = 0; i < count; i++) { // loop for each family member
            for (int t = 0; t < upperCount[i]; t++) {
                char tooth = teeth[i][0][t];
                if (tooth == 'I') incisors++;
                else if (tooth == 'B') bicuspids++;
                else if (tooth == 'M') missing++;
            } // end of upper loop

            for (int t = 0; t < lowerCount[i]; t++) {
                char tooth = teeth[i][1][t];
                if (tooth == 'I') incisors++;
                else if (tooth == 'B') bicuspids++;
                else if (tooth == 'M') missing++;
            } // end of lower loop
        } // end of member loop

        if (incisors == 0) {
            System.out.println("No incisors; cannot compute root canals.");
        } else {
            double disc = Math.sqrt(bicuspids * bicuspids + 4.0 * incisors * missing);
            double root1 = (-bicuspids + disc) / (2 * incisors);
            double root2 = (-bicuspids - disc) / (2 * incisors);

            System.out.printf("One root canal at     %.2f%n", root1);
            System.out.printf("Another root canal at %.2f%n", root2);
        } // end of root calculation
    } // end of reportRoots
} // end of class FloridianToothRecords