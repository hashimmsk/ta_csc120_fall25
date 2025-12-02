import java.util.Scanner;
import java.util.Locale;

/**
 * Florida Dental Association Tooth Records Program
 * Records and manages dental information for Florida families
 *
 * @author Student
 * @version 1.0
 * @since 2025
 */
public class DentalRecords {

    // Constants
    private static final int MAX_FAMILY_MEMBERS = 6;
    private static final int MAX_TEETH_PER_ROW = 8;
    private static final int UPPER_ROW = 0;
    private static final int LOWER_ROW = 1;

    private Scanner scanner;
    private String[] familyNames;
    private char[][][] teethData;
    private int familySize;

    /**
     * Constructor - initializes the program
     */
    public DentalRecords() {
        scanner = new Scanner(System.in);
        familyNames = new String[MAX_FAMILY_MEMBERS];
        teethData = new char[MAX_FAMILY_MEMBERS][2][MAX_TEETH_PER_ROW];
    }

    /**
     * Main method - program entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        DentalRecords program = new DentalRecords();
        program.run();
    }

    /**
     * Main program execution flow
     */
    public void run() {
        displayWelcome();
        getFamilyData();
        displayMenuLoop();
    }

    /**
     * Displays welcome message
     */
    private void displayWelcome() {
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");
    }

    /**
     * Gets all family data from user
     */
    private void getFamilyData() {
        familySize = getValidNumberOfPeople();

        for (int i = 0; i < familySize; i++) {
            System.out.printf("Please enter the name for family member %d   : ", i + 1);
            familyNames[i] = scanner.nextLine().trim();

            // Get upper teeth
            String upperTeeth = getValidToothString("uppers", familyNames[i]);
            populateToothRow(i, UPPER_ROW, upperTeeth);

            // Get lower teeth
            String lowerTeeth = getValidToothString("lowers", familyNames[i]);
            populateToothRow(i, LOWER_ROW, lowerTeeth);
        }
    }

    /**
     * Gets valid number of family members
     * @return valid number of people (1-6)
     */
    private int getValidNumberOfPeople() {
        int numPeople;
        while (true) {
            System.out.print("Please enter number of people in the family : ");
            numPeople = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (numPeople >= 1 && numPeople <= MAX_FAMILY_MEMBERS) {
                break;
            }
            System.out.print("Invalid number of people, try again         : ");
        }
        return numPeople;
    }

    /**
     * Gets valid tooth string from user
     * @param layerType "uppers" or "lowers"
     * @param name family member name
     * @return valid tooth string
     */
    private String getValidToothString(String layerType, String name) {
        String toothString;
        while (true) {
            System.out.printf("Please enter the %s for %-15s : ", layerType, name);
            toothString = scanner.nextLine().trim().toUpperCase();

            // Check length
            if (toothString.length() > MAX_TEETH_PER_ROW) {
                System.out.print("Too many teeth, try again                   : ");
                continue;
            }

            // Check valid characters
            boolean valid = true;
            for (char c : toothString.toCharArray()) {
                if (c != 'I' && c != 'B' && c != 'M') {
                    valid = false;
                    break;
                }
            }

            if (!valid) {
                System.out.print("Invalid teeth types, try again              : ");
            } else {
                break;
            }
        }
        return toothString;
    }

    /**
     * Populates tooth row array with tooth data
     * @param personIndex index of family member
     * @param rowIndex UPPER_ROW or LOWER_ROW
     * @param toothString string of tooth characters
     */
    private void populateToothRow(int personIndex, int rowIndex, String toothString) {
        // Initialize with empty teeth
        for (int i = 0; i < MAX_TEETH_PER_ROW; i++) {
            teethData[personIndex][rowIndex][i] = ' ';
        }

        // Fill with actual teeth data
        for (int i = 0; i < toothString.length(); i++) {
            teethData[personIndex][rowIndex][i] = toothString.charAt(i);
        }
    }

    /**
     * Main menu loop
     */
    private void displayMenuLoop() {
        char choice;
        do {
            displayMenu();
            choice = getValidMenuChoice();
            processMenuChoice(choice);
        } while (choice != 'X');
    }

    /**
     * Displays menu options
     */
    private void displayMenu() {
        System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
    }

    /**
     * Gets valid menu choice from user
     * @return valid menu character
     */
    private char getValidMenuChoice() {
        char choice;
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.isEmpty()) {
                System.out.print("Invalid menu option, try again              : ");
                continue;
            }
            choice = input.charAt(0);

