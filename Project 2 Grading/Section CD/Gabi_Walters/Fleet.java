import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds the entire boat fleet.
 */
public class Fleet implements Serializable {

    private ArrayList<Boat> boats = new ArrayList<>();

    public void addBoat(Boat b) {
        boats.add(b);
    }

    public boolean removeBoat(String name) {
        Boat b = findBoat(name);
        if (b != null) {
            boats.remove(b);
            return true;
        }
        return false;
    }

    public Boat findBoat(String name) {
        for (Boat b : boats) {
            if (b.getName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }

    public double totalPaid() {
        double sum = 0;
        for (Boat b : boats) sum += b.getPurchasePrice();
        return sum;
    }

    public double totalSpent() {
        double sum = 0;
        for (Boat b : boats) sum += b.getExpenses();
        return sum;
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nFleet report:\n");
        for (Boat b : boats) {
            sb.append(b).append("\n");
        }
        sb.append(String.format(
                "    Total                                             : Paid $ %10.2f : Spent $ %10.2f\n",
                totalPaid(), totalSpent()));
        return sb.toString();
    }

}

