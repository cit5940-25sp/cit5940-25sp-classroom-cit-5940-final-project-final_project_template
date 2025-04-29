import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

// Handles indexing and searching movies (uses map) (model)
public class MovieIndex implements IMovieIndex {
    private Map<String, IMovie> indexByTitle = new HashMap<>();
    private Map<String, Set<IMovie>> indexByContributor = new HashMap<>();




//public Map<Integer, Movie> parseMovies(String movieFile, String peopleFile) {
//    Map<Integer, IMovie> movieIndex = new HashMap<>();
//}

    @Override
    public Map<Integer, String> loadMovies(String movieFile) {
        Map<Integer, String> movieTitles = new HashMap<>();

       CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(movieFile));

            String[] header = reader.readNext();
            System.out.println("Headers: " + Arrays.toString(header));
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length > 17) {
                    int id = Integer.parseInt(row[3].trim());
                    System.out.println("ID: " + id);
                    String title = row[17].trim();
                    String releaseDate = row[11].trim();

                    String year = "";
                    if (!releaseDate.isEmpty() && releaseDate.length() >= 4) {
                        year = releaseDate.substring(0,4);
                    }
                    String fullTitle = title + (year.isEmpty() ? "" : " (" + year + ")");
                    movieTitles.put(id, fullTitle);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return movieTitles;
    }

    public Map<Integer, List<String>> loadCast(String movieFile) {
        Map<Integer, List<String>> cast = new HashMap<>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(movieFile));
            String[] header = reader.readNext();
            String[] row = reader.readNext();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void loadMovies(List<IMovie> movieList) {

    }

    @Override
    public IMovie getMovieByTitle(String title) {
        return indexByTitle.get(title.toLowerCase());
    }

    @Override
    public List<String> autocomplete(String input) {
        String inputLowerCase = input.toLowerCase();
        List<String> results = new ArrayList<>();

        for (String title : indexByTitle.keySet()) {
            if (title.startsWith(inputLowerCase)) {
                results.add(indexByTitle.get(title).getTitle());
            }
        }
        return results;
    }

    @Override
    public List<IMovie> getConnectedMovies(IMovie movie) {
        Set<IMovie> connectedMovies = new HashSet<>();

        for (String person : movie.getAllContributors()) {
            String name = person.toLowerCase();

            if (indexByContributor.containsKey(name)) {
                connectedMovies.addAll(indexByContributor.get(name));
            }
        }

        connectedMovies.remove(movie);
        return new ArrayList<>(connectedMovies);
    }

    @Override
    public boolean containsMovie(String title) {
        return indexByTitle.containsKey(title.toLowerCase());
    }
}
