import java.util.*;

public class GameController {
    // === Attributes ===
    private Queue<Player> players;
    private Set<String> usedMovies;
    private Map<String, Integer> connectionTypes;
    private MovieDatabase movieDatabase;
    private Movie currentMovie;

    // === Constructor ===
    public GameController(Queue<Player> players, MovieDatabase movieDatabase) {
        this.players = players;
        this.usedMovies = new HashSet<>();
        this.connectionTypes = new HashMap<>();
        this.movieDatabase = movieDatabase;
    }

    // === Methods ===

    /**
     * Checks if the move is valid.
     */
    public boolean isValidMove(String movieTitle) {
        return movieDatabase.contains(movieTitle) && !usedMovies.contains(movieTitle);
    }

    /**
     * Switches the turn to the next player.
     */
    public void switchTurn() {
        Player current = players.poll();
        if (current != null) {
            players.offer(current);
        }
        // Start timer logic here if needed
    }

    /**
     * Validates if the current player's move is connected to the previous movie.
     */
    public boolean validateConnection(String movieTitle) {
        Movie newMovie = movieDatabase.getMovie(movieTitle);
        if (newMovie == null) return false;

        ConnectionValidator validator = new ConnectionValidator();
        boolean isValid = validator.isValidConnection(currentMovie, newMovie);

        if (isValid) {
            usedMovies.add(movieTitle);
            currentMovie = newMovie;
        }

        return isValid;
    }

    /**
     * Checks if the current player has met the win condition.
     */
    public boolean checkWinCondition(Player player) {
        return player.hasMetWinCondition();
    }

    /**
     * Applies a time limit for the current player's turn.
     */
    public void applyTimeLimit() {
        // Implement timer logic here
    }

    // Additional methods for game logic can be added here
} 