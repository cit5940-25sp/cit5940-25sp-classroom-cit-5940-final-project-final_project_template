import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ConnectionValidatorTest {

    private Movie movie1;
    private Movie movie2;
    private Movie unrelatedMovie;
    private ConnectionValidator validator;

    @Before
    public void setUp() {
        // 初始化测试电影
        movie1 = new Movie(
                "Inception", 2010, "Sci-Fi",
                Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt"),
                "Christopher Nolan",
                "Jonathan Nolan",
                "Wally Pfister",
                "Hans Zimmer"
        );

        movie2 = new Movie(
                "Interstellar", 2014, "Sci-Fi",
                Arrays.asList("Matthew McConaughey", "Anne Hathaway"),
                "Christopher Nolan", // same director
                "Jonathan Nolan",     // same writer
                "Hoyte van Hoytema",
                "Hans Zimmer"         // same composer
        );

        unrelatedMovie = new Movie(
                "Forrest Gump", 1994, "Drama",
                Arrays.asList("Tom Hanks", "Robin Wright"),
                "Robert Zemeckis",
                "Eric Roth",
                "Don Burgess",
                "Alan Silvestri"
        );

        validator = new ConnectionValidator();
    }

    @Test
    public void testSharedActorConnection() {
        Movie m1 = new Movie("TestA", 2000, "Action",
                Arrays.asList("Tom Hanks"), "Dir1", "Writer1", "Cinematographer1", "Composer1");

        Movie m2 = new Movie("TestB", 2001, "Drama",
                Arrays.asList("Tom Hanks", "Other Actor"), "Dir2", "Writer2", "Cinematographer2", "Composer2");

        assertTrue("Should be connected via shared actor", validator.isValidConnection(m1, m2));
    }

    @Test
    public void testSharedDirectorConnection() {
        assertTrue("Should be connected via shared director", validator.isValidConnection(movie1, movie2));
    }

    @Test
    public void testSharedWriterConnection() {
        assertTrue("Should be connected via shared writer", validator.isValidConnection(movie1, movie2));
    }

    @Test
    public void testSharedComposerConnection() {
        assertTrue("Should be connected via shared composer", validator.isValidConnection(movie1, movie2));
    }

    @Test
    public void testNoConnection() {
        assertFalse("Should not be connected", validator.isValidConnection(movie1, unrelatedMovie));
    }

    @Test
    public void testNullOrEmptyFields() {
        Movie nullFieldsMovie = new Movie("Nulls", 2000, "Mystery",
                Arrays.asList(), null, "", null, "");

        assertFalse("Should not be connected with null or empty fields", validator.isValidConnection(movie1, nullFieldsMovie));
    }

    @Test
    public void testNullMovieArguments() {
        assertFalse("Null input should return false", validator.isValidConnection(null, movie1));
        assertFalse("Null input should return false", validator.isValidConnection(movie1, null));
    }
}
