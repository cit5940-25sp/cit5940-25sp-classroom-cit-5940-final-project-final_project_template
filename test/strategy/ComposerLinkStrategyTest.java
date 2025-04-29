package strategy;

import model.Movie;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link strategy.ComposerLinkStrategy} class.
 */
public class ComposerLinkStrategyTest {

    /**
     * Tests that a ComposerLinkStrategy object can be instantiated.
     */
    @Test
    public void testComposerLinkStrategyInstantiation() {
        ComposerLinkStrategy strategy = new ComposerLinkStrategy();
        assertNotNull(strategy);
    }
}
