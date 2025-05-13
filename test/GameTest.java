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
    public void testGetLastFivePlayedHasOneElement() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertEquals(1, game.getLastFivePlayed().size());
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

    @Test
    public void testIsGameOver_NoWinner() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Comedy", "Drama");
        assertFalse(game.isGameOver());
        assertNull(game.getWinner());
    }
}

