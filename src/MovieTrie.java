import java.util.*;

/**
 * @author Ashley Wang
 */
public class MovieTrie {
    private static String creditsFilename = "tmdb_5000_credits.csv";
    private static String moviesFilename = "tmdb_5000_movies.csv";

    private TrieNode root;
    private List<Movie> allMovies = new ArrayList<>();
    private int limit = 10;

    public MovieTrie() {
        root = new TrieNode();
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNormalizedString(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    public TrieNode buildTrie() {
        allMovies = MovieDataLoader.loadMovies(creditsFilename, moviesFilename);
        return root;
    }

    public void insert(String title, Movie movie) {
        TrieNode node = root;
        for (char c : title.toCharArray()) {
            node = node.getChildren().computeIfAbsent(c, k -> new TrieNode());
        }
        node.setEndOfWord(true);
        node.addMovieReference(movie);
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : getNormalizedString(word).toCharArray()) {
            if (!node.getChildren().containsKey(c)) {
                return false;
            }
            node = node.getChild(c);
        }
        return node.isEndOfWord();
    }

    public List<String> getSuggestions(String prefix) {
        List<Movie> suggestions = new ArrayList<>();
        TrieNode node = root;
        for (char c : getNormalizedString(prefix).toCharArray()) {
            if (!node.getChildren().containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.getChild(c);
        }
        dfs(node, suggestions);
        suggestions.sort(Movie.byReverseWeightOrder());

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, suggestions.size()); i++) {
            result.add(suggestions.get(i).getTitle());
        }
        return result;
    }

    private void dfs(TrieNode node, List<Movie> suggestions) {
        if (node.isEndOfWord()) {
            suggestions.addAll(node.getMovieReference());
        }
        for (char c : node.getChildren().keySet()) {
            dfs(node.getChild(c), suggestions);
        }
    }

    public List<Movie> getAllMovieSuggestions(String prefix) {
        List<Movie> movies = new ArrayList<>();
        TrieNode node = root;
        for (char c : getNormalizedString(prefix).toCharArray()) {
            if (!node.getChildren().containsKey(c)) {
                return movies;
            }
            node = node.getChild(c);
        }
        dfsForMovies(node, movies);
        return movies;
    }

    private void dfsForMovies(TrieNode node, List<Movie> movies) {
        if (node.isEndOfWord()) {
            movies.addAll(node.getMovieReference());
        }
        for (char c : node.getChildren().keySet()) {
            dfsForMovies(node.getChild(c), movies);
        }
    }

    public TrieNode getRoot() {
        return root;
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }
}
