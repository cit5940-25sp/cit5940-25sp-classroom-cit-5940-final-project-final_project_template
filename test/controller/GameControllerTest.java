package controller;

import model.MovieIndex;
import model.Player;
import model.Movie;
import strategy.ILinkStrategy;
import strategy.IWinCondition;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for the {@link controller.GameController} class.
 */
public class GameControllerTest {

    /**
     * Tests that a GameController object can be created with dummy data.
     */
    @Test
    public void testGameControllerInstantiation() {
        ILinkStrategy dummyLinkStrategy = new ILinkStrategy() {
            @Override
            public boolean isValidLink(Movie from, Movie to) {
                return true;
            }

            @Override
            public String getReason(Movie from, Movie to) {
                return "Dummy connection";
            }
        };

        IWinCondition dummyWinCondition = new IWinCondition() {
            @Override
            public boolean checkWin(Player player) {
                return false;
            }

            @Override
            public String getDescription() {
                return "Dummy win condition";
            }
        };

        GameController controller = new GameController(
                new MovieIndex(List.of()),
                dummyLinkStrategy,
                dummyWinCondition,
                new Player("Alice"),
                new Player("Bob")
        );
        assertNotNull(controller);
    }

}
