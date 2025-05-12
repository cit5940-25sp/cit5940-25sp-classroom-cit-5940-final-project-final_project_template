import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

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
        List<String> expected = new ArrayList<>(Arrays.asList("Emma Stone", "Charlie Hartsock"));
        assertEquals(expected, actual);
    }

    @Test
    public void createAutocompleteFileTest() {
        movies = new Movies("tmdb_data.txt");
        Collection<String> output = movies.createAutocompleteFile(movies.getAllTitles());
        assertNotNull(output);
        assertFalse(output.isEmpty());
        assertTrue(output.contains("0\tCrazy, Stupid, Love. (2011)"));
    }

    @Test
    public void getMovieGenresEmptyMovieTest() {
        movies = new Movies("tmdb_data.txt");
        List<String> genres = movies.getMovieGenres("Nonexistent Movie (2000)");
        assertTrue(genres.isEmpty());
    }


    @Test
    public void invalidConnectionTest() {
        movies = new Movies("tmdb_data.txt");
        // movies have no connections
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
        Map<String, Integer> genres = movies.getAllGenres();
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
        List<String> genres = movies.getMovieGenres("Superbad (2007)");
        assertTrue(genres.contains("Comedy"));

        List<String> missingGenres = movies.getMovieGenres("Nonexistent Movie (2000)");
        assertTrue(missingGenres.isEmpty());
    }

}
