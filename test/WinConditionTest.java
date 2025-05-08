import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class WinConditionTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player("TestPlayer");
    }

    @Test
    public void testFiveHorrorMoviesWinNotEnoughMovies() {
        player.addGuessedMovie(makeHorrorMovie(1));
        player.addGuessedMovie(makeHorrorMovie(2));
        player.addGuessedMovie(makeNonHorrorMovie(3));

        TwoHorrorMoviesWin winCondition = new TwoHorrorMoviesWin();
        assertFalse("Should not win with less than 5 horror movies.",winCondition.checkVictory(player));
    }

    @Test
    public void testTwoHorrorMoviesWinSuccess() {
        player.addGuessedMovie(makeHorrorMovie(1));
        player.addGuessedMovie(makeHorrorMovie(2));

        TwoHorrorMoviesWin winCondition = new TwoHorrorMoviesWin();
        assertTrue("Should win after guessing 2 horror movies.", winCondition.checkVictory(player));
    }


    @Test
    public void testThreeNolanMoviesWinNotEnoughMovies() {
        player.addGuessedMovie(makeNolanMovie(1, "Inception"));
        player.addGuessedMovie(makeNonNolanMovie(2, "Pulp Fiction"));

        ThreeNolanMoviesWin winCondition = new ThreeNolanMoviesWin();
        assertFalse("Should not win with less than 3 Nolan movies.",winCondition.checkVictory(player));
    }

    @Test
    public void testThreeNolanMoviesWinSuccess() {
        player.addGuessedMovie(makeNolanMovie(1, "Inception"));
        player.addGuessedMovie(makeNolanMovie(2, "Interstellar"));
        player.addGuessedMovie(makeNolanMovie(3, "Dunkirk"));

        ThreeNolanMoviesWin winCondition = new ThreeNolanMoviesWin();
        assertTrue("Should win after guessing 3 Nolan movies.",winCondition.checkVictory(player));
    }

    private Movie makeHorrorMovie(long id) {
        return new Movie(id, "HorrorMovie" + id, 2000,
                Set.of("Horror"),  // genres
                new HashSet<>(),   // actors
                new HashSet<>(),   // directors
                new HashSet<>(),   // writers
                new HashSet<>(),   // composers
                new HashSet<>());  // cinematographers
    }

    private Movie makeNonHorrorMovie(long id) {
        return new Movie(id, "DramaMovie" + id, 2000,
                Set.of("Drama"),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    private Movie makeNolanMovie(long id, String title) {
        return new Movie(id, title, 2010,
                new HashSet<>(),
                new HashSet<>(),
                Set.of("Christopher Nolan"),  // directors
                new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    private Movie makeNonNolanMovie(long id, String title) {
        return new Movie(id, title, 2010,
                new HashSet<>(),
                new HashSet<>(),
                Set.of("Quentin Tarantino"),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

}
