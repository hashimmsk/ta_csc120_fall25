import java.io.Serializable;
import java.util.ArrayList;


/**
 * The Fleet class represents the entire collection of boats
 * owned by the club. It uses an ArrayList to store any number
 * of Boat objects.
 *
 * This class is Serializable so the whole fleet can be saved
 * to a file (FleetData.db) and loaded again later.
 *
 * @author Joseph Joannou
 * @version 1.0
 * @since 2025-12-1
 */
public class Fleet implements Serializable {

    private static final long serialVersionUID = 1L; // required for Serializable

    // List of all boats in the fleet.
    // Use an ArrayList to add/remove boats easily.
    private ArrayList<Boat> boats;


    /**
     * Creates an empty fleet with no boats yet.
     * The boats list is initialized so we can add boats later.
     */
    public Fleet() {
        boats = new ArrayList<Boat>(); // start with an empty list
    } // end of Fleet constructor


    /**
     * Adds a Boat object to the fleet.
     *
     * @param boat the Boat to add (must not be null)
     */
    public void addBoat(Boat boat) {
        if (boat != null) {
            boats.add(boat); // Add the new boat to the list
        }
    } // end of addBoat method


    /**
     * Returns the list of boats in the fleet.
     * This allows other parts of the program to see the fleet.
     *
     * @return the ArrayList of Boat objects
     */
    public ArrayList<Boat> getBoats() {
        return boats;
    } // end of getBoats method


    /**
     * Finds a boat in the fleet by its name (case-insensitive).
     *
     * @param name the name of the boat to search for
     * @return the Boat object if found, or null if not found
     */
    public Boat findBoatByName(String name) {
        for (Boat b : boats) {
            if (b.getName().equalsIgnoreCase(name)) { // Compare names ignoring case
                return b; // Found the matching boat
            }
        }
        return null; // No matching boat found
    } // end of findBoatByName method


    /**
     * Removes a boat from the fleet by its name (case-insensitive).
     *
     * @param name the name of the boat to remove
     * @return true if the boat was found and removed, false otherwise
     */
    public boolean removeBoatByName(String name) {
        Boat boatToRemove = findBoatByName(name); // Try to find the boat
        if (boatToRemove != null) {
            boats.remove(boatToRemove); // Remove it from the list
            return true; // Removal successful
        } else {
            return false; // Boat not found
        }
    } // end of removeBoatByName method


    /**
     * Calculates the total purchase price of all boats in the fleet.
     *
     * @return total amount paid for all boats
     */
    public double getTotalPurchasePrice() {
        double total = 0.0;
        for (Boat b : boats) {
            total += b.getPurchasePrice();
        }
        return total;
    } // end of getTotalPurchasePrice method


    /**
     * Calculates the total expenses of all boats in the fleet.
     *
     * @return total expenses spent on all boats
     */
    public double getTotalExpenses() {
        double total = 0.0;
        for (Boat b : boats) {
            total += b.getExpenses();
        }
        return total;
    } // end of getTotalExpenses method



} // end of the Fleet class
