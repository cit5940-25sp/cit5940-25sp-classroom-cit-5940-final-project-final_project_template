import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MovieDate {
    private Map<Stuff> stuffs;
    private Map<Movie> movies;
    private Map<Genre> grnres;
    Reader reader;
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
