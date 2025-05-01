import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class TermTest {

    @Test
    public void testConstructorAndGetters() {
        Term term = new Term("Inception", 500);
        assertEquals("Inception", term.getTerm());
        assertEquals(500, term.getWeight());
    }


    @Test
    public void testSetWeightValid() {
        Term term = new Term("Inception", 500);
        term.setWeight(800);
        assertEquals(800, term.getWeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWeightInvalid() {
        Term term = new Term("Inception", 500);
        term.setWeight(-10);
    }

    @Test
    public void testByReverseWeightOrderComparator() {
        Term t1 = new Term("MovieA", 100);
        Term t2 = new Term("MovieB", 500);
        Term t3 = new Term("MovieC", 300);

        List<Term> list = new ArrayList<>(Arrays.asList(t1, t2, t3));
        list.sort(Term.byReverseWeightOrder());

        assertEquals("MovieB", list.get(0).getTerm());
        assertEquals("MovieC", list.get(1).getTerm());
        assertEquals("MovieA", list.get(2).getTerm());
    }

    @Test
    public void testToString() {
        Term term = new Term("Inception", 500);
        assertEquals("500\tInception", term.toString());
    }
}
