package angelmartinez.fleetmangement;

import java.io.*;

/**
 * Represents a boat in the fleet management system.
 * Implements Serializable for saving/loading boat data.
 */
class Boat implements Serializable {

    /** The type of boat: SAILING or POWER */
    private BoatType type;

    /** The name of the boat */
    private String name;

    /** The year the boat was made */
    private int year;

    /** The make and model of the boat */
    private String makeModel;

    /** Length of the boat in feet */
    private int lengthFeet;

    /** Purchase price of the boat */
    private double purchasePrice;

    /** Expenses incurred for the boat */
    private double expenses;

    /**
     * Constructs a Boat object with all fields specified.
     *
     * @param type The type of boat (SAILING or POWER)
     * @param name The name of the boat
     * @param year The year the boat was made
     * @param makeModel The make and model of the boat
     * @param lengthFeet The length of the boat in feet
     * @param purchasePrice The purchase price of the boat
     * @param expenses The initial expenses for the boat
     */
    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet,
                double purchasePrice, double expenses) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.makeModel = makeModel;
        this.lengthFeet = lengthFeet;
        this.purchasePrice = purchasePrice;
        this.expenses = expenses;
    }

    /**
     * Constructs a Boat object with initial expenses set to 0.
     *
     * @param type The type of boat (SAILING or POWER)
     * @param name The name of the boat
     * @param year The year the boat was made
     * @param makeModel The make and model of the boat
     * @param lengthFeet The length of the boat in feet
     * @param purchasePrice The purchase price of the boat
     */
    public Boat(BoatType type, String name, int year, String makeModel, int lengthFeet,
                double purchasePrice) {
        this(type, name, year, makeModel, lengthFeet, purchasePrice, 0.0);
    }

    /**
     * Returns the name of the boat.
     *
     * @return The boat's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the purchase price of the boat.
     *
     * @return The purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Returns the total expenses incurred by the boat.
     *
     * @return The expenses
     */
    public double getExpenses() {
        return expenses;
    }

    /**
     * Adds an expense to the boat's total expenses.
     *
     * @param amount The amount to add
     */
    public void addExpense(double amount) {
        expenses = expenses + amount;
    }

    /**
     * Returns the remaining budget (purchase price minus expenses).
     *
     * @return The remaining budget
     */
    public double getRemainingBudget() {
        return purchasePrice - expenses;
    }

    /**
     * Determines whether a given amount can be spent without exceeding the purchase price.
     *
     * @param amount The amount to check
     * @return True if the amount can be spent, false otherwise
     */
    public boolean canSpend(double amount) {
        return (expenses + amount) <= purchasePrice;
    }

    /**
     * Returns a formatted string representation of the boat.
     *
     * @return A string of a boat's information
     */
    public String toString() {
        return String.format(
                "%-7s %-20s %4d %-10s %3d' : Paid $%9.2f : Spent $%9.2f",
                type.toString(),
                name,
                year,
                makeModel,
                lengthFeet,
                purchasePrice,
                expenses
        );
    }
}
