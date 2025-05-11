package controller;

import java.util.*;
import java.util.concurrent.*;
import model.Movie;
import model.MovieIndex;
import model.Player;
import strategy.ILinkStrategy;
import strategy.IWinCondition;
import view.GameView;

/**
 * Controls the main gameplay loop and manages game state transitions for the
 * Movie Name Game.
 */
public class GameController {

    private MovieIndex movieIndex;
    private ILinkStrategy linkStrategy;
    private IWinCondition winCondition;
    private Player currentPlayer;
    private Player otherPlayer;
    private Set<String> usedMovies;
    private GameView view;
    private Scanner scanner;

    /**
     * Constructs a GameController to manage the game with the provided index,
     * strategy, and players.
     *
     * @param movieIndex the movie index for lookup operations
     * @param linkStrategy the strategy for determining valid links
     * @param winCondition the win condition to check for victory
     * @param p1 the first player
     * @param p2 the second player
     */
    public GameController(
            MovieIndex movieIndex,
            ILinkStrategy linkStrategy,
            IWinCondition winCondition,
            Player p1,
            Player p2
    ) {
        this.movieIndex = movieIndex;
        this.linkStrategy = linkStrategy;
        this.winCondition = winCondition;
        this.currentPlayer = p1;
        this.otherPlayer = p2;
        this.usedMovies = new HashSet<>();
        this.view = new GameView();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Runs the main game loop, alternating turns between players until a win or
     * loss occurs.
     */
    public void runGame() {
        while (true) {
            view.renderGameState("Current player: " + currentPlayer.getPlayerName());
            view.renderInputPrompt();
            String input = getTimedInput(30);

            if (input == null) {
                view.displayError("Time out! Turn forfeited.");
                switchTurn();
                continue;
            }

            Movie move = movieIndex.findMovieByTitle(input.trim());
            if (move == null) {
                view.displayError("Movie not found.");
                continue;
            }

            if (usedMovies.contains(move.getTitle())) {
                view.displayError("Movie already used.");
                continue;
            }

            List<Movie> played = currentPlayer.getPlayedMovies();
            if (!played.isEmpty()) {
                Movie last = played.get(played.size() - 1);
                if (!linkStrategy.isValidLink(last, move)) {
                    view.displayError("Invalid link: " + linkStrategy.getReason(last, move));
                    continue;
                }
            }

            currentPlayer.addPlayedMovie(move);
            usedMovies.add(move.getTitle());

            if (winCondition.checkWin(currentPlayer)) {
                view.renderGameState("Player " + currentPlayer.getPlayerName() + " wins!");
                break;
            }

            switchTurn();
        }
    }

    /**
     * Processes a player's move by validating the submitted movie title.
     *
     * @param movieTitle the title of the movie submitted by the player
     */
    public void processMove(String movieTitle) {
        Movie move = movieIndex.findMovieByTitle(movieTitle.trim());
        if (move == null || usedMovies.contains(move.getTitle())) {
            return;
        }
        List<Movie> played = currentPlayer.getPlayedMovies();
        if (!played.isEmpty()) {
            Movie last = played.get(played.size() - 1);
            if (!linkStrategy.isValidLink(last, move)) {
                return;
            }
        }
        currentPlayer.addPlayedMovie(move);
        usedMovies.add(move.getTitle());
    }

    /**
     * Checks whether the game has ended.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return winCondition.checkWin(currentPlayer);
    }

    /**
     * Switches the turn to the other player.
     */
    public void switchTurn() {
        Player temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
    }

    /**
     * Prompts the user for input with a time limit. Waits for the specified
     * number of seconds for user input from the console. If the user does not
     * provide input within the time limit, returns null.
     *
     * @param seconds the time in seconds to wait for user input
     * @return the user's input as a String, or null if time runs out or an
     * error occurs
     */
    private String getTimedInput(int seconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(scanner::nextLine);
        try {
            return future.get(seconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            executor.shutdownNow();
        }
    }
}