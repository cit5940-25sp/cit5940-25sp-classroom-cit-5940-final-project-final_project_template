import java.util.*;

/**
 * Manages a collection of movies and provides lookup features.
 */
public class MovieDatabase {

    /**
     * Finds all movies connected to the given movie via shared attributes.
     *
     * @param movie the movie to find connections from
     * @return list of connected movies
     */
    public List<Movie> findConnections(Movie movie);

    /**
     * Finds all movies featuring the specified actor.
     *
     * @param actor the actor's name
     * @return list of movies with the given actor
     */
    public List<Movie> findByActor(String actor);

    /**
     * Adds a movie to the database.
     *
     * @param movie the movie to add
     */
    public void addMovie(Movie movie);
}
