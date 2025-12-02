import java.util.Scanner;
public class Florida_Dental_Association {


    /** The @code Florida Dental Association stimulates a system for storing,
     * and managing tooth records for a family. Allowing the user to print tooth
     * information, extract tooth position information, and locate a tooth.
     */

    private static final Scanner keyboard = new Scanner(System.in);

    //STATIC VARIABLES:
    private static final int MAX_NUM_TEETH = 8;


        public static void main(String[] args) {

            // Variables initialization
            int numberOfFamilyMembers;
            int index;
            char choice;
            String[] namesOfFamilyMembers;
            char[][][] teeth;
            String upperTeeth;
            String userChoice;
            String lowerTeeth;

            System.out.println("Welcome to the Floridian Tooth Records");
            System.out.println("________________________________________");

            System.out.print("Please enter number of people in the family : ");
            numberOfFamilyMembers = keyboard.nextInt();

            while ((numberOfFamilyMembers < 0) || (numberOfFamilyMembers > 6)) {
                System.out.print("Invalid number of people, try again         : ");
                numberOfFamilyMembers = keyboard.nextInt();
            }// end of loop

            namesOfFamilyMembers = new String[numberOfFamilyMembers];

            teeth = new char[numberOfFamilyMembers][2][];

            for (index = 0; index < numberOfFamilyMembers; index++) {
                System.out.print("Please enter the name for family member " + (index + 1) + "   : ");
                namesOfFamilyMembers[index] = keyboard.next();

                //future method
                System.out.print("Please enter the uppers for " + namesOfFamilyMembers[index] + " : ");
                upperTeeth = keyboard.next().toUpperCase();

                while (!isTeethValid(upperTeeth)) {
                    upperTeeth = keyboard.next().toUpperCase();
                }// end of while loop

                teeth[index][0] = upperTeeth.toCharArray();
                //end of future method


                // begin of future lower method
                System.out.print("Please enter the lower for " + namesOfFamilyMembers[index] + " : ");
                lowerTeeth = keyboard.next().toUpperCase();

                while (!isTeethValid(upperTeeth)) {
                    lowerTeeth = keyboard.next().toUpperCase();
                }// end of while loop

                teeth[index][1] = lowerTeeth.toCharArray();
                // end of future lower method

            }// end of for loop

            menu(teeth,namesOfFamilyMembers);




        }// end of main


    /**
     * menu for the system that allows users between priting, extracting, exiting, and finding the location of a root.
     * @param teeth is a 3-D array that contains the upper and lower teeth for each family member.
     * @param namesOfFamilyMembers an array that contains each family member name
     */
        public static void menu (char [][][] teeth, String [] namesOfFamilyMembers ){

            // Variables initialization

            char choice;
            String userChoice;

            // NOW WORKING ON THE MENU
            System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it       : ");
            userChoice = keyboard.next();

            choice = Character.toUpperCase(userChoice.charAt(0));
            while (choice != 'X'){

                if (choice == 'P'){

                    print(teeth,namesOfFamilyMembers);

                } else if (choice == 'E'){

                    extractTooth(namesOfFamilyMembers,teeth);

                } else if (choice == 'R') {

                    rootCanalLocation(teeth);

                }else{
                    System.out.println("Invalid menu option, try again     :");
                }

                System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it          :");
                userChoice = keyboard.next();
                choice = Character.toUpperCase(userChoice.charAt(0));

            }// end of while
            System.out.println("Exiting the Floridian Tooth Records :-)");

        } // end of the menu method


    /**
     * a method to display the upper and lower teeth of each family member.
     * @param givenTooth is a 3-D array that contains the upper and lower teeth for each family member.
     * @param names is an array that contains each family member name
     */
        public static void print (char [][][] givenTooth, String [] names){

            //Initializing Variables
            int currentTeethPosition;
            int index;


            for(index = 0; index < names.length; index++){
                System.out.println(names[index]);

                System.out.print("Upper: ");
                char []currentUpperTeeth = givenTooth[index][0];

                for (currentTeethPosition = 0; currentTeethPosition < currentUpperTeeth.length; currentTeethPosition++){
                    System.out.print( (currentTeethPosition + 1) + ":" + currentUpperTeeth[currentTeethPosition] + "  ");
                }// end of for loop
                System.out.println();


                System.out.print("Lower: ");
                char []currentLowerTeeth = givenTooth[index][1];

                for (currentTeethPosition = 0; currentTeethPosition < currentLowerTeeth.length; currentTeethPosition++){
                    System.out.print( (currentTeethPosition + 1) + ":" + currentLowerTeeth[currentTeethPosition] + "  ");
                }// end of for loop
                
                System.out.println();

            }// end of for loop


        }// end of the print method


