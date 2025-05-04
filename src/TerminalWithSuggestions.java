import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

enum InputStage {
    PLAYER1_NAME,
    PLAYER2_NAME,
    WIN_CONDITION_SELECTION,
    IN_GAME
}

public class TerminalWithSuggestions {
    private InputStage stage = InputStage.PLAYER1_NAME;
    private String player1Name = "";
    private String player2Name = "";
    private int winConditionIndex = 0;
    private List<WinCondition> winConditions = Arrays.asList(
            new FiveHorrorMoviesWin(),
            new ThreeNolanMoviesWin()
    );

    private GameController controller;
    private Terminal terminal;
    private Screen screen;
    private StringBuilder currentInput = new StringBuilder();
    private List<String> suggestions = new ArrayList<>();
    private int selectedSuggestionIndex = -1;
    private int cursorPosition = 0;

    // Timer variables
    private int secondsRemaining = 30;
    private boolean timerRunning = true;
    private ScheduledExecutorService scheduler;

    public TerminalWithSuggestions(GameController controller) throws IOException {
        this.controller = controller;
        this.terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Initialize timer thread
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (stage == InputStage.IN_GAME && timerRunning && secondsRemaining > 0) {
                secondsRemaining--;
                try {
                    updateScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (secondsRemaining == 0) {
                    timerRunning = false;
                    try {
                        printInfo("⏰ Time's up! " + controller.getGameState().getOtherPlayer().getName() + " wins!");
                        Thread.sleep(3000); // pause so player sees the message
                        screen.close();
                        terminal.close();
                        System.exit(0); // clean exit
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void run() throws IOException, InterruptedException {
        controller.getMovieDatabase().preloadPopularMovies();
        boolean running = true;

        // Initial screen
        screen.clear();
        updateScreen();
        screen.refresh();

        while (running) {
            KeyStroke keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case Character:
                        handleCharacter(keyStroke.getCharacter());
                        break;
                    case Backspace:
                        handleBackspace();
                        break;
                    case Enter:
                        running = handleEnter();  // returns false if game ends
                        break;
                    case Escape:
                    case EOF:
                        running = false;
                        break;
                    case ArrowDown:
                        if (!suggestions.isEmpty())
                            selectedSuggestionIndex = (selectedSuggestionIndex + 1) % suggestions.size();
                        break;
                    case ArrowUp:
                        if (!suggestions.isEmpty())
                            selectedSuggestionIndex = (selectedSuggestionIndex - 1 + suggestions.size()) % suggestions.size();
                        break;
                }
                updateSuggestions();
                updateScreen();
            }

            Thread.sleep(10);
        }

        scheduler.shutdown();
        screen.close();
        terminal.close();
    }

    private void handleCharacter(char c) {
        currentInput.insert(cursorPosition, c);
        cursorPosition++;
    }

    private void handleBackspace() {
        if (cursorPosition > 0) {
            currentInput.deleteCharAt(cursorPosition - 1);
            cursorPosition--;
        }
    }

    private boolean handleEnter() {
        String input = currentInput.toString().trim();

        switch (stage) {
            case PLAYER1_NAME:
                player1Name = input;
                currentInput.setLength(0);
                cursorPosition = 0;
                stage = InputStage.PLAYER2_NAME;
                break;

            case PLAYER2_NAME:
                player2Name = input;
                currentInput.setLength(0);
                cursorPosition = 0;
                stage = InputStage.WIN_CONDITION_SELECTION;
                break;

            case WIN_CONDITION_SELECTION:
                try {
                    winConditionIndex = Integer.parseInt(input);
                    if (winConditionIndex >= 1 && winConditionIndex <= winConditions.size()) {
                        WinCondition selected = winConditions.get(winConditionIndex - 1);
                        System.out.println("Selected win condition: " + selected.description());
                        Movie startingMovie = controller.startGame(player1Name, player2Name, selected);
                        secondsRemaining = 30;
                        controller.getGameState().getTimer().start();
                        stage = InputStage.IN_GAME;
                    } else {
                        printInfo("Please enter a number from 1 to " + winConditions.size());
                    }
                } catch (NumberFormatException e) {
                    printInfo("Invalid input. Please enter a number.");
                }
                currentInput.setLength(0);
                cursorPosition = 0;
                break;

            case IN_GAME:
                if (input.equalsIgnoreCase("exit")) return false;

                // 🎯 First: handle selection from suggestions
                if (selectedSuggestionIndex >= 0) {
                    currentInput.setLength(0);
                    currentInput.append(suggestions.get(selectedSuggestionIndex));
                    cursorPosition = currentInput.length();
                    selectedSuggestionIndex = -1; // reset
                    return true; // Only populate, don’t submit yet
                }

                // ⏰ Check timer expiration
                if (secondsRemaining <= 0) {
                    printInfo("⏰ Time's up! " + controller.getGameState().getOtherPlayer().getName() + " wins!");
                    return false;
                }

                // ✅ Submit actual guess
                TurnResult result = controller.processTurn(input);
                printInfo(result.getMessage());

                if (!result.isSucess()) {
                    // ❌ Invalid move — don't switch player
                    return true;
                }

                // 🎉 Valid move — reset for next player
                currentInput.setLength(0);
                cursorPosition = 0;
                secondsRemaining = 30;
                controller.getGameState().getTimer().start();
                return true;
        }

            return true;
    }

    private void updateSuggestions() {
        String prefix = currentInput.toString();
        suggestions.clear();
        if (!prefix.isEmpty()) {
            suggestions = new ArrayList<>(controller.getAutocompleteSuggestions(prefix));
        }
    }

    private void updateScreen() throws IOException {
        synchronized (screen) {
            screen.clear();
            TerminalSize size = screen.getTerminalSize();

            switch (stage) {
                case PLAYER1_NAME:
                    printString(0, 0, "🎬 Welcome to Movie Game!");
                    printString(0, 2, "Please enter Player 1 name:");
                    printString(0, 4, "> " + currentInput.toString());
                    screen.setCursorPosition(new TerminalPosition(cursorPosition + 2, 4));
                    break;

                case PLAYER2_NAME:
                    printString(0, 0, "Player 1: " + player1Name);
                    printString(0, 2, "Please enter Player 2 name:");
                    printString(0, 4, "> " + currentInput.toString());
                    screen.setCursorPosition(new TerminalPosition(cursorPosition + 2, 4));
                    break;

                case WIN_CONDITION_SELECTION:
                    printString(0, 0, "Player 1: " + player1Name);
                    printString(0, 1, "Player 2: " + player2Name);
                    printString(0, 3, "Please choose a win condition by number:");
                    for (int i = 0; i < winConditions.size(); i++) {
                        printString(2, 4 + i, (i + 1) + ". " + winConditions.get(i).description());
                    }
                    printString(0, 4 + winConditions.size() + 1, "> " + currentInput.toString());
                    screen.setCursorPosition(new TerminalPosition(cursorPosition + 2, 4 + winConditions.size() + 1));
                    break;

                case IN_GAME:
                    GameState state = controller.getGameState();

                    // Header
                    printString(0, 0, "Player: " + state.getCurrentPlayer().getName());
                    printString(0, 1, "Round: " + state.getCurrRound());
                    String timerText = "Time: " + secondsRemaining + "s";
                    printString(size.getColumns() - timerText.length(), 0, timerText);
                    printString(0, 2, "Last movie: " + state.getRecentHistory().get(0).getTitle() + " (" + state.getRecentHistory().get(0).getYear() + ")" );

                    // Prompt
                    printString(0, 4, "> " + currentInput.toString());

                    // Suggestions
                    int row = 5;
                    for (int i = 0; i < suggestions.size(); i++) {
                        String s = suggestions.get(i);
                        if (i == selectedSuggestionIndex) {
                            printStringColored(2, row++, "> " + s, TextColor.ANSI.BLACK, TextColor.ANSI.CYAN); // highlighted
                        } else {
                            printString(2, row++, "- " + s); // normal
                        }
                    }

                    // Recent history
                    row++;
                    printString(0, row++, "Recent History:");
                    for (Movie m : state.getRecentHistory()) {
                        printString(2, row++, m.getTitle() + " (" + m.getYear() + ")");
                    }

                    screen.setCursorPosition(new TerminalPosition(cursorPosition + 2, 4));
                    break;
            }

            screen.refresh();
        }
    }

    private void printString(int column, int row, String text) {
        for (int i = 0; i < text.length(); i++) {
            screen.setCharacter(column + i, row,
                    new TextCharacter(text.charAt(i),
                            TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
    }

    private void printStringColored(int column, int row, String text, TextColor fg, TextColor bg) {
        for (int i = 0; i < text.length(); i++) {
            screen.setCharacter(column + i, row,
                    new TextCharacter(text.charAt(i), fg, bg));
        }
    }

    private void printInfo(String msg) {
        try {
            screen.clear();
            printString(0, 0, msg);
            screen.refresh();
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Launcher
    public static void main(String[] args) {
        String apiKey = ConfigLoader.get("tmdb.api.key");
        GameController controller = new GameController(apiKey);

        try {
            new TerminalWithSuggestions(controller).run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}