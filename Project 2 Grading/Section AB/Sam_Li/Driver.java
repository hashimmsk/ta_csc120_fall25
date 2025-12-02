import java.util.Scanner;

/**
 * Main program to manage the fleet.
 */
public class Driver {

    private static final Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------\n");

        Fleet fleet;
        if (args.length == 1) {
            System.out.println("Loading fleet from CSV file: " + args[0]);
            fleet = CSVLoader.loadFromCSV(args[0]);
        } else {
            System.out.println("Loading fleet from FleetData.db");
            fleet = Fleet.load();
        }

        boolean running = true;
        while (running) {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String option = keyboard.nextLine().trim().toUpperCase();
            switch (option) {
                case "P":
                    fleet.printFleet();
                    break;
                case "A":
                    System.out.print("Please enter the new boat CSV data          : ");
                    String line = keyboard.nextLine();
                    try {
                        String[] p = line.split("\\s*,\\s*");
                        if (p.length != 6) {
                            System.out.println("Bad format");
                            break;
                        }
                        Boat b = new Boat(
                                BoatType.valueOf(p[0].trim().toUpperCase()),
                                p[1].trim(),
                                Integer.parseInt(p[2].trim()),
                                p[3].trim(),
                                Integer.parseInt(p[4].trim()),
                                Double.parseDouble(p[5].trim())
                        );
                        fleet.addBoat(b);
                        System.out.println("Added");
                    } catch (Exception e) {
                        System.out.println("Error adding boat: " + e.getMessage());
                    }
                    break;
                case "R":
                    System.out.print("Which boat do you want to remove?           : ");
                    String rem = keyboard.nextLine();
                    if (fleet.removeBoat(rem)) System.out.println("Removed");
                    else System.out.println("Cannot find boat " + rem);
                    break;
                case "E":
                    System.out.print("Which boat do you want to spend on?         : ");
                    String target = keyboard.nextLine();
                    Boat found = fleet.findBoat(target);
                    if (found == null) {
                        System.out.println("Cannot find boat " + target);
                        break;
                    }
                    System.out.print("How much do you want to spend?              : ");
                    try {
                        double amt = Double.parseDouble(keyboard.nextLine());
                        if (found.canSpend(amt)) {
                            found.addExpense(amt);
                            System.out.printf("Expense authorized, $%.2f spent.\n", found.getExpenses());
                        } else {
                            System.out.printf("Expense not permitted, only $%.2f left to spend.\n", found.remainingBudget());
                        }
                    } catch (NumberFormatException nf) {
                        System.out.println("Invalid amount");
                    }
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
            }
        }

        System.out.println("\nExiting the Fleet Management System");
        Fleet.save(fleet);
    }
}