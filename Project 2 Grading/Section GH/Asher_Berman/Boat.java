import java.io.Serializable;
import java.util.Locale;

/**
 * Represents a single watercraft in the fleet, capable of
 * serialization (for DB) and CSV conversion.
 * Author: Asher Berman
 * Version: 2.3
 */
public class Boat implements Serializable {

    // Unique ID for serialization
    private static final long serialVersionUID = 1L;

    // Define boat types
    public enum BoatType {
        POWER, SAILING
    }

    private final BoatType type;
    private final String name;
    private final int year;
    private final String make;
    private final int lengthFeet;
    private final double purchasePrice;
    private double totalExpenses; // Tracks expenses added after purchase

    private static final double EXPENSE_CAP_FACTOR = 1; // Max expense is 50% of purchase price

    /**
     * Full Constructor
     */
    public Boat(BoatType type, String name, int year, String make, int lengthFeet, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.make = make;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.totalExpenses = 0.0;
    }//End of Boat creation

    // --- GETTERS ---
    public String getName() {
        return name;
    }//End of getName method

    public double getPurchasePrice() {
        return purchasePrice;
    }//End of getPurchasePrice method

    public double getTotalExpenses() {
        return totalExpenses;
    }//End of getTotalExpenses method

    /**
     * Calculates the remaining budget for repairs/expenses.
     * Budget is capped at EXPENSE_CAP_FACTOR * purchasePrice.
     * @return remaining budget
     */
    public double getRemainingBudget() {
        double maxExpense = purchasePrice * EXPENSE_CAP_FACTOR;
        return Math.max(0, maxExpense - totalExpenses);
    }//End of getRemainingBudget method

    // --- LOGIC ---

    /**
     * Adds an expense if it does not exceed the total allowed budget.
     * @param amount the expense amount
     * @return true if the expense was added, false if it exceeded the budget
     */
    public boolean addExpense(double amount) {
        if (amount <= 0) return false;

        double remaining = getRemainingBudget();
        if (amount <= remaining) {
            this.totalExpenses += amount;
            return true;
        }//End of if check
        return false;
    }//End of addExpense method

    /**
     * Sets the total expenses, used only during data loading.
     */
    public void setTotalExpensesForLoad(double loadedExpenses) {
        this.totalExpenses = loadedExpenses;
    }//End of setTotalExpensesForLoad method

    // --- FORMATTING ---

    /**
     * Creates a Boat object from a CSV string.
     * Format: TYPE,Name,Year,Make,Length,Price
     * Example: POWER,Big Brother,2019,Mako,20,12989.56
     * @param csvLine The line to parse
     * @return a new Boat instance, or null if parsing fails
     */
    public static Boat fromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length != 6) return null;

            BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase(Locale.ROOT));
            String name = parts[1].trim();
            int year = Integer.parseInt(parts[2].trim());
            String make = parts[3].trim();
            int length = Integer.parseInt(parts[4].trim());
            double price = Double.parseDouble(parts[5].trim());

            return new Boat(type, name, year, make, length, price);

        } catch (Exception e) {
            return null;
        }//End of exception handling
    }//End of fromCSV method

    /**
     * Converts the Boat object to a CSV string for saving.
     */
    public String toCSV() {
        return String.format(Locale.ROOT, "%s,%s,%d,%s,%d,%.2f,%.2f",
                type.name(), name, year, make, lengthFeet, purchasePrice, totalExpenses);
    }//End of toCSV method

    /**
     * Formats the boat data for the fleet report (P-option).
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "    %-7s %-20s %d %-10s %3d' : Paid $ %10.2f : Spent $ %10.2f",
                type.name(), name, year, make, lengthFeet, purchasePrice, totalExpenses
        );
    }//End of toString method
}//End of Boat class