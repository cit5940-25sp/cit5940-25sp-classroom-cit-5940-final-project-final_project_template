import java.util.*;

public class MovieDatabase {
    private final TMDBClient tmdb;
    private final Map<String, Movie> movieCache = new HashMap<>();
    private final Map<String, List<Movie>> actorCache = new HashMap<>();
    private final Map<Long, List<Movie>> similarCache = new HashMap<>();

    public MovieDatabase(String apiKey) {
        this.tmdb = new TMDBClient();
    }

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


    public void clearCache() {
        movieCache.clear();
        actorCache.clear();
        similarCache.clear();
    }
}
