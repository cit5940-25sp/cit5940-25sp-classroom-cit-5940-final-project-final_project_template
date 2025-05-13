// Finalized TerminalWithSuggestions.java with minimal changes for integration
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;
import java.util.*;

public class TerminalWithSuggestions {
    private Terminal terminal;
    private Screen screen;
    private List<String> dictionary = new ArrayList<>();
    private StringBuilder currentInput = new StringBuilder();
    private List<String> suggestions = new ArrayList<>();
    private int cursorPosition = 0;
    private int currentRow = 0;
    private String currentMovieTitle = "";
    private String currentMovieGenres = "";
    private int secondsRemaining = 30;

    public TerminalWithSuggestions() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
    }

    public String getInputWithSuggestions(List<IMovie> movies, IMovie currentMovie, int timeLimitSeconds) throws IOException {
        dictionary.clear();
        for (IMovie movie : movies) {
            dictionary.add(movie.getTitle());
        }

        currentInput = new StringBuilder();
        suggestions.clear();
        cursorPosition = 2;
        secondsRemaining = timeLimitSeconds;
        currentMovieTitle = currentMovie.getTitle();
        currentMovieGenres = currentMovie.getGenres().toString();

        try {
            screen.clear();
            printString(0, 0, "Current Movie: " + currentMovieTitle);
            printString(0, 1, "Genres: " + currentMovieGenres);
            printString(0, 2, "> ");
            screen.setCursorPosition(new TerminalPosition(cursorPosition, 2));
            screen.refresh();
            updateScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long startMillis = System.currentTimeMillis();
        int timeLimitMillis = timeLimitSeconds * 1000;
        int lastDisplayedSecond = -1;
        boolean screenNeedsRefresh = true;

        while (true) {
            long now = System.currentTimeMillis();
            int elapsedMillis = (int) (now - startMillis);
            int remainingSeconds = Math.max(0, (timeLimitMillis - elapsedMillis + 999) / 1000); // Round up
            secondsRemaining = remainingSeconds;

            KeyStroke keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case Character:
                        handleCharacter(keyStroke.getCharacter());
                        screenNeedsRefresh = true;
                        break;
                    case Backspace:
                        handleBackspace();
                        screenNeedsRefresh = true;
                        break;
                    case Enter:
                        String finalInput = currentInput.toString().trim();
                        if (!finalInput.isEmpty()) {
                            if (dictionary.stream().anyMatch(title -> title.equalsIgnoreCase(finalInput))) {
                                return finalInput;
                            } else {
                                displayMessage("Movie not found. Try again.");
                                currentInput.setLength(0);
                                cursorPosition = 2;
                                updateSuggestions();
                                screenNeedsRefresh = true;
                            }
                        }
                        break;
                    case Escape:
                    case EOF:
                        return ""; // user exits
                }
            }

            if (remainingSeconds != lastDisplayedSecond || screenNeedsRefresh) {
                updateScreen();
                lastDisplayedSecond = remainingSeconds;
                screenNeedsRefresh = false;
            }

            if (remainingSeconds <= 0) {
                return "";
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleCharacter(char c) {
        currentInput.insert(cursorPosition - 2, c);
        cursorPosition++;
        updateSuggestions();
    }

    private void handleBackspace() {
        if (cursorPosition > 2) {
            currentInput.deleteCharAt(cursorPosition - 3);
            cursorPosition--;
            updateSuggestions();
        }
    }

    private void updateSuggestions() {
        suggestions.clear();
        String prefix = currentInput.toString();
        if (!prefix.isEmpty()) {
            for (String word : dictionary) {
                if (word.toLowerCase().startsWith(prefix.toLowerCase()) && suggestions.size() < 5) {
                    suggestions.add(word);
                }
            }
        }
    }

    private void updateScreen() throws IOException {
        synchronized (screen) {
            screen.clear();

            // Top row: Movie title (left) and timer (right)
            printString(0, 0, "Current Movie: " + currentMovieTitle);
            String timerText = "Time: " + secondsRemaining + "s";
            TerminalSize size = screen.getTerminalSize();
            printString(size.getColumns() - timerText.length(), 0, timerText);

            // Row 1: Genres
            printString(0, 1, "Genres: " + currentMovieGenres);

            // Row 2: Input prompt
            printString(0, 2, "> " + currentInput.toString());

            // Row 3+: Suggestions
            int row = 3;
            for (String suggestion : suggestions) {
                printString(2, row++, suggestion);
            }

            screen.setCursorPosition(new TerminalPosition(cursorPosition, 2)); // Input line
            screen.refresh();
        }
    }

    private void printString(int column, int row, String text) {
        for (int i = 0; i < text.length(); i++) {
            screen.setCharacter(column + i, row,
                    new TextCharacter(text.charAt(i), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
    }

    public void displayMessage(String message) {
        try {
            printString(0, currentRow++, message);
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clearScreen() {
        try {
            screen.clear();
            currentRow = 0;
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }
}
