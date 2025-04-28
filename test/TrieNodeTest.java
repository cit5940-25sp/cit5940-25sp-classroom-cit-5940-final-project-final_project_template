import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Ashley Wang
 */

public class TrieNodeTest {
    @Test
    public void testTrieNodeConstructor() {
        TrieNode node = new TrieNode();
        assertNotNull(node.getChildren());
        assertFalse(node.isEndOfWord());
        assertNotNull(node.getMovieReference());
        assertEquals(0, node.getMovieReference().size());
    }

    @Test
    public void testSetEndOfWord() {
        TrieNode node = new TrieNode();
        node.setEndOfWord(true);
        assertTrue(node.isEndOfWord());
    }

    @Test
    public void testAddAndGetChild() {
        TrieNode node = new TrieNode();
        TrieNode child = new TrieNode();
        char c = 'a';
        node.addChild(c, child);
        assertEquals(child, node.getChild(c));
    }

    @Test
    public void testAddMovieReference() {
        TrieNode node = new TrieNode();
        Movie movie = new Movie("Test Movie", 2025);
        node.addMovieReference(movie);
        List<Movie> movieReference = node.getMovieReference();
        assertEquals(1, movieReference.size());
        assertEquals(movie, movieReference.get(0));
    }

}
