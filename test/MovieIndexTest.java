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

    @Test
    public void testGetSuggestions() {
        MovieIndex index = new MovieIndex();

        Movie m1 = new Movie("Iron Man", 2008);
        Movie m2 = new Movie("Iron Giant", 1999);
        Movie m3 = new Movie("Inside Out", 2015);
        Movie m4 = new Movie("Avatar", 2009);

        index.addMovie(m1);
        index.addMovie(m2);
        index.addMovie(m3);
        index.addMovie(m4);

        index.getMovieTrie().insert(index.getMovieTrie().getNormalizedString(m1.getTitle()), m1);
        index.getMovieTrie().insert(index.getMovieTrie().getNormalizedString(m2.getTitle()), m2);
        index.getMovieTrie().insert(index.getMovieTrie().getNormalizedString(m3.getTitle()), m3);
        index.getMovieTrie().insert(index.getMovieTrie().getNormalizedString(m4.getTitle()), m4);


        List<String> suggestions = index.getSuggestions("Iron");
        assertTrue(suggestions.contains("Iron Man"));
        assertTrue(suggestions.contains("Iron Giant"));

        List<String> suggestions2 = index.getSuggestions("Ins");
        assertTrue(suggestions2.contains("Inside Out"));

        List<String> suggestions3 = index.getSuggestions("Ava");
        assertTrue(suggestions3.contains("Avatar"));

        List<String> suggestions4 = index.getSuggestions("xyz");
        assertTrue(suggestions4.isEmpty());

        List<String> suggestions5 = index.getSuggestions("The Broadway Melody");
        for (String suggestion : suggestions5) {
            assertTrue(suggestion.contains("The Broadway Melody"));
        }
    }

}