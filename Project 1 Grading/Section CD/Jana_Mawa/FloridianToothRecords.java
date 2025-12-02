/**
 * FloridianToothRecords.java
 *
 * A program to record the teeth of up to 6 family members.
 * Stores names in a String[] and teeth in a three-dimensional char array:
 *   teeth[personIndex][rowIndex][toothIndex]
 * where rowIndex: 0 = uppers, 1 = lowers, and toothIndex up to 7 (max 8 teeth per row).
 *
 * The program supports:
 *  - Printing the family's teeth record
 *  - Extracting a tooth (setting it to 'M' if it exists)
 *  - Reporting the family's root canal indices (roots of I*x^2 + B*x - M = 0)
 *  - Exit
 *
 * Input is validated per assignment specification.
 *
 * Usage: run the main() method and follow prompts.
 *
 * @author
 * @version 1.0
 */

import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.Locale;

public class FloridianToothRecords {
    private static final int MAX_PEOPLE = 6;
    private static final int MAX_TEETH_PER_ROW = 8;

    private final Scanner scanner;
    private final String[] names;
    private final char[][][] teeth;
    private final int[] upperCount;
    private final int[] lowerCount;
    private int familyCount;

    public FloridianToothRecords() {
        scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        names = new String[MAX_PEOPLE];
        teeth = new char[MAX_PEOPLE][2][MAX_TEETH_PER_ROW];
        upperCount = new int[MAX_PEOPLE];
        lowerCount = new int[MAX_PEOPLE];
        familyCount = 0;
    }

    public static void main(String[] args) {
        FloridianToothRecords app = new FloridianToothRecords();
        app.run();
    }

    /**
     * Orchestrates the program flow.
     */
    public void run() {
        printWelcome();
        readFamily();
        menuLoop();
        System.out.println();
        System.out.println("Exiting the Floridian Tooth Records :-)");
    }

    private void printWelcome() {
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");
    }

    /**
     * Read number of people and then for each person read name, uppers, lowers.
     */
    private void readFamily() {
        familyCount = readIntInRange(
                "Please enter number of people in the family : ",
                "Invalid number of people, try again         : ",
                1, MAX_PEOPLE);

        for (int i = 0; i < familyCount; i++) {
            String promptName = String.format("Please enter the name for family member %d   : ", i + 1);
            System.out.print(promptName);
            String name = scanner.nextLine().trim();
            while (name.isEmpty()) {
                System.out.print("Invalid name, try again                     : ");
                name = scanner.nextLine().trim();
            }
            names[i] = name;

            upperCount[i] = readTeethRow(i, 0, "uppers", name);
            lowerCount[i] = readTeethRow(i, 1, "lowers", name);
        }
    }

