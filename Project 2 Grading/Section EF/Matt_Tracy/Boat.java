import java.io.Serializable;

enum BoatType {
    SAILING,
    POWER
}

/**
 * The Boat object holds the data for the boats, including constructors, getters, and other methods
 * to help the main program deal handle the expenses.
 */
public class Boat implements Serializable {
    private static final long serialVersionUID = 1L;

    private BoatType type;
    private String name;
    private int yearOfManufacture;
    private String makeModel;
    private int lengthFeet;
    private double purchasePrice;
    private double expenses;

    public Boat(BoatType type, String name, int yearOfManufacture,
                String makeModel, int lengthFeet,
                double purchasePrice, double expenses) {
        this.type = type;
        this.name = name;
        this.yearOfManufacture = yearOfManufacture;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    /**
     * Splits the data in the CSV line into separate blocks of text that can be then put into variables.
     * @param csvLine The line from the CSV file.
     * @return Returns a new Boat object.
     */
    public static Boat fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");


        BoatType type = BoatType.valueOf(parts[0].toUpperCase());
        String name = parts[1];
        int year = Integer.parseInt(parts[2]);
        String makeModel = parts[3];
        int length = Integer.parseInt(parts[4]);
        double price = Double.parseDouble(parts[5]);

        return new Boat(type, name, year, makeModel, length, price, 0.0);
    }

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

    public void addExpense(double amount) {
        this.expenses += amount;
    }

    public double remainingBudget() {
        return purchasePrice - expenses;
    }

    @Override
    public String toString() {
        return String.format(
                "    %-7s %-20s %4d %-10s %3d' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, yearOfManufacture, makeModel, lengthFeet, purchasePrice, expenses
        );
    }
}
