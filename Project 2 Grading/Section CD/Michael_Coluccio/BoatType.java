/**
 * BoatType - enumeration of boat types.
 */
public enum BoatType {
    SAILING, POWER;

    /**
     * Convert a string to a BoatType (case-insensitive).
     * @param s string like "sailing" or "POWER"
     * @return BoatType matching the string
     * @throws IllegalArgumentException if no match
     */
    public static BoatType fromString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Boat type string is null");
        }
        switch (s.trim().toUpperCase()) {
            case "SAILING": return SAILING;
            case "POWER": return POWER;
            default: throw new IllegalArgumentException("Unknown boat type: " + s);
        }
    }
}
