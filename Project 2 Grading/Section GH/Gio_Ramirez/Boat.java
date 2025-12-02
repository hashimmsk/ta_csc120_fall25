import java.io.Serializable;

public class Boat implements Serializable {

    // Serialization support
    private static final long serialVersionUID = 1L;

    // Instance fields (per boat)
    private final BoatType type;

    private final String name;

    private final int yearOfManufacture;

    private final String makeModel;

    private final int lengthFeet;

    private final double purchasePrice;

    private double expenses;

    // Constructors
    public Boat(BoatType type,
                String name,
                int yearOfManufacture,
                String makeModel,
                int lengthFeet,
                double purchasePrice) {

        this.type = type;
        this.name = name;
        this.yearOfManufacture = yearOfManufacture;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;

        // Start with no expenses recorded.
        this.expenses = 0.0;
    }

    // Accessor methods

    public BoatType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public String getMakeModel() {
        return makeModel;
    }

    public int getLengthFeet() {
        return lengthFeet;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getExpenses() {
        return expenses;
    }

    // Behaviour methods

    public void addExpense(double amount) {
        this.expenses += amount;
    }

    public double getRemainingBudget() {
        return purchasePrice - expenses;
    }
} // End of Boat class
