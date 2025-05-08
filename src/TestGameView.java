import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
    public void testShowCurrentMovie() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IMovie movie = new Movie("Oppenheimer", 2023, Arrays.asList("Biography", "Drama"));
        view.showCurrentMovie(movie);
        assertEquals("Current Movie: Oppenheimer", terminal.getMessages().get(0));
        assertEquals("Genres: [Biography, Drama]", terminal.getMessages().get(1));
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
        assertEquals("Harry win condition: Win if you guess 5 dramas", terminal.getMessages().get(0));
    }

    @Test
    public void testShowMovieHistory() throws IOException {
        MockTerminal terminal = new MockTerminal();
        GameView view = new GameView(terminal, new DummyConnectionValidator(), new GameModel());
        IMovie movie1 = new Movie("Movie A", 2000, Arrays.asList("Drama"));
        IMovie movie2 = new Movie("Movie B", 2001, Arrays.asList("Action"));
        view.showMovieHistory(Arrays.asList(movie1, movie2));
        assertEquals("Last 5 movies:", terminal.getMessages().get(0));
        assertEquals("- Movie A [Drama]", terminal.getMessages().get(1));
        assertEquals("  ↳ Links to next: [Dummy Link]", terminal.getMessages().get(2));
        assertEquals("- Movie B [Action]", terminal.getMessages().get(3));
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

        assertEquals("Player stats after round 2", terminal.getMessages().get(0));
        assertEquals("Seong Jin Cho | Score: 3", terminal.getMessages().get(1));
        assertEquals("  ↳ Dummy condition", terminal.getMessages().get(2));
        assertEquals("  ↳ Progress: 0 / 5", terminal.getMessages().get(3));
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
        assertEquals("  ↳ Progress: 0 / 5", terminal.getMessages().get(3));
    }
}
