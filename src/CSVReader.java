import java.util.TreeSet;

public class CSVReader implements Reader{
    private ParseJson parseJson;
    private TreeSet<Movie> movies;
    /**
     * Constructs a new CSVReader object.
     * Initializes the movies TreeSet and creates a new ParseJson instance.
     */
    public CSVReader(){
        // Initialize the TreeSet to store Movie objects
        movies = new TreeSet<>();
        // Create a new instance of ParseJson for JSON parsing
        parseJson = new ParseJson();
        loadDate();
    }
s
    List<Movie> readMovie(){

    }
    List<Stuff> readStuff(){

    }
    List<Genre> readGenre(){
        
    }
}
