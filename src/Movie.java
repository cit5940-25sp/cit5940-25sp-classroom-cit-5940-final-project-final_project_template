import java.util.HashSet;
import java.util.Set;

public class Movie {
    private String title;
    private int releaseYear;
    private Set<String> genres;
    private Set<String> actors;
    private Set<String> directors;
    private Set<String> composers;
    private Set<String> writers;
    private Set<String> cinematographers;

    public Movie(String title, int releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
        genres = new HashSet<>();
        actors = new HashSet<>();
        directors = new HashSet<>();
        composers = new HashSet<>();
        writers = new HashSet<>();
        cinematographers = new HashSet<>();
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

    public Set<String> getActors() {
        return actors;
    }

    public Set<String> getDirectors() {
        return directors;
    }

    public Set<String> getComposers() {
        return composers;
    }

    public Set<String> getWriters() {
        return writers;
    }

    public Set<String> getCinematographers() {
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
