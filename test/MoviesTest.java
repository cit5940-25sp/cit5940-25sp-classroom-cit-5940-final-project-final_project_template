import org.junit.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class MoviesTest {

    Movies movies;

    @Test
    public void loadMoviesTest() {
        movies = new Movies("src/tmdb_data.txt");
        int actualSize = movies.getAllTitles().size();
        assertEquals(4803, actualSize);
    }

    @Test
    public void containsMovieTest() {
        movies = new Movies("src/tmdb_data.txt");
        Set<String> titles = movies.getAllTitles();
        assertTrue(titles.contains("Crazy, Stupid, Love. (2011)"));
    }

    @Test
    public void containsGenreTest() {
        movies = new Movies("src/tmdb_data.txt");
        List<String> genres = movies.getMovieGenres("Superbad (2007)");
        assertTrue(genres.contains("Comedy"));
    }

    @Test
    public void validConnectionTest() {
        movies = new Movies("src/tmdb_data.txt");
        String movie1 = "Crazy, Stupid, Love. (2011)";
        String movie2 = "Superbad (2007)";
        List<String> actual = movies.getConnection(movie1, movie2);
        List<String> expected = new ArrayList<>(Arrays.asList("Emma Stone", "Charlie Hartsock"));
        assertEquals(expected, actual);
    }

    @Test
    public void invalidConnectionTest() {
        movies = new Movies("src/tmdb_data.txt");
        // movies have no connections
        String movie1 = "Crazy, Stupid, Love. (2011)";
        String movie2 = "Beetlejuice (1988)";
        List<String> actual = movies.getConnection(movie1, movie2);
        assertTrue(actual.isEmpty());
    }

}
