package model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides indexing and fast lookup functionality for movies and people involved in movies.
 */
public class MovieIndex {
    private Map<String, Movie> titleToMovie;
    private Map<String, Set<Movie>> personToMovies;

    /**
     * Constructs the MovieIndex based on a list of movies.
     * @param movies the list of movies to index
     */
    public MovieIndex(List<Movie> movies) {
        // TODO
    }

    /**
     * Finds a movie by its title.
     * @param title the title of the movie
     * @return the Movie object, or null if not found
     */
    public Movie findMovieByTitle(String title) { return null; }

    /**
     * Finds all movies associated with a given person's name.
     * @param personName the name of the person
     * @return a set of movies that person has worked on
     */
    public Set<Movie> findMoviesByPerson(String personName) { return null; }

    /**
     * Checks if a movie exists in the index.
     * @param title the title of the movie
     * @return true if the movie exists, false otherwise
     */
    public boolean movieExists(String title) { return false; }

    /**
     * Provides autocomplete suggestions based on a given prefix of a movie title.
     * @param prefix the prefix to search for
     * @return a list of movie titles matching the prefix
     */
    public List<String> getAutocompleteSuggestions(String prefix) { return null; }
}
