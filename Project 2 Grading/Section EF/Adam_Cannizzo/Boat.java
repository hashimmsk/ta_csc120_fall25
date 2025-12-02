import java.io.Serializable;

public class Boat implements Serializable{


    // The type of the boar (sailing or power)
    private BoatType type;

    // The name of the boat
    private String boatName;

    // The year the boat was made
    private int manufactureYear;

    // The make or the model of the boar
    private String makeMode;

    // The length of the boat in feet
    private double lengthFeet;

    // The purchase price of the boar
    private double purchasePrice;

    // The total expenses spent on the boat so far
    private double expenses;

    //-----------------------------------------------------------------------------------------------------------------
    // Constructor

    /**
     * This creates a new boat with the data given
     * @param type the type of the boat
     * @param boatName the name of the boar
     * @param manufactureYear year of manufacture
     * @param makeMode make and model
     * @param lengthFeet length of the boat in feet
     * @param purchasePrice purchase price of the boat
     * @param expenses expenses already spent
     */
    public Boat(BoatType type, String boatName, int manufactureYear, String makeMode, double lengthFeet,
                double purchasePrice, double expenses) {
        this.type = type;
        this.boatName = boatName;
        this.manufactureYear = manufactureYear;
        this.makeMode = makeMode;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Getters


    public BoatType getType() {
        return type;
    }

    public String getBoatName() {
        return boatName;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public String getMakeMode() {
        return makeMode;
    }

    public double getLengthFeet() {
        return lengthFeet;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getExpenses() {
        return expenses;
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Business Methods

    /**
     * Returns how much money is left to spend on the selected boat according to the Commodore's policy
     * @return returns the remaining budget
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    } // end of getRemainingBudget

    /**
     * Determines if the requested expense is allowed
     * @param amount - the amount of the requested expense
     * @return returns true if the amount is allowed to be spent, false if not
     */
    public boolean canSpend(double amount) {
        return amount <= getRemainingBudget();
    } // end of canSpend

    /**
     * Adds the given amount to the expenses for the boat so far
     * @param amount - the amount to add to total expenses
     */
    public void addExpense(double amount) {
        expenses = expenses + amount;
    } // end of addExpense

    //-----------------------------------------------------------------------------------------------------------------
    // ToString

    /**
     * Returns a formatted String for the fleet report
     * @return returns formatted boat information
     */
    @Override
    public String toString() {
        String typeString;
        typeString = type.toString();

        return String.format("    %-7s %-20s %4d %-10s %3.0f' : Paid $ %8.2f : Spent $ %8.2f", typeString, boatName,
                manufactureYear, makeMode, lengthFeet, purchasePrice, expenses);
    } // end of toString
} // end of Boat class
