/**
 * Object named Boat that has a type, name, manufacture year, make/model, length, purchase price, and expenses spent.
 * The expenses spent can be changed by spending more money
 * @author Camden Treese
 * @see CoconutGroveSailingClubBoats
 */
public class Boat {

//----Declares an enumerator for the acceptable boat types
    public enum AcceptableBoatType {POWER, SAILING}

//----Declares the variables for the boat qualities: type, name, manufacture year, make/model, length, price, expenses
    private AcceptableBoatType boatType;
    private String boatName;
    private int manufactureYear;
    private String boatMakeAndModel;
    private int boatLength;
    private double purchasePrice;
    private double boatExpenses;

    /**
     * Default constructor for the Boat object that takes no parameters and sets all values to null/0
     */
    public Boat() {

//----Sets all variables to either null or 0
        this.boatType = null;
        this.boatName = null;
        this.manufactureYear = 0;
        this.boatMakeAndModel = null;
        this.boatLength = 0;
        this.purchasePrice = 0;
        this.boatExpenses = 0;


    } // end of the default Boat constructor

    /**
     * Boat object constructor that takes parameters for each of the values of the boat and assigns them accordingly
     * @param boatType
     * @param boatName
     * @param manufactureYear
     * @param boatMakeAndModel
     * @param boatLength
     * @param purchasePrice
     * @param boatExpenses
     */
    public Boat(AcceptableBoatType boatType, String boatName, int manufactureYear, String boatMakeAndModel,
                int boatLength, double purchasePrice, double boatExpenses) {

//----Sets all the boat variables equal to the parameters passed in the method call
        this.boatType = boatType;
        this.boatName = boatName;
        this.manufactureYear = manufactureYear;
        this.boatMakeAndModel = boatMakeAndModel;
        this. boatLength = boatLength;
        this.purchasePrice = purchasePrice;
        this.boatExpenses = boatExpenses;

    } // end of the Boat constructor

    /**
     * Spends money on a boat by adding the money spent to the current expenses and rounding the result to two decimals
     * @param boatExpenses
     */
    public void spendMoneyOnBoat (double boatExpenses) {

//----Sets the boat expenses equal to the current number plus the new money spent
        this.boatExpenses += boatExpenses;

//----Rounds the boat expenses to two decimal places
        this.boatExpenses = Math.round(this.boatExpenses * 100.00) / 100.00;

    } // end of the spendMoneyOnBoat method

    /**
     * Gets the name of the boat and returns the name as a string
     * @return boatName
     */
    public String getBoatName() {

//----Returns the name of the boat
        return boatName;

    } // end of the getBoatName method

    /**
     * Gets the purchase price of the boat and returns it as a double
     * @return purchasePrice
     */
    public double getPurchasePrice() {

//----Returns the purchase price of the boat
        return purchasePrice;

    } // end of the getPurchasePrice method

    /**
     * Gets the expenses spent on the boat and returns it as a double
     * @return boatExpenses
     */
    public double getBoatExpenses() {

//----Returns the expenses spent on the boat
        return boatExpenses;

    } // end of the getBoatExpenses method

    /**
     * Checks if the program is closing a file. If so, returns the boat values in String format meant for saving to the
     * db file. If not, returns the boat values as a String in the format to be printed out to the user
     * @param closingFile
     * @return a string
     */
    public String toString(boolean closingFile) {

//----If statement to check if the file is being closed and therefore needs to save the information
        if(closingFile == true) {

//--------Returns a string in the format needed to properly save to the db file
            return(boatType + "," + boatName + "," + manufactureYear + "," + boatMakeAndModel + "," + boatLength + ","
                    + purchasePrice + "," + boatExpenses);

        } else {

//--------Returns a string in the format needed to print out to the user
            return ("    " + boatType + " " + boatName + " " + manufactureYear + " " + boatMakeAndModel + " " + boatLength +
                    "\' : Paid $ " + purchasePrice + " : Spent $ " + boatExpenses);

        }

    } // end of the toString method

} // end of the Boat class
