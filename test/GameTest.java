import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class GameTest {

    @Test
    public void testGameInitialization() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertNotNull(game);
        assertEquals("Player1", game.usernamePlayer1());
        assertEquals("Player2", game.usernamePlayer2());
        assertNotNull(game.getCurrentMovie());
    }

    @Test
    public void testPlayerTurn() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertEquals("Player1", game.getWhosTurn());
        game.forcePlayerTurn("Player2");
        assertEquals("Player2", game.getWhosTurn());
    }

    @Test
    public void testGameConditions() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertEquals("Comedy", game.gameConditionPlayer1());
        assertEquals("Drama", game.gameConditionPlayer2());
    }

    @Test
    public void testConnectionByVariousRoles() {
        Movies movies = new Movies("src/tmdb_data.txt");

        // These two movies are confirmed to be connected by a cast/crew member
        List<String> connections = movies.getConnection("Avatar (2009)", "Pirates of the Caribbean: At World's End (2007)");
        System.out.println("Connections: " + connections);

        // Validate that they are connected by any role
        assertTrue(!connections.isEmpty());
        boolean validConnection = connections.stream().anyMatch(conn ->
                conn.startsWith("castAndCrew: ") ||
                        conn.startsWith("director: ") ||
                        conn.startsWith("writer: ") ||
                        conn.startsWith("cinematographer: ") ||
                        conn.startsWith("composer: ")
        );
        assertTrue(validConnection);
    }


    @Test
    public void testInvalidMoveAlreadyPlayed() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        String movie = game.getCurrentMovie();
        assertFalse(game.update(movie, "Player1"));
    }

    @Test
    public void testInvalidMoveNoConnection() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertFalse(game.update("Nonexistent Movie", "Player1"));
    }

    @Test
    public void testForcedPlayerTurn() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        game.forcePlayerTurn("Player2");
        assertEquals("Player2", game.getWhosTurn());
    }

    @Test
    public void testGetLastFivePlayedEmpty() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertTrue(game.getLastFivePlayed().isEmpty());
    }

    @Test
    public void testPlayerLinkUsageDisplay() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertNotNull(game.getPlayer1LinkUsageDisplay());
        assertNotNull(game.getPlayer2LinkUsageDisplay());
    }

    @Test
    public void testRoundsPlayed() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertEquals(0, game.getRoundsPlayed());
    }

    @Test
    public void testAutocompleteFileName() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertEquals("src/autocomplete.txt", game.getAutocompleteFileName());
    }

}

