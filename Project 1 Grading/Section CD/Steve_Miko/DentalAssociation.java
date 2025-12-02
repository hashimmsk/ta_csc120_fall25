import java.util.Scanner;

/**
 * Manages dental records for multiple family members, including tracking
 * the state of their upper and lower teeth. Allows for printing records,
 * extracting teeth, and calculating a "root canal" index.
 */
public class DentalAssociation {

    /**
     * A single, shared Scanner object to read all user input from the console.
     */
    public static final Scanner keyboard = new Scanner(System.in);

    public static final int MAX_FAMILY_SIZE = 6;
    /**
     * The main entry point for the application.
     * Handles user input for family setup and the main menu loop.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // --- Variable Initialization ---
        int familySize = 0; // Stores the number of people in the family.

        // --- Welcome and Family Size Input ---
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("---------------------------------------");
        System.out.println("Please enter number of people in the family : ");

        // Loop to validate family size input (must be 1-MAX_FAMILY_SIZE)
        while (familySize < 1 || familySize > MAX_FAMILY_SIZE) {
            familySize = keyboard.nextInt();
            if (familySize < 1 || familySize > MAX_FAMILY_SIZE) {
                System.out.println("Invalid number of people, try again");
            }
        }

        // --- Data Structure Initialization ---
        // Array to store names of family members.
        String[] names = new String[familySize];

        // 3D array to store tooth data:
        // 1st dim: family member index
        // 2nd dim: tooth layer (0 = Uppers, 1 = Lowers)
        // 3rd dim: individual tooth (char 'I', 'B', 'M')
        char[][][] toothData = new char[familySize][2][];

        // --- Loop Variables for Data Entry ---
        int name;         // Index for the current family member
        int toothLevel;   // Index for the tooth layer (0 or 1)
        int currentTooth; // Index for the individual tooth
        boolean validTeeth; // Flag for validation loop

        // --- Data Entry Loop for Each Family Member ---
        for (name = 0; name < familySize; name++) {
            System.out.println("Please enter the name for family member " + (name+1) + " : ");
            String theName = keyboard.next();
            names[name] = theName; // Store the name

            // Inner loop for Uppers (0) and Lowers (1)
            for (toothLevel = 0; toothLevel < 2; toothLevel++) {
                if (toothLevel == 1) { // Index 1 is Lowers
                    System.out.println("Please enter the lowers for " + names[name]);
                } else { // Index 0 is Uppers
                    System.out.println("Please enter the uppers for " + names[name]);
                }

                String teeth = keyboard.next().toUpperCase(); // Read tooth string, force uppercase
                validTeeth = false; // Reset validation flag for each input

                // --- Input Validation Loop for Tooth String ---
                while (!validTeeth) {
                    // Check each character in the input string
                    for (currentTooth = 0; currentTooth < teeth.length(); currentTooth++) {
                        char toothChar = teeth.charAt(currentTooth);
                        // If a character is not 'I', 'B', or 'M', it's invalid
                        if (toothChar != 'I' && toothChar != 'B' && toothChar != 'M') {
                            System.out.println("Invalid teeth types please try again");
                            teeth = keyboard.next().toUpperCase(); // Get new input
                            break; // Exit inner for-loop to re-check the new string
                        }
                    }

                    // Check for length constraint
                    if (teeth.length() > 8) {
                        System.out.println("Too many teeth please try again");
                        teeth = keyboard.next().toUpperCase(); // Get new input
                        continue; // Restart the while loop
                    }

                    // If we get here, all checks passed
                    validTeeth = true;
                }

                // --- Storing Tooth Data ---
                // Initialize the 3rd dimension array to the exact length of the valid input
                toothData[name][toothLevel] = new char[teeth.length()];

                // Copy characters from the valid string into the 3D array
                for (currentTooth = 0; currentTooth < teeth.length(); currentTooth++) {
                    toothData[name][toothLevel][currentTooth] = teeth.charAt(currentTooth);
                }

            } // End of toothLevel loop

        } // End of name loop (all family data entered)


        // --- Main Menu ---
        String selection;        // Stores the user's menu choice
        boolean isRunning = true; // Controls the main menu loop

        while (isRunning) {
            System.out.println("(P)rint, (E)xtract, (R)oot, e(X)it");
            selection = keyboard.next().toUpperCase(); // Get choice, force uppercase

            // Check the first character of the selection
            if (selection.charAt(0) == 'P') {
                print(familySize, names, toothData); // Call print method
                continue; // Restart loop
            }
            if (selection.charAt(0) == 'E') {
                extract(names, toothData); // Call extract method
                continue; // Restart loop
            }
            if (selection.charAt(0) == 'R') {
                root(toothData, familySize); // Call root method
                continue; // Restart loop
            }
            if (selection.charAt(0) == 'X') {
                System.out.println("Exiting the Floridian Tooth Records :-)");
                isRunning = false; // Set flag to false
                break; // Exit the loop
            }

            // If no valid option was chosen
            System.out.println("Invalid menu option, try again");
        } // End of while(isRunning)



    } // End of main method

    /**
     * Calculates and prints the family's root canal indices.
     * This is based on the roots of the quadratic equation Ix^2 + Bx - M = 0,
     * where I, B, and M are the total counts of those tooth types
     * for the entire family.
     *
     * @param toothData  The 3D array of all tooth data.
     * @param familySize The total number of family members.
     */
    public static void root(char[][][] toothData, int familySize) {
        // Use double for calculations to ensure precision
        double countI = 0; // 'a' coefficient
        double countB = 0; // 'b' coefficient
        double countM = 0; // used for 'c' coefficient

        // Iterate through every person, both layers, and every tooth
        for (int person = 0; person < familySize; person++) {
            for (int layer = 0; layer < 2; layer++) {
                for (int tooth = 0; tooth < toothData[person][layer].length; tooth++) {
                    char currentTooth = toothData[person][layer][tooth];
                    // Increment the respective counter
                    if (currentTooth == 'I') {
                        countI++;
                    } else if (currentTooth == 'B') {
                        countB++;
                    } else if (currentTooth == 'M') {
                        countM++;
                    }
                }
            }
        }

        // Now, solve the quadratic equation ax^2 + bx + c = 0
        // a = I, b = B, c = -M
        double a = countI;
        double b = countB;
        double c = -countM; // Note the negative sign

        // Check if 'a' is zero (no incisors)
        if (a == 0) {
            // This would cause a division by zero in the quadratic formula
            System.out.println("Family has no incisors, cannot calculate root canals.");
            return; // Exit the method
        }

        // Calculate the discriminant (b^2 - 4ac)
        double discriminant = (b * b) - (4 * a * c);

        // Calculate the two roots using the quadratic formula
        // Since a, b >= 0 and c <= 0, the discriminant will always be non-negative.
        double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        // Print formatted output
        System.out.printf("One root canal at     %.2f\n", root1);
        System.out.printf("Another root canal at %.2f\n", root2);
    }