            if (choice == 'P' || choice == 'E' || choice == 'R' || choice == 'X') {
                break;
            }
            System.out.print("Invalid menu option, try again              : ");
        }
        return choice;
    }

    /**
     * Processes user's menu choice
     * @param choice menu choice character
     */
    private void processMenuChoice(char choice) {
        switch (choice) {
            case 'P':
                printRecords();
                break;
            case 'E':
                extractTooth();
                break;
            case 'R':
                calculateRootCanals();
                break;
            case 'X':
                exitProgram();
                break;
        }
    }

    /**
     * Prints all family dental records
     */
    private void printRecords() {
        for (int i = 0; i < familySize; i++) {
            System.out.println("\n" + familyNames[i]);
            printToothRow("Uppers", i, UPPER_ROW);
            printToothRow("Lowers", i, LOWER_ROW);
        }
    }

    /**
     * Prints a single row of teeth
     * @param rowName "Uppers" or "Lowers"
     * @param personIndex family member index
     * @param rowIndex tooth row index
     */
    private void printToothRow(String rowName, int personIndex, int rowIndex) {
        System.out.printf("  %s: ", rowName);
        boolean firstTooth = true;

        for (int toothNum = 0; toothNum < MAX_TEETH_PER_ROW; toothNum++) {
            char tooth = teethData[personIndex][rowIndex][toothNum];
            if (tooth != ' ') {
                if (!firstTooth) {
                    System.out.print(" ");
                }
                System.out.printf("%d:%c", toothNum + 1, tooth);
                firstTooth = false;
            }
        }
        System.out.println();
    }

    /**
     * Handles tooth extraction process
     */
    private void extractTooth() {
        // Get family member
        int memberIndex = findFamilyMember();
        if (memberIndex == -1) return;

        // Get layer
        int layer = getValidLayer();
        if (layer == -1) return;

        // Get tooth number
        int toothNumber = getValidToothNumber(memberIndex, layer);
        if (toothNumber == -1) return;

        // Extract tooth (set to missing)
        teethData[memberIndex][layer][toothNumber - 1] = 'M';
        System.out.println("Tooth extracted successfully.");
    }

    /**
     * Finds family member by name
     * @return index of family member or -1 if not found
     */
    private int findFamilyMember() {
        System.out.print("Which family member                         : ");
        String name = scanner.nextLine().trim();

        for (int i = 0; i < familySize; i++) {
            if (familyNames[i].equalsIgnoreCase(name)) {
                return i;
            }
        }

        System.out.print("Invalid family member, try again            : ");
        return findFamilyMember(); // Recursive retry
    }

    /**
     * Gets valid tooth layer from user
     * @return UPPER_ROW, LOWER_ROW, or -1 for error
     */
    private int getValidLayer() {
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.equals("U") || input.equals("UPPER")) {
            return UPPER_ROW;
        } else if (input.equals("L") || input.equals("LOWER")) {
            return LOWER_ROW;
        } else {
            System.out.print("Invalid layer, try again                    : ");
            return getValidLayer(); // Recursive retry
        }
    }

    /**
     * Gets valid tooth number from user
     * @param memberIndex family member index
     * @param layer tooth layer
     * @return valid tooth number or -1 for error
     */
    private int getValidToothNumber(int memberIndex, int layer) {
        int toothNumber;
        while (true) {
            System.out.print("Which tooth number                          : ");
            toothNumber = scanner.nextInt();
            scanner.nextLine(); // consume newline

            // Check range
            if (toothNumber < 1 || toothNumber > MAX_TEETH_PER_ROW) {
                System.out.print("Invalid tooth number, try again             : ");
                continue;
            }

            // Check if tooth exists and is not already missing
            char currentTooth = teethData[memberIndex][layer][toothNumber - 1];
            if (currentTooth == ' ') {
                System.out.print("No tooth at that position, try again        : ");
            } else if (currentTooth == 'M') {
                System.out.print("Missing tooth, try again                    : ");
            } else {
                break;
            }
        }
        return toothNumber;
    }

    /**
     * Calculates and displays root canal indices
     */
    private void calculateRootCanals() {
        int iCount = 0, bCount = 0, mCount = 0;

        // Count all teeth
        for (int person = 0; person < familySize; person++) {
            for (int layer = 0; layer < 2; layer++) {
                for (int tooth = 0; tooth < MAX_TEETH_PER_ROW; tooth++) {
                    char toothType = teethData[person][layer][tooth];
                    switch (toothType) {
                        case 'I': iCount++; break;
                        case 'B': bCount++; break;
                        case 'M': mCount++; break;
                    }
                }
            }
        }

        // Solve quadratic equation: Ix² + Bx - M = 0
        double[] roots = solveQuadratic(iCount, bCount, -mCount);

        if (roots == null) {
            System.out.println("No real roots exist for the tooth data.");
        } else if (roots[0] == roots[1]) {
            System.out.printf("One root canal at %8.2f\n", roots[0]);
        } else {
            System.out.printf("One root canal at %8.2f\n", roots[0]);
            System.out.printf("Another root canal at %8.2f\n", roots[1]);
        }
    }

    /**
     * Solves quadratic equation ax² + bx + c = 0
     * @param a coefficient a
     * @param b coefficient b
     * @param c coefficient c
     * @return array of roots or null if no real roots
     */
    private double[] solveQuadratic(double a, double b, double c) {
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null; // No real roots
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double root1 = (-b + sqrtDiscriminant) / (2 * a);
        double root2 = (-b - sqrtDiscriminant) / (2 * a);

        return new double[]{root1, root2};
    }

    /**
     * Exits program with smile
     */
    private void exitProgram() {
        System.out.println("\nExiting the Floridian Tooth Records :-)");
        scanner.close();
    }
}