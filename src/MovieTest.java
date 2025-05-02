import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Movie;
import models.Tuple;

public class MovieTest {
    private Autocomplete autocomplete;
    private MovieIndexer indexer;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        
        autocomplete = new Autocomplete();
        
        indexer = new MovieIndexer(autocomplete);
        
        Set<String> genres1 = new HashSet<>();
        genres1.add("Action");
        genres1.add("Adventure");
        genres1.add("Sci-Fi");

        Set<String> genres2 = new HashSet<>();
        genres2.add("Drama");
        genres2.add("Crime");

        Set<String> genres3 = new HashSet<>();
        genres3.add("Action");
        genres3.add("Thriller");

        List<Tuple<String, Integer>> cast1 = new ArrayList<>();
        cast1.add(new Tuple<>("Tom Cruise", 1));
        cast1.add(new Tuple<>("Emily Blunt", 2));

        List<Tuple<String, Integer>> crew1 = new ArrayList<>();
        crew1.add(new Tuple<>("Christopher McQuarrie", 101));
        crew1.add(new Tuple<>("Tom Cruise", 102));

        List<Tuple<String, Integer>> cast2 = new ArrayList<>();
        cast2.add(new Tuple<>("Al Pacino", 3));
        cast2.add(new Tuple<>("Robert De Niro", 4));

        List<Tuple<String, Integer>> crew2 = new ArrayList<>();
        crew2.add(new Tuple<>("Martin Scorsese", 103));
        crew2.add(new Tuple<>("Michael Mann", 104));

        Movie movie1 = new Movie("Mission: Impossible - Fallout", 1, 2018, genres1, cast1, crew1);
        Movie movie2 = new Movie("The Irishman", 2, 2019, genres2, cast2, crew2);
        Movie movie3 = new Movie("The Dark Knight", 3, 2008, genres3, cast1, crew1);

        autocomplete.insert(movie1);
        autocomplete.insert(movie2);
        autocomplete.insert(movie3);
    }
    
    @After
    public void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testAutocompleteSearchThe() {
        List<Movie> results = autocomplete.search("the");
        
        assertFalse("搜索'the'应该返回结果", results.isEmpty());
        
        boolean foundDarkKnight = false;
        boolean foundIrishman = false;
        
        for (Movie movie : results) {
            if (movie.getTitle().equals("The Dark Knight")) {
                foundDarkKnight = true;
            } else if (movie.getTitle().equals("The Irishman")) {
                foundIrishman = true;
            }
        }
        
        assertTrue("结果应包含'The Dark Knight'", foundDarkKnight);
        assertTrue("结果应包含'The Irishman'", foundIrishman);
    }
    
    @Test
    public void testAutocompleteSearchMission() {
        List<Movie> results = autocomplete.search("mission");
        
        assertFalse("搜索'mission'应该返回结果", results.isEmpty());
        
        boolean foundMission = false;
        
        for (Movie movie : results) {
            if (movie.getTitle().equals("Mission: Impossible - Fallout")) {
                foundMission = true;
            }
        }
        
        assertTrue("结果应包含'Mission: Impossible - Fallout'", foundMission);
    }
    
    @Test
    public void testAutocompleteSearchNonExistent() {
        List<Movie> results = autocomplete.search("xyz");
        
        assertTrue("搜索'xyz'应该返回空结果", results.isEmpty());
    }
    
    @Test
    public void testMovieIndexerSearchThe() {
        List<Movie> results = indexer.searchMovies("the", 10);

        assertFalse("搜索'the'应该返回结果", results.isEmpty());
        
        assertTrue("结果数量应该至少为2", results.size() >= 2);
        
        boolean foundDarkKnight = false;
        boolean foundIrishman = false;
        
        for (Movie movie : results) {
            if (movie.getTitle().equals("The Dark Knight")) {
                foundDarkKnight = true;
            } else if (movie.getTitle().equals("The Irishman")) {
                foundIrishman = true;
            }
        }
        
        assertTrue("结果应包含'The Dark Knight'", foundDarkKnight);
        assertTrue("结果应包含'The Irishman'", foundIrishman);
    }
    

    @Test
    public void testMovieIndexerSearchMission() {
        List<Movie> results = indexer.searchMovies("mission");
        
        assertFalse("搜索'mission'应该返回结果", results.isEmpty());
        
        boolean foundMission = false;
        
        for (Movie movie : results) {
            if (movie.getTitle().equals("Mission: Impossible - Fallout")) {
                foundMission = true;
            }
        }
        
        assertTrue("结果应包含'Mission: Impossible - Fallout'", foundMission);
    }
    
    @Test
    public void testMovieIndexerLoadCSV() {
        MovieIndexer indexer = new MovieIndexer();
        String csvPath = "src/movies.csv";

        indexer.loadMoviesFromCSV(csvPath);

        List<Movie> results = indexer.searchMovies("Prince of Persia", 10);
        assertFalse("搜索'Prince of Persia'应该返回结果", results.isEmpty());
        
        assertEquals("结果数量应该为1", 1, results.size());
        
        assertEquals("结果应为'Prince of Persia: The Sands of Time'", 
                   "Prince of Persia: The Sands of Time", results.get(0).getTitle());
        assertEquals("电影年份应为2010", 2010, results.get(0).getReleaseYear());
    }
}