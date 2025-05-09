import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameConsoleUITest {

    private CountryLanguageManager dataService;
    private GameEngine gameEngine;

    @BeforeEach
    public void setUp() {
        GameEngine.resetInstance();
        dataService = new CountryLanguageManager();
        Language english = new Language("English", 5);
        Language spanish = new Language("Spanish", 3);

        dataService.addLanguage("English", 5);
        dataService.addLanguage("Spanish", 3);

        dataService.addCountry("USA", "English");
        dataService.addCountry("Spain", "Spanish", "English");

        gameEngine = GameEngine.getInstance(dataService);
    }

    @Test
    public void testStartNormalModeThenQuit() {
        // user input: normal mode, pick language 1, then enter 'quit'
        String simulatedInput = "n\n1\nquit\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        GameConsoleUI ui = new GameConsoleUI(gameEngine, dataService);
        ui.start();

        System.setOut(originalOut);
        System.setIn(System.in);

        String output = outContent.toString();

        assertTrue(output.contains("Normal mode selected"));
        assertTrue(output.contains("Thanks for playing"));
    }
}
