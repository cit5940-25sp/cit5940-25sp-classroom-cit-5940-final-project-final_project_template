import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

/**
 * Unit tests for the ActorWinCondition class.
 * This class tests the functionality of the ActorWinCondition
 * with various actors and scenarios.
 *
 * @author Vera Zhang
 */
public class ActorWinConditionTest {

    @Test
    public void testActorFound() {
        ActorWinCondition winCondition = new ActorWinCondition("Robert Downey Jr.");
        Player player = new Player("TestPlayer", winCondition);
        Movie movie = new Movie("Iron Man", 2008);
        movie.setActors(Arrays.asList("Robert Downey Jr.", "Gwyneth Paltrow"));

        player.addMovie(movie);

        assertTrue(winCondition.checkWin(player));
        assertEquals("Has a movie with actor: Robert Downey Jr.", winCondition.getDescription());
    }

    @Test
    public void testActorNotFound() {
        ActorWinCondition winCondition = new ActorWinCondition("Scarlett Johansson");
        Player player = new Player("TestPlayer", winCondition);
        Movie movie = new Movie("The Hulk", 2003);
        movie.setActors(Arrays.asList("Eric Bana", "Jennifer Connelly"));

        player.addMovie(movie);

        assertFalse(winCondition.checkWin(player));
    }

    @Test
    public void testEmptyMovieList() {
        ActorWinCondition winCondition = new ActorWinCondition("Chris Evans");
        Player player = new Player("TestPlayer", winCondition);

        assertFalse(winCondition.checkWin(player));
    }
}
