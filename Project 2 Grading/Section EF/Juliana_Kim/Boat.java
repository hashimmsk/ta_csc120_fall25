import java.io.Serializable;

/**
 * The purpose of this program is to track costs
 * associated with the various sailing boats and
 * coach (power) boats that the club owns
 * <p>
 * This is the Boat class that represents a single boat in the club's fleet.
 * Each boat has a type (POWER or SAIL), name, year, make/model, length, purchase price, and cumulative expenses
 * </p>
 * There are 2 configurations:
 * Configuration 1: one with the command line parameters, given in the FleetData.csv file,
 * Configuration 2: "normal" with no command line parameters
 * </p>
 *
 * @author Juliana Geyer-Kim
 * * @version 1.0
 * * @since 2025-11-17
 */


public class Boat implements Serializable {

    //Declare variables holding information about each boat
    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int length;
    private double purchasePrice;
    private double expenses;

    /**
     * Constructor that creates the boat object
     * pass the boat information when created
     *
     */

    public Boat(BoatType type, String name, int year, String makeModel, int length, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;

    }//end of constructor

    /**
     * gets the type of boat
     *
     */

    public BoatType getType() {
        return type;

    }//end of getType method


    /**
     * gets the name of the boat
     *
     */
    public String getName() {
        return name;

    }//end of getName method

    /**
     * gets the year
     *
     */
    public int getYear() {
        return year;

    }//end of getYear method

    /**
     * gets the make / model of the boat
     *
     */
    public String getMakeModel() {
        return makeModel;

    }//end of the getMakeModel method

    public int getLength() {
        return length;

    }//end of the getLength method

    /**
     * gets the purchase price for the boat
     *
     */
    public double getPurchasePrice() {

        return purchasePrice;
    }//end of getPurchasePrice method

    /**
     * gets the expenses for the boat
     *
     */
    public double getExpenses() {
        return expenses;

    }//end of getExpenses method


    /**
     * Try to spend money on this boat
     * The rule: We can't spend more than what we paid for the boat
     * <p>
     * Parameters: amount - how much money we want to spend
     * Returns: true if we can spend it, false if it's too much
     */
    public boolean addExpense(double amount) {
        double newTotal = expenses + amount;

        if (purchasePrice >= newTotal) {
            this.expenses += amount;
            return true;
        }//end of if statement
        else {

            return false;
        }//end of else statement

    }//end of addExpense method

    /**
     * Calculate how much money we have left to spend on this boat
     * Returns: The remaining money we're allowed to spend
     */
    public double getRemainingSpending() {
        return purchasePrice - expenses;
    }//end of getRemainingSpending method

    /**
     * toString - Turn this boat into a nicely formatted line of text for printing
     * Format example: "    SAILING Moon Glow            1973 Bristol     30' : Paid $  5500.00 : Spent $     0.00"
     * Returns: A formatted string
     */

    public String toString() {
        return String.format("    %-7s %-20s %4d %-12s %3d' : Paid $%9.2f : Spent $%9.2f",
                type.toString(), name, year, makeModel, length, purchasePrice, expenses);

    }//end of toString method

}//end of Boat class




