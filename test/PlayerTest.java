import org.junit.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    String objectiveGenre = "Horror";
    Player player1 = new Player("Andres", objectiveGenre, 5);
    Player player2 = new Player("Lucia", objectiveGenre, 3);

    @Test
    public void playerUsernameTest() {
        assertEquals("Lucia", player2.getUsername());
    }

    @Test
    public void genreTest() {
        assertEquals(objectiveGenre, player1.getObjectiveGenre());
    }

    @Test
    public void statusTest() {
        assertEquals(objectiveGenre, player1.getObjectiveGenre());
    }

    @Test
    public void progressSoFarTest() {
        List<String> connections = Arrays.asList("Emma Stone", "Ryan Gosling");
        List<String> genres = Arrays.asList("Horror");

        boolean result = player1.handleMovie(connections, genres);
        assertEquals((double) 1 /5 * 100, player1.progressSoFar());
    }

    @Test
    public void connectionUsedUpTest() {
        List<String> repeatedConnection = Arrays.asList("Emma Stone");
        List<String> genres = Arrays.asList("Horror");

        // Use up the connection
        for (int i = 0; i < 3; i++) {
            player1.handleMovie(repeatedConnection, genres);
        }

        boolean result = player1.handleMovie(repeatedConnection, genres);
        assertFalse(result);
    }

    @Test
    public void testHasMetObjectiveReturnsTrueWhenProgressMeetsGoal() {
        List<String> conn1 = Arrays.asList("Emma Stone");
        List<String> conn2 = Arrays.asList("Ryan Gosling");
        List<String> conn3 = Arrays.asList("Winona Ryder");
        List<String> genres = Arrays.asList("Horror");

        player2.handleMovie(conn1, genres);
        player2.handleMovie(conn2, genres);
        player2.handleMovie(conn3, genres);

        assertTrue(player2.hasMetObjective());
    }

    @Test
    public void handleMovieNoConnectionsTest() {
        List<String> connections = Collections.emptyList();
        List<String> genres = Arrays.asList("Horror");

        boolean result = player1.handleMovie(connections, genres);
        assertFalse(result);
    }

    @Test
    public void handleMovieDifferentGenreTest() {
        List<String> connections = Arrays.asList("Emma Stone");
        List<String> genres = Arrays.asList("Comedy");

        boolean result = player1.handleMovie(connections, genres);
        assertTrue(result);
        assertEquals(0.0, player1.progressSoFar());
    }
}