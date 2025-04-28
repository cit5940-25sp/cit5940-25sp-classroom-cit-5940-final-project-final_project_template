import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a movie with various attributes such as title, release year, vote count,
 * genres, actors, directors, composers, writers, and cinematographers.
 * This class provides methods to manage and retrieve information about the movie.
 */
public class Movie {
    /**
     * The title of the movie.
     */
    private String title;
    /**
     * The release year of the movie.
     */
    private int releaseYear;
    /**
     * The vote count of the movie.
     */
    private int voteCount;
    /**
     * A set of genres associated with the movie.
     */
    private Set<String> genres;
    /**
     * A set of actors who participated in the movie.
     */
    private Set<String> actors;
    /**
     * A set of directors who directed the movie.
     */
    private Set<String> directors;
    /**
     * A set of composers who created the music for the movie.
     */
    private Set<String> composers;
    /**
     * A set of writers who wrote the script for the movie.
     */
    private Set<String> writers;
    /**
     * A set of cinematographers who were responsible for the movie's photography.
     */
    private Set<String> cinematographers;

    /**
     * Default constructor for creating a new Movie object.
     * Initializes all fields to their default values.
     */
    public Movie() {
    }

    /**
     * Constructs a new Movie object with the given title and release year.
     * Initializes the vote count to 0 and creates empty sets for genres, actors,
     * directors, composers, writers, and cinematographers.
     *
     * @param title       The title of the movie.
     * @param releaseYear The release year of the movie.
     */
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

    /**
     * Returns a comparator that orders movies in descending order based on their vote count.
     *
     * @return A comparator for sorting movies by vote count in reverse order.
     */
    public static Comparator<Movie> byReverseWeightOrder() {
        return Comparator.comparingLong(Movie::getVoteCount).reversed();
    }

    /**
     * Retrieves the title of the movie.
     *
     * @return The title of the movie.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the release year of the movie.
     *
     * @return The release year of the movie.
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Retrieves the set of genres associated with the movie.
     *
     * @return A set of strings representing the genres of the movie.
     */
    public Set<String> getGenres() {
        return genres;
    }

    /**
     * Retrieves the set of actors who participated in the movie.
     *
     * @return A set of strings representing the actors in the movie.
     */
    public Set<String> getActors() {
        return actors;
    }

    /**
     * Retrieves the set of directors who directed the movie.
     *
     * @return A set of strings representing the directors of the movie.
     */
    public Set<String> getDirectors() {
        return directors;
    }

    /**
     * Retrieves the set of composers who created the music for the movie.
     *
     * @return A set of strings representing the composers of the movie.
     */
    public Set<String> getComposers() {
        return composers;
    }

    /**
     * Retrieves the set of writers who wrote the script for the movie.
     *
     * @return A set of strings representing the writers of the movie.
     */
    public Set<String> getWriters() {
        return writers;
    }

    /**
     * Retrieves the vote count of the movie.
     *
     * @return The vote count of the movie.
     */
    public int getVoteCount() {
        return voteCount;
    }

    /**
     * Retrieves the set of cinematographers who were responsible for the movie's photography.
     *
     * @return A set of strings representing the cinematographers of the movie.
     */
    public Set<String> getCinematographers() {
        return cinematographers;
    }

    /**
     * Adds a genre to the set of genres associated with the movie.
     *
     * @param genre The genre to be added.
     */
    public void addGenre(String genre) {
        genres.add(genre);
    }

    /**
     * Adds an actor to the set of actors who participated in the movie.
     *
     * @param actor The actor to be added.
     */
    public void addActor(String actor) {
        actors.add(actor);
    }

    /**
     * Adds a director to the set of directors who directed the movie.
     *
     * @param director The director to be added.
     */
    public void addDirector(String director) {
        directors.add(director);
    }

    /**
     * Adds a composer to the set of composers who created the music for the movie.
     *
     * @param composer The composer to be added.
     */
    public void addComposer(String composer) {
        composers.add(composer);
    }

    /**
     * Adds a writer to the set of writers who wrote the script for the movie.
     *
     * @param writer The writer to be added.
     */
    public void addWriter(String writer) {
        writers.add(writer);
    }

    /**
     * Adds a cinematographer to the set of cinematographers who were responsible for the movie's photography.
     *
     * @param cinematographer The cinematographer to be added.
     */
    public void addCinematographer(String cinematographer) {
        cinematographers.add(cinematographer);
    }

    /**
     * Sets the title of the movie.
     *
     * @param title The new title of the movie.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the set of actors who participated in the movie.
     *
     * @param actors The new set of actors.
     */
    public void setActors(Set<String> actors) {
        this.actors = actors;
    }

    /**
     * Sets the set of cinematographers who were responsible for the movie's photography.
     *
     * @param cinematographers The new set of cinematographers.
     */
    public void setCinematographers(Set<String> cinematographers) {
        this.cinematographers = cinematographers;
    }

    /**
     * Sets the set of composers who created the music for the movie.
     *
     * @param composers The new set of composers.
     */
    public void setComposers(Set<String> composers) {
        this.composers = composers;
    }

    /**
     * Sets the set of directors who directed the movie.
     *
     * @param directors The new set of directors.
     */
    public void setDirectors(Set<String> directors) {
        this.directors = directors;
    }

    /**
     * Sets the set of genres associated with the movie.
     *
     * @param genres The new set of genres.
     */
    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    /**
     * Sets the release year of the movie.
     *
     * @param releaseYear The new release year of the movie.
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Sets the set of writers who wrote the script for the movie.
     *
     * @param writers The new set of writers.
     */
    public void setWriters(Set<String> writers) {
        this.writers = writers;
    }

    /**
     * Sets the vote count of the movie.
     *
     * @param voteCount The new vote count of the movie.
     */
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    /**
     * Checks if this movie is equal to another object.
     * Two movies are considered equal if they have the same title and release year.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Movie movie = (Movie) o;
        return releaseYear == movie.releaseYear && Objects.equals(title, movie.title);
    }

    /**
     * Computes the hash code of the movie based on its title and release year.
     *
     * @return The hash code of the movie.
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, releaseYear);
    }
}