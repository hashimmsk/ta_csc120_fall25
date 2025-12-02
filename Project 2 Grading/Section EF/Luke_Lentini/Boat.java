import java.io.Serializable;
import java.util.Objects;

/**
 * Boat.java
 * Represents a single boat in the fleet.
 */
public class Boat implements Serializable {
    private static final long serialVersionUID = 1L;

    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int lengthFeet;
    private double purchasePrice;
    private double expenses;

    /**
     * Construct a Boat.
     */
    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet, double purchasePrice) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
        this.year = year;
        this.makeModel = Objects.requireNonNull(makeModel);
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }

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
     * Check if a requested expense amount is allowable (does not exceed purchase price).
     * @param amount requested
     * @return true if permitted
     */
    public boolean canSpend(double amount) {
        return amount >= 0 && (expenses + amount) <= purchasePrice;
    }

    /**
     * Apply an approved expense to this boat.
     * @param amount amount to add to expenses (must be allowed)
     * @throws IllegalArgumentException if amount negative or would exceed purchasePrice
     */
    public void spend(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be nonnegative");
        if (!canSpend(amount)) {
            double left = purchasePrice - expenses;
            throw new IllegalArgumentException(String.format("Expense not permitted, only $%.2f left to spend.", left));
        }
        expenses += amount;
    }

    /**
     * Produce a CSV representation (like the input format) without expenses.
     * TYPE,Name,Year,Make,Length,PurchasePrice
     */
    public String toCsv() {
        return String.format("%s,%s,%d,%s,%d,%.2f",
                type.name(), name, year, makeModel, lengthFeet, purchasePrice);
    }

    /**
     * Produce a formatted report line similar to the sample output.
     */
    public String toReportLine() {
        // Layout approximating the sample:
        // TYPE (left 7), name (left 20), year, make (left 12), length right 3' : Paid $ 12345.67 : Spent $  123.45
        String typeStr = String.format("%-7s", type.name());
        String nameStr = String.format("%-20s", name);
        String yearMake = String.format("%4d %-12s", year, makeModel);
        String lenStr = String.format("%3d'", lengthFeet);
        String paidStr = String.format("Paid $ %8.2f", purchasePrice);
        String spentStr = String.format("Spent $ %8.2f", expenses);
        return String.format("    %s %s %s %s : %s : %s",
                typeStr, nameStr, yearMake, lenStr, paidStr, spentStr);
    }
}
