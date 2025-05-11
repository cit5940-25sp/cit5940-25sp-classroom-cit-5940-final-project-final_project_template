/**
 * This class represents a history entry in the movie recommendation system.
 * Each entry contains a movie and the reason for its connection to the previous
 * movie
 * in the game's history. This class is used to track the sequence of movies
 * played
 * and their connection relationships during gameplay.
 *
 * @author Vera Zhang
 */
public class HistoryEntry {
    /** The movie associated with this history entry */
    private final Movie movie;
    /** The reason explaining how this movie connects to the previous movie */
    private final String connectionReason;

    /**
     * Constructs a new HistoryEntry with the specified movie and connection reason.
     *
     * @param movie            The movie to be recorded in history
     * @param connectionReason The explanation of how this movie connects to the
     *                         previous movie
     */
    public HistoryEntry(final Movie movie, final String connectionReason) {
        this.movie = movie;
        this.connectionReason = connectionReason;
    }

    /**
     * Returns the movie associated with this history entry.
     *
     * @return The movie object
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Returns the reason explaining how this movie connects to the previous movie.
     *
     * @return The connection reason as a string
     */
    public String getConnectionReason() {
        return connectionReason;
    }

    /**
     * Returns a string representation of this history entry.
     * The format is: "Movie: [title] ([year]), [connection reason]"
     *
     * @return A string containing the movie title, release year, and connection
     *         reason
     */
    @Override
    public String toString() {
        return "Movie: " + movie.getTitle() + " (" + movie.getReleaseYear() + "), " + connectionReason;
    }

    /**
     * Compares this history entry with another object for equality.
     * Two history entries are considered equal if they contain the same movie,
     * regardless of their connection reasons.
     *
     * @param obj The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        HistoryEntry that = (HistoryEntry) obj;
        return movie.equals(that.movie);
    }

    /**
     * Returns a hash code value for this history entry.
     *
     * @return A hash code value for this object
     */
    @Override
    public int hashCode() {
        return movie.hashCode();
    }
}
