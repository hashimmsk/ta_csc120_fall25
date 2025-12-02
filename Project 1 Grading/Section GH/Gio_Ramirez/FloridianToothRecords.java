import java.util.*;

public class FloridianToothRecords {

    // Configuration constants (limits & settings)
    private static final int MAX_PEOPLE = 6;
    private static final int MAX_TEETH  = 8;
    private static final int LAYERS     = 2; // 0 = upper, 1 = lower

    // Data (family names and 3-D tooth record array)
    private static String[] names;
    private static char[][][] teeth;
    private static int numPeople;

    private static final Scanner in = new Scanner(System.in);

    // Program entry point (startup banner, input, main loop)
    public static void main(String[] args) {
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        numPeople = getNumPeople();
        names  = new String[numPeople];
        teeth  = new char[numPeople][LAYERS][MAX_TEETH];

        inputFamilyData();

        char choice;
        do {
            displayMenu();
            choice = getMenuChoice();
            processChoice(choice);
        } while (choice != 'X');

        System.out.println();
        System.out.println("Exiting the Floridian Tooth Records :-)");
    }

    // Get and validate family size from the user
    private static int getNumPeople() {
        int n;
        do {
            System.out.print("Please enter number of people in the family : ");
            while (!in.hasNextInt()) {
                System.out.println("Invalid number of people, try again");
                System.out.print("Please enter number of people in the family : ");
                in.next();
            }
            n = in.nextInt();
            in.nextLine(); // clear newline
            if (n < 1 || n > MAX_PEOPLE) {
                System.out.println("Invalid number of people, try again");
            }
        } while (n < 1 || n > MAX_PEOPLE);
        return n;
    }

    // Input and validate names and teeth per person
    private static void inputFamilyData() {
        for (int p = 0; p < numPeople; p++) {
            System.out.printf("Please enter the name for family member %d   : ", p + 1);
            names[p] = in.nextLine().trim();

            for (int layer = 0; layer < LAYERS; layer++) {
                String layerName = (layer == 0) ? "uppers" : "lowers";
                String s;
                boolean ok;
                do {
                    System.out.printf("Please enter the %s for %s       : ", layerName, names[p]);
                    s = in.nextLine().trim().toUpperCase();
                    ok = validateTeethString(s);
                    if (!ok) {
                        if (s.length() > MAX_TEETH)
                            System.out.println("Too many teeth, try again");
                        else
                            System.out.println("Invalid teeth types, try again");
                    }
                } while (!ok);

                for (int t = 0; t < MAX_TEETH; t++) {
                    teeth[p][layer][t] = (t < s.length()) ? s.charAt(t) : 'M';
                }
            }
        }
    }

    private static boolean validateTeethString(String s) {
        if (s.length() < 1 || s.length() > MAX_TEETH) return false;
        for (char c : s.toCharArray()) {
            if ("IBM".indexOf(Character.toUpperCase(c)) == -1) return false;
        }
        return true;
    }

    // Menu UI helper functions (show & read menu)
    private static void displayMenu() {
        System.out.println();
        System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it          : ");
    }

    private static char getMenuChoice() {
        String s = in.nextLine().trim().toUpperCase();
        while (s.length() != 1 || "PERX".indexOf(s.charAt(0)) == -1) {
            System.out.print("Invalid menu option, try again              : ");
            s = in.nextLine().trim().toUpperCase();
        }
        return s.charAt(0);
    }

    private static void processChoice(char ch) {
        switch (ch) {
            case 'P' -> printRecords();
            case 'E' -> extractTooth();
            case 'R' -> computeRoots();
            case 'X' -> {} // handled by main
        }
    }

    // Print current tooth records (all members)
    private static void printRecords() {
        System.out.println();
        for (int p = 0; p < numPeople; p++) {
            System.out.println(names[p]);
            System.out.print("  Uppers: ");
            for (int t = 0; t < MAX_TEETH; t++) {
                System.out.printf(" %d:%s ", t + 1, teeth[p][0][t]);
            }
            System.out.println();
            System.out.print("  Lowers: ");
            for (int t = 0; t < MAX_TEETH; t++) {
                System.out.printf(" %d:%s ", t + 1, teeth[p][1][t]);
            }
            System.out.println();
        }
    }

    // Extract a single tooth with validation
    private static void extractTooth() {
        System.out.print("Which family member                         : ");
        String name = in.nextLine().trim();
        int idx = findPerson(name);
        while (idx == -1) {
            System.out.print("Invalid family member, try again            : ");
            name = in.nextLine().trim();
            idx = findPerson(name);
        }

        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        String layerInput = in.nextLine().trim().toUpperCase();
        while (!layerInput.equals("U") && !layerInput.equals("L")) {
            System.out.print("Invalid layer, try again                    : ");
            layerInput = in.nextLine().trim().toUpperCase();
        }
        int layer = layerInput.equals("U") ? 0 : 1;

        System.out.print("Which tooth number                          : ");
        int toothNum = getValidInt(1, MAX_TEETH);
        while (teeth[idx][layer][toothNum - 1] == 'M') {
            System.out.print("Missing tooth, try again                    : ");
            toothNum = getValidInt(1, MAX_TEETH);
        }

        teeth[idx][layer][toothNum - 1] = 'M';
    }

    private static int getValidInt(int min, int max) {
        int n;
        while (true) {
            String s = in.nextLine().trim();
            try {
                n = Integer.parseInt(s);
                if (n >= min && n <= max) break;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Invalid tooth number, try again             : ");
        }
        return n;
    }

    private static int findPerson(String name) {
        for (int i = 0; i < numPeople; i++) {
            if (names[i].equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    // Compute & print quadratic “root canal” stats
    private static void computeRoots() {
        int I = 0, B = 0, M = 0;
        for (int p = 0; p < numPeople; p++)
            for (int l = 0; l < LAYERS; l++)
                for (int t = 0; t < MAX_TEETH; t++) {
                    switch (teeth[p][l][t]) {
                        case 'I' -> I++;
                        case 'B' -> B++;
                        case 'M' -> M++;
                    }
                }

        double disc = (B * B) + (4.0 * I * M);
        double r1 = (-B + Math.sqrt(disc)) / (2.0 * I);
        double r2 = (-B - Math.sqrt(disc)) / (2.0 * I);

        System.out.printf("One root canal at     %.2f%n", r1);
        System.out.printf("Another root canal at %.2f%n", r2);
    }
}

