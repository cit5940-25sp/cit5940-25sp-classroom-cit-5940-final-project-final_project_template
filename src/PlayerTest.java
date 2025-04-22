import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;
    private Movie inception;
    private Movie interstellar;
    private Movie matrix;

    @Before
    public void setup() {
        // Create test movies
        Set<Tuple<String, Integer>> cast1 = new HashSet<>();
        cast1.add(new Tuple<>("Leonardo DiCaprio", 1));
        Set<Tuple<String, Integer>> crew1 = new HashSet<>();
        crew1.add(new Tuple<>("Christopher Nolan", 101));

        Set<Tuple<String, Integer>> cast2 = new HashSet<>();
        cast2.add(new Tuple<>("Matthew McConaughey", 2));
        Set<Tuple<String, Integer>> crew2 = new HashSet<>();
        crew2.add(new Tuple<>("Christopher Nolan", 101));

        Set<Tuple<String, Integer>> cast3 = new HashSet<>();
        cast3.add(new Tuple<>("Keanu Reeves", 3));
        Set<Tuple<String, Integer>> crew3 = new HashSet<>();
        crew3.add(new Tuple<>("Lana Wachowski", 102));

        inception = new Movie("Inception", 1, 2010, new HashSet<>(Arrays.asList("Sci-Fi")), new ArrayList<>(cast1), new ArrayList<>(crew1));
        interstellar = new Movie("Interstellar", 2, 2014, new HashSet<>(Arrays.asList("Sci-Fi")), new ArrayList<>(cast2), new ArrayList<>(crew2));
        matrix = new Movie("The Matrix", 3, 1999, new HashSet<>(Arrays.asList("Sci-Fi")), new ArrayList<>(cast3), new ArrayList<>(crew3));

        // Create player Alice, aiming to collect 3 Sci-Fi movies
        player = new Player("Alice", "Sci-Fi", 3);
    }

    @Test
    public void testInitialValues() {
        assertEquals("Alice", player.getName());
        assertFalse("Player should not meet win condition at start.", player.hasMetWinCondition());
        assertFalse("Player should not be skipped initially.", player.isSkipped());
        assertFalse("Player should not have used block initially.", player.hasBlocked());
    }

    @Test
    public void testAddMovieAndWinCondition() {
        player.addMovie(inception);
        player.addMovie(interstellar);
        player.addMovie(matrix);

        assertTrue("Player should meet win condition after 3 genre-matching movies.", player.hasMetWinCondition());
    }

    @Test
    public void testDuplicateMovieIsIgnored() {
        player.addMovie(inception);
        player.addMovie(inception);
        player.addMovie(matrix);

        assertFalse("Duplicate movies should not count toward win condition.", player.hasMetWinCondition());
    }

    @Test
    public void testMovieIdTracking() {
        player.addMovie(inception);

        assertTrue("Movie ID should be tracked correctly.", player.hasUsedMovie(1));
        assertFalse("Non-existent movie ID should return false.", player.hasUsedMovie(999));
    }

    @Test
    public void testSkipFlagBehavior() {
        player.activateSkip();
        assertTrue("Player should be marked as skipped after activation.", player.isSkipped());

        player.clearSkip();
        assertFalse("Player skip status should reset after clearing.", player.isSkipped());
    }

    @Test
    public void testBlockFlagBehavior() {
        assertFalse("Block should be false initially.", player.hasBlocked());

        player.useBlock();
        assertTrue("Block flag should be true after usage.", player.hasBlocked());
    }

    @Test
    public void testToStringOutput() {
        player.addMovie(inception);

        String expected = "Alice | Genre Goal: sci-fi (1/3)";
        assertEquals("toString output format mismatch.", expected, player.toString());
    }
}
