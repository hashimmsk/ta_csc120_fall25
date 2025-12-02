import java.io.Serializable;

public class Boat implements Serializable {
    private String type; // "SAILING" or "POWER"
    private String name;
    private int year;
    private String make;
    private int lengthFeet;
    private double amountPaid;
    private double amountSpent;

    public Boat(String type, String name, int year, String make, int lengthFeet, double paid, double spent) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.make = make;
        this.lengthFeet = lengthFeet;
        this.amountPaid = paid;
        this.amountSpent = spent;
    }

    public String getName() {
        return name;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public double getRemainingBudget() {
        return amountPaid - amountSpent;
    }

    public boolean addExpense(double amt) {
        if (amt <= getRemainingBudget()) {
            amountSpent += amt;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        // Note: spacing tuned to roughly match sample output
        return String.format(
                "%-7s %-20s %4d %-11s %3d' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, year, make, lengthFeet, amountPaid, amountSpent
        );
    }
}
