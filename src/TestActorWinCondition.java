import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestActorWinCondition {
    @Test
    public void testCheckWinReturnsTrueWhenActorAppearsFiveTimes() {
        ActorWinCondition condition = new ActorWinCondition("Tom Hanks");

        List<IMovie> movies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Movie m = new Movie("Movie" + i, 2000 + i, List.of("Drama"));
            m.addActor("Tom Hanks");
            movies.add(m);
        }

        assertTrue(condition.checkWin(movies));
    }

    @Test
    public void testCheckWinReturnsFalseWhenActorAppearsLessThanFiveTimes() {
        ActorWinCondition condition = new ActorWinCondition("Tom Hanks");

        List<IMovie> movies = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Movie m = new Movie("Movie" + i, 2000 + i, List.of("Drama"));
            m.addActor("Tom Hanks");
            movies.add(m);
        }

        assertFalse(condition.checkWin(movies));
    }

    @Test
    public void testGetDescription() {
        ActorWinCondition condition = new ActorWinCondition("Emma Watson");
        assertEquals("Play 5 movies of actor Emma Watson.", condition.getDescription());
    }
}
