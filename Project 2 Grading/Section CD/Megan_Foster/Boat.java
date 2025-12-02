import java.io.Serializable;

/**
 * Represents a boat in the fleet.
 * Stores all identifying information along with purchase price and expenses.
 * <p>This class supports serialization so that FleetData.db
 * @author meganfoster
 * @version 1.0
 * @boat
 */
public class Boat implements Serializable {

    private static final long serialVersionUID = 1L;

    private BoatType type;
    private String name;
    private int year;
    private String make;
    private int length;
    private double purchasePrice;
    private double expenses;

    /**
     * Constructs a new Boat object.
     */
    public Boat(BoatType type, String name, int year, String make,
                int length, double purchasePrice, double expenses) {

        this.type = type;
        this.name = name;
        this.year = year;
        this.make = make;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    public String getName() { return name; }
    public BoatType getType() { return type; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getExpenses() { return expenses; }

    /**
     * Adds an expense to the boat.
     * @param amount amount to add
     */
    public void addExpense(double amount) {
        expenses += amount;
    }

    /**
     * Factory method: Construct a Boat from a CSV line.
     */
    public static Boat fromCSV(String line) {
        String[] p = line.split(",");

        BoatType type = BoatType.valueOf(p[0].trim().toUpperCase());
        String name = p[1].trim();
        int year = Integer.parseInt(p[2].trim());
        String make = p[3].trim();
        int length = Integer.parseInt(p[4].trim());
        double price = Double.parseDouble(p[5].trim());

        return new Boat(type, name, year, make, length, price, 0.0);
    }

    /**
     * String representation used in the fleet printout.
     */
    @Override
    public String toString() {
        return String.format(
                "    %-7s %-20s %4d %-12s %3d' : Paid $ %10.2f : Spent $ %10.2f",
                type, name, year, make, length, purchasePrice, expenses
        );
    }
} // end of Boat class
