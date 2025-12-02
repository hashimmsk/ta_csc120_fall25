import java.io.Serializable;

public class Boat implements Serializable {

    private BoatType type;
    private String name;
    private int year;
    private String make;
    private int length;
    private double purchasePrice;
    private double expenses;

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

    public BoatType getType() { return type; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public String getMake() { return make; }
    public int getLength() { return length; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getExpenses() { return expenses; }

    public void setExpenses(double e) {
        expenses = e;
    }


    public String toReportLine() {

        return String.format(
                "    %-7s %-20s %4d %-13s %3d' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, year, make, length, purchasePrice, expenses
        );
    }
} // end of the Boat class
