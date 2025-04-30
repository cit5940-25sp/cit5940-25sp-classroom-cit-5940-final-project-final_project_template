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

    // Timer variables
    private int secondsRemaining = 30;
    private boolean timerRunning = true;

    public TerminalWithSuggestions() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
    }

    public String getInputWithSuggestions(List<IMovie> movies, int timeLimitSeconds) {
        dictionary.clear();
        for (IMovie movie : movies) {
            dictionary.add(movie.getTitle());
        }
        currentInput = new StringBuilder();
        suggestions.clear();
        cursorPosition = 2;
        secondsRemaining = timeLimitSeconds;

        try {
            screen.clear();
            printString(0, 0, "> ");
            screen.setCursorPosition(new TerminalPosition(cursorPosition, 0));
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long start = System.currentTimeMillis();
        while (true) {
            try {
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
                            return currentInput.toString().trim();
                        case Escape:
                        case EOF:
                            return "";
                        default:
                            break;
                    }
                    updateScreen();
                }

                if ((System.currentTimeMillis() - start) / 1000 >= timeLimitSeconds) {
                    return ""; // timeout
                }

                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
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

            screen.setCursorPosition(new TerminalPosition(cursorPosition, 0));
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
            screen.clear();
            printString(0, 0, message);
            printString(0, 2, "> " + currentInput.toString());
            screen.setCursorPosition(new TerminalPosition(cursorPosition, 2));
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
