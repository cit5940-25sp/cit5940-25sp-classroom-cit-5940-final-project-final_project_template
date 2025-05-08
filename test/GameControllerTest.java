import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GameControllerTest {
    private GameController controller;
    private FakeMovieDatabase db;

    @Before
    public void setUp() {
        db = new FakeMovieDatabase();
        controller = new GameController(db);

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
        GameState state = new GameState(p1, p2, new TwoHorrorMoviesWin(), godfather);
        controller.setGameState(state);
        //state.getTimer().start();
        // call proccessTurn and manually update FakeGameView
        TurnResult result = controller.processTurn("Heat");
        // Assert
        assertTrue(result.isSuccess());
        assertTrue(result.getMessage().contains("Nice! The Godfather and Heat connected via"));
    }


    @Test
    public void testProcessTurn_MovieNotFound() {
        controller.startGame("Alice", "Bob", new TwoHorrorMoviesWin());
        //controller.getGameState().getTimer().start();
        //controller.processTurn("Unknown Movie");
        TurnResult result = controller.processTurn("Unknown Movie");
        // Assert
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("is not found in the database."));
    }
    @Test
    public void testProcessTurn_AlreadyUsedMovie() {
        // Arrange
        Movie godfather = db.findByTitle("The Godfather");
        Movie heat = new Movie(2L, "Heat", 1995,
            Set.of(), Set.of("Al Pacino"), Set.of(), Set.of(), Set.of(), Set.of());
        db.addFakeMovie(heat);

        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        GameState state = new GameState(p1, p2, new TwoHorrorMoviesWin(), godfather);
        controller.setGameState(state);

        // Act
        controller.processTurn("Heat");
        TurnResult result = controller.processTurn("Heat");

        // Assert
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("already used"));
    }

    @Test
    public void testProcessTurn_NoValidConnection() {
        // Arrange
        Movie godfather = db.findByTitle("The Godfather");
        Movie titanic = new Movie(3L, "Titanic", 1997,
            Set.of(), Set.of("Leonardo DiCaprio"), Set.of(), Set.of(), Set.of(), Set.of());
        db.addFakeMovie(titanic);

        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        GameState state = new GameState(p1, p2, new TwoHorrorMoviesWin(), godfather);
        controller.setGameState(state);

        // Act
        TurnResult result = controller.processTurn("Titanic");

        // Assert
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("no valid connection"));
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

}


