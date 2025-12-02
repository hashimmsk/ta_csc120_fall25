/**
 * BoatType enum - type of boat.
 */
public enum BoatType {
    SAILING, POWER;

    public static BoatType fromString(String s) {
        if (s == null) return null;
        s = s.trim().toUpperCase();
        switch (s) {
            case "SAILING": return SAILING;
            case "POWER": return POWER;
            default: throw new IllegalArgumentException("Unknown boat type: " + s);
        }
    }
}
