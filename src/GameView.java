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
    private List<String> suggestions = new ArrayList<>();
    private int secondsRemaining = 30;
    private Timer timer;
    private List<String> connectedTitles = new ArrayList<>();

    public void setConnectedMovieTitles(List<String> titles) {
        this.connectedTitles = titles;
    }

    public void setMovieTrie(MovieTrie trie) {
        this.movieTrie = trie;
    }

    public GameView(Screen screen) {
        this.screen = screen;
    }

    public void displayGameState(Player player1, Player player2, Movie currentMovie, int round, String errorMessage) {
        try {
            screen.clear();

            drawBox(0, 0, 70, 3);
            printString(2, 1, "Round: " + round);
            printString(60, 1, "Time: " + secondsRemaining + "s");

            drawBox(0, 4, 70, 3);
            printString(2, 5, "[Movie] Current: " + currentMovie.getTitle() + " (" + currentMovie.getReleaseYear() + ")");
            printString(2, 6, "Genres: " + String.join(", ", currentMovie.getGenres()));

            drawBox(0, 8, 70, 4);
            printString(2, 9, "[P1] Player 1: " + player1.getName());
            printString(2, 10, "Movies: " + player1.getMoviesPlayed().size());
            printString(2, 11, "Win: " + player1.getWinCondition().getDescription());

            drawBox(0, 13, 70, 4);
            printString(2, 14, "[P2] Player 2: " + player2.getName());
            printString(2, 15, "Movies: " + player2.getMoviesPlayed().size());
            printString(2, 16, "Win: " + player2.getWinCondition().getDescription());

            drawBox(0, 18, 70, maxHistory + 2);
            printString(2, 19, "Recent Movies:");
            int row = 20;
            for (HistoryEntry entry : movieHistory) {
                Movie m = entry.getMovie();
                String summary = String.format("%s (%d) [%s] [%s]",
                    m.getTitle(),
                    m.getReleaseYear(),
                    String.join(", ", m.getGenres()),
                    entry.getConnectionReason());
                if (summary.length() > 70) {
                    summary = summary.substring(0, 67) + "…";
                }
                printString(2, row++, summary);
            }

            if (suggestions != null && !suggestions.isEmpty()) {
                int maxToShow = Math.min(10, suggestions.size());
                int suggestionBoxHeight = maxToShow + 3;

                drawBox(72, 0, 50, suggestionBoxHeight);
                printString(74, 1, "Suggestions:");
                int sRow = 2;

                for (int i = 0; i < maxToShow; i++) {
                    String suggestion = suggestions.get(i);
                    if (suggestion.length() > 46) {
                        suggestion = suggestion.substring(0, 46) + "…";
                    }
                    printString(74, sRow++, suggestion);
                }
            }

            if (connectedTitles != null && !connectedTitles.isEmpty()) {
                int connectedStartY = Math.max(2 + suggestions.size() + 2, 10);
                int maxDisplay = 10;
                int boxHeight = Math.min(connectedTitles.size(), maxDisplay) + 2;
                drawBox(72, connectedStartY, 50, boxHeight);
                printString(74, connectedStartY + 1, "Connected:");
                int cRow = connectedStartY + 2;

                for (int i = 0; i < Math.min(connectedTitles.size(), maxDisplay); i++) {
                    String connected = connectedTitles.get(i);
                    if (connected.length() > 46) {
                        connected = connected.substring(0, 46) + "…";
                    }
                    printString(74, cRow++, connected);
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
            for (HistoryEntry entry : movieHistory) {
                Movie m = entry.getMovie();
                String summary = String.format("%s (%d) [%s] [%s]",
                        m.getTitle(),
                        m.getReleaseYear(),
                        String.join(", ", m.getGenres()),
                        entry.getConnectionReason());

                if (summary.length() > 66) {
                    summary = summary.substring(0, 65) + "…";
                }

                printString(2, row++, summary);
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
