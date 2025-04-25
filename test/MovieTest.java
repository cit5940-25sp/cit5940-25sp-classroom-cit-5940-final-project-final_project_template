import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
    public void testFindConnectionsSameActor() {
        List<Connection> connections = inception.findConnections(titanic);
        assertFalse(connections.isEmpty());

        // check if Leonardo DiCaprio is one of the connections
        boolean found = connections.stream()
                .anyMatch(c -> c.getPersonName().equals("Leonardo DiCaprio") && c.getType() == ConnectionType.ACTOR);

        assertTrue(found);
    }

    @Test
    public void testFindConnectionsSameDirector() {
        List<Connection> connections = titanic.findConnections(avatar);
        assertFalse(connections.isEmpty());

        boolean found = connections.stream()
                .anyMatch(c -> c.getPersonName().equals("James Cameron") && c.getType() == ConnectionType.DIRECTOR);

        assertTrue(found);
    }

    @Test
    public void testFindConnectionsNoCommonAttributes() {
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

        List<Connection> connections = inception.findConnections(randomMovie);
        assertTrue(connections.isEmpty());
    }
}