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
 * @author Ashley Wang
 */
public class MovieTrie {
    public static String CREDITS_FILENAME = "tmdb_5000_credits.csv";
    public static String MOVIES_FILENAME = "tmdb_5000_movies.csv";

    private TrieNode root;
    private int limit = 10;
    private Map<String, Movie> movieMap = new HashMap<>();


    public MovieTrie() {
        root = new TrieNode();
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

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

    private String getNormalizdString(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    public TrieNode buildTrie() {
        loadTMDBCredits(CREDITS_FILENAME);
        loadTMDBMoives(MOVIES_FILENAME);
        for (Map.Entry<String, Movie> entry : movieMap.entrySet()) {
            Movie movie = entry.getValue();
            insert(getNormalizdString(movie.getTitle()), movie);
        }
        return root;
    }

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
        for (char c : getNormalizdString(prefix).toCharArray()) {
            if (!node.getChildren().containsKey(c)) {
                return movies;
            }
            node = node.getChild(c);
        }
        dfsForMovies(node, movies);
        return movies;
    }

    private void dfsForMovies(TrieNode node, List<Movie> movies) {
        if (node.isEndOfWord() && node.getMovieReference() != null) {
            movies.addAll(node.getMovieReference());
        }
        for (char c : node.getChildren().keySet()) {
            dfsForMovies(node.getChild(c), movies);
        }
    }

    public TrieNode getRoot() {
        return root;
    }
}