import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameView {
    private final Screen screen;
    private final int maxHistory = 5;
    private final Deque<HistoryEntry> movieHistory = new ArrayDeque<>();
    private MovieTrie movieTrie;
    private List<String> suggestions;
    private int secondsRemaining = 30;
    private Timer timer;

    public void setMovieTrie(MovieTrie trie) {
        this.movieTrie = trie;
    }

    public GameView(Screen screen) {
        this.screen = screen;
    }

    public void displayGameState(Player player1, Player player2, Movie currentMovie, int round, String errorMessage) {
        try {
            screen.clear();

            drawBox(0, 0, 60, 3);
            printString(2, 1, "Round: " + round);
            printString(45, 1, "Time: " + secondsRemaining + "s");

            drawBox(0, 4, 60, 3);
            printString(2, 5, "[Movie] Current: " + currentMovie.getTitle() + " (" + currentMovie.getReleaseYear() + ")");
            printString(2, 6, "Genres: " + String.join(", ", currentMovie.getGenres()));

            drawBox(0, 8, 60, 4);
            printString(2, 9, "[P1] Player 1: " + player1.getName());
            printString(2, 10, "Movies: " + player1.getMoviesPlayed().size());
            printString(2, 11, "Win: " + player1.getWinCondition().getDescription());

            drawBox(0, 13, 60, 4);
            printString(2, 14, "[P2] Player 2: " + player2.getName());
            printString(2, 15, "Movies: " + player2.getMoviesPlayed().size());
            printString(2, 16, "Win: " + player2.getWinCondition().getDescription());

            drawBox(0, 18, 60, maxHistory + 2);
            printString(2, 19, "Recent Movies:");
            int row = 20;
            for (HistoryEntry entry : movieHistory) {
                Movie m = entry.getMovie();
                String summary = String.format("%s (%d) [%s] [%s]",
                        m.getTitle(),
                        m.getReleaseYear(),
                        String.join(", ", m.getGenres()),
                        entry.getConnectionReason());
                printString(2, row++, summary);
            }

            if (suggestions != null && !suggestions.isEmpty()) {
                drawBox(62, 4, 30, suggestions.size() + 2);
                printString(64, 5, "Suggestions:");
                int sRow = 6;
                for (String suggestion : suggestions) {
                    printString(64, sRow++, suggestion);
                }
            }

            if (!errorMessage.isEmpty()) {
                printString(0, screen.getTerminalSize().getRows() - 4, "[X] " + errorMessage);
            }

            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayWin(Player winner, Deque<HistoryEntry> history) {
        try {
            screen.clear();
            printString(0, 0, "[Win] Congratulations " + winner.getName() + " wins!");
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
        if (prefix == null || prefix.isEmpty() || movieTrie == null) {
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
            printString(0, row, String.format("%-" + screen.getTerminalSize().getColumns() + "s", ""));
            printString(0, row, displayText);
            screen.setCursorPosition(new TerminalPosition(displayText.length(), row));
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printString(int column, int row, String text) {
        for (int i = 0; i < text.length(); i++) {
            screen.setCharacter(column + i, row,
                new TextCharacter(text.charAt(i),
                        TextColor.ANSI.WHITE,
                        TextColor.ANSI.BLACK));
        }
    }

    private void drawBox(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            screen.setCharacter(x + i, y, new TextCharacter('-', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            screen.setCharacter(x + i, y + height - 1, new TextCharacter('-', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
        for (int j = 0; j < height; j++) {
            screen.setCharacter(x, y + j, new TextCharacter('|', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            screen.setCharacter(x + width - 1, y + j, new TextCharacter('|', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
        screen.setCharacter(x, y, new TextCharacter('+', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        screen.setCharacter(x + width - 1, y, new TextCharacter('+', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        screen.setCharacter(x, y + height - 1, new TextCharacter('+', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        screen.setCharacter(x + width - 1, y + height - 1, new TextCharacter('+', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
    }
}
