import java.util.HashMap;
import java.util.Map;

/**
 * @author Harry Smith
 */

public class Node {

    private Term term;
    private int words;
    private int prefixes;
//    private Node[] references;
    private Map<Character, Node> references;


    /**
     * Initialize a Node with an empty string and 0 weight; useful for
     * writing tests.
     */
    public Node() {
        this.term = new Term("", 0);
        this.words = 0;
        this.prefixes = 0;
        this.references = new HashMap<>();
    }

    /**
     * Initialize a Node with the given query string and weight.
     * @throws IllegalArgumentException if query is null or if weight is negative.
     */
    public Node(String query, long weight) {
        if (query == null) {
            throw new IllegalArgumentException("query cannot be null.");
        }

        if (weight < 0) {
            throw new IllegalArgumentException("weight cannot be negative.");
        }

        this.term = new Term(query, weight);
        this.words = 0;
        this.prefixes = 0;
        this.references = new HashMap<>();
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(int prefixes) {
        this.prefixes = prefixes;
    }

    public Map<Character, Node> getReferences() {
        return references;
    }

    public void setReferences(Map<Character, Node> references) {
        this.references = references;
    }
}
