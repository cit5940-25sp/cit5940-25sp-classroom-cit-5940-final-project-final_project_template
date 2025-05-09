import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGameView {
    static class MockTerminal extends TerminalWithSuggestions {
        public MockTerminal() throws IOException {
            super();
        }

        List<String> messages = new ArrayList<>();

        @Override
        public void displayMessage(String message) {
            messages.add(message);
        }

        public List<String> getMessages() {
            return messages;
        }
    }

    static class DummyConnectionValidator extends ConnectionValidator {
        @Override
        public List<String> getSharedConnections(IMovie a, IMovie b) {
            return Arrays.asList("Dummy Link");
        }
    }

    @Test
    public void testShowWelcomeMessage() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        view.showWelcomeMessage();
        assertEquals("Welcome to the Movie Name Game!", terminal.getMessages().get(0));
    }

    @Test
    public void testShowGameStart() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IPlayer player = new Player("Melody");
        view.showGameStart(player);
        assertEquals("Round start: Melody", terminal.getMessages().get(0));
    }

    @Test
    public void testPromptForMovie() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IPlayer player = new Player("Yerin");
        view.promptForMovie(player);
        assertEquals("Yerin, enter a movie title.", terminal.getMessages().get(0));
    }

    @Test
    public void testShowMoveSuccess() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IPlayer player = new Player("Tommy");
        view.showMoveSuccess("Inception", player);
        assertEquals("Tommy played Inception", terminal.getMessages().get(0));
    }

    @Test
    public void testShowInvalidMove() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        view.showInvalidMove("BadMovie");
        assertEquals("Invalid movie title.", terminal.getMessages().get(0));
    }

    @Test
    public void testShowTimeout() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IPlayer player = new Player("Stupid Tommy");
        view.showTimeout(player);
        assertEquals("Stupid Tommy ran out of time.", terminal.getMessages().get(0));
    }

    @Test
    public void testShowWinner() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IPlayer player = new Player("Magnus Carlsen");
        view.showWinner(player);
        assertEquals("Magnus Carlsen is the winner!", terminal.getMessages().get(0));
    }

    @Test
    public void testShowNextTurn() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IPlayer player = new Player("Arvind");
        view.showNextTurn(player);
        assertEquals("Next: Arvind", terminal.getMessages().get(0));
    }

    @Test
    public void testShowWinConditions() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        Player player = new Player("Harry");
        player.setWinConditionStrategy(new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) { return false; }
            public String getDescription() { return "Win if you guess 5 dramas"; }
        });
        view.showWinConditions(Arrays.asList(player));
        assertTrue(terminal.getMessages().get(0).contains("Harry win condition: Win if you guess 5 dramas"));
    }

    @Test
    public void testShowMovieHistory() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IMovie movie1 = new Movie("Movie A", 2000, Arrays.asList("Drama"));
        IMovie movie2 = new Movie("Movie B", 2001, Arrays.asList("Action"));
        view.showMovieHistory(Arrays.asList(movie1, movie2));
        assertEquals("Last 5 movies:", terminal.getMessages().get(0));
        assertTrue(terminal.getMessages().get(1).contains("Movie A"));
        assertTrue(terminal.getMessages().get(2).contains("Links to next"));
        assertTrue(terminal.getMessages().get(3).contains("Movie B"));
    }

    @Test
    public void testShowPlayerStats() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameModel model = new GameModel();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), model);

        Player player = new Player("Seong Jin Cho");
        player.setScore(3);
        player.setWinConditionStrategy(new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) { return false; }
            public String getDescription() { return "Dummy condition"; }
        });

        view.showPlayerStats(Arrays.asList(player), 2);

        assertTrue(terminal.getMessages().get(0).contains("Player stats after round 2"));
        assertTrue(terminal.getMessages().get(1).contains("Seong Jin Cho | Score: 3"));
        assertTrue(terminal.getMessages().get(2).contains("Dummy condition"));
        assertTrue(terminal.getMessages().get(3).contains("Progress: 0 / 5"));
    }

    @Test
    public void testEstimateProgressFallback() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameModel model = new GameModel();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), model);

        Player player = new Player("Fallback Player");
        player.setWinConditionStrategy(new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) { return false; }
            public String getDescription() { return "Custom"; }
        });

        view.showPlayerStats(Arrays.asList(player), 1);
        assertTrue(terminal.getMessages().get(3).contains("Progress: 0 / 5"));
    }
}