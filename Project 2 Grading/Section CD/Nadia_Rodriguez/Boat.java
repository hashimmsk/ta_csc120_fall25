import java.io.Serializable;

/*
   Represents a single boat in the fleet with all its attributes
   including type, name, year, make/model, length, purchase price,
   and maintenance expenses.
*/

public class Boat implements Serializable {
    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int length;
    private double purchasePrice;
    private double expenses;

    /**
     * Constructor for Boat
     * @param type The type of boat (SAILING or POWER)
     * @param name The name of the boat
     * @param year The year of manufacture
     * @param makeModel The make and model
     * @param length The length in feet
     * @param purchasePrice The purchase price
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
     * Gets the boat type
     * @return The boat type
     */
    public BoatType getType() {
        return type;
    }

    /**
     * Gets the boat name
     * @return The boat name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the year of manufacture
     * @return The year
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the make and model
     * @return The make and model
     */
    public String getMakeModel() {
        return makeModel;
    }

    /**
     * Gets the length in feet
     * @return The length
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the purchase price
     * @return The purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Gets the total expenses
     * @return The expenses
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Checks if a given amount can be spent on this boat
     * @param amount The amount to spend
     * @return true if expense is allowed, false otherwise
     */
    public boolean canSpend(double amount) {
        return (expenses + amount) <= purchasePrice;
    }

    /**
     * Adds an expense to the boat
     * @param amount The amount to add
     */
    public void addExpense(double amount) {
        expenses = expenses + amount;
    }

    /**
     * Gets the remaining amount that can be spent
     * @return The remaining budget
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    }

    /**
     * Converts boat data to a formatted string for display
     * @return Formatted boat information
     */
    public String toString() {
        String typeStr;
        String result;

        typeStr = String.format("%-7s", type.toString());
        result = String.format("%s %-20s %4d %-12s %3d' : Paid $%9.2f : " +
                        "Spent $%9.2f",
                typeStr, name, year, makeModel, length, purchasePrice, expenses);
        return result;
    }

    /**
     * Creates a Boat object from a CSV line
     * @param csvLine The CSV formatted line
     * @return A new Boat object
     */
    public static Boat parseCSV(String csvLine) {
        String[] parts;
        BoatType type;
        String name;
        int year;
        String makeModel;
        int length;
        double purchasePrice;

        parts = csvLine.split(",");
        type = BoatType.valueOf(parts[0].trim().toUpperCase());
        name = parts[1].trim();
        year = Integer.parseInt(parts[2].trim());
        makeModel = parts[3].trim();
        length = Integer.parseInt(parts[4].trim());
        purchasePrice = Double.parseDouble(parts[5].trim());

        return new Boat(type, name, year, makeModel, length, purchasePrice);
    }
}