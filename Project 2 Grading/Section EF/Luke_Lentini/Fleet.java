import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Fleet.java
 * Container for boats in the club.
 */
public class Fleet implements Serializable {
    private static final long serialVersionUID = 2L;

    private final ArrayList<Boat> boats = new ArrayList<>();

    public Fleet() { }

    public List<Boat> getBoats() {
        return boats;

    }


    public void addBoat(Boat boat) {
        boats.add(boat);
    }

    /**
     * Remove a boat by name (case-insensitive). Removes the first match.
     * @param name name to remove
     * @return true if removed
     */
    public boolean removeBoatByName(String name) {
        Iterator<Boat> it = boats.iterator();
        while (it.hasNext()) {
            Boat b = it.next();
            if (b.getName().equalsIgnoreCase(name.trim())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Find a boat by name (case-insensitive).
     * @param name name to find
     * @return Boat or null
     */
    public Boat findBoatByName(String name) {
        for (Boat b : boats) {
            if (b.getName().equalsIgnoreCase(name.trim())) return b;
        }
        return null;
    }

    public double totalPaid() {
        double sum = 0.0;
        for (Boat b : boats) sum += b.getPurchasePrice();
        return sum;
    }

    public double totalSpent() {
        double sum = 0.0;
        for (Boat b : boats) sum += b.getExpenses();
        return sum;
    }

    /**
     * Build a multi-line fleet report.
     */
    public String getReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nFleet report:\n");
        for (Boat b : boats) {
            sb.append(b.toReportLine()).append('\n');
        }
        sb.append(String.format("    Total                                             : Paid $ %8.2f : Spent $ %8.2f%n",
                totalPaid(), totalSpent()));
        return sb.toString();
    }

    /**
     * Parse a CSV line and produce a Boat.
     * Expected format: TYPE,Name,Year,Make,Length,PurchasePrice
     * Example: SAILING,Finesse,1974,Tartan,34,9200.50
     *
     * This method does minimal validation and assumes "reasonable" data per specification.
     */
    public static Boat fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length != 6) {
            throw new IllegalArgumentException("CSV line must have 6 fields: " + csvLine);
        }
        BoatType type = BoatType.fromString(parts[0].trim());
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String make = parts[3].trim();
        int length = Integer.parseInt(parts[4].trim());
        double price = Double.parseDouble(parts[5].trim());
        Boat b = new Boat(type, name, year, make, length, price);
        return b;
    }
}
