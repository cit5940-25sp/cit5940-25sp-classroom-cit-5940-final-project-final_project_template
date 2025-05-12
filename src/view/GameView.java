package view; // Or your preferred package for the view

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;

import controller.GameController;
import model.*; // Includes Movie, MovieIndex, MovieDataLoader, Person, Player
import strategy.*; // All strategy interfaces and classes

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Provides a Text-based User Interface (TUI) for the Movie Name Game using Lanterna.
 * Layout inspired by the TerminalWithSuggestions example: game info at top,
 * then an input line, then suggestions/options, then feedback/history.
 * All text displayed in white color.
 * Loads data from local CSV files using MovieDataLoader.
 */
public class GameView {

    private Terminal terminal;
    private Screen screen;
    private GameController gameController;
    private MovieIndex movieIndex;

    private StringBuilder currentInput = new StringBuilder(); // For movie title input
    private List<String> currentMovieSuggestions = new ArrayList<>();
    private int selectedSuggestionIndex = -1; // For UP/DOWN navigation of movie suggestions

    private enum GamePhase {
        LOADING_DATA,
        GAME_READY,
        PLAYER_TURN_CHOOSE_LINK,
        PLAYER_TURN_INPUT_MOVIE,
        SHOWING_MOVE_RESULT,
        GAME_OVER
    }
    private volatile GamePhase currentPhase = GamePhase.LOADING_DATA;
    private volatile String feedbackMessage = ""; // For errors, results, etc.

    // Timer related fields
    private static final int TURN_DURATION_SECONDS = 30;
    private volatile int playerMoveSecondsRemaining = TURN_DURATION_SECONDS;
    private AtomicBoolean playerTimerRunning = new AtomicBoolean(false);
    private ScheduledExecutorService turnScheduler;

    // --- UI Layout Constants (Inspired by TerminalWithSuggestions) ---
    private int gameInfoLines = 3; // WinCond, Player, LinkFrom
    private int interactionPromptRow;
    private int interactionInputRow;
    private int suggestionsStartRow;
    private int feedbackRow;
    private int historyStartRow;

    private final int MAX_SUGGESTIONS_DISPLAYED = 4;
    private final int MAX_HISTORY_DISPLAYED = 2;

    // Define a constant for the default white text color
    private static final TextColor DEFAULT_TEXT_COLOR = TextColor.ANSI.WHITE;
    // Define a contrasting background for selected items, if needed, or use default black
    private static final TextColor SELECTED_SUGGESTION_BG = TextColor.ANSI.CYAN; // Example


