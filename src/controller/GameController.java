package controller;

import model.Movie;
import model.MovieIndex;
import model.Player;
import strategy.*; // Assuming all strategy interfaces and classes are here

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manages the game state, player turns, move validation, and win condition checking
 * for the Movie Name Game. Interacts with the TUI (GameView) for user input and display updates.
 */
public class GameController {

    private final MovieIndex movieIndex;
    private ILinkStrategy currentLinkStrategy; // Strategy chosen for the current turn
    private IWinCondition currentWinCondition; // Win condition for the current game
    private Player currentPlayer;
    private Player otherPlayer;
    private Player winner; // Stores the winner when the game ends

    private boolean gameOver = false;
    private final List<Movie> gameHistory; // Global list of movies played in this game session
    private final Random random = new Random();

    /**
     * Constructs a GameController.
     *
     * @param movieIndex The index containing all movie data.
     * @param p1         Player 1.
     * @param p2         Player 2.
     */
    public GameController(MovieIndex movieIndex, Player p1, Player p2) {
        if (movieIndex == null || p1 == null || p2 == null) {
            throw new IllegalArgumentException("MovieIndex and Players cannot be null.");
        }
        this.movieIndex = movieIndex;
        this.gameHistory = new ArrayList<>();
        this.currentPlayer = p1;
        this.otherPlayer = p2;
        // Game initialization (random movie and win condition) is done via initializeNewGame().
    }

    /**
     * Initializes a new game session:
     * - Resets player states (played movies, strategy usage).
     * - Clears the global game history.
     * - Randomly selects a starting movie and adds it to the history.
     * - Randomly selects a win condition for this game.
     * - Resets game over state and winner.
     *
     * @return The initially selected movie to start the game, or null if initialization fails (e.g., no movies).
     */
    public Movie initializeNewGame() {
        this.gameOver = false;
        this.winner = null;
        this.gameHistory.clear();
        this.currentPlayer.resetForNewGame();
        this.otherPlayer.resetForNewGame();
        this.currentLinkStrategy = null; // Reset, player chooses each turn

        // 1. Select and add the initial random movie
        Movie initialMovie = selectRandomInitialMovie();
        if (initialMovie != null) {
            gameHistory.add(initialMovie);
        } else {
            System.err.println("CRITICAL: No movies available in MovieIndex to start the game.");
            this.gameOver = true; // Mark game as over because it cannot start
            return null;
        }

        // 2. Select a random win condition
        this.currentWinCondition = selectRandomWinCondition();
        if (this.currentWinCondition == null) {
            System.err.println("CRITICAL: Could not set a random win condition.");
            this.gameOver = true; // Mark game as over
            return null;
        }

        System.out.println("Game Initialized. Starting Movie: " + initialMovie.getTitle() + ". Win Condition: " + currentWinCondition.getDescription());
        return initialMovie; // Return the starting movie for display
    }

    /**
     * Selects a random movie from the index to start the game.
     *
     * @return A random Movie object, or null if the index is empty.
     */
    private Movie selectRandomInitialMovie() {
        Set<String> allTitles = movieIndex.getAllTitlesSorted();
        if (allTitles == null || allTitles.isEmpty()) {
            return null;
        }
        List<String> titlesList = new ArrayList<>(allTitles);
        String randomTitle = titlesList.get(random.nextInt(titlesList.size()));
        return movieIndex.findMovieByTitle(randomTitle);
    }

    /**
     * Selects a random win condition (either Genre or Year based) using available data.
     *
     * @return A randomly chosen IWinCondition instance, or null if no suitable condition can be created.
     */
    private IWinCondition selectRandomWinCondition() {
        Set<String> allTitles = movieIndex.getAllTitlesSorted();
        if (allTitles == null || allTitles.isEmpty()) return null; // Cannot determine conditions without movies

        // Collect all unique genres and valid years from the movie index
        Set<String> uniqueGenres = allTitles.stream()
                .map(movieIndex::findMovieByTitle)
                .filter(m -> m != null && m.getGenres() != null)
                .flatMap(m -> m.getGenres().stream())
                .filter(g -> g != null && !g.trim().isEmpty())
                .collect(Collectors.toSet());

        Set<Integer> uniqueYears = allTitles.stream()
                .map(movieIndex::findMovieByTitle)
                .filter(m -> m != null && m.getYear() > 0) // Assuming 0 or negative years are invalid
                .map(Movie::getYear)
                .collect(Collectors.toSet());

        boolean canUseGenre = !uniqueGenres.isEmpty();
        boolean canUseYear = !uniqueYears.isEmpty();

        if (!canUseGenre && !canUseYear) {
            return null; // No valid genres or years found in the data
        }

        // Randomly choose between Genre (0) and Year (1), if both are possible
        int choice = -1;
        if (canUseGenre && canUseYear) {
            choice = random.nextInt(2);
        } else if (canUseGenre) {
            choice = 0; // Only Genre is possible
        } else {
            choice = 1; // Only Year is possible
        }

        // Create the chosen win condition
        if (choice == 0) {
            List<String> genreList = new ArrayList<>(uniqueGenres);
            String randomGenre = genreList.get(random.nextInt(genreList.size()));
            return new GenreWinCondition(randomGenre);
        } else { // choice == 1
            List<Integer> yearList = new ArrayList<>(uniqueYears);
            int randomYear = yearList.get(random.nextInt(yearList.size()));
            return new YearWinCondition(randomYear);
        }
    }

