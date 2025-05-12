import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MoviesTest {

    Movies movies;

    @Test
    public void loadMoviesTest() {
        movies = new Movies("tmdb_data.txt");
        int actualSize = movies.getAllTitles().size();
        assertEquals(4803, actualSize);
    }

    @Test
    public void containsMovieTest() {
        movies = new Movies("tmdb_data.txt");
        Set<String> titles = movies.getAllTitles();
        assertTrue(titles.contains("Crazy, Stupid, Love. (2011)"));
    }

    @Test
    public void containsGenreTest() {
        movies = new Movies("tmdb_data.txt");
        List<String> genres = movies.getMovieGenres("Superbad (2007)");
        assertTrue(genres.contains("Comedy"));
    }

    @Test
    public void validConnectionTest() {
        movies = new Movies("tmdb_data.txt");
        String movie1 = "Crazy, Stupid, Love. (2011)";
        String movie2 = "Superbad (2007)";
        List<String> actual = movies.getConnection(movie1, movie2);
        List<String> expected = Arrays.asList("Emma Stone", "Charlie Hartsock");
        assertEquals(expected, actual);
    }

    @Test
    public void invalidConnectionTest() {
        movies = new Movies("tmdb_data.txt");
        String movie1 = "Crazy, Stupid, Love. (2011)";
        String movie2 = "Beetlejuice (1988)";
        List<String> actual = movies.getConnection(movie1, movie2);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void invalidFilePathTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> new Movies("tmdb_data.txt"));
        assertTrue(exception.getMessage().contains("RuntimeException"));
    }

    @Test
    public void getAllGenresTest() {
        movies = new Movies("tmdb_data.txt");
        TreeMap<String, List<String>> genres = movies.getAllGenres();
        assertNotNull(genres);
        assertTrue(genres.size() > 0);
    }

    @Test
    public void getRandomMovieTest() {
        movies = new Movies("tmdb_data.txt");
        String randomMovie = movies.getRandomMovie();
        assertNotNull(randomMovie);
        assertTrue(movies.getAllTitles().contains(randomMovie));
    }

    @Test
    public void getMovieGenresTest() {
        movies = new Movies("tmdb_data.txt");
        List<String> genres = movies.getMovieGenres("The Dark Knight Rises (2012)");
        assertTrue(genres.contains("Comedy"));

        List<String> missingGenres = movies.getMovieGenres("Nonexistent Movie (2000)");
        assertTrue(missingGenres.isEmpty());
    }
}


