/**
 * BoatType.java
 * Enum representing the type of a boat.
 */
public enum BoatType {
    SAILING,
    POWER;

    /**
     * Parse a string to BoatType (case-insensitive).
     * @param s input string
     * @return matching BoatType
     * @throws IllegalArgumentException if no match
     */
    public static BoatType fromString(String s) {
        if (s == null) throw new IllegalArgumentException("Boat type is null");
        String t = s.trim().toUpperCase();
        switch (t) {
            case "SAILING": return SAILING;
            case "POWER": return POWER;
            default: throw new IllegalArgumentException("Unknown boat type: " + s);
        }
    }
}

