package controller;

import model.MovieIndex;
import model.Player;
import strategy.ILinkStrategy;
import strategy.IWinCondition;

/**
 * Controls the main gameplay loop and manages game state transitions for the Movie Name Game.
 */
public class GameController {
    private MovieIndex movieIndex;
    private ILinkStrategy linkStrategy;
    private IWinCondition winCondition;
    private Player currentPlayer;
    private Player otherPlayer;

    /**
     * Constructs a GameController
     * to manage the game with the provided index, strategy, and players.
     * @param movieIndex the movie index for lookup operations
     * @param linkStrategy the strategy for determining valid links
     * @param winCondition the win condition to check for victory
     * @param p1 the first player
     * @param p2 the second player
     */
    public GameController(
            MovieIndex movieIndex,
            ILinkStrategy linkStrategy,
            IWinCondition winCondition,
            Player p1,
            Player p2
    ) {
        // TODO
    }

    /**
     * Runs the main game loop, alternating turns between players until a win or loss occurs.
     */
    public void runGame() {
        // TODO
    }

    /**
     * Processes a player's move by validating the submitted movie title.
     * @param movieTitle the title of the movie submitted by the player
     */
    public void processMove(String movieTitle) {
        // TODO
    }

    /**
     * Checks whether the game has ended.
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return false;
    }

    /**
     * Switches the turn to the other player.
     */
    public void switchTurn() {
        // TODO
    }
}
