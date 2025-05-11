import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Movies {

    private final HashMap<String, HashMap<String, List<String>>> allMovies = new HashMap<>();
    private final HashMap<String, Integer> allGenres = new HashMap<>();

        // load data: create a Movie object for each movie
        // in the file and put it in the HashSet
        // look for name and year and append it in format "Movie Name, (year)"

        // get genre and make a HashSet with all the Staff. Make an AbstractMap.Entry<genre, staff>

        // allMovies.put("Movie Name, (year)", AbstractMap.Entry<genre, staff>)


    public Movies(String filePath) {
        loadMovies(filePath);
    }

    public void loadMovies(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);

                String movieName = parts[0].trim().replaceAll("^\"|\"$", "");
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

                HashMap<String, List<String>> details = new HashMap<>(2);
                details.put("castAndCrew", castCrew);
                details.put("genres", genres);
                allMovies.put(movieName, details);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    use all genres map to return a TreeMap with 3 elements. Keys should be:
    "easy", "intermediate" and "hard". the values should be the lists with the
    genres according to each level
     */

    public TreeMap<String, List<String>> getAllGenres() {

        TreeMap<Integer, String> orderedGenres = new TreeMap<>();
        allGenres.forEach((k, v) -> {
            orderedGenres.put(v, k);
        });

        int hard = orderedGenres.size();
        int easy = hard / 3;
        int intermediate = easy * 2;

        String[] genres = new String[hard];

        for (Map.Entry<Integer, String> entry : orderedGenres.descendingMap().entrySet()) {

        }




        TreeMap<String, List<String>> allGenres = new TreeMap<>();

    }



    /*
    Takes in the name of the previous movie and the current
    that was played and returns the name of the connection
    returns null if there are no connections
     */
    public List<String> getConnection (String prev, String curr) {
        List<String> connections = new LinkedList<>();

        List<String> cast1 = allMovies.get(prev).get("castAndCrew");
        List<String> currStaff = allMovies.get(curr).get("castAndCrew");
        Set<String> prevStaff = new HashSet<>(cast1);

        for (String staff : currStaff) {
            if (prevStaff.contains(staff)) {
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

    // add getter genre

}


