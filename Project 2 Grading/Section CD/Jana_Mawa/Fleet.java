import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Container for a collection of boats and operations on them.
 */
public class Fleet implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ArrayList<Boat> boats;

    public Fleet() {
        this.boats = new ArrayList<>();
    }

    public void addBoat(Boat b) {
        boats.add(b);
    }

    /**
     * Remove first boat whose name matches (case-insensitive).
     * @param name name to match
     * @return the removed Boat, or null if not found
     */
    public Boat removeBoatByName(String name) {
        Boat found = findBoatByName(name);
        if (found != null) {
            boats.remove(found);
            return found;
        }
        return null;
    }

    /**
     * Find a boat by name, case-insensitive.
     * @param name name to find
     * @return Boat or null
     */
    public Boat findBoatByName(String name) {
        if (name == null) return null;
        String key = name.trim().toLowerCase(Locale.ROOT);
        for (Boat b : boats) {
            if (b.getName().trim().toLowerCase(Locale.ROOT).equals(key)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Try to authorize and apply an expense on a boat.
     * @param name boat name (case-insensitive)
     * @param amount amount requested (>= 0)
     * @return AuthorizationResult describing outcome
     */
    public AuthorizationResult authorizeExpense(String name, double amount) {
        Boat b = findBoatByName(name);
        if (b == null) return new AuthorizationResult(false, 0.0, "Cannot find boat " + name);
        double remaining = b.remainingBudget();
        if (amount <= remaining + 1e-9) { // allow small fp tolerance
            b.addExpense(amount);
            return new AuthorizationResult(true, amount, null);
        } else {
            return new AuthorizationResult(false, remaining, String.format("Expense not permitted, only $%.2f left to spend.", remaining));
        }
    }

    public List<Boat> getBoatList() {
        return new ArrayList<>(boats);
    }

    public double getTotalPaid() {
        double sum = 0.0;
        for (Boat b : boats) sum += b.getPurchasePrice();
        return sum;
    }

    public double getTotalSpent() {
        double sum = 0.0;
        for (Boat b : boats) sum += b.getExpenses();
        return sum;
    }

    /**
     * Simple result object for authorizeExpense
     */
    public static class AuthorizationResult {
        public final boolean authorized;
        public final double amountOrRemaining; // if authorized => amount spent, otherwise => remaining allowed
        public final String message; // optional message (e.g. not found or denial text)

        public AuthorizationResult(boolean authorized, double amountOrRemaining, String message) {
            this.authorized = authorized;
            this.amountOrRemaining = amountOrRemaining;
            this.message = message;
        }
    }
}
