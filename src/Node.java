import java.util.*;

/**
 * Node structure used in the autocomplete trie.
 */
public class Node {
    Map<Character, Node> children = new HashMap<>();
    boolean isEndOfWord = false;

    // All terms that pass through this node (used for autocomplete suggestions)
    List<Term> suggestions = new ArrayList<>();
}
