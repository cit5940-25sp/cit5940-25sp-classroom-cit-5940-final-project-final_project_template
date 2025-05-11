import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;
import java.util.*;

import static com.googlecode.lanterna.input.KeyType.Enter;

/**
 * Controls the main logic of the movie-connection game, handling turns, player
 * input,
 * move validation, and win condition checking.
 * <p>
 * MVC Role: Controller. Responsible for coordinating players, the model
 * (MovieIndex), and the view (GameView),
 * implementing the main game loop, input handling, turn progression, and
 * win/loss determination.
 * </p>
 *
 * @author Vera Zhang
 * @author Jianing Yin
 */
public class GameController {

    /** Player 1 */
    private final Player player1;
    /** Player 2 */
    private final Player player2;
    /** The player whose turn it is */
    private Player currentPlayer;
    /** The current movie being connected */
    private Movie currentMovie;
    /** Movie index, responsible for lookup and connection logic */
    private final MovieIndex index;
    /** Current round number */
    private int round;

    /**
     * Records the usage count for each connection reason (to prevent infinite
     * loops)
     */
    private final Map<String, Integer> connectionUsageMap;
    /** Set of movies that have already been used */
    private final Set<Movie> usedMovies;
    /** Game history (each step's movie and connection reason) */
    private final Deque<HistoryEntry> history;
    /** View object responsible for UI display */
    private final GameView view;
    /** Time limit for each turn (seconds) */
    private static final int TIME_LIMIT_SECONDS = 30;
    /** Console input scanner */
    private final Scanner inputScanner;
    /** Whether the game has ended */
    private boolean gameEnded = false;
    /** Message for the last invalid input */
    private String lastInvalidMessage = "";
    /** Movie title trie for autocomplete */
    private final MovieTrie movieTrie;

    /**
     * Constructor, initializes the controller.
     *
     * @param p1         Player 1
     * @param p2         Player 2
     * @param movieIndex Movie index (model)
     * @param gameView   View object
     * @throws IllegalArgumentException if a player does not have a win condition
     */
    public GameController(final Player p1, final Player p2, final MovieIndex movieIndex,
            final GameView gameView) {
        this.player1 = p1;
        this.player2 = p2;
        this.index = movieIndex;
        this.view = gameView;

        if (p1.getWinCondition() == null || p2.getWinCondition() == null) {
            throw new IllegalArgumentException("Players must have a WinCondition set.");
        }

        this.inputScanner = new Scanner(System.in);
        this.movieTrie = new MovieTrie();
        movieTrie.buildTrie();

        for (Movie movie : index.getAllMovies()) {
            movieTrie.insert(movieTrie.getNormalizedString(movie.getTitle()), movie);
        }

        this.view.setMovieTrie(movieTrie);
        this.connectionUsageMap = new HashMap<>();
        this.usedMovies = new HashSet<>();
        this.history = new ArrayDeque<>();
    }

    /**
     * Closes the input stream and cleans up resources.
     */
    public void cleanup() {
        if (inputScanner != null) {
            inputScanner.close();
        }
    }

    /**
     * Starts a new game, initializes all relevant data, and enters the main loop.
     * <p>
     * Does nothing if the game has already ended.
     * </p>
     */
    public void startGame() {
        if (gameEnded)
            return;

        this.round = 1;
        this.usedMovies.clear();
        this.history.clear();
        this.connectionUsageMap.clear();
        this.currentMovie = index.getRandomMovie();

        if (this.currentMovie == null) {
            gameEnded = true;
            return;
        }

        this.usedMovies.add(this.currentMovie);
        this.currentPlayer = player1;
        runGameLoop();
        cleanup();
    }

    /**
     * The main game loop, runs until the game ends.
     */
    private void runGameLoop() {
        while (!gameEnded) {
            processTurn();
        }
    }

