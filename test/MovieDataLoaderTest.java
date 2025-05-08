import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * JUnit 4 test class for MovieDataLoader.
 */
public class MovieDataLoaderTest {

    private static List<Movie> movies;

    @BeforeClass
    public static void setup() {
        String creditsPath = "tmdb_5000_credits.csv";
        String moviesPath = "tmdb_5000_movies.csv";

        movies = MovieDataLoader.loadMovies(creditsPath, moviesPath);

        assertNotNull("Movie list should not be null", movies);
        assertFalse("Movie list should not be empty", movies.isEmpty());
    }

    @Test
    public void testMovieIdAndTitlePresent() {
        for (Movie movie : movies) {
            assertNotNull("Title should not be null", movie.getTitle());
            assertFalse("Title should not be empty", movie.getTitle().isEmpty());
        }
    }

    @Test
    public void testGenresParsedCorrectly() {
        for (Movie movie : movies) {
            Set<String> genres = movie.getGenres();
            assertNotNull("Genres should not be null", genres);
        }
    }

    @Test
    public void testActorsAndCrewParsedCorrectly() {
        for (Movie movie : movies) {
            assertNotNull("Actors should not be null", movie.getActors());
            assertNotNull("Directors should not be null", movie.getDirectors());
            assertNotNull("Composers should not be null", movie.getComposers());
            assertNotNull("Writers should not be null", movie.getWriters());
            assertNotNull("Cinematographers should not be null", movie.getCinematographers());
        }
    }

    @Test
    public void testVoteCountAndYearValid() {
        for (Movie movie : movies) {
            assertTrue("Vote count should be >= 0", movie.getVoteCount() >= 0);
            assertTrue("Release year should be >= 0, with 0 for null", movie.getReleaseYear() >= 0);
        }
    }
}
