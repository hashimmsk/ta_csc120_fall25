import java.io.Serializable;

/**
 * Represents a boat in the fleet management system
 */
public class Boat implements Serializable {
    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int length;
    private double purchasePrice;
    private double expenses;

    /**
     * Constructor for creating a boat object
     * @param type The type of boat (SAILING or POWER)
     * @param name The name of the boat
     * @param year The year of manufacture
     * @param makeModel The make/model of the boat
     * @param length The length in feet
     * @param purchasePrice The purchase price
     */
    public Boat(BoatType type, String name, int year, String makeModel, int length, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }

    // Getters
    public String getName() { return name; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getExpenses() { return expenses; }
    public BoatType getType() { return type; }
    public int getYear() { return year; }
    public String getMakeModel() { return makeModel; }
    public int getLength() { return length; }

    /**
     * Adds an expense to the boat if permitted by policy
     * @param amount The amount to spend
     * @return true if expense was authorized, false otherwise
     */
    public boolean addExpense(double amount) {
        if (expenses + amount <= purchasePrice) {
            expenses += amount;
            return true;
        }
        return false;
    }

    /**
     * Checks if more money can be spent on this boat
     * @return true if spending is allowed, false otherwise
     */
    public boolean canSpendMore() {
        return expenses < purchasePrice;
    }

    /**
     * Returns the remaining amount that can be spent on this boat
     * @return The remaining spendable amount
     */
    public double getRemainingSpend() {
        return purchasePrice - expenses;
    }

    /**
     * String representation of the boat for display
     * @return Formatted string showing boat details
     */
    @Override
    public String toString() {
        return String.format("    %-7s %-20s %4d %-12s %2d' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, year, makeModel, length, purchasePrice, expenses);
    }

    /**
     * CSV representation for adding new boats
     * @return CSV formatted string
     */
    public String toCSV() {
        return String.format("%s,%s,%d,%s,%d,%.2f",
                type, name, year, makeModel, length, purchasePrice);
    }
}