import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class MovieTest {

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @Before
    public void setUp() {
        movie1 = new Movie(
                "Inception",
                2010,
                "Sci-Fi",
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"),
                "Christopher Nolan",
                "Jonathan Nolan",
                "Wally Pfister",
                "Hans Zimmer"
        );

        movie2 = new Movie(
                "Interstellar",
                2014,
                "Sci-Fi",
                List.of("Matthew McConaughey", "Anne Hathaway", "Jessica Chastain"),
                "Christopher Nolan",
                "Jonathan Nolan",
                "Hoyte van Hoytema",
                "Hans Zimmer"
        );

        movie3 = new Movie(
                "Forrest Gump",
                1994,
                "Drama",
                List.of("Tom Hanks", "Robin Wright"),
                "Robert Zemeckis",
                "Eric Roth",
                "Don Burgess",
                "Alan Silvestri"
        );
    }

    @Test
    public void testGetters() {
        assertEquals("Inception", movie1.getTitle());
        assertEquals(2010, movie1.getReleaseYear());
        assertEquals("Sci-Fi", movie1.getGenre());
        assertTrue(movie1.getActors().contains("Leonardo DiCaprio"));
        assertEquals("Christopher Nolan", movie1.getDirector());
    }

    @Test
    public void testHasActor() {
        assertTrue(movie1.hasActor("Leonardo DiCaprio"));
        assertFalse(movie1.hasActor("Tom Hanks"));
    }

    @Test
    public void testIsConnectedTo_SameDirector() {
        assertTrue(movie1.isConnectedTo(movie2)); // Same director: Christopher Nolan
    }

    @Test
    public void testIsConnectedTo_SameComposer() {
        assertTrue(movie1.isConnectedTo(movie2)); // Same composer: Hans Zimmer
    }

    @Test
    public void testIsConnectedTo_NoConnection() {
        assertFalse(movie1.isConnectedTo(movie3)); // No shared actor or crew
    }

    @Test
    public void testEqualsAndHashCode() {
        Movie duplicate = new Movie(
                "Inception",
                2010,
                "Action",
                List.of("Someone Else"),
                "Some Director",
                "Some Writer",
                "Some Cinematographer",
                "Some Composer"
        );
        assertEquals(movie1, duplicate); // Same title and year → equals() returns true
        assertEquals(movie1.hashCode(), duplicate.hashCode());
    }

    @Test
    public void testToStringFormat() {
        assertEquals("Inception (2010) - Genre: Sci-Fi", movie1.toString());
    }
}
