import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the fleet of boats in the sailing club.
 * Uses an ArrayList to hold all SingleBoat objects.
 *
 * @see SingleBoat
 * @author Aakash Singh
 * @version 1.0
 */

public class FleetOfBoats implements Serializable {

    // ----------------------------------------------------------------------

    private final ArrayList<SingleBoat> boats;

    // ----------------------------------------------------------------------

    /**
     * Constructs an empty fleet of boats.
     */

    public FleetOfBoats() {
        boats = new ArrayList<>();
    } //End of FleetOfBoats constructor

    // ----------------------------------------------------------------------

    /**
     * Adds a boat to the fleet.
     *
     * @param boat The SingleBoat object to add
     */

    public void addBoat(SingleBoat boat) {
        boats.add(boat);
    } //End of addBoat method

    // ----------------------------------------------------------------------

    /**
     * Removes a boat from the fleet by name (case-insensitive).
     *
     * @param name The name of the boat to remove
     * @return true if a boat was removed, false otherwise
     */

    public boolean removeBoat(String name) {
        for (int i = 0; i < boats.size(); i++) {
            if (boats.get(i).getName().equalsIgnoreCase(name)) {
                boats.remove(i);
                return true;
            }
        }
        return false;
    } //End of removeBoat method

    // ----------------------------------------------------------------------

    /**
     * Finds a boat in the fleet by name (case-insensitive).
     *
     * @param name The name of the boat to find
     * @return The SingleBoat object if found, null otherwise
     */

    public SingleBoat findBoat(String name) {
        for (SingleBoat boat : boats) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }
        return null;
    } //End of findBoat method

    // ----------------------------------------------------------------------

    /**
     * Returns the list of all boats in the fleet.
     *
     * @return ArrayList of SingleBoat
     */

    public ArrayList<SingleBoat> getBoats() {
        return boats;
    } //End of getBoats method
} //End of FleetOfBoats class