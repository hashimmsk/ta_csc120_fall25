import java.util.Scanner;
//=============================================================================
/**
     * Family Dental Records
     * @author Mina Guler
     * @version 1.0
     */
    public class DentalRecords {
        /**
         * default constructor
         */
        public DentalRecords() {}
//----------------------------------------------------------------------------------------------    ---
        /**
         * Global Scanner object to use keyboard
         */
        static Scanner keyboard = new Scanner(System.in);
        /**
         * Maximum family size is 6
         */
        private static final int MAX_FAMILY_SIZE = 6;
        /**
         * Minimum family size is 1
         */
        private static final int MIN_FAMILY_SIZE = 1;
        /**
         * Two teeth layers (upper/lower)
         */
        private static final int TEETH_LAYERS = 2;
        /**
         * Maximum amount of teeth in each layer is 8
         */
        private static final int MAX_TEETH = 8;
//-------------------------------------------------------------------------------------------------
        /**
         * The main method
         * @param args Passed in from the command line
         */
        public static void main(String[] args) {
            // variables to hold familySize, names of every person in the family, and teeth information
            int familySize;
            String[] names;
            char[][][] teeth; // [person][upper/lower][teeth]

            System.out.println("Welcome to the Floridian Tooth Records");
            System.out.println("--------------------------------------");

            // use methods to find family size, name of each family member, and their teeth information
            familySize = getFamilySize();

            //update names and teeth arrays according to family size and teeth layers
            names = new String [familySize];
            teeth = new char[familySize][TEETH_LAYERS][];

            getFamilyNamesAndTeeth(familySize, names, teeth);

            showMenu(names, teeth);

        } // end of main method
//--------------------------------------------------------------------------------
        /**
         Ask for the number of family members (1–6).
         @return Number of family members
         */
        public static int getFamilySize() {
            // declare family size
            int size;

            // enter number of people
            System.out.print("Please enter number of people in the family: ");
            do {
                size = keyboard.nextInt();
                if (size < MIN_FAMILY_SIZE || size > MAX_FAMILY_SIZE)
                    System.out.print("Invalid number of people, try again: ");
            } while (size < MIN_FAMILY_SIZE || size > MAX_FAMILY_SIZE);
            return size;
        } // end of getFamilySize method
//---------------------------------------------------------------------------------
        /**
         Ask family names and teeth Information. Hold information in names and teeth arrays respectively.
         @param familySize size of the family
         @param names array of names
         @param teeth array of names, teeth layer, and teeth information
         */
        public static void getFamilyNamesAndTeeth(int familySize, String[] names, char[][][]teeth) {
            int sizeIndex;
            int locationIndex;
            int teethIndex;
            int characterIndex;
            String teethInput;
            String layer;
            char characterInput = ' ';

            for (sizeIndex = 0; sizeIndex < familySize; sizeIndex++) {
                System.out.print("Please enter the name for family member " + (sizeIndex + 1) + ": ");
                names[sizeIndex] = keyboard.next();

                for (locationIndex = 0; locationIndex < TEETH_LAYERS; locationIndex++) {
                    if (locationIndex == 0) {
                        layer = "uppers";
                    } else {
                        layer = "lowers";
                    } // end of if statement

                    System.out.print("Please enter the " + layer + " for " + names[sizeIndex] + ": ");

                    do {
                        teethInput = keyboard.next().toUpperCase();

                        if (teethInput.length() > MAX_TEETH) {
                            System.out.print("Too many teeth, try again: ");
                        } else {
                            for (characterIndex = 0; characterIndex < teethInput.length(); characterIndex++) {
                                characterInput = teethInput.charAt(characterIndex);
                                if (!(characterInput == 'I' || characterInput == 'B' || characterInput == 'M')) {
                                    System.out.print("Invalid teeth types, try again: ");
                                    break; // if there is one Invalid teeth type, don't check the others (and leave if statement)
                                } // end of if statement on teeth types
                            } // end of for loop
                        } // end of if statement (maximum amount of teeth)

                    } while (teethInput.length() > MAX_TEETH || !(characterInput == 'I' || characterInput == 'B' || characterInput == 'M'));

                    teeth[sizeIndex][locationIndex] = new char[teethInput.length()]; // update teeth length

                    for (teethIndex = 0; teethIndex < teethInput.length(); teethIndex++) {
                        teeth[sizeIndex][locationIndex][teethIndex] = teethInput.charAt(teethIndex);
                    } // end of for loop (teeth index)
                } // end of for loop (location index)
            } // end of for loop (size index)
        } // end of getFamilyNames method
//---------------------------------------------------------------------------------
        /**
         Show menu to print teeth information, extract a tooth, take the root, or exit the program.
         @param names array of names
         @param teeth array of names, teeth layer, and teeth information
         */
        public static void showMenu(String[] names, char[][][] teeth) {
            char choice;

            do {
                System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it: ");
                choice = Character.toUpperCase(keyboard.next().charAt(0));

                if (choice == 'P') {
                    printFamily(names, teeth);
                } else if (choice == 'E') {
                    extractTooth(names, teeth);
                } else if (choice == 'R') {
                    calcRootCanals(teeth);
                } else if (choice == 'X') {
                    exitProgram();
                } else {
                    System.out.print("Invalid menu option, try again: ");
                } // end of if statement

            } while (choice != 'X');
        } // end of showMenu method
//---------------------------------------------------------------------------------
        /**
         Print family teeth information.
         @param names array of names
         @param teeth array of names, teeth layer, and teeth information
         */
        public static void printFamily(String[] names, char[][][] teeth) {
            int nameIndex;
            int locationIndex;

            for (nameIndex = 0; nameIndex < names.length; nameIndex++) {
                System.out.println(names[nameIndex]);

                System.out.print("  Uppers:");
                for (locationIndex = 0; locationIndex < teeth[nameIndex][0].length; locationIndex++) {
                    System.out.print(" " + (locationIndex + 1) + ":" + teeth[nameIndex][0][locationIndex]);
                } // end of for loop (uppers)

                System.out.println();

                System.out.print("  Lowers:");
                for (locationIndex = 0; locationIndex < teeth[nameIndex][1].length; locationIndex++) {
                    System.out.print(" " + (locationIndex + 1) + ":" + teeth[nameIndex][1][locationIndex]);
                } // end of for loop (lowers)
                System.out.println();
            } // end of for loop (name index)
        } // end of printFamily method
//---------------------------------------------------------------------------------
        /**
         Extract a tooth from one of the family members
         @param names array of names
         @param teeth array of names, teeth layer, and teeth information
         */
        public static void extractTooth(String[] names, char[][][] teeth) {
            int nameIndex;
            int locationIndex;
            int personIndex; // -1 if false
            int teethIndex;
            int toothNum;
            String person;
            char layer; // teeth layer

            // ask for family member
            System.out.print("Which family member: ");
            do {
                person = keyboard.next();
                personIndex = -1;

                for (nameIndex = 0; nameIndex < names.length; nameIndex++) {
                    if (names[nameIndex].equalsIgnoreCase(person)) {
                        personIndex = nameIndex;
                    } // end of if statement
                } // end of for loop (name index)

                if (personIndex == -1) {
                    System.out.print("Invalid family member, try again: ");
                } // end of if statement
            } while (personIndex == -1);

            System.out.print("Which tooth layer (U)pper or (L)ower: ");

            do {
                layer = Character.toUpperCase(keyboard.next().charAt(0));
                if (layer != 'U' && layer != 'L') {
                    System.out.print("Invalid layer, try again: ");
                } // end of if statement
            } while (layer != 'U' && layer != 'L');

            if (layer == 'U') {
                locationIndex = 0;
            } else {
                locationIndex = 1;
            } // end of if statement

            // check tooth number
            System.out.print("Which tooth number: ");
            do {
                toothNum = keyboard.nextInt();
                teethIndex = toothNum - 1;

                if (teethIndex < 0 || teethIndex > teeth[personIndex][locationIndex].length) {
                    System.out.print("Invalid tooth number, try again: ");
                } else if (teeth[personIndex][locationIndex][teethIndex] == 'M') {
                    System.out.print("Missing tooth, try again: ");
                } // end of if statement

            } while (teethIndex < 0 || teethIndex > teeth[personIndex][locationIndex].length || teeth[personIndex][locationIndex][teethIndex] == 'M');

            teeth[personIndex][locationIndex][teethIndex] = 'M';

        } // end of extractTooth method
//---------------------------------------------------------------------------------
        /**
         Calculate Root Canals
         @param teeth array of names, teeth layer, and teeth information
         */
        public static void calcRootCanals(char[][][] teeth) {
            int I = 0;
            int B = 0;
            int M = 0;
            int personIndex;
            int locationIndex;
            int toothIndex;
            char currentTeeth;
            double discriminant;

            for (personIndex = 0; personIndex < teeth.length; personIndex++) {
                for (locationIndex = 0; locationIndex < TEETH_LAYERS; locationIndex++) {
                    for (toothIndex = 0; toothIndex < teeth[personIndex][locationIndex].length; toothIndex++) {
                        currentTeeth = teeth[personIndex][locationIndex][toothIndex];

                        // count each type of tooth
                        if (currentTeeth == 'I') {
                            I++;
                        } else if (currentTeeth == 'B') {
                            B++;
                        } else if (currentTeeth == 'M') {
                            M++;
                        } // end of if statement
                    } // end of for loop (tooth index)
                } // end of for loop (location index)
            } // end of for loop (person index)

            if (I == 0) {
                System.out.println("Cannot calculate roots (I = 0).");
                return;
            } // end of if statement

            discriminant = B * B + 4.0 * I * M;
            double r1 = (-B + Math.sqrt(discriminant)) / (2 * I);
            double r2 = (-B - Math.sqrt(discriminant)) / (2 * I);

            System.out.printf("One root canal at %.2f%n", r1);
            System.out.printf("Another root canal at %.2f%n", r2);
        } // end of calcRootCanals method
//---------------------------------------------------------------------------------
        /**
         Exit the program
         */
        public static void exitProgram() {
            System.out.println("Exiting the Floridian Tooth Records :-)");
        } // end of exitProgram method
    } // end of Dental Records class



