import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class GameStateTest {

    private Player player1;
    private Player player2;
    private Movie startingMovie;
    private GameState gameState;

    @Before
    public void setUp() {
        player1 = new Player("Alice");
        player2 = new Player("Bob");

        startingMovie = new Movie(
                1L,
                "Inception",
                2010,
                new HashSet<>(Arrays.asList("Sci-Fi")),
                new HashSet<>(Arrays.asList("Leonardo DiCaprio")),
                new HashSet<>(Arrays.asList("Christopher Nolan")),
                new HashSet<>(Arrays.asList("Jonathan Nolan")),
                new HashSet<>(Arrays.asList("Hans Zimmer")),
                new HashSet<>(Arrays.asList("Wally Pfister"))
        );

        WinCondition dummyWinCondition = new WinCondition() {
            @Override
            public boolean checkVictory(Player player) {
                return player.getNumMoviesGuessed() >= 1;
            }

            @Override
            public String description() {
                return "Dummy win after 1 movie";
            }
        };

        gameState = new GameState(player1, player2, dummyWinCondition, startingMovie);
    }

    @Test
    public void testInitialState() {
        assertEquals(player1, gameState.getCurrentPlayer());
        assertEquals(player2, gameState.getOtherPlayer());
        assertEquals(1, gameState.getCurrRound());
        assertTrue(gameState.isMovieUsed(startingMovie));
        assertEquals(startingMovie, gameState.getCurrentMovie());
    }

    @Test
    public void testAddMovieToHistory() {
        Movie movie2 = new Movie(
                2L,
                "Titanic",
                1997,
                new HashSet<>(Arrays.asList("Romance")),
                new HashSet<>(Arrays.asList("Leonardo DiCaprio")),
                new HashSet<>(Arrays.asList("James Cameron")),
                new HashSet<>(Arrays.asList("James Cameron")),
                new HashSet<>(Arrays.asList("James Horner")),
                new HashSet<>(Arrays.asList("Russell Carpenter"))
        );

        gameState.addMovieToHistory(movie2);
        assertTrue(gameState.isMovieUsed(movie2));
        assertEquals(movie2, gameState.getCurrentMovie());
    }

    @Test
    public void testSwitchPlayer() {
        Player current = gameState.getCurrentPlayer();
        gameState.switchPlayer();
        assertNotEquals(current, gameState.getCurrentPlayer());
        assertEquals(2, gameState.getCurrRound());
    }

    @Test
    public void testConnectionUsage() {
        String person = "Leonardo DiCaprio";
        assertTrue(gameState.canUseConnection(person));

        gameState.incrementConnectionUsage(person);
        gameState.incrementConnectionUsage(person);
        gameState.incrementConnectionUsage(person);

        assertFalse(gameState.canUseConnection(person));
    }

    @Test
    public void testRecentHistory() {
        Movie movie2 = new Movie(2L, "Titanic", 1997,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        Movie movie3 = new Movie(3L, "Dunkirk", 2017,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        Movie movie4 = new Movie(4L, "Tenet", 2020,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        Movie movie5 = new Movie(5L, "Memento", 2000,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        Movie movie6 = new Movie(6L, "The Prestige", 2006,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());

        gameState.addMovieToHistory(movie2);
        gameState.addMovieToHistory(movie3);
        gameState.addMovieToHistory(movie4);
        gameState.addMovieToHistory(movie5);
        gameState.addMovieToHistory(movie6);

        List<Movie> recent = gameState.getRecentHistory();
        assertEquals(5, recent.size());
        assertEquals(movie2, recent.get(0)); // oldest among recent
        assertEquals(movie6, recent.get(4)); // newest
    }

    @Test
    public void testWinConditionCheck() {
        assertTrue(gameState.hasCurrentPlayerWon()); // Based on dummy condition: guessed ≥ 1
    }
}