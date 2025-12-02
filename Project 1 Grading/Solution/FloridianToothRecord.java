import java.util.Scanner;
//=========================================================================================================
/**
 * Manage and store information about the teeth records of family members in Florida. It allows
 * users to input family member names, their upper and lower teeth records, and provides options to
 * print the records, extract teeth, and calculate root canal indices
 * @author Hashim Shahzad Khan
 */
public class FloridianToothRecord {
    //-----------------------------------------------------------------------------------------------------
    /**
     * Global Scanner object to use keyboard
     */
    private static final Scanner keyboard = new Scanner(System.in);
    //-----------------------------------------------------------------------------------------------------
    /**
     * Maximum number of people in a family can be 6
     */
    private static final int MAXIMUM_NUMBER_OF_PEOPLE = 6;
    //-----------------------------------------------------------------------------------------------------
    /**
     * Maximum number of teeth in a layer, upper or lower, can be 8
     */
    private static final int MAXIMUM_NUMBER_OF_TEETH = 8;
    //-----------------------------------------------------------------------------------------------------
    /**
     * The main method
     * @param args Passed in from the command line
     */
    public static void main(String[] args) {

        String[] namesOfFamilyMembers;
        String[][][] teethDataOfFamilyMembers;

        int familySize;

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        // Get total number of people in the family
        familySize = getNumberOfPeopleInFamily();

        // Initialize arrays for family members' names and their teeth data
        namesOfFamilyMembers = new String[familySize];
        teethDataOfFamilyMembers = new String[familySize][2][];

        // Take input and set details of family members: their name, upper and lower teeth strings
        setFamilyDetails(familySize, namesOfFamilyMembers, teethDataOfFamilyMembers);

        // Handle the menu system: take input option and call relevant method to execute
        handleMenu(namesOfFamilyMembers, teethDataOfFamilyMembers, familySize);

    } // end of the main method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Prompts the user to input the number of people in the family and validates the input
     * @return The number of people in the family
     */
    private static int getNumberOfPeopleInFamily() {

        int numPeople = -1; // Initialize numPeople with invalid value

        System.out.print("Please enter number of people in the family : ");

        // Loop until the input is valid
        while (numPeople < 1 || numPeople > MAXIMUM_NUMBER_OF_PEOPLE) {

            numPeople = keyboard.nextInt();
            keyboard.nextLine();

            if (numPeople < 1 || numPeople > MAXIMUM_NUMBER_OF_PEOPLE) {
                System.out.print("Invalid number of people, try again         : ");
            }
        }

        return numPeople;

    } // end of the getNumberOfPeopleInFamily method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Collects and sets the names and teeth data ,upper and lower, for each family member
     * @param familySize The number of people in the family
     * @param namesOfFamilyMembers The array to store family members' names
     * @param teethDataOfFamilyMembers 3D array to store family members' teeth data
     */
    private static void setFamilyDetails(int familySize, String[] namesOfFamilyMembers,
                                         String[][][] teethDataOfFamilyMembers) {

        int index;

        for (index = 0; index < familySize; index++) {

            // Get and store the family member's name
            System.out.print("Please enter the name for family member " + (index + 1) + "   : ");
            namesOfFamilyMembers[index] = keyboard.nextLine().trim();

            // Get and store the upper teeth data
            System.out.printf("Please enter the uppers for %-14s  : ", namesOfFamilyMembers[index]);
            teethDataOfFamilyMembers[index][0] = getTeethData();

            // Get and store the lower teeth data
            System.out.printf("Please enter the lowers for %-14s  : ", namesOfFamilyMembers[index]);
            teethDataOfFamilyMembers[index][1] = getTeethData();

        }

    } // end of the setFamilyDetails method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Prompts the user to input a string of teeth data, ensuring that the input is valid (i.e., no
     * more than the maximum number of teeth, and only valid characters 'I', 'B', 'M')
     * @return An array of strings representing teeth data
     */
    private static String[] getTeethData() {

        boolean validInput;
        String inputString;
        String[] teeth;
        int index;

        validInput = false;
        teeth = null;

        // Loop until valid teeth data is provided
        while (!validInput) {

            inputString = keyboard.nextLine().trim().toUpperCase();

            if (inputString.length() > MAXIMUM_NUMBER_OF_TEETH) {
                System.out.print("Too many teeth, try again                   : ");

            } else if (!inputString.matches("[IBM]*")) {
                System.out.print("Invalid teeth types, try again              : ");

            } else {
                validInput = true;
                teeth = new String[inputString.length()];
                for (index = 0; index < inputString.length(); index++) {
                    teeth[index] = String.valueOf(inputString.charAt(index));
                }
            }
        }

        return teeth;

    } // end of the getTeethData method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Handles the menu system, allowing the user to print records, extract a tooth, calculate root
     * canal indices, or exit
     * @param namesOfFamilyMembers Array containing family members' names
     * @param teethDataOfFamilyMembers 3D array containing teeth data of family members
     * @param familySize The number of family members
     */
    private static void handleMenu(String[] namesOfFamilyMembers, String[][][] teethDataOfFamilyMembers,
                                   int familySize) {

        boolean exit;
        String userOption;

        exit = false;

        // Loop to handle and validate menu until the user chooses to exit
        while (!exit) {

            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            userOption = keyboard.nextLine().trim().toUpperCase();

            while (!userOption.equals("P") && !userOption.equals("E") && !userOption.equals("R") &&
                    !userOption.equals("X")) {

                System.out.print("Invalid menu option, try again              : ");
                userOption = keyboard.nextLine().trim().toUpperCase();
            }

            // Call appropriate methods based on the user's option
            switch (userOption) {
                case "P":
                    printTeethRecords(namesOfFamilyMembers, teethDataOfFamilyMembers, familySize);
                    break;
                case "E":
                    extractTooth(namesOfFamilyMembers, teethDataOfFamilyMembers);
                    break;
                case "R":
                    calculateRootCanalIndices(teethDataOfFamilyMembers, familySize);
                    break;
                case "X":
                    System.out.println();
                    System.out.println("Exiting the Floridian Tooth Records :-)");
                    exit = true;
                    break;
            }
        }

    } // end of the handleMenu method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Prints the teeth records ,upper and lower, of each family member
     * @param namesOfFamilyMembers Array containing family members' names
     * @param teethDataOfFamilyMembers 3D array containing teeth data of family members
     * @param familySize The number of family members
     */
    private static void printTeethRecords(String[] namesOfFamilyMembers,
                                          String[][][] teethDataOfFamilyMembers, int familySize) {

        int index;

        System.out.println();

        // Print teeth records for each family member
        for (index = 0; index < familySize; index++) {

            System.out.println(namesOfFamilyMembers[index]);
            System.out.print("  Uppers: ");
            printTeeth(teethDataOfFamilyMembers[index][0]);
            System.out.print("  Lowers: ");
            printTeeth(teethDataOfFamilyMembers[index][1]);

        }

    } // end of the printTeethRecords method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Prints the details of the teeth ,either upper or lower, for a family member
     * @param teeth Array containing the teeth data
     */
    private static void printTeeth(String[] teeth) {

        int index;

        // Loop through each tooth and print its position and type
        for (index = 0; index < teeth.length; index++) {
            System.out.print((index + 1) + ":" + teeth[index] + "  ");
        }
        System.out.println();

    } // end of the printTeeth method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Extracts a specific tooth from a family member, marking it as missing ('M')
     * Prompts the user for the family member's name, the tooth layer ,upper or lower,
     * and the tooth number to extract
     * @param namesOfFamilyMembers Array containing family members' names
     * @param teethDataOfFamilyMembers 3D array containing teeth data of family members
     */
    private static void extractTooth(String[] namesOfFamilyMembers,
                                     String[][][] teethDataOfFamilyMembers) {

        String memberName;
        char layer;

        int memberIndex;
        int layerIndex;
        int toothNumber;

        boolean validLayer;
        boolean validTooth;


        // Get the family member's name
        memberName = getValidatedMemberName(namesOfFamilyMembers);
        memberIndex = getMemberIndex(namesOfFamilyMembers, memberName);

        // Ask for the tooth layer
        validLayer = false;
        layerIndex = -1;

        System.out.print("Which tooth layer (U)pper or (L)ower        : ");

        while (!validLayer) {

            layer = keyboard.nextLine().trim().toUpperCase().charAt(0);

            if (layer == 'U') {
                layerIndex = 0;
                validLayer = true;
            } else if (layer == 'L') {
                layerIndex = 1;
                validLayer = true;
            } else {
                System.out.print("Invalid layer, try again                    : ");
            }
        }

        // Ask for the tooth number (validate range and if tooth is not missing)
        validTooth = false;

        System.out.print("Which tooth number                          : ");

        while (!validTooth) {

            toothNumber = keyboard.nextInt();
            keyboard.nextLine();

            if (toothNumber < 1 || toothNumber > teethDataOfFamilyMembers[memberIndex][layerIndex].length) {
                System.out.print("Invalid tooth number, try again             : ");
            } else if (teethDataOfFamilyMembers[memberIndex][layerIndex][toothNumber - 1].equals("M")) {
                System.out.print("Missing tooth, try again                    : ");
            } else {
                teethDataOfFamilyMembers[memberIndex][layerIndex][toothNumber - 1] = "M";
                validTooth = true;
            }
        }

    } // end of the extractTooth method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Prompts the user to input and validate the name of a family member
     * Ensures that the name exists in the list of family members
     * @param namesOfFamilyMembers Array containing family members' names
     * @return The validated name of the family member
     */
    private static String getValidatedMemberName(String[] namesOfFamilyMembers) {

        String memberName;

        boolean valid;

        memberName = "";
        valid = false;

        // Prompt user for the family member's name and then validate it
        System.out.print("Which family member                         : ");

        while (!valid) {

            memberName = keyboard.nextLine().trim();

            if (getMemberIndex(namesOfFamilyMembers, memberName) != -1) {
                valid = true;
            } else {
                System.out.print("Invalid family member, try again            : ");
            }
        }

        return memberName;

    } // end of the getValidatedMemberName method;

    //-----------------------------------------------------------------------------------------------------
    /**
     * Returns the index of a family member in the array, given the member's name
     * @param namesOfFamilyMembers Array containing family members' names
     * @param name The name of the family member
     * @return The index of the family member, or -1 if the name is not found
     */
    private static int getMemberIndex(String[] namesOfFamilyMembers, String name) {

        int index;

        for (index = 0; index < namesOfFamilyMembers.length; index++) {
            if (namesOfFamilyMembers[index].equalsIgnoreCase(name)) {
                return index;
            }
        }

        return -1; // Not found

    } // end of the getMemberIndex method

    //-----------------------------------------------------------------------------------------------------
    /**
     * Calculates and prints the root canal indices for the family, based on the teeth data
     * Uses the quadratic equation: Ix^2 + Bx - M = 0, where I = Incisors, B = Bicuspids, M = Missing teeth
     * @param teethDataOfFamilyMembers 3D array containing teeth data of family members
     * @param familySize The number of family members
     */
    private static void calculateRootCanalIndices(String[][][] teethDataOfFamilyMembers, int familySize) {

        int incisors;
        int bicuspids;
        int missing;

        double discriminant;
        double root1;
        double root2;

        incisors = 0;
        bicuspids = 0;
        missing = 0;

        // Count the number of Incisors (I), Bicuspids (B), and Missing teeth (M)
        for (int i = 0; i < familySize; i++) {

            for (int j = 0; j < 2; j++) { // 0 = upper, 1 = lower

                for (String tooth : teethDataOfFamilyMembers[i][j]) {

                    switch (tooth) {
                        case "I":
                            incisors++;
                            break;
                        case "B":
                            bicuspids++;
                            break;
                        case "M":
                            missing++;
                            break;
                    }
                }
            }
        }

        // Solve Ix^2 + Bx - M = 0 using the quadratic formula
        discriminant = Math.sqrt(bicuspids * bicuspids + 4 * incisors * missing);
        root1 = (-bicuspids + discriminant) / (2 * incisors);
        root2 = (-bicuspids - discriminant) / (2 * incisors);

        System.out.printf("One root canal at %.2f%n", root1);
        System.out.printf("Another root canal at %.2f%n", root2);

    } // end of the calculateRootCanalIndices method


} // end of the FloridianToothRecord class
