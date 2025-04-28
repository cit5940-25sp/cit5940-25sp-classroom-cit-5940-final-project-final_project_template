import java.util.*;

/**
 * Represents a movie in the database, storing metadata such as title, year,
 * and associated people (actors, directors, etc.).
 */
public class Movie {
    private long movieId;
    private String title;
    private int year;
    private Set<String> genres;
    private Set<String> actors;
    private Set<String> directors;
    private Set<String> writers;
    private Set<String> composers;
    private Set<String> cinematographers;

    /**
     * Checks whether this movie shares any attribute (e.g., actor, director)
     * with another movie.
     *
     * @param other the other movie to compare attributes with
     * @return true if there is a shared attribute; false otherwise
     */
    public boolean sharesAttributeWith(Movie other);
}
