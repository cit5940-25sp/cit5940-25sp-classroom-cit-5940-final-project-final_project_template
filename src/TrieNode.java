import java.util.*;

/**
 * Node structure used in the autocomplete trie.
 */
public class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;
    List<String> suggestions;
}
