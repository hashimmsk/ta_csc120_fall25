import java.io.Serializable;

/**
 * Represents a single boat with details such as type, name, year, make/model, length, purchase price, and expenses.
 */
public class Boat implements Serializable {

    /**
     * Enum for boat types in the fleet
     */
    public enum BoatType {
        POWER,
        SAILING
    }

    /** Type of boat (POWER or SAIL) */
    private BoatType type;
    /** Name of the boat */
    private String name;
    /** Year of manufacture */
    private int year;
    /** Make or model of the boat */
    private String make;
    /** Length of the boat in feet */
    private int length;
    /** Purchase price of the boat */
    private double purchasePrice;
    /** Expenses spent on the boat */
    private double expenses;

    /**
     * Default constructor. Initializes all fields to default values.
     */
    public Boat() {
        this.type = null;
        this.name = "";
        this.year = 0;
        this.make = "";
        this.length = 0;
        this.purchasePrice = 0;
        this.expenses = 0;
    } // End of Default constructor

    /**
     * Constructs a new Boat object with all fields initialized.
     * @param type              Type of the boat (POWER or SAIL)
     * @param name              Name of the boat
     * @param year              Year of manufacture
     * @param make              Make or model of the boat
     * @param length            Length of the boat in feet
     * @param purchasePrice     Purchase price of the boat
     * @param expenses          Expenses already spent on the boat
     */
    public Boat(BoatType type, String name, int year, String make, int length, double purchasePrice, double expenses) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.make = make;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    } // End of initialized constructor

    /**
     * Returns the name of the boat.
     *
     * @return Boat name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the purchase price of the boat.
     *
     * @return Purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Returns the total expenses spent on the boat.
     *
     * @return Total expenses
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Adds an expense to the boat.
     *
     * @param amount Amount to add to expenses
     */
    public void addExpense(double amount) {
        this.expenses += amount;
    }

    /**
     * Returns a formatted string representing the boat's details.
     *
     * @return Formatted string with boat type, name, year, make/model, length, price, and expenses
     */
    @Override
    public String toString() {
        return String.format("%-8s %-15s %4d %-10s %3d' : Paid $%10.2f : Spent $%10.2f",
                type, name, year, make, length, purchasePrice, expenses);
    }


} // End of Boat class
