import java.util.Scanner;
//=================================================================================================
/**
 * Perform certain tasks related to teeth record of a florida family
 * @ Zecheng (Sam) Li
 */
public class FloridaFamily {
//-------------------------------------------------------------------------------------------------
    /**
     * Global Scanner object to use keyboard
     */
    private static final Scanner keyboard = new Scanner(System.in);
//-------------------------------------------------------------------------------------------------
    /**
     * the main method
     * @param args Passed in from the command line
     */
    public static void main(String[] args) {

        // Declare variables and arrays:
        int numberInFamily;
        String [] FamilyMembersNames;
        String [] TeethLayers;
        char [][][] FamilyMembersTeethTypes;

        // Display welcome message:
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        // Take inputs and form the 3D array:
        TeethLayers = new String[] {"Uppers", "Lowers"};
        numberInFamily = getFamilyNumbers();
        FamilyMembersNames = new String[numberInFamily];
        getFamilyMembersNames(FamilyMembersNames);
        FamilyMembersTeethTypes = new char[numberInFamily][TeethLayers.length][];
        getFamilyMembersTeethTypes(FamilyMembersNames, TeethLayers, FamilyMembersTeethTypes);

        // Process menu options and output results:
        MenuOptions(FamilyMembersNames, TeethLayers, FamilyMembersTeethTypes);

    } // end of the main method
//-------------------------------------------------------------------------------------------------
    /**
     * Prompt user to input the number of family members
     * @return the number of family members
     */
    public static int getFamilyNumbers() {

        int numberInFamily;

        System.out.println("Please enter number of people in the family : ");
        numberInFamily = keyboard.nextInt();

        while (numberInFamily < 1 || numberInFamily > 8) {

            System.out.println("Invalid number of people, try again         : ");
            numberInFamily = keyboard.nextInt();

        }
        // end of the while loop

        return numberInFamily;

    } // end of the getFamilyNumbers Method
//-------------------------------------------------------------------------------------------------
    /**
     * Prompt user to input each family member's name
     * @param inputFamilyMembersNames
     */
    public static void getFamilyMembersNames(String [] inputFamilyMembersNames) {

        // Declare variables:
        int name;

        // Fill the string array:
        for (name = 0; name < inputFamilyMembersNames.length; name++) {

            System.out.println("Please enter the name for family member " + (name + 1) + "   : ");
            inputFamilyMembersNames[name] = keyboard.next();

        } // end of the for loop

    } // end of the getFamilyMembersNames method
//-------------------------------------------------------------------------------------------------
    /**
     * Prompt user to input each family member's teeth types and store them in 3D array
     * @param inputFamilyMembersNames
     * @param inputTeethLayers
     * @param inputFamilyMembersTeethTypes
     */
    public static void getFamilyMembersTeethTypes (String[] inputFamilyMembersNames, String[] inputTeethLayers, char[][][] inputFamilyMembersTeethTypes) {

        int name, layer, type;
        String teethTypesString;

        for (name = 0; name < inputFamilyMembersNames.length; name++) {

            for (layer = 0; layer < inputTeethLayers.length; layer++) {

                System.out.println("Please enter the " + inputTeethLayers[layer] + " for " + inputFamilyMembersNames[name] + "       : ");

                teethTypesString = keyboard.next();
                teethTypesString = teethTypesString.toUpperCase();

                while (teethTypesString.length() > 8 || !teethTypesString.matches("[IBM]+")) {

                    if (teethTypesString.length() > 8) {

                        System.out.println("Too many teeth, try again                   : ");
                        teethTypesString = keyboard.next();
                        teethTypesString = teethTypesString.toUpperCase();

                    }
                    else {

                        System.out.println("Invalid teeth types, try again              : ");
                        teethTypesString = keyboard.next();
                        teethTypesString = teethTypesString.toUpperCase();

                    } // end of the if-else statements

                } // end of the while loop

                // Initialize the third dimension of the 3D array:
                inputFamilyMembersTeethTypes[name][layer] = new char[teethTypesString.length()];

                for (type = 0; type < teethTypesString.length(); type++) {

                    inputFamilyMembersTeethTypes[name][layer][type] = teethTypesString.charAt(type);

                } // end of the for loop

            } // end of the first layer nested loop

        } // end of the for loop

    } // end of the getFamilyMembersTeethTypes method
//-------------------------------------------------------------------------------------------------
    /**
     * Prompt user to choose one task to perform and output the result of the performance
     * @param inputFamilyMembersNames
     * @param inputTeethLayers
     * @param inputFamilyMembersTeethTypes
     */
    public static void MenuOptions (String[] inputFamilyMembersNames, String[] inputTeethLayers, char [][][] inputFamilyMembersTeethTypes) {

        String userOption;
        System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it          : ");
        userOption = keyboard.next();
        userOption = userOption.toUpperCase();

        while (!userOption.equals("P") && !userOption.equals("E") && !userOption.equals("R") && !userOption.equals("X")) {

            System.out.println("Invalid menu option, try again              : ");
            userOption = keyboard.next();
            userOption = userOption.toUpperCase();

        } // end of the while loop

        while (!userOption.equals("X")) {

            if (userOption.equals("P")) {

                PrintRecords(inputFamilyMembersNames, inputTeethLayers, inputFamilyMembersTeethTypes);

            }
            else if (userOption.equals("E")) {

                ExtractTooth(inputFamilyMembersNames, inputTeethLayers, inputFamilyMembersTeethTypes);

            }
            else if (userOption.equals("R")) {

                ReportRoots(inputFamilyMembersNames, inputTeethLayers, inputFamilyMembersTeethTypes);

            }

            userOption = keyboard.next();
            userOption = userOption.toUpperCase();

        } // end of the while loop

        if (userOption.equals("X")) {

            ExitProgram();

        } // end of the if-else statements

    } // end of the MenuOptions method
//-------------------------------------------------------------------------------------------------
    /**
     * Print teeth records of the family
     * @param inputFamilyMembersNames
     * @param inputTeethLayers
     * @param inputFamilyMembersTeethTypes
     */
    public static void PrintRecords (String[] inputFamilyMembersNames, String[] inputTeethLayers, char [][][] inputFamilyMembersTeethTypes) {

        int name, layer, type;

        for (name = 0; name < inputFamilyMembersNames.length; name++) {

            System.out.println(inputFamilyMembersNames[name]);

            for (layer = 0; layer < inputTeethLayers.length; layer++) {

                System.out.print("  " + inputTeethLayers[layer] + ":  ");

                for (type = 0; type < inputFamilyMembersTeethTypes[name][layer].length; type++) {

                    if (type < inputFamilyMembersTeethTypes[name][layer].length -1) {

                        System.out.print((type + 1) + ":" + inputFamilyMembersTeethTypes[name][layer][type] + "  ");

                    }
                    else {

                        System.out.println((type + 1) + ":" + inputFamilyMembersTeethTypes[name][layer][type] + "  ");

                    } // end of the if else statement
                } // end of the second nested for loop
            } // end of the first nested for loop
        } // end of the for loop
    } // end of the PrintRecords method
//-------------------------------------------------------------------------------------------------
    /**
     * Extract a certain tooth's type
     * @param inputFamilyMembersNames
     * @param inputTeethLayers
     * @param inputFamilyMembersTeethTypes
     */
    public static void ExtractTooth (String[] inputFamilyMembersNames, String[] inputTeethLayers, char [][][] inputFamilyMembersTeethTypes) {

        //declare variables:
        String userChoiceOfFamilyMember;
        String userChoiceOfTeethLayer;
        int userChoiceOfToothNumber;
        int name, layer;
        int nameIndex, layerIndex, typeIndex;
        boolean foundName;
        boolean foundLayer;

        // prompt a user input of family member:
        System.out.println("Which family member                         : ");
        userChoiceOfFamilyMember = keyboard.next();
        foundName = false;

        do {

            for (name = 0; name < inputFamilyMembersNames.length; name++) {

                if (userChoiceOfFamilyMember.equalsIgnoreCase(inputFamilyMembersNames[name])) {

                    foundName = true;
                    break;

                } // end of the if else statement

            } // end of the for loop

            if (!foundName) {

                System.out.println("Invalid family member, try again            : ");
                userChoiceOfFamilyMember = keyboard.next();

            } // end of the if statement

        } while (!foundName);
        // end of the do while loop

        // prompt a user input of teeth layer:
        System.out.println("Which tooth layer (U)pper or (L)ower        : ");
        userChoiceOfTeethLayer = keyboard.next();
        foundLayer = false;

        do {

            for (layer = 0; layer < inputTeethLayers.length; layer++) {

                if(userChoiceOfTeethLayer.equalsIgnoreCase(String.valueOf(inputTeethLayers[layer].charAt(0)))) {

                    foundLayer = true;
                    break;

                } // end of the if statement

            } // end of the for loop

            if(!foundLayer) {

                System.out.println("Invalid layer, try again                    : ");
                userChoiceOfTeethLayer = keyboard.next();

            } // end of the if statement

        } while(!foundLayer);
        // end of the do while loop

        // get the index of the chosen name:
        nameIndex = 0;

        for (name = 0; name < inputFamilyMembersNames.length; name++) {

            if(inputFamilyMembersNames[name].equalsIgnoreCase(userChoiceOfFamilyMember)) {

                nameIndex += name;

            } // end of the if statement

        } // end of the for loop

        // get the index of the chosen layer:
        layerIndex = 0;

        for (layer = 0; layer < inputTeethLayers.length; layer++) {

            if (Character.toUpperCase(inputTeethLayers[layer].charAt(0)) == Character.toUpperCase(userChoiceOfTeethLayer.charAt(0))) { // fix to

                layerIndex += layer;

            } // end of the if statement

        } // end of the for loop

        // prompt a user input of tooth number:
        System.out.println("Which tooth number                          : ");
        userChoiceOfToothNumber = keyboard.nextInt();

        while (userChoiceOfToothNumber > inputFamilyMembersTeethTypes[nameIndex][layerIndex].length || userChoiceOfToothNumber < 1) {

            System.out.println("Invalid tooth number, try again             : ");
            userChoiceOfToothNumber = keyboard.nextInt();

        } // end of the while loop

        typeIndex = userChoiceOfToothNumber - 1;

        // output the wanted tooth type:
        System.out.println(inputFamilyMembersTeethTypes[nameIndex][layerIndex][typeIndex]);

    } // end of the ExtractTooth method
//-------------------------------------------------------------------------------------------------
    /**
     * Calculate the root canal(s) of the family
     * @param inputFamilyMembersNames
     * @param inputTeethLayers
     * @param inputFamilyMembersTeethTypes
     */
    public static void ReportRoots (String[] inputFamilyMembersNames, String[] inputTeethLayers, char [][][] inputFamilyMembersTeethTypes) {

        // declare variables:
        int numberOfIncisors, numberOfBicuspids, numberOfMissingTeeth;
        int name, layer, type;
        double discriminant;
        double firstRoot;
        double secondRoot;
        double commonRoot;
        double linearRoot;

        // initialize variables:
        numberOfIncisors = 0;
        numberOfBicuspids = 0;
        numberOfMissingTeeth = 0;

        // get of family's number of different types of teeth:
        for (name = 0; name < inputFamilyMembersNames.length; name++) {

            for (layer = 0; layer < inputTeethLayers.length; layer++) {

                for (type = 0; type < inputFamilyMembersTeethTypes[name][layer].length; type++) {

                    if (inputFamilyMembersTeethTypes[name][layer][type] == 'I') {

                        numberOfIncisors++;

                    }
                    else if(inputFamilyMembersTeethTypes[name][layer][type] == 'B') {

                        numberOfBicuspids++;

                    }
                    else if(inputFamilyMembersTeethTypes[name][layer][type] == 'M') {

                        numberOfMissingTeeth++;

                    } // end of the if-else statements

                } // end of the second nested for loop

            } // end of the first nested for loop

        } // end of the for loop

        if (numberOfIncisors == 0) {

            if (numberOfBicuspids == 0) {

                System.out.println("The root does not exist.");

            }
            else {

                linearRoot = (numberOfMissingTeeth * 1.0) / (numberOfBicuspids * 1.0);
                System.out.println("One and only root canal at " + linearRoot);

            } // end of the nested if-else statement
        }
        else {

            // Calculate and analyze the discriminant:
            discriminant = Math.pow(numberOfBicuspids, 2) - 4 * numberOfIncisors * (-numberOfMissingTeeth);

            if (discriminant > 0) {

                firstRoot = (1.0 / (2.0 * numberOfIncisors)) * (-numberOfBicuspids + Math.sqrt(discriminant));
                secondRoot = (1.0 / (2.0 * numberOfIncisors)) * (-numberOfBicuspids - Math.sqrt(discriminant));

                System.out.println("One root canal at     " + firstRoot);
                System.out.println("Another root canal at " + secondRoot);

            }
            else if (discriminant == 0) {

                commonRoot = (1.0 / (2.0 * numberOfIncisors)) * (-numberOfBicuspids);

                System.out.println("One and only root canal at " + commonRoot);

            }
            else {

                System.out.println("Complex Roots");

            } // end of the if-else statements

        } // end of the most outside if-else statement

    } // end of the ReportRoots method
//-------------------------------------------------------------------------------------------------
    /**
     * Exit the program
     */
    public static void ExitProgram () {

        System.out.println("Exiting the Floridian Tooth Records :-)");

    } // end of the ExitProgram method

} // end of the Florida Family class