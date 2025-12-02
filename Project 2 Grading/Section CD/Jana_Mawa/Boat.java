import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a boat in the fleet.
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

    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet, double purchasePrice) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
        this.year = year;
        this.makeModel = Objects.requireNonNull(makeModel);
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }

    // Getters & setters (expenses is mutatable)
    public BoatType getType() { return type; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public String getMakeModel() { return makeModel; }
    public int getLengthFeet() { return lengthFeet; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getExpenses() { return expenses; }

    /**
     * Add an expense to this boat. Caller must enforce policy before invoking if needed.
     * @param amount amount to add (assumed >= 0)
     */
    public void addExpense(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Expense cannot be negative");
        this.expenses += amount;
    }

    /**
     * Amount left that can be spent on this boat (purchasePrice - expenses)
     * @return remaining budget (>= 0)
     */
    public double remainingBudget() {
        double rem = purchasePrice - expenses;
        return rem >= 0 ? rem : 0.0;
    }

    @Override
    public String toString() {
        // Used by printing in Fleet; formatting done by Fleet.printReport for alignment.
        return String.format("%s,%s,%d,%s,%d,%.2f",
                type, name, year, makeModel, lengthFeet, purchasePrice);
    }
}