    public GameView() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);
        turnScheduler = Executors.newScheduledThreadPool(1);

        terminal.addResizeListener((term, newSize) -> {
            try {
                screen.doResizeIfNecessary();
                updateScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        calculateLayoutConstants();
    }

    private void calculateLayoutConstants() {
        interactionPromptRow = gameInfoLines + 1;
        interactionInputRow = interactionPromptRow;
        suggestionsStartRow = interactionInputRow + 1;
        feedbackRow = suggestionsStartRow + MAX_SUGGESTIONS_DISPLAYED + 1;
        historyStartRow = feedbackRow + 2;
    }


    public boolean initializeGame(String moviesCsvPath, String creditsCsvPath) {
        currentPhase = GamePhase.LOADING_DATA;
        feedbackMessage = "Loading movie data from CSV...";
        try {
            updateScreen();

            List<Movie> movies = MovieDataLoader.loadMovies(moviesCsvPath, creditsCsvPath);
            if (movies == null || movies.isEmpty()) {
                feedbackMessage = "FATAL: No movies loaded from CSV!";
                currentPhase = GamePhase.GAME_OVER;
                updateScreen();
                return false;
            }
            System.out.println("Loaded " + movies.size() + " movies.");

            this.movieIndex = new MovieIndex(movies);
            Player p1 = new Player("Player 1");
            Player p2 = new Player("Player 2");
            this.gameController = new GameController(this.movieIndex, p1, p2);

            Movie initialMovie = this.gameController.initializeNewGame();
            if (initialMovie == null || this.gameController.isGameOver()) {
                feedbackMessage = "FATAL: Failed to initialize game logic.";
                currentPhase = GamePhase.GAME_OVER;
                updateScreen();
                return false;
            }

            feedbackMessage = gameController.getCurrentPlayer().getPlayerName() + " starts.";
            currentPhase = GamePhase.PLAYER_TURN_CHOOSE_LINK;
            return true;

        } catch (Exception e) {
            feedbackMessage = "FATAL Error during initialization: " + e.getMessage();
            currentPhase = GamePhase.GAME_OVER;
            e.printStackTrace();
            try { updateScreen(); } catch (IOException ioEx) { /* ignore */ }
            return false;
        }
    }

    public void runGameLoop() throws IOException {
        if (currentPhase == GamePhase.LOADING_DATA || (currentPhase == GamePhase.GAME_OVER && gameController == null)) {
            updateScreen();
            terminal.readInput();
            return;
        }

        boolean running = true;
        while (running) {
            if (gameController != null && gameController.isGameOver() && currentPhase != GamePhase.GAME_OVER) {
                currentPhase = GamePhase.GAME_OVER;
                feedbackMessage = gameController.getWinner() != null ?
                        gameController.getWinner().getPlayerName() + " wins!" : "Game Over!";
                feedbackMessage += " (" + gameController.getCurrentWinConditionDescription() + ")";
                stopPlayerTurnTimer();
            }

            updateScreen();

            if (currentPhase == GamePhase.GAME_OVER) {
                KeyStroke key = terminal.readInput();
                if (key.getKeyType() == KeyType.Escape || key.getKeyType() == KeyType.Enter) {
                    running = false;
                }
                continue;
            }

            KeyStroke keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                handleKeyStroke(keyStroke);
            }

            try { Thread.sleep(30); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
                feedbackMessage = "Game loop interrupted.";
                currentPhase = GamePhase.GAME_OVER;
            }
        }
    }

    // --- Timer Methods ---
    private void startPlayerTurnTimer() {
        playerMoveSecondsRemaining = TURN_DURATION_SECONDS;
        playerTimerRunning.set(true);
        if (turnScheduler == null || turnScheduler.isShutdown() || turnScheduler.isTerminated()) {
            turnScheduler = Executors.newScheduledThreadPool(1);
        }
        turnScheduler.scheduleAtFixedRate(() -> {
            if (playerTimerRunning.get()) {
                if (playerMoveSecondsRemaining > 0) {
                    playerMoveSecondsRemaining--;
                } else {
                    if (playerTimerRunning.compareAndSet(true, false)) {
                        handleTimeoutLogic();
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void stopPlayerTurnTimer() {
        playerTimerRunning.set(false);
    }

    private void handleTimeoutLogic() {
        if (currentPhase == GamePhase.PLAYER_TURN_INPUT_MOVIE || currentPhase == GamePhase.PLAYER_TURN_CHOOSE_LINK) {
            String playerName = (gameController != null && gameController.getCurrentPlayer() != null)
                    ? gameController.getCurrentPlayer().getPlayerName() : "Player";
            feedbackMessage = "Time's up! " + playerName + "'s turn forfeited.";
            currentMovieSuggestions.clear();
            selectedSuggestionIndex = -1;

            if (gameController != null) {
                gameController.switchTurn();
            }
            currentPhase = GamePhase.PLAYER_TURN_CHOOSE_LINK;
            currentInput.setLength(0);
        }
    }

    // --- Input Handling ---
    private void handleKeyStroke(KeyStroke keyStroke) throws IOException {
        if (gameController == null && currentPhase != GamePhase.GAME_OVER) return;

        if (keyStroke.getKeyType() == KeyType.Escape) {
            currentPhase = GamePhase.GAME_OVER;
            feedbackMessage = "Game exited by user.";
            stopPlayerTurnTimer();
            return;
        }

        if (currentPhase == GamePhase.PLAYER_TURN_CHOOSE_LINK || currentPhase == GamePhase.PLAYER_TURN_INPUT_MOVIE) {
            if (keyStroke.getKeyType() == KeyType.Character || keyStroke.getKeyType() == KeyType.Backspace) {
                feedbackMessage = "";
            }
        }

        switch (currentPhase) {
            case PLAYER_TURN_CHOOSE_LINK:
                handleLinkStrategyChoiceInput(keyStroke);
                break;
            case PLAYER_TURN_INPUT_MOVIE:
                handleMovieTitleInput(keyStroke);
                break;
            case SHOWING_MOVE_RESULT:
                if (keyStroke.getKeyType() == KeyType.Enter) {
                    if (gameController.isGameOver()) {
                        currentPhase = GamePhase.GAME_OVER;
                    } else {
                        gameController.switchTurn();
                        currentPhase = GamePhase.PLAYER_TURN_CHOOSE_LINK;
                    }
                    feedbackMessage = "";
                    currentInput.setLength(0);
                    currentMovieSuggestions.clear();
                    selectedSuggestionIndex = -1;
                }
                break;
        }
    }

    private void handleLinkStrategyChoiceInput(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() != KeyType.Character) return;
        if (gameController == null || gameController.getCurrentPlayer() == null) return;

        char choiceChar = keyStroke.getCharacter();
        ILinkStrategy selectedStrategy = null;
        List<ILinkStrategy> availableStrategies = Arrays.asList(
                new ActorLinkStrategy(), new DirectorLinkStrategy(), new WriterLinkStrategy(),
                new ComposerLinkStrategy(), new CinematographerLinkStrategy()
        );

        int choiceIndex = -1;
        try {
            choiceIndex = Integer.parseInt(String.valueOf(choiceChar)) - 1;
        } catch (NumberFormatException e) {
            feedbackMessage = "Invalid input. Press 1-" + availableStrategies.size() + ".";
            return;
        }

        if (choiceIndex >= 0 && choiceIndex < availableStrategies.size()) {
            selectedStrategy = availableStrategies.get(choiceIndex);
        }

        if (selectedStrategy != null) {
            Player currentPlayer = gameController.getCurrentPlayer();
            String strategyName = selectedStrategy.getClass().getSimpleName();

            if (currentPlayer.getConnectionUsage().getOrDefault(strategyName, 0) < 3) {
                gameController.setCurrentLinkStrategy(selectedStrategy);
                currentPhase = GamePhase.PLAYER_TURN_INPUT_MOVIE;
                currentInput.setLength(0);
                currentMovieSuggestions.clear();
                selectedSuggestionIndex = -1;
                feedbackMessage = "";
                startPlayerTurnTimer();
            } else {
                feedbackMessage = "Strategy [" + strategyName.replace("LinkStrategy", "") + "] used 3 times. Choose another.";
            }
        } else {
            feedbackMessage = "Invalid strategy number. Press 1-" + availableStrategies.size() + ".";
        }
    }

    private void handleMovieTitleInput(KeyStroke keyStroke) throws IOException {
        switch (keyStroke.getKeyType()) {
            case Character:
                currentInput.append(keyStroke.getCharacter());
                selectedSuggestionIndex = -1;
                updateMovieSuggestionsDisplay();
                break;
            case Backspace:
                if (currentInput.length() > 0) {
                    currentInput.deleteCharAt(currentInput.length() - 1);
                    selectedSuggestionIndex = -1;
                    updateMovieSuggestionsDisplay();
                } else {
                    currentMovieSuggestions.clear();
                }
                break;
            case Enter:
                stopPlayerTurnTimer();
                String finalGuess;
                if (selectedSuggestionIndex != -1 && selectedSuggestionIndex < currentMovieSuggestions.size()) {
                    finalGuess = currentMovieSuggestions.get(selectedSuggestionIndex);
                } else {
                    finalGuess = currentInput.toString().trim();
                }
                processPlayerGuessedMovie(finalGuess);
                break;
            case ArrowDown:
                if (!currentMovieSuggestions.isEmpty()) {
                    selectedSuggestionIndex = (selectedSuggestionIndex + 1) % currentMovieSuggestions.size();
                }
                break;
            case ArrowUp:
                if (!currentMovieSuggestions.isEmpty()) {
                    selectedSuggestionIndex = (selectedSuggestionIndex - 1 + currentMovieSuggestions.size()) % currentMovieSuggestions.size();
                }
                break;
            default:
                break;
        }
    }

    private void updateMovieSuggestionsDisplay() {
        if (movieIndex == null || gameController == null) {
            currentMovieSuggestions.clear();
            return;
        }
        if (currentInput.length() > 0) {
            List<String> rawSuggestions = movieIndex.getAutocompleteSuggestions(currentInput.toString());
            List<Movie> gameHistory = gameController.getGameHistory();
            Set<String> playedTitles = gameHistory.stream()
                    .map(Movie::getTitle)
                    .collect(Collectors.toSet());
            currentMovieSuggestions = rawSuggestions.stream()
                    .filter(title -> playedTitles.stream().noneMatch(played -> played.equalsIgnoreCase(title)))
                    .limit(MAX_SUGGESTIONS_DISPLAYED)
                    .collect(Collectors.toList());
        } else {
            currentMovieSuggestions.clear();
        }
        if (selectedSuggestionIndex >= currentMovieSuggestions.size()) {
            selectedSuggestionIndex = -1;
        }
    }

    private void processPlayerGuessedMovie(String guessedMovieTitle) throws IOException {
        if (gameController == null) return;

        if (guessedMovieTitle.isEmpty()) {
            feedbackMessage = "No movie entered.";
            currentPhase = GamePhase.PLAYER_TURN_INPUT_MOVIE;
            startPlayerTurnTimer();
            return;
        }

        String result = gameController.processPlayerMove(guessedMovieTitle);
        currentPhase = GamePhase.SHOWING_MOVE_RESULT;

        if ("OK".equals(result)) {
            feedbackMessage = "'" + guessedMovieTitle + "' - Valid link!";
            Player currentPlayer = gameController.getCurrentPlayer();
            ILinkStrategy usedStrategy = gameController.getCurrentLinkStrategy();
            if (currentPlayer != null && usedStrategy != null) {
                currentPlayer.recordConnectionUsage(usedStrategy.getClass().getSimpleName());
            }
        } else if ("VALID_MOVE_AND_WIN".equals(result)) {
            Player winner = gameController.getWinner();
            ILinkStrategy usedStrategy = gameController.getCurrentLinkStrategy();
            if (winner != null && usedStrategy != null) {
                winner.recordConnectionUsage(usedStrategy.getClass().getSimpleName());
            }
            feedbackMessage = "'" + guessedMovieTitle + "' - Winning link!";
        } else {
            feedbackMessage = result;
        }
        feedbackMessage += " (Press Enter)";
    }


    // --- Drawing Methods (Revised for TerminalWithSuggestions style and white text) ---

    private synchronized void updateScreen() throws IOException {
        if (screen == null) return;
        screen.clear();
        TerminalSize size = screen.getTerminalSize();
        if(size == null) return;

        calculateLayoutConstants();

        // 1. Draw Header (Game Info + Timer)
        drawHeaderAndTimer(size);

        // 2. Draw Content based on Phase
        switch (currentPhase) {
            case LOADING_DATA:
                printStringCentered(size.getRows() / 2, feedbackMessage.isEmpty() ? "Loading..." : feedbackMessage, DEFAULT_TEXT_COLOR);
                break;

            case GAME_OVER:
                printStringCentered(size.getRows() / 2 - 1, " G A M E   O V E R ", DEFAULT_TEXT_COLOR); // Was RED_BRIGHT
                printStringCentered(size.getRows() / 2, feedbackMessage, DEFAULT_TEXT_COLOR); // Was CYAN
                printStringCentered(size.getRows() - 2, "Press ESC or Enter to exit.", DEFAULT_TEXT_COLOR);
                break;

            case PLAYER_TURN_CHOOSE_LINK:
                drawLinkStrategyChoicesLayout(interactionPromptRow);
                drawGameHistoryLayout(size, historyStartRow);
                drawFeedbackMessageLayout(size, feedbackRow);
                break;

            case PLAYER_TURN_INPUT_MOVIE:
                drawMovieInputLayout(interactionPromptRow);
                drawSuggestionsLayout(suggestionsStartRow);
                drawGameHistoryLayout(size, historyStartRow);
                drawFeedbackMessageLayout(size, feedbackRow);
                break;

            case SHOWING_MOVE_RESULT:
                printStringCentered(feedbackRow, feedbackMessage, DEFAULT_TEXT_COLOR); // Was YELLOW
                drawGameHistoryLayout(size, historyStartRow);
                break;

            case GAME_READY:
                drawGameHistoryLayout(size, historyStartRow);
                drawFeedbackMessageLayout(size, feedbackRow);
                break;

            default:
                printStringCentered(size.getRows()/2, "Unknown game state: " + currentPhase, DEFAULT_TEXT_COLOR); // Was RED
        }
        screen.refresh();
    }

    private void drawHeaderAndTimer(TerminalSize size) throws IOException {
        if (size == null) return;
        String timerStr = (playerTimerRunning.get() && (currentPhase == GamePhase.PLAYER_TURN_INPUT_MOVIE || currentPhase == GamePhase.PLAYER_TURN_CHOOSE_LINK))
                ? "Time: " + String.format("%2d", playerMoveSecondsRemaining) + "s"
                : "";
        int timerX = size.getColumns() - timerStr.length() - 1;
        if (timerX < 0) timerX = 0;
        printString(timerX, 0, timerStr, DEFAULT_TEXT_COLOR); // Was YELLOW

        if (gameController == null) return;

        String winCond = gameController.getCurrentWinConditionDescription();
        String winCondDisplay = "Win: " + (winCond != null ? winCond : "N/A");
        int maxWinCondWidth = timerX - 1;
        if (maxWinCondWidth < 10) maxWinCondWidth = size.getColumns() -1;
        if (winCondDisplay.length() > maxWinCondWidth && maxWinCondWidth > 3) {
            winCondDisplay = winCondDisplay.substring(0, maxWinCondWidth - 3) + "...";
        }
        printString(0, 0, winCondDisplay, DEFAULT_TEXT_COLOR); // Was CYAN

        Player currentPlayer = gameController.getCurrentPlayer();
        if (currentPlayer != null) {
            String playerStatus = "Player: " + currentPlayer.getPlayerName();
            if (currentPhase == GamePhase.PLAYER_TURN_INPUT_MOVIE && gameController.getCurrentLinkStrategy() != null) {
                playerStatus += " (Link: " + gameController.getCurrentLinkStrategyName() + ")";
            }
            printString(0, 1, playerStatus, DEFAULT_TEXT_COLOR); // Was GREEN
        }

        Movie lastMovie = gameController.getLastPlayedMovie();
        String linkFromText = "Link from: ";
        if (lastMovie != null) {
            linkFromText += lastMovie.getTitle() + " (" + lastMovie.getYear() + ")";
        } else {
            linkFromText += "(Game Start)";
        }
        printString(0, 2, linkFromText, DEFAULT_TEXT_COLOR); // Was MAGENTA
    }

    private void drawLinkStrategyChoicesLayout(int promptRow) throws IOException {
        if (gameController == null || gameController.getCurrentPlayer() == null) return;
        Player currentPlayer = gameController.getCurrentPlayer();

        printString(0, promptRow, "Choose Link Strategy (1-5):", DEFAULT_TEXT_COLOR);
        List<ILinkStrategy> strategies = Arrays.asList(
                new ActorLinkStrategy(), new DirectorLinkStrategy(), new WriterLinkStrategy(),
                new ComposerLinkStrategy(), new CinematographerLinkStrategy()
        );
        for (int i = 0; i < strategies.size(); i++) {
            String stratClassName = strategies.get(i).getClass().getSimpleName();
            String stratNameSimple = stratClassName.replace("LinkStrategy", "");
            int usesLeft = 3 - (currentPlayer.getConnectionUsage() != null ? currentPlayer.getConnectionUsage().getOrDefault(stratClassName, 0) : 0);
            // All strategy text is white. Distinction for uses left can be through text itself.
            TextColor color = DEFAULT_TEXT_COLOR; // Was YELLOW or RED_BRIGHT
            String text = String.format("  %d. %-15s (Uses: %d/3)", (i + 1), stratNameSimple, currentPlayer.getConnectionUsage().getOrDefault(stratClassName,0));
            printString(0, promptRow + 1 + i, text, color);
        }
    }

    private void drawMovieInputLayout(int promptRow) throws IOException {
        interactionInputRow = promptRow + 1;
        suggestionsStartRow = interactionInputRow + 1;
        feedbackRow = suggestionsStartRow + MAX_SUGGESTIONS_DISPLAYED +1;
        historyStartRow = feedbackRow + 2;

        printString(0, promptRow, "Enter Movie Title:", DEFAULT_TEXT_COLOR);
        String inputDisplay = "> " + currentInput.toString();
        printString(0, interactionInputRow, inputDisplay, DEFAULT_TEXT_COLOR);
        if (screen != null) {
            screen.setCharacter(("> ".length() + currentInput.length()), interactionInputRow,
                    new TextCharacter('|', DEFAULT_TEXT_COLOR, TextColor.ANSI.BLACK));
        }
    }

    private void drawSuggestionsLayout(int startRow) throws IOException {
        if (currentMovieSuggestions.isEmpty()) return;

        printString(2, startRow -1, "Suggestions:", DEFAULT_TEXT_COLOR); // Was BLUE
        for (int i = 0; i < currentMovieSuggestions.size() && i < MAX_SUGGESTIONS_DISPLAYED; i++) {
            // Selected suggestion: White text on a contrasting background (e.g., BLUE or DARK_GREY)
            // Non-selected: White text on black background
            TextColor fgColor = (i == selectedSuggestionIndex) ? TextColor.ANSI.WHITE : DEFAULT_TEXT_COLOR;
            TextColor bgColor = (i == selectedSuggestionIndex) ? TextColor.ANSI.BLUE : TextColor.ANSI.BLACK; // Example: BLUE for selection
            String suggestionText = "  " + currentMovieSuggestions.get(i);

            TerminalSize size = screen.getTerminalSize();
            if (size == null) return;
            int row = startRow + i;
            if (row >= size.getRows()) continue;

            for(int j=0; j < suggestionText.length(); j++){
                int col = 2;
                if(col + j < size.getColumns()) {
                    screen.setCharacter(col + j, row, new TextCharacter(suggestionText.charAt(j), fgColor, bgColor));
                } else { break; }
            }
            int startClearCol = 2 + suggestionText.length();
            if (startClearCol < size.getColumns()) {
                for (int k = startClearCol; k < size.getColumns(); k++) {
                    screen.setCharacter(k, row, TextCharacter.DEFAULT_CHARACTER.withBackgroundColor(TextColor.ANSI.BLACK));
                }
            }
        }
        TerminalSize size = screen.getTerminalSize();
        if (size == null) return;
        for (int i = currentMovieSuggestions.size(); i < MAX_SUGGESTIONS_DISPLAYED; i++) {
            int row = startRow + i;
            if (row < size.getRows()) {
                for (int k = 2; k < size.getColumns(); k++) {
                    screen.setCharacter(k, row, TextCharacter.DEFAULT_CHARACTER.withBackgroundColor(TextColor.ANSI.BLACK));
                }
            }
        }
    }

    private void drawFeedbackMessageLayout(TerminalSize size, int row) throws IOException {
        if (!feedbackMessage.isEmpty()) {
            int actualFeedbackRow = Math.min(row, size.getRows() - 1);
            if (actualFeedbackRow < 0) actualFeedbackRow = 0;
            for (int i = 0; i < size.getColumns(); i++) {
                printString(i, actualFeedbackRow, " ", DEFAULT_TEXT_COLOR);
            }
            printString(0, actualFeedbackRow, feedbackMessage, DEFAULT_TEXT_COLOR); // Was RED or YELLOW
        }
    }

    private void drawGameHistoryLayout(TerminalSize size, int startRow) throws IOException {
        if (gameController == null) return;
        List<Movie> history = gameController.getGameHistory();
        if (history.isEmpty()) return;

        int actualStartRow = Math.max(startRow, 0);
        if (actualStartRow -1 >=0) {
            printString(0, actualStartRow -1, "--- Recent Moves ---", DEFAULT_TEXT_COLOR); // Was BLUE
        }

        int count = 0;
        for (int i = history.size() - 1; i >= 0 && count < MAX_HISTORY_DISPLAYED; i--) {
            Movie m = history.get(i);
            int currentRow = actualStartRow + count;
            if (currentRow < size.getRows()) {
                String historyLine = String.format("  %d. %s (%d)", history.size() - i, m.getTitle(), m.getYear());
                printString(0, currentRow, historyLine, DEFAULT_TEXT_COLOR); // Was WHITE_BRIGHT
            } else {
                break;
            }
            count++;
        }
    }

    // --- Low-level Drawing Helpers ---
    private void printString(int x, int y, String text, TextColor color) throws IOException {
        if (screen == null || text == null) return;
        TerminalSize terminalSize = screen.getTerminalSize();
        if (terminalSize == null || y < 0 || y >= terminalSize.getRows()) return;

        for (int i = 0; i < text.length(); i++) {
            int currentX = x + i;
            if (currentX >= 0 && currentX < terminalSize.getColumns()) {
                try {
                    screen.setCharacter(currentX, y, new TextCharacter(
                            text.charAt(i), color, TextColor.ANSI.BLACK // Keep background black
                    ));
                } catch (Exception e) { /* ignore drawing error */ }
            } else { break; }
        }
    }

    private void printStringCentered(int y, String text, TextColor color) throws IOException {
        if (screen == null || text == null) return;
        TerminalSize size = screen.getTerminalSize();
        if (size == null) return;
        int x = (size.getColumns() - text.length()) / 2;
        if (x < 0) x = 0;
        printString(x, y, text, color);
    }

    public void shutdown() throws IOException {
        System.out.println("GameView shutdown initiated...");
        stopPlayerTurnTimer();
        if (turnScheduler != null && !turnScheduler.isShutdown()) {
            turnScheduler.shutdownNow();
            try {
                if (!turnScheduler.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    System.err.println("Warning: Timer scheduler did not terminate cleanly after 500ms.");
                } else {
                    System.out.println("Timer scheduler terminated.");
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                turnScheduler.shutdownNow();
                System.err.println("Timer scheduler shutdown interrupted.");
            }
        } else {
            System.out.println("Timer scheduler already shutdown or null.");
        }
        if (screen != null) {
            try { screen.close(); System.out.println("Lanterna screen closed."); }
            catch (IOException e) { System.err.println("Error closing Lanterna screen: " + e.getMessage()); }
            screen = null;
        }
        if (terminal != null) {
            try { terminal.close(); System.out.println("Lanterna terminal closed."); }
            catch (IOException e) { System.err.println("Error closing Lanterna terminal: " + e.getMessage()); }
            terminal = null;
        }
        System.out.println("GameView shutdown completed.");
    }
}
