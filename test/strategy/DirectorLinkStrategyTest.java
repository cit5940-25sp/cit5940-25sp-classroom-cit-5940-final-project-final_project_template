package strategy;

import model.Movie;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link strategy.DirectorLinkStrategy} class.
 */
public class DirectorLinkStrategyTest {

    /**
     * Tests that a DirectorLinkStrategy object can be instantiated.
     */
    @Test
    public void testDirectorLinkStrategyInstantiation() {
        DirectorLinkStrategy strategy = new DirectorLinkStrategy();
        assertNotNull(strategy);
    }
}
