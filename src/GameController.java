import java.util.ArrayList;
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
    GameView getView() {
        return view;
    }
    public MovieDatabase getMovieDatabase() {
        return movieDb;
    }
    void setGameState(GameState state) {
        this.gameState = state;
    }


    /**
     * Starts a new game session with the specified players and win condition.
     *
     * @param p1   name of Player 1
     * @param p2   name of Player 2
     * @param cond the win condition strategy
     */
    public Movie startGame(String p1, String p2, WinCondition cond){
        // Create players
        Player player1 = new Player(p1);
        Player player2 = new Player(p2);

        Movie startingMovie = movieDb.getRandomMovie();
        
        // Make sure we have a valid starting movie
        if (startingMovie == null) {
            view.displayInfo("Could not find a starting movie. Please check your database connection.");
            return null;
        }
        
        // Initialize game state with players, win condition, and starting movie
        gameState = new GameState(player1, player2, cond, startingMovie);
        
        // Display initial game state
        return startingMovie;
    }

    /**
     * Processes a player's turn using the guessed movie title.
     *
     * @param movieTitle the title of the movie guessed
     */
    public TurnResult processTurn(String movieTitle) {

        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            return new TurnResult(false, "⚠️ Movie title cannot be empty.");
        }

        Player currentPlayer = gameState.getCurrentPlayer();
        Movie guessedMovie = movieDb.findByTitle(movieTitle);

        if (guessedMovie != null) {
            System.out.println("Guessed: " + guessedMovie.toString());
        }

        if (guessedMovie == null) {
            return new TurnResult(false, "❌ Movie not found: " + movieTitle);
        }

        if (gameState.isMovieUsed(guessedMovie)) {
            return new TurnResult(false, "⚠️ Movie already used: " + movieTitle);
        }

        Movie lastMovie = gameState.getCurrentMovie();
        List<Connection> connections = lastMovie.findConnections(guessedMovie);

//        if (validConn == null || !gameState.canUseConnection(validConn.getPersonName())) {
//            return new TurnResult(false,
//                    "❌ No valid connection found between " + lastMovie.getTitle() + " and " + guessedMovie.getTitle());
//        }
        if (connections.isEmpty()) {
            return new TurnResult(false,
                    "❌ No valid connection found between " + lastMovie.getTitle() + " and " + guessedMovie.getTitle());
        }

        if (gameState.canUseConnection(connections).isEmpty()) {
            String connectionStr = "";
            for (Connection con: connections) {
                connectionStr += (con.getPersonName() + " ");
            }
            return new TurnResult(false,
                    "⚠️ Connection with " + connectionStr + " has already been used 3 times.");
        }

        // ✅ Valid move
        gameState.addMovieToHistory(guessedMovie);
        currentPlayer.addGuessedMovie(guessedMovie);

        String validConnStr = "";
        for (Connection con: connections) {
            validConnStr += (con.getPersonName() + " (" + con.getType() + ")");
        }

        String msg = "✅ " + currentPlayer.getName() + " connected via " +
                validConnStr;

        if (gameState.hasCurrentPlayerWon()) {
            return new TurnResult(true, "🏆 " + currentPlayer.getName() + " has won the game!");
        }

        gameState.switchPlayer();
        return new TurnResult(true, msg);
    }

    public List<String> getAutocompleteSuggestions(String input) {
        List<String> results = new ArrayList<>();
        for (String title : movieDb.getAllTitles()) {
            if (title.toLowerCase().startsWith(input.toLowerCase())) {
                results.add(title);
            }
        }
        return results.stream().limit(5).toList(); // 最多顯示5個
    }

}
