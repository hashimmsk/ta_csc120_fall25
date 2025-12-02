import java.util.Scanner;

public class DentalRecords {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Welcome to the Floridian Tooth Records");
        // 1. Input number of family members
        int numPeople;
        while (true) {
            System.out.print("Please enter number of people in the family : ");
            numPeople = keyboard.nextInt();
            keyboard.nextLine();
            if (numPeople >= 1 && numPeople <= 6) break;
            System.out.println("Invalid number of people, try again ");
        }

        String[] names = new String[numPeople];
        char[][][] teeth = new char[numPeople][2][8];

        // 2. Record names + teeth
        for (int i = 0; i < numPeople; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + "   : ");
            names[i] = keyboard.nextLine();

            // Uppers
            while (true) {
                System.out.print("Please enter the uppers for " + names[i] + "       : ");
                String upper = keyboard.nextLine().toUpperCase();

                if (!upper.matches("[IBM]*")) {
                    System.out.println("Invalid teeth types, try again ");
                    continue;
                }
                if (upper.length() > 8 || upper.length() == 0) {
                    System.out.println("Too many teeth, try again");
                    continue;
                }
                for (int j = 0; j < upper.length(); j++) {
                    teeth[i][0][j] = upper.charAt(j);
                }
                for (int j = upper.length(); j < 8; j++) {
                    teeth[i][0][j] = 0;
                }
                break;
            }

            // Lowers
            while (true) {
                System.out.print("Please enter the lowers for " + names[i] + "       : ");
                String lower = keyboard.nextLine().toUpperCase();

                if (!lower.matches("[IBM]*")) {
                    System.out.println("Invalid teeth types, try again ");
                    continue;
                }
                if (lower.length() > 8 || lower.length() == 0) {
                    System.out.println("Too many teeth, try again");
                    continue;
                }
                for (int j = 0; j < lower.length(); j++) {
                    teeth[i][1][j] = lower.charAt(j);
                }
                for (int j = lower.length(); j < 8; j++) {
                    teeth[i][1][j] = 0;
                }
                break;
            }
        }

        // 3. Menu loop (P/E/R/X)
        while (true) {
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            String choice = keyboard.nextLine().toUpperCase();

            switch (choice) {
                case "P":
                    printRecords(names, teeth);
                    break;

                case "E":
                    extractTooth(names, teeth, keyboard);
                    break;

                case "R":
                    rootCanal(teeth);
                    break;

                case "X":
                    System.out.println("\nExiting the Floridian Tooth Records :-)");
                    return;

                default:
                    System.out.println("Invalid menu option, try again ");
            }
        }
    }

    // Print function
    public static void printRecords(String[] names, char[][][] teeth) {
        for (int i = 0; i < names.length; i++) {
            System.out.println("\n" + names[i]);

            System.out.print("  Uppers: ");
            for (int j = 0; j < 8; j++) {
                if (teeth[i][0][j] != 0)
                    System.out.print(" " + (j + 1) + ":" + teeth[i][0][j]);
            }
            System.out.println();

            System.out.print("  Lowers: ");
            for (int j = 0; j < 8; j++) {
                if (teeth[i][1][j] != 0)
                    System.out.print(" " + (j + 1) + ":" + teeth[i][1][j]);
            }
            System.out.println();
        }
    }

    // Extract a tooth
    public static void extractTooth(String[] names, char[][][] teeth, Scanner in) {
        int person = -1;
        while (true) {
            System.out.print("Which family member                         : ");
            String nameInput = in.nextLine();
            for (int i = 0; i < names.length; i++) {
                if (names[i].equalsIgnoreCase(nameInput)) {
                    person = i;
                    break;
                }
            }
            if (person != -1) break;
            System.out.println("Invalid family member, try again ");
        }

        int layer = -1;
        while (true) {
            System.out.print("Which tooth layer (U)pper or (L)ower        : ");
            String layerInput = in.nextLine().toUpperCase();
            if (layerInput.equals("U")) { layer = 0; break; }
            if (layerInput.equals("L")) { layer = 1; break; }
            System.out.println("Invalid layer, try again ");
        }

        int toothNum;
        while (true) {
            System.out.print("Which tooth number                          : ");
            toothNum = in.nextInt();
            in.nextLine();
            if (toothNum >= 1 && toothNum <= 8 && teeth[person][layer][toothNum - 1] != 0) break;
            System.out.println("Invalid tooth number, try again");
        }

        if (teeth[person][layer][toothNum - 1] == 'M') {
            System.out.println("Missing tooth, try again        :");
        } else {
            teeth[person][layer][toothNum - 1] = 'M';
        }
    }

    // Calculate root canal index
    public static void rootCanal(char[][][] teeth) {
        int I = 0, B = 0, M = 0;

        for (char[][] person : teeth) {
            for (char[] row : person) {
                for (char t : row) {
                    if (t == 'I') I++;
                    if (t == 'B') B++;
                    if (t == 'M') M++;
                }
            }
        }

        double a = I, b = B, c = -M;
        double root1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        double root2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);

        System.out.printf("One root canal at     %.2f\n", root1);
        System.out.printf("Another root canal at %.2f\n", root2);
    }
}
