import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrieNode {
    private Map<Character, TrieNode> children;
    private boolean isEndOfWord;
    private List<Movie> movieReference;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        movieReference = new ArrayList<>();
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }

    public List<Movie> getMovieReference() {
        return movieReference;
    }

    public void addMovieReference(Movie movie) {
        movieReference.add(movie);
    }

    public TrieNode getChild(Character c) {
        return children.get(c);
    }

    public void addChild(Character c, TrieNode node) {
        children.put(c, node);
    }

}