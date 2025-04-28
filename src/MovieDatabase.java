import java.util.*;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class MovieDatabase {
    private Map<String, Movie> movieNameIndex = new HashMap<>();
    private Map<Integer, Person> personIndex = new HashMap<>();
    private Map<String, List<Movie>> genreIndex = new HashMap<>();
    private MovieTrie movieTrie = new MovieTrie();

    public void addMovie(Movie movie) {
        movieNameIndex.put(movie.getTitle(), movie);
        movieTrie.insert(movie.getTitle());
    }

    public Movie findMovie(String name) {
        return movieNameIndex.get(name);
    }

    public List<String> getAutocompleteSuggestions(String prefix, int k) {
        return movieTrie.getWordsWithPrefix(prefix, k);
    }

    public Connection validateConnection(Movie m1, Movie m2) {
        // Check for shared actors
        for (Person actor : m1.getCast()) {
            if (m2.getCast().contains(actor)) {
                return new Connection(m1, m2, actor, "actor");
            }
        }
        // Check for shared directors
        for (Person director : m1.getCrew()) {
            if (m2.getCrew().contains(director)) {
                return new Connection(m1, m2, director, "director");
            }
        }
        // No valid connection found
        return null;
    }

    // ==================== CSV LOADING AND PARSING ====================
    public void loadFromCSV(String moviesPath, String creditsPath) throws IOException, CsvValidationException {
        Map<Integer, Movie> idToMovie = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(moviesPath))) {
            String[] fields;
            reader.readNext(); // skip header
            while ((fields = reader.readNext()) != null) {
                int id = Integer.parseInt(fields[3]);
                String title = fields[17];
                List<String> genres = parseGenres(fields[1]);
                Movie movie = new Movie(id, title, 0, genres, new ArrayList<>(), new ArrayList<>());
                idToMovie.put(id, movie);
                addMovie(movie);
            }
        }

        // 2. Load credits (cast and crew) using OpenCSV
        try (CSVReader reader = new CSVReader(new FileReader(creditsPath))) {
            String[] fields;
            reader.readNext(); // skip header
            while ((fields = reader.readNext()) != null) {
                // Skip lines that don't have at least 4 columns
                if (fields.length < 4) {
                    System.err.println("Skipping malformed credits row: " + Arrays.toString(fields));
                    continue;
                }
                int movieId = Integer.parseInt(fields[0]);
                Movie movie = idToMovie.get(movieId);
                if (movie == null) continue;
                List<Person> cast = parsePeople(fields[2], "cast");
                List<Person> crew = parsePeople(fields[3], "crew");
                movie.getCast().addAll(cast);
                movie.getCrew().addAll(crew);
            }
        }
    }

    // Helper to parse genres from JSON string
    private List<String> parseGenres(String json) {
        List<String> genres = new ArrayList<>();
        if (json == null || json.isEmpty() || !json.trim().startsWith("[")) {
            return genres;
        }
        // Convert CSV-style double double-quotes to single double-quote
        json = json.replaceAll("\"\"", "\"");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                genres.add(arr.getJSONObject(i).getString("name"));
            }
        } catch (Exception e) {
            System.err.println("Skipping malformed JSON for genres: " + json);
        }
        return genres;
    }

    // Helper to parse cast/crew from JSON string
    private List<Person> parsePeople(String json, String type) {
        List<Person> people = new ArrayList<>();
        if (json == null || json.isEmpty() || !json.trim().startsWith("[")) {
            return people;
        }
        // Convert CSV-style double double-quotes to single double-quote
        json = json.replaceAll("\"\"", "\"");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                int id = obj.getInt("id");
                String name = obj.getString("name");
                String role = type.equals("cast") ? obj.optString("character", "") : obj.optString("job", "");
                people.add(new Person(id, name, role));
            }
        } catch (Exception e) {
            System.err.println("Skipping malformed JSON for " + type + ": " + json);
        }
        return people;
    }
}
