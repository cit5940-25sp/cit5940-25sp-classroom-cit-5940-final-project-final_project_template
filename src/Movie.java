import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Movie implements Comparable<Movie> {
    private String title;
    private int id;
    private String date;
    private List<Genre> genres;
    private TreeSet<Stuff> stuffs;
    public Movie(int id){
        this.title = "";
        this.id = id;
        this.date = "";
    }
    public List<Genre> getGenres() {
        return genres;
    }

    public Movie(String title){
        this.title = title;
        this.id = -1;
        this.date = "";
    }
    public Movie(String title, int id, String date){
        this.title = title;
        this.id = id;
        this.date = date;
        genres = new ArrayList<>();
        stuffs = new TreeSet<>();
    }