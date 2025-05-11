import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Indexes a collection of {@link Movie} objects by different metadata such as actor, director,
 * composer, writer, and cinematographer. Also provides methods to retrieve connected movies
 * and autocomplete suggestions.
 *
 * Used for gameplay in a movie connection game.
 *
 * @author Jianing Yin
 */
public class MovieIndex {
    private Map<String, Set<Movie>> actorMap;
    private Map<String, Set<Movie>> directorMap;
    private Map<String, Set<Movie>> composerMap;
    private Map<String, Set<Movie>> writerMap;
    private Map<String, Set<Movie>> cinematographerMap;
    private MovieTrie movieTrie;
    private List<Movie> allMovies = new ArrayList<>();

    /**
     * Constructs a new MovieIndex and builds the internal lookup structures
     * using a {@link MovieTrie}.
     */
    public MovieIndex() {
        actorMap = new HashMap<>();
        directorMap = new HashMap<>();
        composerMap = new HashMap<>();
        writerMap = new HashMap<>();
        cinematographerMap = new HashMap<>();

        movieTrie = new MovieTrie();
        movieTrie.buildTrie();
        allMovies = movieTrie.getAllMovies();

        for (Movie movie : allMovies) {
            addMovie(movie);
        }
    }

    /**
     * Adds a movie to the internal maps by each metadata category (actor, director, etc.).
     *
     * @param movie the movie to add
     */
    public void addMovie(Movie movie) {
        for (String actor : movie.getActors()) {
            addToMap(actorMap, actor, movie);
        }
        for (String director : movie.getDirectors()) {
            addToMap(directorMap, director, movie);
        }
        for (String composer : movie.getComposers()) {
            addToMap(composerMap, composer, movie);
        }
        for (String writer : movie.getWriters()) {
            addToMap(writerMap, writer, movie);
        }
        for (String cinematographer : movie.getCinematographers()) {
            addToMap(cinematographerMap, cinematographer, movie);
        }
    }

    /**
     * Returns a map of movies connected to the given movie, along with the reason for connection
     * (e.g., shared actor or director).
     *
     * @param movie the movie to find connections for
     * @return a map from connected movies to connection reasons
     */
    public Map<Movie, String> getConnectedMoviesWithReason(Movie movie) {
        Map<Movie, String> connected = new HashMap<>();

        for (String actor : movie.getActors()) {
            for (Movie m : actorMap.getOrDefault(actor, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "actor: " + actor);
                }
            }
        }
        for (String director : movie.getDirectors()) {
            for (Movie m : directorMap.getOrDefault(director, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "director: " + director);
                }
            }
        }
        for (String composer : movie.getComposers()) {
            for (Movie m : composerMap.getOrDefault(composer, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "composer: " + composer);
                }
            }
        }
        for (String writer : movie.getWriters()) {
            for (Movie m : writerMap.getOrDefault(writer, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "writer: " + writer);
                }
            }
        }
        for (String cinematographer : movie.getCinematographers()) {
            for (Movie m : cinematographerMap.getOrDefault(cinematographer, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "cinematographer: " + cinematographer);
                }
            }
        }

        return connected;
    }

    /**
     * Returns a list of movie titles connected to the given movie,
     * along with the reason for each connection (for UI display).
     *
     * @param movie the movie to check
     * @return a list of strings in the format "Title (reason)"
     */
    public List<String> getConnectedMovieTitlesWithReason(Movie movie) {
        Map<Movie, String> connected = getConnectedMoviesWithReason(movie);
        List<String> titles = new ArrayList<>();
        for (Map.Entry<Movie, String> entry : connected.entrySet()) {
            Movie m = entry.getKey();
            String reason = entry.getValue();
            titles.add(m.getTitle() + " (" + reason + ")");
        }
        return titles;
    }

    /**
     * Returns a list of title suggestions based on a prefix.
     *
     * @param partialTitle the input string prefix
     * @return a list of suggested full titles
     */
    public List<String> getSuggestions(String partialTitle) {
        return movieTrie.getSuggestions(partialTitle);
    }

    /**
     * Returns the full list of all movies indexed.
     *
     * @return list of all movies
     */
    public List<Movie> getAllMovies() {
        return allMovies;
    }

    /**
     * Returns a random movie that has at least one valid connection.
     *
     * @return a randomly selected movie
     */
    public Movie getRandomMovie() {
        if (allMovies.isEmpty()) {
            return null;
        }

        int index = ThreadLocalRandom.current().nextInt(allMovies.size());
        Movie randomMovie = allMovies.get(index);

        if (getConnectedMoviesWithReason(randomMovie).isEmpty()) {
            return getRandomMovie();
        }
        return randomMovie;
    }

    /**
     * Retrieves a movie by its exact title (case-insensitive).
     *
     * @param title the movie title
     * @return the movie if found, null otherwise
     */
    public Movie getMovieByTitle(String title) {
        for (Movie movie : allMovies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Returns the internal {@link MovieTrie} used for title suggestion.
     *
     * @return the movie trie
     */
    public MovieTrie getMovieTrie() {
        return movieTrie;
    }

    /**
     * Helper method to add a movie to a metadata map.
     *
     * @param map the metadata map
     * @param key the key (e.g., actor name)
     * @param movie the movie to add
     */
    private void addToMap(Map<String, Set<Movie>> map, String key, Movie movie) {
        map.computeIfAbsent(key, k -> new HashSet<>()).add(movie);
    }
}
