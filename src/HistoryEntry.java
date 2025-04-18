public class HistoryEntry {
    private Movie movie;
    private String connectionReason;

    public HistoryEntry(Movie movie, String connectionReason) {
        this.movie = movie;
        this.connectionReason = connectionReason;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getConnectionReason() {
        return connectionReason;
    }

    @Override
    public String toString() {
        return "Movie: " + movie.getTitle() + " (" + movie.getReleaseYear() + "), " + connectionReason;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HistoryEntry that = (HistoryEntry) obj;
        return movie.equals(that.movie);
    }
}
