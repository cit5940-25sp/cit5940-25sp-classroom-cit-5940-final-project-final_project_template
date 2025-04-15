import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovieIndexTest {
    @Test
    public void testAddMovieAndGetConnectedMovies() {
        MovieIndex index = new MovieIndex();

        Movie m1 = new Movie("Iron Man", 2008);
        m1.addActor("Robert Downey Jr.");
        m1.addDirector("Jon Favreau");

        Movie m2 = new Movie("Sherlock Holmes", 2009);
        m2.addActor("Robert Downey Jr.");
        m2.addDirector("Guy Ritchie");

        Movie m3 = new Movie("Avengers", 2012);
        m3.addActor("Robert Downey Jr.");
        m3.addDirector("Joss Whedon");

        Movie m4 = new Movie("Top Gun", 1986); // not connected

        index.addMovie(m1);
        index.addMovie(m2);
        index.addMovie(m3);
        index.addMovie(m4);

        Set<Movie> connected = index.getConnectedMovies(m1);
        assertTrue(connected.contains(m2));
        assertTrue(connected.contains(m3));
        assertFalse(connected.contains(m4));
    }

    //add test for getSuggestions
}