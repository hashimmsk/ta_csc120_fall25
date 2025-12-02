import java.util.Scanner;

/**
 * this class contains the program DentalRecords
 * used to record, print, calculate root canal
 * @author Giovani Sanchez Alvarez
 * @version 1
 */

public class DentalRecords {
    //get java scanner object
    private static Scanner keyboard = new Scanner(System.in);

    //establish constants for later calculations
    private static final int TEETH_MAX = 10;
    private static final int TEETH_LOCATIONS_MAX = 2;

    /**
     * Main method -executes program code of DentalRecords, prompts user with the main menu and input
     * @param args
     */

    public static void main(String[] args) {

        //establishing variables at beginning of main method
        int totalAmountFamily;
        //establish arrays
        String[] nameRecord;
        char[][][] teethInformation;
        int[][] amountTeeth;

        char selectMenu;

        System.out.println("Welcome to the Floridian Tooth Records");
        //1st have to call the getAmountFamily method to get
        totalAmountFamily = getAmountFamily();
        //initialize arrays now that the amount of people in the family is known
        amountTeeth = new int[2][totalAmountFamily];
        //initialize teethData
        teethInformation = new char[totalAmountFamily][TEETH_LOCATIONS_MAX][TEETH_MAX];

        //collect the family names now initializing amount of elements and call familyinfo method
        nameRecord = new String[totalAmountFamily];
        familyInfo(totalAmountFamily, nameRecord, teethInformation, amountTeeth);

        //display menu for user to select option
        // print/extract/report/exit :D
        System.out.println("(P)rint, (E)xtract, (R)oot, e(x)it:  ");
        // at index char at 0 to get first letter
        selectMenu = keyboard.next().charAt(0);

        //open different program parts in the menu with switch
        //make sure to be able to account for lower and uppercase values

        while (selectMenu != 'X' && selectMenu != 'x') {
            switch (selectMenu) {
                case 'P':
                case 'p':
                    printRecords(totalAmountFamily, nameRecord, teethInformation, amountTeeth);
                    System.out.println("(P)rint, (E)xtract, (R)oot, e(x)it:  ");

                    break;
                case 'E':
                case 'e':
                    toothExtract(totalAmountFamily, nameRecord, teethInformation, amountTeeth);
                    System.out.println("(P)rint, (E)xtract, (R)oot, e(x)it:  ");

                    break;
                case 'R':
                case 'r':
                    calculateRoot(teethInformation);
                    System.out.println("(P)rint, (E)xtract, (R)oot, e(x)it:  ");

                    break;
                default:
                    System.out.print("Invalid menu item, try again     :");

            }// end of main menu switch
            selectMenu = keyboard.next().charAt(0);

        }// end of menu selection while loop
        System.out.println("Exiting the Floridian Tooth Records :D");


    }//end of main method

    /**
     * get AmountFamily - collects data on family members
     * @param - no parameters used in this method
     */



    public static int getAmountFamily() {
        int amountFamily;

        //this part is used to get the amount of people in the family for array element amount

        System.out.println("Please enter the number of people in the family: ");
        amountFamily = keyboard.nextInt();

        //maintins the amount in a family to a range of 1-6 people
        while (amountFamily <= 0 || amountFamily > 6) {
            System.out.println("invalid number of people in a family, input valid amount");
            //prompts user for new valid input
            amountFamily = keyboard.nextInt();

        }//end of while loop
        return (amountFamily);


    }// end of amount family method

    /**
     * collects data on family's names and tooth data
     * @param totalAmountFamily takes the amount of people in the family
     * @param familyNames names of individual family members
     * @param teethInformation 3d array holding data on the teeth and family members
     * @param amountTeeth
     */

