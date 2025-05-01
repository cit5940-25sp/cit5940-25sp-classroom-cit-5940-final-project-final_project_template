import java.util.List;

/**
 * Controls the overall game flow, coordinating interactions between
 * the model (GameState, MovieDatabase), and the view (GameView).
 */
public class GameController {
    private GameState gameState;
    private GameView view;
    private MovieDatabase movieDb;

    /**
     * Constructs a GameController with the specified API key.
     *
     * @param apiKey the API key for the TMDB service
     */
    public GameController(String apiKey) {
        this.movieDb = new MovieDatabase(apiKey);
        this.view = new GameView();
    }

    GameController(MovieDatabase db, GameView view) {
        this.movieDb = db;
        this.view = view;
    }

    GameState getGameState() {
        return gameState;
    }

    /**
     * Starts a new game session with the specified players and win condition.
     *
     * @param p1   name of Player 1
     * @param p2   name of Player 2
     * @param cond the win condition strategy
     */
    public void startGame(String p1, String p2, WinCondition cond){
        // Create players
        Player player1 = new Player(p1);
        Player player2 = new Player(p2);
        
        // Find a starting movie (using "The Godfather" as a default starting movie)
        Movie startingMovie = movieDb.findByTitle("The Godfather");
        if (startingMovie == null) {
            // Fallback if the first choice is not found
            startingMovie = movieDb.findByTitle("Star Wars");
        }
        
        // Make sure we have a valid starting movie
        if (startingMovie == null) {
            view.displayInfo("Could not find a starting movie. Please check your database connection.");
            return;
        }
        
        // Initialize game state with players, win condition, and starting movie
        gameState = new GameState(player1, player2, cond, startingMovie);
        
        // Display initial game state
        view.displayInfo("Game started with " + p1 + " and " + p2);
        view.displayInfo("Win condition: " + cond.description());
        view.displayInfo("Starting movie: " + startingMovie.getTitle() + " (" + startingMovie.getYear() + ")");
        view.render(gameState);
    }

    /**
     * Processes a player's turn using the guessed movie title.
     *
     * @param movieTitle the title of the movie guessed
     */
    public void processTurn(String movieTitle){
        // check if time is up
        if (!gameState.getTimer().isRunning()) {
            view.displayInfo("Time's up! Turn skipped.");
            gameState.switchPlayer();
            view.render(gameState);
            return;
        }

        // check if the input is valid
        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            view.displayInfo("Movie title cannot be empty.");
            return;
        }

        Player currentPlayer = gameState.getCurrentPlayer();
        Movie guessedMovie = movieDb.findByTitle(movieTitle);

        if (guessedMovie == null) {
            view.displayInfo("Movie not found: " + movieTitle);
            return;
        }

        if (gameState.isMovieUsed(guessedMovie)) {
            view.displayInfo("Movie already used: " + movieTitle);
            return;
        }

        Movie lastMovie = gameState.getCurrentMovie();

        Connection validConn = findValidConnection(lastMovie, guessedMovie);
        if (validConn == null || !gameState.canUseConnection(validConn.getPersonName())) {
            view.displayInfo("No valid connection found between " + lastMovie.getTitle() +
                " and " + guessedMovie.getTitle());
            return;
        }

        // successfully proccess
        gameState.incrementConnectionUsage(validConn.getPersonName());
        gameState.addMovieToHistory(guessedMovie);
        currentPlayer.addGuessedMovie(guessedMovie);
        view.displayInfo(currentPlayer.getName() + " successfully connected movies via: " +
            validConn.getPersonName() + " (" + validConn.getType() + ")");

        if (gameState.hasCurrentPlayerWon()) {
            view.displayInfo(currentPlayer.getName() + " has won! (" +
                gameState.getWinCondition().description() + ")");
            return;
        }

        gameState.switchPlayer();
        view.render(gameState);
    }


    /**
     * Checks if two movies are connected by a valid shared attribute.
     *
     * @param from the previously guessed movie
     * @param to   the newly guessed movie
     * @return true if the connection is valid; false otherwise
     */
    public boolean isValidConnection(Movie from, Movie to){
        // Get all possible connections between the movies
        List<Connection> connections = from.findConnections(to);
        
        // Check if any connection is valid (not used more than 3 times)
        for (Connection conn : connections) {
            if (gameState.canUseConnection(conn.getPersonName())) {
                return true;
            }
        }
        
        // No valid connections found
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
        // Get all possible connections between the movies
        List<Connection> connections = from.findConnections(to);
        
        // Find the first valid connection (not used more than 3 times)
        for (Connection conn : connections) {
            if (gameState.canUseConnection(conn.getPersonName())) {
                return conn;
            }
        }
        
        // No valid connections found
        return null;
    }
}
