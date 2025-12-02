import java.util.Scanner;
public class Project1 {


    private static final int MAX_PEOPLE = 6;
    private static final int NUM_LAYERS = 2; // 0 = upper, 1 = lower
    private static final int MAX_TEETH_PER_LAYER = 8;

    private final Scanner keyboard = new Scanner(System.in);
    private String[] names;
    private char[][][] teeth;
    private int numPeople;

    public static void main(String[] args) {
        new Project1().run();
    }

    private void run() {
        printWelcome();
        setupFamilyData();
        menuLoop();
        exitProgram();
    }

    private void printWelcome() {
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");
    }

    private void setupFamilyData() {
        numPeople = getValidatedInt("Please enter number of people in the family", 1, MAX_PEOPLE);

        names = new String[numPeople];
        teeth = new char[numPeople][NUM_LAYERS][MAX_TEETH_PER_LAYER];

        for (int i = 0; i < numPeople; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + "   : ");
            names[i] = keyboard.nextLine().trim();

            String uppers = getValidatedTeethString("Please enter the uppers for " + names[i]);
            fillTeethRow(teeth[i][0], uppers);

            String lowers = getValidatedTeethString("Please enter the lowers for " + names[i]);
            fillTeethRow(teeth[i][1], lowers);
        }
    }

    private void fillTeethRow(char[] row, String data) {
        for (int i = 0; i < MAX_TEETH_PER_LAYER; i++) row[i] = '\0';
        for (int i = 0; i < data.length(); i++) row[i] = Character.toUpperCase(data.charAt(i));
    }

    private String getValidatedTeethString(String prompt) {
        String input;
        boolean valid = false;
        do {
            System.out.print(prompt + " : ");
            input = keyboard.nextLine().trim().toUpperCase();
            if (input.length() > MAX_TEETH_PER_LAYER) {
                System.out.println("Too many teeth, try again");
                continue;
            }
            valid = true;
            for (char c : input.toCharArray()) {
                if (c != 'I' && c != 'B' && c != 'M') {
                    valid = false;
                    break;
                }
            }
            if (!valid) System.out.println("Invalid teeth types, try again");
        } while (!valid);
        return input;
    }

    private void menuLoop() {
        char option = '\0';
        do {
            option = getMenuOption();
            switch (option) {
                case 'P' -> printRecords();
                case 'E' -> extractTooth();
                case 'R' -> reportRootCanalIndices();
            }
        } while (option != 'X');
    }

    private char getMenuOption() {
        char opt = '\0';
        boolean valid = false;
        do {
            System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it          : ");
            String line = keyboard.nextLine().trim().toUpperCase();
            if (!line.isEmpty()) {
                opt = line.charAt(0);
                if (opt == 'P' || opt == 'E' || opt == 'R' || opt == 'X') valid = true;
            }
            if (!valid) System.out.println("Invalid menu option, try again");
        } while (!valid);
        return opt;
    }

    private void printRecords() {
        for (int i = 0; i < numPeople; i++) {
            System.out.println(names[i]);
            printLayer("Uppers", teeth[i][0]);
            printLayer("Lowers", teeth[i][1]);
        }
    }

    private void printLayer(String name, char[] row) {
        System.out.print("  " + name + ": ");
        for (int j = 0; j < MAX_TEETH_PER_LAYER; j++) {
            if (row[j] == '\0') break;
            System.out.print((j + 1) + ":" + row[j] + "  ");
        }
        System.out.println();
    }

    private void extractTooth() {
        int index = getValidatedFamilyMemberIndex("Which family member");
        char layer = getValidatedLayer("Which tooth layer (U)pper or (L)ower");
        int layerIndex = (layer == 'U') ? 0 : 1;

        boolean validExtraction = false;
        do {
            int toothNum = getValidatedInt("Which tooth number", 1, MAX_TEETH_PER_LAYER);
            int tIndex = toothNum - 1;
            if (teeth[index][layerIndex][tIndex] == '\0' || teeth[index][layerIndex][tIndex] == 'M') {
                System.out.println("Missing tooth, try again");
            } else {
                teeth[index][layerIndex][tIndex] = 'M';
                validExtraction = true;
            }
        } while (!validExtraction);
    }

    private void reportRootCanalIndices() {
        int countI = 0, countB = 0, countM = 0;
        for (int i = 0; i < numPeople; i++) {
            for (int l = 0; l < NUM_LAYERS; l++) {
                for (int t = 0; t < MAX_TEETH_PER_LAYER; t++) {
                    char c = teeth[i][l][t];
                    if (c == 'I') countI++;
                    else if (c == 'B') countB++;
                    else if (c == 'M') countM++;
                }
            }
        }

        if (countI == 0) {
            if (countB == 0) System.out.println("Cannot calculate indices");
            else System.out.printf("One root canal at %.2f%n", (double) countM / countB);
            return;
        }

        double discriminant = countB * countB + 4 * countI * countM;
        if (discriminant < 0) {
            System.out.println("No real root canal indices (negative discriminant)");
            return;
        }

        double sqrtDelta = Math.sqrt(discriminant);
        double r1 = (-countB + sqrtDelta) / (2 * countI);
        double r2 = (-countB - sqrtDelta) / (2 * countI);

        System.out.printf("One root canal at     %.2f%n", r1);
        if (Math.abs(r1 - r2) > 0.000001) System.out.printf("Another root canal at %.2f%n", r2);
    }

    private int getValidatedFamilyMemberIndex(String prompt) {
        int index = -1;
        boolean valid = false;
        do {
            System.out.print(prompt + " : ");
            String input = keyboard.nextLine().trim();
            for (int i = 0; i < numPeople; i++) {
                if (input.equalsIgnoreCase(names[i])) {
                    index = i;
                    valid = true;
                    break;
                }
            }
            if (!valid) System.out.println("Invalid family member, try again");
        } while (!valid);
        return index;
    }

    private char getValidatedLayer(String prompt) {
        char layer = '\0';
        boolean valid = false;
        do {
            System.out.print(prompt + " : ");
            String input = keyboard.nextLine().trim().toUpperCase();
            if (!input.isEmpty() && (input.charAt(0) == 'U' || input.charAt(0) == 'L')) {
                layer = input.charAt(0);
                valid = true;
            }
            if (!valid) System.out.println("Invalid layer, try again");
        } while (!valid);
        return layer;
    }

    private int getValidatedInt(String prompt, int min, int max) {
        int val = -1;
        boolean valid = false;
        do {
            System.out.print(prompt + " : ");
            val = keyboard.nextInt();
            keyboard.nextLine(); // consume newline
            if (val >= min && val <= max) valid = true;
            else System.out.println("Invalid number, try again");
        } while (!valid);
        return val;
    }

    private void exitProgram() {
        System.out.println("Exiting the Floridian Tooth Records :-)");
        keyboard.close();
    }





} // end of Project1 class
