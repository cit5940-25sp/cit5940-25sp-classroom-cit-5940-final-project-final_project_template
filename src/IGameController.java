public interface IGameController {

    /*
    initializes the game, sets up players, loads movie data, pick randomized movie to start with
     */
    public void initializeGame();

    /*
    starts the main game loop and handles turn-based logic
     */
    public void startGame();

    /*
    handles input from current player
    parameter: input typed by player
     */
    public void handlePlayerInput(String input);

    /*
    advances to the next player after valid move or time out
     */
    public void nextTurn();

    /*
    checks if the game is over (win condition met or player timed out)
     */
    public boolean isGameOver();

    /*
    ends the game and triggers gameView to display final result
     */
    public void endGame();

    /*
    handles case where timer runs out
     */
    public void handleTimeout();
}