    /**
     * Read teeth string for a given row (0=uppers,1=lowers) for person i.
     */
    private int readTeethRow(int personIndex, int rowIndex, String rowName, String personName) {
        String prompt = String.format("Please enter the %s for %s       : ", rowName, personName);
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            // allow empty string (no teeth) — treat length 0
            if (!isValidTeethString(line)) {
                System.out.print("Invalid teeth types, try again              : ");
                continue;
            }
            if (line.length() > MAX_TEETH_PER_ROW) {
                System.out.print("Too many teeth, try again                   : ");
                continue;
            }
            // store uppercase chars
            for (int t = 0; t < line.length(); t++) {
                teeth[personIndex][rowIndex][t] = Character.toUpperCase(line.charAt(t));
            }
            // Fill remaining slots with '\0' for clarity (not necessary but clean)
            for (int t = line.length(); t < MAX_TEETH_PER_ROW; t++) {
                teeth[personIndex][rowIndex][t] = '\0';
            }
            return line.length();
        }
    }

    /**
     * Validates a teeth string contains only I, B, M (case-insensitive).
     * Empty string is valid.
     */
    private boolean isValidTeethString(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = Character.toUpperCase(s.charAt(i));
            if (c != 'I' && c != 'B' && c != 'M') {
                return false;
            }
        }
        return true;
    }

    /**
     * Reads an int using the prompt strings and enforces min..max inclusive.
     * Assumes numeric input is syntactically correct (per assignment).
     */
    private int readIntInRange(String prompt, String invalidPrompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) {
                    prompt = invalidPrompt; // subsequent prompt text
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                // The assignment states numeric input will be syntactically correct;
                // but be robust anyway.
                prompt = invalidPrompt;
            }
        }
    }

    /** Main menu loop */
    private void menuLoop() {
        outer:
        while (true) {
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            String choiceRaw = scanner.nextLine().trim();
            if (choiceRaw.isEmpty()) {
                System.out.print("Invalid menu option, try again              : ");
                continue;
            }
            char choice = Character.toUpperCase(choiceRaw.charAt(0));
            switch (choice) {
                case 'P':
                    performPrint();
                    break;
                case 'E':
                    performExtract();
                    break;
                case 'R':
                    performRoot();
                    break;
                case 'X':
                    break outer;
                default:
                    System.out.print("Invalid menu option, try again              : ");
            }
        }
    }

    /** Print the family's teeth record in the specified format. */
    private void performPrint() {
        System.out.println();
        for (int i = 0; i < familyCount; i++) {
            System.out.println(names[i]);
            // Uppers
            System.out.print("  Uppers: ");
            if (upperCount[i] == 0) {
                System.out.print("(none)");
            } else {
                for (int t = 0; t < upperCount[i]; t++) {
                    char c = teeth[i][0][t];
                    if (c == '\0') c = 'M'; // defensive
                    System.out.printf(" %d:%c", t + 1, c);
                }
            }
            System.out.println();

            System.out.print("  Lowers: ");
            if (lowerCount[i] == 0) {
                System.out.print("(none)");
            } else {
                for (int t = 0; t < lowerCount[i]; t++) {
                    char c = teeth[i][1][t];
                    if (c == '\0') c = 'M';
                    System.out.printf(" %d:%c", t + 1, c);
                }
            }
            System.out.println();
        }
    }

    /** Extract a tooth: find member, row, tooth number, ensure not missing, set to 'M'. */
    private void performExtract() {

        int personIndex = -1;
        while (true) {
            System.out.print("Which family member                         : ");
            String nameInput = scanner.nextLine().trim();
            personIndex = findMemberByName(nameInput);
            if (personIndex == -1) {
                System.out.print("Invalid family member, try again            : ");
            } else break;
        }

        // Which tooth layer
        int row = -1;
        while (true) {
            System.out.print("Which tooth layer (U)pper or (L)ower        : ");
            String layer = scanner.nextLine().trim();
            if (layer.isEmpty()) {
                System.out.print("Invalid layer, try again                    : ");
                continue;
            }
            char c = Character.toUpperCase(layer.charAt(0));
            if (c == 'U') { row = 0; break; }
            else if (c == 'L') { row = 1; break; }
            else {
                System.out.print("Invalid layer, try again                    : ");
            }
        }

        // Which tooth number
        int toothNum = -1;
        int maxForRow = (row == 0) ? upperCount[personIndex] : lowerCount[personIndex];
        if (maxForRow == 0) {
            System.out.println("No teeth in that layer to extract.");
            return;
        }
        while (true) {
            System.out.print("Which tooth number                          : ");
            String numLine = scanner.nextLine().trim();
            try {
                toothNum = Integer.parseInt(numLine);
                if (toothNum < 1 || toothNum > maxForRow) {
                    System.out.print("Invalid tooth number, try again             : ");
                    continue;
                }
                char current = teeth[personIndex][row][toothNum - 1];
                if (Character.toUpperCase(current) == 'M') {
                    System.out.print("Missing tooth, try again                    : ");
                    // according to sample, re-ask for a valid tooth number until non-missing
                    continue;
                }
                // perform extraction: set to 'M'
                teeth[personIndex][row][toothNum - 1] = 'M';
                // done
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid tooth number, try again             : ");
            }
        }
    }

    /** Find member by case-insensitive name match. Returns index or -1. */
    private int findMemberByName(String input) {
        for (int i = 0; i < familyCount; i++) {
            if (names[i].equalsIgnoreCase(input)) return i;
        }
        return -1;
    }

    /**
     * Compute family totals of I, B, M and solve I*x^2 + B*x - M = 0 for x.
     * Print real roots with two decimals or appropriate messages for degenerate cases.
     */
    private void performRoot() {
        int totalI = 0, totalB = 0, totalM = 0;
        for (int i = 0; i < familyCount; i++) {
            for (int t = 0; t < upperCount[i]; t++) {
                char c = Character.toUpperCase(teeth[i][0][t]);
                if (c == 'I') totalI++;
                else if (c == 'B') totalB++;
                else if (c == 'M') totalM++;
            }
            for (int t = 0; t < lowerCount[i]; t++) {
                char c = Character.toUpperCase(teeth[i][1][t]);
                if (c == 'I') totalI++;
                else if (c == 'B') totalB++;
                else if (c == 'M') totalM++;
            }
        }

        DecimalFormat df = new DecimalFormat("0.00");


        if (totalI == 0) {
            // linear equation: B*x - M = 0 -> x = M / B (if B != 0)
            if (totalB == 0) {
                if (totalM == 0) {
                    System.out.println("Every coefficient zero: Infinite solutions (all x).");
                } else {
                    System.out.println("No roots (equation reduces to 0 = " + (-totalM) + ").");
                }
            } else {
                double x = (double) totalM / (double) totalB;
                System.out.println("One root canal at     " + df.format(x));
            }
            return;
        }


        double a = (double) totalI;
        double b = (double) totalB;
        double c = -(double) totalM; // because equation is a x^2 + b x + c = 0 where c = -M

        double discriminant = b * b - 4.0 * a * c;
        if (discriminant < 0) {
            System.out.println("No real roots.");
            return;
        }
        double sqrtD = Math.sqrt(discriminant);
        double x1 = (-b + sqrtD) / (2.0 * a);
        double x2 = (-b - sqrtD) / (2.0 * a);


        System.out.println("One root canal at     " + df.format(x1));
        System.out.println("Another root canal at " + df.format(x2));
    }
}

