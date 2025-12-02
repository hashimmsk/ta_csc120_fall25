import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class FleetManagementSystem {

    private static final String DB_FILENAME = "FleetData.db";

    /**
     *
     * @param args
     */

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner in = new Scanner(System.in);

        ArrayList<Boat> fleet = null;

        if (args.length > 0) {
            String csvFileName = args[0];
            fleet = loadFromCsv(csvFileName);
            if (fleet == null) {
                System.out.println("Error loading CSV file. Starting with an empty fleet.");
                fleet = new ArrayList<>();
            }
        } else {
            fleet = loadFromDb();
            if (fleet == null) {
                fleet = new ArrayList<>();
            }
        }

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        boolean done = false;
        while (!done) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choice = in.nextLine().trim();

            if (choice.isEmpty()) {
                System.out.println("Invalid menu option, try again");
                continue;
            }

            char c = Character.toUpperCase(choice.charAt(0));

            switch (c) {
                case 'P':
                    printFleetReport(fleet);
                    break;
                case 'A':
                    addBoatFromUser(in, fleet);
                    break;
                case 'R':
                    removeBoatFromUser(in, fleet);
                    break;
                case 'E':
                    requestExpense(in, fleet);
                    break;
                case 'X':
                    done = true;
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
            }
        }

        System.out.println();
        System.out.println("Exiting the Fleet Management System");

        // 退出前保存到 FleetData.db
        saveToDb(fleet);
    }

    /**
     *
     * @param fileName
     * @return
     */

    private static ArrayList<Boat> loadFromCsv(String fileName) {
        ArrayList<Boat> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                Boat boat = parseBoatFromCsvLine(line);
                if (boat != null) {
                    list.add(boat);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return null;
        }
        return list;
    }

    /**
     *
     * @param line
     * @return
     */

    private static Boat parseBoatFromCsvLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            System.out.println("Invalid CSV line (ignored): " + line);
            return null;
        }
        try {
            BoatType type = BoatType.fromString(parts[0]);
            String name = parts[1].trim();
            int year = Integer.parseInt(parts[2].trim());
            String makeModel = parts[3].trim();
            double length = Double.parseDouble(parts[4].trim());
            double purchasePrice = Double.parseDouble(parts[5].trim());

            double expenses = 0.0;
            if (parts.length > 6) {
                expenses = Double.parseDouble(parts[6].trim());
            }

            return new Boat(type, name, year, makeModel, length, purchasePrice, expenses);
        } catch (Exception e) {
            System.out.println("Error parsing CSV line (ignored): " + line);
            return null;
        }
    }

    /**
     *
     * @param fleet
     */

    private static void saveToDb(ArrayList<Boat> fleet) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILENAME))) {
            oos.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Error saving fleet data: " + e.getMessage());
        }
    }

    /**
     *
     * @return
     */

    private static ArrayList<Boat> loadFromDb() {
        File f = new File(DB_FILENAME);
        if (!f.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DB_FILENAME))) {
            Object obj = ois.readObject();
            return (ArrayList<Boat>) obj;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading fleet data: " + e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param fleet
     */

    private static void printFleetReport(ArrayList<Boat> fleet) {
        System.out.println();
        System.out.println("Fleet report:");

        double totalPaid = 0.0;
        double totalSpent = 0.0;

        for (Boat b : fleet) {
            System.out.println(b.toString());
            totalPaid += b.getPurchasePrice();
            totalSpent += b.getExpenses();
        }

        System.out.printf("    %-7s %-20s %-12s %3s : Paid $ %9.2f : Spent $ %9.2f%n",
                "Total", "", "", "", totalPaid, totalSpent);

        System.out.println();
    }

    /**
     *
     * @param in
     * @param fleet
     */

    private static void addBoatFromUser(Scanner in, ArrayList<Boat> fleet) {
        System.out.print("Please enter the new boat CSV data          : ");
        String line = in.nextLine().trim();
        Boat boat = parseBoatFromCsvLine(line);
        if (boat != null) {
            fleet.add(boat);
        }
        System.out.println();
    }

    /**
     *
     * @param in
     * @param fleet
     */

    private static void removeBoatFromUser(Scanner in, ArrayList<Boat> fleet) {
        System.out.print("Which boat do you want to remove?           : ");
        String name = in.nextLine().trim();

        Boat found = findBoatByName(fleet, name);

        if (found == null) {
            System.out.println("Cannot find boat " + name);
        } else {
            fleet.remove(found);
        }

        System.out.println();
    }

    /**
     *
     * @param in
     * @param fleet
     */

    private static void requestExpense(Scanner in, ArrayList<Boat> fleet) {
        System.out.print("Which boat do you want to spend on?         : ");
        String name = in.nextLine().trim();

        Boat boat = findBoatByName(fleet, name);
        if (boat == null) {
            System.out.println("Cannot find boat " + name);
            System.out.println();
            return;
        }

        System.out.print("How much do you want to spend?              : ");
        String amountStr = in.nextLine().trim();

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            System.out.println();
            return;
        }

        double newTotal = boat.getExpenses() + amount;
        double maxAllowed = boat.getPurchasePrice();

        if (newTotal <= maxAllowed + 1e-6) {
            boat.addExpense(amount);
            System.out.printf("Expense authorized, $%.2f spent.%n", boat.getExpenses());
        } else {
            double remaining = maxAllowed - boat.getExpenses();
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", remaining);
        }

        System.out.println();
    }

    /**
     *
     * @param fleet
     * @param name
     * @return
     */

    private static Boat findBoatByName(ArrayList<Boat> fleet, String name) {
        for (Boat b : fleet) {
            if (b.getName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }
}