    //use the familyInfo method to gather info about the family familyNames etc...
    public static void familyInfo(int totalAmountFamily, String[] familyNames, char[][][] teethInformation, int[][] amountTeeth) {
        // have to account for possible upper and lowercase string inputs

        int displayNumbers = 0;
        String lowerTeeth;
        String upperTeeth;
        // now prompt the user to input data about the family
        for (int i = 0; i < totalAmountFamily; i++) {
            displayNumbers = i + 1;
            System.out.println("please enter name of family member #" + displayNumbers + ": ");
            familyNames[i] = keyboard.next();

            // now lets collect data for individual family members' teeth by calling the teethinfo method
            upperTeeth = inputValidTeeth(i, familyNames[i], "upper Teeth");
            amountTeeth[0][i] = upperTeeth.length();
            for (int k = 0; k < upperTeeth.length(); k++) {
                teethInformation[i][0][k] = upperTeeth.charAt(k);

            }

            lowerTeeth = inputValidTeeth(i, familyNames[i], "lower Teeth");
            //store info for each family member
            amountTeeth[1][i] = lowerTeeth.length();
            for (int j = 0; j < lowerTeeth.length(); j++) {
                teethInformation[i][1][j] = lowerTeeth.charAt(j);


            }//end of inner for loop
        }//end of the for loop
    }//end of familyInfo method

    //establish teethInfo method to collect data about individual family members' teeth about upper and lower teeth

    /**
     * separate method called in familyInfo used to input teeth data.
     * @param i
     * @param individualName checks for individual family members
     * @param teethLocation used to prompt user for finding input of location of teeth
     * @return data of upper and lower teeth
     */

    public static String inputValidTeeth(int i, String individualName, String teethLocation) {
        String teethUpLow;
        int lengthTeeth = 0;
        boolean validInput = true;

        System.out.printf("Please enter the %s for %-10s: ", teethLocation, individualName);
        teethUpLow = keyboard.next();

        //check for valid input - use a new method to create this function

        validInput = checkTeethAreValid(teethUpLow);

        lengthTeeth = teethUpLow.length();

        //check legths input are less than ten
        while (!validInput || lengthTeeth >= 10) {
            if (!validInput) {

                System.out.println("invalid tooth type, try again: ");

            } else {
                System.out.println("Too many teeth try again: ");

            }
            teethUpLow = keyboard.next();
            lengthTeeth = teethUpLow.length();
            validInput = checkTeethAreValid(teethUpLow);


        }//end of while loop checking for value

        //final return of the method to be used in familyInfo method
        teethUpLow = teethUpLow.toUpperCase();
        return teethUpLow;

    }// end of teethInfo method

    /**
     * checkTeethAreValid ensures that user inputs in inputValidTeeth work for the calculations and real world limits on the teeth in somebody's mouth as well as capital/lowercase characters
     * @param teeth takes teeth type inputs b, i, and m
     * @return
     */



    public static boolean checkTeethAreValid(String teeth) {

        boolean isValid = false;

        // using this method will allow inputs upper and lowercase to be valid
        for (int i = 0; i < teeth.length(); i++) {
            switch (teeth.charAt(i)) {
                case 'C':
                case 'c':
                case 'B':
                case 'b':
                case 'M':
                case 'm':
                    isValid = true;
                    break;

                //default will revert value of isValid to false
                default:
                    isValid = false;
            }

        }//end of for loop
        return isValid;


    }// end of checkTeethInputValid method---------------

    //--------------------------- menu methods below

    /**
     * printRecords holds the actions performed for P on the main menu, prints the tooth number along with its type
     * @param totalAmountFamily
     * @param familyNames
     * @param teethInformation
     * @param amountTeeth
     */

    public static void printRecords(int totalAmountFamily, String[] familyNames, char[][][] teethInformation, int[][] amountTeeth) {
        //iterate through records for different family members
        for (int i = 0; i < totalAmountFamily; i++) {
            System.out.println(familyNames[i]);
            System.out.print("   Uppers:   ");
            for (int toothIndex = 0; toothIndex < amountTeeth[0][i]; toothIndex++) {
                System.out.printf("%3d:%S", toothIndex + 1, teethInformation[i][0][toothIndex]);
            }
            System.out.println();
            System.out.print("   Lowers   :");
            for (int toothIndex = 0; toothIndex < amountTeeth[1][i]; toothIndex++) {
                System.out.printf("%3d:%S", toothIndex + 1, teethInformation[i][1][toothIndex]);
            }
            System.out.println();
        }
    }// end of main for loop

