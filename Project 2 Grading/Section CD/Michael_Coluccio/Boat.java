import java.io.Serializable;

/**
 * represents a single boat in the fleet.
 */
public class Boat implements Serializable {
    private static final long serialVersionUID = 1L;

    private BoatType type;
    private String name;
    private short year;
    private String makeModel;
    private byte lengthFeet;
    private double purchasePrice;
    private double expenses;

    /**
     * Construct a boat
     */
    public Boat(BoatType type, String name, short year, String makeModel, byte lengthFeet, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }

    // Getters and setters
    public BoatType getType() { return type; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public String getMakeModel() { return makeModel; }
    public int getLengthFeet() { return lengthFeet; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getExpenses() { return expenses; }

    /**
     * Attempt to add an expense. Respects policy: total expenses cannot exceed purchase price.
     * @param amount amount to add (assumed > 0)
     * @return true if authorized and added, false if it would exceed purchasePrice
     */
    public boolean addExpense(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Expense amount must be non-negative");
        if (expenses + amount <= purchasePrice) {
            expenses += amount;
            return true;
        } else {
            return false;
        }
    }

    /**
     * How much remaining can be spent on this boat.
     */
    public double remainingBudget() {
        return purchasePrice - expenses;
    }

    /**
     * Build the formatted string for the fleet report consistent with the example.
     */
    public String toReportString() {
        String typeStr = String.format("%-7s", type.toString());
        String nameStr = String.format("%-22s", name); // matches sample spacing
        String yearStr = String.format("%4d", year);
        String makeModelStr = String.format("%-12s", makeModel);
        String lengthStr = String.format("%2d'", lengthFeet);
        String paidStr = String.format("%8.2f", purchasePrice);
        String spentStr = String.format("%9.2f", expenses);
        return String.format("    %s %s %s %s %s : Paid $ %s : Spent $ %s",
                typeStr, nameStr, yearStr, makeModelStr,lengthStr, paidStr, spentStr);
    }



    @Override
    public String toString() {
        return "Boat{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", makeModel='" + makeModel + '\'' +
                ", lengthFeet=" + lengthFeet +
                ", purchasePrice=" + purchasePrice +
                ", expenses=" + expenses +
                '}';
    }
}
