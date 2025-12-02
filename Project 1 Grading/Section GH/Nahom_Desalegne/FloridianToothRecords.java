import java.util.Scanner;

public class FloridianToothRecords {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("---------------------------------------");

        int familyCount = getValidFamilyCount(input);

        // Data structures
        String[] names = new String[familyCount];
        char[][][] teeth = new char[familyCount][2][8];
        int[] upperCounts = new int[familyCount];
        int[] lowerCounts = new int[familyCount];

        // Get family data
        for (int i = 0; i < familyCount; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + " : ");
            names[i] = input.nextLine();

            String upper = getValidTeethInput(input, "Please enter the uppers for " + names[i] + " : ");
            upperCounts[i] = upper.length();
            for (int j = 0; j < upper.length(); j++)
                teeth[i][0][j] = Character.toUpperCase(upper.charAt(j));

            String lower = getValidTeethInput(input, "Please enter the lowers for " + names[i] + " : ");
            lowerCounts[i] = lower.length();
            for (int j = 0; j < lower.length(); j++)
                teeth[i][1][j] = Character.toUpperCase(lower.charAt(j));
        }

        boolean exit = false;
        while (!exit) {
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it : ");
            String choice = input.nextLine().trim().toUpperCase();

            if (choice.equals("P")) {
                printRecords(names, teeth, upperCounts, lowerCounts);
            } else if (choice.equals("E")) {
                extractTooth(input, names, teeth, upperCounts, lowerCounts);
            } else if (choice.equals("R")) {
                reportRoots(teeth, upperCounts, lowerCounts);
            } else if (choice.equals("X")) {
                System.out.println("\nExiting the Floridian Tooth Records :-)");
                exit = true;
            } else {
                System.out.println("Invalid menu option, try again");
            }
        }

        input.close();
    }

    // Get valid family count (1–6)
    public static int getValidFamilyCount(Scanner input) {
        int num = 0;
        boolean valid = false;
        do {
            System.out.print("Please enter number of people in the family : ");
            if (input.hasNextInt()) {
                num = input.nextInt();
                input.nextLine();
                if (num >= 1 && num <= 6) {
                    valid = true;
                } else {
                    System.out.println("Invalid number of people, try again");
                }
            } else {
                input.next();
                System.out.println("Invalid number of people, try again");
            }
        } while (!valid);
        return num;
    }

    // Get valid teeth string (only I, B, M)
    public static String getValidTeethInput(Scanner input, String message) {
        String s;
        boolean valid;
        do {
            System.out.print(message);
            s = input.nextLine().trim();
            valid = isValidTeeth(s);
            if (!valid) {
                System.out.println("Invalid teeth types, try again");
            }
        } while (!valid);
        return s;
    }

    // Check teeth string
    public static boolean isValidTeeth(String s) {
        if (s.length() == 0 || s.length() > 8)
            return false;
        for (char c : s.toCharArray()) {
            c = Character.toUpperCase(c);
            if (c != 'I' && c != 'B' && c != 'M')
                return false;
        }
        return true;
    }

    // Print all family members' teeth
    public static void printRecords(String[] names, char[][][] teeth, int[] uppers, int[] lowers) {
        for (int i = 0; i < names.length; i++) {
            System.out.println("\n" + names[i]);
            System.out.print("Uppers: ");
            for (int j = 0; j < uppers[i]; j++) {
                System.out.print((j + 1) + ":" + teeth[i][0][j]);
                if (j < uppers[i] - 1)
                    System.out.print(" ");
            }
            System.out.println();
            System.out.print("Lowers: ");
            for (int j = 0; j < lowers[i]; j++) {
                System.out.print((j + 1) + ":" + teeth[i][1][j]);
                if (j < lowers[i] - 1)
                    System.out.print(" ");
            }
            System.out.println();
        }
    }

    // Extract a tooth
    public static void extractTooth(Scanner input, String[] names, char[][][] teeth, int[] uppers, int[] lowers) {
        int personIndex = -1;
        boolean found = false;
        do {
            System.out.print("Which family member : ");
            String name = input.nextLine().trim();
            for (int i = 0; i < names.length; i++) {
                if (names[i].equalsIgnoreCase(name)) {
                    personIndex = i;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Invalid family member, try again");
            }
        } while (!found);

        int layer = -1;
        boolean validLayer = false;
        do {
            System.out.print("Which tooth layer (U)pper or (L)ower : ");
            String layerInput = input.nextLine().trim().toUpperCase();
            if (layerInput.equals("U")) {
                layer = 0;
                validLayer = true;
            } else if (layerInput.equals("L")) {
                layer = 1;
                validLayer = true;
            } else {
                System.out.println("Invalid layer, try again");
            }
        } while (!validLayer);

        int maxTeeth = (layer == 0 ? uppers[personIndex] : lowers[personIndex]);
        boolean done = false;
        do {
            System.out.print("Which tooth number : ");
            if (!input.hasNextInt()) {
                input.next();
                System.out.println("Invalid tooth number, try again");
                continue;
            }
            int toothNum = input.nextInt();
            input.nextLine();

            if (toothNum < 1 || toothNum > maxTeeth) {
                System.out.println("Invalid tooth number, try again");
            } else if (teeth[personIndex][layer][toothNum - 1] == 'M') {
                System.out.println("Missing tooth, try again");
            } else {
                teeth[personIndex][layer][toothNum - 1] = 'M';
                done = true;
            }
        } while (!done);
    }

    // Compute quadratic root canal indices
    public static void reportRoots(char[][][] teeth, int[] uppers, int[] lowers) {
        int countI = 0, countB = 0, countM = 0;
        for (int i = 0; i < teeth.length; i++) {
            for (int j = 0; j < uppers[i]; j++) {
                char t = teeth[i][0][j];
                if (t == 'I')
                    countI++;
                else if (t == 'B')
                    countB++;
                else if (t == 'M')
                    countM++;
            }
            for (int j = 0; j < lowers[i]; j++) {
                char t = teeth[i][1][j];
                if (t == 'I')
                    countI++;
                else if (t == 'B')
                    countB++;
                else if (t == 'M')
                    countM++;
            }
        }

        if (countI == 0) {
            System.out.println("No incisors found. Cannot compute roots.");
            return;
        }

        double disc = countB * countB + 4.0 * countI * countM;
        double root1 = (-countB + Math.sqrt(disc)) / (2.0 * countI);
        double root2 = (-countB - Math.sqrt(disc)) / (2.0 * countI);
        System.out.printf("One root canal at %6.2f%n", root1);
        System.out.printf("Another root canal at %6.2f%n", root2);
    }//end of main method
}// end of FloridianToothRecords class
