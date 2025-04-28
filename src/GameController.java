import java.util.List;

public class GameController {
    private GameState gameState;
    private MovieDatabase movieDB;
    private GameView view;

    public GameController(GameState gameState, MovieDatabase movieDB, GameView view) {
        this.gameState = gameState;
        this.movieDB = movieDB;
        this.view = view;
    }

    public void startGame() {
        // Initialize game state, start timer, etc.
    }

    public void processInput(String input) {
        Movie movie = movieDB.findMovie(input);
        if (movie == null) {
            view.displayError("Movie not found!");
            return;
        }
        boolean valid = gameState.makeMove(movie);
        if (!valid) {
            view.displayError("Invalid connection! The movie must be connected to the previous one by actor or director.");
            return;
        }
        // ... update view, check win condition, etc.
    }

    public void endTurn() {
        // Switch player, update round, etc.
    }

    public void checkTimeLimit() {
        // Check if timer expired
    }
}
