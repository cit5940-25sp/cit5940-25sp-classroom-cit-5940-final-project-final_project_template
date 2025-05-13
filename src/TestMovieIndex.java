import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class TestMovieIndex {
    @Test
    public void testLoadMovies() {
        MovieIndex movieIndex = new MovieIndex();
        Map<Integer, IMovie> movieMap = movieIndex.loadMovies("tmdb_5000_movies.csv");
        movieIndex.loadCast("tmdb_5000_credits.csv", movieMap);

        IMovie movie = movieMap.get(27205);

        assertNotNull(movie);

        assertEquals("Inception (2010)", movie.getTitle());
        assertEquals(2010, movie.getYear());
        assertTrue(movie.getGenres().contains("Action"));
        assertTrue(movie.getGenres().contains("Science Fiction"));
    }

    @Test
    public void testLoadCast() {
        MovieIndex movieIndex = new MovieIndex();

        Map<Integer, IMovie> movieMap = movieIndex.loadMovies("tmdb_5000_movies.csv");
        movieIndex.loadCast("tmdb_5000_credits.csv", movieMap);

        IMovie movie = movieMap.get(27205);

        Set<String> contributors = movie.getAllContributors();
        assertTrue(contributors.contains("Christopher Nolan"));
        assertTrue(contributors.contains("Leonardo DiCaprio"));

        IMovie otherMovie = movieMap.get(597);
        assertNotNull(otherMovie);
        List<IMovie> connected = movieIndex.getConnectedMovies(movie);
        assertTrue(connected.contains(otherMovie));

    }

    @Test
    public void testGetMovieByTitle() {
        MovieIndex movieIndex = new MovieIndex();
        Map<Integer, IMovie> movieMap = movieIndex.loadMovies("tmdb_5000_movies.csv");
        movieIndex.loadCast("tmdb_5000_credits.csv", movieMap);

        IMovie movie = movieIndex.getMovieByTitle("Inception (2010)");
        assertNotNull(movie);
        assertEquals(2010, movie.getYear());
    }

    @Test
    public void getConnectedMovies() {
        MovieIndex movieIndex = new MovieIndex();

        Map<Integer, IMovie> movieMap = movieIndex.loadMovies("tmdb_5000_movies.csv");
        movieIndex.loadCast("tmdb_5000_credits.csv", movieMap);

        IMovie movie = movieIndex.getMovieByTitle("Inception (2010)");
        List<IMovie> connected = movieIndex.getConnectedMovies(movie);

        assertFalse(connected.contains(movie));

        assertFalse(connected.isEmpty());
    }

    @Test
    public void testContainsMovie() {
        MovieIndex movieIndex = new MovieIndex();
        Map<Integer, IMovie> movieMap = movieIndex.loadMovies("tmdb_5000_movies.csv");
        movieIndex.loadCast("tmdb_5000_credits.csv", movieMap);

        assertTrue(movieIndex.containsMovie("Inception (2010)"));
        assertFalse(movieIndex.containsMovie("Fake Melody Yerin Tommy (2029)"));
    }
}
