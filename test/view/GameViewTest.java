package view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link view.GameView} class.
 */
public class GameViewTest {

    /**
     * Basic test to ensure a GameView object can be instantiated.
     */
    @Test
    public void testGameViewInstantiation() {
        GameView view = new GameView();
        assertNotNull(view);
    }
}
