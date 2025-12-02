import java.util.ArrayList;

/**
 * Manages a collection of Boat objects, providing operations to add,
 * remove, search, and print fleet data.
 *
 * @author Isaac Tetel
 * @version 1
 * @see Boat
 */
public class FleetManager {

    /** A list holding the fleet of boats. */
    private ArrayList<Boat> fleet;

    /**
     * Constructs an empty FleetManager.
     */
    public FleetManager() {
        fleet = new ArrayList<Boat>();
    }

    /**
     * Adds a boat to the fleet using data from a CSV-formatted line.
     *
     * @param csvLine A comma-separated list of boat attributes
     * @exception IllegalArgumentException If the CSV line is malformed
     * @see Boat
     */
    public void addBoatFromCSV(String csvLine) {
        String[] p = csvLine.split(",");

        if (p.length != 6) {
            throw new IllegalArgumentException("CSV must contain 6 fields");
        }

        BoatType type = BoatType.valueOf(p[0].trim().toUpperCase());
        String name = p[1].trim();
        int year = Integer.parseInt(p[2].trim());
        String make = p[3].trim();
        double tempLen = Double.parseDouble(p[4]);
        int length = (int) tempLen;
        double price = Double.parseDouble(p[5].trim());

        Boat b = new Boat(type, name, year, make, length, price);
        fleet.add(b);
    }

    /**
     * Removes a boat by name, ignoring case.
     *
     * @param name Name of the boat to remove
     * @return true if a boat was removed, false otherwise
     */
    public boolean removeBoat(String name) {
        for (int i = 0; i < fleet.size(); i++) {
            if (fleet.get(i).getName().equalsIgnoreCase(name)) {
                fleet.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for a boat in the fleet by name.
     *
     * @param name Name of the boat to search for
     * @return The matching Boat, or null if not found
     */
    public Boat findBoat(String name) {
        for (Boat b : fleet) {
            if (b.getName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Prints a formatted report of the entire fleet.
     */
    public void printFleetReport() {
        System.out.println("\nFleet report:");

        double totalPaid = 0;
        double totalSpent = 0;

        for (Boat b : fleet) {
            System.out.println("    " + b.toString());
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        System.out.printf(
                "    %-44s : Paid $ %9.2f : Spent $ %9.2f%n%n",
                "Total", totalPaid, totalSpent
        );
    }

    /**
     * Returns the internal fleet list.
     *
     * @return The list of boats
     */
    public ArrayList<Boat> getFleet() {
        return fleet;
    }

    /**
     * Sets the fleet list to a new list (used when loading from disk).
     *
     * @param f New fleet list
     */
    public void setFleet(ArrayList<Boat> f) {
        fleet = f;
    }
}