import java.util.ArrayList;
import java.util.HashSet;

/**
 * Movie Object
 *
 */
public class Movie {
    private String movieTitle;
    private int movieID;

    private HashSet<String> actors;
    private HashSet<String> directors;
    private HashSet<String> writers;
    private HashSet<String> cinematographers;
    private HashSet<String> composers;

    private Long releaseYear;
    private String genre;
    private ArrayList<SingleConnection> linksToPreviousMovie;  //list of connections to previous movie //TODO - might make more sense ot store somewhere else



    public String getMovieTitle() {
        return movieTitle;
    }

    public int getMovieID() {
        return movieID;
    }

    public HashSet<String> getActors() {
        return actors;
    }

    public HashSet<String> getDirectors() {
        return directors;
    }

    public HashSet<String> getWriters() {
        return writers;
    }

    public HashSet<String> getCinematographers() {
        return cinematographers;
    }

    public HashSet<String> getComposers() {
        return composers;
    }

    public Long getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public ArrayList<SingleConnection> getLinksToPreviousMovie() {
        return linksToPreviousMovie;
    }
}
