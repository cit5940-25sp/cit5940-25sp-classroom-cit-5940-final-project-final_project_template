import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CountryTest {

    @Test
    public void testCountryEquals() {
        Set<Language> lang1 = new HashSet<>();
        lang1.add(new Language("spanish", 1));
        Country spain1 = new Country("spain", lang1);
        Country spain2 = new Country("spain", new HashSet<>());
        Country spain3 = new Country("Spain", new HashSet<>());

        assertEquals(spain1, spain2);
        assertEquals(spain1, spain3);
        assertEquals(spain1, spain1);
        assertNotEquals(null, spain1);
        assertNotEquals("spain", spain1);
    }

    @Test
    public void testCountryHashCode() {
        Country c1 = new Country("Mexico", Set.of(new Language("Spanish", 1)));
        Country c2 = new Country("mexico", Set.of());
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testHasLanguage() {
        Language spanish = new Language("spanish", 1);
        Country colombia = new Country("Colombia", Set.of(spanish));
        assertTrue(colombia.hasLanguage(spanish));
        assertFalse(colombia.hasLanguage(new Language("french", 1)));
    }

    @Test
    public void testGetSharedLanguages() {
        Language english = new Language("english", 1);
        Language french = new Language("french", 2);

        Country canada = new Country("Canada", Set.of(english, french));
        Country france = new Country("France", Set.of(french));

        Set<Language> shared = canada.getSharedLanguages(france);
        assertEquals(1, shared.size());
        assertTrue(shared.contains(french));
    }

    @Test
    public void testToString() {
        Country japan = new Country("Japan", Set.of(new Language("Japanese", 1)));
        assertEquals("Japan", japan.toString());
    }
}