    /** This method locates a Tooth Canal Location.
     * By adding the missing teeth, bicuspids, and Incisors. Then
     * applying calculating the quadratic equation to Ix2 + Bx - M.
     * @param mouth is a 3D array representing all family members’ teeth (upper and lower).
     */

        public static void rootCanalLocation (char[][][] mouth){

            //Initializing Variables
            double countForB;
            double countForI;
            double countForM;
            int layer;
            int toothIndex;
            int person;

            // Declaring Variables
            countForB = 0;
            countForI = 0;
            countForM = 0;

            for (person = 0; person < mouth.length; person++) {
                for (layer  = 0; layer < 2; layer++) {
                    for (toothIndex = 0; toothIndex < mouth[person][layer].length; toothIndex++) {

                        char currentValue = mouth[person][layer][toothIndex];

                        if (currentValue == 'B'){
                            countForB = countForB + 1;
                        }else if (currentValue == 'I'){
                            countForI = countForI + 1;
                        }else{
                            countForM = countForM + 1;
                        }
                    }// end of for loop [][][]
                }// end of for loop[][]
            }// end of for []

            double discriminant = Math.sqrt(countForB * countForB - 4 * countForI * -countForM);
            double root = (-countForB + discriminant) / (2 * countForI);
            double anotherRoot = (-countForB - discriminant) / (2 * countForI);
            System.out.printf("One root canal at % .2f\n", root);
            System.out.printf("Another root canal at % .2f\n", anotherRoot);

        }// end of rootCanalLocation

    /**
     *
     * Validates a string representing a set of teeth to ensure it is the correct range.
     * @param inputTeeth the string representing a person's upper or lower teeth
     * @return (true) if the input string is valid, otherwise (false).
     *
     */


        public static boolean isTeethValid (String inputTeeth){
            // Variables Declaration
            int index;

            if (inputTeeth.length() > MAX_NUM_TEETH) {
                System.out.print("Too many teeth, try again                   : ");
                return false;
            }// end of if statement

            for (index = 0; index < inputTeeth.length(); index++) {
                inputTeeth = inputTeeth.toUpperCase();

                if ((inputTeeth.charAt(index) != 'B') && (inputTeeth.charAt(index) != 'I') && (inputTeeth.charAt(index) != 'M')) {
                    System.out.print("Invalid teeth types, try again              :");
                    return false;
                }// end of if

            }//end of for loop

            return true;

        }// end of isTeethValid method


    /**
     *
     * Extracts a specific tooth for a given family member by replacing it as ('M') missing.
     * @param names a array of string that contain each family memeber names
     * @param tooth is a 3-D array that contains upper teeth and lower teeth for each of
     * the family members.
     *
     */


        private static void extractTooth (String [] names, char [][][]tooth){

            // Variables initialization
            String familyMember;
            String layer;
            int familyIndex;
            int toothNumber;
            int nameIndex;
            int toothLayerIndex;
            boolean isValidMember;
            boolean isValidTooth;
            boolean isValidLayer;


            // Declaring Variables
            toothLayerIndex = 0;
            toothNumber = 0;
            familyIndex = 0;
            isValidLayer = false;
            isValidMember = false;
            isValidTooth = false;

            System.out.print("Which family member                         : ");
            familyMember = keyboard.next();

            do {


                for (nameIndex = 0; nameIndex < names.length; nameIndex++) {

                    if (names[nameIndex].equalsIgnoreCase(familyMember)) {
                        isValidMember = true;
                        familyIndex = nameIndex;
                        break;
                    }// end of if statement

                }// end of for loop

                if (isValidMember == false) {

                    System.out.print("Invalid family member, try again     :");
                    familyMember = keyboard.next();

                }//end of if


            } while (isValidMember == false); // end of Do while loop



            System.out.print("Which tooth layer (U)pper or (L)ower        :");
            layer = keyboard.next().toUpperCase();


            do {


                if (layer.equalsIgnoreCase("U")) {

                    toothLayerIndex = 0;
                    isValidLayer = true;

                }else if (layer.equalsIgnoreCase("L")) {

                    toothLayerIndex = 1;
                    isValidLayer = true;

                }else{

                    System.out.print("Invalid layer, try again                    :");
                    layer = keyboard.next().toUpperCase();
                }

            } while (isValidLayer == false); // End of Do while loop


            System.out.print("Which tooth number                          :");
            toothNumber = keyboard.nextInt();

            do {
                if ( (toothNumber >= 0) && (toothNumber < tooth[nameIndex][toothLayerIndex].length) && (tooth[nameIndex][toothLayerIndex][toothNumber] != 'M')) {
                    isValidTooth = true;
                }else{
                    System.out.print("Invalid tooth number, try again             :");
                    toothNumber = keyboard.nextInt();
                }

            } while(isValidTooth == false);

            tooth[nameIndex][toothLayerIndex][toothNumber-1] = 'M';

        }// end of extractTooth method


    } //end of FDA class