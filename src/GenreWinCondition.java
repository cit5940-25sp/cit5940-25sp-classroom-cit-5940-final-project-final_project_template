/**
 * A win condition where a player wins if they have played any movie of a specific genre.
 *
 * @author Jianing Yin
 */
public class GenreWinCondition implements WinCondition {
    private String genre;

    /**
     * Constructs a GenreWinCondition for the specified genre.
     *
     * @param genre The genre that triggers a win condition (e.g., "Action", "Comedy").
     */
    public GenreWinCondition(String genre) {
        this.genre = genre;
    }

    /**
     * Checks whether the player has played any movie that belongs to the specified genre.
     *
     * @param player The player whose movie history is checked.
     * @return true if any movie played by the player contains the target genre, false otherwise.
     */
    @Override
    public boolean checkWin(Player player) {
        for (Movie movie : player.getMoviesPlayed()) {
            if (movie.getGenres().contains(genre)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a description of the genre-based win condition.
     *
     * @return A string describing the genre requirement.
     */
    @Override
    public String getDescription() {
        return "Has a movie with genre: " + genre;
    }
}
