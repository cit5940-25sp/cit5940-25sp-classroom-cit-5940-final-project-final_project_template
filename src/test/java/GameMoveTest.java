import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameMoveTest {

    @Test
    public void testGameMoveFields() {
        Language spanish = new Language("Spanish", 2);
        Country spain = new Country("Spain", Set.of(spanish));
        GameMove move = new GameMove(spain, spanish, 10);

        assertEquals(spain, move.getCountry());
        assertEquals(spanish, move.getLanguage());
        assertEquals(10, move.getPoints());
    }

    @Test
    public void testToStringWithLanguage() {
        Language french = new Language("French", 3);
        Country france = new Country("France", Set.of(french));
        GameMove move = new GameMove(france, french, 9);

        String output = move.toString();
        assertTrue(output.contains("France"));
        assertTrue(output.contains("French"));
        assertTrue(output.contains("9"));
    }

    @Test
    public void testToStringWithoutLanguage() {
        Country usa = new Country("United States", Set.of(new Language("english", 1)));
        GameMove move = new GameMove(usa, null, 0);

        String output = move.toString();
        assertTrue(output.contains("United States"));
        assertTrue(output.contains("Starting country"));
        assertTrue(output.contains("0"));
    }
}
