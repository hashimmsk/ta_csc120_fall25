import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Fleet class represents a collection of Boat objects.
 * It provides options for the user to add, remove, search, and print fleet details.
 * @author Ashley Howe-Smith
 * @version 1.0
 */
public class Fleet implements Serializable {
    private ArrayList<Boat> boats = new ArrayList<>();

    /**
     * Adds a boat to the fleet.
     * @param b is the Boat to add.
     */
    public void addBoat(Boat b) {
        boats.add(b);
    }//end of addBoat method

    /**
     * Removes a boat from the fleet by name
     * @param name is the name of the boat to remove.
     */
    public boolean removeBoat(String name) {
        Boat toRemove = findBoat(name);
        if (toRemove != null) {
            boats.remove(toRemove);
            return true;
        }//end of if statement
        return false;
    }//end of removeBoat method

    /**
     * Finds a boat by its name
     * @param name is the name of the boat to find.
     */
    public Boat findBoat(String name) {
        for (int i = 0; i < boats.size(); i++) {
            Boat b = boats.get(i);
            if (b.getName().equalsIgnoreCase(name)) {
                return b;
            }//end of if statement
        }//end of for loop
        return null;
    }//end of findBoat method

    /**
     * Prints a report of all boats in the fleet, including total purchase and expense summaries.
     */
    public void printFleet() {
        System.out.println("\nFleet report:");
        double totalPaid = 0;
        double totalSpent = 0;
        for (int i = 0; i < boats.size(); i++) {
            Boat b = boats.get(i);
            System.out.println(b);
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }//end of for loop
        System.out.printf("    Total                                             : Paid $ %9.2f : Spent $ %9.2f%n%n",
                totalPaid, totalSpent);
    }//end of printFleet method


}//end of Fleet class
