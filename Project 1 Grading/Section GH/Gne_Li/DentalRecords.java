import java.util.Scanner;

/**
 * DentalRecords.java
 * <p>
 * This program records and manages the teeth information for a Florida family.
 * It supports printing teeth, extracting teeth, calculating root canal indices,
 * and exiting the program with a smile.
 * <p>
 * Student: Gen Li
 * Course: CSC120
 */
public class DentalRecords {

    // Claim Constants
    public static final int MAX_PEOPLE = 6;
    public static final int MAX_TEETH = 8;

    /**
     * Main program method
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        // Module 1: Print welcome message and get input from users
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        int numPeople = getNumberOfPeople(keyboard);  // get family size
        String[] familyNames = new String[numPeople];
        char[][][] teethData = new char[numPeople][2][MAX_TEETH];

        // Input names and teeth data for each family member
        for (int i = 0; i < numPeople; i++) {
            familyNames[i] = getPersonName(keyboard, i);
            teethData[i][0] = getTeethInput(keyboard, familyNames[i], "upper");
            teethData[i][1] = getTeethInput(keyboard, familyNames[i], "lower");
        }//end of this for loop

        // Module 2: Menu processing loop using boolean expression and loops
        char menuOption = ' ';
        boolean menuLoop = true;
        while (menuLoop) {
            menuOption = showMenu(keyboard);
            if (menuOption == 'P') {
                printFamilyRecords(familyNames, teethData);
            } else if (menuOption == 'E') {
                extractTooth(keyboard, familyNames, teethData);
            } else if (menuOption == 'R') {
                computeRootCanals(teethData);
            } else if (menuOption == 'X') {
                printExitMessage();
                menuLoop = false;
            }//end of the last else if statement
        }// end of the while loop
    }

    /**
     * Get the number of people in the family
     *
     * @param keyboard Scanner object for input
     * @return number of people (1 to MAX_PEOPLE)
     */
    public static int getNumberOfPeople(Scanner keyboard) {
        int num = 0;
        boolean inputOK = false;
        // Local variables declared at start
        while (!inputOK) {
            System.out.print("Please enter number of people in the family: ");
            num = keyboard.nextInt();
            keyboard.nextLine(); // consume newline
            if (num >= 1 && num <= MAX_PEOPLE) {
                inputOK = true;
            } else {
                System.out.println("Invalid number of people, try again");
            }
        }//end of while loop
        return num;//return the value
    }

    /**
     * Get the name of a family member
     *
     * @param keyboard Scanner object for input
     * @param index    family member index
     * @return family member name
     */
    public static String getPersonName(Scanner keyboard, int index) {
        System.out.print("Please enter the name for family member " + (index + 1) + ": ");
        return keyboard.nextLine();
    }

    /**
     * Get the teeth input for a person
     *
     * @param keyboard Scanner object for input
     * @param name     person name
     * @param layer    "upper" or "lower"
     * @return char array representing teeth
     */
    public static char[] getTeethInput(Scanner keyboard, String name, String layer) {
        char[] teeth = new char[MAX_TEETH];
        boolean inputOK = false;
        int i; // declare variable at start
        char c; // temporary character
        while (!inputOK) {
            System.out.print("Please enter the " + layer + "s for " + name + ": ");
            String input = keyboard.nextLine();
            if (input.length() > MAX_TEETH) {
                System.out.println("Too many teeth, try again");
            } else {
                for (i = 0; i < input.length(); i++) {
                    c = input.charAt(i);
                    if (c != 'I' && c != 'i' && c != 'B' && c != 'b' && c != 'M' && c != 'm') {
                        break;
                    }
                    // Store as uppercase for consistency
                    if (c == 'i' || c == 'I') teeth[i] = 'I';
                    else if (c == 'b' || c == 'B') teeth[i] = 'B';
                    else if (c == 'm' || c == 'M') teeth[i] = 'M';
                }
                if (i == input.length()) {
                    inputOK = true;
                } else {
                    System.out.println("Invalid teeth types, try again");
                }
            }
        }
        return teeth;
    }

