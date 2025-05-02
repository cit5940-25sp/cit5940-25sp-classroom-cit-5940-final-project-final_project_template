import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
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
            new FiveHorrorMoviesWin()
    );

    private GameController controller;
    private Terminal terminal;
    private Screen screen;
    private StringBuilder currentInput = new StringBuilder();
    private List<String> suggestions = new ArrayList<>();
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
            if (timerRunning && secondsRemaining > 0) {
                secondsRemaining--;
                try {
                    updateScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void run() throws IOException, InterruptedException {
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
                    int choice = Integer.parseInt(input);
                    if (choice >= 1 && choice <= winConditions.size()) {
                        WinCondition selected = winConditions.get(choice - 1);
                        controller.getMovieDatabase().preloadPopularMovies();
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

                if (secondsRemaining <= 0) {
                    printInfo("⏰ Time's up! " + controller.getGameState().getOtherPlayer().getName() + " wins!");
                    return false;
                }

                TurnResult result = controller.processTurn(input);
                printInfo(result.getMessage());

                if (!result.isSucess()) {
                    // Invalid move — no reset or switch
                    return true;
                }

                // Valid move: reset input & timer
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

                    // Prompt
                    printString(0, 3, "> " + currentInput.toString());

                    // Suggestions
                    int row = 4;
                    for (String s : suggestions) {
                        printString(2, row++, "- " + s);
                    }

                    // Recent history
                    row++;
                    printString(0, row++, "Recent History:");
                    for (Movie m : state.getRecentHistory()) {
                        printString(2, row++, m.getTitle() + " (" + m.getYear() + ")");
                    }

                    screen.setCursorPosition(new TerminalPosition(cursorPosition + 2, 3));
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
        controller.getMovieDatabase().preloadPopularMovies();
        controller.startGame("Player 1", "Player 2", new FiveHorrorMoviesWin());

        try {
            new TerminalWithSuggestions(controller).run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}