import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class GameTest {

    // testing all connections

    @Test
    public void testAnyConnection() {
        Movie movie1 = new Movie("Movie A");
        Movie movie2 = new Movie("Movie B");

        Person actor = new Person("Actor A", Set.of("ACTOR"));
        Person director = new Person("Director A", Set.of("DIRECTOR"));
        Person writer = new Person("Writer A", Set.of("WRITER"));
        Person composer = new Person("Composer A", Set.of("COMPOSER"));
        Person cinematographer = new Person("Cinematographer A", Set.of("CINEMATOGRAPHER"));

        movie1.addPerson(actor);
        movie2.addPerson(actor);

        movie1.addPerson(director);
        movie2.addPerson(director);

        movie1.addPerson(writer);
        movie2.addPerson(writer);

        movie1.addPerson(composer);
        movie2.addPerson(composer);

        movie1.addPerson(cinematographer);
        movie2.addPerson(cinematographer);

        Map<String, List<String>> common = ConnectionFinder.findCommonConnections(movie1, movie2);

        // Check all connection types
        assertTrue(common.containsKey("ACTOR"));
        assertTrue(common.containsKey("DIRECTOR"));
        assertTrue(common.containsKey("WRITER"));
        assertTrue(common.containsKey("COMPOSER"));
        assertTrue(common.containsKey("CINEMATOGRAPHER"));

        // Ensure each type has only one person
        assertEquals(1, common.get("ACTOR").size());
        assertEquals(1, common.get("DIRECTOR").size());
        assertEquals(1, common.get("WRITER").size());
        assertEquals(1, common.get("COMPOSER").size());
        assertEquals(1, common.get("CINEMATOGRAPHER").size());
    }

    // test for no connections

    @Test
    public void testNoConnection() {
        Movie movie1 = new Movie("Movie A");
        Movie movie2 = new Movie("Movie B");

        movie1.addPerson(new Person("Actor A", Set.of("ACTOR")));
        movie2.addPerson(new Person("Actor B", Set.of("ACTOR")));

        Map<String, List<String>> common = ConnectionFinder.findCommonConnections(movie1, movie2);
        assertTrue(common.isEmpty());
    }

    // test for repeats
    @Test
    public void testNoRepeatMovies() {
        GameState gameState = new GameState();
        String movieTitle = "Unique Movie";

        gameState.addUsedMovie(movieTitle.toLowerCase());

        assertTrue(gameState.isMovieUsed(movieTitle.toLowerCase()));
        assertFalse(gameState.isMovieUsed("Another Movie".toLowerCase()));
    }

    //
    @Test
    public void testGameFlow() {
        GameState gameState = new GameState();
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        gameState.setPlayers(player1, player2);

        GameController controller = new GameController(gameState);

        controller.processTurn("Valid Movie 1");
        assertEquals(player2, gameState.getCurrentPlayer());

        controller.processTurn("Valid Movie 2");
        assertEquals(player1, gameState.getCurrentPlayer());

        controller.processTurn("Valid Movie 1");
        assertEquals(player1, gameState.getCurrentPlayer()); // Stays player 1
    }
}

