import java.io.Serializable;

/**
 * The Boat class represents an individual boat with identifying and financial data.
 * @author Ashley Howe-Smith
 * @version 1.0
 */
public class Boat implements Serializable {
    private BoatType type;
    private String name;
    private int year;
    private String makeModel;
    private int lengthFeet;
    private double purchasePrice;
    private double expenses;

    /**
     * Constructs a Boat object.
     * @param type is the type of the boat
     * @param name is the boat's name
     * @param year is the manufacturing year
     * @param makeModel is the make and model
     * @param lengthFeet is the boat's length in feet
     * @param purchasePrice is the purchase price
     */
    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }//end of Boat method

    /**
     * Creates a Boat instance from a single line of data.
     * @param line is a line of CSV text
     */
    public static Boat fromCSV(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }//end of if statement

        String[] parts = line.split(",");
        if (parts.length < 6) {
            System.out.println("Invalid CSV line: " + line);
            return null;
        }//end of if statement

        try {

            BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
            String name = parts[1].trim();
            int year = Integer.parseInt(parts[2].trim());
            String makeModel = parts[3].trim();
            int length = Integer.parseInt(parts[4].trim());
            double price = Double.parseDouble(parts[5].trim());

            return new Boat(type, name, year, makeModel, length, price);
        } catch (IllegalArgumentException e) {
            System.out.println("Error parsing CSV line: " + line);
            return null;
        }//end of try catch statement

    } //end of fromCSV

    /** @return the boat's name */
    public String getName() {

        return name;

    }//end of getName method

    /** @return the purchase price */
    public double getPurchasePrice() {

        return purchasePrice;

    }//end of getPurchasePrice method

    /** @return the total expenses spent on this boat */
    public double getExpenses() {

        return expenses;

    }//end of getExpenses method

    /**
     * Adds an expense to this boat's total.
     * @param amt is the amount to add.
     */
    public void addExpense(double amt) {

        expenses += amt;

    }//end of addExpense method

    @Override
    public String toString() {
        return String.format("    %-7s %-20s %4d %-10s %3d' : Paid $ %9.2f : Spent $ %9.2f",
                type, name, year, makeModel, lengthFeet, purchasePrice, expenses);
    }//end of toString method

}//end of Boat class
