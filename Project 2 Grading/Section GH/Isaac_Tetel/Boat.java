import java.io.Serializable;

/**
 * Represents a single boat in the fleet, including type, name, year,
 * make/model, length, purchase price, and maintenance expenses.
 *
 * @author Isaac Tetel
 * @version 1
 * @see BoatType
 */
public class Boat implements Serializable {

    /** The type of the boat (sailing or power). */
    private BoatType type;

    /** The name of the boat. */
    private String name;

    /** The year the boat was manufactured. */
    private int year;

    /** The make and model of the boat. */
    private String makeModel;

    /** The length of the boat in feet. */
    private int lengthFeet;

    /** The purchase price of the boat. */
    private double purchasePrice;

    /** The total maintenance expenses spent on the boat. */
    private double expenses;

    /**
     * Constructs a new Boat object with the given attributes.
     *
     * @param t Boat type
     * @param n Boat name
     * @param y Year of manufacture
     * @param mm Make/model description
     * @param len Length in feet
     * @param price Purchase price of the boat
     */
    public Boat(BoatType t, String n, int y, String mm, int len, double price) {
        type = t;
        name = n;
        year = y;
        makeModel = mm;
        lengthFeet = len;
        purchasePrice = price;
        expenses = 0.0;
    }

    /**
     * Returns the name of the boat.
     *
     * @return Boat name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the purchase price of the boat.
     *
     * @return Purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Returns the total expenses spent on the boat.
     *
     * @return Current expenses
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Attempts to add an expense to the boat, but only if doing so
     * does not exceed the purchase price.
     *
     * @param amount Amount of the expense to add
     * @return true if the expense is allowed, false otherwise
     */
    public boolean addExpense(double amount) {
        if (expenses + amount <= purchasePrice) {
            expenses = expenses + amount;
            return true;
        }
        return false;
    }

    /**
     * Returns a formatted string describing the boat.
     *
     * @return A formatted string representation of the boat
     */
    public String toString() {
        return String.format(
                "%-7s %-20s %4d %-12s %3d' : Paid $ %9.2f : Spent $ %9.2f",
                type, name, year, makeModel, lengthFeet, purchasePrice, expenses
        );
    }
}