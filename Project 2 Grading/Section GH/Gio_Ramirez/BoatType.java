public enum BoatType {
    SAILING,
    POWER;
    public static BoatType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Boat type cannot be null");
        }

        // Normalize to upper case and let valueOf do the work.
        return BoatType.valueOf(value.trim().toUpperCase());
    }
} // End of enum BoatType
