import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerTest {

    private Player player;
    private Movie movie1;
    private Movie movie2;

    @Before
    public void setUp() {
        player = new Player("Yo");
        movie1 = new Movie(
                1L,
                "Inception",
                2010,
                new HashSet<>(Arrays.asList("Sci-Fi")),
                new HashSet<>(Arrays.asList("Leonardo DiCaprio")),
                new HashSet<>(Arrays.asList("Christopher Nolan")),
                new HashSet<>(Arrays.asList("Jonathan Nolan")),
                new HashSet<>(Arrays.asList("Wally Pfister")),
                new HashSet<>(Arrays.asList("Hans Zimmer"))
        );

        movie2 = new Movie(
                2L,
                "Titanic",
                1997,
                new HashSet<>(Arrays.asList("Romance")),
                new HashSet<>(Arrays.asList("Leonardo DiCaprio", "Kate Winslet")),
                new HashSet<>(Arrays.asList("James Cameron")),
                new HashSet<>(Arrays.asList("James Cameron")),
                new HashSet<>(Arrays.asList("Russell Carpenter")),
                new HashSet<>(Arrays.asList("James Horner"))
        );
    }

    @Test
    public void testPlayerName() {
        assertEquals("Yo", player.getName());
    }

    @Test
    public void testAddGuessedMovie() {
        player.addGuessedMovie(movie1);
        Set<Movie> guessed = player.getMoviesGuessed();
        assertTrue(guessed.contains(movie1));
        assertEquals(1, player.getNumMoviesGuessed());
    }

    @Test
    public void testAddMultipleMovies() {
        player.addGuessedMovie(movie1);
        player.addGuessedMovie(movie2);
        Set<Movie> guessed = player.getMoviesGuessed();
        assertTrue(guessed.contains(movie1));
        assertTrue(guessed.contains(movie2));
        assertEquals(2, player.getNumMoviesGuessed());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMoviesGuessedIsUnmodifiable() {
        player.addGuessedMovie(movie1);
        Set<Movie> guessed = player.getMoviesGuessed();
        guessed.add(movie2);
    }
}
