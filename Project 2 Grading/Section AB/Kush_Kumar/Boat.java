import java.io.Serializable;

/**
 * Generic boat class to create boats
 * @author Kush Kumar
 */
public class Boat implements Serializable {
    //Enum creation for boatType

    /**
     * The choice of boatType is restricted to SAILING & POWER
     */
    public enum BoatType {SAILING, POWER}

    //Instance variables to hold data associated with boat
    private final BoatType boatType;
    private final String boatName;
    private final int boatManufactureYear;
    private final String boatModel;
    private final int boatLength;
    private double boatPurchasePrice;
    private double boatExpenses;

    /**
     * Default constructor.
     */

    public Boat() {
        boatType = null;
        boatName = "";
        boatManufactureYear = 0;
        boatModel = "";
        boatLength = 0;
        boatPurchasePrice = 0.0;
        boatExpenses = 0.0;
    } // end of the default constructor

    /**
     * Initial value constructor.
     * @param boatType The type of boat: sailing or power
     * @param boatName The name of the boat
     * @param boatManufactureYear The boat manufacture year
     * @param boatModel The boat make/model
     * @param boatLength The boat length
     * @param boatPurchasePrice The boat purchase price
     * @param boatExpenses The boat expenses so far, will be zero initially
     */

    public Boat(BoatType boatType, String boatName, int boatManufactureYear, String boatModel, int boatLength, double boatPurchasePrice, double boatExpenses) {
        this.boatType = boatType;
        this.boatName = boatName;
        this.boatManufactureYear = boatManufactureYear;
        this.boatModel = boatModel;
        this.boatLength = boatLength;
        this.boatPurchasePrice = boatPurchasePrice;
        this.boatExpenses = boatExpenses;
    } // end of parameterized constructor

    //Accessor Methods to Get Relevant Boat Data

    /**
     * Accessor method to get boat name
     * @return boat name
     */

    public String getBoatName () {
        return boatName;
    } // end of getBoatName method

    /**
     * Accessor method to get boat purchase price
     * @return boat purchase price
     */

    public double getBoatPurchasePrice () {
        return boatPurchasePrice;
    } // end of getBoatPurchasePrice method

    /**
     * Accessor method to get boat expenses
     * @return boat expenses so far
     */

    public double getBoatExpenses () {
        return boatExpenses;
    } // end of getBoatExpenses method

    /**
     * Produce printable information about boat.
     * @return formatted String with data of the boat
     */

    @Override
    public String toString() {
        return String.format(
                "%-7s %-20s %4d %-10s %2d' : Paid $ %8.2f : Spent $ %8.2f",
                boatType,
                boatName,
                boatManufactureYear,
                boatModel,
                boatLength,
                boatPurchasePrice,
                boatExpenses
        );
    }

    //Methods to change boat data

    /**
     * Checks if additional expenses can be added to the boat expenses
     * @param additionalBoatExpense Additional expenses considered to be added to the boat
     * @return boolean to signal whether additional expenses can be added
     */

    public boolean changeBoatExpenses (double additionalBoatExpense) {
        //If additional expenses are too large, they cannot be added
        if ((additionalBoatExpense + boatExpenses) >= boatPurchasePrice) {
            return false;
        }
        //If additional expenses are valid, they can be added
        else {
            boatExpenses = boatExpenses + additionalBoatExpense;
            return true;
        }
    } // end of changeBoatExpenses method

} // end of the Boat class
