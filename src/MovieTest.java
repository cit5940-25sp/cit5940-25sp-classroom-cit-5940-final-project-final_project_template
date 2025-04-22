import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class MovieTest {

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @Before
    public void setUp() {
        // Create sets of tuples for cast and crew
        Set<Tuple<String, Integer>> cast1 = new HashSet<>();
        cast1.add(new Tuple<>("Leonardo DiCaprio", 1));
        cast1.add(new Tuple<>("Joseph Gordon-Levitt", 2));
        cast1.add(new Tuple<>("Elliot Page", 3));

        Set<Tuple<String, Integer>> crew1 = new HashSet<>();
        crew1.add(new Tuple<>("Christopher Nolan", 101));
        crew1.add(new Tuple<>("Jonathan Nolan", 102));
        crew1.add(new Tuple<>("Wally Pfister", 103));
        crew1.add(new Tuple<>("Hans Zimmer", 104));

        Set<Tuple<String, Integer>> cast2 = new HashSet<>();
        cast2.add(new Tuple<>("Matthew McConaughey", 4));
        cast2.add(new Tuple<>("Anne Hathaway", 5));
        cast2.add(new Tuple<>("Jessica Chastain", 6));

        Set<Tuple<String, Integer>> crew2 = new HashSet<>();
        crew2.add(new Tuple<>("Christopher Nolan", 101));
        crew2.add(new Tuple<>("Jonathan Nolan", 102));
        crew2.add(new Tuple<>("Hoyte van Hoytema", 105));
        crew2.add(new Tuple<>("Hans Zimmer", 104));

        Set<Tuple<String, Integer>> cast3 = new HashSet<>();
        cast3.add(new Tuple<>("Tom Hanks", 7));
        cast3.add(new Tuple<>("Robin Wright", 8));

        Set<Tuple<String, Integer>> crew3 = new HashSet<>();
        crew3.add(new Tuple<>("Robert Zemeckis", 106));
        crew3.add(new Tuple<>("Eric Roth", 107));
        crew3.add(new Tuple<>("Don Burgess", 108));
        crew3.add(new Tuple<>("Alan Silvestri", 109));

        // Create movies with unique IDs
        movie1 = new Movie("Inception", 1, 2010, new HashSet<>(Arrays.asList("Sci-Fi")), new ArrayList<>(cast1), new ArrayList<>(crew1));
        movie2 = new Movie("Interstellar", 2, 2014, new HashSet<>(Arrays.asList("Sci-Fi")), new ArrayList<>(cast2), new ArrayList<>(crew2));
        movie3 = new Movie("Forrest Gump", 3, 1994, new HashSet<>(Arrays.asList("Drama")), new ArrayList<>(cast3), new ArrayList<>(crew3));
    }

    @Test
    public void testGetters() {
        assertEquals("Inception", movie1.getTitle());
        assertEquals(2010, movie1.getReleaseYear());
        assertTrue(movie1.getGenre().contains("Sci-Fi"));
        assertTrue(movie1.hasCast(1)); // Check for Leonardo DiCaprio's ID
        assertTrue(movie1.hasCrew(101)); // Check for Christopher Nolan's ID
        assertEquals(1, movie1.getId()); // Check movie ID
    }

    @Test
    public void testHasCast() {
        assertTrue(movie1.hasCast(1)); // Leonardo DiCaprio
        assertFalse(movie1.hasCast(7)); // Tom Hanks
    }

    @Test
    public void testHasCrew() {
        assertTrue(movie1.hasCrew(101)); // Christopher Nolan
        assertFalse(movie1.hasCrew(106)); // Robert Zemeckis
    }

    @Test
    public void testIsConnectedTo_SharedCrew() {
        assertTrue(movie1.isConnectedTo(movie2)); // Shared crew: Christopher Nolan and Hans Zimmer
    }

    @Test
    public void testIsConnectedTo_NoConnection() {
        assertFalse(movie1.isConnectedTo(movie3)); // No shared cast or crew
    }

    @Test
    public void testEqualsAndHashCode() {
        Movie duplicate = new Movie(
                "Different Title",
                1, // Same ID as movie1
                2000,
                new HashSet<>(Arrays.asList("Different Genre")),
                new ArrayList<>(),
                new ArrayList<>()
        );
        assertEquals(movie1, duplicate); // Same ID → equals() returns true
        assertEquals(movie1.hashCode(), duplicate.hashCode());
    }

    @Test
    public void testToStringFormat() {
        assertEquals("Inception (2010)", movie1.toString());
    }
}
