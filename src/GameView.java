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

public class GameView {
    private Terminal terminal;
    private Screen screen;
    private List<String> dictionary;
    private StringBuilder currentInput = new StringBuilder();
    private List<String> suggestions = new ArrayList<>();
    private int cursorPosition = 0;
    private MovieTrie movieTrie;
    // Timer variables
    private int                      secondsRemaining = 30;
    private boolean                  timerRunning = true;
    private ScheduledExecutorService scheduler;

    // Game State
    private Player player1;
    private Player player2;
    private Movie currentMovie;
    int round;
    // Win
    private Player winner;
    // Move validation
    private boolean isValidMove;

    // History
    Deque<HistoryEntry> historyEntries;

    public GameView() {
        historyEntries = new ArrayDeque<>();
        currentMovie = new Movie("", -1);
        round = 0;
        player1 = new Player("", null);
        player2 = new Player("", null);
        isValidMove = true;

        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            dictionary = Arrays.asList(
                    "java", "javascript", "python", "terminal", "program",
                    "code", "compiler", "development", "interface", "application"
            );

            // Initialize timer thread
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                if (timerRunning && secondsRemaining > 0) {
                    secondsRemaining--;
                    updateScreen();
                }
            }, 1, 1, TimeUnit.SECONDS);

//            movieTrie = new MovieTrie();
//            movieTrie.buildTrie();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void run() throws IOException {
//        boolean running = true;
//
//        screen.clear();
//        printString(0, 0, "> ");
//        cursorPosition = 2;
//        updateScreen();
//
//        while (running) {
//            KeyStroke keyStroke = terminal.pollInput();
//            if (keyStroke != null) {
//                switch (keyStroke.getKeyType()) {
//                    case Character:
//                        handleCharacter(keyStroke.getCharacter());
//                        break;
//                    case Backspace:
//                        handleBackspace();
//                        break;
//                    case Enter:
//                        handleEnter();
//                        break;
//                    case EOF:
//                    case Escape:
//                        running = false;
//                        break;
//                    default:
//                        break;
//                }
//                updateScreen();
//            }
//            updateScreen();
//
//            // Small delay to prevent CPU hogging
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//
//        // Shutdown timer
//        scheduler.shutdown();
//        screen.close();
//        terminal.close();
//    }

    private void display() {
        // Game State
        int col = 0;
        int row = 15;
        printString(col, row++, "===== GAME STATUS =====");
        printString(col, row++, "Current Round: " + round);
        printString(col, row++, "Current Movie: " + currentMovie.getTitle() + " (" + currentMovie.getReleaseYear() + ")");
        printString(col, row++, "Player 1: " + player1.getName());
        printString(col, row++, "Movies Collected: " + player1.getMoviesPlayed().size());
        printString(col, row++, "Player 2: " + player2.getName());
        printString(col, row++, "Movies Collected: " + player2.getMoviesPlayed().size());
        printString(col, row++, "=======================");

        // Win
        col = 40;
        row = 8;
        if (winner != null) {
            printString(col, row++, "=== CONGRATULATIONS ===");
            printString(col, row++, winner.getName() + " wins!");
            printString(col, row++, "Number of movies collected: " + winner.getMoviesPlayed().size());
            printString(col, row++, "=======================");
        }

        // Move validation
        col = 40;
        row = 2;
        if (!isValidMove) {
            printString(col, row++, "==== INVALID MOVE! ====");
            printString(col, row++, "This move does not comply with the game rules.");
            printString(col, row++, "=======================");}

        // Sug
        col = 2;
        row = 2;
        for (String suggestion : suggestions) {
            printString(col, row++, suggestion);
        }

        // History
        col = 40;
        row = 15;
        if (historyEntries.size() > 0) {
            printString(col, row++, "======= HISTORY =======");
            for (HistoryEntry historyEntry : historyEntries) {
                printString(col, row++, historyEntry.toString());
            }
            printString(col, row++, "=======================");
        }
    }

//    private void handleCharacter(char c) {
//        currentInput.insert(cursorPosition - 2, c);
//        cursorPosition++;
//        updateSuggestions();
//    }
//
//    private void handleBackspace() {
//        if (cursorPosition > 2) {
//            currentInput.deleteCharAt(cursorPosition - 3);
//            cursorPosition--;
//            updateSuggestions();
//        }
//    }
//
//    private void handleEnter() throws IOException {
//        int currentRow = screen.getCursorPosition().getRow();
//        currentRow += 1 + suggestions.size();
//
//        printString(0, currentRow, "> ");
//        currentInput = new StringBuilder();
//        cursorPosition = 2;
//        suggestions.clear();
//
//        String inputMovieName = currentInput.toString();
//
//    }
//
//    private void updateSuggestions() {
//        suggestions.clear();
//        String prefix = currentInput.toString();
//
//        if (!prefix.isEmpty()) {
//            suggestions = movieTrie.getSuggestions(prefix);
//        }
//    }

    private void updateScreen() {
        try {
            synchronized (screen) {
                screen.clear();

                // Print timer at top right
                String timerText = "Time: " + secondsRemaining + "s";
                TerminalSize size = screen.getTerminalSize();
                printString(size.getColumns() - timerText.length(), 0, timerText);

                // Print current command line
                printString(0, 0, "> " + currentInput.toString());

                // Print suggestions
                int row = 1;
                for (String suggestion : suggestions) {
                    printString(2, row++, suggestion);
                }

                display();
                screen.setCursorPosition(new TerminalPosition(cursorPosition, 0));
                screen.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printString(int column, int row, String text) {
        for (int i = 0; i < text.length(); i++) {
            screen.setCharacter(column + i, row,
                    new TextCharacter(text.charAt(i),
                            TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
    }

    public static void main(String[] args) {
        try {
            GameView app = new GameView();
//            app.run();

            Thread.sleep(3000);
            Player player1 = new Player("p1", null);
            Player player2 = new Player("p2", null);
            Movie movie = new Movie("Avengers", 2012);
            int round = 2;
            app.displayGameState(player1, player2, movie, round);

            Thread.sleep(3000);
            app.displaySuggestions(Arrays.asList("aa", "ab", "ac", "ad", "ae", "af"));

            Thread.sleep(3000);
            app.displayInvalidMove();

            Thread.sleep(3000);
            app.displayWin(player2);

            Thread.sleep(3000);
            Deque<HistoryEntry> historyEntries1 = new ArrayDeque<>();
            for (int i = 0; i < 5; i++) {
                HistoryEntry historyEntry = new HistoryEntry(movie, "conn reason");
                historyEntries1.add(historyEntry);
            }
            app.displayHistory(historyEntries1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayGameState(Player player1, Player player2, Movie currentMovie, int round) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentMovie = currentMovie;
        this.round = round;
        updateScreen();
    }

    public void displayInvalidMove() {
        this.isValidMove = false;
        updateScreen();
    }

    public void displayWin(Player winner) {
        this.winner = winner;
        updateScreen();
    }

    public void displaySuggestions(List<String> movieTitles) {
        this.suggestions = movieTitles;
    }

    public void displayHistory(Deque<HistoryEntry> historyEntries) {
        this.historyEntries = historyEntries;
        updateScreen();
    }
}