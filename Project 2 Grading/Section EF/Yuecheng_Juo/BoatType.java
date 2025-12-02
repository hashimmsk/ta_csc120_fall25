public enum BoatType {
    SAILING,
    POWER;

    /**
     *
     * @param s
     * @return
     */

    public static BoatType fromString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Boat type cannot be null");
        }
        s = s.trim().toUpperCase();
        switch (s) {
            case "SAILING":
                return SAILING;
            case "POWER":
                return POWER;
            default:
                throw new IllegalArgumentException("Unknown boat type: " + s);
        }
    }
}