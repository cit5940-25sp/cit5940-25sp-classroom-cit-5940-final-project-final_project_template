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
        if (movieCache.containsKey(title.toLowerCase().trim())) {
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
    public Set<String> getAllTitles() {
        return movieCache.keySet();
    }
    public List<String> getMatchingTitles(String prefix) {
        String lower = prefix.toLowerCase();
        return movieCache.keySet().stream()
            .filter(title -> title.toLowerCase().startsWith(lower))
            .limit(5)
            .toList();
    }

    public void preloadPopularMovies() {
        List<Movie> popular = tmdb.fetchPopularMovies();
        for (Movie movie : popular) {
            movieCache.put(movie.getTitle(), movie);
        }
    }
    public Movie getRandomMovie() {
        if (movieCache.isEmpty()) {
            preloadPopularMovies();
        }

        if (movieCache.isEmpty()) return null;

        List<Movie> all = new ArrayList<>(movieCache.values());
        return all.get(new Random().nextInt(all.size()));
    }
}
