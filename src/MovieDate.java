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

    /**
     * Constructs a new MovieDate object.
     * Initializes the reader, loads movie data, and populates the sorted movie set.
     */
    public MovieDate() {
        // Initialize the reader as a CSVReader instance
        reader = new CSVReader();
        // Read all movie objects using the reader
        movies = reader.readMovies();
        // Initialize the TreeSet with a comparator to sort movies by title
        movieByTitile = new TreeSet<>((m1, m2) -> m1.getTitle().compareTo(m2.getTitle()));
        // Add all movies to the sorted TreeSet
        for (Movie movie : movies) {
            movieByTitile.add(movie);
        }
    }

    /**
     * Retrieves a movie by its title.
     *
     * @param title The title of the movie to retrieve.
     * @return The movie object if found, null otherwise.
     */
    public Movie getMovie(String title) {
        // Create a dummy movie object with the given title
        Movie movie = new Movie(title);
        // Check if the sorted movie set contains a movie with the given title
        if (movieByTitile.contains(movie)) {
            // Return the greatest movie in the set less than or equal to the given movie
            return movieByTitile.floor(movie);
        }
        // Return null if no movie is found
        return null;
    }

    public boolean contains(String movieTitle){
        return movieByTitile.contains(new Movie(movieTitle));
    }

    public Movie getMovieByTitle(String movieTitle){
        return movieByTitile.floor(new Movie(movieTitle));
    }

    public Set<Movie> getMovies() {
        return movieByTitile;
    }
}
*