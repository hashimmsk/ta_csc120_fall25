// Nahom Desalegne
// 11/30/2025
// CSC 120
// Final Project - Fleet Manager

import java.io.*;
// Nahom Desalegne
// 11/30/2025
// CSC 120
// Final Project

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FleetManager {

    // File where we save the boats between runs
    private static final String DB_FILE = "FleetData.db";

    // Two kinds of boats the program supports
    private enum BoatType {
        SAILING, POWER
    }

    // Boat class holds all info about one boat
    private static class Boat implements Serializable {
        private static final long serialVersionUID = 1L;

        BoatType type;
        String name;
        int year;
        String make;
        int lengthFeet;
        double pricePaid;
        double expenses;

        public Boat(BoatType type, String name, int year, String make,
                    int lengthFeet, double pricePaid, double expenses) {
            this.type = type;
            this.name = name;
            this.year = year;
            this.make = make;
            this.lengthFeet = lengthFeet;
            this.pricePaid = pricePaid;
            this.expenses = expenses;
        }

        // Makes one line of the fleet report (professor's format)
        public String formatReportLine() {
            return String.format(
                    "    %-8s %-12s %4d %-10s %3d' : Paid $ %10.2f : Spent $ %7.2f",
                    type.name(),
                    name,
                    year,
                    make,
                    lengthFeet,
                    pricePaid,
                    expenses
            );
        }
    }

    // ---------------- FILE LOADING AND SAVING ----------------

    // Reads boats from CSV when there is no saved DB file
    private static List<Boat> loadFromCsv(String fileName) {
        List<Boat> boats = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;  // skip empty lines

                String[] p = line.split(",");

                BoatType type = BoatType.valueOf(p[0].trim().toUpperCase());
                String name = p[1].trim();
                int year = Integer.parseInt(p[2].trim());
                String make = p[3].trim();
                int length = Integer.parseInt(p[4].trim());
                double price = Double.parseDouble(p[5].trim());

                boats.add(new Boat(type, name, year, make, length, price, 0.0));
            }

        } catch (IOException e) {
            System.out.println("Error reading CSV file.");
        }
        return boats;
    }

    // Loads saved boats from the DB file
    private static List<Boat> loadFromDb() {
        File file = new File(DB_FILE);
        if (!file.exists()) return new ArrayList<>(); // nothing saved yet

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Boat>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Saves the boats to the DB file
    private static void saveToDb(List<Boat> boats) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILE))) {
            oos.writeObject(boats);
        } catch (IOException ignored) {}
    }

    // ---------------- MAIN PROGRAM FEATURES ----------------

    // Finds a boat by name (not case sensitive)
    private static Boat findBoat(List<Boat> boats, String name) {
        name = name.trim().toLowerCase();
        for (Boat b : boats) {
            if (b.name.toLowerCase().equals(name)) return b;
        }
        return null;
    }

    // Prints the whole fleet just like the example
    private static void printFleet(List<Boat> boats) {
        System.out.println();
        System.out.println("Fleet report:");

        double totalPaid = 0.0;
        double totalSpent = 0.0;

        for (Boat b : boats) {
            System.out.println(b.formatReportLine());
            totalPaid += b.pricePaid;
            totalSpent += b.expenses;
        }

        // Prints summary line
        System.out.printf(
                "    %-8s %-12s %-4s %-10s %3s  : Paid $ %10.2f : Spent $ %7.2f%n",
                "Total", "", "", "", "",
                totalPaid, totalSpent
        );
    }

    // Adds a new boat using CSV-style input
    private static void addBoat(List<Boat> boats, Scanner in) {
        System.out.print("Please enter the new boat CSV data      : ");
        String line = in.nextLine();
        String[] p = line.split(",");

        BoatType type = BoatType.valueOf(p[0].trim().toUpperCase());
        String name = p[1].trim();
        int year = Integer.parseInt(p[2].trim());
        String make = p[3].trim();
        int length = Integer.parseInt(p[4].trim());
        double price = Double.parseDouble(p[5].trim());

        boats.add(new Boat(type, name, year, make, length, price, 0.0));
    }

    // Removes a boat if found
    private static void removeBoat(List<Boat> boats, Scanner in) {
        System.out.print("Which boat do you want to remove?       : ");
        String name = in.nextLine();

        Boat b = findBoat(boats, name);
        if (b == null) {
            System.out.println("Cannot find boat " + name);
        } else {
            boats.remove(b);
        }
    }

    // Adds expenses to a boat but only up to its pricePaid limit
    private static void addExpense(List<Boat> boats, Scanner in) {

        System.out.print("Which boat do you want to spend on?    : ");
        String name = in.nextLine();
        Boat b = findBoat(boats, name);

        if (b == null) {
            System.out.println("Cannot find boat " + name);
            return;
        }

        System.out.print("How much do you want to spend?         : ");
        double amount = Double.parseDouble(in.nextLine());

        double remaining = b.pricePaid - b.expenses;

        if (amount <= remaining) {
            b.expenses += amount;
            System.out.printf("Expense authorized, $%.2f spent.%n", b.expenses);
        } else {
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", remaining);
        }
    }

    // ---------------- MENU LOOP ----------------

    // Shows menu until user picks X to exit
    private static void menuLoop(List<Boat> boats) {
        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

        boolean running = true;

        while (running) {
            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            String choice = in.nextLine().trim();

            if (choice.isEmpty()) continue;

            char c = Character.toUpperCase(choice.charAt(0));

            switch (c) {
                case 'P':
                    printFleet(boats);
                    break;

                case 'A':
                    addBoat(boats, in);
                    break;

                case 'R':
                    removeBoat(boats, in);
                    break;

                case 'E':
                    addExpense(boats, in);
                    break;

                case 'X':
                    System.out.println();
                    System.out.println("Exiting the Fleet Management System");
                    saveToDb(boats); // save before quitting
                    running = false; // stop loop
                    break;

                default:
                    System.out.println("Invalid menu option, try again");
            }
        }
    }

    // ---------------- MAIN ----------------

    public static void main(String[] args) {

        List<Boat> boats;

        // If DB exists, load it. If not, load the CSV instead.
        File file = new File(DB_FILE);

        if (file.exists()) {
            boats = loadFromDb();
        } else {
            boats = loadFromCsv("FleetData.csv");
        }

        // Start the menu
        menuLoop(boats);
    }
}
