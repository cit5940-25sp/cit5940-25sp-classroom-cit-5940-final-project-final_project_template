/**
 * WinCondition implementation that checks whether a player has collected a movie
 * featuring a specific actor.
 *
 * @author Jianing Yin
 */
public class ActorWinCondition implements WinCondition {
    private String actor;

    /**
     * Constructs an ActorWinCondition for a specific actor.
     *
     * @param actor the actor's name to check for in the player's movies
     */
    public ActorWinCondition(String actor) {
        this.actor = actor;
    }

    /**
     * Checks whether the player has collected a movie with the specified actor.
     *
     * @param player the player whose movie list is to be checked
     * @return true if at least one movie contains the actor; false otherwise
     */
    @Override
    public boolean checkWin(Player player) {
        for (Movie movie : player.getMoviesPlayed()) {
            if (movie.getActors().contains(actor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a description of this win condition.
     *
     * @return a string describing the win condition
     */
    @Override
    public String getDescription() {
        return "Has a movie with actor: " + actor;
    }
}
