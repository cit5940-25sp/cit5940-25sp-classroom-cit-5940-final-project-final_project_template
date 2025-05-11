package view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GameView {
    private MultiWindowTextGUI gui;
    private BasicWindow window;
    private Panel mainPanel;
    private Label gameStateLabel;
    private Label timerLabel;
    private Label errorLabel;
    private TextBox inputBox;
    private Panel suggestionsPanel;
    private AtomicReference<String> asyncInputRef = new AtomicReference<>(null);

    public GameView() {
        try {
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();

            gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
            window = new BasicWindow("Movie Name Game");

            mainPanel = new Panel();
            mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            gameStateLabel = new Label("Welcome to the Movie Name Game!");
            timerLabel = new Label("Time left: 30");
            errorLabel = new Label("").setForegroundColor(TextColor.ANSI.RED);

            inputBox = new TextBox(new TerminalSize(40, 1));
            inputBox.setValidationPattern("[a-zA-Z0-9\\s:\\-']*");

            suggestionsPanel = new Panel();
            suggestionsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            mainPanel.addComponent(gameStateLabel);
            mainPanel.addComponent(timerLabel);
            mainPanel.addComponent(errorLabel);
            mainPanel.addComponent(inputBox);
            mainPanel.addComponent(new Label("Suggestions:"));
            mainPanel.addComponent(suggestionsPanel);

            window.setComponent(mainPanel);
            gui.addWindow(window);
            gui.setActiveWindow(window);

        } catch (IOException e) {
            System.err.println("Error initializing Lanterna TUI: " + e.getMessage());
        }
    }
}