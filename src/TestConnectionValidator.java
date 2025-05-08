import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class TestConnectionValidator {

    private ConnectionValidator validator;
    private IMovie movie1;
    private IMovie movie2;

    private class DummyMovie implements IMovie {
        private final Set<String> contributors;

        public DummyMovie(Set<String> contributors) {
            this.contributors = contributors;
        }

        @Override
        public String getTitle() { return ""; }
        @Override
        public int getYear() { return 0; }
        @Override
        public List<String> getGenres() { return new ArrayList<>(); }
        @Override
        public List<String> getActors() { return new ArrayList<>(); }
        @Override
        public List<String> getDirectors() { return new ArrayList<>(); }
        @Override
        public List<String> getWriters() { return new ArrayList<>(); }
        @Override
        public List<String> getComposers() { return new ArrayList<>(); }
        @Override
        public List<String> getCinematographers() { return new ArrayList<>(); }
        @Override
        public Set<String> getAllContributors() { return contributors; }
        @Override
        public List<String> getCrew() { return new ArrayList<>(); }
    }

    @Before
    public void setUp() {
        validator = new ConnectionValidator();
        movie1 = new DummyMovie(Set.of("Tom Hanks", "John Williams"));
        movie2 = new DummyMovie(Set.of("Tom Hanks", "Steven Spielberg"));
    }

    @Test
    public void testIsValidConnectionReturnsTrueWhenUsageBelowLimit() {
        boolean result = validator.isValidConnection(movie1, movie2);
        assertTrue(result);
    }

    @Test
    public void testIsValidConnectionReturnsFalseWhenNoSharedContributors() {
        IMovie m1 = new DummyMovie(Set.of("A"));
        IMovie m2 = new DummyMovie(Set.of("B"));
        boolean result = validator.isValidConnection(m1, m2);
        assertFalse(result);
    }

    @Test
    public void testGetSharedConnectionsReturnsCorrectSharedPeople() {
        List<String> shared = validator.getSharedConnections(movie1, movie2);
        assertEquals(List.of("Tom Hanks"), shared);
    }

    @Test
    public void testGetUsageCountReturnsZeroIfNotRecorded() {
        int count = validator.getUsageCount("Hans Zimmer");
        assertEquals(0, count);
    }

    @Test
    public void testRecordConnectionUseIncrementsUsage() {
        validator.recordConnectionUse(List.of("Hans Zimmer"));
        assertEquals(1, validator.getUsageCount("Hans Zimmer"));
    }

    @Test
    public void testRecordConnectionUseAccumulates() {
        validator.recordConnectionUse(List.of("Hans Zimmer"));
        validator.recordConnectionUse(List.of("Hans Zimmer"));
        assertEquals(2, validator.getUsageCount("Hans Zimmer"));
    }

    @Test
    public void testResetUsageCountClearsAllUsage() {
        validator.recordConnectionUse(List.of("Hans Zimmer"));
        validator.resetUsageCount();
        assertEquals(0, validator.getUsageCount("Hans Zimmer"));
    }
}
