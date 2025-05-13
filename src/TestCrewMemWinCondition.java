import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestCrewMemWinCondition {
    @Test
    public void testCheckWinReturnsTrueWhenCrewAppearsFiveTimes() {
        CrewMemWinCondition condition = new CrewMemWinCondition("Hans Zimmer");
        List<IMovie> movies = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Movie movie = new Movie("Movie" + i, 2000 + i, List.of("Drama"));
            movie.addCrew("Hans Zimmer");
            movies.add(movie);
        }

        assertTrue(condition.checkWin(movies));
    }

    @Test
    public void testCheckWinReturnsFalseWhenCrewAppearsLessThanFiveTimes() {
        CrewMemWinCondition condition = new CrewMemWinCondition("Hans Zimmer");
        List<IMovie> movies = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Movie movie = new Movie("Movie" + i, 2000 + i, List.of("Drama"));
            movie.addCrew("Hans Zimmer");
            movies.add(movie);
        }

        assertFalse(condition.checkWin(movies));
    }

    @Test
    public void testCheckWinReturnsFalseWhenCrewNeverAppears() {
        CrewMemWinCondition condition = new CrewMemWinCondition("Hans Zimmer");
        List<IMovie> movies = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Movie movie = new Movie("Movie" + i, 2000 + i, List.of("Drama"));
            movie.addCrew("Someone Else");
            movies.add(movie);
        }

        assertFalse(condition.checkWin(movies));
    }

    @Test
    public void testGetDescription() {
        CrewMemWinCondition condition = new CrewMemWinCondition("Hans Zimmer");
        assertEquals("Play 5 movies of crew member Hans Zimmer.", condition.getDescription());
    }
}
