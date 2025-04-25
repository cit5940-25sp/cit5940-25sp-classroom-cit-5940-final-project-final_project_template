import java.util.*;

public class MovieDatabase {
    private final TMDBClient tmdb;
    private final Map<String, Movie> movieCache = new HashMap<>();
    private final Map<String, List<Movie>> actorCache = new HashMap<>();
    private final Map<Long, List<Movie>> similarCache = new HashMap<>();

    public MovieDatabase(String apiKey) {
        this.tmdb = new TMDBClient();
    }

    /**
     * 查找電影（快取機制）
     */
    public Movie findByTitle(String title) {
        if (movieCache.containsKey(title)) {
            return movieCache.get(title);
        }

        Movie movie = tmdb.fetchMovieByTitle(title);
        if (movie != null) {
            movieCache.put(title, movie);
        }

        return movie;
    }

    /**
     * 查找與指定電影類似的電影（快取機制）
     */
    public List<Movie> findConnections(Movie movie) {
        if (similarCache.containsKey(movie.getMovieId())) {
            return similarCache.get(movie.getMovieId());
        }

        List<Movie> similar = tmdb.fetchSimilarMovies(movie);
        if (similar != null) {
            similarCache.put(movie.getMovieId(), similar);
        }

        return similar;
    }

    /**
     * 根據演員查找電影（快取機制）
     */
    public List<Movie> findByActor(String actor) {
        if (actorCache.containsKey(actor)) {
            return actorCache.get(actor);
        }

        List<Movie> movies = tmdb.fetchMoviesByActor(actor);
        if (movies != null) {
            actorCache.put(actor, movies);
        }

        return movies;
    }

    /**
     * 清除所有快取（測試或重設用）
     */
    public void clearCache() {
        movieCache.clear();
        actorCache.clear();
        similarCache.clear();
    }
}
