/**
 * Represents a boat in the Coconut Grove Sailing Club fleet.
 * Tracks boat information including type, specifications, purchase price, and expenses.
 */
public class Boat {

    /**
     * Enumeration of boat types in the fleet.
     */
    public enum BoatType {
        /** Power boats with engines */
        POWER,
        /** Sailing boats */
        SAILING
    }

    /** The type of boat (POWER or SAILING) */
    private BoatType type;
    /** The name of the boat */
    private String name;
    /** The year the boat was manufactured */
    private int year;
    /** The make and model of the boat */
    private String makeModel;
    /** The length of the boat in feet */
    private int length;
    /** The purchase price of the boat in dollars */
    private double purchasePrice;
    /** The total expenses spent on maintaining the boat */
    private double expenses;

    /**
     * Constructs a new Boat with the specified attributes.
     * Initial expenses are set to 0.
     *
     * @param type the type of boat (POWER or SAILING)
     * @param name the name of the boat
     * @param year the year of manufacture
     * @param makeModel the make and model of the boat
     * @param length the length in feet
     * @param purchasePrice the purchase price in dollars
     */
    public Boat(BoatType type, String name, int year, String makeModel,
                int length, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }

    /**
     * Parses a CSV line to create a new Boat object (for initial data load).
     * Expected format: TYPE,NAME,YEAR,MAKEMODEL,LENGTH,PRICE
     *
     * @param csvLine a comma-separated string containing boat data
     * @return a new Boat object created from the CSV data
     */
    public static Boat parseCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String makeModel = parts[3].trim();
        int length = Integer.parseInt(parts[4].trim());
        double price = Double.parseDouble(parts[5].trim());
        return new Boat(type, name, year, makeModel, length, price);
    }

    /**
     * Converts the boat to CSV format including current expenses.
     * Used for saving data to the database file.
     *
     * @return a CSV string representation of the boat
     */
    public String toCSV() {
        return type + "," + name + "," + year + "," + makeModel + "," +
                length + "," + purchasePrice + "," + expenses;
    }

    /**
     * Creates a Boat object from a CSV line that includes expenses.
     * Expected format: TYPE,NAME,YEAR,MAKEMODEL,LENGTH,PRICE,EXPENSES
     * Used for loading data from the database file.
     *
     * @param csvLine a comma-separated string containing boat data with expenses
     * @return a Boat object with expenses restored from saved data
     */
    public static Boat fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String makeModel = parts[3].trim();
        int length = Integer.parseInt(parts[4].trim());
        double price = Double.parseDouble(parts[5].trim());
        double expenses = Double.parseDouble(parts[6].trim());

        Boat boat = new Boat(type, name, year, makeModel, length, price);
        boat.expenses = expenses;
        return boat;
    }

    /**
     * Attempts to add an expense to the boat.
     * The Commodore's policy restricts total expenses to not exceed the purchase price.
     *
     * @param amount the amount to spend on the boat
     * @return true if the expense was authorized and added, false if it would exceed the budget
     */
    public boolean addExpense(double amount) {
        if (expenses + amount <= purchasePrice) {
            expenses += amount;
            return true;
        }
        return false;
    }

    /**
     * Calculates the remaining budget available for expenses on this boat.
     *
     * @return the amount that can still be spent (purchase price minus current expenses)
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    }

    /**
     * Gets the name of the boat.
     *
     * @return the boat's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the boat.
     *
     * @return the boat type (POWER or SAILING)
     */
    public BoatType getType() {
        return type;
    }

    /**
     * Gets the purchase price of the boat.
     *
     * @return the purchase price in dollars
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Gets the total expenses spent on the boat.
     *
     * @return the total expenses in dollars
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Returns a formatted string representation of the boat for display.
     *
     * @return a formatted string showing boat details, purchase price, and expenses
     */
    @Override
    public String toString() {
        return String.format("    %-7s %-20s %4d %-12s %3d' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, year, makeModel, length, purchasePrice, expenses);
    }
}