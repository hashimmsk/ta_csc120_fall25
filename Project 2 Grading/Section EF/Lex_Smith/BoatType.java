/**
 * Enum representing boat types
 */

public enum BoatType {
    SAILING,
    POWER;

    public static BoatType fromString(String str) {
        return BoatType.valueOf(str.toUpperCase());
    }
}