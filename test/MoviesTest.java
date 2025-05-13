import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class MoviesTest {

    Movies movies = new Movies("src/tmdb_data.txt");

    @Test
    public void loadMoviesTest() {
        int actualSize = movies.getAllTitles().size();
        assertEquals(4804, actualSize);
    }

    @Test
    public void containsMovieTest() {
        Set<String> titles = movies.getAllTitles();
        assertTrue(titles.contains("crazy, stupid, love. (2011)"));
    }

    @Test
    public void containsGenreTest() {
        List<String> genres = movies.getMovieGenres("superbad (2007)");
        assertTrue(genres.contains("Comedy"));
    }

    @Test
    public void validConnectionTest() {
        Movies movies = new Movies("src/tmdb_data.txt");
        // Testing for a connection between two movies by cast
        List<String> connections = movies.getConnection("Crazy, Stupid, Love. (2011)", "Superbad (2007)");
        System.out.println("Connections: " + connections);

        // Expected connections with role type
        List<String> expectedConnections = Arrays.asList(
                "castAndCrew: Charlie Hartsock",
                "castAndCrew: Emma Stone"
        );
        assertEquals(expectedConnections, connections);
    }


    @Test
    public void createAutocompleteFileTest() {
        Collection<String> output = movies.createAutocompleteFile(movies.getAllTitles());
        assertNotNull(output);
    }

    @Test
    public void getMovieGenresEmptyMovieTest() {
        List<String> genres = movies.getMovieGenres("Nonexistent Movie (2000)");
        assertTrue(genres.isEmpty());
    }

    @Test
    public void invalidConnectionTest() {
        // movies have no connections
        String movie1 = "crazy, stupid, love. (2011)";
        String movie2 = "beetlejuice (1988)";
        List<String> actual = movies.getConnection(movie1, movie2);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getRandomMovieTest() {
        String randomMovie = movies.getRandomMovie();
        assertNotNull(randomMovie);
        assertTrue(movies.getAllTitles().contains(randomMovie));
    }

    @Test
    public void getMovieGenresTest() {
        List<String> genres = movies.getMovieGenres("superbad (2007)");
        assertTrue(genres.contains("Comedy"));

        List<String> missingGenres = movies.getMovieGenres("nonexistent movie (2000)");
        assertTrue(missingGenres.isEmpty());
    }
}