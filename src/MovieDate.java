import java.util.Set;
import java.util.TreeSet;


/**
 * MovieDate is a class designed to manage a collection of Movie objects.
 * It loads movie data using a reader and provides a method to retrieve a movie by its title.
 */
public class MovieDate {
    // A set to store all movie objects
    private Set<Movie> movies;
    // A TreeSet to store movies sorted by their titles
    private TreeSet<Movie> movieByTitile;
    // A reader object used to read movie data
    private Reader reader;

    public MovieDate() {
        stuffs = new HashMap<>();
        movies = new HashMap<>();
        grnres = new HashMap<>(); 
    }

    public void addStuff(Stuff stuff) {
        stuffs.put(stuff); 
    }
    public void addMovie(Movie movie) {
        movies.put(movie); 
    }
    public void addGenre(Genre genre) {
        grnres.put(genre); 
    }
    public Stuff getStuff(String name) {
        return null;
    }
    public Movie getMovie(String title) {
        return null;
    }
}
