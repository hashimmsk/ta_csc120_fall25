import java.util.Scanner;
public class Project1 {

    private static Scanner keyboard = new Scanner(System.in);
    private static final int MAX_FAMILY = 6;
    private static final int MAX_TEETH = 8;

    /**
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        String[] membersName = new String[MAX_FAMILY];
        char[][][] membersTeeth = new char[MAX_FAMILY][2][MAX_TEETH];

        int familySize = 0;
        familySize = inputFamilyInfo(membersName, membersTeeth);

        showMenu(membersName, membersTeeth, familySize);


    } // end of the main method

    /**
     *
     * @param membersName an array to store the names of family members
     * @param membersTeeth a 3D array to store the upper and lower teeth for each member
     * @return the total number of family members
     */
    private static int inputFamilyInfo(String[] membersName, char[][][] membersTeeth){
        int familySize;

        System.out.print("Please enter number of people in the family : ");
            do {
                familySize = keyboard.nextInt();
                keyboard.nextLine();

                if ( familySize < 1 || familySize > MAX_FAMILY){
                    System.out.print("Invalid number of people, try again         : ");
                }
            }while (familySize < 1 || familySize > MAX_FAMILY);

            for (int i = 0; i < familySize; i++) {
                System.out.print("Please enter the name for family member " + (i + 1) + "   : ");
                membersName[i] = keyboard.nextLine();

                String userInput;
                boolean validInput;
                System.out.print("Please enter the uppers for " + membersName[i] + "       : ");
                do{
                    validInput = true;
                    userInput = keyboard.nextLine().toUpperCase();

                    if (userInput.length() > 8){
                        System.out.print("Too many teeth, try again                   : ");
                        validInput = false;

                    }
                    else{
                        for (int index = 0; index < userInput.length(); index++){
                            char t = userInput.charAt(index);
                            if (t != 'I' && t != 'M' && t != 'B'){
                                System.out.print("Invalid teeth types, try again              : ");
                                validInput = false;
                                break;
                            }
                        }
                    }

                } while (!validInput);
                membersTeeth[i][0] = new char[userInput.length()];
                for (int j = 0; j < userInput.length(); j++){
                    membersTeeth[i][0][j] = userInput.charAt(j);
                }


                System.out.print("Please enter the lowers for " + membersName[i] + "        : ");
                do {
                    validInput = true;
                    userInput = keyboard.nextLine().toUpperCase();

                    if (userInput.length() > MAX_TEETH){
                        System.out.print("Too many teeth, try again                   : ");
                        validInput = false;
                    } else {
                        for (int index = 0; index < userInput.length(); index++){
                            char t = userInput.charAt(index);
                            if (t != 'I' && t != 'B' && t != 'M'){
                                System.out.print("Invalid teeth types, try again              : ");
                                validInput = false;
                                break;
                            }
                        }
                    }
                } while (!validInput);
                membersTeeth[i][1] = new char[userInput.length()];
                for (int j = 0; j < userInput.length(); j++){
                    membersTeeth[i][1][j] = userInput.charAt(j);
                }
            }// end of the for loop
        return familySize;
    } //end of the inputFamilyInfo method

