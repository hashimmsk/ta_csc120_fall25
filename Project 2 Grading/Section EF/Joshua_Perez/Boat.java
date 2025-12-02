//=================================================================================================
import java.io.Serializable;
//=================================================================================================
/**
 * Represents a single boat in the fleet.
 *
 * <p>Each boat knows its type (sailing or power), name, year of manufacture,
 * make/model, length in feet, purchase price, and the amount spent on
 * maintenance. Boats are {@link Serializable} so that the fleet can be
 * saved and loaded from a database file.</p>
 *
 * @author  Joshua Perez
 * @version 1.0
 */
//=================================================================================================
public class Boat implements Serializable {
//-------------------------------------------------------------------------------------------------
    /** Required serialization identifier. */
    private static final long serialVersionUID = 1L;

    /** Type of the boat (sailing or power). */
    private BoatType type;
    /** Name of the boat. */
    private String name;
    /** Year the boat was manufactured. */
    private int year;
    /** Make and model of the boat. */
    private String makeModel;
    /** Length of the boat in feet. */
    private int lengthFeet;
    /** Purchase price of the boat. */
    private double purchasePrice;
    /** Total amount spent on maintaining this boat. */
    private double expenses;
//-------------------------------------------------------------------------------------------------
    /**
     * Construct a new {@code Boat}.
     *
     * @param boatType        type of boat (sailing or power)
     * @param boatName        name of the boat
     * @param manufactureYear year of manufacture
     * @param makeAndModel    make/model string
     * @param length          length in feet
     * @param purchaseAmt     purchase price
     * @param initialSpent    amount already spent on maintenance
     */
    public Boat(BoatType boatType,
                String boatName,
                int manufactureYear,
                String makeAndModel,
                int length,
                double purchaseAmt,
                double initialSpent) {

        type = boatType;
        name = boatName;
        year = manufactureYear;
        makeModel = makeAndModel;
        lengthFeet = length;
        purchasePrice = purchaseAmt;
        expenses = initialSpent;
    } // end of the Boat constructor
//-------------------------------------------------------------------------------------------------
    /**
     * Get the type of this boat.
     *
     * @return the boat type
     */
    public BoatType getType() {
        return type;
    } // end of getType method
//-------------------------------------------------------------------------------------------------
    /**
     * Get the name of this boat.
     *
     * @return the boat name
     */
    public String getName() {
        return name;
    } // end of getName method
//-------------------------------------------------------------------------------------------------
    /**
     * Get the year this boat was manufactured.
     *
     * @return the year of manufacture
     */
    public int getYear() {
        return year;
    } // end of getYear method
//-------------------------------------------------------------------------------------------------
    /**
     * Get the make/model of this boat.
     *
     * @return the make/model string
     */
    public String getMakeModel() {
        return makeModel;
    } // end of getMakeModel method
//-------------------------------------------------------------------------------------------------
    /**
     * Get the length of this boat in feet.
     *
     * @return the length in feet
     */
    public int getLengthFeet() {
        return lengthFeet;
    } // end of getLengthFeet method
//-------------------------------------------------------------------------------------------------
    /**
     * Get the purchase price of this boat.
     *
     * @return the purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    } // end of getPurchasePrice method
//-------------------------------------------------------------------------------------------------
    /**
     * Get the total expenses so far for this boat.
     *
     * @return the current expenses
     */
    public double getExpenses() {
        return expenses;
    } // end of getExpenses method
//-------------------------------------------------------------------------------------------------
    /**
     * Add an amount to the current expenses.
     *
     * @param amount amount to add
     */
    public void addExpense(double amount) {
        expenses = expenses + amount;
    } // end of addExpense method
//-------------------------------------------------------------------------------------------------
    /**
     * Get the remaining money allowed to be spent on this boat under the
     * Commodore's policy (purchase minus expenses).
     *
     * @return remaining allowed amount (may be zero)
     */
    public double getRemainingAllowance() {
        return purchasePrice - expenses;
    } // end of getRemainingAllowance method
//-------------------------------------------------------------------------------------------------
    /**
     * Return a formatted description of this boat suitable for the fleet report.
     *
     * @return formatted boat line
     */
    public String toString() {
        String formatted;

        formatted = String.format(
                "    %-7s %-20s %4d %-12s %3d' : Paid $ %8.2f : Spent $ %9.2f",
                type.name(),
                name,
                year,
                makeModel,
                lengthFeet,
                purchasePrice,
                expenses
        );

        return formatted;
    } // end of toString method
//-------------------------------------------------------------------------------------------------
} // end of the Boat class
//=================================================================================================
