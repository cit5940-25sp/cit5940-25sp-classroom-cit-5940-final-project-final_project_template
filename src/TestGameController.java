import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TestGameController {

    private GameController controller;
    private Player player1;
    private Player player2;
    private GameView view;
    private Clock clock;
    private List<IMovie> movies;

    //    @Before
    public void setUp() {
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        view = new GameView();
        clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        movies = new ArrayList<>();

        controller = new GameController(player1, player2, clock, movies, view);
    }

    @Test
    public void testInitializeGame() {
        controller.initializeGame(movies);
        assertNotNull(controller);
    }

    @Test
    public void testStartGame() throws IOException {
        controller.startGame();
        assertFalse(controller.isGameOver());
    }

    @Test
    public void testNextTurn() {
        Player startingPlayer = controller.getCurrentPlayer();
        controller.nextTurn();
        assertNotEquals(startingPlayer, controller.getCurrentPlayer());
    }

    // entering an invalid movie shouldn't be game over
    @Test
    public void testHandlePlayerInput() {
        controller.handlePlayerInput("Invalid Movie");
        assertFalse(controller.isGameOver());
    }

    // ending the game should result in game over
    @Test
    public void testEndGame() {
        controller.endGame();
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testHandleTimeout() {
        controller.handleTimeout();
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testIsGameOverInitiallyFalseThenTrueAfterTimeout() {
        assertFalse(controller.isGameOver());
        controller.handleTimeout();
        assertTrue(controller.isGameOver());
    }
}
