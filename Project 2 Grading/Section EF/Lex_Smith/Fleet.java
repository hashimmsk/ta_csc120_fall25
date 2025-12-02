import java.io.*;
import java.util.ArrayList;

/**
 * Represents the fleet of boats.
 */

public class Fleet {

    private ArrayList<Boat> boats = new ArrayList<>();

    public void addBoat(Boat b) {
        boats.add(b);
    }

    /**
     * Removes a boat by name. Case-insensitive.
     * @param name
     * @return
     */

    public boolean removeBoatByName(String name) {
        Boat b = findBoat(name);
        if (b == null) return false;
        boats.remove(b);
        return true;
    }

    /**
     * Finds a boat case-insensitively.
     * @param name
     * @return
     */

    public Boat findBoat(String name) {
        for (Boat b : boats) {
            if (b.getName().equalsIgnoreCase(name)) return b;
        }
        return null;
    }

    /**
     * Prints the fleet report.
     */

    public void printReport() {
        System.out.println("\nFleet report:");

        double totalPaid = 0;
        double totalSpent = 0;

        for (Boat b : boats) {
            System.out.println("   " + b);
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        System.out.printf("    %-45s : Paid $ %9.2f : Spent $ %9.2f\n\n", "  Total", totalPaid, totalSpent);

    }

    /**
     * Loads boats from a CSV file.
     * CSV format: TYPE,NAME,YEAR,MAKE,FLEET,PRICE,EXPENSES
     * @param filename
     */

    public void loadFromCSV(String filename) {
        File file = new File(filename);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                BoatType type = BoatType.valueOf(p[0].trim().toUpperCase());
                String name = p[1].trim();
                int year = Integer.parseInt(p[2].trim());
                String make = p[3].trim();
                int len = Integer.parseInt(p[4].trim());
                double price = Double.parseDouble(p[5].trim());

                double expenses = (p.length > 6) ? Double.parseDouble(p[6].trim()) : 0.0;

                boats.add(new Boat(type, name, year, make, len, price, expenses));
            }
        }
        catch (Exception e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        }
    }

    /**
     * Writes fleet to CSV file.
     * @param filename
     */

    public void saveToCSV(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Boat b : boats) {
                pw.println(b.toCSV());
            }
        }
        catch (Exception e) {
            System.err.println("Error saving CSV: " + e.getMessage());
        }
    }
}
