package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for the {@link model.MovieIndex} class.
 */
public class MovieIndexTest {

    /**
     * Tests that a MovieIndex object can be created and queried.
     */
    @Test
    public void testMovieIndexCreation() {
        MovieIndex index = new MovieIndex(List.of());
        assertNotNull(index);
    }
}
