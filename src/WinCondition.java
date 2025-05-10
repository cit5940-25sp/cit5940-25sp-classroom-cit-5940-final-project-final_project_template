/**
 * Represents a winning condition for a player in the movie connection game.
 * Implementing classes define specific rules for determining if a player has won.
 *
 * @author Jianing Yin
 */
public interface WinCondition {

    /**
     * Checks if the specified player satisfies the win condition.
     *
     * @param player the player whose status is to be evaluated
     * @return true if the player meets the win condition; false otherwise
     */
    boolean checkWin(Player player);

    /**
     * Returns a human-readable description of the win condition.
     *
     * @return description of the win condition
     */
    String getDescription();
}
