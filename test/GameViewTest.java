import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class GameViewTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private GameView view;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        view = new GameView();
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testDisplayInfo() {
        view.displayInfo("Test message");
        String output = outContent.toString();
        assertTrue(output.contains("[INFO] Test message"));
    }

    @Test
    public void testShowAutocomplete() {
        view.showAutocomplete(List.of("Inception", "Interstellar"));
        String output = outContent.toString();
        assertTrue(output.contains("Did you mean:"));
        assertTrue(output.contains("Inception"));
        assertTrue(output.contains("Interstellar"));
    }

    @Test
    public void testRender() {
        Player player = new Player("TestPlayer");
        Movie movie1 = new Movie(1L, "The Matrix", 1999, Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of());
        Movie movie2 = new Movie(2L, "John Wick", 2014, Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of());

        GameState state = new GameState(player, new Player("Other"), new TwoHorrorMoviesWin(), movie1);
        state.addMovieToHistory(movie2);

        view.render(state);
        String output = outContent.toString();
        assertTrue(output.contains("Current Round:"));
        assertTrue(output.contains("TestPlayer"));
        assertTrue(output.contains("The Matrix"));
        assertTrue(output.contains("John Wick"));
    }
}
