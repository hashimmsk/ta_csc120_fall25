import java.io.Serial;
import java.io.Serializable;
/**
 * A serializable Boat record.
 * @author Mina Guler
 */
public class Boat implements Serializable {
    /**
     * Specific ID to read file
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 2 types of boats: Sailing and Power
     */
    public enum Type {SAILING, POWER}
    /**
     * boat's type
     */
    private Type type;
    /**
     * boat's name
     */
    private String name;
    /**
     * boat's make year
     */
    private int year;
    /**
     * boat's make modal
     */
    private String makeModel;
    /**
     * boat's length in feet
     */
    private int lengthFeet;
    /**
     * boat's purchase price
     */
    private double purchasePrice;
    /**
     * boat's expenses
     */
    private double expenses;
    /**
     * Boat constructor
     * @param type boat type
     * @param name boat name
     * @param year boat year
     * @param makeModel boat's make and model
     * @param lengthFeet boat's length in feet
     * @param purchasePrice boat's purchase price
     */
    public Boat(Type type, String name, int year, String makeModel, int lengthFeet, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    } // end of boat constructor
    /**
     * Get boat's Type
     * @return boat's type
     */
    public Type getType() {
        return type;
    } // end of getType getter
    /**
     * Get boat's name
     * @return boat's name
     */
    public String getName() {
        return name;
    } // end of getName getter
    /**
     * Get boat's year
     * @return boat's year
     */
    public int getYear() {
        return year;
    } // end of getYear getter
    /**
     * Get boat's make and model
     * @return boat's make and model
     */
    public String getMakeModel() {
        return makeModel;
    } // end of getMakeModel getter
    /**
     * Get boat's length in feet
     * @return boat's length in feet
     */
    public int getLengthFeet() {
        return lengthFeet;
    } // end of getLengthFeet getter
    /**
     * Get boat's purchase price
     * @return boat's purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    } // end of getPurchasePrice getter
    /**
     * Get boat's total expenses
     * @return boat's total expenses
     */
    public double getExpenses() {
        return expenses;
    } // end of getExpenses getter
    /**
     * Attempt to spend amount on this boat.
     * @param amount amount to spend on boat
     * @return true if amount can be spent, false if amount cannot be spent
     */
    public boolean spend(double amount) {
        if (amount <= 0) return false;
        if (expenses + amount <= purchasePrice) {
            expenses += amount;
            return true;
        } // end of if statement
        return false;
    } // end of spend method
    /**
     * How much is left that can be spent on this boat.
     *
     * @return amount left after expenses
     */
    public double getRemaining() {
        return purchasePrice - expenses;
    } // end of getRemaining method
    /**
     * Printing boat information
     */
    @Override
    public String toString() {
        return String.format("%-8s %-20s %4d %-10s %3d' : Paid $%9.2f : Spent $%9.2f",
                type, name, year, makeModel, lengthFeet, purchasePrice, expenses);
    } //end of toString
} // end of Boat object
