import java.util.Scanner;
/**
 * This program manages dental records for a family.
 * Uses a 3D array for data entry, extraction, and root canal calculations.
 * @author Daniel Guzman
 * @version 1.0
 */
public class DentalRecord {

    static final int MAX_PEOPLE = 6;
    static final int MAX_TEETH = 8;
    private static final Scanner keyboard = new Scanner(System.in);

    //---------------------------------------------------------------------------------------------
    /**
     * Main method that runs the Dental Record program.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("-".repeat(38));

        int peopleCount;
        System.out.printf("%-45s: ","Please enter number of people in the family");
        peopleCount = keyboard.nextInt();
        while (peopleCount > MAX_PEOPLE || peopleCount < 1){
            System.out.printf("%-45s: ","Invalid number of people, try again");
            peopleCount = keyboard.nextInt();
        } // end of while loop to validate peopleCount input

        String[] familyNames = new String[peopleCount]; // initializes array of strings
        Character[][][] teethData = new Character[peopleCount][2][MAX_TEETH]; // initializes 3D array
        for (int i= 0; i < peopleCount; i++){

            System.out.printf("%-45s: ","Please enter the name for family member " + (i+1));
            familyNames[i] = keyboard.next();
            keyboard.nextLine();

            System.out.printf("%-45s: ","Please enter the uppers for " + familyNames[i]);
            String upperTeeth = getValidTeeth(keyboard); // receives valid teeth input
            for (int j = 0; j < upperTeeth.length(); j++) {
                teethData[i][0][j] = upperTeeth.charAt(j);
            } // end of upperTeeth for loop

            System.out.printf("%-45s: ","Please enter the lowers for " + familyNames[i]);
            String lowerTeeth = getValidTeeth(keyboard); // receives valid teeth input
            for (int j = 0; j < lowerTeeth.length(); j++) {
                teethData[i][1][j] = lowerTeeth.charAt(j);
            } // end of lowerTeeth for loop

        } // end of peopleCount for loop

        System.out.println("-".repeat(38)); //prints line for clarity
        System.out.printf("%-45s: ","(P)rint, (E)xtract, (R)oot, e(X)it");

        boolean runMenu = true;
        while (runMenu) {

            String input = keyboard.nextLine().toUpperCase();
            char userChoice = input.charAt(0);

            switch (userChoice) {
                case 'P' -> {
                    printTeethRecord(familyNames, teethData);
                    System.out.printf("%-45s: ","(P)rint, (E)xtract, (R)oot, e(X)it");
                }
                case 'E' -> {
                    extractTooth(familyNames, teethData, keyboard);
                    System.out.printf("%-45s: ","(P)rint, (E)xtract, (R)oot, e(X)it");
                }
                case 'R' -> {
                    calculateIndices(familyNames, teethData);
                    System.out.printf("%-45s: ","(P)rint, (E)xtract, (R)oot, e(X)it");
                }
                case 'X' -> runMenu = false;
                default -> System.out.printf("%-45s: ","Invalid menu option, try again");
            }
        }
        System.out.println();
        System.out.print("Exiting the Floridian Tooth Records :-)");
    } // end of main method

    //---------------------------------------------------------------------------------------------
    /**
     * Gets valid tooth input from user (I, B, or M, and up to 8 characters).
     * @param keyboard Scanner for user input
     * @return valid string of tooth characters
     */
    public static String getValidTeeth(Scanner keyboard) {
        String teethInput;
        boolean valid;

        do {
            teethInput = keyboard.nextLine().toUpperCase();
            valid = true;

            if (teethInput.length() > MAX_TEETH) { // checks user input length
                System.out.printf("%-45s: ","Too many teeth, try again");
                valid = false;
                continue; // go back to start of loop
            } // end of valid length if statement

            for (int i = 0; i < teethInput.length(); i++) { // for loop reads each character input
                char tooth = teethInput.charAt(i);
                if (tooth != 'I' && tooth != 'B' && tooth != 'M') { // checks for required characters
                    valid = false;
                    System.out.printf("%-45s: ","Invalid teeth types, try again");
                    break; // stops checking string for invalid teeth
                }
            } // end of teethInput character validation for loop
        } while (!valid); // end of overall do while validation loop

        return teethInput; // returns final valid input

    } // end of getValidTeeth method

