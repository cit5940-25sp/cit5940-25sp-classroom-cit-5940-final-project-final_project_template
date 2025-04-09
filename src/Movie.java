import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Movie {
    // === Attributes ===
    private final String title;
    private final int releaseYear;
    private final String genre;
    private final Set<String> actors;
    private final String director;
    private final String writer;
    private final String cinematographer;
    private final String composer;

    // === Constructor ===
    public Movie(String title, int releaseYear, String genre, List<String> actors,
                 String director, String writer, String cinematographer, String composer) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.actors = new HashSet<>(actors); // Convert list to HashSet for O(1) lookups
        this.director = director;
        this.writer = writer;
        this.cinematographer = cinematographer;
        this.composer = composer;
    }

    // === Getters ===
    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public Set<String> getActors() {
        return new HashSet<>(actors); // Return copy to protect internal set
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getCinematographer() {
        return cinematographer;
    }

    public String getComposer() {
        return composer;
    }

    // === Functional Methods ===

    /**
     * Checks if the movie has a given actor.
     */
    public boolean hasActor(String actorName) {
        return actors.contains(actorName);
    }

    /**
     * Checks if this movie shares any of the connection attributes with another movie.
     */
    public boolean isConnectedTo(Movie other) {
        if (other == null) return false;

        // Check actor intersection
        for (String actor : this.actors) {
            if (other.actors.contains(actor)) return true;
        }

        // Check other connection types
        return Objects.equals(this.director, other.director) ||
                Objects.equals(this.writer, other.writer) ||
                Objects.equals(this.cinematographer, other.cinematographer) ||
                Objects.equals(this.composer, other.composer);
    }

    @Override
    public String toString() {
        return title + " (" + releaseYear + ") - Genre: " + genre;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Movie)) return false;
        Movie other = (Movie) obj;
        return this.title.equalsIgnoreCase(other.title) && this.releaseYear == other.releaseYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title.toLowerCase(), releaseYear);
    }
}
