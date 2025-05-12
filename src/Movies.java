import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Movies {
    private final HashMap<String, HashMap<String, List<String>>> allMovies = new HashMap<>();
    //key is genre name and value is number of movies with that genre
    private final HashMap<String, Integer> allGenres = new HashMap<>();

    public Movies(String filePath) {
        loadMovies(filePath);
    }

    /**
     * Loads movies from the specified file path and populates the movies and genres collections.
     * This method processes each line of the file, extracting the movie title, cast, and genres.
     * It ensures that the cast and genres are stored without duplicates using HashSet.
     * @param filePath Path to the file containing movie data.
     */
    public void loadMovies(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);

                // Extract movie name, cast, and genres with trimming and formatting
                String movieName = parts[0].trim().toLowerCase().replaceAll("^\"|\"$", "");
                String castTemp = parts.length > 1 ? parts[1].trim().replaceAll("^\"|\"$", "") : "";
                String genresTemp = parts.length > 2 ? parts[2].trim().replaceAll("^\"|\"$", "") : "";

                // Convert cast list to HashSet to remove duplicates and then back to List
                List<String> castCrew = castTemp.isEmpty() ? new ArrayList<>() :
                        Arrays.stream(castTemp.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();

                HashSet<String> castCrewHash = new HashSet<>(castCrew);
                List<String> castCrewList = new ArrayList<>(castCrewHash);

                // Convert genres list to HashSet to remove duplicates and then back to List
                List<String> genres = genresTemp.isEmpty() ? new ArrayList<>() :
                        Arrays.stream(genresTemp.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();

                // Populate genre count map (ensuring unique genres)
                for (String g : genres) {
                    if (!allGenres.containsKey(g)) {
                        allGenres.put(g, 1);
                    } else {
                        allGenres.put(g, allGenres.get(g) + 1);
                    }
                }

                HashSet<String> genresHash = new HashSet<>(genres);
                List<String> genresList = new ArrayList<>(genresHash);

                // Store the movie details (cast and genres)
                HashMap<String, List<String>> details = new HashMap<>();
                details.put("castAndCrew", castCrewList);
                details.put("genres", genresList);
                allMovies.put(movieName, details);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds the list of common cast members between two movies.
     * This method checks the cast of both movies and identifies shared cast members.
     * @param movie1 Title of the first movie.
     * @param movie2 Title of the second movie.
     * @return List of common cast members between the two movies, or an empty list if none exist.
     */
    public List<String> getConnection(String movie1, String movie2) {
        List<String> connections = new LinkedList<>();

        List<String> cast1 = allMovies.get(movie1).get("castAndCrew");
        List<String> cast2 = allMovies.get(movie2).get("castAndCrew");
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

    public static void main(String[] args) {
        Movies movies = new Movies("src/tmdb_data.txt");
        Collection<String> output = movies.createAutocompleteFile(movies.getAllTitles());
        System.out.println(output);
    }
}
