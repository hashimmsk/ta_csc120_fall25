import java.io.Serializable;

public class Boat implements Serializable {

    private static final long serialVersionUID = 1L;

    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private double length;
    private double purchasePrice;
    private double expenses;

    /**
     *
     * @param type
     * @param name
     * @param year
     * @param makeModel
     * @param length
     * @param purchasePrice
     * @param expenses
     */

    public Boat(BoatType type,
                String name,
                int year,
                String makeModel,
                double length,
                double purchasePrice,
                double expenses) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    /**
     *
     * @return
     */

    public BoatType getType() {
        return type;
    }

    /**
     *
     * @return
     */

    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */

    public int getYear() {
        return year;
    }

    /**
     *
     * @return
     */

    public String getMakeModel() {
        return makeModel;
    }

    /**
     *
     * @return
     */

    public double getLength() {
        return length;
    }

    /**
     *
     * @return
     */

    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     *
     * @return
     */

    public double getExpenses() {
        return expenses;
    }

    /**
     *
     * @param amount
     */

    public void addExpense(double amount) {
        this.expenses += amount;
    }

    /**
     *
     * @return
     */

    public String toString() {

        return String.format("    %-7s %-20s %4d %-12s %3.0f' : Paid $ %9.2f : Spent $ %9.2f",
                type,
                name,
                year,
                makeModel,
                length,
                purchasePrice,
                expenses);
    }
}