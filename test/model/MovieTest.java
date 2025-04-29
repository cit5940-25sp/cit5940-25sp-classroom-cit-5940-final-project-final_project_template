package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link model.Movie} class.
 */
public class MovieTest {

    /**
     * Basic test to verify that a Movie object can be instantiated.
     */
    @Test
    public void testMovieCreation() {
        Movie movie = new Movie("Inception", 2010);
        assertNotNull(movie);
    }
}
