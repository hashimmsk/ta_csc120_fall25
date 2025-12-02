import java.io.Serializable;
//========================================================================================================
/**
 * Represents a boat with details about its type, name, year of manufacture, make/model, length,
 * purchase price, and expenses. This class allows for managing expenses within the limits of the boat's
 * purchase price and includes functionality for displaying the boat's information.
 *
 * @author Hashim Shahzad Khan
 */
public class Boat implements Serializable {
    //----------------------------------------------------------------------------------------------------
    /**
     * The serial version UID for serialization compatibility
     */
    private static final long serialVersionUID = 1L;
    //----------------------------------------------------------------------------------------------------
    /**
     * The type of boat (SAILING or POWER)
     */
    private BoatType type;
    //----------------------------------------------------------------------------------------------------
    /**
     * The name of the boat
     */
    private String name;
    //----------------------------------------------------------------------------------------------------
    /**
     * The year the boat was manufactured
     */
    private int yearOfManufacture;
    //----------------------------------------------------------------------------------------------------
    /**
     * The make and model of the boat
     */
    private String makeModel;
    //----------------------------------------------------------------------------------------------------
    /**
     * The length of the boat in feet
     */
    private int lengthInFeet;
    //----------------------------------------------------------------------------------------------------
    /**
     * The purchase price of the boat
     */
    private double purchasePrice;
    //----------------------------------------------------------------------------------------------------
    /**
     * The expenses spent on the boat
     */
    private double expenses;
    //----------------------------------------------------------------------------------------------------
    /**
     * Default constructor that initializes the boat with default values.
     */
    public Boat() {

        type = BoatType.SAILING;
        name = "";
        yearOfManufacture = 0;
        makeModel = "";
        lengthInFeet = 0;
        purchasePrice = 0.0;
        expenses = 0.0;

    } // end of the default constructor
    //----------------------------------------------------------------------------------------------------
    /**
     * Constructor that initializes the boat with specified values.
     * @param boatType The type of the boat (SAILING or POWER)
     * @param nameOfBoat The name of the boat
     * @param year The year the boat was manufactured
     * @param model The make/model of the boat
     * @param length The length of the boat in feet
     * @param price The purchase price of the boat
     */
    public Boat(BoatType boatType, String nameOfBoat, int year, String model, int length, double price) {

        type = boatType;
        name = nameOfBoat;
        yearOfManufacture = year;
        makeModel = model;
        lengthInFeet = length;
        purchasePrice = price;
        expenses = 0.0;

    } // end of the constructor with parameters
    //----------------------------------------------------------------------------------------------------
    /**
     * Returns the name of the boat.
     * @return The name of the boat
     */
    public String getName() {

        return name;

    } // end of the getName method
    //----------------------------------------------------------------------------------------------------
    /**
     * Checks if an expense can be added to the boat without exceeding its purchase price.
     * @param amount The amount to be added as an expense
     * @return True if the expense can be added, otherwise false
     */
    public boolean canSpend(double amount) {

        return (expenses + amount) <= purchasePrice;

    } // end of the canSpend method
    //----------------------------------------------------------------------------------------------------
    /**
     * Adds an expense to the boat, ensuring it does not exceed the boat's purchase price.
     * @param amount The amount to be added as an expense
     * @throws IllegalArgumentException If the expense exceeds the allowed limit
     */
    public void addExpense(double amount) {

        if (canSpend(amount)) {
            expenses += amount;
        } else {
            throw new IllegalArgumentException("Expense exceeds the allowed limit!");
        }

    } // end of the addExpense method
    //----------------------------------------------------------------------------------------------------
    /**
     * Returns the total expenses spent on the boat.
     * @return The total expenses of the boat
     */
    public double getExpenses() {

        return expenses;

    } // end of the getExpenses method
    //----------------------------------------------------------------------------------------------------
    /**
     * Returns the purchase price of the boat.
     * @return The purchase price of the boat
     */
    public double getPurchasePrice() {

        return purchasePrice;

    } // end of the getPurchasePrice method
    //----------------------------------------------------------------------------------------------------
    /**
     * Returns a string representation of the boat, including details about the boat's type, name,
     * year of manufacture, make/model, length, purchase price, and expenses.
     * @return A formatted string representing the boat
     */
    @Override
    public String toString() {

        return String.format("%-8s %-20s %4d %-12s %4d' : Paid $%10.2f : Spent $%10.2f",
                type, name, yearOfManufacture, makeModel, lengthInFeet, purchasePrice, expenses);

    } // end of the toString method
    //----------------------------------------------------------------------------------------------------
    /**
     * Enum that represents the two types of boats: SAILING and POWER.
     * This enum is used to distinguish between sailing boats and power boats in the FleetManagement system.
     */
    public enum BoatType {

        /**
         * Represents a sailing boat.
         */
        SAILING,

        /**
         * Represents a power boat.
         */
        POWER

    } // end of the BoatType enum
    //----------------------------------------------------------------------------------------------------
} // end of the Boat class
//========================================================================================================
