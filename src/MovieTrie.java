import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class represents a Trie data structure for movie information.
 * It loads movie data from two CSV files: "tmdb_5000_credits.csv" and "tmdb_5000_movies.csv".
 * The Trie can be used to search for movies by title prefix and get movie suggestions.
 *
 * @author Ashley Wang
 */
public class MovieTrie {
    /**
     * The filename of the credits CSV file.
     */
    public static String CREDITS_FILENAME = "tmdb_5000_credits.csv";
    /**
     * The filename of the movies CSV file.
     */
    public static String MOVIES_FILENAME = "tmdb_5000_movies.csv";

    /**
     * The root node of the Trie.
     */
    private TrieNode root;
    /**
     * The maximum number of suggestions to return.
     */
    private int limit = 10;
    /**
     * A map that stores movie information with movie ID as the key.
     */
    private Map<String, Movie> movieMap = new HashMap<>();

    /**
     * Constructs a new MovieTrie object and initializes the root node.
     */
    public MovieTrie() {
        root = new TrieNode();
    }

    /**
     * Sets the maximum number of suggestions to return.
     *
     * @param limit the maximum number of suggestions
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Loads movie credits data from the specified CSV file.
     *
     * @param fileName the name of the credits CSV file
     */
    private void loadTMDBCredits(String fileName) {
        Gson gson = new Gson();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName));
             CSVParser csvParser = new CSVParser(br, CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                String id = csvRecord.get("movie_id");
                String title = csvRecord.get("title");
                String castJson = csvRecord.get("cast");
                String crewJson = csvRecord.get("crew");

                JsonArray castArray = gson.fromJson(castJson, JsonArray.class);
                JsonArray crewArray = gson.fromJson(crewJson, JsonArray.class);

                Set<String> castNames = new HashSet<>();
                for (int i = 0; i < castArray.size(); i++) {
                    JsonObject castObj = castArray.get(i).getAsJsonObject();
                    castNames.add(castObj.get("name").getAsString());
                }

                Set<String> directors = new HashSet<>();
                Set<String> composers = new HashSet<>();
                Set<String> writers = new HashSet<>();
                Set<String> cinematographers = new HashSet<>();

                for (int i = 0; i < crewArray.size(); i++) {
                    JsonObject crewObj = crewArray.get(i).getAsJsonObject();
                    String job = crewObj.get("job").getAsString();
                    String name = crewObj.get("name").getAsString();
                    switch (job) {
                        case "Director":
                            directors.add(name);
                            break;
                        case "Original Music Composer":
                            composers.add(name);
                            break;
                        case "Writer":
                            writers.add(name);
                            break;
                        case "Director of Photography":
                            cinematographers.add(name);
                            break;
                    }
                }

                Movie movie = new Movie();
                movie.setTitle(title);
                movie.setActors(castNames);
                movie.setDirectors(directors);
                movie.setComposers(composers);
                movie.setWriters(writers);
                movie.setCinematographers(cinematographers);

                movieMap.put(id, movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads movie information data from the specified CSV file.
     *
     * @param fileName the name of the movies CSV file
     */
    private void loadTMDBMoives(String fileName) {
        Gson gson = new Gson();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName));
             CSVParser csvParser = new CSVParser(br, CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord csvRecord : csvParser) {

                String id = csvRecord.get("id");
                if (!movieMap.containsKey(id)) {
                    continue;
                }
                Movie movie = movieMap.get(id);

                movie.setVoteCount(Integer.parseInt(csvRecord.get("vote_count")));

                String genresJson = csvRecord.get("genres");
                JsonArray genresArray = gson.fromJson(genresJson, JsonArray.class);
                Set<String> genreNames = new HashSet<>();
                for (int i = 0; i < genresArray.size(); i++) {
                    JsonObject genreObj = genresArray.get(i).getAsJsonObject();
                    genreNames.add(genreObj.get("name").getAsString());
                }
                movie.setGenres(genreNames);

                String releaseDate = csvRecord.get("release_date");
                if (releaseDate != null && !releaseDate.isEmpty()) {
                    String[] parts = releaseDate.split("/");
                    if (parts.length > 0) {
                        movie.setReleaseYear(Integer.parseInt(releaseDate.substring(0, 4)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Normalizes a string by removing non-alphanumeric characters and converting to lowercase.
     *
     * @param s the input string
     * @return the normalized string
     */
    private String getNormalizdString(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    /**
     * Builds the Trie by loading movie data from CSV files and inserting movie titles into the Trie.
     *
     * @return the root node of the Trie
     */
    public TrieNode buildTrie() {
        loadTMDBCredits(CREDITS_FILENAME);
        loadTMDBMoives(MOVIES_FILENAME);
        for (Map.Entry<String, Movie> entry : movieMap.entrySet()) {
            Movie movie = entry.getValue();
            insert(getNormalizdString(movie.getTitle()), movie);
        }
        return root;
    }

    /**
     * Inserts a movie title and its corresponding movie object into the Trie.
     *
     * @param title the normalized movie title
     * @param movie the movie object
     */
    public void insert(String title, Movie movie) {
        TrieNode node = root;
        for (char c : title.toCharArray()) {
            if (!node.getChildren().containsKey(c)) {
                node.addChild(c, new TrieNode());
            }
            node = node.getChild(c);
        }
        node.setEndOfWord(true);
        node.addMovieReference(movie);
    }

    /**
     * Searches for a movie title in the Trie.
     *
     * @param word the movie title to search for
     * @return true if the movie title is found, false otherwise
     */
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : getNormalizdString(word).toCharArray()) {
            if (!node.getChildren().containsKey(c)) {
                return false;
            }
            node = node.getChild(c);
        }
        return node.isEndOfWord();
    }

    /**
     * Gets a list of movie title suggestions based on the given prefix.
     *
     * @param prefix the prefix to search for
     * @return a list of movie title suggestions
     */
    public List<String> getSuggestions(String prefix) {
        List<Movie> suggestions = new ArrayList<>();
        TrieNode node = root;
        for (char c : getNormalizdString(prefix).toCharArray()) {
            if (!node.getChildren().containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.getChild(c);
        }
        dfs(node, suggestions);
        suggestions.sort(Movie.byReverseWeightOrder());

        List<String> result = new ArrayList<>();
        if (suggestions.size() > this.limit) {
            suggestions = suggestions.subList(0, this.limit);
        }
        suggestions.forEach(s -> result.add(s.getTitle()));
        return result;
    }

    /**
     * Performs a depth-first search on the Trie to find all movies starting from the given node.
     *
     * @param node        the starting node for the search
     * @param suggestions the list to store the found movies
     */
    private void dfs(TrieNode node, List<Movie> suggestions) {
        if (node.isEndOfWord()) {
            suggestions.addAll(node.getMovieReference());
        }
        for (char c : node.getChildren().keySet()) {
            dfs(node.getChild(c), suggestions);
        }
    }

    /**
     * Gets a list of all movie objects based on the given prefix.
     *
     * @param prefix the prefix to search for
     * @return a list of movie objects
     */
    public List<Movie> getAllMovieSuggestions(String prefix) {
        List<Movie> movies = new ArrayList<>();
        TrieNode node = root;
        for (char c : getNormalizdString(prefix).toCharArray()) {
            if (!node.getChildren().containsKey(c)) {
                return movies;
            }
            node = node.getChild(c);
        }
        dfsForMovies(node, movies);
        return movies;
    }

    /**
     * Performs a depth-first search on the Trie to find all movie objects starting from the given node.
     *
     * @param node   the starting node for the search
     * @param movies the list to store the found movie objects
     */
    private void dfsForMovies(TrieNode node, List<Movie> movies) {
        if (node.isEndOfWord() && node.getMovieReference() != null) {
            movies.addAll(node.getMovieReference());
        }
        for (char c : node.getChildren().keySet()) {
            dfsForMovies(node.getChild(c), movies);
        }
    }

    /**
     * Gets the root node of the Trie.
     *
     * @return the root node of the Trie
     */
    public TrieNode getRoot() {
        return root;
    }
}