import java.io.Serializable;
import java.util.Locale;

/**
 * Represents a boat in the fleet.
 *
 * Fields:
 * - type: SAILING or POWER
 * - name: boat name
 * - year: year of manufacture
 * - makeModel: make/model string
 * - lengthFeet: integer length in feet (up to 100)
 * - purchasePrice: double, less than 1,000,000
 * - spent: double, cumulative expenses
 */
public class Boat implements Serializable {
    private static final long serialVersionUID = 1L;

    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int lengthFeet;
    private double purchasePrice;
    private double spent;

    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.spent = 0.0;
    }

    /* Getters */
    public BoatType getType() { return type; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public String getMakeModel() { return makeModel; }
    public int getLengthFeet() { return lengthFeet; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getSpent() { return spent; }

    /**
     * Check whether the requested amount can be spent without exceeding purchasePrice.
     */
    public boolean canSpend(double amount) {
        return (spent + amount) <= purchasePrice + 1e-9; // tiny epsilon
    }

    /**
     * Spend the amount (assumes caller checked canSpend)
     */
    public void spend(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Negative amount");
        this.spent += amount;
    }

    /**
     * Return remaining allowance (purchasePrice - spent)
     */
    public double remainingAllowance() {
        return purchasePrice - spent;
    }

    /**
     * Return a CSV line representing this boat (spent is not part of CSV format)
     */
    public String toCSVLine() {
        return String.format("%s,%s,%d,%s,%d,%.2f",
                type.name(), name, year, makeModel, lengthFeet, purchasePrice);
    }

    /**
     * Return a nicely formatted single-line report (matching sample roughly)
     */
    public String toFormattedString() {
        // Example target:
        // "    SAILING Moon Glow            1973 Bristol     30' : Paid $  5500.00 : Spent $     0.00"
        // We'll format fields with fixed widths.
        String typeStr = String.format("%-7s", type.name());
        String nameStr = String.format("%-20s", name);
        String yearMake = String.format("%4d %-10s", year, makeModel);
        String lengthStr = String.format("%3d'", lengthFeet);
        String paidStr = String.format("%10.2f", purchasePrice);
        String spentStr = String.format("%10.2f", spent);

        return String.format("    %s %s %s %6s : Paid $%s : Spent $%s",
                typeStr, nameStr, yearMake, lengthStr, paidStr, spentStr);
    }

    /**
     * Compare names case-insensitively for find/removal convenience.
     */
    public boolean nameEqualsIgnoreCase(String otherName) {
        return this.name.equalsIgnoreCase(otherName);
    }
}