    /**
     * Display menu and get user option
     *
     * @param keyboard Scanner object
     * @return char representing menu option
     */
    public static char showMenu(Scanner keyboard) {
        char option = ' ';
        boolean inputOK = false;
        String input;
        char c;
        while (!inputOK) {
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it: ");
            input = keyboard.nextLine();
            if (input.length() == 1) {
                c = input.charAt(0);
                if (c == 'P' || c == 'p') {
                    option = 'P';
                    inputOK = true;
                } else if (c == 'E' || c == 'e') {
                    option = 'E';
                    inputOK = true;
                } else if (c == 'R' || c == 'r') {
                    option = 'R';
                    inputOK = true;
                } else if (c == 'X' || c == 'x') {
                    option = 'X';
                    inputOK = true;
                } else System.out.println("Invalid menu option, try again");
            } else {
                System.out.println("Invalid menu option, try again");
            }
        }
        return option;
    }

    /**
     * Extract a tooth from a person's teeth
     *
     * @param keyboard Scanner object
     * @param names    family member names
     * @param teeth    3D array storing teeth
     */
    public static void extractTooth(Scanner keyboard, String[] names, char[][][] teeth) {
        String name = "";
        int personIndex = -1;
        int layer = -1;
        int toothNum = 0;
        boolean toothOK = false;

        // Find valid family member
        while (personIndex == -1) {
            System.out.print("Which family member: ");
            name = keyboard.nextLine();
            for (int i = 0; i < names.length; i++) {
                if (names[i].equalsIgnoreCase(name)) {
                    personIndex = i;
                    break;
                }
            }//end of for loop
            if (personIndex == -1) System.out.println("Invalid family member, try again");
        }//end of this while loop

        // Select tooth layer
        while (layer == -1) {
            System.out.print("Which tooth layer (U)pper or (L)ower: ");
            String input = keyboard.nextLine();
            if (input.equals("U") || input.equals("u")) layer = 0;
            else if (input.equals("L") || input.equals("l")) layer = 1;
            else System.out.println("Invalid layer, try again");
        }

        // Select tooth number
        while (!toothOK) {
            System.out.print("Which tooth number: ");
            toothNum = keyboard.nextInt();
            keyboard.nextLine(); // consume newline
            if (toothNum >= 1 && toothNum <= MAX_TEETH) {
                if (teeth[personIndex][layer][toothNum - 1] == 'M') {
                    System.out.println("Missing tooth, try again");
                } else {
                    teeth[personIndex][layer][toothNum - 1] = 'M';
                    toothOK = true;
                }
            } else {
                System.out.println("Invalid tooth number, try again");
            }
        }//end of the while loop
    }

    /**
     * Compute root canal indices based on teeth data
     *
     * @param teeth 3D array of teeth
     */
    public static void computeRootCanals(char[][][] teeth) {
        int totalI = 0, totalB = 0, totalM = 0;
        int i, layer, t;
        char c;
        for (i = 0; i < teeth.length; i++) {
            for (layer = 0; layer < 2; layer++) {
                for (t = 0; t < teeth[i][layer].length; t++) {
                    c = teeth[i][layer][t];
                    if (c == 'I') totalI++;
                    else if (c == 'B') totalB++;
                    else if (c == 'M') totalM++;
                }//end of the inner for loop
            }//end of the middle for loop
        }//end of the outer for loop
        double discriminant = totalB * totalB + 4.0 * totalI * totalM;
        double root1 = (-totalB + Math.sqrt(discriminant)) / (2.0 * totalI);
        double root2 = (-totalB - Math.sqrt(discriminant)) / (2.0 * totalI);
        System.out.printf("One root canal at %.2f%n", root1);//give output for the first root canal result
        System.out.printf("Another root canal at %.2f%n", root2);//give output for the second root canal result
    }

    /**
     * Print all family records
     *
     * @param names family member names
     * @param teeth 3D array of teeth
     */
    public static void printFamilyRecords(String[] names, char[][][] teeth) {
        int i, t;
        for (i = 0; i < names.length; i++) {
            System.out.println(names[i]);
            System.out.print("  Uppers: ");
            for (t = 0; t < teeth[i][0].length; t++) {
                if (teeth[i][0][t] != '\u0000') {
                    System.out.print(" " + (t + 1) + ":" + teeth[i][0][t]);
                }
            }
            System.out.println();
            System.out.print("  Lowers: ");
            for (t = 0; t < teeth[i][1].length; t++) {
                if (teeth[i][1][t] != '\u0000') {
                    System.out.print(" " + (t + 1) + ":" + teeth[i][1][t]);
                }
            }//end of for loop
            System.out.println();
        }
    }

    /**
     * Print exit message with a smile
     */
    public static void printExitMessage() {
        System.out.println("\nExiting the Floridian Tooth Records :-)");
    }
}


