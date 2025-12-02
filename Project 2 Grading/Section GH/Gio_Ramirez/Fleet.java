import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fleet implements Serializable {

    // Serialization support
    private static final long serialVersionUID = 1L;

    // Instance data
    private final List<Boat> boats;

    // Constructors
    public Fleet() {
        this.boats = new ArrayList<>();
    }

    // Modification methods
    public void addBoat(Boat boat) {
        boats.add(boat);
    }

    public boolean removeBoatByName(String name) {
        Boat boat = findBoatByName(name);
        if (boat != null) {
            boats.remove(boat);
            return true;
        }
        return false;
    }

    // Query methods

    public Boat findBoatByName(String name) {
        if (name == null) {
            return null;
        }
        for (Boat boat : boats) {
            if (boat.getName().equalsIgnoreCase(name.trim())) {
                return boat;
            }
        }
        return null;
    }

    public double getTotalPurchasePrice() {
        double sum = 0.0;
        for (Boat boat : boats) {
            sum += boat.getPurchasePrice();
        }
        return sum;
    }

    public double getTotalExpenses() {
        double sum = 0.0;
        for (Boat boat : boats) {
            sum += boat.getExpenses();
        }
        return sum;
    }

    // Reporting/display
    public void printReport() {
        System.out.println();
        System.out.println("Fleet report:");

        // One line per boat.
        for (Boat boat : boats) {
            // Example:
            //     POWER   Big Brother          2019 Mako        20' : Paid $ 12989.56 : Spent $     0.00
            System.out.printf(
                    "    %-7s %-20s %4d %-12s %3d' : Paid $ %8.2f : Spent $ %8.2f%n",
                    boat.getType(),
                    boat.getName(),
                    boat.getYearOfManufacture(),
                    boat.getMakeModel(),
                    boat.getLengthFeet(),
                    boat.getPurchasePrice(),
                    boat.getExpenses()
            );
        }

        // "Total" line: use the same column layout, but leave most fields blank
        // so that the payment and expense columns line up perfectly.
        System.out.printf(
                "    %-7s %-20s %4s %-12s %3s  : Paid $ %8.2f : Spent $ %8.2f%n",
                "Total", "", "", "", "",
                getTotalPurchasePrice(),
                getTotalExpenses()
        );
        System.out.println();
    }
} // End of Fleet class
