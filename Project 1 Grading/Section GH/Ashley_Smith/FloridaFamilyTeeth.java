import java.util.Scanner;

/**
 * The FloridaFamilyTeeth program tracks dental records for a family.
 * It allows the user to input the upper and lower teeth for each family
 * member, print the family's dental records, extract a tooth, and
 * preform a root canal calculation.
 *
 * This program uses:
 * Incisors (the 'I' teeth)
 * Bicuspids (the 'B' teeth)
 * Missing teeth (the 'M' teeth)
 *
 * Menu options include:
 * Print the dental records for the family
 * Extract a tooth
 * Preform a root canal
 * Exit the program
 *
 * The max number of family members that can be entered is 6.
 * The max number of teeth that can be entered is 8 per layer.
 *
 * @author Ashley Howe-Smith
 * @version 1.0
 */

public class FloridaFamilyTeeth {
    private static final Scanner keyboard = new Scanner(System.in);

    private static final int MAX_PEOPLE = 6;
    private static final int MAX_TEETH = 8;

    /**
     * Main method for the program.
     * Allows the user to input names of family members.
     * Allows the user to interact with the main menu.
     * @param args not used
     */
    public static void main(String[] args) {

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");
        System.out.println("Please enter number of people in the family : ");

        int numFamilyMembers = keyboard.nextInt();
        while((numFamilyMembers < 1) || (numFamilyMembers > MAX_PEOPLE)){
            System.out.println("Invalid number of people, try again : ");
            numFamilyMembers = keyboard.nextInt();
        }//end of while loop

        keyboard.nextLine();

        String [] familyNames = new String[numFamilyMembers];
        char [][][] teethRecords = new char[numFamilyMembers][2][MAX_TEETH];
        getTeethInfo(familyNames, teethRecords);

        System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it : ");
        String menuOption = keyboard.nextLine().toUpperCase();

        boolean validMenu = true;
        while(validMenu) {
            if (menuOption.equals("P")) {
                printFamilyTeeth(familyNames, teethRecords);
                System.out.println();
                System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it : ");
                menuOption = keyboard.nextLine().toUpperCase();
            } else if (menuOption.equals("E")) {
                extractTeeth(familyNames, teethRecords);
                System.out.println();
                System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it : ");
                menuOption = keyboard.nextLine().toUpperCase();
            } else if (menuOption.equals("R")) {
                rootCanal(familyNames, teethRecords);
                System.out.println();
                System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it : ");
                menuOption = keyboard.nextLine().toUpperCase();
            } else if (menuOption.equals("X")) {
                System.out.println("Exiting the Floridian Tooth Records :-)");
                validMenu = false;
            } else {
                System.out.println("Invalid menu option. Please try again : ");
                menuOption = keyboard.nextLine().toUpperCase();
            }//end of if else statement
        }//end of while loop

    }//end of main method

    /**
     * Allows the user to input the upper and lower teeth for each family member.
     * Checks that tooth data is the correct length and valid characters
     * @param familyNames array to store the names of the family members
     * @param teethRecords 3D array to store the upper and lower teeth for each person
     */
    public static void getTeethInfo (String [] familyNames, char [][][] teethRecords) {

        int i;
        int j;
        int k;
        int l;
        for (i = 0; i < familyNames.length; i++) {
            System.out.println("Please enter the name for family member " + (i + 1) + " : ");
            familyNames[i] = keyboard.nextLine();

            boolean validInput = false;
            while (!validInput) {
                System.out.println("Please enter the uppers for " + familyNames[i] + " : ");
                String uppers = keyboard.nextLine().toUpperCase();

                boolean validLength = true;
                boolean validCharacter = true;
                if (uppers.length() > MAX_TEETH) {
                    System.out.println("Too many teeth, try again: ");
                    validLength = false;
                }//end of if statement

                if(validLength) {
                    for (j = 0; j < uppers.length(); j++) {
                        char a = uppers.charAt(j);
                        if (a != 'I' && a != 'B' && a != 'M') {
                            validCharacter = false;
                        }//end of if statement
                    }//end of for loop
                    if (!validCharacter) {
                        System.out.println("Invalid teeth types, try again : ");
                    }//end of if statement
                }//end of if statement

                if(validLength && validCharacter) {
                    validInput = true;
                    for (k = 0; k < uppers.length(); k++) {
                        teethRecords[i][0][k] = uppers.charAt(k);
                    }//end of for loop
                    for (l = uppers.length(); l < MAX_TEETH; l++) {
                        teethRecords[i][0][l] = ' ';
                    }//end of for loop
                }//end of if statement

            }//end of while loop

            boolean validLower = false;
            while (!validLower) {
                System.out.println("Please enter the lowers for " + familyNames[i] + " : ");
                String lowers = keyboard.nextLine().toUpperCase();

                boolean validLength = true;
                boolean validCharacter = true;
                if (lowers.length() > MAX_TEETH) {
                    System.out.println("Too many teeth, try again: ");
                    validLength = false;
                }//end of if statement

                if(validLength) {
                    for (j = 0; j < lowers.length(); j++) {
                        char a = lowers.charAt(j);
                        if (a != 'I' && a != 'B' && a != 'M') {
                            validCharacter = false;
                        }//end of if statement
                    }//end of for loop
                    if (!validCharacter) {
                        System.out.println("Invalid teeth types, try again : ");
                    }//end of if statement
                }//end of if statement

                if(validLength && validCharacter) {
                    validLower = true;
                    for (k = 0; k < lowers.length(); k++) {
                        teethRecords[i][1][k] = lowers.charAt(k);
                    }//end of for loop
                    for (l = lowers.length(); l < MAX_TEETH; l++) {
                        teethRecords[i][1][l] = ' ';
                    }//end of for loop
                }//end of if statement

            }//end of while loop

        }//end of for loop
    }//end of getTeethInfo method

