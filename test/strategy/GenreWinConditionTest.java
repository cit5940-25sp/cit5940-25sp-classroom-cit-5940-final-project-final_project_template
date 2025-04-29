package strategy;

import model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link strategy.GenreWinCondition} class.
 */
public class GenreWinConditionTest {

    /**
     * Tests that a GenreWinCondition object can be created.
     */
    @Test
    public void testGenreWinConditionInstantiation() {
        GenreWinCondition condition = new GenreWinCondition("Horror");
        assertNotNull(condition);
    }
}
