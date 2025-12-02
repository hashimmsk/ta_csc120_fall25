import java.io.Serializable;

/**
 * @author Elijah Corpuz
 * The Boat class represents a single boat in the fleet.
 * It stores information such as type, name, year, model,
 * size, purchase price, and expenses.
 */
public class Boat implements Serializable {

    private String type;
    private String name;
    private int year;
    private String model;
    private double lengthInFeet;
    private double price;
    private double expenses;

    /**
     * Constructs a Boat object using provided values.
     *
     * @param type the type of boat (POWER or SAILING)
     * @param name the name of the boat
     * @param year the year the boat was made
     * @param model the model of the boat
     * @param lengthInFeet the boat length in feet
     * @param price the purchase price
     */
    public Boat(String type, String name, int year, String model,
                double lengthInFeet, double price) {

        this.type = type;
        this.name = name;
        this.year = year;
        this.model = model;
        this.lengthInFeet = lengthInFeet;
        this.price = price;
        this.expenses = 0.0;

    } // end of Boat constructor


    /**
     * Creates a Boat object from a CSV row.
     *
     * @param csv a CSV string in the format:
     *            TYPE,NAME,YEAR,MODEL,LENGTH,PRICE
     * @return a new Boat object
     */
    public static Boat fromCSV(String csv) {

        String[] parts = csv.split(",");

        String type = parts[0];
        String name = parts[1];
        int year = Integer.parseInt(parts[2]);
        String model = parts[3];
        double length = Double.parseDouble(parts[4]);
        double price = Double.parseDouble(parts[5]);

        return new Boat(type, name, year, model, length, price);

    } // end of fromCSV


    /**
     * Adds an expense to the total spent.
     *
     * @param amount the expense amount
     */
    public void addExpense(double amount) {
        expenses += amount;
    } // end of addExpense


    /**
     * Returns remaining budget before reaching purchase price.
     *
     * @return remaining budget
     */
    public double remainingBudget() {
        return price - expenses;
    } // end of remainingBudget


    /** @return the boat type */
    public String getType() {
        return type;
    }

    /** @return the boat name */
    public String getName() {
        return name;
    }

    /** @return the model year */
    public int getYear() {
        return year;
    }

    /** @return the boat model */
    public String getModel() {
        return model;
    }

    /** @return the length in feet */
    public double getLengthInFeet() {
        return lengthInFeet;
    }

    /** @return the purchase price */
    public double getPrice() {
        return price;
    }

    /** @return total expenses */
    public double getExpenses() {
        return expenses;
    }

} // end of Boat class
