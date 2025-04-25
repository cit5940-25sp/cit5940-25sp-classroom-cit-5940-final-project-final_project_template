/**
 * Controls the overall game flow, coordinating interactions between
 * the model (GameState, MovieDatabase), and the view (GameView).
 */
public class GameController {
    private GameState gameState;
    private GameView view;
    private MovieDatabase movieDb;

    /**
     * Starts a new game session with the specified players and win condition.
     *
     * @param p1   name of Player 1
     * @param p2   name of Player 2
     * @param cond the win condition strategy
     */
    public void startGame(String p1, String p2, WinCondition cond){

    }

    /**
     * Processes a player's turn using the guessed movie title.
     *
     * @param movieTitle the title of the movie guessed
     */
    public void processTurn(String movieTitle){

    }

    /**
     * Checks if two movies are connected by a valid shared attribute.
     *
     * @param from the previously guessed movie
     * @param to   the newly guessed movie
     * @return true if the connection is valid; false otherwise
     */
    public boolean isValidConnection(Movie from, Movie to){
        return false;
    }

    /**
     * Finds a valid connection between two movies if one exists.
     *
     * @param from the source movie
     * @param to   the target movie
     * @return the Connection object if a valid one is found
     */
    private Connection findValidConnection(Movie from, Movie to){
        return null;
    }
}
