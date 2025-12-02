/**
 * The BoatType enum represents the two types of boats
 * that the club owns: SAILING boats and POWER (coach) boats.
 *
 * Using an enum ensures that only valid boat types are used.
 * @author Joseph Joannou
 * @version 1.0
 * @since 2025-12-1
 */
public enum BoatType {
    SAILING,
    POWER;

    /**
     * This method helps convert text (like from the CSV file)
     * into a BoatType, ignoring uppercase/lowercase differences.
     *
     * Example:
     * "sailing", "SAILING", or "Sailing" will all return SAILING.
     *
     * @param value the text to convert (e.g., "power")
     * @return the matching BoatType
     * @throws IllegalArgumentException if the text doesn't match any type
     */
    public static BoatType fromString(String value) {
        if (value == null) { // if null we signal an error
            throw new IllegalArgumentException("Boat type cannot be null");
        }

        value = value.trim().toUpperCase();  // Remove extra spaces and convert to uppercase

        if (value.equals("SAILING")) {
            return SAILING;
        } else if (value.equals("POWER")) {
            return POWER;
        } else {
            throw new IllegalArgumentException("Invalid boat type: " + value); // throw an exception to show the value was invalid
        }
    } // end of fromString method

} // end of enum BoatType

