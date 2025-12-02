import java.util.Scanner;

public class DentalRecord {
    private static final Scanner keyboard = new Scanner(System.in);
    private static final int MAX_FAMILY = 6;
    private static final int MAX_TEETH = 8;

    public static void main(String[] args) {

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");
        String[] names = familyMembers();
        char[][][] teeth = teethOfFamily(names);

        menuLoop(names, teeth);

    }// end of main method

    /**
     *
     * @param familyArray Gets the array of the names of the family members and implents the length
     * @return Will return a 3D array of each Family members upper and lower teeth
     */
    public static char[][][] teethOfFamily(String[] familyArray) {

        boolean valid = false;
        String uppers;
        String lowers;


        char[][][] dentalRecords = new char[familyArray.length][][];
        for (int i = 0; i < familyArray.length; i++) {

            System.out.println("Please enter the uppers for " + familyArray[i]);
            uppers = keyboard.next();
            uppers = uppers.toUpperCase();
            valid = validTeeth(uppers);
            while (!valid) {
                if (uppers.length() > MAX_TEETH) {
                    System.out.println("Too many teeth, try again : ");
                    uppers = keyboard.next();
                    uppers = uppers.toUpperCase();
                    valid = validTeeth(uppers);
                } else {
                    System.out.println("Invalid teeth types, try again  :");
                    uppers = keyboard.next();
                    uppers = uppers.toUpperCase();
                    valid = validTeeth(uppers);
                }
            }
            dentalRecords[i] = new char[2][];
            System.out.println("Please enter the lowers for " + familyArray[i]);
            lowers = keyboard.next();
            lowers = lowers.toUpperCase();
            valid = validTeeth(lowers);
            while (!valid) {
                if (lowers.length() > MAX_TEETH) {
                    System.out.println("Too many teeth, try again : ");
                    lowers = keyboard.next();
                    lowers = lowers.toUpperCase();
                    valid = validTeeth(lowers);
                } else {
                    System.out.println("Invalid teeth types, try again  :");
                    lowers = keyboard.next();
                    lowers = lowers.toUpperCase();
                    valid = validTeeth(lowers);
                }
            }


            dentalRecords[i][0] = new char[uppers.length()];
            dentalRecords[i][1] = new char[lowers.length()];
            for (int j = 0; j < uppers.length(); j++) {
                dentalRecords[i][0][j] = Character.toUpperCase(uppers.charAt(j));
            }
            for (int j = 0; j < lowers.length(); j++) {
                dentalRecords[i][1][j] = Character.toUpperCase(lowers.charAt(j));
            }
        }
        return dentalRecords;

    }// end of teethOfFamily method


    /**
     *
     * @return Will initialize an array of each Family member's name
     */
    public static String[] familyMembers() {
        System.out.println("Please enter the number of family members : ");
        while (!keyboard.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid integer: ");
            keyboard.next();
        }
        int numFamily = keyboard.nextInt();
        while (numFamily > MAX_FAMILY || numFamily <= 0) {
            System.out.println("Invalid number of people, try again  :");
            while (!keyboard.hasNextInt()) {
                System.out.print("Invalid input. Please enter a valid integer: ");
                keyboard.next();
            }
            numFamily = keyboard.nextInt();
        }
        String[] names = new String[numFamily];
        for (int i = 0; i < numFamily; i++) {
            System.out.println("Please enter the name for family member " + (i + 1) + " : ");
            names[i] = keyboard.next();
            names[i] = names[i].replaceAll("\\s+", "");
        }
        return names;
    }

    /**
     *
     * @param teeth gets the input of teeth from the teeth of family method
     * @return make sure its a valid input and Capitalizes all the letters of the string
     */
    public static boolean validTeeth(String teeth) {
        int count = 0;
        for (int i = 0; i < teeth.length(); i++) {
            if (teeth.charAt(i) != 'I' && teeth.charAt(i) != 'B' && teeth.charAt(i) != 'M') {
                count = count + 1;
            }


        }
        if (teeth.length() > MAX_TEETH) {
            count += 1;
        }
        return count <= 0;
    }// end of validTeeth method

