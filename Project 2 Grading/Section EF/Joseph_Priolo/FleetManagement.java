import java.util.Scanner;

public class FleetManagement {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Fleet fleet = new Fleet();

        // Correct initial fleet order (matches assignment sample initial run)
        fleet.addBoat(new Boat("POWER", "Big Brother", 2019, "Mako", 20, 12989.56, 0.00));
        fleet.addBoat(new Boat("SAILING", "Moon Glow", 1973, "Bristol", 30, 5500.00, 0.00));
        fleet.addBoat(new Boat("SAILING", "Osita", 1988, "Tartan", 40, 11500.07, 0.00));
        fleet.addBoat(new Boat("POWER", "Rescue II", 2016, "Zodiac", 12, 8900.00, 0.00));
        // NOTE: Finesse is NOT preloaded here — it should be added via (A)dd as in the sample.

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        String choice;
        do {
            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine().trim().toUpperCase();

            switch (choice) {
                case "P":
                    fleet.printReport();
                    break;

                case "A":
                    System.out.print("Please enter the new boat CSV data          : ");
                    String line = keyboard.nextLine().trim();
                    String[] parts = line.split(",");
                    if (parts.length != 6) {
                        System.out.println("Invalid CSV data. Must have 6 fields.");
                        break;
                    }
                    try {
                        String type = parts[0].trim().toUpperCase();
                        String name = parts[1].trim();
                        int year = Integer.parseInt(parts[2].trim());
                        String make = parts[3].trim();
                        int len = Integer.parseInt(parts[4].trim());
                        double paid = Double.parseDouble(parts[5].trim());
                        Boat b = new Boat(type, name, year, make, len, paid, 0.0);
                        fleet.addBoat(b);
                    } catch (Exception ex) {
                        System.out.println("Error parsing CSV: " + ex.getMessage());
                    }
                    break;

                case "R":
                    System.out.print("Which boat do you want to remove?           : ");
                    String rName = keyboard.nextLine().trim();
                    boolean removed = fleet.removeBoat(rName);
                    if (!removed) {
                        System.out.println("Cannot find boat " + rName);
                    }
                    break;

                case "E":
                    System.out.print("Which boat do you want to spend on?         : ");
                    String eName = keyboard.nextLine().trim();
                    Boat b = fleet.findBoat(eName);
                    if (b == null) {
                        System.out.println("Cannot find boat " + eName);
                        break;
                    }
                    System.out.print("How much do you want to spend?              : ");
                    double amt;
                    try {
                        amt = Double.parseDouble(keyboard.nextLine().trim());
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid amount.");
                        break;
                    }
                    if (b.addExpense(amt)) {
                        System.out.printf("Expense authorized, $%.2f spent.\n", b.getAmountSpent());
                    } else {
                        double left = b.getRemainingBudget();
                        System.out.printf("Expense not permitted, only $%.2f left to spend.\n", left);
                    }
                    break;

                case "X":
                    break;

                default:
                    System.out.println("Invalid menu option, try again");
            }

        } while (!choice.equals("X"));

        System.out.println("Exiting the Fleet Management System");
    }
}
