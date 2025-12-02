import java.io.Serializable;

    public class Boat implements Serializable {

        public enum BoatType {
            SAILING,
            POWER
        }


        private BoatType type;
        private String name;
        private int year;
        private String model;
        private double length;
        private double purchasePrice;
        private double expenses;

        public Boat(BoatType type, String name, int year,
                    String model, double length,
                    double purchasePrice, double expenses) {
            this.type = type;
            this.name = name;
            this.year = year;
            this.model = model;
            this.length = length;
            this.purchasePrice = purchasePrice;
            this.expenses = expenses;

        } //end of the Constructor method

        public BoatType getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public int getYear() {
            return year;
        }

        public String getModel() {
            return model;
        }

        public double getLength() {
            return length;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public double getExpenses() {
            return expenses;
        }

        public void setExpenses(double expenses) {
            this.expenses = expenses;
        }

        public String toString() {
            return String.format("%s (%s), Year: %d, Model: %s, Length: %.1fft, Price: %.2f, Expenses: %.2f",
                    name, type, year, model, length, purchasePrice, expenses);
        } //end of the toString method

    } //end of the Boat class


