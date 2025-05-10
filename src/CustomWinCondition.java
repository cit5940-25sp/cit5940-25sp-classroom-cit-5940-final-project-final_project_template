import java.util.List;
import java.util.function.Function;

/**
 * A customizable win condition based on a provided function that evaluates
 * a player's movie list.
 *
 * @author Jianing Yin
 */
public class CustomWinCondition implements WinCondition {
    private Function<List<Movie>, Boolean> condition;
    private String description;

    /**
     * Constructs a custom win condition.
     *
     * @param condition   a function that takes a list of movies and returns true if the win condition is met
     * @param description a human-readable description of the win condition
     */
    public CustomWinCondition(Function<List<Movie>, Boolean> condition, String description) {
        this.condition = condition;
        this.description = description;
    }

    /**
     * Checks whether the player's movie list satisfies the custom condition.
     *
     * @param player the player whose movie list is being checked
     * @return true if the condition is met; false otherwise
     */
    @Override
    public boolean checkWin(Player player) {
        return condition.apply(player.getMoviesPlayed());
    }

    /**
     * Returns a description of this win condition.
     *
     * @return a string describing the win condition
     */
    @Override
    public String getDescription() {
        return description;
    }
}
