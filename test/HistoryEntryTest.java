import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.*;
import org.apache.commons.csv.*;

/**
 * @author Vera Zhang
 */

public class HistoryEntryTest {

    private Movie loadFirstMovieFromCSV() {
        try (Reader reader = Files.newBufferedReader(Paths.get("tmdb_5000_movies.csv"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(reader);

            for (CSVRecord record : records) {
                String title = record.get("title");
                String date = record.get("release_date");
                int year = date != null && date.length() >= 4 ? Integer.parseInt(date.substring(0, 4)) : 0;
                return new Movie(title, year);
            }
        } catch (IOException e) {
            fail("Failed to load movie from CSV: " + e.getMessage());
        }
        return null;
    }

    @Test
    public void testConstructorAndGetters() {
        Movie movie = loadFirstMovieFromCSV();
        String reason = "Sample connection reason";
        HistoryEntry entry = new HistoryEntry(movie, reason);

        assertEquals(movie, entry.getMovie());
        assertEquals(reason, entry.getConnectionReason());
    }

    @Test
    public void testToString() {
        Movie movie = loadFirstMovieFromCSV();
        String reason = "Testing string format";
        HistoryEntry entry = new HistoryEntry(movie, reason);

        String expected = "Movie: " + movie.getTitle() + " (" + movie.getReleaseYear() + "), " + reason;
        assertEquals(expected, entry.toString());
    }

    @Test
    public void testEqualsSameMovie() {
        Movie movie = loadFirstMovieFromCSV();
        HistoryEntry entry1 = new HistoryEntry(movie, "Reason 1");
        HistoryEntry entry2 = new HistoryEntry(movie, "Reason 2");

        assertEquals(entry1, entry2);
    }

    @Test
    public void testEqualsDifferentMovie() {
        Movie movie1 = new Movie("Dunkirk", 2017);
        Movie movie2 = new Movie("Tenet", 2020);
        HistoryEntry entry1 = new HistoryEntry(movie1, "Reason 1");
        HistoryEntry entry2 = new HistoryEntry(movie2, "Reason 2");

        assertNotEquals(entry1, entry2);
    }

    @Test
    public void testEqualsNullAndDifferentClass() {
        Movie movie = loadFirstMovieFromCSV();
        HistoryEntry entry = new HistoryEntry(movie, "Connection");

        assertNotEquals(entry, null);
        assertNotEquals(entry, "Different Type");
    }
}