import java.io.Serializable;

/**
 * The Boat class represents ONE boat in the sailing club's fleet.
 *
 * Each Boat object stores:
 * - the type of boat (sailing or power)
 * - the name of the boat
 * - the year it was made
 * - the make and model
 * - the length in feet
 * - how much the club paid for it (purchase price)
 * - how much the club has spent on it so far (expenses)
 *
 * This class implements Serializable so that boats can be written
 * to a .db file and loaded again later.
 */
public class Boat implements Serializable {

    /** The type of this boat (SAILING or POWER). */
    private BoatType type;

    /** The name of this boat. */
    private String name;

    /** The year this boat was manufactured. */
    private int year;

    /** The make and model of this boat. */
    private String makeModel;

    /** The length of this boat in feet. */
    private int lengthFeet;

    /** The amount the club originally paid to buy this boat. */
    private double purchasePrice;

    /** The total amount spent maintaining this boat so far. */
    private double expenses;

    /**
     * Creates a new Boat with the given details.
     * When a Boat is first created, its expenses start at 0.0.
     *
     * @param type          the type of the boat (sailing or power)
     * @param name          the name of the boat
     * @param year          the year the boat was manufactured
     * @param makeModel     the make and model of the boat
     * @param lengthFeet    the length of the boat in feet
     * @param purchasePrice the amount paid to buy the boat
     */
    public Boat(BoatType type,
                String name,
                int year,
                String makeModel,
                int lengthFeet,
                double purchasePrice) {

        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;  // no money spent yet when we first buy the boat
    }

    /**
     * Returns the name of this boat.
     * This is used to find the boat by name when removing or adding expenses.
     *
     * @return the boat's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns how much the club originally paid for this boat.
     *
     * @return the purchase price of the boat
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Returns the total amount spent on this boat so far.
     *
     * @return the total expenses for this boat
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Adds a new expense amount to this boat.
     * This does NOT check any rules about spending limits.
     * The caller (the main system) is responsible for checking limits.
     *
     * @param amount the amount of money to add to the expenses
     */
    public void addExpense(double amount) {
        expenses += amount;
    }

    /**
     * Returns a nicely formatted line of text that describes this boat.
     * This is used when printing the fleet report.
     *
     * The formatting uses String.format to line up all the values so it matches
     * the sample output in the assignment as closely as possible.
     *
     * @return a formatted string containing this boat's information
     */
    @Override
    public String toString() {
        return String.format(
                "%8s %-20s %4d %-12s %3d' : Paid $ %9.2f : Spent $ %9.2f",
                type,
                name,
                year,
                makeModel,
                lengthFeet,
                purchasePrice,
                expenses
        );
    }
}



