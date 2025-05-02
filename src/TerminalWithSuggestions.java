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

public class TerminalWithSuggestions {
    private final GameController controller;
    private final Terminal terminal;
    private final Screen screen;

    private final StringBuilder currentInput = new StringBuilder();
    private final List<String> suggestions = new ArrayList<>();
    private int cursorPosition = 0;

    private int secondsRemaining = 30;
    private boolean timerRunning = true;
    private ScheduledExecutorService scheduler;

    public TerminalWithSuggestions(GameController controller) throws IOException {
        this.controller = controller;
        this.terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        screen.startScreen();
    }

    public void run() throws IOException, InterruptedException {
        boolean running = true;
        setupGame();

        while (running) {
            screen.clear();
            updateSuggestions();
            drawUI();
            screen.refresh();

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
                        running = handleSubmit(); // may return false to end game
                        break;
                    case Escape:
                        running = false;
                        break;
                }
            }

            Thread.sleep(10);
        }

        scheduler.shutdown();
        screen.close();
        terminal.close();
    }

    private void setupGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Player 1 name: ");
        String p1 = scanner.nextLine().trim();
        System.out.print("Enter Player 2 name: ");
        String p2 = scanner.nextLine().trim();

        controller.getMovieDatabase().preloadPopularMovies();
        controller.startGame(p1, p2, new FiveHorrorMoviesWin());

        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (timerRunning && secondsRemaining > 0) {
                secondsRemaining--;
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void drawUI() {
        GameState state = controller.getGameState();
        TerminalSize size = screen.getTerminalSize();

        print(0, 0, "Player: " + state.getCurrentPlayer().getName());
        print(0, 1, "Round: " + state.getCurrRound());
        print(size.getColumns() - 12, 0, "Time: " + secondsRemaining + "s");
        print(0, 3, "> " + currentInput.toString());

        int row = 4;
        for (String suggestion : suggestions) {
            print(2, row++, suggestion);
        }

        int historyStart = row + 1;
        print(0, historyStart++, "History:");
        for (Movie m : state.getRecentHistory()) {
            print(2, historyStart++, m.getTitle() + " (" + m.getYear() + ")");
        }

        screen.setCursorPosition(new TerminalPosition(cursorPosition + 2, 3));
    }

    private void print(int col, int row, String text) {
        for (int i = 0; i < text.length(); i++) {
            screen.setCharacter(col + i, row,
                    new TextCharacter(text.charAt(i),
                            TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
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

    private boolean handleSubmit() {
        String input = currentInput.toString().trim();
        if (input.equalsIgnoreCase("exit")) return false;

        controller.processTurn(input);

        GameState state = controller.getGameState();
        if (state.hasCurrentPlayerWon()) {
            try {
                print(0, screen.getTerminalSize().getRows() - 2, "🎉 " + state.getCurrentPlayer().getName() + " wins!");
                screen.refresh();
                Thread.sleep(3000);
            } catch (Exception e) { }
            return false;
        }

        // reset input and timer
        currentInput.setLength(0);
        cursorPosition = 0;
        secondsRemaining = 30;

        state.switchPlayer();
        return true;
    }

    private void updateSuggestions() {
        suggestions.clear();
        String prefix = currentInput.toString();
        if (!prefix.isEmpty()) {
            List<String> auto = controller.getAutocompleteSuggestions(prefix);
            suggestions.addAll(auto);
        }
    }
}