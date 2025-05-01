import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testDefaultInitialization() {
        Node node = new Node();
        assertNotNull(node.children);
        assertTrue(node.children.isEmpty());
        assertFalse(node.isEndOfWord);
        assertNotNull(node.suggestions);
        assertTrue(node.suggestions.isEmpty());
    }

    @Test
    public void testAddChildNode() {
        Node parent = new Node();
        Node child = new Node();
        parent.children.put('a', child);

        assertTrue(parent.children.containsKey('a'));
        assertSame(child, parent.children.get('a'));
    }

    @Test
    public void testSetIsEndOfWord() {
        Node node = new Node();
        assertFalse(node.isEndOfWord);
        node.isEndOfWord = true;
        assertTrue(node.isEndOfWord);
    }

    @Test
    public void testAddSuggestions() {
        Node node = new Node();
        Term t1 = new Term("Inception", 500);
        Term t2 = new Term("Interstellar", 800);

        node.suggestions.add(t1);
        node.suggestions.add(t2);

        assertEquals(2, node.suggestions.size());
        assertEquals("Inception", node.suggestions.get(0).getTerm());
        assertEquals("Interstellar", node.suggestions.get(1).getTerm());
    }
}
