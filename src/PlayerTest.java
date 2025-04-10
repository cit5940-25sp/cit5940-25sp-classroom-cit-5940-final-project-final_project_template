import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;

    @Before
    public void setup() {
        // 创建玩家 Alice，目标是说出 3 部 Sci-Fi 类型的电影
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
        player.addMovie("Inception", "Sci-Fi");
        player.addMovie("Interstellar", "Sci-Fi");
        player.addMovie("The Matrix", "Sci-Fi");

        assertTrue("Player should meet win condition after 3 genre-matching movies.", player.hasMetWinCondition());
    }

    @Test
    public void testDuplicateMovieIsIgnored() {
        player.addMovie("Inception", "Sci-Fi");
        player.addMovie("Inception", "Sci-Fi");
        player.addMovie("The Matrix", "Sci-Fi");

        assertFalse("Duplicate movies should not count toward win condition.", player.hasMetWinCondition());
    }

    @Test
    public void testMovieTitleIsCaseInsensitive() {
        player.addMovie("Inception", "Sci-Fi");

        assertTrue("Movie title check should be case-insensitive.", player.hasUsedMovie("INCEPTION"));
        assertTrue(player.hasUsedMovie("inception"));
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
        player.addMovie("Inception", "Sci-Fi");

        String expected = "Alice | Genre Goal: sci-fi (1/3)";
        assertEquals("toString output format mismatch.", expected, player.toString());
    }
}
