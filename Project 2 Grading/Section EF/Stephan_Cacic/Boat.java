import java.io.Serializable;
/**
 * This file represents a single boat stored in the fleet.
 * <p>
 * Every boat will have a name, year, type, size, model
 * as well as price and total expenses for said boat
 * Boats are immutable except for their accumulating expense total
 *
 * @author Stephan Cacic
 */
public class Boat implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Type of boat (either SAILING or POWER) */
    private final BoatType type;

    /** Name of the boat */
    private final String name;

    /** Year the boat was manufactured*/
    private final int year;

    /** Manufacturer and model of the boat */
    private final String makeModel;

    /** Length of the boat in feet */
    private final int lengthFeet;

    /** Price originally paid for the boat */
    private final double purchasePrice;

    /** Total expenses recorded for this boat */
    private double expenses;

    /**
     * Given the provided information, a new boat is constructed
     *
     * @param type          the boat's type (SAILING or POWER)
     * @param name          the name of the boat
     * @param year          the manufacturing year
     * @param makeModel     manufacturer and model description
     * @param lengthFeet    length of the boat in feet
     * @param purchasePrice the initial price paid for the boat
     * @param expenses      starting expense total (usually 0)
     */
    public Boat(BoatType type, String name, int year, String makeModel,
                int lengthFeet, double purchasePrice, double expenses) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    /** @return type of boat */
    public BoatType getType() {
        return type;
    }

    /** @return name of boat */
    public String getName() {
        return name;
    }

    /** @return manufacturing year of boat */
    public int getYear() {
        return year;
    }

    /** @return make and model of boat*/
    public String getMakeModel() {
        return makeModel;
    }

    /** @return length in feet of boat*/
    public int getLengthFeet() {
        return lengthFeet;
    }

    /** @return the original purchase price of the boat */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /** @return the total expenses recorded for the boat */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Adds a new expense to the boat’s total
     * @param amount the expense amount to add
     */
    public void addExpense(double amount) {
        expenses += amount;
    }

    /**
     * Returns a formatted string showing all the boat’s information
     * in aligned columns for printing in the fleet report
     * @return formatted row of boat details
     */
    @Override
    public String toString() {
        return String.format(
                " %-7s %-15s %-4d %-12s %3d' : Paid $ %10.2f : Spent $ %10.2f",
                type.name(),
                name,
                year,
                makeModel,
                lengthFeet,
                purchasePrice,
                expenses
        );
    }
}