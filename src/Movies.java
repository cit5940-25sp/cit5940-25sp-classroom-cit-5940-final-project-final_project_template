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

    public void loadMovies(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);

                String movieName = parts[0].trim().toLowerCase().replaceAll("^\"|\"$", "");
                String castTemp = parts.length > 1 ? parts[1].trim().replaceAll("^\"|\"$", "") : "";
                String genresTemp = parts.length > 2 ? parts[2].trim().replaceAll("^\"|\"$", "") : "";

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

                for (String g : genres) {
                    if (!allGenres.containsKey(g)) {
                        allGenres.put(g, 1);
                    } else {
                        allGenres.put(g, allGenres.get(g) + 1);
                    }
                }

                HashMap<String, List<String>> details = new HashMap<>();
                details.put("castAndCrew", castCrew);
                details.put("genres", genres);
                allMovies.put(movieName, details);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Takes in the name of the previous movie and the current
    that was played and returns the name of the connection
    returns null if there are no connections
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

    public List<String> getMovieGenres(String movieTitle) {
        return allMovies.getOrDefault(movieTitle, new HashMap<>()).getOrDefault("genres", new ArrayList<>());
    }

    public Set<String> getAllTitles() {
        return allMovies.keySet();
    }

    public String getRandomMovie() {
        List<String> titles = new ArrayList<>(allMovies.keySet());

        if (titles.isEmpty()) {
            return null;
        }

        Random rand = new Random();

        return titles.get(rand.nextInt(titles.size()));
    }

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
