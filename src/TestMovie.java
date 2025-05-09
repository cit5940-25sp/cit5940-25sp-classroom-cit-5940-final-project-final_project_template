import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestMovie {
    @Test
    public void testGetTitle() {
        Movie movie = new Movie("Inception", 2010, List.of("Action", "Sci-Fi"));
        assertEquals("Inception", movie.getTitle());
    }

    @Test
    public void testGetYear() {
        Movie movie = new Movie("Inception", 2010, List.of("Action", "Sci-Fi"));
        assertEquals(2010, movie.getYear());
    }

    @Test
    public void testGetGenres() {
        Movie movie = new Movie("Inception", 2010, List.of("Action", "Sci-Fi"));
        assertEquals(List.of("Action", "Sci-Fi"), movie.getGenres());
    }

    @Test
    public void testAddActor() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addActor("Leonardo DiCaprio");
        assertEquals(List.of("Leonardo DiCaprio"), movie.getActors());
        assertTrue(movie.getAllContributors().contains("Leonardo DiCaprio"));
    }

    @Test
    public void testGetActors() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addActor("Leonardo DiCaprio");
        assertTrue(movie.getActors().contains("Leonardo DiCaprio"));
    }

    @Test
    public void testAddCrew() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addCrew("Chris Nolan");
        assertEquals(List.of("Chris Nolan"), movie.getCrew());
        assertTrue(movie.getAllContributors().contains("Chris Nolan"));
    }

    @Test
    public void testGetCrew() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addCrew("Chris Nolan");
        assertTrue(movie.getCrew().contains("Chris Nolan"));
    }

    @Test
    public void testAddDirector() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addDirector("Christopher Nolan");
        assertEquals(List.of("Christopher Nolan"), movie.getDirectors());
        assertTrue(movie.getAllContributors().contains("Christopher Nolan"));
    }

    @Test
    public void testGetDirectors() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addDirector("Christopher Nolan");
        assertTrue(movie.getDirectors().contains("Christopher Nolan"));
    }

    @Test
    public void testAddWriter() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addWriter("Jonathan Nolan");
        assertEquals(List.of("Jonathan Nolan"), movie.getWriters());
        assertTrue(movie.getAllContributors().contains("Jonathan Nolan"));
    }

    @Test
    public void testGetWriters() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addWriter("Jonathan Nolan");
        assertTrue(movie.getWriters().contains("Jonathan Nolan"));
    }

    @Test
    public void testAddComposer() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addComposer("Franz Schubert");
        assertEquals(List.of("Franz Schubert"), movie.getComposers());
        assertTrue(movie.getAllContributors().contains("Franz Schubert"));
    }

    @Test
    public void testGetComposers() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addComposer("Robert Schumann");
        assertTrue(movie.getComposers().contains("Robert Schumann"));
    }

    @Test
    public void testAddCinematographer() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addCinematographer("Wally Pfister");
        assertEquals(List.of("Wally Pfister"), movie.getCinematographers());
        assertTrue(movie.getAllContributors().contains("Wally Pfister"));
    }

    @Test
    public void testGetCinematographers() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addCinematographer("Wally Pfister");
        assertTrue(movie.getCinematographers().contains("Wally Pfister"));
    }

    @Test
    public void testAddContributor() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addContributor("Someone Special");
        assertTrue(movie.getAllContributors().contains("Someone Special"));
    }

    @Test
    public void testGetAllContributors() {
        Movie movie = new Movie("Test", 2020, List.of("Drama"));
        movie.addActor("A");
        movie.addCrew("B");
        movie.addContributor("C");
        Set<String> contributors = movie.getAllContributors();
        assertTrue(contributors.contains("A"));
        assertTrue(contributors.contains("B"));
        assertTrue(contributors.contains("C"));
    }

}
