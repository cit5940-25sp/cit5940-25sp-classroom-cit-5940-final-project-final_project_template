import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

// Dummy WinCondition implementation for testing
class DummyWinCondition implements WinCondition {
    @Override
    public boolean checkWin(Player player) {
        return !player.getMoviesPlayed().isEmpty();  // Win if at least one movie is played
    }

    @Override
    public String getDescription() {
        return "Win if any movie is played";
    }
}

public class PlayerTest {

    @Test
    public void testConstructorAndGetters() {
        WinCondition winCondition = new DummyWinCondition();
        Player player = new Player("Alice", winCondition);

        assertEquals("Alice", player.getName());
        assertTrue(player.getMoviesPlayed().isEmpty());
        assertEquals(winCondition, player.getWinCondition());
    }

    @Test
    public void testAddMovie() {
        WinCondition winCondition = new DummyWinCondition();
        Player player = new Player("Bob", winCondition);
        Movie movie = new Movie("The Matrix", 1999);

        player.addMovie(movie);
        List<Movie> playedMovies = player.getMoviesPlayed();

        assertEquals(1, playedMovies.size());
        assertEquals(movie, playedMovies.get(0));
    }

    @Test
    public void testWinConditionIsMet() {
        WinCondition winCondition = new DummyWinCondition();
        Player player = new Player("Charlie", winCondition);

        assertFalse(winCondition.checkWin(player));

        Movie movie = new Movie("Inception", 2010);
        player.addMovie(movie);

        assertTrue(winCondition.checkWin(player));
    }
}