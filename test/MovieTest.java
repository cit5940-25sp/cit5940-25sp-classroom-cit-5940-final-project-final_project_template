import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class MovieTest {

    private Movie inception;
    private Movie titanic;
    private Movie avatar;

    @Before
    public void setUp() {
        inception = new Movie(
                1L,
                "Inception",
                2010,
                new HashSet<>(Arrays.asList("Sci-Fi")),
                new HashSet<>(Arrays.asList("Leonardo DiCaprio")),
                new HashSet<>(Arrays.asList("Christopher Nolan")),
                new HashSet<>(Arrays.asList("Jonathan Nolan")),
                new HashSet<>(Arrays.asList("Hans Zimmer")),
                new HashSet<>(Arrays.asList("Wally Pfister"))
        );

        titanic = new Movie(
                2L,
                "Titanic",
                1997,
                new HashSet<>(Arrays.asList("Romance")),
                new HashSet<>(Arrays.asList("Leonardo DiCaprio", "Kate Winslet")),
                new HashSet<>(Arrays.asList("James Cameron")),
                new HashSet<>(Arrays.asList("James Cameron")),
                new HashSet<>(Arrays.asList("James Horner")),
                new HashSet<>(Arrays.asList("Russell Carpenter"))
        );

        avatar = new Movie(
                3L,
                "Avatar",
                2009,
                new HashSet<>(Arrays.asList("Sci-Fi")),
                new HashSet<>(Arrays.asList("Sam Worthington")),
                new HashSet<>(Arrays.asList("James Cameron")),
                new HashSet<>(Arrays.asList("James Cameron")),
                new HashSet<>(Arrays.asList("James Horner")),
                new HashSet<>(Arrays.asList("Mauro Fiore"))
        );
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(1L, inception.getMovieId());
        assertEquals("Inception", inception.getTitle());
        assertEquals(2010, inception.getYear());
        assertTrue(inception.getGenres().contains("Sci-Fi"));
        assertTrue(inception.getActors().contains("Leonardo DiCaprio"));
        assertTrue(inception.getDirectors().contains("Christopher Nolan"));
        assertTrue(inception.getWriters().contains("Jonathan Nolan"));
        assertTrue(inception.getComposers().contains("Hans Zimmer"));
        assertTrue(inception.getCinematographers().contains("Wally Pfister"));
    }

    @Test
    public void testSharesAttributeWithSameActor() {
        assertTrue(inception.sharesAttributeWith(titanic));
    }

    @Test
    public void testSharesAttributeWithSameDirector() {
        assertTrue(titanic.sharesAttributeWith(avatar));
    }

    @Test
    public void testSharesAttributeWith_sameGenre() {
        assertTrue(inception.sharesAttributeWith(avatar));
    }

    @Test
    public void testSharesAttributeWithNoCommonAttributes() {
        Movie randomMovie = new Movie(
                4L,
                "Random Movie",
                2022,
                new HashSet<>(Arrays.asList("Action")),
                new HashSet<>(Arrays.asList("Random Actor")),
                new HashSet<>(Arrays.asList("Random Director")),
                new HashSet<>(Arrays.asList("Random Writer")),
                new HashSet<>(Arrays.asList("Random Composer")),
                new HashSet<>(Arrays.asList("Random Cinematographer"))
        );
        assertFalse(inception.sharesAttributeWith(randomMovie));
    }
}