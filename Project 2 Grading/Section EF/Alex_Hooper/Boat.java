// Imports
import java.io.Serializable;

// Define boat kinds

/**
 * Define setup for the kind of boat
 */

enum boatKinds {
    /**
     * A sailing boat.
     */
    SAILING,
    /**
     * A power boat.
     */
    POWER
}



/**
 * Class to hold all of a boat's attributes.
 * @author alexhooper
 * @version 1.0
 */
public class Boat implements Serializable {

// DATA ATTRIBUTES
    /**
     * Define alignment for boat output string
     */
    final String BOAT_STRING_ALIGNMENT = "%-10s%-15s%-8s%-10s%-4s  :  Paid: $%-10s  : Spent: $%-5s";
    /**
     * Set the initial spending default to 0 when a new boat is added
     */
    private static final double INITIAL_SPENT_NUMBER = 0.00;
    /**
     * The kind of boat, an enum
     */
    private boatKinds boatKind;
    /**
     * The name of the boat
     */
    private String boatName;
    /**
     * The year the boat was manufactured - an integer
     */
    private short manufactureYear;
    /**
     * The model of the boat
     */
    private String model;
    /**
     * The length of the boat in feet
     */
    private byte boatLength;
    /**
     * The price the boat was to buy, in USD
     */
    private double purchasePrice;
    /**
     * The amount spent on the boat, in USD
     */
    private double expenses;


// CONSTRUCTORS

    /**
     * Constructor method to validate and set all parts of boat.
     * @param inputString A string in the format kind, name, manufacture year, model, length, price, and, if from .db, amount spent
     * @see Boat
     */
    public Boat(String inputString) {

        String[] stringSplit;

        // Import the input string, split, and clean it.
        try {
            stringSplit = inputString.split(",");
            for (var i = 0; i < stringSplit.length; i++) {
                stringSplit[i] = stringSplit[i].strip();
            }
        } catch (Error e) {
            return;
        }

        // Set boat type to enum by converting first value in string
        try {
            boatKind = boatKinds.valueOf(stringSplit[0].toUpperCase());
        } catch (IllegalArgumentException e ) {
            System.out.println("The boat type " + stringSplit[0] + " isn't valid.");
            throw new RuntimeException(e);
        }

        // Set boat name
        boatName = stringSplit[1];

        // Set manufactured year
        try {
            manufactureYear = Short.parseShort(stringSplit[2]);
        } catch (NumberFormatException e) {
            System.out.println("The input for the manufacture year wasn't a valid year");
            throw new RuntimeException(e);
        }

        // Set model
        model = stringSplit[3];

        // Set boat length
        try {
            boatLength = Byte.parseByte(stringSplit[4]);
        } catch (NumberFormatException e) {
            System.out.println("The length of the boat wasn't a valid number.");
            throw new RuntimeException(e);
        }

        // Set purchase price
        try {
            purchasePrice = Double.parseDouble(stringSplit[5]);
        } catch (NumberFormatException e) {
            System.out.println("The purchase price wasn't a valid number.");
            throw new RuntimeException(e);
        }


        // If there is a value for a boat's expense, set it, otherwise cancel to 0.00
        try {
            if (stringSplit.length == 6) {
                expenses = INITIAL_SPENT_NUMBER;
            }
            else if (stringSplit.length == 7) {
                expenses = Double.parseDouble(stringSplit[6]);
            }
        } catch (NumberFormatException e) {
            System.out.println("The expense price isn't a valid number.");
            throw new RuntimeException(e);
        }
    } // End of single parameter constructor


// SETTERS

    // N/A


// GETTERS

    /**
     * Return the string format for the full fleet report.
     * @return Properly formatted output string
     */
    @Override
    public String toString() {
        return String.format(BOAT_STRING_ALIGNMENT,boatKind, boatName, manufactureYear, model, boatLength, String.format("%.2f", purchasePrice), String.format("%.2f", expenses));
    } // End of toString method

    /**
     * Return string of name.
     * @return Boat name.
     */
    public String getName() {
        return boatName;
    }

    /**
     * Return how much has been spent on the boat.
     * @return Amount spent on boat.
     */
    public double getExpenses() {
        return expenses;
    } // End of getExpenses method

    /**
     * Return how much the boat cost.
     * @return Amount the boat coast
     */
    public double getPaid() {
        return purchasePrice;
    } // End of getPaid method


// MUTATORS

    /**
     * Check if expense can be added, if so, add it.
     * @param additionalExpenses How much is supposed to be added.
     * @return Whether the expense was allowed to be added.
     */
    public boolean addExpense(double additionalExpenses) {
        // Conditional: does the expense make this more than it cost?
        if (expenses + additionalExpenses > purchasePrice) {
            return false;
        }

        // If expense is valid, add it and tell caller addition was successful
        expenses += additionalExpenses;
        return true;
    } // End of addExpense method

} // End of Boat class
