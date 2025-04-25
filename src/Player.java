import java.util.HashSet;
import java.util.Set;

/**
 * Represents a player in the Movie Name Game.
 * Each player has a name and a record of correctly guessed movies.
 */
public class Player {
    private String name;
    private Set<Movie> moviesGuessed;

    /**
     * Constructs a Player with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.moviesGuessed = new HashSet<>();
    }

    /**
     * Adds a correctly guessed movie to the player's record.
     *
     * @param movie the movie to add
     */
    public void addGuessedMovie(Movie movie) {
        moviesGuessed.add(movie);
    }

    // ======== Getters ========

    public String getName() {
        return name;
    }

    public Set<Movie> getMoviesGuessed() {
        return Set.copyOf(moviesGuessed); // creates an unmodifiable Set
    }

    /**
     * Returns the number of correctly guessed movies.
     *
     * @return the number of guessed movies
     */
    public int getNumMoviesGuessed() {
        return moviesGuessed.size();
    }
}