    /**
     *
     * @param membersName the array of family member names
     * @param membersTeeth the 3D array of teeth information
     * @param familySize the number of family members
     */
    private static void showMenu(String[] membersName, char[][][] membersTeeth, int familySize){
        String userInput;
        char menuChoice;

        do{
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");

            do {
                userInput = keyboard.nextLine();
                menuChoice = Character.toUpperCase(userInput.charAt(0));
                if(menuChoice != 'P' && menuChoice != 'E' && menuChoice != 'R' && menuChoice != 'X'){
                    System.out.print("Invalid menu option, try again              : ");
                }
            } while(menuChoice != 'P' && menuChoice != 'E' && menuChoice != 'R' && menuChoice != 'X');

            if (menuChoice == 'P'){
                for(int i = 0; i < familySize; i++){

                    System.out.println(membersName[i]);
                    System.out.print("  Uppers:  ");
                    for(int index = 0; index < membersTeeth[i][0].length; index++ ) {
                        System.out.print((index + 1) + ":" + membersTeeth[i][0][index] + " ");
                    }
                    System.out.println();

                    System.out.print("  Lowers:  ");
                    for(int index = 0; index < membersTeeth[i][1].length; index++){
                        System.out.print((index + 1)+ ":" + membersTeeth[i][1][index]+ " ");
                    }
                    System.out.println();
                } //end of the for loop

            } else if (menuChoice == 'E'){
                String memberInput;
                int memberIndex = -1;

                System.out.print("which family member                         : ");
                do{
                    memberInput = keyboard.nextLine();
                    memberIndex = -1;
                    for (int m = 0; m < familySize; m++){
                        if (membersName[m].equalsIgnoreCase(memberInput)){
                            memberIndex = m;
                        }
                    }
                    if (memberIndex == -1){
                        System.out.print("Invalid family member, try again            : ");
                    }

                }while (memberIndex == -1);

                int layerIndex = -1;
                System.out.print("Which tooth layer (U)pper or (L)ower        : ");
                do{
                    userInput = keyboard.nextLine();
                    if (userInput.length() == 0){
                        System.out.print("Invalid layer, try again                    : ");
                    }
                    char chooseLayer = Character.toUpperCase(userInput.charAt(0));
                    if (chooseLayer == 'U'){
                        layerIndex = 0;
                    }
                    else if (chooseLayer == 'L'){
                        layerIndex = 1;
                    }
                    else{
                        System.out.print("Invalid layer, try again                    : ");
                    }

                } while(layerIndex == -1);

                int toothIndex = -1;
                int teethMax = membersTeeth[memberIndex][layerIndex].length;;
                System.out.print("Which tooth number                          : ");
                do{
                    userInput = keyboard.nextLine();
                    toothIndex = Integer.valueOf(userInput);

                    if (toothIndex < 1 || toothIndex > teethMax){
                        System.out.print("Invalid tooth number, try again             : ");
                        toothIndex = -1;
                    }
                    else {
                        char chooseTooth = Character.toUpperCase(membersTeeth[memberIndex][layerIndex][toothIndex - 1]);
                        if (chooseTooth == 'M') {
                            System.out.print("Missing tooth, try again                    : ");
                            toothIndex = -1;
                        } else {
                            membersTeeth[memberIndex][layerIndex][toothIndex - 1] = 'M';
                        }
                    }

                } while (toothIndex == -1);
            }
            else if (menuChoice == 'R'){
                int totalI = 0;
                int totalB = 0;
                int totalM = 0;

                for (int i = 0; i < familySize; i++){
                    for (int j = 0; j < 2; j++) {
                        for (int k = 0; k < membersTeeth[i][j].length; k++) {
                            char computeTooth = Character.toUpperCase(membersTeeth[i][j][k]);
                            if (computeTooth == 'I'){
                                totalI++;
                            }
                            else if (computeTooth == 'B'){
                                totalB++;
                            }
                            else if (computeTooth == 'M'){
                                totalM++;
                            }

                        }
                    }
                }
                double a = totalI;
                double b = totalB;
                double c = -totalM;

                double root = b * b - 4 * a * c;
                if (root < 0){
                    System.out.println("No real root canal indices found.");
                }
                else{
                    double rootOne = (-b + Math.sqrt(root)) / (2 * a);
                    double rootTwo = (-b - Math.sqrt(root)) / (2 * a);

                    System.out.printf("One root canal at     %.2f\n", rootOne);
                    System.out.printf("Another root canal at %.2f\n", rootTwo);

                }

            }
            else if (menuChoice == 'X'){
                System.out.println("\nExiting the Floridian Tooth Records :-)");
            }

        }while (menuChoice != 'X');


    } //end of the showMenu method



} // end the Project1 class
