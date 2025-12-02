import java.util.Scanner;

public class DentalRecords {

    public static final int MAX_PEOPLE = 6;
    public static final int MAX_TEETH = 8;
    public static final int UPPER = 0;
    public static final int LOWER = 1;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        int numPeople;
        System.out.print("Please enter number of people in the family : ");
        numPeople = input.nextInt();

        while (numPeople < 1 || numPeople > MAX_PEOPLE) {
            System.out.print("Invalid number of people, try again         : ");
            numPeople = input.nextInt();
        }

        input.nextLine();

        String[] names = new String[numPeople];
        char[][][] teeth = new char[numPeople][2][MAX_TEETH];
        int[] upperCounts = new int[numPeople];
        int[] lowerCounts = new int[numPeople];

        //Get data for each family member
        for (int i = 0; i < numPeople; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + "   : ");
            names[i] = input.nextLine();

            // Get uppers
            String uppers = getTeethString(input, names[i], "uppers");
            upperCounts[i] = uppers.length();
            for (int j = 0; j < upperCounts[i]; j++) {
                teeth[i][UPPER][j] = Character.toUpperCase(uppers.charAt(j));
            }

            // Get lowers
            String lowers = getTeethString(input, names[i], "lowers");
            lowerCounts[i] = lowers.length();
            for (int j = 0; j < lowerCounts[i]; j++) {
                teeth[i][LOWER][j] = Character.toUpperCase(lowers.charAt(j));
            }
        }

        //Menu Loop
        char option = ' ';
        while (option != 'X') {
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            option = Character.toUpperCase(input.next().charAt(0));

            if (option == 'P') {
                printRecords(names, teeth, upperCounts, lowerCounts, numPeople);
            } else if (option == 'E') {
                extractTooth(input, names, teeth, upperCounts, lowerCounts, numPeople);
            } else if (option == 'R') {
                computeRoots(teeth, upperCounts, lowerCounts, numPeople);
            } else if (option == 'X') {
                System.out.println("\nExiting the Floridian Tooth Records :-)");
            } else {
                System.out.print("Invalid menu option, try again              : ");
            }
        }

        input.close();
    }

    public static String getTeethString(Scanner input, String name, String layer) {
        String teethInput;
        boolean valid = false;

        do {
            System.out.print("Please enter the " + layer + " for " + name + "       : ");
            teethInput = input.nextLine().trim();

            // Convert to uppercase for checking
            teethInput = teethInput.toUpperCase();

            if (teethInput.length() > MAX_TEETH) {
                System.out.print("Too many teeth, try again                   : ");
            } else {
                valid = true;
                for (int i = 0; i < teethInput.length(); i++) {
                    char c = teethInput.charAt(i);
                    if (c != 'I' && c != 'B' && c != 'M') {
                        System.out.print("Invalid teeth types, try again              : ");
                        valid = false;
                        break;
                    }
                }
            }
        } while (!valid);

        return teethInput;
    }

    //Print all family records
    public static void printRecords(String[] names, char[][][] teeth,
                                    int[] upperCounts, int[] lowerCounts, int numPeople) {

        for (int i = 0; i < numPeople; i++) {
            System.out.println("\n" + names[i]);
            System.out.print("  Uppers: ");
            for (int j = 0; j < upperCounts[i]; j++) {
                System.out.print(" " + (j + 1) + ":" + teeth[i][UPPER][j]);
            }
            System.out.print("\n  Lowers: ");
            for (int j = 0; j < lowerCounts[i]; j++) {
                System.out.print(" " + (j + 1) + ":" + teeth[i][LOWER][j]);
            }
            System.out.println();
        }
    }

    public static void extractTooth(Scanner input, String[] names, char[][][] teeth,
                                    int[] upperCounts, int[] lowerCounts, int numPeople) {

        input.nextLine(); // clear buffer
        String memberName;
        int memberIndex = -1;

        System.out.print("Which family member                         : ");
        memberName = input.nextLine();

        // Find family member
        boolean found = false;
        for (int i = 0; i < numPeople; i++) {
            if (names[i].equalsIgnoreCase(memberName)) {
                memberIndex = i;
                found = true;
            }
        }

        while (!found) {
            System.out.print("Invalid family member, try again            : ");
            memberName = input.nextLine();
            for (int i = 0; i < numPeople; i++) {
                if (names[i].equalsIgnoreCase(memberName)) {
                    memberIndex = i;
                    found = true;
                }
            }
        }

        // Get layer
        char layer;
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        layer = Character.toUpperCase(input.next().charAt(0));
        while (layer != 'U' && layer != 'L') {
            System.out.print("Invalid layer, try again                    : ");
            layer = Character.toUpperCase(input.next().charAt(0));
        }

        int row = (layer == 'U') ? UPPER : LOWER;
        int maxTeeth = (row == UPPER) ? upperCounts[memberIndex] : lowerCounts[memberIndex];

        // Get tooth number
        System.out.print("Which tooth number                          : ");
        int toothNum = input.nextInt();
        while (toothNum < 1 || toothNum > maxTeeth ||
                teeth[memberIndex][row][toothNum - 1] == 'M') {
            if (toothNum < 1 || toothNum > maxTeeth)
                System.out.print("Invalid tooth number, try again             : ");
            else
                System.out.print("Missing tooth, try again                    : ");
            toothNum = input.nextInt();
        }

        // Extract the tooth
        teeth[memberIndex][row][toothNum - 1] = 'M';
    }

    //---- Method to compute and print root canal indices ----//
    public static void computeRoots(char[][][] teeth, int[] upperCounts,
                                    int[] lowerCounts, int numPeople) {

        int Icount = 0;
        int Bcount = 0;
        int Mcount = 0;

        for (int i = 0; i < numPeople; i++) {
            for (int j = 0; j < upperCounts[i]; j++) {
                char t = teeth[i][UPPER][j];
                if (t == 'I') Icount++;
                else if (t == 'B') Bcount++;
                else if (t == 'M') Mcount++;
            }
            for (int j = 0; j < lowerCounts[i]; j++) {
                char t = teeth[i][LOWER][j];
                if (t == 'I') Icount++;
                else if (t == 'B') Bcount++;
                else if (t == 'M') Mcount++;
            }
        }

        double determinant = (Bcount * Bcount) + (4.0 * Icount * Mcount);
        double root1 = (-Bcount + Math.sqrt(determinant)) / (2 * Icount);
        double root2 = (-Bcount - Math.sqrt(determinant)) / (2 * Icount);

        System.out.printf("One root canal at     %.2f%n", root1);
        System.out.printf("Another root canal at %.2f%n", root2);
    }
}
