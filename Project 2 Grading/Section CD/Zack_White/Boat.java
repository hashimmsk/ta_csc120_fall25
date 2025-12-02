import java.io.Serializable;

/**
 * Class Boat represents a single boat in the fleet.
 * It stores all boat attributes, including type, name, year of manufacture,
 * make/model, length, purchase price, and total expenses.
 * It also provides methods to authorize expenses and format output.
 * @author Zack White
 */
public class Boat implements Serializable {

    //----Boat data fields
    private BoatType type;
    private String name;
    private int yearManufactured;
    private String makeModel;
    private int lengthFeet;
    private double purchasePrice;
    private double totalExpenses;

    /**
     * Constructs a new Boat object with the given attributes.
     *
     * @param boatType the type of boat (SAILING or POWER)
     * @param boatName the name of the boat
     * @param year the year the boat was manufactured
     * @param model the make and model of the boat
     * @param length the length of the boat in feet
     * @param paidAmount the purchase price of the boat
     */
    public Boat(BoatType boatType, String boatName, int year,
                String model, int length, double paidAmount) {

        type = boatType;
        name = boatName;
        yearManufactured = year;
        makeModel = model;
        lengthFeet = length;
        purchasePrice = paidAmount;
        totalExpenses = 0.0;
    }// end of Boat constructor

    /**
     * Creates a Boat object from a CSV line.
     * The CSV format is:
     * TYPE,NAME,YEAR,MAKE/MODEL,LENGTH,PRICE
     *
     * @param csvLine a single line of CSV representing a boat
     * @return a Boat object parsed from the CSV
     */
    public static Boat fromCsv(String csvLine) {
        String[] parts;
        BoatType boatType;
        String boatName;
        int year;
        String model;
        int length;
        double paidAmount;

        parts = csvLine.split(",");

        boatType = BoatType.valueOf(parts[0].trim().toUpperCase());
        boatName = parts[1].trim();
        year = Integer.parseInt(parts[2].trim());
        model = parts[3].trim();
        length = Integer.parseInt(parts[4].trim());
        paidAmount = Double.parseDouble(parts[5].trim());

        return new Boat(boatType, boatName, year, model, length, paidAmount);
    }// end of fromCsv method

    /**
     * Gets the name of the boat.
     *
     * @return the name of the boat
     */
    public String getName() {
        return name;
    }// end of getter

    /**
     * Gets the purchase price of the boat.
     *
     * @return the purchase price of the boat
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }// end of getter

    /**
     * Gets the total expenses spent on the boat.
     *
     * @return the total expenses of the boat
     */
    public double getTotalExpenses() {
        return totalExpenses;
    }// end of getter

    /**
     * Attempts to add an expense to this boat.
     * Expense is only added if it does not exceed the purchase price.
     *
     * @param expenseAmount the expense amount to add
     * @return true if the expense is authorized, false otherwise
     */
    public boolean authorizeExpense(double expenseAmount) {
        if (totalExpenses + expenseAmount <= purchasePrice) {
            totalExpenses = totalExpenses + expenseAmount;
            return true;
        }
        return false;
    }// end of authorizeExpense method

    /**
     * Returns a formatted string representing this boat for printing.
     *
     * @return a formatted string with type, name, year, make/model, length,
     * purchase price, and total expenses
     */
    public String toString() {
        return String.format(
                "    %-7s %-20s %4d %-12s %2d' : Paid $ %9.2f : Spent $ %9.2f",
                type, name, yearManufactured, makeModel, lengthFeet,
                purchasePrice, totalExpenses
        );
    }// end of toString method

}// end of Boat class