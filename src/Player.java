import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the movie connection game.
 * Each player has a name, a list of movies they have played,
 * and a win condition to determine whether they have won.
 *
 * @author Vera Zhang
 * @author Jianing Yin
 */
public class Player {
    private String name;
    private List<Movie> moviesPlayed;
    private WinCondition winCondition;

    /**
     * Constructs a Player with a given name and win condition.
     *
     * @param name          the name of the player
     * @param winCondition  the win condition assigned to the player
     */
    public Player(String name, WinCondition winCondition) {
        this.name = name;
        this.moviesPlayed = new ArrayList<>();
        this.winCondition = winCondition;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of movies the player has played so far.
     *
     * @return the list of played movies
     */
    public List<Movie> getMoviesPlayed() {
        return moviesPlayed;
    }

    /**
     * Adds a movie to the list of movies played by the player.
     *
     * @param movie the movie to add
     */
    public void addMovie(Movie movie) {
        moviesPlayed.add(movie);
    }

    /**
     * Returns the win condition assigned to this player.
     *
     * @return the player's win condition
     */
    public WinCondition getWinCondition() {
        return winCondition;
    }
}
