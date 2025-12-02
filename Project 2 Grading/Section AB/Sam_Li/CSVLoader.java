import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Loads a Fleet from a CSV file.
 */
public class CSVLoader {

    /**
     * Reads a CSV file and returns a Fleet with all boats.
     */
    public static Fleet loadFromCSV(String filename) {
        Fleet fleet = new Fleet();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int ln = 0;
            while ((line = br.readLine()) != null) {
                ln++;
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    String[] p = line.split("\\s*,\\s*");
                    if (p.length != 6) continue;
                    Boat b = new Boat(
                            BoatType.valueOf(p[0].trim().toUpperCase()),
                            p[1].trim(),
                            Integer.parseInt(p[2].trim()),
                            p[3].trim(),
                            Integer.parseInt(p[4].trim()),
                            Double.parseDouble(p[5].trim())
                    );
                    fleet.addBoat(b);
                } catch (Exception ex) {
                    // ignore parse errors
                }
            }
        } catch (Exception e) {
            // ignore file errors
        }
        return fleet;
    }
}