/**
 * Represents a boat in the fleet.
 */

public class Boat {

    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int lengthFeet;
    private double purchasePrice;
    private double expenses;

    /**
     * Full constructor
     * @param type
     * @param name
     * @param year
     * @param makeModel
     * @param lengthFeet
     * @param purchasePrice
     * @param expenses
     */

    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet, double purchasePrice, double expenses) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet, double purchasePrice) {
        this(type, name, year, makeModel, lengthFeet, purchasePrice, 0.0);

    }

    public String getName() {
        return name;
    }

    public double getExpenses() {
        return expenses;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Attempts to add expense if allowed by Commodore policy.
     * @param amt
     * @return
     */

    public boolean addExpense(double amt) {
        if (expenses + amt <= purchasePrice) {
            expenses += amt;
            return true;
        }
        return false;
    }

    /**
     * Formats the output as specified.
     * @return
     */

    @Override
    public String toString() {
        return String.format("%8s %-18s %4d %-12s %3d' : Paid $ %9.2f : Spent $ %9.2f", type, name, year, makeModel, lengthFeet, purchasePrice, expenses);
    }

    public String toCSV() {
        return type + "," + name + "," + year + "," + makeModel + "," + lengthFeet + "," + purchasePrice + "," + expenses;
    }

}



