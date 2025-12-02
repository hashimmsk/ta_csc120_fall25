import java.io.Serializable;

/**
 * Represents a boat with details and expense tracking.
 */
public class Boat implements Serializable {

    // instance variables
    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int length;
    private double purchasePrice;
    private double expenses;

    /**
     * Constructor for Boat
     * @param type the type of boat (POWER or SAILING)
     * @param name the name of the boat
     * @param year the year of manufacture
     * @param makeModel the make and model
     * @param length the length in feet
     * @param purchasePrice the purchase price
     */
    public Boat(BoatType type, String name, int year, String makeModel, int length, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }

    /**
     * Gets the boat type
     * @return the boat type
     */
    public BoatType getType() {
        return type;
    }

    /**
     * Gets the boat name
     * @return the boat name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the year of manufacture
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the make and model
     * @return the make/model
     */
    public String getMakeModel() {
        return makeModel;
    }

    /**
     * Gets the length in feet
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the purchase price.
     * @return the purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Gets the total expenses
     * @return the expenses
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Adds an expense if it does not exceed the purchase price
     * @param amount the amount to spend
     * @return true if authorized, false otherwise
     */
    public boolean addExpense(double amount) {
        if (expenses + amount <= purchasePrice) {
            expenses += amount;
            return true;
        }
        return false;
    }

    /**
     * Gets the amount left to spend
     * @return the remaining amount
     */
    public double getAmountLeftToSpend() {
        return purchasePrice - expenses;
    }
}