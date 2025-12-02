import java.util.Scanner;

/**
 * The {@code Project1} class allows users to input family members,
 * record their upper and lower teeth types, and perform operations such as
 * printing records, extracting teeth, and computing root canals
 * based on tooth data.
 *
 * @author Oriana
 * @version 1.0
 */
public class Project1 {
    /** Declare constants. Including scanner, max teeth */
    private static final Scanner keyboard = new Scanner(System.in);
    private static final int TEETH_MAX = 8;
    private static final int TEETH_SETS = 2;
    /**
     * The main method manages user input for family members and their teeth data,
     * and provides a menu for the user to:
     *
     * Print all recorded family teeth data (P)
     * Extract a specific tooth (E)
     * Compute root canal results (R)
     * Exit the program (X)
     *
     *
     * @param args
     */

    public static void main(String[] args) {
        //Declare variables and arrays
        int familySize = 0;
        String[] familyNames;
        String[][][] teethData;
        boolean valid = false;
        boolean optionsExit = false;
        String userInput;
        String lower;
        String upper;

        //Welcome message
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");
        System.out.print("Please enter number of people in the family: ");

        //Validate user input
        while (!valid) {
            if (keyboard.hasNextInt()) {
                familySize = keyboard.nextInt();
                if (familySize > 0 && familySize <= 6) {
                    valid = true;
                } else {
                    System.out.print("Invalid number of people, try again: ");
                }
            } else {
                System.out.print("Invalid number of people, try again: ");
                keyboard.next();
            }
        }

        familyNames = new String[familySize];
        keyboard.nextLine();

        //Declare how many elements in the 3D array
        teethData = new String[familySize][TEETH_SETS][TEETH_MAX];


        //Prompt user for family names as well as upper and lower teeth types
        for (int i = 0; i < familySize; i++) {

            System.out.print("Enter the name for family member " + (i + 1) + ": ");
            familyNames[i] = keyboard.nextLine();
            System.out.print("Please enter the uppers for " + familyNames[i] + ": ");
            upper = keyboard.nextLine().trim().toUpperCase();


            while (upper.length() > 8 || !upper.matches("[IBM]+")) {
                System.out.print("Invalid teeth types, try again: ");
                upper = keyboard.nextLine().trim().toUpperCase();

            }

            System.out.print("Please enter the lowers for " + familyNames[i] + ": ");
            lower = keyboard.nextLine().trim().toUpperCase();

            while (lower.length() > 8 || !lower.matches("[IBM]+")) {
                System.out.print("Invalid. Enter LOWER teeth for " + familyNames[i] + " (max 8, I/B/M): ");
                lower = keyboard.nextLine().trim().toUpperCase();
            }

            //Store upper teeth individually
            for (int t = 0; t < upper.length(); t++) {
                teethData[i][0][t] = String.valueOf(upper.charAt(t));
            }

            //Store lower teeth individually
            for (int t = 0; t < lower.length(); t++) {
                teethData[i][1][t] = String.valueOf(lower.charAt(t));
            }

        }

        //Handle options menu input
        System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it: ");
        while (!optionsExit) {
            userInput = keyboard.next().trim().toUpperCase();

            if (userInput.equals("P")) {
                for (int i = 0; i < familySize; i++) {
                    System.out.println("");
                    System.out.println(familyNames[i]);

                    //Print upper teeth
                    System.out.print("  Uppers:");
                    for (int j = 0; j < 8; j++) {
                        if (teethData[i][0][j] != null) {
                            System.out.print("  " + (j + 1) + ":" + teethData[i][0][j]);
                        }
                    }
                    System.out.println();

                    //Print lower teeth
                    System.out.print("  Lowers:");
                    for (int j = 0; j < 8; j++) {
                        if (teethData[i][1][j] != null) {
                            System.out.print("  " + (j + 1) + ":" + teethData[i][1][j] );
                        }
                    }


                }
                System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it: ");
            } else if (userInput.equals("E")) {
                keyboard.nextLine();
                boolean validMember = false;
                int memberIndex = -1;
                String memberName;

                //Ask for family member
                System.out.print("Which family member: ");
                while (!validMember) {
                    memberName = keyboard.nextLine().trim();
                    for (int i = 0; i < familySize; i++) {
                        if (familyNames[i].equalsIgnoreCase(memberName)) {
                            validMember = true;
                            memberIndex = i;
                            break;
                        }
                    }
                    if (!validMember) {
                        System.out.print("Invalid family member, try again: ");
                    }
                }

                //Ask for upper or lower layer
                boolean validLayer = false;
                int layerIndex = -1;
                System.out.print("Which tooth layer (U)pper or (L)ower: ");
                while (!validLayer) {
                    String layer = keyboard.nextLine().trim().toUpperCase();
                    if (layer.equals("U")) {
                        validLayer = true;
                        layerIndex = 0;
                    } else if (layer.equals("L")) {
                        validLayer = true;
                        layerIndex = 1;
                    } else {
                        System.out.print("Invalid layer, try again: ");
                    }
                }

                //Ask for tooth number
                boolean validTooth = false;
                int toothNum = -1;
                System.out.print("Which tooth number: ");
                while (!validTooth) {
                    if (keyboard.hasNextInt()) {
                        toothNum = keyboard.nextInt();
                        keyboard.nextLine(); //clear buffer
                        if (toothNum >= 1 && toothNum <= 8) {
                            validTooth = true;
                        } else {
                            System.out.print("Invalid tooth number, try again: ");
                        }
                    } else {
                        System.out.print("Invalid tooth number, try again: ");
                        keyboard.nextLine();
                    }
                }

                //Validate if tooth exists
                if (teethData[memberIndex][layerIndex][toothNum - 1] == null || teethData[memberIndex][layerIndex][toothNum - 1].equals("M")) {
                    System.out.print("Missing tooth, try again: ");
                    keyboard.nextLine();
                    System.out.print("(P)rint, (E)xtract, (R)oot, e(x)it: ");
                    continue;
                } else {
                    teethData[memberIndex][layerIndex][toothNum - 1] = "M";
                }

                //Return to menu
                System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it: ");

            } else if (userInput.equals("R")) {
                int I = 0;
                int B = 0;
                int M = 0;

                //Count all teeth types across the entire family
                for (int i = 0; i < familySize; i++) {
                    for (int j = 0; j < 2; j++) { // 0 = upper, 1 = lower
                        for (int k = 0; k < 8; k++) {
                            if (teethData[i][j][k] != null) {
                                if (teethData[i][j][k].equals("I")) {
                                    I++;
                                } else if (teethData[i][j][k].equals("B")) {
                                    B++;
                                } else if (teethData[i][j][k].equals("M")) {
                                    M++;
                                }
                            }
                        }
                    }
                }

                //Check for divide-by-zero before solving the quadratic
                if (I == 0) {
                    System.out.println("No root canals can be computed (I = 0).");
                } else {
                    double discriminant = (B * B) + (4.0 * I * M);
                    double root1 = (-B + Math.sqrt(discriminant)) / (2 * I);
                    double root2 = (-B - Math.sqrt(discriminant)) / (2 * I);

                    System.out.printf("One root canal at %.2f%n", root1);
                    System.out.printf("Another root canal at %.2f%n", root2);
                }

                System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it: ");
            } else if (userInput.equals("X")) {
                optionsExit = true;
                System.out.println("Exiting the Floridian Tooth Records :-)");
            } else {
                System.out.print("Invalid menu option, try again: ");
            }
        }//end of options menu input
    }//end of main method
}//end of Project1 class
