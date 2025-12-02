import java.io.Serializable;
/**
 * Boat class to store relevant boat information.
 * Implements Serializable to save/load object with file.
 */
public class Boat implements Serializable {

    /**
     * Enum representing type of boat.
     */
    public enum BoatKind { SAILING, POWER }

    // Data members
    private final BoatKind boatType;
    private final String name;
    private final int year;
    private final String model;
    private final int length;
    private final double price;
    private double expenses;

    /**
     * Constructs Boat object with required information.
     * @param boatType type of boat
     * @param name name of boat
     * @param year year boat was made
     * @param model make/model of boat
     * @param length length in feet of boat
     * @param price purchase price of boat
     * @param expenses current maintenance expense of boat
     */
    public Boat(BoatKind boatType, String name, int year, String model,
                int length, double price, double expenses) {
        this.boatType = boatType;
        this.name = name;
        this.year = year;
        this.model = model;
        this.length = length;
        this.price = price;
        this.expenses = expenses;
    }

    /**
     * Returns name of boat.
     * @return boat name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns purchase price of boat.
     * @return purchase price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns current maintenance expense of boat.
     * @return total expenses
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Adds expense to boat's current maintenance total.
     * @param amount expense to add
     */
    public void addExpense(double amount) {
        this.expenses += amount;
    }

    /**
     * Returns aligned string representing boat data.
     * @return formatted string of boat information
     */
    @Override
    public String toString() {
        return String.format(
                "    %-7s %-20s %4d %-12s %3d' : Paid $ %10.2f : Spent $ %10.2f",
                boatType,
                name,
                year,
                model,
                length,
                price,
                expenses
        ); // Formats boat data into alignment

    } //end of toString method

} //end of Boat class