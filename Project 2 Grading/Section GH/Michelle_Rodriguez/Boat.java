import java.io.Serializable;

/**
 * Represents a Boat with attributes such as name, type, model, length, purchase price,
 * and maintenance cost. Implements {@link Serializable} to allow object serialization.
 * Provides methods to get and set boat information and check maintenance expenses.
 * @see Serializable
 */

public class Boat implements Serializable {
    public enum BoatType {UNKNOWN, POWER, SAILING}

    // Attributes :

    BoatType type;
    private String boatName;
    private int boatYear;
    private String model;
    private double boatLength;
    private double purchasePrice;
    private double maintainCost;


    public Boat() {
        this.type = BoatType.UNKNOWN;
        this.boatName = "NO NAME";
        this.boatLength = 0.0;
        this.model = "No model";
        this.purchasePrice = 0;
        this.boatYear = 0;
        this.maintainCost = 0;
    }// end of default constructor

    public Boat(BoatType type, String boatName, int year, String model, double length, double price) {
        this.type = type;
        this.boatName = boatName;
        this.boatYear = year;
        this.model = model;
        this.boatLength = length;
        this.purchasePrice = price;
        this.maintainCost = 0.0;
    }// end of constructor


    // get methods

    /**
     * Returns the name of the boat.
     * @return The boat name
     */
    public String getBoatName() {
        return boatName;
    }// end of Get boatName;

    /**
     * Returns the length of the boat in feet.
     * @return The boat length
     */
    public double getBoatLength() {
        return boatLength;
    }// end of Get boatLength

    /**
     * Returns the model of the boat.
     * @return The boat model
     */
    public String getModel() {
        return model;
    }//end of Get Model

    /**
     * Returns the purchase price of the boat.
     * @return The purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }// end of Get price

    /**
     * returns that the boat year
     * @return the boat year
     */
    public int getBoatYear() {
        return boatYear;
    }// end of Get year

    /**
     *returns the maintenance cost
     * @return the maintenance Cost
     */
    public double getMaintainCost() {
        return maintainCost;
    }// end of Get MaintainCost

    /**
     *
     * @param boatLength
     */
    public void setBoatLength(int boatLength) {
        this.boatLength = boatLength;
    }// end of set of boatLength

    /**
     * Sets the name of the boat.
     * @param boatName The new name of the boat
     */
    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }// end of set of boatName

    /**
     * Sets the model of the boat.
     * @param model The new model of the boat
     */
    public void setModel(String model) {
        this.model = model;
    }// end of set of model

    /**
     * Sets the purchase price of the boat.
     * @param purchasePrice The new purchase price
     */
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }// end of set of price

    /**
     *Sets the year of manufacture of the boat.
     * @param boatYear The new year of the boat
     */
    public void setBoatYear(int boatYear) {
        this.boatYear = boatYear;
    }// end of set of year

    /**
     *Sets the maintenance cost of the boat.
     * @param maintainCost
     */
    public void setMaintainCost(double maintainCost) {
        this.maintainCost = maintainCost;
    }// end of Set MaintainCost


    /**
     * Returns a string representation of the boat, including type, name, year, model, length,
     * purchase price, and maintenance cost.
     * @return Formatted string representing the boat
     */
    public String toString() {

        return (String.format("%-7s %-20s %4d %-10s %3.0f' : Paid $%9.2f : Spent $%9.2f",
                type.toString(), this.boatName, this.boatYear, model, this.boatLength, this.purchasePrice, this.maintainCost));

    }// end of toString


    /**
     * Determines if a new maintenance expense is approved. The expense is approved only if
     * the total maintenance cost after adding the new expense does not exceed the purchase price.
     * @param newExpense The new maintenance expense to consider
     * @return {@code False} if the expense is not approved, {@code True} if the expense is approved
     */
    public boolean isApprovedCost(double newExpense) {
        double totalNewEstimatedExpense;

        totalNewEstimatedExpense = newExpense + this.maintainCost;
        if (totalNewEstimatedExpense >= this.purchasePrice) {
            return (false);
        } else {
            this.maintainCost = this.maintainCost + newExpense;
            return (true);
        }

    }// end of IsApprovedCost Method


}// end of boat Class
