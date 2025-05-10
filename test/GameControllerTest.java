import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameControllerTest {

    @Test
    public void testGameInitialization() throws Exception {
        WinCondition winCondition1 = new SimpleWinCondition(3);
        WinCondition winCondition2 = new SimpleWinCondition(3);

        Player player1 = new Player("Player 1", winCondition1);
        Player player2 = new Player("Player 2", winCondition2);

        MovieIndex movieIndex = new MovieIndex();
        populateMovieIndexWithTestData(movieIndex);

        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        GameView gameView = new GameView(screen, movieIndex.getMovieTrie());

        GameController controller = new GameController(player1, player2, movieIndex, gameView);

        assertNotNull("Controller should not be null", controller);
        assertEquals("Player 1 should be set correctly", player1, controller.player1);
        assertEquals("Player 2 should be set correctly", player2, controller.player2);
        assertEquals("MovieIndex should be set correctly", movieIndex, controller.index);
        assertEquals("Initial round should be 0", 0, controller.round);
        assertFalse("Game should not be ended initially", isGameEnded(controller));

        screen.stopScreen(); // Clean up
    }

    @Test
    public void testStartGameSetup() throws Exception {
        WinCondition winCondition1 = new SimpleWinCondition(3);
        WinCondition winCondition2 = new SimpleWinCondition(3);
        Player player1 = new Player("Player 1", winCondition1);
        Player player2 = new Player("Player 2", winCondition2);
        MovieIndex movieIndex = new MovieIndex();
        populateMovieIndexWithTestData(movieIndex);
        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        GameView gameView = new GameView(screen, movieIndex.getMovieTrie());

        GameController controller = new GameController(player1, player2, movieIndex, gameView);

        assertNotNull("Controller should be ready to start game", controller);

        screen.stopScreen(); // Clean up
    }

    private void populateMovieIndexWithTestData(MovieIndex index) {
        Movie movie1 = new Movie("The Avengers", 2012);
        movie1.addActor("Robert Downey Jr.");
        movie1.addActor("Chris Evans");
        movie1.addDirector("Joss Whedon");

        Movie movie2 = new Movie("Iron Man", 2008);
        movie2.addActor("Robert Downey Jr.");
        movie2.addActor("Gwyneth Paltrow");
        movie2.addDirector("Jon Favreau");

        Movie movie3 = new Movie("Thor", 2011);
        movie3.addActor("Chris Hemsworth");
        movie3.addActor("Natalie Portman");
        movie3.addDirector("Kenneth Branagh");

        index.addMovie(movie1);
        index.addMovie(movie2);
        index.addMovie(movie3);
    }

    private boolean isGameEnded(GameController controller) {
        try {
            java.lang.reflect.Field field = GameController.class.getDeclaredField("gameEnded");
            field.setAccessible(true);
            return (boolean) field.get(controller);
        } catch (Exception e) {
            fail("Could not access gameEnded field: " + e.getMessage());
            return false;
        }
    }

    private static class SimpleWinCondition implements WinCondition {
        private final int requiredMovies;
        public SimpleWinCondition(int requiredMovies) {
            this.requiredMovies = requiredMovies;
        }
        @Override
        public boolean checkWin(Player player) {
            return player.getMoviesPlayed().size() >= requiredMovies;
        }
    }
}
