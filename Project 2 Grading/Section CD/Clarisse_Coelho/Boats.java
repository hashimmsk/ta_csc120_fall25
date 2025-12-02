import java.io.Serializable;

/**
 * Creates and handles the Boats Objects
 * @author Clarisse Coelho
 */

public class Boats implements Serializable {


    private String name;
    public enum typeOfBoat {POWER,SAILING}
    private typeOfBoat boatType;
    private int year;
    private String model;
    private int length;
    private double purchasePrice;
    private double maintenanceExpenses;


    /**
     * Default Constructor with no parameters.
     */
    public Boats(){

        name = "Unknown";
        year = 0;
        model = "Unknown";
        length = 0;
        purchasePrice = 0.0;
        maintenanceExpenses = 0.0;

    }

    /**
     * Parametrized Default Constructor for a new Boat object.
     *
     * @param boatType            type of the boat (from enum)
     * @param name                name of the boat
     * @param year                year the boat was manufactured
     * @param model               model of the boat
     * @param length              length of the boat in feet
     * @param purchasePrice       purchase price of the boat
     * @param maintenanceExpenses expected maintenance expenses of the boat
     */
    public Boats(typeOfBoat boatType, String name, int year, String model, int length,
                 double purchasePrice, double maintenanceExpenses) {

        this.name = name;
        this.boatType = boatType;
        this.year = year;
        this.model = model;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.maintenanceExpenses = maintenanceExpenses;
    }


    //=====Setters

    /**
     * Edits the name of the boat object to the new value provided.
     *
     * @param name                name of the boat
     */
    public void setName(String name){this.name = name;}

    /**
     * Edits the boatType of the boat object to the new value provided.
     *
     * @param boatType            type of the boat (from enum)
     */
    public void setBoatType(typeOfBoat boatType){this.boatType = boatType;}

    /**
     * Edits the year of the boat object to the new value provided.
     *
     * @param year                year the boat was manufactured
     */
    public void setYear(int year){this.year = year;}

    /**
     * Edits the model of the boat object to the new value provided.
     *
     * @param model               model of the boat
     */
    public void setModel(String model){this.model = model;}

    /**
     * Edits the length of the boat object to the new value provided.
     *

     * @param length              length of the boat in feet

     */
    public void setLength(int length){this.length = length;}

    /**
     * Edits the Purchase Price of the boat object to the new value provided.
     *
     * @param purchasePrice       purchase price of the boat
     */
    public void setPurchasePrice(double purchasePrice){this.purchasePrice = purchasePrice;}

    /**
     * Edits the maintenance Expenses of the boat object to the new value provided.
     * @param maintenanceExpenses expected maintenance expenses of the boat
     */
    public void setMaintenanceExpenses(double maintenanceExpenses){this.maintenanceExpenses = maintenanceExpenses;}

    //=====Getters
    /**
     * @return name of the boat.
     */
    public String getName() {
        return name;
    }

    /**
     * @return type of Boat.
     */
    public typeOfBoat getBoatType() {
        return boatType;
    }

    /**
     * @return year of the boat.
     */
    public int getYear() {
        return year;
    }

    /**
     * @return model of the boat.
     */
    public String getModel() {
        return model;
    }

    /**
     * @return length of the boat.
     */
    public int getLength() {
        return length;
    }

    /**
     * @return PurchasePrice of the Boat.
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * @return total Maintenance Expenses so far for a boat.
     */
    public double getMaintenanceExpenses() {
        return maintenanceExpenses;
    }



    //====TO STRING

    /**
     * @return all the information of one boat object.
     */
    public String toString() {

        return String.format(
                "  %-8s %-12s %-6d %-8s %2d' : Paid $ %8.2f : Spent $ %1.2f",
                boatType,
                name,
                year,
                model,
                length,
                purchasePrice,
                maintenanceExpenses
        );

    }

}// end of Boats Class




