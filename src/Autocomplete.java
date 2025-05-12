import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Autocomplete implements IAutocomplete {
    private Node root = new Node();

    @Override
    public void addWord(String word, long weight) {
        if (word == null || weight < 0) {
            return;
        }

        word = word.toLowerCase();

        Node current = root;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Map<Character, Node> children = current.getReferences();

            if (!children.containsKey(c)) {
                children.put(c, new Node());
            }

            current = children.get(c);

            current.setPrefixes(current.getPrefixes() + 1);
        }

        current.setWords(1);
        current.setTerm(new Term(word, weight));
    }

    @Override
    public Node buildTrie(String filename, int k) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\t");

                if (parts.length != 2) {
                    continue;
                }

                long weight = Long.parseLong(parts[0].trim());
                String word = parts[1].trim();
                addWord(word, weight);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return root;
    }

    @Override
    public Node getSubTrie(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return null;
        }

        prefix = prefix.toLowerCase(); // keep if you're storing lowercase, otherwise remove this
        Node current = root;

        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            Map<Character, Node> children = current.getReferences();

            if (!children.containsKey(c)) {
                return null;
            }

            current = children.get(c);
        }

        return current;
    }

    @Override
    public int countPrefixes(String prefix) {
        Node subRoot = getSubTrie(prefix);

        if (subRoot == null) {
            return 0;
        }

        return subRoot.getPrefixes();
    }

    @Override
    public List<ITerm> getSuggestions(String prefix) {
        List<ITerm> suggestions = new ArrayList<>();

        Node subRoot = getSubTrie(prefix);

        if (subRoot == null) {
            return suggestions;
        }

        suggestionsHelper(subRoot, suggestions);

        return suggestions;
    }

    private void suggestionsHelper(Node node, List<ITerm> results) {
        if (node == null) {
            return;
        }

        if (node.getWords() == 1) {
            Term suggestion = node.getTerm();
            results.add(new Term(suggestion.getTerm(), suggestion.getWeight()));
        }

        for (Node child : node.getReferences().values()) {
            if (child != null) {
                suggestionsHelper(child, results);
            }
        }
    }

}
