public enum BoatType {
    SAILING,
    POWER;

    public static BoatType fromString(String s) {
        if (s == null) return null;
        s = s.trim().toUpperCase();
        switch (s) {
            case "SAILING": return SAILING;
            case "POWER":   return POWER;
            default:        return null;
        }
    }
} // end of the BoatType enum