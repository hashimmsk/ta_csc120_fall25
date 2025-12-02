/**
 * The class which will hold all the attributes of each boat of the fleet
 * @author Jake Maksimiak
 * @see FleetManagement
 */
public class Boat {

    /**
     * Represents the 2 types of boats that can be in the fleet
     */
    enum TypeOfBoat {

        /**
         * Indicates the boat is a sailing boat
         */
        SAILING,
        /**
         * Indicates the boat is a power boat
         */
        POWER

    }

    /**
     * The type of boat
     */
    TypeOfBoat currentBoat;
    /**
     * The name of the boat
     */
    String nameOfBoat;
    /**
     * The manufacture year of the boat
     */
    int yearOfManufacture;
    /**
     * The make and model of the boat
     */
    String makeAndModel;
    /**
     * The length of the boat, in feet and under 100 feet
     */
    byte lengthOfBoat;
    /**
     * The money paid for the boat
     */
    double moneyPaid;
    /**
     * The money spent on the boat
     */
    double moneySpent;


    /**
     * The default constructor for the Boat Class
     */
    public Boat() {

        this.currentBoat = null;
        this.nameOfBoat = null;
        this.yearOfManufacture = 0000;
        this.makeAndModel = null;
        this.lengthOfBoat = 0;
        this.moneyPaid = 0;
        this.moneySpent = 0;

    }

    /**
     * The constructor which will assign the following attributes to each Boat object
     * @param currentBoat the type of boat, sailing or power
     * @param nameOfBoat the name of the boat
     * @param yearOfManufacture the year of manufacture
     * @param makeAndModel the make and model
     * @param lengthOfBoat the length of the boat in feet
     * @param moneyPaid the money paid for the boat
     */
    public Boat(TypeOfBoat currentBoat, String nameOfBoat, int yearOfManufacture, String makeAndModel, byte lengthOfBoat, double moneyPaid) {

        this.currentBoat = currentBoat;
        this.nameOfBoat = nameOfBoat;
        this.yearOfManufacture = yearOfManufacture;
        this.makeAndModel = makeAndModel;
        this.lengthOfBoat = lengthOfBoat;
        this.moneyPaid = moneyPaid;

    } // end of constructor

    /**
     * The getter method which gets the money paid for a boat
     * @return the money paid for a boat
     */
    public double getMoneyPaid() {
        return moneyPaid;
    } // end of getter method

    /**
     * The getter method which gets the money spent on a boat
     * @return the money spent for a boat
     */
    public double getMoneySpent() {
        return moneySpent;
    } // end of getter method

    /**
     * The getter method which gets the name of a boat
     * @return the name of a boat
     */
    public String getNameOfBoat() {
        return nameOfBoat;
    } // end of getter method

    /**
     * The setter method which updates the amount spent on a boat
     * @param moneySpent the total amount spent on a boat
     */
    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    } // end of setter method

    /**
     * The getter method which gets the available money that can be spent on a boat, given by the money paid for the
     * boat minus the money spent on the boat
     * @return The money that can be spent on a boat
     */
    public double getAvailableToSpend() {

        return (getMoneyPaid() - getMoneySpent());

    } // end of getter method

    /**
     * The toString method which returns the data of the fleet as a string
     * @return the data of the fleet
     */
    @Override
    public String toString() {

        return "    " +
                String.format("%-8s", currentBoat) +
                String.format("%-21s", nameOfBoat) +
                String.format("%-5s", yearOfManufacture) +
                String.format("%-12s", makeAndModel) +
                lengthOfBoat + "' : Paid $" +
                String.format("%9s", String.format("%.2f", moneyPaid)) +
                " : Spent $" +
                String.format("%9s", String.format("%.2f", moneySpent));

    } // end of toString

    /**
     * The getter method returns the data of a boat in csv format
     * @return data of a boat in csv format
     */
    public String toSerializedForm () {

        return currentBoat + "," +
                nameOfBoat + "," +
                yearOfManufacture + "," +
                makeAndModel + "," +
                lengthOfBoat + "," +
                moneyPaid + "," +
                moneySpent;

    } // end of getter method


} // end of Boat Class
