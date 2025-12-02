import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Fleet represents a collection of Boat objects.
 * This class manages the list of boats, provides methods to add, find, and
 * calculate totals for all the boats in teh fleet, and generates a formatted report.
 *
 * @author sarahlaughlin
 * @version 1.0
 * @see Boat
 */

public class Fleet implements Serializable {

    private final ArrayList<Boat> boats;

    /**
     * Construct an empty fleet to start.
     */

    public Fleet() {
        boats = new ArrayList<>();
    } // end Fleet array list

    /**
     * Add a boat to the fleet.
     * @param newBoat is the new boat to add
     */
    public void addBoat(Boat newBoat) {
        boats.add(newBoat);
    } //end addBoat method

    /**
     * Get a read-only view of the boats.
     * @return list of the boats
     */

    public List<Boat> getBoats() {
        return boats;
    } // end of Boat List

    /**
     * Find a boat by the name, make sure it is case-insensitive.
     * @param nameOfBoat is the name of boat to search for
     * @return the Boat if found, otherwise it is null
     */

    public Boat findBoatByName(String nameOfBoat) {
        int index;
        int size;
        Boat boat;
        Boat foundBoat;
        String searchName;
        String boatName;

        foundBoat = null;
        searchName = nameOfBoat.toLowerCase();
        size = boats.size();

        index = 0;
        while (index < size && foundBoat == null ) {
            boat = boats.get(index);
            boatName = boat.getNameOfBoat().toLowerCase();

            if (boatName.equals(searchName)) {
                foundBoat = boat;
            } // end if
            index = index + 1;
        }// end while loop
        return foundBoat;

    } // end of findBoatByName method

    /**
     * Remove a boat by name (case-insensitive).
     * @param nameOfBoat name of the boat to remove
     * @return true if a boat was removed, false if not found
     */
    public boolean removeBoatByName(String nameOfBoat) {
        int index;
        int size;
        Boat boat;
        String searchName;
        String boatName;

        searchName = nameOfBoat.toLowerCase();
        size = boats.size();

        index = 0;
        while (index < size) {
            boat = boats.get(index);
            boatName = boat.getNameOfBoat().toLowerCase();

            if (boatName.equals(searchName)) {
                boats.remove(index);
                return true;
            }
            index = index + 1;
        }

        return false;
    } // end removeBoatByName

    /**
     * Request an expense for a boat by their name.
     * @param nameOfBoat is the boat name
     * @param amount is the amount to spend
     * @return true if expense is authorized and added, otherwise false
     */

    public boolean requestExpense(String nameOfBoat, double amount) {

        Boat boat;
        boolean okToSpend;

        boat = findBoatByName(nameOfBoat);

        if (boat == null) {
            okToSpend = false;
        } // end if
        else {
            okToSpend = boat.additionalExpense(amount);
        } // end else

        return okToSpend;

    } // end of requestExpense method

    /**
     * Compute total purchase price for all boats
     * @return total purchase price
     */

    public double totalPaid() {
        double total;
        int index;
        int size;
        Boat boat;

        total = 0.0;
        size = boats.size();
        index = 0;

        while (index < size) {
            boat = boats.get(index);
            total = total + boat.getPurchasePrice();
            index = index + 1;
        } // end while loop
        return total;
    } // end of totalPaid method

    /**
     * Compute total expenses for all boats.
     * @return total expenses
     */
    public double totalSpent() {
        double total;
        int index;
        int size;
        Boat boat;

        total = 0.0;
        size = boats.size();
        index = 0;

        while (index < size) {
            boat = boats.get(index);
            total = total + boat.getExpenses();
            index = index + 1;
        }

        return total;
    } // end of totalSpent method

    /**
     * Build full fleet report string, all of the boats plus the totals.
     * @return multi-line report
     */

    public String formattedReport() {

        StringBuilder builder;
        int index;
        int size;
        Boat boat;
        double totalPaid;
        double totalSpent;
        String totalPaidString;
        String totalSpentString;
        String totalLine;

        builder = new StringBuilder();

        builder.append("Fleet report:\n");

        size = boats.size();
        index = 0;

        while (index < size) {
            boat = boats.get(index);
            builder.append(boat.toString());
            builder.append("\n");
            index = index + 1;
        } // end while loop

        totalPaid = totalPaid();
        totalSpent = totalSpent();

        totalPaidString = String.format("%10.2f", totalPaid);
        totalSpentString = String.format("%10.2f", totalSpent);

        totalLine = "    Total                                             : Paid $ " + totalPaidString + " : Spent $ " + totalSpentString;

        builder.append(totalLine);
        builder.append("\n");

        return builder.toString();

    } // end of formattedReport method

} // end of the Fleet class
