package strategy;

import model.Movie;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link strategy.ActorLinkStrategy} class.
 */
public class ActorLinkStrategyTest {

    /**
     * Tests that an ActorLinkStrategy object can be created.
     */
    @Test
    public void testActorLinkStrategyInstantiation() {
        ActorLinkStrategy strategy = new ActorLinkStrategy();
        assertNotNull(strategy);
    }
}