    //---------------------------------------------------------------------------------------------
    /**
     * Prints family member's upper and lower teeth.
     * @param familyNames array of family names
     * @param teethData 3D array of family teeth data
     */
    public static void printTeethRecord(String[] familyNames, Character[][][] teethData){
        System.out.println();

        for (int i = 0; i < familyNames.length; i++ ){ // loops through family names
            System.out.println(familyNames[i]); // prints member name

            System.out.print("  Uppers: ");
            for (int j = 0; j < teethData[i][0].length; j++) {
                if (teethData[i][0][j] == null) // skips null in array when printing
                    continue;
                System.out.print(" " + (j + 1) + ":" + teethData[i][0][j]); // prints numbered upper teeth
            } // end of uppers print loop

            System.out.println();

            System.out.print("  Lowers: ");
            for (int j = 0; j < teethData[i][1].length; j++) {
                if (teethData[i][1][j] == null) // skips null in array when printing
                    continue;
                System.out.print(" " + (j + 1) + ":" + teethData[i][1][j]); // prints numbered lower teeth
            } // end of lowers print loop

            System.out.println();
            System.out.println();

        } // end of print for loop

    } // end of printTeethRecord method

    //---------------------------------------------------------------------------------------------
    /**
     * Removes tooth from array of specific family member.
     * @param familyNames array of family names
     * @param teethData 3D array of family teeth data
     * @param keyboard Scanner for user input
     */
    public static void extractTooth(String[] familyNames, Character[][][] teethData, Scanner keyboard){
        String nameInput;
        char toothLayerInput;
        int toothIndex;
        int row = 0;
        int personIndex = 0;
        boolean valid = false;

        System.out.printf("%-45s: ","Which family member?");
        do { // do while loop to validate correct member name
            nameInput = keyboard.nextLine();
            for (int i = 0; i < familyNames.length; i++) {
                if (familyNames[i].equalsIgnoreCase(nameInput)) { // compares input to names aray
                    personIndex = i;
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.printf("%-45s: ","Invalid family member, try again");
            }
        } while (!valid); // end of family member name validation loop

        System.out.printf("%-45s: ","Which tooth layer, (U)pper or (L)ower?");
        do { // do while loop to validate correct layer input
            toothLayerInput = keyboard.nextLine().toUpperCase().charAt(0);
            if (toothLayerInput == 'U'){
                valid = true;
            } else if (toothLayerInput == 'L'){
                row = 1;
                valid = true;
            } else{
                System.out.printf("%-45s: ","Invalid layer, try again");
                valid = false;
            }
        } while (!valid); // end of layer input validation loop

        System.out.printf("%-45s: ","Which tooth number?");
        int maxTeeth = teethData[personIndex][row].length; // initializes variable for max teeth
        do { // do while loop to validate position
            toothIndex = keyboard.nextInt() - 1;
            keyboard.nextLine();
            valid = true;
            if (toothIndex < 0 || toothIndex >= maxTeeth){
                System.out.printf("%-45s: ","Invalid tooth number, try again");
                valid = false;
            } else if (teethData[personIndex][row][toothIndex] == null){
                System.out.printf("%-45s: ","Missing tooth, try again");
                valid = false;
            } else if (teethData[personIndex][row][toothIndex] == 'M') {
                System.out.printf("%-45s: ","Tooth already (M)issing there, try again");
                valid = false;
            }
        } while (!valid); // end of tooth position validation loop

        System.out.println("Extracted tooth: " + teethData[personIndex][row][toothIndex]);
        teethData[personIndex][row][toothIndex] = 'M' ; // sets tooth in array position to M(issing)

    } // end of extractTooth method

    //---------------------------------------------------------------------------------------------
    /**
     * Calculates and prints family root canal indices using formula.
     * @param familyNames array of family names
     * @param teethData 3D array of family teeth data
     */
    public static void calculateIndices(String[] familyNames, Character[][][] teethData) {
        int I = 0, B = 0, M = 0;

        for (int i = 0; i < familyNames.length; i++) { // loops through each member's teeth

            for (int layer = 0; layer < 2; layer++) { // loops through both layers of teeth

                for (int j = 0; j < teethData[i][layer].length; j++) { // loops through teeth layer
                    Character tooth = teethData[i][layer][j]; // uses tooth variable for each loop

                    if (tooth != null) { // conditions for only existing teeth be used, adds if exists

                        if (tooth == 'I')
                            I++;
                        else if (tooth == 'B')
                            B++;
                        else if (tooth == 'M')
                            M++;
                    }
                }
            }
        } // end of teeth counting loop

        // initializes abc variables as doubles to preform accurate calculations
        double a = I;
        double b = B;
        double c = -M;

        // initializes discriminant for equation
        double discriminant = (b * b) - (4 * a * c);

        // initializes and calculates both roots with equation
        double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        // prints both root canals with two decimal points
        System.out.printf("One root canal at %.2f%n", root1);
        System.out.printf("Another root canal at %.2f%n", root2);

    } // end of rootCanalIndices method

} // end of DentalRecord class