import java.io.Serializable;

    /**
     * Represents a single boat in the fleet.
     */
    public class Boat implements Serializable {
        private BoatType type;
        private String name;
        private int year;
        private String makeModel;
        private int lengthFeet;
        private double purchasePrice;
        private double expenses;

        public Boat(BoatType type, String name, int year, String makeModel,
                    int lengthFeet, double purchasePrice) {
            this.type = type;
            this.name = name;
            this.year = year;
            this.makeModel = makeModel;
            this.lengthFeet = lengthFeet;
            this.purchasePrice = purchasePrice;
            this.expenses = 0.0;
        }

        public String getName() {
            return name;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public double getExpenses() {
            return expenses;
        }

        public void addExpense(double amt) {
            expenses += amt;
        }

        public double remainingBudget() {
            return purchasePrice - expenses;
        }

        @Override
        public String toString() {
            return String.format("    %-7s %-20s %4d %-12s %2d' : Paid $ %10.2f : Spent $ %10.2f",
                    type, name, year, makeModel, lengthFeet, purchasePrice, expenses);
        }
    }