    /**
     * Gets the movie index.
     * @return The MovieIndex instance.
     */
    public MovieIndex getMovieIndex() {
        return movieIndex;
    }

    /**
     * Sets the link strategy to be used for the current player's turn.
     * Called by the TUI after the player makes a choice.
     *
     * @param strategy The chosen ILinkStrategy.
     */
    public void setCurrentLinkStrategy(ILinkStrategy strategy) {
        this.currentLinkStrategy = strategy;
    }

    /**
     * Gets the simple name of the currently selected link strategy.
     * @return The strategy name (e.g., "Actor") or "None".
     */
    public String getCurrentLinkStrategyName() {
        return (this.currentLinkStrategy != null) ? this.currentLinkStrategy.getClass().getSimpleName().replace("LinkStrategy", "") : "None";
    }

    /**
     * Gets the currently active link strategy object.
     * @return The ILinkStrategy instance.
     */
    public ILinkStrategy getCurrentLinkStrategy() {
        return this.currentLinkStrategy;
    }

    /**
     * Switches the turn to the other player.
     */
    public void switchTurn() {
        Player temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
        this.currentLinkStrategy = null; // Reset link strategy for the new player's turn choice
        System.out.println("Controller: Switched turn to " + currentPlayer.getPlayerName());
    }

    /**
     * Processes the player's movie guess, validating it against the game rules.
     * Checks if the movie exists, if it's already been played, and if it links correctly
     * via the currently selected link strategy. If valid, adds the movie to history
     * and checks for a win condition.
     *
     * @param movieTitle The title of the movie guessed by the player.
     * @return A String message code indicating the result:
     * "OK" - Move valid, game continues.
     * "VALID_MOVE_AND_WIN" - Move valid, player wins.
     * "Error: Movie not found: '[title]'" - Movie doesn't exist in index.
     * "Error: '[title]' has already been played in this game." - Movie is duplicate.
     * "Error: Invalid link to '[title]'. Reason: [reason]" - Link strategy failed.
     * "Error: No link strategy selected for this turn." - Pre-condition failed.
     * "Error: Game is already over." - Game has ended.
     */
    public String processPlayerMove(String movieTitle) {
        if (gameOver) return "Error: Game is already over.";
        if (currentLinkStrategy == null) return "Error: No link strategy selected for this turn.";
        if (movieTitle == null || movieTitle.trim().isEmpty()) return "Error: Movie title cannot be empty.";

        Movie guessedMovie = movieIndex.findMovieByTitle(movieTitle.trim());

        if (guessedMovie == null) {
            return "Error: Movie not found: '" + movieTitle.trim() + "'.";
        }

        // Use contains check on List for game history
        if (gameHistory.stream().anyMatch(m -> m.getTitle().equalsIgnoreCase(guessedMovie.getTitle()))) {
            return "Error: '" + guessedMovie.getTitle() + "' has already been played in this game.";
        }


        // Get the last movie successfully added to the history
        Movie lastMovieInHistory = gameHistory.isEmpty() ? null : gameHistory.get(gameHistory.size() - 1);

        // The very first move doesn't need link validation
        if (lastMovieInHistory != null) {
            if (!currentLinkStrategy.isValidLink(lastMovieInHistory, guessedMovie)) {
                String reason = currentLinkStrategy.getReason(lastMovieInHistory, guessedMovie);
                return "Error: Invalid link to '" + guessedMovie.getTitle() + "'. Reason: " + reason;
            }
        }

        // --- Move is Valid ---
        System.out.println("Controller: Valid move '" + guessedMovie.getTitle() + "' by " + currentPlayer.getPlayerName());
        currentPlayer.addPlayedMovie(guessedMovie); // Add to player's personal history
        gameHistory.add(guessedMovie); // Add to global game history

        // Check for win condition
        if (currentWinCondition != null && currentWinCondition.checkWin(currentPlayer)) {
            gameOver = true;
            winner = currentPlayer;
            System.out.println("Controller: Player " + winner.getPlayerName() + " wins!");
            return "VALID_MOVE_AND_WIN"; // Signal win
        }

        return "OK"; // Signal valid move, game continues
    }

    /**
     * Checks if the game is over.
     * @return true if the game has ended, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets the global history of movies played in this game session.
     * @return An unmodifiable list of Movie objects.
     */
    public List<Movie> getGameHistory() {
        return Collections.unmodifiableList(gameHistory);
    }

    /**
     * Gets the most recently played movie in the game.
     * @return The last Movie added to the history, or null if history is empty.
     */
    public Movie getLastPlayedMovie() {
        return gameHistory.isEmpty() ? null : gameHistory.get(gameHistory.size() - 1);
    }

    /**
     * Gets the current player whose turn it is.
     * @return The current Player object.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the winner of the game, if declared.
     * @return The winning Player object, or null if the game is not over or there's no winner.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Gets the description of the currently active win condition.
     * @return A string describing how to win the current game.
     */
    public String getCurrentWinConditionDescription() {
        return (currentWinCondition != null) ? currentWinCondition.getDescription() : "Win condition not set.";
    }
}
