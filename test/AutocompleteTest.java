import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class AutocompleteTest {

    private Autocomplete engine;

    @Before
    public void setUp() {
        engine = new Autocomplete();
        List<Term> terms = Arrays.asList(
                new Term("Inception", 500),
                new Term("Inside Out", 300),
                new Term("Interstellar", 800),
                new Term("Indiana Jones", 200),
                new Term("Into the Wild", 450)
        );
        engine.loadTerms(terms);
    }

    @Test
    public void testTermCreation() {
        Term t = new Term("Test Movie", 100);
        assertEquals("Test Movie", t.getTerm());
        assertEquals(100, t.getWeight());
    }

    @Test
    public void testInsertAndSuggest() {
        List<Term> suggestions = engine.suggest("In");
        List<String> titles = new ArrayList<>();
        for (Term t : suggestions) {
            titles.add(t.getTerm());
        }

        List<String> expected = Arrays.asList(
                "Interstellar", "Inception", "Into the Wild", "Inside Out", "Indiana Jones"
        );
        assertEquals(expected, titles);
    }

    @Test
    public void testCaseInsensitivity() {
        List<Term> suggestions = engine.suggest("inTer");
        assertFalse(suggestions.isEmpty());
        assertEquals("Interstellar", suggestions.get(0).getTerm());
    }

    @Test
    public void testNonExistingPrefix() {
        List<Term> suggestions = engine.suggest("Zzz");
        assertTrue(suggestions.isEmpty());
    }

    @Test
    public void testSuggestionLimit() {
        engine.setSuggestionLimit(3);
        List<Term> suggestions = engine.suggest("In");
        assertEquals(3, suggestions.size());
    }
}
