import java.util.Scanner;

/**
 * Tracks tooth records for a family and provides print, extract, and root reports.
 */
public class DentalRecords { // DentalRecords class

    // constants and shared state
    private static final Scanner KEYBOARD = new Scanner(System.in);
    private static final int MAX_PEOPLE = 6;
    private static final int LAYERS = 2; 
    private static final int MAX_TEETH = 8;

    private static int peopleCount;
    private static String[] names;
    private static char[][][] teeth;
    private static int[][] counts;     // actual counts per row

    /**
     * Entry point. Runs setup and the menu loop.
     * @param args not used
     */
    public static void main(String[] args) {

        // intro
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        setupFamily(); // reads family size, names, and teeth

        boolean running = true; // menu loop flag
        while (running) {

            String s;
            char choice;

            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            s = KEYBOARD.nextLine();
            while (s.length() == 0 || "PERXperx".indexOf(s.charAt(0)) == -1) {
                System.out.print("Invalid menu option, try again              : ");
                s = KEYBOARD.nextLine();
            }
            choice = Character.toUpperCase(s.charAt(0)); // normalize

            if (choice == 'P') {
                printRecords();
            } else if (choice == 'E') {
                extractTooth();
            } else if (choice == 'R') {
                rootReport();
            } else if (choice == 'X') {
                System.out.println();
                System.out.println("Exiting the Floridian Tooth Records :-)");
                running = false;
            }
        }
    } // end of main method

    /**
     * Reads family size, names, and both rows of teeth for each person.
     */
    private static void setupFamily() {

        // family size
        System.out.print("Please enter number of people in the family : ");
        peopleCount = KEYBOARD.nextInt();
        KEYBOARD.nextLine();
        while (peopleCount < 1 || peopleCount > MAX_PEOPLE) {
            System.out.print("Invalid number of people, try again         : ");
            peopleCount = KEYBOARD.nextInt();
            KEYBOARD.nextLine();
        }

        // allocate arrays
        names  = new String[peopleCount];
        teeth  = new char[peopleCount][LAYERS][MAX_TEETH];
        counts = new int[peopleCount][LAYERS];

        int p = 0;
        while (p < peopleCount) {
            System.out.print("Please enter the name for family member " + (p + 1) + "   : ");
            names[p] = KEYBOARD.nextLine();

            // read uppers then lowers
            for (int layer = 0; layer < LAYERS; layer++) {
                String layerWord = (layer == 0) ? "uppers" : "lowers";
                String line;

                System.out.print("Please enter the " + layerWord + " for " + names[p] + "       : ");
                line = KEYBOARD.nextLine();

                // validate letters and length
                boolean okType, okLen;
                do {
                    okType = true;
                    if (line == null || line.length() == 0) okType = false;
                    if (okType) {
                        for (int i = 0; i < line.length(); i++) {
                            char c = Character.toUpperCase(line.charAt(i));
                            if (!(c == 'I' || c == 'B' || c == 'M')) {
                                okType = false;
                                break;
                            }
                        }
                    }
                    okLen = line != null && line.length() >= 1 && line.length() <= MAX_TEETH;

                    if (!okType) {
                        System.out.print("Invalid teeth types, try again              : ");
                        line = KEYBOARD.nextLine();
                    } else if (!okLen) {
                        System.out.print("Too many teeth, try again                   : ");
                        line = KEYBOARD.nextLine();
                    }
                } while (!okType || !okLen);

                // store uppercase row
                line = line.toUpperCase();
                int i = 0;
                while (i < line.length()) {
                    teeth[p][layer][i] = line.charAt(i);
                    i++;
                }
                while (i < MAX_TEETH) {
                    teeth[p][layer][i] = '\0';
                    i++;
                }
                counts[p][layer] = line.length(); // save count
            }
            p++;
        }
    } // end of setupFamily

    /**
     * Prints all names with labeled upper and lower teeth.
     */
    private static void printRecords() {

        System.out.println();

        int p = 0;
        while (p < peopleCount) {
            System.out.println(names[p]);

            // uppers
            System.out.print("  Uppers: ");
            System.out.print(" ");
            int t = 0;
            while (t < counts[p][0]) {
                System.out.print((t + 1) + ":" + teeth[p][0][t]);
                if (t < counts[p][0] - 1) {
                    System.out.print("  ");
                }
                t++;
            }
            System.out.println();

            // lowers
            System.out.print("  Lowers: ");
            System.out.print(" ");
            t = 0;
            while (t < counts[p][1]) {
                System.out.print((t + 1) + ":" + teeth[p][1][t]);
                if (t < counts[p][1] - 1) {
                    System.out.print("  ");
                }
                t++;
            }
            System.out.println();

            p++;
        }
    } // end of printRecords

