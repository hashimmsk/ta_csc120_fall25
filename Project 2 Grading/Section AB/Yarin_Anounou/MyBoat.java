import java.io.Serializable;
import java.util.Objects;

public class MyBoat implements Serializable {

    private static final long serialVersionUID = 1L;

    private final BoatType boatType;
    private final String name;
    private final int year;
    private final String makeAndModel;
    private final int lengthInFeet;
    private final double purchasePrice;
    private double expenses;

    /**
     * This is the constructor method of the Boat that assigns all attributes.
     * @param boatType
     * @param name
     * @param year
     * @param makeAndModel
     * @param lengthInFeet
     * @param purchasePrice
     */
    public MyBoat(BoatType boatType, String name, int year, String makeAndModel, int lengthInFeet, double purchasePrice) {
        this.boatType = boatType;
        this.name = name;
        this.year = year;
        this.makeAndModel = makeAndModel;
        this.lengthInFeet = lengthInFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    } // end of constructor method

    /**
     * This is the getter method for the name.
     * @return
     */
    public String getName() {
        return name;
    } // end of getter for name

    /**
     * This is the getter method for the year.
     * @return
     */
    public int getYear() {
        return year;
    } // end of getter for year

    /**
     * This is the getter method for the make and model.
     * @return
     */
    public String getMakeAndModel() {
        return makeAndModel;
    } // end of getter for make and model

    /**
     * This is the getter method for the length in feet.
     * @return
     */
    public int getLengthInFeet() {
        return lengthInFeet;
    } // end of getter for length in feet

    /**
     * This is the getter method for the purchase price.
     * @return
     */
    public double getPurchasePrice() {
        return purchasePrice;
    } // end of getter for purchase price

    /**
     * This is the getter method for expenses.
     * @return
     */
    public double getExpenses() {
        return expenses;
    } // end of getter for expenses

    /**
     * This is the method that checks if there is enough money to spend.
     * @param amount
     * @return
     */
    public boolean ifCanSpend(double amount) {
        return (this.expenses + amount) <= this.purchasePrice;
    } // end of the ifCanSpend method

    /**
     * This is the method that adds the money to the expenses.
     * @param amount
     */
    public void addExpenses(double amount) {
        this.expenses += amount;
    } // end of the addExpenses method

    /**
     * This is the method that solves for the remaining budget allowed to spend.
     * @return
     */
    public double getRemainingBudget() {
        return Math.max(0.0, this.purchasePrice - this.expenses);
    } // end of the getRemainingBudget method

    /**
     * This method reads the information for the original CSV file that has the initial boats for the fleet.
     * @param csvLine
     * @return
     * @throws IllegalArgumentException
     */
    public static MyBoat fromCSV(String csvLine) throws IllegalArgumentException {
        String[] parts = csvLine.split(",");

        if (parts.length < 6) {
            throw new IllegalArgumentException("Incomplete boat data provided. Expected at least 6 fields.");
        }

        BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String makeAndModel = parts[3].trim();
        int lengthInFeet = Integer.parseInt(parts[4].trim());
        double purchasePrice = Double.parseDouble(parts[5].trim());

        double initialExpenses = (parts.length > 6) ? Double.parseDouble(parts[6].trim()) : 0.0;

        MyBoat newBoat = new MyBoat(type, name, year, makeAndModel, lengthInFeet, purchasePrice);
        newBoat.expenses = initialExpenses;

        return newBoat;
    } // end of the fromCSV method

    /**
     * This is the toString method that creates the correct format for the string.
     * @return
     */
    @Override
    public String toString() {
        return String.format("%-7s %-20s %4d %-10s %2d' : Paid $ %10.2f : Spent $ %10.2f",
                boatType, name, year, makeAndModel, lengthInFeet, purchasePrice, expenses);
    } // end of the toString method

    /**
     * The main purpose of this method is to comapre a boat to a known boat to see if they are the same.
     * @param object the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        MyBoat myBoat = (MyBoat) object;

        return this.name.trim().toLowerCase().equals(myBoat.name.trim().toLowerCase());
    }

    /**
     * This method is used to calculate the hash code needed.
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name.trim().toLowerCase());
    }
}