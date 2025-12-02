// make the Boat class serializable so boats can be stored and accessed later
import java.io.Serializable;

/**
 * Generic boat
 * @author Andrew Ortego
 */
public class Boat implements Serializable {

    /**
     * The type of the boat, either sailing or power
     */
    public enum BoatType {SAILING, POWER}

    /**
     * The type of the boat
     */
    private BoatType boat_type;
    /**
     * The name of the boat
     */
    private String name;
    /**
     * The year of manufacture of the boat
     */
    private int yearOfManufacture;
    /**
     * The model of the boat
     */
    private String model;
    /**
     * The length of the boat, in feet
     */
    private int length;
    /**
     * The price of the boat, in dollars ($)
     */
    private double price;
    /**
     * The amount of money spent on the boat
     */
    private double expense;

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Initial value constructor
     * @param name Name of the boat
     * @param yearOfManufacture Year of manufacture of the boat
     * @param model Model of the boat
     * @param length Length of the boat
     * @param price Price of the boat
     * @param expense Amount spent on the boat
     * @param boat_type Type of the boat
     */
    public Boat(String name, int yearOfManufacture, String model, int length,
                 double price, double expense, BoatType boat_type) {
        this.name = name;
        this.yearOfManufacture = yearOfManufacture;
        this.model = model;
        this.length = length;
        this.price = price;
        this.expense = expense;
        this.boat_type = boat_type;
    } // end of the Boat constructor

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Getter method for the expense
     * @return double with boat expense
     */
    public double getExpense() {
        return expense;
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Getter method for the price
     * @return double with boat price
     */
    public double getPrice() {
        return price;
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Getter method for the length
     * @return integer with boat length
     */
    public int getLength() {
        return length;
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Getter method for the model
     * @return String with boat model
     */
    public String getModel() {
        return model;
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Getter method for the year of manufacture
     * @return int with year of manufacture
     */
    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Getter method for the name
     * @return String with boat name
     */
    public String getName() {
        return name;
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Getter method for boat type
     * @return enum with boat type
     */
    public BoatType getBoat_type() {
        return boat_type;
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Set the new expense when a purchase is authorized
     * @param expense The amount to be spent on the boat
     */
    public void setExpense(double expense) {
        this.expense = expense;
    }

//--------------------------------------------------------------------------------------------------------------------

    /**
     * Return information about the boat.
     * @return String with boat's type, name, year of manufacture, model, length, price, and expense
     */
    @Override
    public String toString() {
        String row = String.format("    %-8s %-20s %5d %-12s %3d' : Paid $ %8.2f : Spent $ %10.2f",
                boat_type,
                name,
                yearOfManufacture,
                model,
                length,
                price,
                expense);
        return row;
    } // end of the toString method

} // end of the Boat class