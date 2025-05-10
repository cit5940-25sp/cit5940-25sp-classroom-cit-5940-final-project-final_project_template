import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * JUnit 4 test class for MovieDataLoaderAPI.
 * This validates the movie metadata fetched from the TMDB API.
 */
public class MovieDataLoaderAPITest {

    private static List<Movie> movies;

    @BeforeClass
    public static void setUp() {
        // Fetch 1 page (20 movies) from TMDB API before tests
        movies = MovieDataLoaderAPI.loadMoviesFromAPI(1);
        assertNotNull("Movie list should not be null", movies);
        assertFalse("Movie list should not be empty", movies.isEmpty());
    }

    @Test
    public void testMovieIdsAndTitlesArePresent() {
        for (Movie movie : movies) {
            assertNotNull("Movie ID should not be null", movie.getId());
            assertFalse("Movie ID should not be empty", movie.getId().isEmpty());

            assertNotNull("Movie title should not be null", movie.getTitle());
            assertFalse("Movie title should not be empty", movie.getTitle().isEmpty());
        }
    }

    @Test
    public void testReleaseYearsAreValid() {
        for (Movie movie : movies) {
            int year = movie.getReleaseYear();
            assertTrue("Release year should be larger than 0", year > 0);
        }
    }

    @Test
    public void testVoteCountsAreNonNegative() {
        for (Movie movie : movies) {
            assertTrue("Vote count should be non-negative", movie.getVoteCount() >= 0);
        }
    }

    @Test
    public void testGenresActorsDirectorsParsed() {
        for (Movie movie : movies) {
            Set<String> genres = movie.getGenres();
            Set<String> actors = movie.getActors();
            Set<String> directors = movie.getDirectors();

            assertNotNull("Genres should not be null", genres);
            assertNotNull("Actors should not be null", actors);
            assertNotNull("Directors should not be null", directors);
        }
    }

    @Test
    public void testCrewSectionsNotNull() {
        for (Movie movie : movies) {
            assertNotNull("Composers should not be null", movie.getComposers());
            assertNotNull("Writers should not be null", movie.getWriters());
            assertNotNull("Cinematographers should not be null", movie.getCinematographers());
        }
    }
}
