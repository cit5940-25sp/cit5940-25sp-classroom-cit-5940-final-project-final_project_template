import org.junit.Test;
import java.io.IOException;
import java.time.Clock;
import java.util.*;

import static org.junit.Assert.*;

public class TestGameController {

    static class MockTerminal extends TerminalWithSuggestions {
        private Queue<String> inputs = new LinkedList<>();
        private List<String> messages = new ArrayList<>();

        public MockTerminal(String... responses) throws IOException {
            super();
            inputs.addAll(Arrays.asList(responses));
        }

        @Override
        public String getInputWithSuggestions(List<IMovie> movies, IMovie currentMovie, int timeLimitSeconds) {
            return inputs.isEmpty() ? "" : inputs.poll();
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
            return List.of("Nolan");
        }
    }

    private IMovie mockMovie() {
        Movie movie = new Movie("Inception", 2010, List.of("Sci-Fi", "Action"));
        movie.addActor("Nolan");
        movie.addDirector("Nolan");
        movie.addWriter("Nolan");
        movie.addContributor("Nolan");
        return movie;
    }

    private GameController makeController(MockTerminal terminal, List<IMovie> movieList, GameModel model) throws IOException {
        model.initializePlayers();
        model.getPlayer1().setWinConditionStrategy(new ActorWinCondition("Nolan"));
        model.getPlayer2().setWinConditionStrategy(new ActorWinCondition("Nolan"));
        ConnectionValidator validator = new DummyConnectionValidator();
        return new GameController(Clock.systemUTC(), movieList, terminal, validator, model);
    }

    @Test
    public void testInitializeGameInitializesPlayers() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameModel model = new GameModel();
        GameController controller = makeController(terminal, List.of(mockMovie()), model);
        controller.initializeGame(List.of(mockMovie()));
        assertEquals(2, model.getPlayers().size());
    }

    @Test
    public void testHandlePlayerInputInvalid() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameModel model = new GameModel();
        GameController controller = makeController(terminal, List.of(mockMovie()), model);
        controller.initializeGame(List.of(mockMovie()));
        controller.handlePlayerInput("Fake Movie");
        assertEquals(0, model.getCurrentPlayer().getScore());
    }

    @Test
    public void testNextTurnSwitchesPlayerCorrectly() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameModel model = new GameModel();
        GameController controller = makeController(terminal, List.of(mockMovie()), model);
        controller.initializeGame(List.of(mockMovie()));
        IPlayer original = model.getCurrentPlayer();
        controller.nextTurn();
        assertNotSame(original, model.getCurrentPlayer());
    }

    @Test
    public void testEndGameSetsGameOverTrue() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameModel model = new GameModel();
        GameController controller = makeController(terminal, List.of(mockMovie()), model);
        assertFalse(controller.isGameOver());
        controller.endGame();
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testStartGameWithValidMoveCompletesSuccessfully() throws IOException {
        MockTerminal terminal = new MockTerminal("Inception");
        GameModel model = new GameModel();
        GameController controller = makeController(terminal, List.of(mockMovie()), model);
        controller.initializeGame(List.of(mockMovie()));
        controller.startGame();
        assertTrue(controller.isGameOver() || model.getCurrentPlayer().getScore() > 0);
    }

    @Test
    public void testStartGameWithTimeoutTriggersGameOver() throws IOException {
        MockTerminal terminal = new MockTerminal("");
        GameModel model = new GameModel();
        GameController controller = makeController(terminal, List.of(mockMovie()), model);
        controller.initializeGame(List.of(mockMovie()));
        controller.startGame();
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testStartGameHandlesInvalidThenValidMove() throws IOException {
        MockTerminal terminal = new MockTerminal("Fake", "Inception");
        GameModel model = new GameModel();
        GameController controller = makeController(terminal, List.of(mockMovie()), model);
        controller.initializeGame(List.of(mockMovie()));
        controller.startGame();
        assertTrue(controller.isGameOver() || model.getCurrentPlayer().getScore() > 0);
    }

    @Test
    public void testCurrentPlayerInModelNotNull() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameModel model = new GameModel();
        GameController controller = makeController(terminal, List.of(mockMovie()), model);
        controller.initializeGame(List.of(mockMovie()));
        assertNotNull(model.getCurrentPlayer());
    }
}