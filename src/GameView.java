import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameView {
    private final Screen screen;
    private final int maxHistory = 5;
    private final Deque<HistoryEntry> movieHistory = new ArrayDeque<>();
    private final MovieTrie movieTrie;
    private List<String> suggestions;
    private int secondsRemaining = 30;
    private Timer timer;


    public GameView(Screen screen, MovieTrie movieTrie) {
        this.screen = screen;
        this.movieTrie = movieTrie;
    }

    public void displayGameState(Player player1, Player player2, Movie currentMovie, int round) {
        try {
            screen.clear();

            printString(0, 0, "===== GAME STATUS =====");
            printString(0, 1, "Current Round: " + round);
            printString(50, 1, "Time Left: " + secondsRemaining + "s");

            printString(0, 3, "Current Movie:");
            printString(2, 4, currentMovie.getTitle() + " (" + currentMovie.getReleaseYear() + ")");
            printString(2, 5, "Genres: " + String.join(", ", currentMovie.getGenres()));

            printString(0, 7, "Player 1: " + player1.getName());
            printString(2, 8, "Movies Collected: " + player1.getMoviesPlayed().size());
            printString(2, 9, "Win Condition: " + player1.getWinCondition());

            printString(0, 11, "Player 2: " + player2.getName());
            printString(2, 12, "Movies Collected: " + player2.getMoviesPlayed().size());
            printString(2, 13, "Win Condition: " + player2.getWinCondition());

            printString(0, 15, "===================");

            // Movie history
            printString(0, 17, "Recent Movies:");
            int row = 18;
            for (HistoryEntry entry : movieHistory) {
                Movie m = entry.getMovie();
                String summary = String.format("%s (%d) [%s] [%s]",
                        m.getTitle(),
                        m.getReleaseYear(),
                        String.join(", ", m.getGenres()),
                        entry.getConnectionReason());
                printString(2, row++, summary);
            }

            // Autocomplete suggestions
            if (suggestions != null && !suggestions.isEmpty()) {
                printString(0, row++, "Suggestions:");
                for (String suggestion : suggestions) {
                    printString(2, row++, suggestion);
                }
            }

            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayInvalidMove(Player player, String reason) {
        try {
            int row = screen.getTerminalSize().getRows() - 3;
            printString(0, row, "❌ INVALID MOVE! ❌");
            printString(0, row + 1, player.getName() + " loses. Reason: " + reason);
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayWin(Player winner, Deque<HistoryEntry> history) {
        try {
            screen.clear();
            printString(0, 0, "🎉 Congratulations " + winner.getName() + " wins! 🎉");
            printString(0, 2, "Number of movies collected: " + winner.getMoviesPlayed().size());

            printString(0, 4, "Final Movie Chain:");
            int row = 5;
            for (HistoryEntry entry : history) {
                Movie m = entry.getMovie();
                String title = m != null ? String.format("%s (%d) [%s] [%s]",
                        m.getTitle(),
                        m.getReleaseYear(),
                        String.join(", ", m.getGenres()),
                        entry.getConnectionReason()) : "[Unknown Movie]";
                printString(2, row++, title);
            }
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToHistory(HistoryEntry entry) {
        if (movieHistory.size() == maxHistory) {
            movieHistory.removeFirst();
        }
        movieHistory.addLast(entry);
    }

    public void updateSuggestions(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            suggestions = List.of();
        } else {
            suggestions = movieTrie.getSuggestions(prefix);
        }
    }

    public void startTimer(Runnable onTimeout, Runnable onTick) {
        if (timer != null) timer.cancel();
        secondsRemaining = 30;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsRemaining--;
                if (secondsRemaining <= 0) {
                    timer.cancel();
                    onTimeout.run();
                } else {
                    onTick.run();
                }
            }
        }, 1000, 1000);
    }


    public void stopTimer() {
        if (timer != null) timer.cancel();
    }

    private void printString(int column, int row, String text) {
        for (int i = 0; i < text.length(); i++) {
            screen.setCharacter(column + i, row,
                new TextCharacter(text.charAt(i),
                        TextColor.ANSI.WHITE,
                        TextColor.ANSI.BLACK));
        }
    }

    public void displayPrompt(String message) {
        try {
            printString(0, screen.getTerminalSize().getRows() - 2, message);
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KeyStroke readKeyStrokeNonBlocking() throws IOException {
        return screen.pollInput();
    }

    public void displayInputLine(String input) {
        try {
            int row = screen.getTerminalSize().getRows() - 1;
            String displayText = "> " + input;
            // Clear old input by overwriting with spaces
            printString(0, row, String.format("%-" + screen.getTerminalSize().getColumns() + "s", ""));
            // Display input
            printString(0, row, displayText);
            // Move cursor after the input
            screen.setCursorPosition(new TerminalPosition(displayText.length(), row));
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