    /**
     * Prompts the user to extract a tooth for a specific family member.
     * It validates the family member's name, the tooth layer (upper/lower),
     * and the tooth number. If the tooth exists and is not already missing,
     * it marks it as 'M' (Missing).
     *
     * @param names     The array of family member names.
     * @param toothData The 3D array of all tooth data.
     */
    public static void extract(String[] names, char[][][] toothData) {
        int familyMemberIndex = -1; // Stores index of the person, -1 means not found

        // 1. Find the family member
        while (familyMemberIndex == -1) {
            System.out.print("Which family member : ");
            String inputName = keyboard.next();
            // Loop through names array to find a match (case-insensitive)
            for (int i = 0; i < names.length; i++) {
                if (names[i].equalsIgnoreCase(inputName)) {
                    familyMemberIndex = i; // Found them, store the index
                    break; // Exit the for-loop
                }
            }
            // If loop finishes and index is still -1, name wasn't found
            if (familyMemberIndex == -1) {
                System.out.println("Invalid family member, try again");
            }
        } // End of family member validation loop

        // 2. Get the tooth layer
        int toothLayerIndex = -1; // 0 for U, 1 for L
        while (toothLayerIndex == -1) {
            System.out.print("Which tooth layer (U)pper or (L)ower : ");
            char layerChar = keyboard.next().toUpperCase().charAt(0);
            if (layerChar == 'U') {
                toothLayerIndex = 0; // Uppers are at index 0
            } else if (layerChar == 'L') {
                toothLayerIndex = 1; // Lowers are at index 1
            } else {
                System.out.println("Invalid layer, try again");
            }
        } // End of layer validation loop

        // 3. Get the tooth number and perform extraction
        int toothNumber; // 1-based number from user
        // Get the number of teeth in the selected layer for this person
        int maxTeeth = toothData[familyMemberIndex][toothLayerIndex].length;

        while (true) { // Loop until a valid extraction is made
            System.out.print("Which tooth number : ");
            toothNumber = keyboard.nextInt();

            // Validate range (user enters 1-based, array is 0-based)
            if (toothNumber < 1 || toothNumber > maxTeeth) {
                System.out.println("Invalid tooth number, try again");
                continue; // Restart the loop
            }

            // Check if already missing (use 0-based index)
            if (toothData[familyMemberIndex][toothLayerIndex][toothNumber - 1] == 'M') {
                System.out.println("Missing tooth, try again");
                continue; // Restart the loop
            }

            // If we're here, all checks passed. Extract the tooth.
            // Set the character at the 0-based index to 'M'
            toothData[familyMemberIndex][toothLayerIndex][toothNumber - 1] = 'M';
            System.out.println("Tooth extracted."); // Confirmation message
            break; // Exit the loop
        } // End of tooth number validation loop
    }

    /**
     * Prints the current dental records for all family members.
     * Iterates through each person and prints their name followed by
     * their Uppers and Lowers tooth data.
     *
     * @param familySize The total number of family members.
     * @param names      The array of family member names.
     * @param toothData  The 3D array of all tooth data.
     */
    public static void print(int familySize, String[] names, char[][][] toothData) {
        // These variables are shadows of the ones in main, but part of the original code
        int name;
        int toothLevel;
        int currentTooth;
        // boolean validTeeth; // This variable is unused in this method

        // Loop through each family member by index
        for (name = 0; name < familySize; name++) {
            System.out.println(names[name]); // Print the person's name

            // Loop through the two tooth layers
            for (toothLevel = 0; toothLevel < 2; toothLevel++) {
                if (toothLevel == 0) { // Index 0
                    System.out.print("Uppers: ");
                } else { // Index 1
                    System.out.print("Lowers: ");
                }

                // Loop through each tooth in the current layer
                for (currentTooth = 0; currentTooth < toothData[name][toothLevel].length; currentTooth++) {
                    // Print 1-based index and the tooth character
                    System.out.print((currentTooth+1) + ":" + toothData[name][toothLevel][currentTooth] + " ");
                }
                System.out.println(); // Add a newline after each layer
            }

        } // End of family member loop
    }

} // End of class DentalAssociation
