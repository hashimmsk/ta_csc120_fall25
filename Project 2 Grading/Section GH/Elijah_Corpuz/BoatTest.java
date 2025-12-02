//public class BoatTest {
//    public static void main(String[] args) {
//
//        // 1. Test constructor
//        Boat b1 = new Boat(
//                Boat.BoatType.SAILING,
//                "Moon Glow",
//                1973,
//                "Bristol",
//                30,
//                5500.00,
//                0.0
//        );
//
//        Boat b2 = new Boat(
//                Boat.BoatType.POWER,
//                "Big Brother",
//                2019,
//                "Mako",
//                20,
//                12989.56,
//                0.0
//        );
//
//        // 2. Print boats
//        System.out.println("=== Testing toString() ===");
//        System.out.println(b1);
//        System.out.println(b2);
//        System.out.println();
//
//        // 3. Test expense addition
//        System.out.println("=== Testing expense logic ===");
//        System.out.println("Remaining before: " + b1.remainingBudget());
//        b1.addExpense(2000.00);
//        System.out.println("Added $2000 expense...");
//        System.out.println("Remaining after : " + b1.remainingBudget());
//        System.out.println();
//
//        // 4. Test CSV parsing
//        System.out.println("=== Testing CSV parser ===");
//        String csv = "POWER, Rescue II, 2016, Zodiac, 12, 8900.00";
//        Boat b3 = Boat.fromCSV(csv);
//        System.out.println("Parsed boat: " + b3);
//        System.out.println();
//
//        // 5. Validate data integrity
//        System.out.println("=== Validating Boat Fields ===");
//        System.out.println("Name: " + b3.getName());
//        System.out.println("Purchase Price: " + b3.getPrice());
//        System.out.println("Expenses: " + b3.getExpenses());
//        System.out.println("Remaining Budget: " + b3.remainingBudget());
//        System.out.println();
//
//        System.out.println("=== Tests Complete ===");
//    }
//}
