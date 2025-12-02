import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Enum for boat types
enum BoatType {
    SAILING, POWER
}

// Boat class to represent each boat
class Boat implements Serializable {
    private static final long serialVersionUID = 1L;
    private BoatType type;
    private String name;
    private int year;
    private String make;
    private int length;
    private double purchasePrice;
    private double expenses;

    public Boat(BoatType type, String name, int year, String make, int length, double purchasePrice) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.make = make;
        this.length = length;
        this.purchasePrice = purchasePrice;
        this.expenses = 0.0;
    }

    // Getters
    public BoatType getType() { return type; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public String getMake() { return make; }
    public int getLength() { return length; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getExpenses() { return expenses; }

    // Method to add expense if permitted
    public boolean addExpense(double amount) {
        if (expenses + amount <= purchasePrice) {
            expenses += amount;
            return true;
        }
        return false;
    }

    // Method to get remaining budget
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %4d %-12s %3d' : Paid $ %8.2f : Spent $ %8.2f",
                type, name, year, make, length, purchasePrice, expenses);
    }

    // Static method to create boat from CSV line
    public static Boat fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid CSV format");
        }

        BoatType type = BoatType.valueOf(parts[0].trim().toUpperCase());
        String name = parts[1].trim();
        int year = Integer.parseInt(parts[2].trim());
        String make = parts[3].trim();
        int length = Integer.parseInt(parts[4].trim());
        double price = Double.parseDouble(parts[5].trim());

        return new Boat(type, name, year, make, length, price);
    }
}

// Main Fleet Management System class
public class FleetManagementSystem {
    private ArrayList<Boat> fleet;
    private static final String SERIALIZED_FILE = "FleetData.db";

    public FleetManagementSystem() {
        fleet = new ArrayList<>();
    }

    // Load data from either CSV or serialized file
    public void loadData(String[] args) {
        if (args.length > 0 && args[0].endsWith(".csv")) {
            System.out.println("Loading from CSV file: " + args[0]);
            loadFromCSV(args[0]);
        } else {
            System.out.println("Loading from serialized file: " + SERIALIZED_FILE);
            loadFromSerialized();
        }
        System.out.println("Total boats loaded: " + fleet.size());
    }

    // Save data to serialized file
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZED_FILE))) {
            oos.writeObject(fleet);
            System.out.println("Data saved successfully to " + SERIALIZED_FILE);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load from CSV file
    private void loadFromCSV(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            int lineCount = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                lineCount++;
                if (!line.isEmpty()) {
                    try {
                        Boat boat = Boat.fromCSV(line);
                        fleet.add(boat);
                        System.out.println("Loaded boat: " + boat.getName());
                    } catch (Exception e) {
                        System.out.println("Skipping invalid line " + lineCount + ": " + line + " - " + e.getMessage());
                    }
                }
            }
            System.out.println("Successfully loaded " + fleet.size() + " boats from CSV file.");
        } catch (FileNotFoundException e) {
            System.out.println("CSV file not found: " + filename);
            System.out.println("Please make sure the file exists in the correct location.");
        }
    }

    // Load from serialized file
    @SuppressWarnings("unchecked")
    private void loadFromSerialized() {
        File file = new File(SERIALIZED_FILE);
        if (!file.exists()) {
            System.out.println("Serialized file not found. Starting with empty fleet.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE))) {
            fleet = (ArrayList<Boat>) ois.readObject();
            System.out.println("Successfully loaded " + fleet.size() + " boats from serialized file.");
        } catch (FileNotFoundException e) {
            System.out.println("Serialized file not found: " + SERIALIZED_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading serialized data: " + e.getMessage());
            System.out.println("Starting with empty fleet.");
            fleet = new ArrayList<>();
        }
    }

    // Print fleet report
    public void printFleet() {
        if (fleet.isEmpty()) {
            System.out.println("\nNo boats in the fleet.");
            return;
        }

        System.out.println("\nFleet report:");
        double totalPaid = 0;
        double totalSpent = 0;

        for (Boat boat : fleet) {
            System.out.println("    " + boat);
            totalPaid += boat.getPurchasePrice();
            totalSpent += boat.getExpenses();
        }

        System.out.printf("    Total                                             : Paid $ %8.2f : Spent $ %8.2f\n\n",
                totalPaid, totalSpent);
    }

    // Add a boat from CSV string
    public void addBoat(String csvData) {
        try {
            Boat boat = Boat.fromCSV(csvData);
            // Check if boat with same name already exists
            for (Boat existing : fleet) {
                if (existing.getName().equalsIgnoreCase(boat.getName())) {
                    System.out.println("Boat with name '" + boat.getName() + "' already exists.");
                    return;
                }
            }
            fleet.add(boat);
            System.out.println("Boat '" + boat.getName() + "' added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding boat: " + e.getMessage());
            System.out.println("Format should be: TYPE,NAME,YEAR,MAKE,LENGTH,PRICE");
            System.out.println("Example: SAILING,Summer Breeze,2020,Beneteau,35,25000.00");
        }
    }

    // Remove a boat by name (case-insensitive)
    public void removeBoat(String boatName) {
        boolean found = false;
        for (int i = 0; i < fleet.size(); i++) {
            if (fleet.get(i).getName().equalsIgnoreCase(boatName)) {
                String removedName = fleet.get(i).getName();
                fleet.remove(i);
                System.out.println("Boat '" + removedName + "' removed successfully.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Cannot find boat '" + boatName + "'");
        }
    }

    // Add expense to a boat
    public void addExpense(String boatName, double amount) {
        boolean found = false;
        for (Boat boat : fleet) {
            if (boat.getName().equalsIgnoreCase(boatName)) {
                found = true;
                if (boat.addExpense(amount)) {
                    System.out.printf("Expense authorized, $%.2f spent.\n", amount);
                } else {
                    System.out.printf("Expense not permitted, only $%.2f left to spend.\n",
                            boat.getRemainingBudget());
                }
                break;
            }
        }
        if (!found) {
            System.out.println("Cannot find boat '" + boatName + "'");
        }
    }

    // Main menu loop
    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nWelcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        while (true) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "P":
                    printFleet();
                    break;

                case "A":
                    System.out.print("Please enter the new boat CSV data          : ");
                    String csvData = scanner.nextLine();
                    addBoat(csvData);
                    break;

                case "R":
                    System.out.print("Which boat do you want to remove?           : ");
                    String removeName = scanner.nextLine();
                    removeBoat(removeName);
                    break;

                case "E":
                    System.out.print("Which boat do you want to spend on?         : ");
                    String expenseName = scanner.nextLine();
                    System.out.print("How much do you want to spend?              : ");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine());
                        if (amount <= 0) {
                            System.out.println("Amount must be positive.");
                        } else {
                            addExpense(expenseName, amount);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount entered. Please enter a valid number.");
                    }
                    break;

                case "X":
                    System.out.println("\nExiting the Fleet Management System");
                    saveData();
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid menu option, try again");
            }
        }
    }

    public static void main(String[] args) {
        FleetManagementSystem system = new FleetManagementSystem();
        system.loadData(args);
        system.run();
    }
}