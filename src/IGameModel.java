import java.util.List;
import java.util.Map;

public interface IGameModel {

    /*
    initializes the player objects with names and win conditions
     */
    public void initializePlayers();

    /*
    loads all movie in the model via the movie index
     */
    public Map<Integer, IMovie> loadMovieData(String moviesCsvFile, String creditsCsvFile);

    /*
    returns list of players in the game
     */
    public List<IPlayer> getPlayers();

    /*
    returns the current player whose turn it is
     */
    public IPlayer getCurrentPlayer();

    /*
    switches to next player
     */
    public void switchToNextPlayer();

    /*
    returns the current movie in play
     */
    public IMovie getCurrentMovie();

    /*
    checks if given input is valid move
    (is valid move title && connects to current movie && has not been used)
     */
    public boolean isValidMove(String movieTitle);

    /*
    makes the move with the given movie title and updates the gamestate
     */
    public void makeMove(String movieTitle);

    /*
    returns true if given player has met their win condition
     */
    public boolean checkWinCondition(IPlayer player);

    /*
    returns true if the game has ended
    (if player wins or no valid moves remain or player times out)
     */
    public boolean isGameOver();

    /*
    returns the winner of the game or null if no one has won yet
     */
    public IPlayer getWinner();

    /*
    returns a list of last 5 played movies
     */
    public List<IMovie> getRecentHistory();

    /*
    returns the number of rounds played so far
     */
    public int getRoundCount();
}
