import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Ashley Wang
 */
public class MovieTest {
    @Test
    public void testMovieConstructor() {
        String title = "Test Movie";
        int releaseYear = 2025;
        Movie movie = new Movie(title, releaseYear);

        assertEquals(title, movie.getTitle());
        assertEquals(releaseYear, movie.getReleaseYear());
        assertEquals(0, movie.getVoteCount());
        assertNotNull(movie.getGenres());
        assertNotNull(movie.getActors());
        assertNotNull(movie.getDirectors());
        assertNotNull(movie.getComposers());
        assertNotNull(movie.getWriters());
        assertNotNull(movie.getCinematographers());
    }

    @Test
    public void testSettersAndGetters() {
        Movie movie = new Movie();
        String title = "New Movie";
        int releaseYear = 2024;
        int voteCount = 100;

        Set<String> genres = new HashSet<>();
        genres.add("Action");
        Set<String> actors = new HashSet<>();
        actors.add("Actor1");
        Set<String> directors = new HashSet<>();
        directors.add("Director1");
        Set<String> composers = new HashSet<>();
        composers.add("Composer1");
        Set<String> writers = new HashSet<>();
        writers.add("Writer1");
        Set<String> cinematographers = new HashSet<>();
        cinematographers.add("Cinematographer1");

        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setVoteCount(voteCount);
        movie.setGenres(genres);
        movie.setActors(actors);
        movie.setDirectors(directors);
        movie.setComposers(composers);
        movie.setWriters(writers);
        movie.setCinematographers(cinematographers);

        assertEquals(title, movie.getTitle());
        assertEquals(releaseYear, movie.getReleaseYear());
        assertEquals(voteCount, movie.getVoteCount());
        assertEquals(genres, movie.getGenres());
        assertEquals(actors, movie.getActors());
        assertEquals(directors, movie.getDirectors());
        assertEquals(composers, movie.getComposers());
        assertEquals(writers, movie.getWriters());
        assertEquals(cinematographers, movie.getCinematographers());
    }

}