    /**
     * Handles a single player's turn, including input, validation, and win/loss
     * checking.
     * <p>
     * Handles timeout, invalid input, win condition, etc.
     * </p>
     */
    public void processTurn() {
        if (gameEnded)
            return;

        String input = null;
        StringBuilder inputBuilder = new StringBuilder();
        lastInvalidMessage = "";

        List<String> connectedTitles = index.getConnectedMovieTitlesWithReason(currentMovie)
                .stream()
                .limit(10)
                .map(str -> str.length() > 46 ? str.substring(0, 46) + "…" : str)
                .toList();
        view.setConnectedMovieTitles(connectedTitles);

        view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
        view.startTimer(
                () -> {
                    lastInvalidMessage = "Time out or empty input";
                    view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    endGame(getOpponent(currentPlayer));
                },
                () -> {
                    view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
                    view.displayPrompt("Enter connected movie title: ");
                    view.displayInputLine(inputBuilder.toString());
                });

        view.displayPrompt("Enter connected movie title: ");
        view.displayInputLine("");

        long startTime = System.currentTimeMillis();

        try {
            while (System.currentTimeMillis() - startTime < TIME_LIMIT_SECONDS * 1000L) {
                KeyStroke key = view.readKeyStrokeNonBlocking();
                if (key != null) {
                    switch (key.getKeyType()) {
                        case Enter -> input = inputBuilder.toString();
                        case Backspace -> {
                            if (inputBuilder.length() > 0) {
                                inputBuilder.deleteCharAt(inputBuilder.length() - 1);
                            }
                        }
                        case Character -> inputBuilder.append(key.getCharacter());
                        default -> {
                        }
                    }
                    view.updateSuggestions(inputBuilder.toString());
                    view.displayInputLine(inputBuilder.toString());
                }

                if (input != null)
                    break;
                Thread.sleep(50);
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        view.stopTimer();

        if (input == null || input.trim().isEmpty()) {
            lastInvalidMessage = "Time out or empty input";
            view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            endGame(getOpponent(currentPlayer));
            return;
        }

        Movie nextMovie = index.getMovieByTitle(input.trim());
        if (nextMovie == null) {
            lastInvalidMessage = "Movie not found";
            view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            endGame(getOpponent(currentPlayer));
            return;
        }

        if (usedMovies.contains(nextMovie)) {
            lastInvalidMessage = "Movie already used";
            view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            endGame(getOpponent(currentPlayer));
            return;
        }

        String reason = findConnectionReason(currentMovie, nextMovie);
        if (reason == null) {
            lastInvalidMessage = "No valid connection";
            view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            endGame(getOpponent(currentPlayer));
            return;
        }

        recordMove(nextMovie, reason);
        view.displayPrompt("✅ Valid connection: " + reason);

        if (currentPlayer.getWinCondition().checkWin(currentPlayer)) {
            endGame(currentPlayer);
            return;
        }

        currentMovie = nextMovie;
        currentPlayer = getOpponent(currentPlayer);
        round++;
    }

    /**
     * Gets the opponent of the current player.
     *
     * @param player The current player
     * @return The other player
     */
    private Player getOpponent(final Player player) {
        return (player == player1) ? player2 : player1;
    }

    /**
     * Determines the reason for a valid connection between two movies (e.g., actor,
     * director, etc.).
     *
     * @param from Source movie
     * @param to   Target movie
     * @return Connection reason string, or null if no valid connection exists
     */
    private String findConnectionReason(final Movie from, final Movie to) {
        String reason;
        reason = findSpecificReason("actor", from.getActors(), to.getActors());
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
        return findSpecificReason("composer", from.getComposers(), to.getComposers());
    }

    /**
     * Checks whether two sets of movie attributes (e.g., actors) intersect and
     * returns the connection reason.
     *
     * @param category Attribute category (e.g., actor, director, etc.)
     * @param c1       First set of attributes
     * @param c2       Second set of attributes
     * @return If there is an intersection and not overused, returns the connection
     *         reason; otherwise null
     */
    private String findSpecificReason(final String category, final Collection<String> c1, final Collection<String> c2) {
        if (c1 == null || c2 == null || c1.isEmpty() || c2.isEmpty())
            return null;
        for (String item : c2) {
            if (c1.contains(item)) {
                String fullReason = category + ": " + item;
                if (connectionUsageMap.getOrDefault(fullReason, 0) < 3) {
                    return fullReason;
                }
            }
        }
        return null;
    }

    /**
     * Records a valid movie connection, adds it to history, marks it as used, and
     * updates player data.
     *
     * @param movie  The newly connected movie
     * @param reason The connection reason
     */
    public void recordMove(final Movie movie, final String reason) {
        if (gameEnded)
            return;
        usedMovies.add(movie);
        HistoryEntry entry = new HistoryEntry(movie, reason);
        history.addLast(entry);
        view.addToHistory(entry);
        connectionUsageMap.put(reason, connectionUsageMap.getOrDefault(reason, 0) + 1);
        currentPlayer.addMovie(movie);
    }

    /**
     * Ends the game and displays the final result.
     *
     * @param winner The winning player
     */
    private void endGame(final Player winner) {
        if (gameEnded)
            return;
        gameEnded = true;
        view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
        view.displayWin(winner, history);
    }
}
