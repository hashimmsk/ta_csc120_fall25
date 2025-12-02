import java.io.Serializable;
/**
 * Numeration representing two possible types of boats
 * managed by the fleet system.
 * <p>Each boat is labeled as either:
 * <ul>
 *     <li>{@code SAILING} as in wind-powered sailboats</li>
 *     <li>{@code POWER} as in motor-powered boats</li>
 * </ul>
 * <p>
 * This enum can be saved inside the fleet database file
 *
 * @author Stephan Cacic
 */
public enum BoatType implements Serializable {
    /** A sailboat powered by wind */
    SAILING,

    /** A boat powered by motor engine */
    POWER
}