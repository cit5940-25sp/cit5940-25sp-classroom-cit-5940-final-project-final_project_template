import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;
import java.util.*;

import static com.googlecode.lanterna.input.KeyType.Enter;

public class GameController {

    public Player player1;
    public Player player2;
    public Player currentPlayer;
    public Movie currentMovie;
    public MovieIndex index;
    public int round;
    private Map<String, Integer> connectionUsageMap;
    private Set<Movie> usedMovies;
    private Deque<HistoryEntry> history;
    private GameView view;
    private static final int TIME_LIMIT_SECONDS = 30;
    private Scanner inputScanner;
    private boolean gameEnded = false;
    private String lastInvalidMessage = "";
    private MovieTrie movieTrie;

    public GameController(Player p1, Player p2, MovieIndex movieIndex, GameView gameView) {
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
    }

    public void cleanup() {
        if (inputScanner != null) {
            inputScanner.close();
        }
    }

    public void startGame() {
        if (gameEnded) return;
        this.round = 1;
        this.usedMovies = new HashSet<>();
        this.history = new ArrayDeque<>();
        this.connectionUsageMap = new HashMap<>();

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

    private void runGameLoop() {
        while (!gameEnded) {
            processTurn();
        }
    }

    public void processTurn() {
        if (gameEnded) return;
        String input = null;
        StringBuilder inputBuilder = new StringBuilder();
        lastInvalidMessage = "";
        view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
        view.startTimer(
            () -> {
                lastInvalidMessage = "Time out or empty input";
                view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                endGame(getOpponent(currentPlayer));
            },
            () -> {
                view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
                view.displayPrompt("Enter connected movie title: ");
                view.displayInputLine(inputBuilder.toString());
            }
        );

        view.displayPrompt("Enter connected movie title: ");
        view.displayInputLine("");

        long startTime = System.currentTimeMillis();

        try {
            while (System.currentTimeMillis() - startTime < TIME_LIMIT_SECONDS * 1000L) {
                KeyStroke key = view.readKeyStrokeNonBlocking();
                if (key != null) {
                    switch (key.getKeyType()) {
                        case Enter:
                            input = inputBuilder.toString();
                            break;
                        case Backspace:
                            if (inputBuilder.length() > 0) {
                                inputBuilder.deleteCharAt(inputBuilder.length() - 1);
                            }
                            break;
                        case Character:
                            inputBuilder.append(key.getCharacter());
                            break;
                        default:
                            break;
                    }
                    view.updateSuggestions(inputBuilder.toString());
                    view.displayInputLine(inputBuilder.toString());
                }

                if (input != null) break;
                Thread.sleep(50);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        view.stopTimer();

        if (input == null || input.trim().isEmpty()) {
            lastInvalidMessage = "Time out or empty input";
            view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
            endGame(getOpponent(currentPlayer));
            return;
        }

        Movie nextMovie = index.getMovieByTitle(input.trim());

        if (nextMovie == null) {
            lastInvalidMessage = "Movie not found";
            view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
            endGame(getOpponent(currentPlayer));
            return;
        }

        if (usedMovies.contains(nextMovie)) {
            lastInvalidMessage = "Movie already used";
            view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
            endGame(getOpponent(currentPlayer));
            return;
        }

        String reason = findConnectionReason(currentMovie, nextMovie);
        if (reason == null) {
            lastInvalidMessage = "No valid connection";
            view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
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

    private Player getOpponent(Player player) {
        return (player == player1) ? player2 : player1;
    }

    private String findConnectionReason(Movie from, Movie to) {
        String reason;
        reason = findSpecificReason("actor", from.getActors(), to.getActors());
        if (reason != null) return reason;
        reason = findSpecificReason("director", from.getDirectors(), to.getDirectors());
        if (reason != null) return reason;
        reason = findSpecificReason("writer", from.getWriters(), to.getWriters());
        if (reason != null) return reason;
        reason = findSpecificReason("cinematographer", from.getCinematographers(), to.getCinematographers());
        if (reason != null) return reason;
        reason = findSpecificReason("composer", from.getComposers(), to.getComposers());
        return reason;
    }

    private String findSpecificReason(String category, Collection<String> c1, Collection<String> c2) {
        if (c1 == null || c2 == null || c1.isEmpty() || c2.isEmpty()) return null;
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

    public void recordMove(Movie movie, String reason) {
        if (gameEnded) return;
        usedMovies.add(movie);
        HistoryEntry entry = new HistoryEntry(movie, reason);
        history.addLast(entry);
        view.addToHistory(entry);
        connectionUsageMap.put(reason, connectionUsageMap.getOrDefault(reason, 0) + 1);
        currentPlayer.addMovie(movie);
    }

    private void endGame(Player winner) {
        if (gameEnded) return;
        gameEnded = true;
        view.displayGameState(player1, player2, currentMovie, round, lastInvalidMessage);
        view.displayWin(winner, history);
    }
}
