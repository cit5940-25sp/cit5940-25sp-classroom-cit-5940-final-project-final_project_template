import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GenreWinConditionTest {

    @Test
    public void testGenreMatch() {
        GenreWinCondition winCondition = new GenreWinCondition("Action");
        Player player = new Player("TestPlayer", winCondition);
        Movie actionMovie = new Movie("Avengers", 2012);
        actionMovie.setGenres(Arrays.asList("Action", "Adventure"));

        player.addMovie(actionMovie);

        assertTrue(winCondition.checkWin(player));
        assertEquals("Has a movie with genre: Action", winCondition.getDescription());
    }

    @Test
    public void testNoGenreMatch() {
        GenreWinCondition winCondition = new GenreWinCondition("Drama");
        Player player = new Player("TestPlayer", winCondition);
        Movie comedyMovie = new Movie("The Mask", 1994);
        comedyMovie.setGenres(Arrays.asList("Comedy"));

        player.addMovie(comedyMovie);

        assertFalse(winCondition.checkWin(player));
    }

    @Test
    public void testEmptyMovieList() {
        GenreWinCondition winCondition = new GenreWinCondition("Horror");
        Player player = new Player("TestPlayer", winCondition);

        assertFalse(winCondition.checkWin(player));
    }
}
