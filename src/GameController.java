import java.io.IOException;
import java.util.*;

public class GameController {

    public Player player1;
    public Player player2;
    public Player currentPlayer;
    public Movie currentMovie;
    public MovieIndex index; // Assuming MovieIndex is provided/initialized
    public int round;
    private Map<String, Integer> connectionUsageMap;
    private Set<Movie> usedMovies;
    private Deque<HistoryEntry> history;
    private GameView view; // For displaying game state
    private static final int TIME_LIMIT_SECONDS = 30;
    private Scanner inputScanner; // For reading user input
    private boolean gameEnded = false; // Flag to control game loop

    // Constructor to initialize the game
    public GameController(Player p1, Player p2, MovieIndex movieIndex, GameView gameView) {
        this.player1 = p1;
        this.player2 = p2;
        this.index = movieIndex;
        this.view = gameView;
        // Ensure players have win conditions set before passing them here
        if (p1.getWinCondition() == null || p2.getWinCondition() == null) {
            throw new IllegalArgumentException("Players must have a WinCondition set.");
        }
        // Use non-blocking scanner if possible, or manage input differently for timeout
        this.inputScanner = new Scanner(System.in);
    }

    // Closes resources like the scanner
    public void cleanup() {
        if (inputScanner != null) {
            inputScanner.close();
        }
        System.out.println("Game resources cleaned up.");
    }

    public void startGame() {
        if (gameEnded) {
            System.out.println("Game has already ended. Please create a new GameController to play again.");
            return;
        }
        this.round = 1;
        this.usedMovies = new HashSet<>();
        this.history = new ArrayDeque<>();
        this.connectionUsageMap = new HashMap<>();

        // Select starting movie using getRandomMovie()
        this.currentMovie = index.getRandomMovie();

        if (this.currentMovie == null) {
            System.err.println("Error: Failed to select a starting movie from the index.");
            gameEnded = true;
            return;
        }

        this.usedMovies.add(this.currentMovie);

        this.currentPlayer = player1; // Player 1 starts

        System.out.println("--- Game Start ---");
        System.out.println("Starting movie: " + this.currentMovie.getTitle());

        // Start the game loop (now iterative for robustness)
        runGameLoop();

        // Game loop finished, perform cleanup
        cleanup();
    }

    // Iterative game loop
    private void runGameLoop() {
        while (!gameEnded) {
            processTurn();
        }
    }

    // Process a single turn
    public void processTurn() { // Made public for potential external control/testing if needed, was private
        if (gameEnded)
            return; // Exit if game ended during opponent's turn or async event

        // Display current state using GameView
        view.displayGameState(player1, player2, currentMovie, round);

        System.out.println(" --- Round " + round + " ---");
        System.out.println(currentPlayer.getName() + "'s turn. Connect to: '" + currentMovie.getTitle() + "'");
        System.out.println("You have " + TIME_LIMIT_SECONDS + " seconds.");

        String input = getInputWithSimplifiedTimeout(TIME_LIMIT_SECONDS);

        if (input == null) {
            System.out.println(" Time's up! " + currentPlayer.getName() + " loses.");
            endGame(getOpponent(currentPlayer));
            return; // Exit turn processing
        }

        // Find the movie entered by the player
        Movie nextMovie = index.getMovieByTitle(input.trim());

        if (nextMovie == null) {
            System.out.println(" Movie '" + input.trim() + "' not found in the database."); // Fixed newline placement
            view.displayInvalidMove();
            System.out.println(currentPlayer.getName() + " loses due to invalid input.");
            endGame(getOpponent(currentPlayer));
            return; // Exit turn processing
        }

        if (usedMovies.contains(nextMovie)) {
            System.out.println(" Movie '" + nextMovie.getTitle() + "' has already been used.");
            view.displayInvalidMove();
            System.out.println(currentPlayer.getName() + " loses by repeating a movie.");
            endGame(getOpponent(currentPlayer));
            return; // Exit turn processing
        }

        // Simplified connection check: Find a usable reason directly
        String reason = findConnectionReason(currentMovie, nextMovie);

        if (reason == null) {
            // Handles both "no connection" and "connection types used up"
            System.out.println(" No usable connection found between '" + currentMovie.getTitle() + "' and '"
                    + nextMovie.getTitle() + "'."); // Fixed newline placement
            view.displayInvalidMove();
            System.out.println(currentPlayer.getName() + " loses (failed connection or usage limit).");
            endGame(getOpponent(currentPlayer));
            return; // Exit turn processing
        }

        // --- Valid move ---
        System.out.println(" Valid connection: " + reason); // Fixed newline placement
        recordMove(nextMovie, reason);

        // Check win condition for the current player
        // checkWin(Player)
        if (currentPlayer.getWinCondition().checkWin(currentPlayer)) {
            System.out.println(" " + currentPlayer.getName() + " fulfills the win condition!");
            endGame(currentPlayer);
            return; // Exit turn processing (game ended)
        }

        // --- Prepare for the next turn ---
        currentMovie = nextMovie;
        currentPlayer = getOpponent(currentPlayer);
        round++;
        // The loop in runGameLoop() will continue to the next turn
    }

