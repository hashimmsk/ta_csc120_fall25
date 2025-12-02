import java.io.Serializable;

/**
 * Represents a boat in the Coconut Grove Sailing Club fleet.
 * Tracks boat details including type, specifications, purchase price,
 * and maintenance expenses. Implements Serializable for database storage.
 *
 * @author Gen Li
 * @version 1.0
 */
public class Boat implements Serializable {

    // Instance variables
    private final BoatType type;
    private final String name;
    private final int year;
    private final String makeModel;
    private final int length;
    private final double purchasePrice;
    private double expenses;
    /**
     * Constructs a new Boat with all specifications.
     *
     * @param type          the type of boat (SAILING or POWER)
     * @param name          the name of the boat
     * @param year          the year of manufacture
     * @param makeModel     the make and model
     * @param length        the length in feet
     * @param purchasePrice the purchase price in dollars
     * @param expenses      the current maintenance expenses in dollars
     */
    public Boat(BoatType type, String name, int year, String makeModel,
                int length, double purchasePrice, double expenses) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    /**
     * Constructs a new Boat from a CSV formatted string.
     * Format: TYPE,Name,Year,Make,Length,Price[,Expenses]
     * Expenses defaults to 0.0 if not provided.
     *
     * @param csvLine the CSV formatted string containing boat data
     */
    public Boat(String csvLine) {
        String[] parts;

        parts = csvLine.split(",");
        this.type = BoatType.valueOf(parts[0].toUpperCase());
        this.name = parts[1];
        this.year = Integer.parseInt(parts[2]);
        this.makeModel = parts[3];
        this.length = Integer.parseInt(parts[4]);
        this.purchasePrice = Double.parseDouble(parts[5]);

        // Expenses is optional in CSV
        if (parts.length > 6) {
            this.expenses = Double.parseDouble(parts[6]);
        } else {
            this.expenses = 0.0;
        }
    }

    /**
     * Gets the name of the boat.
     *
     * @return the boat's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the purchase price of the boat.
     *
     * @return the purchase price in dollars
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Gets the total maintenance expenses for the boat.
     *
     * @return the total expenses in dollars
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Checks if a proposed expense amount is within the Commodore's policy.
     * The policy states that total expenses cannot exceed the purchase price.
     *
     * @param amount the proposed expense amount in dollars
     * @return true if the expense is permitted, false otherwise
     */
    public boolean canSpend(double amount) {
        return (expenses + amount) <= purchasePrice;
    }

    /**
     * Adds an expense amount to the boat's maintenance expenses.
     * This method should only be called after verifying with canSpend().
     *
     * @param amount the expense amount to add in dollars
     */
    public void addExpense(double amount) {
        expenses += amount;
    }

    /**
     * Calculates the remaining budget available for expenses.
     *
     * @return the remaining amount that can be spent
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    }

    /**
     * Returns a formatted string representation of the boat.
     * Format matches the fleet report display requirements.
     *
     * @return formatted string with boat details and financial information
     */
    @Override
    public String toString() {
        String typeStr;
        String nameStr;
        String makeStr;
        String lengthStr;
        String paidStr;
        String spentStr;

        typeStr = String.format("%-7s", type);
        nameStr = String.format("%-20s", name);
        makeStr = String.format("%-12s", makeModel);
        lengthStr = String.format("%3d'", length);
        paidStr = String.format("$ %9.2f", purchasePrice);
        spentStr = String.format("$ %9.2f", expenses);

        return String.format("    %s %s %4d %s %s : Paid %s : Spent %s",
                typeStr, nameStr, year, makeStr, lengthStr,
                paidStr, spentStr);
    }

    /**
     * Enum representing the type of boat.
     */
    public enum BoatType {
        /**
         * Sailing boat
         */
        SAILING,
        /**
         * Power/motor boat
         */
        POWER
    }
}
