import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GameControllerTest {
    private GameController controller;
    private FakeGameView view;
    private FakeMovieDatabase db;

    @Before
    public void setUp() {
        db = new FakeMovieDatabase();
        view = new FakeGameView();
        controller = new GameController(db, view);

        // Add fake starting movie
        Movie godfather = new Movie(1L, "The Godfather", 1972,
            Set.of(), Set.of("Al Pacino"), Set.of(), Set.of(), Set.of(), Set.of());
        db.addFakeMovie(godfather);
    }

    @Test
    public void testProcessTurn_ValidConnection_Succeeds() {
        Movie godfather = db.findByTitle("The Godfather");
        Movie heat = new Movie(2L, "Heat", 1995,
            Set.of(), Set.of("Al Pacino"), Set.of(), Set.of(), Set.of(), Set.of());
        db.addFakeMovie(heat);

        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        GameState state = new GameState(p1, p2, new FiveHorrorMoviesWin(), godfather);
        controller.setGameState(state);
        state.getTimer().start();

        controller.processTurn("Heat");

        assertTrue(view.messages.stream().anyMatch(msg -> msg.contains("successfully connected")));
    }


    @Test
    public void testProcessTurn_MovieNotFound() {
        controller.startGame("Alice", "Bob", new FiveHorrorMoviesWin());
        controller.getGameState().getTimer().start();

        controller.processTurn("Unknown Movie");

        assertTrue(view.messages.stream().anyMatch(msg -> msg.contains("Movie not found")));
    }

    @Test
    public void testProcessTurn_Timeout() {
        controller.startGame("Alice", "Bob", new FiveHorrorMoviesWin());
        // Do not start the timer (simulate timeout)

        controller.processTurn("Any Movie");

        assertTrue(view.messages.stream().anyMatch(msg -> msg.contains("Time's up")));
    }

    // Helper fake classes
    class FakeMovieDatabase extends MovieDatabase {
        private final Map<String, Movie> movies = new HashMap<>();

        public FakeMovieDatabase() {
            super("fake-api-key");
        }

        public void addFakeMovie(Movie movie) {
            movies.put(movie.getTitle(), movie);
        }

        @Override
        public Movie findByTitle(String title) {
            return movies.get(title);
        }
    }

    class FakeGameView extends GameView {
        public final List<String> messages = new ArrayList<>();

        @Override
        public void displayInfo(String message) {
            messages.add(message);
        }

        @Override
        public void render(GameState state) {

        }
    }
}


