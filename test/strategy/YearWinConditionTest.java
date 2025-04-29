package strategy;

import model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link strategy.YearWinCondition} class.
 */
public class YearWinConditionTest {

    /**
     * Tests that a YearWinCondition object can be instantiated with a target year.
     */
    @Test
    public void testYearWinConditionInstantiation() {
        YearWinCondition condition = new YearWinCondition(2010);
        assertNotNull(condition);
    }
}
