import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Movies class that handles loading, storing, and managing movie data.
 * It can read a list of movies from a file, track their genres, cast, and connections.
 */
public class Movies {
    // Stores all movies with their details (cast and genres)
    private final HashMap<String, HashMap<String, List<String>>> allMovies = new HashMap<>();
    //key is genre name and value is number of movies with that genre
    private final HashMap<String, Integer> allGenres = new HashMap<>();

    /**
     * Constructor that initializes the Movies object by loading data from the given file.
     * @param filePath Path to the file containing movie data.
     */
    public Movies(String filePath) {
        loadMovies(filePath);
    }

    /**
     * Loads movies from the specified file path and populates the movies and genres collections.
     * @param filePath Path to the file containing movie data.
     */
    public void loadMovies(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);

                // Extract movie name, cast, and genres
                String movieName = parts[0].trim().toLowerCase().replaceAll("^\"|\"$", "");
                String castTemp = parts.length > 1 ? parts[1].trim().replaceAll("^\"|\"$", "") : "";
                String genresTemp = parts.length > 2 ? parts[2].trim().replaceAll("^\"|\"$", "") : "";

                // Process cast and genres
                List<String> castCrew = castTemp.isEmpty() ? new ArrayList<>() :
                        Arrays.stream(castTemp.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toList());

                List<String> genres = genresTemp.isEmpty() ? new ArrayList<>() :
                        Arrays.stream(genresTemp.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toList());

                // Add genres to the genre count map
                for (String g : genres) {
                    if (!allGenres.containsKey(g)) {
                        allGenres.put(g, 1);
                    } else {
                        allGenres.put(g, allGenres.get(g) + 1);
                    }
                }

                // Store movie details
                HashMap<String, List<String>> details = new HashMap<>();
                details.put("castAndCrew", castCrew);
                details.put("genres", genres);
                allMovies.put(movieName, details);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of common cast members between two movies.
     * @param movie1 Title of the first movie.
     * @param movie2 Title of the second movie.
     * @return List of common cast members, or an empty list if none exist.
     */
    public List<String> getConnection(String movie1, String movie2) {
        List<String> connections = new LinkedList<>();

        HashMap<String, List<String>> details1 = allMovies.get(movie1.toLowerCase());
        HashMap<String, List<String>> details2 = allMovies.get(movie2.toLowerCase());

        if (details1 == null || details2 == null) {
            return connections; // Return empty list if any movie is not found
        }

        List<String> cast1 = details1.getOrDefault("castAndCrew", new ArrayList<>());
        List<String> cast2 = details2.getOrDefault("castAndCrew", new ArrayList<>());
        Set<String> set1 = new HashSet<>(cast1);

        for (String staff : cast2) {
            if (set1.contains(staff)) {
                connections.add(staff);
            }
        }

        return connections;
    }

    /**
     * Retrieves the list of genres for a given movie.
     * @param movieTitle Title of the movie.
     * @return List of genres or an empty list if the movie is not found.
     */
    public List<String> getMovieGenres(String movieTitle) {
        return allMovies.getOrDefault(movieTitle, new HashMap<>()).getOrDefault("genres", new ArrayList<>());
    }

    /**
     * Retrieves the list of all genres along with their counts.
     * @return A map of genres and their corresponding movie counts.
     */
    public Map<String, Integer> getAllGenres() {
        return new HashMap<>(allGenres);
    }

    /**
     * Returns the set of all movie titles loaded in the system.
     * @return Set of movie titles.
     */
    public Set<String> getAllTitles() {
        return allMovies.keySet();
    }

    /**
     * Selects a random movie title from the loaded movies.
     * @return A random movie title or null if no movies are loaded.
     */
    public String getRandomMovie() {
        List<String> titles = new ArrayList<>(allMovies.keySet());

        if (titles.isEmpty()) {
            return null;
        }

        Random rand = new Random();

        return titles.get(rand.nextInt(titles.size()));
    }

    /**
     * Creates an autocomplete file containing all movie titles for fast searching.
     * @param movieTitles Collection of movie titles to be written to the file.
     * @return Collection of written lines in the file.
     */
    public Collection<String> createAutocompleteFile(Collection<String> movieTitles) {
        List<String> lines = new ArrayList<>();

        try (BufferedWriter buff = new BufferedWriter(new FileWriter("src/autocomplete.txt"))) {
            buff.write(String.valueOf(movieTitles.size()));
            buff.newLine();

            for (String title : movieTitles) {
                String toWrite = "0\t" + title;
                buff.write(toWrite);
                buff.newLine();
                lines.add(toWrite);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

    /**
     * Main method for testing the Movies class.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Movies movies = new Movies("src/tmdb_data.txt");
        Collection<String> output = movies.createAutocompleteFile(movies.getAllTitles());
        System.out.println(output);
    }
}
