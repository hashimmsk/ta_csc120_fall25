import java.util.Scanner;

/**
 * Manages the tooth information for a Florida family using tooth types of individual members;
 * Allows for printing their records, Extracting a tooth, Computing root canal indices, and exiting
 * @author Zack White
 */
public class DentalRecords {

    private static final Scanner input = new Scanner(System.in);

    // Declare Constants
    private static final int MAX_FAMILY = 6;
    private static final int MAX_TEETH = 8;
    private static final char INCISOR = 'I';
    private static final char BICUSPID = 'B';
    private static final char MISSING = 'M';

    // Main Method

    /**
     * Collects family data, displays the menu, and processes the user selections until the exit
     * @param args received from command line
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Floridian Tooth Records");

        int familyCount = getFamilyCount();

        String[] familyNames = new String[familyCount];
        char[][][] teethRecords = new char[familyCount][2][MAX_TEETH];

        for (int personIndex = 0; personIndex < familyCount; personIndex++) {
            familyNames[personIndex] = getFamilyMemberName(personIndex + 1);
            teethRecords[personIndex][0] = getToothRow(familyNames[personIndex], "uppers");
            teethRecords[personIndex][1] = getToothRow(familyNames[personIndex], "lowers");
        }// end of for loop

        boolean finished = false;
        while (!finished) {
            char menuOption = getMenuOption();
            switch (menuOption) {
                case 'P':
                    printFamilyRecords(familyNames, teethRecords);
                    break;
                case 'E':
                    extractTooth(familyNames, teethRecords);
                    break;
                case 'R':
                    reportRootCanals(familyNames, teethRecords);
                    break;
                case 'X':
                    finished = true;
                    System.out.println("Exiting the Floridian Tooth Records :-) ");
                    break;
            }// end of switch
        }// end of while loop

        input.close();

    }// end of main method

    // Input Methods

    /**
     * Prompts input and  validates the number of family members
     * @return a valid number of family members
     */
    private static int getFamilyCount() {
        int count;
        System.out.print("Please enter number of people in the family : ");
        while (true) {
            count = input.nextInt();
            input.nextLine();
            if (count >= 1 && count <= MAX_FAMILY) break;
            System.out.print("Invalid number of people, try again         : ");
        }// end of while loop

        return count;

    }// end of getFamilyCount method

    /**
     * Prompts input for the name of family member
     * @param memberNumber index of the family member
     * @return name of family member
     */
    private static String getFamilyMemberName(int memberNumber) {
        System.out.print("Please enter the name for family member " + memberNumber + "   : ");

        return input.nextLine();

    }// end of getFamilyMemberName method

    /**
     * Prompts for and validates teeth for a given row
     * @param name name of designated family member
     * @param rowType either upper or lower row
     * @return character array representing teeth row
     */
    private static char[] getToothRow(String name, String rowType) {
        char[] row;
        System.out.print("Please enter the " + rowType + " for " + name + "       : ");
        while (true) {
            String teethString = input.nextLine().toUpperCase();
            if (!teethString.matches("[IBM]+")) {
                System.out.print("Invalid teeth types, try again              : ");
                continue;
            }
            if (teethString.length() > MAX_TEETH) {
                System.out.print("Too many teeth, try again                   : ");
                continue;
            }
            row = teethString.toCharArray();
            break;
        }// end of while loop

        return row;

    }// end of getToothRow method

    /**
     * Displays menu of navigation
     * @return chosen option with uppercase character
     */
    private static char getMenuOption() {
        char option;
        System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it          : ");
        while (true) {
            option = Character.toUpperCase(input.next().charAt(0));
            input.nextLine(); // consume newline
            if (option == 'P' || option == 'E' || option == 'R' || option == 'X') break;
            System.out.print("Invalid menu option, try again              : ");
        }// end of while loop

        return option;

    }// end of getMenuOption method

    // Functional Methods

    /**
     * Prints entire family's tooth records, for both upper and lower
     * @param names array of family member names
     * @param teeth array of all tooth types
     */
    private static void printFamilyRecords(String[] names, char[][][] teeth) {
        System.out.println();
        for (int personIndex = 0; personIndex < names.length; personIndex++) {
            System.out.println(names[personIndex]);
            System.out.print("  Uppers: ");
            printTeethRow(teeth[personIndex][0]);
            System.out.print("  Lowers: ");
            printTeethRow(teeth[personIndex][1]);
        }// end of for loop

    }// end of printFamilyRecords method

    /**
     * Prints row of teeth with labels of number and type
     * @param row array of tooth characters
     */
    private static void printTeethRow(char[] row) {
        for (int i = 0; i < row.length; i++) {
            System.out.print(" " + (i + 1) + ":" + row[i]);
        }

        System.out.println();

    }// end of printTeethRow method

    /**
     * Extracts data on a given tooth of a given family member
     * Validates that the tooth is not missing
     * @param names array of family member names
     * @param teeth array of all tooth data
     */
    private static void extractTooth(String[] names, char[][][] teeth) {
        int personIndex = getPersonIndex(names);
        int layerIndex = getLayerIndex();
        int toothCount = teeth[personIndex][layerIndex].length;

        System.out.print("Which tooth number                          : ");
        int toothNumber;
        while (true) {
            toothNumber = input.nextInt();
            input.nextLine(); // consume newline

            if (toothNumber < 1 || toothNumber > toothCount) {
                System.out.print("Invalid tooth number, try again             : ");
                continue;
            }

            if (teeth[personIndex][layerIndex][toothNumber - 1] == MISSING) {
                System.out.print("Missing tooth, try again                    : ");
                continue;
            }

            teeth[personIndex][layerIndex][toothNumber - 1] = MISSING;
            break;
        }// end of while loop

    }// end of extractTooth method

    /**
     * Prompts user to select a valid family member
     * @param names array of family member names
     * @return index of given family member
     */
    private static int getPersonIndex(String[] names) {
        System.out.print("Which family member                         : ");
        while (true) {
            String name = input.nextLine().trim();
            for (int i = 0; i < names.length; i++) {
                if (names[i].equalsIgnoreCase(name)) return i;
            }
            System.out.print("Invalid family member, try again            : ");
        }// end of while loop

    }// end of getPersonIndex method

    /**
     * Prompts user to select upper or lower layer
     * @return 0 for upper layer, 1 for lower
     */
    private static int getLayerIndex() {
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        while (true) {
            char layer = Character.toUpperCase(input.next().charAt(0));
            input.nextLine();
            if (layer == 'U') return 0;
            if (layer == 'L') return 1;
            System.out.print("Invalid layer, try again                    : ");
        }// end of while loop

    }// end of getLayerIndex method

    /**
     * Calculate and display root canal indices
     * @param names array of family member names
     * @param teeth array of all tooth data
     */
    private static void reportRootCanals(String[] names, char[][][] teeth) {
        int countI = 0, countB = 0, countM = 0;
        for (int person = 0; person < names.length; person++) {
            for (int row = 0; row < 2; row++) {
                for (char tooth : teeth[person][row]) {
                    if (tooth == INCISOR) countI++;
                    else if (tooth == BICUSPID) countB++;
                    else if (tooth == MISSING) countM++;
                }
            }
        }// end of total for loop

        double discriminant = countB * countB + 4.0 * countI * countM;
        double root1 = (-countB + Math.sqrt(discriminant)) / (2.0 * countI);
        double root2 = (-countB - Math.sqrt(discriminant)) / (2.0 * countI);

        System.out.printf("One root canal at     %.2f%n", root1);
        System.out.printf("Another root canal at %.2f%n", root2);

    }// end of reportRootCanals method

}// end of DentalRecords class