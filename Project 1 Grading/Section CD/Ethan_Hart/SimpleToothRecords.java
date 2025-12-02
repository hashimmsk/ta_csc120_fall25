import java.util.Scanner;

public class SimpleToothRecords {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("---");


        int familySize = 0;
        while (familySize < 1 || familySize > 6) {
            System.out.print("Please enter number of people in the family: ");
            familySize = input.nextInt();
            input.nextLine(); // clear the input buffer
        }


        String[] names = new String[familySize];
        char[][][] teeth = new char[familySize][2][8]; // [person][upper/lower][tooth]

        // Get data for each family member
        for (int i = 0; i < familySize; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + "   : ");
            names[i] = input.nextLine();

            // Get upper teeth
            String upperTeeth = "";
            while (true) {
                System.out.print("Please enter the uppers for " + names[i] + " : ");
                upperTeeth = input.nextLine().toUpperCase();
                if (isValidTeeth(upperTeeth)) {
                    break;
                }
            }

            // Get lower teeth
            String lowerTeeth = "";
            while (true) {
                System.out.print("Please enter the lowers for " + names[i] + " : ");
                lowerTeeth = input.nextLine().toUpperCase();
                if (isValidTeeth(lowerTeeth)) {
                    break;
                }
            }

            // Store teeth data
            storeTeeth(teeth, i, 0, upperTeeth); // 0 for upper
            storeTeeth(teeth, i, 1, lowerTeeth); // 1 for lower
        }

        // Main menu loop
        while (true) {
            System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it: ");
            String choice = input.nextLine().toUpperCase();

            if (choice.equals("P")) {
                printRecords(names, teeth, familySize);
            } else if (choice.equals("E")) {
                extractTooth(names, teeth, familySize, input);
            } else if (choice.equals("R")) {
                calculateRoots(teeth, familySize);
            } else if (choice.equals("X")) {
                break;
            } else {
                System.out.print("Invalid menu option, try again: ");
            }
        }

        System.out.println("\nExiting the Floridian Tooth Records: ");
        input.close();
    } // end of the main method


    public static boolean isValidTeeth(String teeth) {
        if (teeth.length() > 8) {
            System.out.print("Too many teeth, try again: ");
            return false;
        }

        for (int i = 0; i < teeth.length(); i++) {
            char c = teeth.charAt(i);
            if (c != 'I' && c != 'B' && c != 'M') {
                System.out.print("Invalid teeth types, try again: ");
                return false;
            }
        }

        return true;
    } // end of isValidTeeth


    public static void storeTeeth(char[][][] teeth, int person, int row, String toothString) {
        for (int i = 0; i < 8; i++) {
            teeth[person][row][i] = ' '; // Initialize as empty
        }

        for (int i = 0; i < toothString.length(); i++) {
            teeth[person][row][i] = toothString.charAt(i);
        }
    } // end of storeTeeth

    // Print all family records
    public static void printRecords(String[] names, char[][][] teeth, int familySize) {
        for (int person = 0; person < familySize; person++) {
            System.out.println(names[person]);
            printToothRow("Uppers", teeth, person, 0);
            printToothRow("Lowers", teeth, person, 1);
        }
    }


    public static void printToothRow(String rowName, char[][][] teeth, int person, int row) {
        System.out.print("  " + rowName + ": ");
        for (int tooth = 0; tooth < 8; tooth++) {
            if (teeth[person][row][tooth] != ' ') {
                System.out.print((tooth + 1) + ":" + teeth[person][row][tooth] + " ");
            }
        }
        System.out.println();
    } // end of printToothROw


    public static void extractTooth(String[] names, char[][][] teeth, int familySize, Scanner input) {
        // Find family member
        int personIndex = -1;
        while (personIndex == -1) {
            System.out.print("Which family member: ");
            String name = input.nextLine();
            for (int i = 0; i < familySize; i++) {
                if (names[i].equalsIgnoreCase(name)) {
                    personIndex = i;
                    break;
                }
            }
            if (personIndex == -1) {
                System.out.print("Invalid family member, try again: ");
            }
        }

        // Get upper or lower
        int row = -1;
        while (row == -1) {
            System.out.print("Which tooth layer (U)pper or (L)ower: ");
            String layer = input.nextLine().toUpperCase();
            if (layer.equals("U")) {
                row = 0;
            } else if (layer.equals("L")) {
                row = 1;
            } else {
                System.out.print("Invalid layer, try again: ");
            }
        }

        // Get tooth number
        int toothNum = 0;
        while (toothNum < 1 || toothNum > 8) {
            System.out.print("Which tooth number: ");
            toothNum = input.nextInt();
            input.nextLine(); // clear buffer
            if (toothNum < 1 || toothNum > 8) {
                System.out.print("Invalid tooth number, try again: ");
            }
        }

        // Check if tooth exists and extract it
        if (teeth[personIndex][row][toothNum - 1] == ' ' || teeth[personIndex][row][toothNum - 1] == 'M') {
            System.out.println("Missing tooth, try again");
        } else {
            teeth[personIndex][row][toothNum - 1] = 'M';
            System.out.println("Tooth extracted!");
        }
    } // end of the extractTooth method


    public static void calculateRoots(char[][][] teeth, int familySize) {
        int I = 0; // Incisors count
        int B = 0; // Bicuspids count
        int M = 0; // Missing teeth count

        // Count all teeth
        for (int person = 0; person < familySize; person++) {
            for (int row = 0; row < 2; row++) {
                for (int tooth = 0; tooth < 8; tooth++) {
                    char t = teeth[person][row][tooth];
                    if (t == 'I') I++;
                    else if (t == 'B') B++;
                    else if (t == 'M') M++;
                }
            }
        }

        // Calculate roots for equation: Ix² + Bx - M = 0
        double a = I;
        double b = B;
        double c = -M;

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            System.out.println("No real roots exist");
        } else {
            double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);
            System.out.printf("One root canal at     %.2f\n", root1);
            System.out.printf("Another root canal at %.2f\n", root2);
        }
    } // end of the calculateRoots method


} // end of SimpleToothRecords
