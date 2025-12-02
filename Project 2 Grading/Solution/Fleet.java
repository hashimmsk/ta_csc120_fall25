import java.io.Serializable;
import java.util.ArrayList;
//========================================================================================================
/**
 * Represents a fleet of boats, including functionality for adding, removing, and retrieving boats,
 * as well as calculating the total purchase cost and expenses for all boats.
 * The fleet is stored as an ArrayList of Boat objects.
 *
 * @author Hashim Shahzad Khan
 */
public class Fleet implements Serializable {
    //----------------------------------------------------------------------------------------------------
    /**
     * The serial version UID for serialization compatibility
     */
    private static final long serialVersionUID = 1L;
    //----------------------------------------------------------------------------------------------------
    /**
     * List of boats in the fleet
     */
    private ArrayList<Boat> boats;
    //----------------------------------------------------------------------------------------------------
    /**
     * Constructor that initializes the fleet with an empty list of boats.
     */
    public Fleet() {

        boats = new ArrayList<>();

    } // end of the constructor
    //----------------------------------------------------------------------------------------------------
    /**
     * Adds a new boat to the fleet.
     * @param boat The boat to be added
     */
    public void addBoat(Boat boat) {

        boats.add(boat);

    } // end of the addBoat method
    //----------------------------------------------------------------------------------------------------
    /**
     * Removes a boat from the fleet by its name.
     * @param name The name of the boat to be removed
     * @return True if the boat was found and removed, false otherwise
     */
    public boolean removeBoat(String name) {

        return boats.removeIf(boat -> boat.getName().equalsIgnoreCase(name));

    } // end of the removeBoat method
    //----------------------------------------------------------------------------------------------------
    /**
     * Retrieves a boat from the fleet by its name.
     * @param name The name of the boat to retrieve
     * @return The boat with the specified name, or null if no boat is found
     */
    public Boat getBoatByName(String name) {

        for (Boat boat : boats) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }

        return null;

    } // end of the getBoatByName method
    //----------------------------------------------------------------------------------------------------
    /**
     * Calculates the total amount spent on all boats in the fleet.
     * @return The total expenses for all boats
     */
    public double totalSpent() {

        return boats.stream().mapToDouble(Boat::getExpenses).sum();

    } // end of the totalSpent method
    //----------------------------------------------------------------------------------------------------
    /**
     * Calculates the total purchase cost of all boats in the fleet.
     * @return The total purchase cost for all boats
     */
    public double totalPurchaseCost() {

        return boats.stream().mapToDouble(Boat::getPurchasePrice).sum();

    } // end of the totalPurchaseCost method
    //----------------------------------------------------------------------------------------------------
    /**
     * Gets the list of all boats in the fleet.
     * @return The list of boats
     */
    public ArrayList<Boat> getBoats() {

        return boats;

    } // end of the getBoats method
    //----------------------------------------------------------------------------------------------------
    /**
     * Returns a string representation of the fleet, including details of each boat
     * and the total purchase cost and expenses for the fleet.
     * @return A formatted string representing the fleet
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Fleet report:\n");

        for (Boat boat : boats) {
            sb.append("    ").append(boat.toString()).append("\n");
        }

        sb.append(String.format("    Total                                              " +
                        "   : Paid $%10.2f : Spent $%10.2f\n",
                totalPurchaseCost(), totalSpent()));

        return sb.toString();

    } // end of the toString method
    //----------------------------------------------------------------------------------------------------
} // end of the Fleet class
//========================================================================================================
