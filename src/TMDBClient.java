import java.util.*;

public class TMDBClient {
    private final String apiKey;

    public TMDBClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public Movie fetchMovieByTitle(String title) { ... }

    public List<Movie> fetchSimilarMovies(Movie movie) { ... }

    public List<Movie> fetchMoviesByActor(String actor) { ... }
}
