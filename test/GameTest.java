package test;
import AutoComplete.Autocomplete;
import AutoComplete.Node;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {


    @Test
    public void testAddWord() {
        Autocomplete autocomplete = new Autocomplete(5);
        String word = "123";
        long weight = 123;
        autocomplete.addWord(word, weight);
        Node node = autocomplete.getSubTrie(word);
        assertNull(node);
        word = "abc";
        weight = 123;
        autocomplete.addWord(word, weight);
        node = autocomplete.getSubTrie(word);
        assertNotNull(node);
        assertEquals(weight, node.getTerm().getWeight());
        autocomplete.addWord("abcd", 101);
        autocomplete.addWord("abcde", 101);
        int count = autocomplete.countPrefixes(word);
        assertEquals(3, count);
        count = autocomplete.countPrefixes("abcde");
        assertEquals(1, count);
    }

    @Test
    public void testBuildTrieAndSuggestions() {
        Autocomplete autocomplete = new Autocomplete(5);
        String filename = "pokemon.txt";
        int k = 3;
        //Node root = autocomplete.buildTrie(filename, k);
        //List<ITerm> suggestions = autocomplete.getSuggestions("b");
        //assertEquals(5, suggestions.size());
        //assertEquals("braixen", suggestions.get(0).getTerm());
        //assertEquals("braviary", suggestions.get(1).getTerm());
        //assertEquals("breloom", suggestions.get(2).getTerm());
    }


}