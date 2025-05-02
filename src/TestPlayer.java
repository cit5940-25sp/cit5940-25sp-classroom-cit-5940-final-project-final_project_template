import org.junit.Test;

import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class TestPlayer {
    @Test
    public void testGetName() {
        Player player = new Player("Tommy");
        assertEquals("Tommy", player.getName());
    }

    @Test
    public void testGetScore() {
        Player player = new Player("Tommy");
        assertEquals(0, player.getScore());
    }

    @Test
    public void testSetScore() {
        Player player = new Player("Tommy");
        player.setScore(5);
        assertEquals(5, player.getScore());
    }

    @Test
    public void testIncreaseScore() {
        Player player = new Player("Tommy");
        player.setScore(3);
        player.increaseScore(1);
        assertEquals(4, player.getScore());
    }

    @Test
    public void testGetPlayedMovies() {
        Player player = new Player("Tommy");
        assertTrue(player.getPlayedMovies().isEmpty());

        Movie movie = new Movie("Inception", 2010, List.of("Action", "Sci-Fi"));
        player.addPlayedMovie(movie);

        assertEquals(1, player.getPlayedMovies().size());
        assertEquals("Inception", player.getPlayedMovies().get(0).getTitle());
    }

    @Test
    public void testAddPlayedMovie() {
        Player player = new Player("Tommy");
        Movie movie = new Movie("Inception", 2010, List.of("Action"));
        player.addPlayedMovie(movie);
        assertEquals(1, player.getPlayedMovies().size());
    }

    @Test
    public void testSetWinConditionStrategy() {
        Player player = new Player("Tommy");

        IWinConditionStrategy strategy = new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) { return false; }
            public String getDescription() { return "Always false"; }
        };

        player.setWinConditionStrategy(strategy);
        assertEquals(strategy, player.getWinConditionStrategy());
    }

    @Test
    public void testGetWinConditionStrategy() {
        Player player = new Player("Tommy");

        IWinConditionStrategy strategy = new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) { return false; }
            public String getDescription() { return "Test strategy"; }
        };

        player.setWinConditionStrategy(strategy);
        assertEquals(strategy, player.getWinConditionStrategy());
    }

    @Test
    public void testHasWon() {
        Player player = new Player("Tommy");

        IWinConditionStrategy strategy = new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) {
                return !movies.isEmpty();  // win if at least one movie
            }
            public String getDescription() {
                return "Win if played one";
            }
        };

        player.setWinConditionStrategy(strategy);
        assertFalse(player.hasWon());

        Movie movie = new Movie("Dune", 2021, List.of("Sci-Fi"));
        player.addPlayedMovie(movie);
        assertTrue(player.hasWon());
    }

    @Test
    public void testGetWinConditionDescription() {
        Player player = new Player("Yerin");

        IWinConditionStrategy strategy = new IWinConditionStrategy() {
            public boolean checkWin(List<IMovie> movies) { return true; }
            public String getDescription() { return "Always win"; }
        };

        player.setWinConditionStrategy(strategy);
        assertEquals("Always win", player.getWinConditionDescription());
    }
}
