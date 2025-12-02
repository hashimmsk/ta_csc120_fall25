import java.io.Serializable;

/**
 * Represents a single boat in the fleet.
 * Each boat stores its type, name, year of manufacture, make/model, length,
 * purchase price, and total expenses.
 *
 * @see BoatType
 * @author Aakash Singh
 * @version 1.0
 */

public class SingleBoat implements Serializable {

    // ----------------------------------------------------------------------

    private final BoatType type;
    private final String name;
    private final int year;
    private final String makeModel;
    private final double length;
    private final double purchasePrice;
    private double expenses;

    // ----------------------------------------------------------------------

    /**
     * Constructs a SingleBoat object with the provided parameters.
     *
     * @param type The type of boat (SAILING or POWER)
     * @param name The name of the boat
     * @param year The year of manufacture
     * @param makeModel The make and model of the boat
     * @param length The length of the boat in feet
     * @param purchasePrice The purchase price of the boat
     */

    public SingleBoat(BoatType type, String name, int year, String makeModel,
                      double length, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    } //End of SingleBoat constructor

    // ----------------------------------------------------------------------

    /**
     * Adds an expense to the boat if it does not exceed the purchase price.
     *
     * @param amount The amount to spend on the boat
     * @return true if expense is allowed, false if exceeds purchase price
     */

    public boolean addExpense(double amount) {
        if (expenses + amount <= purchasePrice) {
            expenses += amount;
            return true;
        } else {
            return false;
        }
    } //End of addExpense method

    // ----------------------------------------------------------------------

    /**
     * Returns the remaining budget for expenses for this boat.
     *
     * @return Remaining amount that can be spent
     */
    public double remainingBudget() {
        return purchasePrice - expenses;
    } //End of remainingBudget method

    // ----------------------------------------------------------------------

    /**
     * Gets the type of the boat.
     *
     * @return Boat type
     */
    public BoatType getType() {
        return type;
    } //End of getType method

    // ----------------------------------------------------------------------

    /**
     * Gets the name of the boat.
     *
     * @return Boat name
     */
    public String getName() {
        return name;
    } //End of getName method

    // ----------------------------------------------------------------------

    /**
     * Gets the year of manufacture of the boat.
     *
     * @return Year of manufacture
     */
    public int getYear() {
        return year;
    } //End of getYear method

    // ----------------------------------------------------------------------

    /**
     * Gets the make and model of the boat.
     *
     * @return Make and model
     */
    public String getMakeModel() {
        return makeModel;
    } //End of getMakeModel method

    // ----------------------------------------------------------------------

    /**
     * Gets the length of the boat in feet.
     *
     * @return Boat length
     */
    public double getLength() {
        return length;
    } //End of getLength method

    // ----------------------------------------------------------------------

    /**
     * Gets the purchase price of the boat.
     *
     * @return Purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    } //End of getPurchasePrice method

    // ----------------------------------------------------------------------

    /**
     * Gets the total expenses of the boat.
     *
     * @return Total expenses
     */
    public double getExpenses() {
        return expenses;
    } //End of getExpenses method

    // ----------------------------------------------------------------------

    /**
     * Returns a formatted string representation of the boat.
     *
     * @return String describing the boat
     */
    @Override
    public String toString() {
        return String.format("%-7s %-20s %4d %-12s %3.0f' : Paid $%8.2f : Spent $%8.2f",
                type, name, year, makeModel, length, purchasePrice, expenses);
    } //End of toString method

} //End of SingleBoat class