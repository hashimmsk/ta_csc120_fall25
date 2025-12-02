import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * FleetManager: main application class for the Fleet Management System.
 *
 * Only this class holds a Scanner instance (keyboard).
 */
public class FleetManager {
    private static final String DB_FILENAME = "FleetData.db";
    private ArrayList<Boat> fleet;

    public FleetManager() {
        fleet = new ArrayList<>();
    }

    /* File handling */

    public void loadFromCSV(String filename) throws IOException {
        ArrayList<Boat> newFleet = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineno = 0;
            while ((line = br.readLine()) != null) {
                lineno++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                BoatType type = BoatType.fromString(parts[0]);
                String name = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                String make = parts[3].trim();
                int length = Integer.parseInt(parts[4].trim());
                double price = Double.parseDouble(parts[5].trim());

                newFleet.add(new Boat(type, name, year, make, length, price));
            }
        }
        this.fleet = newFleet;
    }

    public void saveToDB() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILENAME))) {
            oos.writeObject(fleet);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean loadFromDB() {
        File f = new File(DB_FILENAME);
        if (!f.exists()) return false;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                this.fleet = (ArrayList<Boat>) obj;
                return true;
            }
        } catch (Exception e) {
            System.err.println("DB load error: " + e.getMessage());
        }
        return false;
    }

    /* Fleet operations */

    public void printFleetReport() {
        System.out.println();
        System.out.println("Fleet report:");
        double totalPaid = 0, totalSpent = 0;

        for (Boat b : fleet) {
            System.out.println(b.toFormattedString());
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getSpent();
        }

        System.out.printf(Locale.US,
                "    Total                                             : Paid $%10.2f : Spent $%10.2f%n%n",
                totalPaid, totalSpent);
    }

    public void addBoatFromCSVLine(String csvLine) {
        try {
            String[] parts = csvLine.split(",", -1);
            BoatType type = BoatType.fromString(parts[0]);
            String name = parts[1].trim();
            int year = Integer.parseInt(parts[2].trim());
            String make = parts[3].trim();
            int length = Integer.parseInt(parts[4].trim());
            double price = Double.parseDouble(parts[5].trim());

            fleet.add(new Boat(type, name, year, make, length, price));
        } catch (Exception e) {
            System.out.println("Invalid boat data.");
        }
    }

    public boolean removeBoatByName(String name) {
        for (int i = 0; i < fleet.size(); i++) {
            if (fleet.get(i).getName().equalsIgnoreCase(name)) {
                fleet.remove(i);
                return true;
            }
        }
        return false;
    }

    public Boat findBoatByName(String name) {
        for (Boat b : fleet) {
            if (b.getName().equalsIgnoreCase(name)) return b;
        }
        return null;
    }

    /* Menu loop */

    private void menuLoop(Scanner keyboard) {
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        boolean running = true;

        while (running) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String input = keyboard.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Invalid menu option, try again");
                continue;
            }

            char c = Character.toUpperCase(input.charAt(0));

            switch (c) {
                case 'P':
                    printFleetReport();
                    break;

                case 'A':
                    System.out.print("Please enter the new boat CSV data          : ");
                    addBoatFromCSVLine(keyboard.nextLine());
                    break;

                case 'R':
                    System.out.print("Which boat do you want to remove?           : ");
                    String rm = keyboard.nextLine();
                    if (!removeBoatByName(rm))
                        System.out.println("Cannot find boat " + rm);
                    break;

                case 'E':
                    System.out.print("Which boat do you want to spend on?         : ");
                    String name = keyboard.nextLine();
                    Boat boat = findBoatByName(name);

                    if (boat == null) {
                        System.out.println("Cannot find boat " + name);
                        break;
                    }

                    System.out.print("How much do you want to spend?              : ");
                    double amt;

                    try {
                        amt = Double.parseDouble(keyboard.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid amount.");
                        break;
                    }

                    if (boat.canSpend(amt)) {
                        boat.spend(amt);

                        // 🔥 FIXED — show TOTAL spent, not the single amount
                        System.out.printf(Locale.US,
                                "Expense authorized, $%.2f spent.%n",
                                boat.getSpent());
                    } else {
                        double left = boat.getPurchasePrice() - boat.getSpent();
                        System.out.printf(Locale.US,
                                "Expense not permitted, only $%.2f left to spend.%n",
                                left);
                    }

                    break;

                case 'X':
                    running = false;
                    break;

                default:
                    System.out.println("Invalid menu option, try again");
            }
        }

        System.out.println();
        System.out.println("Exiting the Fleet Management System");
    }

    /* Main */

    public static void main(String[] args) {
        FleetManager app = new FleetManager();
        Scanner keyboard = new Scanner(System.in);

        if (args.length > 0) {
            try {
                app.loadFromCSV(args[0]);
            } catch (IOException e) {
                System.err.println("Error loading CSV: " + e.getMessage());
            }
        } else {
            app.loadFromDB(); // okay if fails; fleet stays empty
        }

        app.menuLoop(keyboard);

        try {
            app.saveToDB();
        } catch (IOException e) {
            System.err.println("Error saving DB: " + e.getMessage());
        }

        keyboard.close();
    }
}
