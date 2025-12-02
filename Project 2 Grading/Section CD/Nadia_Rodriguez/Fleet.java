import java.io.Serializable;
import java.util.ArrayList;

/*
   Fleet Class

   Manages a collection of boats. Provides methods to add, remove,
   find boats, and display the fleet inventory with totals.
*/

public class Fleet implements Serializable {
    private ArrayList<Boat> boats;

    /**
     * Constructor for Fleet
     * Initializes an empty boat collection
     */
    public Fleet() {
        boats = new ArrayList<Boat>();
    }

    /**
     * Adds a boat to the fleet
     * @param boat The boat to add
     */
    public void addBoat(Boat boat) {
        boats.add(boat);
    }

    /**
     * Removes a boat from the fleet by name (case-insensitive)
     * @param name The name of the boat to remove
     * @return true if boat was found and removed, false otherwise
     */
    public boolean removeBoat(String name) {
        int index;

        for (index = 0; index < boats.size(); index = index + 1) {
            if (boats.get(index).getName().equalsIgnoreCase(name)) {
                boats.remove(index);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a boat by name (case-insensitive)
     * @param name The name of the boat to find
     * @return The Boat object if found, null otherwise
     */
    public Boat findBoat(String name) {
        int index;

        for (index = 0; index < boats.size(); index = index + 1) {
            if (boats.get(index).getName().equalsIgnoreCase(name)) {
                return boats.get(index);
            }
        }
        return null;
    }

    /**
     * Prints the fleet inventory with totals
     */
    public void printInventory() {
        int index;
        double totalPaid;
        double totalSpent;

        System.out.println();
        System.out.println("Fleet report:");

        for (index = 0; index < boats.size(); index = index + 1) {
            System.out.println("    " + boats.get(index).toString());
        }

        totalPaid = getTotalPurchasePrice();
        totalSpent = getTotalExpenses();

        System.out.printf(
                "    %-7s %-20s %4s %-12s %3s : Paid $%9.2f : Spent $%9.2f%n",
                "Total", "", "", "", "", totalPaid, totalSpent);
        System.out.println();
    }

    /**
     * Calculates the total purchase price of all boats
     * @return The sum of all purchase prices
     */
    public double getTotalPurchasePrice() {
        double total;
        int index;

        total = 0.0;
        for (index = 0; index < boats.size(); index = index + 1) {
            total = total + boats.get(index).getPurchasePrice();
        }
        return total;
    }

    /**
     * Calculates the total expenses of all boats
     * @return The sum of all expenses
     */
    public double getTotalExpenses() {
        double total;
        int index;

        total = 0.0;
        for (index = 0; index < boats.size(); index = index + 1) {
            total = total + boats.get(index).getExpenses();
        }
        return total;
    }

    /**
     * Gets the list of boats
     * @return The ArrayList of boats
     */
    public ArrayList<Boat> getBoats() {
        return boats;
    }
}