    /**
     *
     * @param names gets the name of family members array to use for parameters for other methods
     * @param teeth gets the teeth of family members array to use for parameters for other methods
     */
    private static void menuLoop(String[] names, char[][][] teeth) {

        while (true) {
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            String choice = keyboard.next();
            choice = choice.toUpperCase();
            char letter = choice.charAt(0);

            switch (letter) {
                case 'P':
                    printRecords(names, teeth);
                    break;
                case 'E':
                    extractTooth(names, teeth);
                    break;
                case 'R':
                    reportRoots(names, teeth);
                    break;
                case 'X':
                    System.out.println("Exiting the Floridian Tooth Records :)");
                    return;
                default:
                    System.out.print("Invalid menu option, try again : ");
            }
        }
    } // end of menuLoop method

    /**
     *
     * @param names         gets the name of the family members array to display for each family members row of teeth
     * @param dentalRecords gets the teeth array to display each person's teeth upper and lower rows
     */
    private static void printRecords(String[] names, char[][][] dentalRecords) {
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
            System.out.print("Uppers: ");
            for (int j = 0; j < dentalRecords[i][0].length; j++) {
                System.out.print((j + 1) + ":" + dentalRecords[i][0][j] + " ");
            }
            System.out.println("");
            System.out.print("Lowers: ");
            for (int j = 0; j < dentalRecords[i][1].length; j++) {
                System.out.print((j + 1) + ":" + dentalRecords[i][1][j] + " ");
            }
            System.out.println("");

        }

    } // end of printRecords method

    /**
     *
     * @param names         gets the name's of family members array to identify what part of the 3D array to edit
     * @param dentalRecords gets the 3D teeth array in order to edit and remove the teeth in the array
     */
    public static void extractTooth(String[] names, char[][][] dentalRecords) {
        int toothLayer;
        int index = -1;

        System.out.println("Which family member ");
        String choice = keyboard.next().toUpperCase();
        while (index == -1) {
            for (int i = 0; i < names.length; i++) {
                String familyMemberName = names[i].toUpperCase();
                if (choice.equals(familyMemberName)) {
                    index = i;
                }
            } if (index == -1) {
                    System.out.println("Invalid family member, try again : ");
                    choice = keyboard.next();
            }
        }


        System.out.println("Which tooth layer (U)pper or (L)ower : ");
        String layer = keyboard.next();
        layer = layer.trim().toUpperCase();
        while (!layer.equals("U") && !layer.equals("L")) {
            System.out.println("Invalid layer, try again");
            layer = keyboard.next().trim().toUpperCase();
        }
        if (layer.equals("U")) {
            toothLayer = 0;
        } else {
            toothLayer = 1;
        }
        System.out.println("Which tooth number : ");
        int toothNumber = keyboard.nextInt() - 1;
        while (true) {
            if (toothNumber > dentalRecords[index][toothLayer].length - 1 || toothNumber < 0) {
                System.out.println("Invalid tooth number, try again");
                toothNumber = keyboard.nextInt() - 1;
            } else if ((dentalRecords[index][toothLayer][(toothNumber)] == 'M')) {
                System.out.println("Missing tooth, try again : ");
                toothNumber = keyboard.nextInt() - 1;

            } else {
                break;
            }


        }

        dentalRecords[index][toothLayer][toothNumber] = 'M';


    }// end of extractTooth

    /**
     *
     * @param names         gets the name's of family members array in order to get the correct length for no errors
     * @param dentalRecords gets the 3D teeth array in order to get the numbers to calculate the roots of the family
     */
    private static void reportRoots(String[] names, char[][][] dentalRecords) {
        int I = 0;
        int B = 0;
        int M = 0;
        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < dentalRecords[i][j].length; k++) {
                    switch (dentalRecords[i][j][k]) {
                        case 'I':
                            I++;
                            break;
                        case 'B':
                            B++;
                            break;
                        case 'M':
                            M++;
                            break;
                    }
                }
            }
        }

        double discriminant = Math.sqrt(B * B + 4 * I * M);
        double root1 = (-B + discriminant) / (2 * I);
        double root2 = (-B - discriminant) / (2 * I);

        System.out.printf("One root canal at     %.2f%n", root1);
        System.out.printf("Another root canal at %.2f%n", root2);
    }// end of reportRoots


}// end of DentalRecords class
