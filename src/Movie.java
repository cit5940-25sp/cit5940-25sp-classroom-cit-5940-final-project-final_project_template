import java.util.*;

/**
 * Represents a movie in the database, storing metadata such as title, year,
 * and associated people (actors, directors, etc.).
 */
public class Movie {
    private long movieId;
    private String title;
    private int year;
    private Set<String> genres;
    private Set<String> actors;
    private Set<String> directors;
    private Set<String> writers;
    private Set<String> composers;
    private Set<String> cinematographers;

    /**
     * Constructor for full metadata
     */
    public Movie(long movieId, String title, int year,
                 Set<String> genres,
                 Set<String> actors,
                 Set<String> directors,
                 Set<String> writers,
                 Set<String> composers,
                 Set<String> cinematographers) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.genres = (genres != null) ? genres : new HashSet<>();
        this.actors = (actors != null) ? actors : new HashSet<>();
        this.directors = (directors != null) ? directors : new HashSet<>();
        this.writers = (writers != null) ? writers : new HashSet<>();
        this.composers = (composers != null) ? composers : new HashSet<>();
        this.cinematographers = (cinematographers != null) ? cinematographers : new HashSet<>();
    }

    /**
     * Checks whether this movie shares any attribute (e.g., actor, director)
     * with another movie.
     */
    public boolean sharesAttributeWith(Movie other) {
        return !Collections.disjoint(this.actors, other.actors)
            || !Collections.disjoint(this.directors, other.directors)
            || !Collections.disjoint(this.writers, other.writers)
            || !Collections.disjoint(this.composers, other.composers)
            || !Collections.disjoint(this.cinematographers, other.cinematographers)
            || !Collections.disjoint(this.genres, other.genres);
    }

    // ======== Getters ========

    public long getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
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

    public Set<String> getWriters() {
        return writers;
    }

    public Set<String> getComposers() {
        return composers;
    }

    public Set<String> getCinematographers() {
        return cinematographers;
    }
}
