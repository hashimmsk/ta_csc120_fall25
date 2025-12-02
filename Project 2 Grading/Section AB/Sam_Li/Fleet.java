import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a fleet of boats.
 */
public class Fleet implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Boat> boats = new ArrayList<>();

    public Fleet() {}

    /** Adds a boat to the fleet. */
    public void addBoat(Boat b) { boats.add(b); }

    /** Removes a boat by name. Returns true if removed. */
    public boolean removeBoat(String boatName) {
        Boat b = findBoat(boatName);
        if (b != null) {
            boats.remove(b);
            return true;
        }
        return false;
    }

    /** Finds a boat by name (case-insensitive). */
    public Boat findBoat(String boatName) {
        if (boatName == null) return null;
        for (Boat b : boats) {
            if (b.getName().equalsIgnoreCase(boatName.trim())) return b;
        }
        return null;
    }

    /** Prints the fleet and totals. */
    public void printFleet() {
        if (boats.isEmpty()) {
            System.out.println("Fleet is empty.");
            return;
        }
        System.out.println("\nFleet report:");
        double totalPaid = 0.0;
        double totalSpent = 0.0;
        for (Boat b : boats) {
            System.out.println(b);
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }
        System.out.printf("    Total                                             : Paid $ %8.2f : Spent $ %8.2f\n\n",
                totalPaid, totalSpent);
    }

    private static final String DB_NAME = "FleetData.db";

    /** Loads a fleet from the database file. */
    public static Fleet load() {
        File file = new File(DB_NAME);
        if (!file.exists()) return new Fleet();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Fleet) return (Fleet) obj;
            else return new Fleet();
        } catch (Exception e) {
            return new Fleet();
        }
    }

    /** Saves a fleet to the database file. */
    public static void save(Fleet fleet) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_NAME))) {
            oos.writeObject(fleet);
            oos.flush();
        } catch (IOException e) {
            // ignore
        }
    }

    public int size() { return boats.size(); }
}