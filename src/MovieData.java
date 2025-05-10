import java.util.Set;
import java.util.TreeSet;

/**
 * MovieDate is a class designed to manage a collection of Movie objects.
 * It loads movie data using a reader and provides a method to retrieve a movie by its title.
 */
public class MovieData {
    // A set to store all movie objects
    private Set<Movie> movies;
    // A TreeSet to store movies sorted by their titles
    private TreeSet<Movie> movieByTitle;
    // A reader object used to read movie data
    private Reader reader;
    private TreeSet<Stuff> stuffsByName;

    /**
     * Constructs a new MovieDate object.
     * Initializes the reader, loads movie data, and populates the sorted movie set.
     */
    public MovieData() {
        // Initialize the reader as a CSVReader instance
        reader = new CSVReader();
        // Read all movie objects using the reader
        movies = reader.readMovies();
        TreeSet<Stuff> stuffs = reader.readStuffs();
        // Initialize the TreeSet with a comparator to sort movies by title
        movieByTitle = new TreeSet<>((m1, m2) ->
                m1.getTitle().toLowerCase().compareTo(m2.getTitle().toLowerCase()));
        // Add all movies to the sorted TreeSet
        for (Movie movie : movies) {
            movieByTitle.add(movie);
        }
        stuffsByName = new TreeSet<>((m1,m2) -> m1.getName().toLowerCase().compareTo(m2.getName().toLowerCase()));
        for (Stuff stuff : stuffs) {
            stuffsByName.add(stuff);
        }
    }

    public Stuff getStuffByName(String name){
        Stuff stuff = new Stuff(name);
        if(stuffsByName.contains(stuff)){
            return stuffsByName.floor(stuff);
        }
        return null;
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
        if (movieByTitle.contains(movie)) {
            // Return the greatest movie in the set less than or equal to the given movie
            return movieByTitle.floor(movie);
        }
        // Return null if no movie is found
        return null;
    }

    public boolean contains(String movieTitle){
        return movieByTitle.contains(new Movie(movieTitle));
    }

    public Movie getMovieByTitle(String movieTitle){
        return movieByTitle.floor(new Movie(movieTitle));
    }

    public Set<Movie> getMovies() {
        return movieByTitle;
    }
}
