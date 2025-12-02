import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Represents a single boat with type, name, year, model, length, purchase price, and expenses.
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
     * Constructor to create a new Boat.
     */
    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }

    public BoatType getType() { return type; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public String getMakeModel() { return makeModel; }
    public int getLengthFeet() { return lengthFeet; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getExpenses() { return expenses; }

    /**
     * Returns true if adding amount does not exceed purchase price.
     */
    public boolean canSpend(double amount) {
        return expenses + amount <= purchasePrice + 1e-9;
    }

    /** Adds an expense to this boat. */
    public void addExpense(double amount) {
        expenses += amount;
    }

    /** Returns remaining budget (purchasePrice - expenses). */
    public double remainingBudget() {
        return purchasePrice - expenses;
    }

    /** Returns a formatted string of boat information. */
    @Override
    public String toString() {
        DecimalFormat money = new DecimalFormat("#,##0.00");
        String typeStr = (type == BoatType.POWER) ? "POWER   " : "SAILING ";
        return String.format("    %s %-20s %4d %-12s %3d' : Paid $ %8s : Spent $ %8s",
                typeStr,
                String.format("%-20s", name),
                year,
                String.format("%-10s", makeModel),
                lengthFeet,
                money.format(purchasePrice),
                money.format(expenses)
        );
    }
}