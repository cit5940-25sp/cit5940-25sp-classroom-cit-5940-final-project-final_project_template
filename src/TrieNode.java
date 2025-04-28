import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a node in a Trie data structure.
 * Each node contains a map of child nodes, a flag indicating if it's the end of a word,
 * and a list of movie references associated with the word formed up to this node.
 */
public class TrieNode {
    /**
     * A map that stores the children of this TrieNode. The key is a character,
     * and the value is the corresponding child TrieNode.
     */
    private Map<Character, TrieNode> children;
    /**
     * A flag indicating whether this TrieNode is the end of a word.
     */
    private boolean isEndOfWord;
    /**
     * A list that stores the movie references associated with the word formed up to this node.
     */
    private List<Movie> movieReference;

    /**
     * Constructs a new TrieNode.
     * Initializes the children map, sets the end-of-word flag to false,
     * and creates an empty list for movie references.
     */
    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        movieReference = new ArrayList<>();
    }

    /**
     * Returns the map of children of this TrieNode.
     *
     * @return A map where the keys are characters and the values are child TrieNodes.
     */
    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    /**
     * Checks if this TrieNode is the end of a word.
     *
     * @return true if this TrieNode is the end of a word, false otherwise.
     */
    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    /**
     * Sets the end-of-word flag for this TrieNode.
     *
     * @param endOfWord A boolean value indicating whether this TrieNode is the end of a word.
     */
    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }

    /**
     * Returns the list of movie references associated with the word formed up to this node.
     *
     * @return A list of Movie objects.
     */
    public List<Movie> getMovieReference() {
        return movieReference;
    }

    /**
     * Adds a movie reference to the list of movie references associated with this TrieNode.
     *
     * @param movie The Movie object to be added.
     */
    public void addMovieReference(Movie movie) {
        movieReference.add(movie);
    }

    /**
     * Retrieves the child TrieNode corresponding to the given character.
     *
     * @param c The character for which to retrieve the child TrieNode.
     * @return The child TrieNode corresponding to the given character, or null if not found.
     */
    public TrieNode getChild(Character c) {
        return children.get(c);
    }

    /**
     * Adds a child TrieNode for the given character.
     *
     * @param c    The character for which to add the child TrieNode.
     * @param node The child TrieNode to be added.
     */
    public void addChild(Character c, TrieNode node) {
        children.put(c, node);
    }

}