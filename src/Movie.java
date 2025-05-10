import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Represents a movie with attributes such as title, ID, release date, genres, and staff members.
 * Implements the Comparable interface to allow movies to be sorted based on their ID.
 */
public class Movie implements Comparable<Movie> {
    // The title of the movie
    private String title;
    // The unique identifier of the movie
    private int id;
    // The release date of the movie
    private String date;
    // A list to store the genres associated with the movie
    private List<Genre> genres;
    // A set to store the staff members associated with the movie, sorted automatically
    private TreeSet<Stuff> stuffs;

    /**
     * Constructs a new Movie object with the specified ID.
     * Initializes the title and release date to empty strings.
     *
     * @param id The unique identifier of the movie.
     */
    public Movie(int id){
        this.title = "";
        this.id = id;
        this.date = "";
    }



    /**
     * Retrieves the list of genres associated with the movie.
     *
     * @return A list containing all the genres of the movie.
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * Constructs a new Movie object with the specified title.
     * Initializes the ID to -1 and the release date to an empty string.
     *
     * @param title The title of the movie.
     */
    public Movie(String title){
        this.title = title;
        this.id = -1;
        this.date = "";
    }

    /**
     * Constructs a new Movie object with the specified title, ID, and release date.
     * Initializes the lists for genres and staff members.
     *
     * @param title The title of the movie.
     * @param id The unique identifier of the movie.
     * @param date The release date of the movie.
     */
    public Movie(String title, int id, String date){
        this.title = title.toLowerCase();
        this.id = id;
        // Set the release date of the movie
        this.date = date;
        // Initialize the list to store movie genres
        genres = new ArrayList<>();
        // Initialize the set to store movie staff, using TreeSet for automatic sorting
        stuffs = new TreeSet<>();
    }

    /**
     * Adds a staff member to the movie.
     *
     * @param stuff The staff member to be added.
     */
    public void addStuff(Stuff stuff){
        // Add the provided staff member to the set of movie staff
        stuffs.add(stuff);
    }

    /**
     * Retrieves the set of staff members associated with the movie.
     *
     * @return A TreeSet containing all the staff members of the movie.
     */
    public TreeSet<Stuff> getStuffs() {
        return stuffs;
    }

    /**
     * Retrieves the title of the movie.
     *
     * @return The title of the movie.
     */
    public String getTitle(){
        return title;
    }

    /**
     * Retrieves the unique identifier of the movie.
     *
     * @return The ID of the movie.
     */
    public int getID(){
        return id;
    }

    /**
     * Retrieves the release date of the movie.
     *
     * @return The release date of the movie.
     */
    public String getDate(){
        return date;
    }

    /**
     * Returns a string representation of the movie, including its title, release date, and genres.
     *
     * @return A string containing the movie's title, release date, and genres.
     */
    public String toString(){
        // Start building the string with the movie title and release date
        String str = "Title: " + title + "(" + date + ") ";
        str += "Genres: {";
        // Iterate through all genres and append them to the string
        for(Genre genre : genres){
            str += genre + " ";
        }
        str += "} ";
        return str;
    }

    /**
     * Adds a genre to the movie.
     *
     * @param genre The genre to be added.
     */
    public void addGenre(Genre genre){
        // Add the provided genre to the list of movie genres
        genres.add(genre);
    }

    @Override
    public int compareTo(Movie o) {
        return Integer.compare(id, o.id);
    }


}