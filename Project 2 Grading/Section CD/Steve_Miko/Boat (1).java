import java.io.Serializable;

/**
 * Represents a single boat within the fleet.
 * This class stores the boat's specifications (type, name, year, etc.) and manages
 * the financial data regarding its purchase price and accumulated expenses.
 * It implements Serializable to allow fleet data to be saved to a file.
 *
 * @author Steve Mikolajewski
 * @version 1.0
 * @see Main
 */
public class Boat implements Serializable {
    /**
     * The category of the boat (e.g., SAILING or POWER).
     * @see Main.boatType
     */
    private Main.boatType type;

    /**
     * The unique name of the boat.
     * @see String
     */
    private String name;

    /**
     * The year the boat was manufactured.
     * @see Integer
     */
    private int year;

    /**
     * The make and model string of the boat.
     * @see String
     */
    private String makeModel;

    /**
     * The length of the boat in feet.
     * @see Integer
     */
    private int length;

    /**
     * The original purchase price of the boat, which also acts as the expense limit.
     * @see Double
     */
    private double purchasePrice;

    /**
     * The running total of maintenance expenses incurred by the boat.
     * @see Double
     */
    private double expenses;

    /**
     * Version identifier for Serializable class to ensure compatibility.
     * @see Serializable
     */
    private static final long serialVersionUID = 1L;

    // Constructor
    /**
     * Constructs a new Boat object with the specified details.
     * Initializes the accumulated expenses to zero.
     *
     * @param type The type of boat (SAILING or POWER)
     * @param name The name of the boat
     * @param year The year of manufacture
     * @param makeModel The manufacturer and model information
     * @param length The length of the boat in feet
     * @param purchasePrice The cost to buy the boat
     */
    public Boat(Main.boatType type, String name, int year, String makeModel, int length, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    } // end of Boat constructor

    /**
     * Retrieves the name of the boat.
     *
     * @return The boat's name
     */
    public String getName() {
        return name;
    } // end of getName method

    /**
     * Retrieves the original purchase price of the boat.
     *
     * @return The purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    } // end of purchasePrice method

    /**
     * Retrieves the total expenses currently accrued by the boat.
     *
     * @return The total expenses
     */
    public double getExpenses() {
        return expenses;
    } // end of getExpenses method

    /**
     * Adds a new amount to the boat's total expenses.
     * Note: This method does not check the budget; use canSpend() to verify first.
     *
     * @param expense The dollar amount to add to expenses
     */
    public void addExpense(double expense) {
        this.expenses += expense;
    } // end of addExpense method

    /**
     * Checks if adding a new expense would stay within the budget.
     * The budget policy dictates that total expenses cannot exceed the purchase price.
     *
     * @param expense The proposed expense amount
     * @return true if the expense is allowed, false if it exceeds the limit
     */
    public boolean canSpend(double expense) {
        if (this.expenses + expense <= this.purchasePrice) {
            return true;
        }
        return false;
    } // end of canSpend method

    /**
     * Calculates the remaining amount that can be spent on this boat.
     * Defined as Purchase Price minus Current Expenses.
     *
     * @return The remaining budget available
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    } // end of getRemainingBudget method

    /**
     * Returns a formatted string representation of the boat for the fleet report.
     * Formats fields into specific column widths to ensure table alignment.
     *
     * @return A formatted String containing boat details and financials
     */
    @Override
    public String toString() {
        return String.format("    %-7s %-16s %4d %-10s %3d' : Paid $ %9.2f : Spent $ %9.2f",
                this.type,
                this.name,
                this.year,
                this.makeModel,
                this.length,
                this.purchasePrice,
                this.expenses);
    } // end of toString method
} // end of Boat class