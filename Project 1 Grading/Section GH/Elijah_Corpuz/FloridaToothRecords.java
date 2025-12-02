import java.util.Scanner;

public class FloridaToothRecords {

    //Define constants and keyboard
    private static final Scanner keyboard = new Scanner(System.in);

    private static final int MAX_PEOPLE = 6;

    private static final int MAX_TEETH = 8;

    //Main method to define arrays and call methods
    public static void main(String[] args) {
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        int numPeople = getFamilyCount();
        String[] names = new String[numPeople];
        char[][][] teeth = new char[numPeople][2][];

        getFamilyData(names, teeth);
        showMenu(names, teeth);

        System.out.println("Exiting the Floridian Tooth Records :-)");
    }//end of main method


    //Method to get number of family members
    private static int getFamilyCount() {
        int count;
        System.out.print("Please enter number of people in the family: ");
        count = keyboard.nextInt();
        keyboard.nextLine();

        //Validation loop
        while (count < 1 || count > MAX_PEOPLE) {
            System.out.print("Invalid number of people, try again: ");
            count = keyboard.nextInt();
            keyboard.nextLine();
        }
        return count;
    }//end of getFamilyCount method

    //Method that gets family member name and their teeth
    private static void getFamilyData(String[] names, char[][][] teeth) {
        int index;
        for (index = 0; index < names.length; index++) {
            System.out.print("Please enter the name for family member " + (index + 1) + ": ");
            names[index] = keyboard.nextLine().trim();

            teeth[index][0] = getTeethRow("uppers", names[index]);
            teeth[index][1] = getTeethRow("lowers", names[index]);
        }
    }//end of getFamilyData method

    //getTeethRow method
    private static char[] getTeethRow(String whichRow, String name) {
        String input;
        boolean valid = false;

        System.out.print("Please enter the " + whichRow + " for " + name + ": ");
        input = keyboard.nextLine().trim();

        //Validation loops
        while (!valid) {
            boolean allGood = true;
            int index;

            // Character check
            for (index = 0; index < input.length(); index++) {
                char c = Character.toUpperCase(input.charAt(index));
                if (c != 'I' && c != 'B' && c != 'M') {
                    allGood = false;
                }
            }

            // Length check
            if (input.length() == 0 || input.length() > MAX_TEETH) {
                System.out.print("Too many teeth, try again: ");
                input = keyboard.nextLine().trim();
            } else if (!allGood) {
                System.out.print("Invalid teeth types, try again: ");
                input = keyboard.nextLine().trim();
            } else {
                valid = true;
            }
        }
        return input.toUpperCase().toCharArray();
    }//end of getToothRow method

    //Menu method to access other methods based on character input
    private static void showMenu(String[] names, char[][][] teeth) {
        boolean exit = false;
        String choice;

        System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it: ");
        choice = keyboard.nextLine().trim();

        //Switch statement to access method based on input
        while (!exit) {
            switch (choice.toUpperCase()) {
                case "P":
                    printRecords(names, teeth);
                    break;
                case "E":
                    extractTooth(names, teeth);
                    break;
                case "R":
                    reportRoots(teeth);
                    break;
                case "X":
                    exit = true;
                    break;
                default:
                    System.out.print("Invalid menu option, try again: ");
                    choice = keyboard.nextLine().trim();

            }

            if (!exit) {
                System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it: ");
                choice = keyboard.nextLine().trim();
            }
        }
    }//end of showMenu method

    //Method to print family information
    private static void printRecords(String[] names, char[][][] teeth) {
        int index;
        int index2;
        int index3;
        for (index = 0; index < names.length; index++) {
            System.out.println(names[index]);
            for (index2 = 0; index2 < 2; index2++) {
                if (index2 == 0) {
                    System.out.print("Uppers: ");
                } else {
                    System.out.print("Lowers: ");
                }
                for (index3 = 0; index3 < teeth[index][index2].length; index3++) {
                    System.out.print("  " + (index3 + 1) + ":" + teeth[index][index2][index3]);
                }
                System.out.println();
            }
        }
    }//end of printRecords method

    //Method to extract tooth
    private static void extractTooth(String[] names, char[][][] teeth) {
        String memberName;
        int personIndex = -1;

        // Ask for member name, validate
        System.out.print("Which family member: ");
        memberName = keyboard.nextLine().trim();

        int index;
        for (index = 0; index < names.length; index++) {
            if (names[index].equalsIgnoreCase(memberName)) {
                personIndex = index;
            }
        }

        while (personIndex == -1) {
            System.out.print("Invalid family member, try again: ");
            memberName = keyboard.nextLine().trim();
            for (index = 0; index < names.length; index++) {
                if (names[index].equalsIgnoreCase(memberName)) {
                    personIndex = index;
                }
            }
        }

        // Ask for layer
        System.out.print("Which tooth layer (U)pper or (L)ower: ");
        String layer = keyboard.nextLine().trim();
        int layerIndex = -1;

        if (layer.equalsIgnoreCase("U")) {
            layerIndex = 0;
        } else if (layer.equalsIgnoreCase("L")) {
            layerIndex = 1;
        }

        while (layerIndex == -1) {
            System.out.print("Invalid layer, try again: ");
            layer = keyboard.nextLine().trim();
            if (layer.equalsIgnoreCase("U")) {
                layerIndex = 0;
            } else if (layer.equalsIgnoreCase("L")) {
                layerIndex = 1;
            }
        }

        // Ask for tooth number once, then on error only print the error prompt and read again.
        int toothNum;
        System.out.print("Which tooth number: ");
        toothNum = keyboard.nextInt();
        keyboard.nextLine();

        boolean valid = false;
        while (!valid) {

            // Check if out of range
            if (toothNum < 1 || toothNum > teeth[personIndex][layerIndex].length) {
                System.out.print("Invalid tooth number, try again: ");
                toothNum = keyboard.nextInt();
                keyboard.nextLine();
                // loop continues to re-check without printing the "Which tooth number" prompt again
            } else if (Character.toUpperCase(teeth[personIndex][layerIndex][toothNum - 1]) == 'M') {
                System.out.print("Missing tooth, try again: ");
                toothNum = keyboard.nextInt();
                keyboard.nextLine();
            } else {
                valid = true;
            }
        }

        // Perform extraction (set to 'M')
        teeth[personIndex][layerIndex][toothNum - 1] = 'M';
    } // end of extractTooth

    //Method to print and calculate root canals
    private static void reportRoots(char[][][] teeth) {
        int totalI = 0, totalB = 0, totalM = 0;
        int index;
        int index2;
        int index3;

        for (index = 0; index < teeth.length; index++) {
            for (index2 = 0; index2 < 2; index2++) {
                for (index3 = 0; index3 < teeth[index][index2].length; index3++) {
                    char c = teeth[index][index2][index3];
                    if (c == 'I') totalI++;
                    else if (c == 'B') totalB++;
                    else if (c == 'M') totalM++;
                }
            }
        }

        double a = totalI;
        double b = totalB;
        double c = -totalM;

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            System.out.println("No real root canals!");
        } else {
            double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);
            System.out.printf("One root canal at     %.2f%n", root1);
            System.out.printf("Another root canal at %.2f%n", root2);
        }
    }//end of reportRoots method
}//end of class


