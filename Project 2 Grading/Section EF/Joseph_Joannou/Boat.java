import java.io.Serializable;

/**
 * The Boat class represents a single boat in the club's fleet.
 * It stores basic information about the boat and how much
 * money has been spent on maintaining it.
 *
 * This class implements Serializable so Boat objects can be
 * saved to a file and loaded again in a later run.
 *
 * @author Joseph Joannou
 * @version 1.0
 * @since 2025-12-1
 */
public class Boat implements Serializable {
    private static final long serialVersionUID = 1L;

    private BoatType type;       // SAILING or POWER
    private String name;         // Boat name (ex. "Moon Glow")
    private int year;            // Year the boat was built
    private String makeModel;    // Make/model (ex. "Bristol")
    private int lengthFeet;      // Length in feet (up to 100)
    private double purchasePrice; // Original purchase price
    private double expenses;      // Money spent on maintenance so far


/**
 * Constructor used when a new boat is added (e.g., from CSV input).
 * Expenses start at 0.0 because it's a new boat.
 */
public Boat(BoatType type, String name, int year,
            String makeModel, int lengthFeet, double purchasePrice) {
    this.type = type;
    this.name = name;
    this.year = year;
    this.makeModel = makeModel;
    this.lengthFeet = lengthFeet;
    this.purchasePrice = purchasePrice;
    this.expenses = 0.0; // No expenses yet so 0.0
}  // end of Boat constructor (no expenses parameter)


/**
 * Constructor used when loading a boat from a saved file.
 * Allows expenses to be restored from previous runs.
 */
public Boat(BoatType type, String name, int year,
            String makeModel, int lengthFeet,
            double purchasePrice, double expenses) {
    this.type = type;
    this.name = name;
    this.year = year;
    this.makeModel = makeModel;
    this.lengthFeet = lengthFeet;
    this.purchasePrice = purchasePrice;
    this.expenses = expenses;
} // end of Boat constructor (with expenses parameter)


    //Getter methods
    public BoatType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getMakeModel() {
        return makeModel;
    }

    public int getLengthFeet() {
        return lengthFeet;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getExpenses() {
        return expenses;
    }


    /**
     * Adds an expense amount to this boat's total expenses.
     *
     * @param amount The amount to add (must be positive).
     */
    public void addExpense(double amount) {
        if (amount < 0) { // You cannot add a negative expense
            throw new IllegalArgumentException("Expense amount cannot be negative.");
        }
        this.expenses += amount;
    } // end of addExpense method


    /**
     * Returns how much money is left to spend before reaching
     * the boat's purchase price limit.
     *
     * @return remaining budget (purchasePrice - expenses)
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    } // end of getRemainingBudget method


    /**
     * Returns a simple text description of the boat.
     * This is used mainly for debugging or basic display.
     */
    @Override
    public String toString() {
        return type + " " + name + " (" + year + ") - " + makeModel + ", " +
                lengthFeet + "ft, Paid: $" + String.format("%.2f", purchasePrice) +
                ", Spent: $" + String.format("%.2f", expenses);
    } // end of toString method


    /**
     * Returns a formatted line of text for this boat, used in the fleet report.
     * The spacing is chosen to roughly match the sample output format.
     *
     * Example:
     *     SAILING Moon Glow            1973 Bristol     30' : Paid $  5500.00 : Spent $  3456.78
     *
     * @return a formatted String representing this boat
     */
    public String getReportLine() {
        // %-7s  = left-align type in 7 spaces
        // %-20s = left-align name in 20 spaces
        // %4d   = year in 4 spaces
        // %-10s = left-align make/model in 10 spaces
        // %3d   = length in 3 spaces, followed by '
        // %8.2f = money with width 8, 2 decimal places
        return String.format(
                "    %-7s %-20s %4d %-10s %3d' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, year, makeModel, lengthFeet, purchasePrice, expenses
        );
    } // end of getReportLine method


} // end of Boat class


