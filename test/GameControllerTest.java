import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class GameControllerTest {

    @Test
    public void testGameInitialization() {
        // Create win conditions
        WinCondition winCondition1 = new SimpleWinCondition(3); // Simple condition: collect 3 movies
        WinCondition winCondition2 = new SimpleWinCondition(3);

        // Create two players with win conditions
        Player player1 = new Player("Player 1", winCondition1);
        Player player2 = new Player("Player 2", winCondition2);

        // Create movie index and populate with test data
        MovieIndex movieIndex = new MovieIndex();
        populateMovieIndexWithTestData(movieIndex);

        // Create game view
        GameView gameView = new GameView();

        // Create game controller
        GameController controller = new GameController(player1, player2, movieIndex, gameView);

        // Verify initial state
        assertNotNull("Controller should not be null", controller);
        assertEquals("Player 1 should be set correctly", player1, controller.player1);
        assertEquals("Player 2 should be set correctly", player2, controller.player2);
        assertEquals("MovieIndex should be set correctly", movieIndex, controller.index);
        assertEquals("Initial round should be 0", 0, controller.round);
        assertFalse("Game should not be ended initially", isGameEnded(controller));
    }

    @Test
    public void testStartGame() {
        // Setup similar to previous test
        WinCondition winCondition1 = new SimpleWinCondition(3);
        WinCondition winCondition2 = new SimpleWinCondition(3);
        Player player1 = new Player("Player 1", winCondition1);
        Player player2 = new Player("Player 2", winCondition2);
        MovieIndex movieIndex = new MovieIndex();
        populateMovieIndexWithTestData(movieIndex);
        GameView gameView = new GameView();

        GameController controller = new GameController(player1, player2, movieIndex, gameView);

        // Since startGame interacts with System.in, we won't call it directly
        // but we can verify that setup is correct for starting a game
        assertNotNull("Controller should be ready to start game", controller);

        // This is where we would test specific game mechanics if we mocked System.in
    }

    // Helper method to populate MovieIndex with test data
    private void populateMovieIndexWithTestData(MovieIndex index) {
        // Create some test movies
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

        // Add movies to index
        index.addMovie(movie1);
        index.addMovie(movie2);
        index.addMovie(movie3);
    }

    // Helper method to check if game is ended (since the field is private)
    private boolean isGameEnded(GameController controller) {
        // We're using reflection to access the private field
        try {
            java.lang.reflect.Field field = GameController.class.getDeclaredField("gameEnded");
            field.setAccessible(true);
            return (boolean) field.get(controller);
        } catch (Exception e) {
            fail("Could not access gameEnded field: " + e.getMessage());
            return false;
        }
    }

    // Simple win condition implementation for testing
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

    public static void main(String[] args) {
        WinCondition winCondition1 = new GenreWinCondition("Action");
        WinCondition winCondition2 = new GenreWinCondition("Action");

        Player player1 = new Player("Alice", winCondition1);
        Player player2 = new Player("Bob", winCondition2);

        MovieIndex movieIndex = new MovieIndex();

        GameView view = new GameView();

        // 5. 创建控制器并启动游戏
        GameController controller = new GameController(player1, player2, movieIndex, view);
        controller.startGame();
    }

}
