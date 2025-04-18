import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Movie {
    private String title;
    private int releaseYear;
    private int voteCount;
    private Set<String> genres;
    private Set<String> actors;
    private Set<String> directors;
    private Set<String> composers;
    private Set<String> writers;
    private Set<String> cinematographers;

    public Movie() {
    }

    public Movie(String title, int releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.voteCount = 0;
        genres = new HashSet<>();
        actors = new HashSet<>();
        directors = new HashSet<>();
        composers = new HashSet<>();
        writers = new HashSet<>();
        cinematographers = new HashSet<>();
    }

    public static Comparator<Movie> byReverseWeightOrder() {
        return Comparator.comparingLong(Movie::getVoteCount).reversed();
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

    public int getVoteCount() {
        return voteCount;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setActors(Set<String> actors) {
        this.actors = actors;
    }

    public void setCinematographers(Set<String> cinematographers) {
        this.cinematographers = cinematographers;
    }

    public void setComposers(Set<String> composers) {
        this.composers = composers;
    }

    public void setDirectors(Set<String> directors) {
        this.directors = directors;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setWriters(Set<String> writers) {
        this.writers = writers;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
