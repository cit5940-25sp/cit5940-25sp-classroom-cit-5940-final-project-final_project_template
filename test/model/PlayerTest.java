package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link model.Player} class.
 */
public class PlayerTest {

    /**
     * Tests that a Player object is created with the correct name.
     */
    @Test
    public void testPlayerCreation() {
        Player player = new Player("Alice");
        assertEquals("Alice", player.getPlayerName());
    }
}
