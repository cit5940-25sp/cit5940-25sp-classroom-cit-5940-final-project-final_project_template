import org.junit.Test;

import java.io.IOException;
import java.time.Clock;
import java.util.*;

import static org.junit.Assert.*;

public class TestGameController {

    static class MockTerminal extends TerminalWithSuggestions {
        private Queue<String> inputs = new LinkedList<String>();
        private List<String> messages = new ArrayList<String>();

        public MockTerminal(String... responses) throws IOException {
            super();
            for (int i = 0; i < responses.length; i++) {
                inputs.add(responses[i]);
            }
        }

        @Override
        public String getInputWithSuggestions(List<IMovie> movies, IMovie currentMovie, int timeLimitSeconds) {
            if (inputs.isEmpty()) return "";
            return inputs.poll();
        }

        @Override
        public void displayMessage(String message) {
            messages.add(message);
        }

        public List<String> getMessages() {
            return messages;
        }

        @Override
        public void clearScreen() {}
    }

    static class DummyConnectionValidator extends ConnectionValidator {
        @Override
        public List<String> getSharedConnections(IMovie a, IMovie b) {
            return Arrays.asList("Shared Contributor");
        }
    }

    private IMovie mockMovie() {
        Movie movie = new Movie("Inception", 2010, Arrays.asList("Sci-Fi", "Action"));
        movie.addActor("Nolan");
        movie.addDirector("Nolan");
        movie.addWriter("Nolan");
        return movie;
    }

    private GameController makeController(MockTerminal terminal, List<IMovie> movieList) throws IOException {
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        p1.setWinConditionStrategy(new ActorWinCondition("Nolan"));
        p2.setWinConditionStrategy(new ActorWinCondition("Nolan"));
        ConnectionValidator validator = new DummyConnectionValidator();
        GameModel model = new GameModel();
        return new GameController(p1, p2, Clock.systemUTC(), movieList, terminal, validator, model);
    }

    @Test
    public void testInitializeGameInitializesPlayers() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameController controller = makeController(terminal, Arrays.asList(mockMovie()));
        controller.initializeGame(Arrays.asList(mockMovie()));
        assertNotNull(controller.getCurrentPlayer());
    }

    @Test
    public void testHandlePlayerInputInvalid() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameController controller = makeController(terminal, Arrays.asList(mockMovie()));
        controller.initializeGame(Arrays.asList(mockMovie()));
        controller.handlePlayerInput("Fake Movie");
        assertEquals(0, controller.getCurrentPlayer().getScore());
    }

    @Test
    public void testNextTurnSwitchesPlayerCorrectly() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameController controller = makeController(terminal, Arrays.asList(mockMovie()));
        controller.initializeGame(Arrays.asList(mockMovie()));
        Player original = controller.getCurrentPlayer();
        controller.nextTurn();
        assertNotSame(original, controller.getCurrentPlayer());
    }

    @Test
    public void testEndGameSetsGameOverTrue() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameController controller = makeController(terminal, Arrays.asList(mockMovie()));
        assertFalse(controller.isGameOver());
        controller.endGame();
        assertTrue(controller.isGameOver());
    }


    @Test
    public void testStartGameWithValidMoveCompletesSuccessfully() throws IOException {
        MockTerminal terminal = new MockTerminal("Inception");
        GameController controller = makeController(terminal, Arrays.asList(mockMovie()));
        controller.initializeGame(Arrays.asList(mockMovie()));
        controller.startGame();
        assertTrue(controller.isGameOver() || controller.getCurrentPlayer().getScore() > 0);
    }

    @Test
    public void testStartGameWithTimeoutTriggersGameOver() throws IOException {
        MockTerminal terminal = new MockTerminal("");
        GameController controller = makeController(terminal, Arrays.asList(mockMovie()));
        controller.initializeGame(Arrays.asList(mockMovie()));
        controller.startGame();
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testStartGameHandlesInvalidThenValidMove() throws IOException {
        MockTerminal terminal = new MockTerminal("Fake", "Inception");
        GameController controller = makeController(terminal, Arrays.asList(mockMovie()));
        controller.initializeGame(Arrays.asList(mockMovie()));
        controller.startGame();
        assertTrue(controller.isGameOver() || controller.getCurrentPlayer().getScore() > 0);
    }

    @Test
    public void testGetCurrentPlayerNotNull() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameController controller = makeController(terminal, Arrays.asList(mockMovie()));
        assertNotNull(controller.getCurrentPlayer());
    }
}
