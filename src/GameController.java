import java.util.*;

public class GameController {
    // === Attributes ===
    private Queue<Player> players;
    private Set<String> usedMovies;
    private Map<String, Integer> connectionTypes;
    private MovieDatabase movieDatabase;
    private Movie currentMovie;
    private int timeLimit; // Time limit in seconds

    // === Constructor ===
    public GameController(Queue<Player> players, MovieDatabase movieDatabase, Movie startMovie, int timeLimit) {
        this.players = players;
        this.usedMovies = new HashSet<>();
        this.connectionTypes = new HashMap<>();
        this.movieDatabase = movieDatabase;
        this.currentMovie = startMovie;
        this.timeLimit = timeLimit;
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
        startTimer();
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
     * Starts a timer for the current player's turn.
     */
    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Time's up!");
                switchTurn(); // Automatically switch turn when time is up
            }
        }, timeLimit * 1000);
    }

    // Additional methods for game logic can be added here
} 