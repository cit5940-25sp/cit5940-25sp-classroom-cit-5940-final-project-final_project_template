import java.util.List;

public interface IGameView {

    /*
    displays welcome message and game introduction
     */
    public void showWelcomeMessage();

    /*
    displays the win condition for each player
     */
    public void showWinConditions(List<IPlayer> players);

    /*
    displays the start of the game and the current player (as in whose turn it is)
     */
    public void showGameStart(IPlayer currentPlayer);

    /*
    prompts the current player to enter a movie title
     */
    public void promptForMovie(IPlayer currentPlayer);

    /*
    displays a success message for a valid movie move
     */
    public void showMoveSuccess(String movieTitle, IPlayer currentPlayer);

    /*
    displays a message when input movie is invalid
     */
    public void showInvalidMove(String movieTitle);

    /*
    displays message when current player's timer runs out
     */
    public void showTimeout(IPlayer currentPlayer);

    /*
    displays the winner of the game
     */
    public void showWinner(IPlayer winner);

    /*
    displays a draw or timeout result when no player wins (no other options and if player 1 does not get first
    movie)
    don't implement for now
     */
    public void showDrawOrTimeout();

    /*
    displays the start of the next player's turn
     */
    public void showNextTurn(IPlayer currentPlayer);

    /*
    displays the last 5 movies played
     */
    public void showMovieHistory(List<IMovie> recentMovies);

    /*
    displays the current progress and status of each player
     */
    public void showPlayerStats(List<IPlayer> players, int roundCount);
}
