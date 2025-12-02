/**
 * Enum representing the two boat types supported by the Fleet Management System.
 */
public enum BoatType {
    SAILING,
    POWER;

    /**
     * Parse a string into a BoatType (case-insensitive).
     * @param s string like "SAILING" or "power"
     * @return BoatType parsed
     * @throws IllegalArgumentException if parsing fails
     */
    public static BoatType fromString(String s) {
        if (s == null) throw new IllegalArgumentException("BoatType string is null");
        String t = s.trim().toUpperCase();
        switch (t) {
            case "SAILING": return SAILING;
            case "POWER": return POWER;
            default: throw new IllegalArgumentException("Unknown boat type: " + s);
        }
    }
}
