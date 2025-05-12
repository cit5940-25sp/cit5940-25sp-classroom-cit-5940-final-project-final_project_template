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
    public void testPlayerProgress() {
        Game game = new Game("src/tmdb_data.txt", "Player1", "Player2", "Drama", "Comedy");

        // Ensure initial progress is zero
        assertEquals(0.0, game.progressPlayer1(), 0.001);
        assertEquals(0.0, game.progressPlayer2(), 0.001);

        // Use a Drama movie for Player 1's objective
        boolean move1 = game.update("Dead Like Me: Life After Death (2009)", "Player1");
        System.out.println("Player 1 Move 1: " + move1);
        System.out.println("Player 1 Progress: " + game.progressPlayer1());

        // Validate progress
        assertTrue(move1);
        assertTrue(game.progressPlayer1() > 0.0);
        assertEquals(0.0, game.progressPlayer2(), 0.001);

        // Use a Comedy movie for Player 2's objective
        game.forcePlayerTurn("Player2");
        boolean move2 = game.update("Flying By (2009)", "Player2");
        System.out.println("Player 2 Move 2: " + move2);
        System.out.println("Player 2 Progress: " + game.progressPlayer2());

        // Validate progress
        assertTrue(move2);
        assertTrue(game.progressPlayer2() > 0.0);
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

    @Test
    public void testWinningCondition() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Drama", "Comedy");

        // Two valid connected Drama movies for Player 1
        boolean move1 = game.update("Dead Like Me: Life After Death (2009)", "Player1");
        System.out.println("Player 1 Move 1: " + move1);

        game.forcePlayerTurn("Player1");
        boolean move2 = game.update("Flying By (2009)", "Player1");
        System.out.println("Player 1 Move 2: " + move2);

        assertTrue(game.isGameOver());
        assertEquals("Player1", game.getWinner());
    }


    @Test
    public void testLastFivePlayed() {
        Game game = new Game("src/tmdb_data.txt", "Player1",
                "Player2", "Drama", "Comedy");

        assertTrue(game.getLastFivePlayed().isEmpty());
        boolean move1 = game.update("Dead Like Me: Life After Death (2009)", "Player1");
        System.out.println("Player 1 Move 1: " + move1);

        game.forcePlayerTurn("Player2");
        boolean move2 = game.update("Flying By (2009)", "Player2");
        System.out.println("Player 2 Move 2: " + move2);

        // The list should now contain two movies
        System.out.println("Last Five Played: " + game.getLastFivePlayed());
        assertEquals(2, game.getLastFivePlayed().size());
    }
}

