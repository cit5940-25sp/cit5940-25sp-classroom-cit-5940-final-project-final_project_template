import java.util.*;

/**
 * Maintains the current state of the game including players, history,
 * and current round.
 */
public class GameState {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private int currRound;
    private WinCondition winCondition;
    private List<Movie> history;
    private Map<Connection, Integer> countConnection;

    /**
     * Adds a movie to the game's history.
     *
     * @param movie the movie to add
     */
    public void addMovieToHistory(Movie movie){

    }

    /**
     * Retrieves the most recent history of played movies.
     *
     * @return list of recently played movies
     */
    public List<Movie> getRecentHistory(){
        return new ArrayList<>();
    }

    /**
     * Checks if a movie has already been used in the game.
     *
     * @param movie the movie to check
     * @return true if the movie has been used, false otherwise
     */
    public boolean isMovieUsed(Movie movie){
        return false;
    }

    /**
     * Switches the turn to the next player.
     */
    public void switchPlayer(){

    }
}
