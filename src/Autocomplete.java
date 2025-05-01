import java.util.*;

/**
 * Provides autocomplete functionality for movie title input using a Trie.
 */

public class Autocomplete {

    private final Node root = new Node();
    private int suggestionLimit = 10;

    /**
     * Inserts a term (movie title + weight) into the trie.
     */
    public void insert(String title, long weight) {
        Node node = root;
        Term term = new Term(title, weight);
        String lowerTitle = title.toLowerCase();

        for (char c : lowerTitle.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new Node());
            node.suggestions.add(term);  // Accumulate suggestions
        }
        node.isEndOfWord = true;
    }

    /**
     * Bulk insert of all movie titles and weights.
     */
    public void loadTerms(Collection<Term> terms) {
        for (Term t : terms) {
            insert(t.getTerm(), t.getWeight());
        }
    }

    /**
     * Suggest titles that match the prefix, sorted by descending weight.
     */
    public List<Term> suggest(String prefix) {
        Node node = root;
        String lowerPrefix = prefix.toLowerCase();

        for (char c : lowerPrefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                return Collections.emptyList();
            }
        }

        // Sort suggestions (optional: cache this for performance)
        return node.suggestions.stream()
                .sorted(Term.byReverseWeightOrder())
                .limit(suggestionLimit)
                .toList(); // Java 16+, use `collect(Collectors.toList())` for older
    }

    public void setSuggestionLimit(int limit) {
        this.suggestionLimit = limit;
    }
}
