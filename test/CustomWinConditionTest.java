import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;
import java.util.List;

/**
 * Unit tests for the CustomWinCondition class.
 * This class tests the functionality of the CustomWinCondition
 * with various conditions and scenarios.
 *
 * @author Vera Zhang
 */
public class CustomWinConditionTest {

    @Test
    public void testConditionMet() {
        Function<List<Movie>, Boolean> condition = movies -> movies.size() >= 2;
        CustomWinCondition winCondition = new CustomWinCondition(condition, "At least 2 movies");

        Player player = new Player("TestPlayer", winCondition);
        player.addMovie(new Movie("Movie A", 2000));
        player.addMovie(new Movie("Movie B", 2001));

        assertTrue(winCondition.checkWin(player));
        assertEquals("At least 2 movies", winCondition.getDescription());
    }

    @Test
    public void testConditionNotMet() {
        Function<List<Movie>, Boolean> condition = movies -> movies.stream()
                .anyMatch(m -> m.getTitle().equalsIgnoreCase("Avengers"));
        CustomWinCondition winCondition = new CustomWinCondition(condition, "Contains 'Avengers'");

        Player player = new Player("TestPlayer", winCondition);
        player.addMovie(new Movie("Iron Man", 2008));

        assertFalse(winCondition.checkWin(player));
    }

    @Test
    public void testEmptyMovieList() {
        Function<List<Movie>, Boolean> condition = List::isEmpty;
        CustomWinCondition winCondition = new CustomWinCondition(condition, "Empty list means win");

        Player player = new Player("TestPlayer", winCondition);

        assertTrue(winCondition.checkWin(player));
    }
}

