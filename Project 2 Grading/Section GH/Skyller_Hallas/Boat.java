import java.io.Serializable;

/**
 * Represents a single boat in the club's fleet.
 * Stores all information needed for the project.
 * @author Skyler Hallas
 * @version 1.0
 */

public class Boat implements Serializable {

    /**
     * The type of boat (SAILING or POWER).
     * @see BoatType
     */
    private BoatType type;

    /**
     * The name of the boat.
     */
    private String name;

    /**
     * The year the boat was manufactured.
     */
    private int year;

    /**
     * The make and model of the boat.
     */
    private String makeModel;

    /**
     * The boat's length in feet.
     */
    private int lengthFeet;

    /**
     * The purchase price of the boat.
     */
    private double purchasePrice;

    /**
     * Total expenses recorded for the boat.
     */
    private double expenses;

    /**
     * Creates a new Boat with no expenses yet.
     * @param type The type of boat (SAILING or POWER).
     * @param name The name of the boat.
     * @param year The year the boat was manufactured.
     * @param makeModel The boat’s make and model.
     * @param lengthFeet The boat's length in feet.
     * @param purchasePrice The purchase price of the boat.
     */
    public Boat (BoatType type, String name, int year, String makeModel, int lengthFeet, double purchasePrice) {

        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;

    }// End of constructor

    /**
     * Creates a Boat from a CSV line.
     * Format: TYPE, Name, Year, MakeModel, Length, Price
     * @param line One comma-separated line representing boat data.
     * @return A Boat object created from the CSV line.
     * @exception IllegalArgumentException If the CSV fields are invalid.
     * @see BoatType
     */
    public static Boat fromCsv(String line) {
        String[] parts = line.split(",");

        BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String make = parts[3].trim();
        int len = Integer.parseInt(parts[4].trim());
        double price = Double.parseDouble(parts[5].trim());

        return new Boat(type, name, year, make, len, price);
    }// End of from CSV

    // Getters
    public BoatType getType() { return type; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public String getMakeModel() { return makeModel; }
    public int getLengthFeet() { return lengthFeet; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getExpenses() { return expenses; }

    /**
     * Adds money to this boat's expenses.
     * @param amount The dollar amount to add.
     */
    public void addExpense(double amount){
        expenses = expenses + amount;
    }// End of addExpenses

    /**
     * Returns how much money can still be spent.
     * @return The remaining budget (purchasePrice − expenses).
     */
    public double remainingBudget() {
        return purchasePrice - expenses;
    }

    /**
     * Returns a formatted string representing this boat.
     * @return A formatted string used in fleet reports.
     */
    public String toString(){
        return String.format(
                "    %-7s %-20s %4d %-10s %3d' : Paid $ %9.2f : Spent $ %9.2f",
                type, name, year, makeModel, lengthFeet, purchasePrice, expenses );
    }//End of string
}// End of class