    /**
     * allows for user to select a tooth for a specific family member to extract
     * @param totalAmountFamily
     * @param familyNames
     * @param teethData
     * @param amountTeeth
     */


    public static void toothExtract(int totalAmountFamily, String[] familyNames, char[][][] teethData, int[][] amountTeeth) {
        String individualMember;
        boolean found = false;
        char toothLayer;
        int toothNumber;

        boolean correctUpperLowerTeeth = false;
        int memberID = 0;
        int toothRow = 0;

        System.out.println("which family member: ");
        individualMember = keyboard.next();

        do {
            for (int i = 0; i < totalAmountFamily; i++) {
                if (individualMember.equalsIgnoreCase(familyNames[i])) {
                    found = true;
                    memberID = i;
                }

            }//end of inner for loop
            //catch invalid inputs
            if (!found) {
                System.out.println("invalid family member, try again: ");
                individualMember = keyboard.next();


            }//end of secondary conditional statement
            //retrieving the tooth layer
        } while (!found);
        System.out.println("Which tooth layer (U)pper or (L)ower: ");
        toothLayer = keyboard.next().charAt(0);
        do { //checking if tooth layer is upper or lower
            switch (toothLayer) {
                case 'U':
                case 'u':
                    correctUpperLowerTeeth = true;
                    break;
                case 'L':
                case 'l':
                    correctUpperLowerTeeth = true;
                    toothRow = 1;
                    break;
                default:
                    System.out.print("Invalid layer, try again: ");
                    toothLayer = keyboard.next().charAt(0);
            }
        } while (!correctUpperLowerTeeth);

        //now get tooth number

        System.out.println("which tooth number? :");
        toothNumber = keyboard.nextInt();
        while (toothNumber > amountTeeth[toothRow][memberID] || toothNumber <= 0 || teethData[memberID][toothRow][toothNumber - 1] == 'M') {
            if (toothNumber > amountTeeth[toothRow][memberID] || toothNumber <= 0) {
                System.out.println("Invalid tooth Number, try again: ");


            }//end of inner if statement
            else if (teethData[memberID][toothRow][toothNumber - 1] == 'M') {
                System.out.println("try again, missing tooth");

            }
            toothNumber = keyboard.nextInt();

        }
        teethData[memberID][toothRow][toothNumber - 1] = 'M';

    }//end of toothExtract method

    /**
     * calculateRoot, uses the quadratic form to calculate root canals, also calculates if there are one or two
     *
     * @param teethData
     */

    private static void calculateRoot(char [][][] teethData) {

        int incisorsTeeth = 0;
        int bicuspidsTeeth = 0;
        int missingTeeth = 0;

        for (char[][] person : teethData) {
            for (char[] row : person) {
                for (char tooth : row) {
                    if (tooth == 'i' || tooth == 'I') {
                        incisorsTeeth++;
                    } else if (tooth == 'b' || tooth == 'B') {
                        bicuspidsTeeth++;
                    } else if (tooth == 'm' || tooth == 'M') {
                        missingTeeth++;
                    }
                }
            }
        }

        // Calculate roots of the equation I*x^2 + B*x - M = 0
        double discriminant = Math.pow(bicuspidsTeeth, 2) - 4 * incisorsTeeth * (-missingTeeth);
        if (discriminant < 0) {
            System.out.println("No real roots.");
        } else {
            double root1 = (-bicuspidsTeeth + Math.sqrt(discriminant)) / (2 * incisorsTeeth);
            double root2 = (-bicuspidsTeeth - Math.sqrt(discriminant)) / (2 * incisorsTeeth);
            System.out.printf("One root canal at %.2f%n", root1);
            System.out.printf("Another root canal at %.2f%n", root2);
        }


    }//end of calculateRoot method
}//end of public class-----------


