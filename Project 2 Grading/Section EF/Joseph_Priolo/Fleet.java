import java.io.Serializable;
import java.util.ArrayList;

public class Fleet implements Serializable {
    private ArrayList<Boat> boats;

    public Fleet() {
        boats = new ArrayList<>();
    }

    public void addBoat(Boat b) {
        boats.add(b);
    }

    public boolean removeBoat(String name) {
        for (int i = 0; i < boats.size(); i++) {
            if (boats.get(i).getName().equalsIgnoreCase(name)) {
                boats.remove(i);
                return true;
            }
        }
        return false;
    }

    public Boat findBoat(String name) {
        for (Boat b : boats) {
            if (b.getName().equalsIgnoreCase(name)) return b;
        }
        return null;
    }

    public void printReport() {
        System.out.println();
        System.out.println("Fleet report:");
        double totalPaid = 0;
        double totalSpent = 0;
        for (Boat b : boats) {
            System.out.println("    " + b.toString());
            totalPaid += b.getAmountPaid();
            totalSpent += b.getAmountSpent();
        }
        System.out.printf("    %-47s : Paid $ %8.2f : Spent $ %8.2f\n", "Total", totalPaid, totalSpent);
        System.out.println();
    }
}
