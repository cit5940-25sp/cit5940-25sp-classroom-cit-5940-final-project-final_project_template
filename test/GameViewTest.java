import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameViewTest {
    @Test
    public void testSetConnectedMovieTitles() throws Exception {
        Screen screen = new DefaultTerminalFactory().createScreen();
        GameView gameView = new GameView(screen);

        List<String> titles = new ArrayList<>();
        titles.add("Movie 1");
        titles.add("Movie 2");
        gameView.setConnectedMovieTitles(titles);
        assertEquals(titles, gameView.getConnectedTitles());
    }

    @Test
    public void testSetMovieTrie() throws Exception {
        Screen screen = new DefaultTerminalFactory().createScreen();
        GameView gameView = new GameView(screen);
        MovieTrie movieTrie = new MovieTrie();
        gameView.setMovieTrie(movieTrie);
        assertEquals(movieTrie, gameView.getMovieTrie());
    }

    @Test
    public void testAddToHistory() throws Exception {
        Screen screen = new DefaultTerminalFactory().createScreen();
        GameView gameView = new GameView(screen);
        Movie movie = new Movie("Avengers", 2012);
        HistoryEntry entry = new HistoryEntry(movie, "Test Reason");
        gameView.addToHistory(entry);
        assertTrue(gameView.getMovieHistory().contains(entry));
    }
}
