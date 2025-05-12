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
 * An invalid link by the current player results in the other player winning.
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
    }

    /**
     * Initializes a new game session.
     *
     * @return The initially selected movie to start the game, or null if initialization fails.
     */
    public Movie initializeNewGame() {
        this.gameOver = false;
        this.winner = null;
        this.gameHistory.clear();
        this.currentPlayer.resetForNewGame();
        this.otherPlayer.resetForNewGame();
        this.currentLinkStrategy = null;

        Movie initialMovie = selectRandomInitialMovie();
        if (initialMovie != null) {
            gameHistory.add(initialMovie);
        } else {
            System.err.println("CRITICAL: No movies available in MovieIndex to start the game.");
            this.gameOver = true;
            return null;
        }

        this.currentWinCondition = selectRandomWinCondition();
        if (this.currentWinCondition == null) {
            System.err.println("CRITICAL: Could not set a random win condition.");
            this.gameOver = true;
            return null;
        }

        System.out.println("Game Initialized. Starting Movie: " + initialMovie.getTitle() + ". Win Condition: " + currentWinCondition.getDescription());
        return initialMovie;
    }

    private Movie selectRandomInitialMovie() {
        Set<String> allTitles = movieIndex.getAllTitlesSorted();
        if (allTitles == null || allTitles.isEmpty()) {
            return null;
        }
        List<String> titlesList = new ArrayList<>(allTitles);
        String randomTitle = titlesList.get(random.nextInt(titlesList.size()));
        return movieIndex.findMovieByTitle(randomTitle);
    }

    private IWinCondition selectRandomWinCondition() {
        Set<String> allTitles = movieIndex.getAllTitlesSorted();
        if (allTitles == null || allTitles.isEmpty()) return null;

        Set<String> uniqueGenres = allTitles.stream()
                .map(movieIndex::findMovieByTitle)
                .filter(m -> m != null && m.getGenres() != null)
                .flatMap(m -> m.getGenres().stream())
                .filter(g -> g != null && !g.trim().isEmpty())
                .collect(Collectors.toSet());

        Set<Integer> uniqueYears = allTitles.stream()
                .map(movieIndex::findMovieByTitle)
                .filter(m -> m != null && m.getYear() > 0)
                .map(Movie::getYear)
                .collect(Collectors.toSet());

        boolean canUseGenre = !uniqueGenres.isEmpty();
        boolean canUseYear = !uniqueYears.isEmpty();

        if (!canUseGenre && !canUseYear) return null;

        int choice = -1;
        if (canUseGenre && canUseYear) choice = random.nextInt(2);
        else if (canUseGenre) choice = 0;
        else choice = 1;

        if (choice == 0) {
            List<String> genreList = new ArrayList<>(uniqueGenres);
            String randomGenre = genreList.get(random.nextInt(genreList.size()));
            return new GenreWinCondition(randomGenre);
        } else {
            List<Integer> yearList = new ArrayList<>(uniqueYears);
            int randomYear = yearList.get(random.nextInt(yearList.size()));
            return new YearWinCondition(randomYear);
        }
    }

    /**
     * Handles the scenario where the current player runs out of time.
     * The current player loses, and the other player wins.
     * This method is called by the GameView when its timer expires.
     */
    public void playerLostOnTimeout() {
        if (this.gameOver) {
            // Game has already ended (e.g., by a winning move just before timeout was processed)
            return;
        }

        System.out.println("Controller: Player " + (currentPlayer != null ? currentPlayer.getPlayerName() : "N/A") + " timed out.");
        this.gameOver = true;
        this.winner = this.otherPlayer; // The other player wins by default
        // The GameView will set the feedback message based on this state.
    }

    public MovieIndex getMovieIndex() { return movieIndex; }
    public void setCurrentLinkStrategy(ILinkStrategy strategy) { this.currentLinkStrategy = strategy; }
    public String getCurrentLinkStrategyName() { return (this.currentLinkStrategy != null) ? this.currentLinkStrategy.getClass().getSimpleName().replace("LinkStrategy", "") : "None"; }
    public ILinkStrategy getCurrentLinkStrategy() { return this.currentLinkStrategy; }

    public void switchTurn() {
        Player temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
        this.currentLinkStrategy = null;
        System.out.println("Controller: Switched turn to " + currentPlayer.getPlayerName());
    }

    /**
     * Processes the player's movie guess.
     * An invalid link results in the current player losing and the other player winning.
     *
     * @param movieTitle The title of the movie guessed by the player.
     * @return A String message indicating the result of the move.
     */
    public String processPlayerMove(String movieTitle) {
        if (gameOver) return "Error: Game is already over.";
        if (currentLinkStrategy == null) return "Error: No link strategy selected for this turn.";
        if (movieTitle == null || movieTitle.trim().isEmpty()) return "Error: Movie title cannot be empty.";

        Movie guessedMovie = movieIndex.findMovieByTitle(movieTitle.trim());

        if (guessedMovie == null) {
            // If movie not found, current player makes an invalid move, other player wins.
            this.gameOver = true;
            this.winner = otherPlayer;
            return "Error: Movie '" + movieTitle.trim() + "' not found. " +
                    currentPlayer.getPlayerName() + " loses. " + otherPlayer.getPlayerName() + " wins!";
        }

        if (gameHistory.stream().anyMatch(m -> m.getTitle().equalsIgnoreCase(guessedMovie.getTitle()))) {
            // If movie already played, current player makes an invalid move, other player wins.
            this.gameOver = true;
            this.winner = otherPlayer;
            return "Error: '" + guessedMovie.getTitle() + "' has already been played. " +
                    currentPlayer.getPlayerName() + " loses. " + otherPlayer.getPlayerName() + " wins!";
        }

        Movie lastMovieInHistory = gameHistory.isEmpty() ? null : gameHistory.get(gameHistory.size() - 1);

        if (lastMovieInHistory != null) { // Only validate link if it's not the very first movie of the game
            if (!currentLinkStrategy.isValidLink(lastMovieInHistory, guessedMovie)) {
                String reason = currentLinkStrategy.getReason(lastMovieInHistory, guessedMovie);
                this.gameOver = true;
                this.winner = otherPlayer; // The other player wins due to invalid link
                return "Error: Invalid link to '" + guessedMovie.getTitle() + "' by " + currentPlayer.getPlayerName() +
                        ". Reason: " + reason + ". " + otherPlayer.getPlayerName() + " wins!";
            }
        }

        // --- Move is Valid ---
        System.out.println("Controller: Valid move '" + guessedMovie.getTitle() + "' by " + currentPlayer.getPlayerName());
        currentPlayer.addPlayedMovie(guessedMovie);
        gameHistory.add(guessedMovie);

        if (currentWinCondition != null && currentWinCondition.checkWin(currentPlayer)) {
            this.gameOver = true;
            this.winner = currentPlayer; // Current player wins by meeting win condition
            System.out.println("Controller: Player " + winner.getPlayerName() + " wins!");
            return "VALID_MOVE_AND_WIN:" + guessedMovie.getTitle() + " is the winning link! " + currentPlayer.getPlayerName() + " wins!";
        }

        return "OK:" + guessedMovie.getTitle() + " is a valid link!";
    }

    public boolean isGameOver() { return gameOver; }
    public List<Movie> getGameHistory() { return Collections.unmodifiableList(gameHistory); }
    public Movie getLastPlayedMovie() { return gameHistory.isEmpty() ? null : gameHistory.get(gameHistory.size() - 1); }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getOtherPlayer() { return otherPlayer; } // Added getter for otherPlayer
    public Player getWinner() { return winner; }
    public String getCurrentWinConditionDescription() { return (currentWinCondition != null) ? currentWinCondition.getDescription() : "Win condition not set."; }
}
