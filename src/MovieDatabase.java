import java.util.*;

public class MovieDatabase {
    private TMDBClient tmdb;

    public MovieDatabase(String apiKey) {
        this.tmdb = new TMDBClient(apiKey);
    }

    public Movie findByTitle(String title) {
        return tmdb.fetchMovieByTitle(title);
    }

    public List<Movie> findConnections(Movie movie) {
        return tmdb.fetchSimilarMovies(movie);
    }

    public List<Movie> findByActor(String actor) {
        return tmdb.fetchMoviesByActor(actor);
    }
}