    /**
     * Prints the dental records for each family member
     * @param familyNames array to store the names of the family members
     * @param teethRecords 3D array to store the upper and lower teeth for each person
     */
    public static void printFamilyTeeth(String [] familyNames, char [][][] teethRecords){

        int i;
        int j;
        int k;
        for(i = 0; i < familyNames.length; i++){
            System.out.println(familyNames[i]);
            System.out.print("\tUppers :  ");
            for(j = 0; j < MAX_TEETH; j++){
                if (teethRecords[i][0][j] != ' '){
                    System.out.print((j + 1) + ":" + teethRecords[i][0][j] + " ");
                }//end of if statement
            }//end of inner for loop
            System.out.println();
            System.out.print("\tLowers :  ");
            for(k = 0; k < MAX_TEETH; k++){
                if (teethRecords[i][1][k] != ' '){
                    System.out.print((k + 1) + ":" + teethRecords[i][1][k] + " ");
                }//end of if statement
            }//end of seconds inner for loop
            System.out.println();
        }//end of outer for loop

    }//end of printFamilyTeeth method

    /**
     * Allows the user to select a family member to extract a tooth from.
     * The user can select which layer (upper/lower) and the tooth number.
     * Makes sure the selected tooth is valid and updates the tooth to missing ('M')
     * @param familyNames array to store the names of the family members
     * @param teethRecords 3D array to store the upper and lower teeth for each person
     */
    public static void extractTeeth(String [] familyNames, char [][][] teethRecords){

        int person = -1;
        int i;
        while(person == -1) {
            System.out.println("Which family member : ");
            String name = keyboard.nextLine();

            for (i = 0; i < familyNames.length; i++) {
                if (familyNames[i].equals(name)) {
                    person = i;
                }//end of if statement
            }//end of for loop
            if (person == -1) {
                System.out.println("Invalid family member, try again : ");
            }//end of if statement
        }//end of while loop

        int layer = -1;
        while (layer == -1) {
            System.out.print("Which tooth layer (U)pper or (L)ower : ");
            String layerInput = keyboard.nextLine().toUpperCase();
            if (layerInput.equals("U")) {
                layer = 0;
            } else if (layerInput.equals("L")) {
                layer = 1;
            } else {
                System.out.println("Invalid layer, try again : ");
            }//end of if else statement
        }//end of while loop

        int toothNumber = -1;
        boolean validTooth = false;
        while (!validTooth) {
            System.out.print("Which tooth number : ");
            toothNumber = keyboard.nextInt();
            keyboard.nextLine();
            if (toothNumber > 0 && toothNumber <= MAX_TEETH) {
                char currentTooth = teethRecords[person][layer][toothNumber - 1];

                if (currentTooth == ' ') {
                    System.out.println("Invalid tooth number, try again.");
                } else if (currentTooth == 'M') {
                    System.out.println("Missing tooth, try again.");
                } else {
                    teethRecords[person][layer][toothNumber - 1] = 'M';
                    validTooth = true;
                }//end of inside if statement
            } else {
                System.out.println("Invalid tooth number, try again.");
            }//end of if else statement
        }//end of while loop

    }//end of extractTeeth method

    /**
     * Preforms a root canal calculation using the Quadratic formula.
     * Discriminate is B^2 - 4IM
     * The roots are (-B ± sqrt(discriminant)) / (2I)
     * The roots are only calculated if 'I' is not zero and discriminate is greater than zero
     * @param familyNames array to store the names of the family members
     * @param teethRecords 3D array to store the upper and lower teeth for each person
     */
    public static void rootCanal(String [] familyNames, char [][][] teethRecords){

        int countI = 0;
        int countB = 0;
        int countM = 0;
        int i, j, k;
        for (i = 0; i < familyNames.length; i++) {

            for (j = 0; j < 2; j++) {

                for (k = 0; k < MAX_TEETH; k++) {

                    char a = teethRecords[i][j][k];
                    if (a == 'I') {
                        countI++;
                    } else if (a == 'B') {
                        countB++;
                    } else if (a == 'M') {
                        countM++;
                    }//end of if else
                }//end of innermost for loop
            }//end of middle for loop
        }//end of outside for loop

        boolean calculateRoots = true;
        if (countI == 0) {
            System.out.println("Cannot calculate root canals because divisor would be zero.");
            calculateRoots = false;
        }//end of if statement

        double discriminant = (countB * countB) - (4.0 * countI * countM);

        if (discriminant < 0) {
            System.out.println("No real root canals because discriminant < 0.");
            calculateRoots = false;
        }//end of if statement

        if(calculateRoots) {
            double root1 = (-countB + Math.sqrt(discriminant)) / (2.0 * countI);
            double root2 = (-countB - Math.sqrt(discriminant)) / (2.0 * countI);
            System.out.printf("One root canal at %.2f\n", root1);
            System.out.printf("Another root canal at %.2f\n", root2);
        }//end of if statement

    }//end of rootCanal method


}//end of FloridaFamilyTeeth class
