import java.io.Serializable;

/**
 * Boat represents one boat within the fleet.
 * Each boat has a type (SAILING or POWER), a name, a year of manufacture,
 * a make/model, a length in feet, a purchase price, and an amount of money
 * spent on it (expenses). Provides methods to track expenses and display
 * formatted boat information.
 *
 * @author sarahlaughlin
 * @version 1.0
 * @see BoatType
 */

public class Boat implements Serializable {

    private final BoatType typeOfBoat;
    private final String nameOfBoat;
    private final int year;
    private final String makeModel;
    private final int lengthFeet;
    private final double purchasePrice;
    private double expenses;

    /**
     * Construct a boat with zero expenses
     * @param typeOfBoat is the type of boat (SAILING or POWER)
     * @param nameOfBoat is the name of the boat
     * @param year is the year of manufacture
     * @param makeModel is the make or model name
     * @param lengthFeet is the length of the boat in feet
     * @param purchasePrice is the purchase price of the boat
     */
    //Constructor
    public Boat(BoatType typeOfBoat, String nameOfBoat, int year, String makeModel, int lengthFeet, double purchasePrice) {
        this.typeOfBoat = typeOfBoat;
        this.nameOfBoat = nameOfBoat;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0; // start with no expenses
    } // end of Boat constructor

    //nameOfBoat getter
    public String getNameOfBoat() {
        return nameOfBoat;
    } // end of nameOfBoat getter
    //purchasePrice getter
    public double getPurchasePrice() {
        return purchasePrice;
    } // end of purchasePrice getter
    //expenses getter
    public double getExpenses() {
        return expenses;
    } // end of expenses getter

    /**
     * Check to see if we can spend this amount of a boat.
     * @param amount is the amount we want to spend
     * @return true if total expenses are less than purchase price
     */
    public boolean canSpendOnBoat(double amount) {
        double newTotal = expenses + amount;
        return newTotal <= purchasePrice; // allow spending as long as <= purchase price
    } // end of canSpendOnBoat method

    /**
     * Attempt to add an expense to the boat.
     * @param amount is amount to add to the boat
     * @return true if the expense was able to be added, false if it is more than the purchase price
     */
    public boolean additionalExpense(double amount) {

        boolean okToSpend;

        okToSpend = canSpendOnBoat(amount);

        if (okToSpend) {
            expenses += amount;
        } // end if
        return okToSpend;

    } // end of additionalExpense method

    /**
     * Format this boat as one line in the fleet report
     * @return formatted string
     */

    public String toString() {
        String typeOfBoatString;
        String nameOfBoatString;
        String yearMakeString;
        String lengthString;
        String paidString;
        String spentString;
        String line;

        typeOfBoatString = String.format("%-7s", typeOfBoat);
        nameOfBoatString = String.format("%-20s", nameOfBoat);
        yearMakeString = String.format("%4d %-10s", year, makeModel);
        lengthString = String.format("%3d'", lengthFeet);
        paidString = String.format("%10.2f", purchasePrice);
        spentString = String.format("%10.2f", expenses);

        line = "    " + typeOfBoatString + " " + nameOfBoatString + " " + yearMakeString + " " + lengthString + " : Paid $ " + paidString + " : Spent $ " + spentString;
        return line;

    } // end of toString

} // end of the Boat class
