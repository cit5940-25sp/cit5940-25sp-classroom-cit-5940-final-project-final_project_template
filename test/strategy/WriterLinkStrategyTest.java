package strategy;

import model.Movie;
import model.Person;
import model.PersonRole;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link WriterLinkStrategy}.
 */
class WriterLinkStrategyTest {

    private final WriterLinkStrategy strat = new WriterLinkStrategy();

    @Test
    void testValidLinkWhenSharedWriter() {
        Movie a = new Movie("A", 2000);
        Movie b = new Movie("B", 2001);
        Person writer = new Person("Alice Smith", PersonRole.WRITER);
        a.addWriter(writer);
        b.addWriter(writer);

        assertTrue(strat.isValidLink(a, b));
        assertEquals("Shared writer: Alice Smith", strat.getReason(a, b));
    }

    @Test
    void testInvalidLinkWithoutSharedWriter() {
        Movie a = new Movie("A", 2000);
        Movie b = new Movie("B", 2001);
        a.addWriter(new Person("Writer X", PersonRole.WRITER));
        b.addWriter(new Person("Writer Y", PersonRole.WRITER));

        assertFalse(strat.isValidLink(a, b));
    }
}
