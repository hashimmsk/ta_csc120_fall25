import java.io.Serializable;

/**
 * Represents a boat with its characteristics, purchase details, and expenses.
 * Implements {@link Serializable} to allow object serialization.
 *
 * @author Muhammad Ahsan Saleem
 */
public class Boat implements Serializable {

    /**
     * Enum representing the type of boat. Either SAILING or POWER.
     */
    public enum BoatType {SAILING, POWER}

    // Data members

    /** The type of boat (SAILING or POWER). */
    private BoatType type;
    /** The year the boat was manufactured. */
    private short yearOfManufacture; // Short since year has a smaller value than the maximum value short can store
    /** The make and model of the boat. */
    private String makeModel;
    /** The length of the boat in feet. */
    private byte length; // Byte since the maximum length is 100 feet, and byte can store up to 127
    /** The original purchase price of the boat. */
    private double purchasePrice;
    /** The name assigned to the boat. */
    private String name;
    /** The total expenses spent on the boat. */
    private double expenses;

    // Methods

    /**
     * Constructs a Boat object with the specified attributes.
     *
     * @param type the type of the boat (SAILING or POWER)
     * @param name the name of the boat
     * @param yearOfManufacture the year the boat was manufactured
     * @param makeModel the make and model of the boat
     * @param length the length of the boat in feet
     * @param purchasePrice the price paid to purchase the boat
     */
    public Boat(BoatType type, String name, short yearOfManufacture, String makeModel, byte length, double purchasePrice) {

        this.type = type;
        this.name = name;
        this.yearOfManufacture = yearOfManufacture;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;

    } // end of the constructor

    // Getter and setter methods

    /**
     * Gets the name of the boat.
     *
     * @return the boat's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the purchase price of the boat.
     *
     * @return the price paid for the boat
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Gets the total expenses spent on the boat.
     *
     * @return the total expenses
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Sets the total expenses for the boat.
     *
     * @param expenses the new expenses amount
     */
    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    /**
     * Returns a formatted string representation of the boat's details.
     *
     * @return a formatted string describing the boat
     */
    public String toString() {

        // Output is formatted in a way to align each printed boat
        return String.format("%-7s %-20s %4d %-12s %3d' : Paid $ %10.2f : Spent $ %10.2f",
                type, name, yearOfManufacture, makeModel, length, purchasePrice, expenses);

    } // end of the toString method

} // end of the Boat class
