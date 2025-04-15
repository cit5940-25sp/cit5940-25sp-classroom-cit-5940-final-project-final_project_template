import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Movie {
    private String title;
    private int releaseYear;
    private Set<String> genres;
    private List<String> actors;
    private List<String> directors;
    private List<String> composers;
    private List<String> writers;
    private List<String> cinematographers;

    public Movie(String title, int releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
        genres = new HashSet<>();
        actors = new ArrayList<>();
        directors = new ArrayList<>();
        composers = new ArrayList<>();
        writers = new ArrayList<>();
        cinematographers = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getComposers() {
        return composers;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getCinematographers() {
        return cinematographers;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public void addActor(String actor) {
        actors.add(actor);
    }

    public void addDirector(String director) {
        directors.add(director);
    }

    public void addComposer(String composer) {
        composers.add(composer);
    }

    public void addWriter(String writer) {
        writers.add(writer);
    }

    public void addCinematographer(String cinematographer) {
        cinematographers.add(cinematographer);
    }
}
