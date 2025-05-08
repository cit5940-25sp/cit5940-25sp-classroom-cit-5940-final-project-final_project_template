import java.util.Set;

/**
 * A win condition where the player wins after guessing two horror movies.
 */
public class TwoHorrorMoviesWin implements WinCondition {

    private static final String TARGET_GENRE = "Horror";
    private static final int REQUIRED_COUNT = 2;

    /**
     * Checks if the player has guessed at least five movies of the horror genre.
     *
     * @param player the player to evaluate
     * @return true if the player has guessed five or more horror movies; false otherwise
     */
    @Override
    public boolean checkVictory(Player player) {
        int horrorCount = 0;
        Set<Movie> guessedMovies = player.getMoviesGuessed();

        for (Movie movie : guessedMovies) {
            if (movie.getGenres().contains(TARGET_GENRE)) {
                horrorCount++;
            }
            if (horrorCount >= REQUIRED_COUNT) {
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
    public String description() {
        return "Win by guessing five horror movies!";
    }
}
