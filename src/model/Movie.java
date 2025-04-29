package model;

import java.util.Set;

/**
 * Represents a movie with its title, release year, genres, and associated people
 * such as actors, directors, writers, and composers.
 */
public class Movie {
    private String title;
    private int year;
    private Set<String> genres;
    private Set<Person> actors;
    private Set<Person> directors;
    private Set<Person> writers;
    private Set<Person> composers;

    /**
     * Constructs a Movie object with the specified title and year.
     * @param title the title of the movie
     * @param year the release year of the movie
     */
    public Movie(String title, int year) {
        // TODO
    }

    public String getTitle() { return null; }

    public int getYear() { return 0; }

    public Set<String> getGenres() { return null; }

    public Set<Person> getActors() { return null; }

    public Set<Person> getDirectors() { return null; }

    public Set<Person> getWriters() { return null; }

    public Set<Person> getComposers() { return null; }

    /**
     * Adds a genre to the movie.
     * @param genre the genre to add
     */
    public void addGenre(String genre) {}

    /**
     * Adds an actor to the movie.
     * @param actor the actor to add
     */
    public void addActor(Person actor) {}

    /**
     * Adds a director to the movie.
     * @param director the director to add
     */
    public void addDirector(Person director) {}

    /**
     * Adds a writer to the movie.
     * @param writer the writer to add
     */
    public void addWriter(Person writer) {}

    /**
     * Adds a composer to the movie.
     * @param composer the composer to add
     */
    public void addComposer(Person composer) {}
}