    /**
     * Prompts for person, layer, and tooth number and marks it missing.
     */
    private static void extractTooth() {

        // choose person
        System.out.print("Which family member                         : ");
        String who = KEYBOARD.nextLine();
        int personIndex = -1;
        int i = 0;
        while (i < peopleCount) {
            if (names[i].equalsIgnoreCase(who)) { personIndex = i; break; }
            i++;
        }
        while (personIndex == -1) {
            System.out.print("Invalid family member, try again            : ");
            who = KEYBOARD.nextLine();
            personIndex = -1;
            i = 0;
            while (i < peopleCount) {
                if (names[i].equalsIgnoreCase(who)) { personIndex = i; break; }
                i++;
            }
        }

        // choose layer
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        String s = KEYBOARD.nextLine();
        while (s.length() == 0) {
            System.out.print("Invalid layer, try again                    : ");
            s = KEYBOARD.nextLine();
        }
        char c = Character.toUpperCase(s.charAt(0));
        while (!(c == 'U' || c == 'L')) {
            System.out.print("Invalid layer, try again                    : ");
            s = KEYBOARD.nextLine();
            while (s.length() == 0) {
                System.out.print("Invalid layer, try again                    : ");
                s = KEYBOARD.nextLine();
            }
            c = Character.toUpperCase(s.charAt(0));
        }
        int layer = (c == 'U') ? 0 : 1;

        // choose tooth number
        System.out.print("Which tooth number                          : ");
        s = KEYBOARD.nextLine();
        while (s.length() == 0 || !allDigits(s)) {
            System.out.print("Invalid tooth number, try again             : ");
            s = KEYBOARD.nextLine();
        }
        int toothNum = Integer.parseInt(s);
        while (toothNum < 1 || toothNum > counts[personIndex][layer]) {
            System.out.print("Invalid tooth number, try again             : ");
            s = KEYBOARD.nextLine();
            while (s.length() == 0 || !allDigits(s)) {
                System.out.print("Invalid tooth number, try again             : ");
                s = KEYBOARD.nextLine();
            }
            toothNum = Integer.parseInt(s);
        }

        // ensure not already missing
        boolean missing = teeth[personIndex][layer][toothNum - 1] == 'M';
        while (missing) {
            System.out.print("Missing tooth, try again                    : ");
            String s2 = KEYBOARD.nextLine();
            while (s2.length() == 0 || !allDigits(s2)) {
                System.out.print("Invalid tooth number, try again             : ");
                s2 = KEYBOARD.nextLine();
            }
            toothNum = Integer.parseInt(s2);
            while (toothNum < 1 || toothNum > counts[personIndex][layer]) {
                System.out.print("Invalid tooth number, try again             : ");
                s2 = KEYBOARD.nextLine();
                while (s2.length() == 0 || !allDigits(s2)) {
                    System.out.print("Invalid tooth number, try again             : ");
                    s2 = KEYBOARD.nextLine();
                }
                toothNum = Integer.parseInt(s2);
            }
            missing = teeth[personIndex][layer][toothNum - 1] == 'M';
        }

        teeth[personIndex][layer][toothNum - 1] = 'M'; // mark missing
    } // end of extractTooth

    /**
     * Computes totals and prints the root canal report.
     */
    private static void rootReport() {

        int I = 0, B = 0, M = 0; // totals

        int p = 0;
        while (p < peopleCount) {
            for (int layer = 0; layer < LAYERS; layer++) {
                int t = 0;
                while (t < counts[p][layer]) {
                    char ch = teeth[p][layer][t];
                    if (ch == 'I') I++;
                    else if (ch == 'B') B++;
                    else if (ch == 'M') M++;
                    t++;
                }
            }
            p++;
        }

        double a = I;
        double b = B;
        double c = -M;

        // roots
        double[] roots;
        if (a == 0.0) {
            if (b == 0.0) {
                roots = new double[0];
            } else {
                roots = new double[]{ -c / b };
            }
        } else {
            double disc = b * b - 4.0 * a * c;
            if (disc < 0.0) {
                roots = new double[0];
            } else {
                double sqrt = Math.sqrt(disc);
                double r1 = (-b + sqrt) / (2.0 * a);
                double r2 = (-b - sqrt) / (2.0 * a);
                roots = new double[]{ r1, r2 };
            }
        }

        if (roots.length == 2) {
            System.out.printf("One root canal at %6.2f%n", roots[0]);
            System.out.printf("Another root canal at %6.2f%n", roots[1]);
        } else if (roots.length == 1) {
            System.out.printf("One root canal at %6.2f%n", roots[0]);
            System.out.printf("Another root canal at %6.2f%n", roots[0]);
        } else {
            System.out.println("One root canal at     0.00");
            System.out.println("Another root canal at 0.00");
        }
    } // end of rootReport

    /**
     * Tests if a string is non-empty and all digits.
     * @param s input
     * @return true if all digits
     */
    private static boolean allDigits(String s) {
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (!(c >= '0' && c <= '9')) return false;
            i++;
        }
        return s.length() > 0;
    } // end of allDigits
}
