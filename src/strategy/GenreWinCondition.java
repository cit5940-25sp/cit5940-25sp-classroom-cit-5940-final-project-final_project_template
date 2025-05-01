package strategy;

import model.Movie;
import model.Player;

/**
 * Win condition where a player wins by naming a certain number of movies in a specific genre.
 */
public class GenreWinCondition implements IWinCondition {
    private String genre;
    private static final int WIN_COUNT = 3;


    /**
     * Constructs a GenreWinCondition for the given genre.
     * @param genre the target genre
     */
    public GenreWinCondition(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean checkWin(Player player) {
        int count = 0;
        for (Movie movie : player.getPlayedMovies()) {
            if (movie.getGenres().contains(genre)) {
                count++;
                if (count >= WIN_COUNT) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "You win! - by naming " + WIN_COUNT + " number of movies in the genre: " + genre;
    }
}
