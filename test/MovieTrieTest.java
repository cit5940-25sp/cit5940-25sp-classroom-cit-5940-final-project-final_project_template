import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Ashley Wang
 */
public class MovieTrieTest {

    @Test
    public void testBuildTrie() {
        MovieTrie movieTrie = new MovieTrie();
        TrieNode trieNode = movieTrie.buildTrie();
        assertEquals(33, trieNode.getChildren().size());
    }

    @Test
    public void testInsert() {
        MovieTrie movieTrie = new MovieTrie();
        TrieNode trieNode = movieTrie.getRoot();
        assertEquals(0, trieNode.getChildren().size());
        Movie movie = new Movie("test", 2000);
        movieTrie.insert("test", movie);
        assertEquals(1, trieNode.getChildren().size());

    }

    @Test
    public void testSearch() {
        MovieTrie movieTrie = new MovieTrie();
        TrieNode trieNode = movieTrie.getRoot();
        assertFalse(movieTrie.search("test"));
        Movie movie = new Movie("test", 2000);
        movieTrie.insert("test", movie);
        assertTrue(movieTrie.search("test"));
    }

    @Test
    public void getSuggestions() {
        MovieTrie movieTrie = new MovieTrie();
        movieTrie.buildTrie();
        assertEquals(10, movieTrie.getSuggestions("a").size());
//        movieTrie.getSuggestions("a").forEach(x -> System.out.println(x));
//        System.out.println(movieTrie.getSuggestions("a"));
        movieTrie.setLimit(5000);
        assertEquals(308, movieTrie.getSuggestions("a").size());

    }

//    @Test
//    public void getAllMovieSuggestions() {
//        MovieTrie movieTrie = new MovieTrie();
//        movieTrie.buildTrie();
//        movieTrie.getAllMovieSuggestions()
//    }
}