    // Helper to get the opponent
    private Player getOpponent(Player player) {
        return (player == player1) ? player2 : player1;
    }

    // Finds a valid connection reason (one that hasn't been used 3 times)
    // Returns the reason string or null if no *usable* connection found.
    private String findConnectionReason(Movie from, Movie to) {
        String reason = findSpecificReason("actor", from.getActors(), to.getActors());
        if (reason != null)
            return reason;

        reason = findSpecificReason("director", from.getDirectors(), to.getDirectors());
        if (reason != null)
            return reason;

        reason = findSpecificReason("writer", from.getWriters(), to.getWriters());
        if (reason != null)
            return reason;

        reason = findSpecificReason("cinematographer", from.getCinematographers(), to.getCinematographers());
        if (reason != null)
            return reason;

        reason = findSpecificReason("composer", from.getComposers(), to.getComposers());
        if (reason != null)
            return reason;

        return null; // No usable connection found
    }

    // Helper for findConnectionReason to check a specific category
    private String findSpecificReason(String category, Collection<String> c1, Collection<String> c2) {
        if (c1 == null || c2 == null || c1.isEmpty() || c2.isEmpty())
            return null;

        Set<String> set1 = (c1 instanceof Set) ? (Set<String>) c1 : new HashSet<>(c1);
        Set<String> intersection = new HashSet<>();
        for (String item : c2) {
            if (set1.contains(item)) {
                intersection.add(item);
            }
        }

        if (!intersection.isEmpty()) {
            for (String item : intersection) {
                String fullReason = category + ": " + item;
                if (connectionUsageMap.getOrDefault(fullReason, 0) < 3) {
                    return fullReason; // Found a usable connection
                }
            }
        }
        return null; // No item in this category provides a usable connection
    }

    public String getInputWithSimplifiedTimeout(int timeoutSeconds) {
        if (gameEnded)
            return null;

        System.out.print("Enter connected movie title: ");
        long startTime = System.currentTimeMillis();
        long timeLimitMillis = timeoutSeconds * 1000L;

        try {
            while (System.currentTimeMillis() - startTime < timeLimitMillis) {
                if (System.in.available() > 0) { // Check if bytes are available to read
                    String line = inputScanner.nextLine();
                    if (line != null && !line.trim().isEmpty()) {
                        return line;
                    } else {
                        // Handle empty line entry if needed, maybe treat as invalid?
                        return null; // Or continue loop? For now, treat empty as null/timeout.
                    }
                }
                // Short sleep to prevent busy-waiting
                Thread.sleep(100); // Sleep for 100ms
            }
        } catch (IOException e) {
            System.err.println("IO Error during input check: " + e.getMessage());
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            System.err.println("Input polling interrupted.");
            return null;
        } catch (NoSuchElementException | IllegalStateException e) {
            System.err.println("Scanner error during input: " + e.getMessage());
            return null; // Error reading input
        }

        // If the loop finishes without returning, time is up.
        return null;
    }

    public void recordMove(Movie movie, String reason) {
        if (gameEnded)
            return;

        usedMovies.add(movie);
        history.addLast(new HistoryEntry(movie, reason));
        connectionUsageMap.put(reason, connectionUsageMap.getOrDefault(reason, 0) + 1);

        currentPlayer.addMovie(movie);

        // logging:
        System.out.println("Move recorded: " + currentPlayer.getName() + " played '"
        + movie.getTitle() + "' connected via " + reason);
        System.out.println("Connection '" + reason + "' used " +
        connectionUsageMap.get(reason) + " times.");
        System.out.println("Movies used: " + usedMovies.size());
    }

    // Helper method to end the game and announce winner
    private void endGame(Player winner) {
        if (gameEnded)
            return;
        gameEnded = true;
        System.out.println("--- Game Over ---"); // Fixed newline placement
        view.displayWin(winner);

        System.out.println("Final history:");
        for (HistoryEntry entry : history) {
            // Defensive check in case getMovie() or getTitle() returns null
            String title = (entry.getMovie() != null) ? entry.getMovie().getTitle() : "[Unknown Movie]";
            System.out.println(" - " + title + " (" + entry.getConnectionReason() + ")");
        }
    }
}
