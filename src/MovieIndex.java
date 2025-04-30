import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.json.JSONArray;
import org.json.JSONObject;

// Handles indexing and searching movies (uses map) (model)
public class MovieIndex implements IMovieIndex {
    private Map<String, IMovie> indexByTitle = new HashMap<>();
    private Map<String, Set<IMovie>> indexByContributor = new HashMap<>();


//public Map<Integer, Movie> parseMovies(String movieFile, String peopleFile) {
//    Map<Integer, IMovie> movieIndex = new HashMap<>();
//}


    public Map<Integer, IMovie> loadMovies(String movieFile) {
        Map<Integer, IMovie> movieMap = new HashMap<>();

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(movieFile));

            String[] header = reader.readNext();
            System.out.println("Headers: " + Arrays.toString(header));
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length > 17) {
                    String idStr = row[3].trim();
                    if (idStr.isEmpty()) {
                        continue;
                    }
                    int id = Integer.parseInt(idStr);
                    System.out.println("ID: " + id);
                    String title = row[17].trim();
                    String releaseDate = row[11].trim();
                    String genreJson = row[1].trim();

                    String year = "";
                    if (!releaseDate.isEmpty() && releaseDate.length() >= 4) {
                        year = releaseDate.substring(0, 4);
                    }

                    String fullTitle = title + (year.isEmpty() ? "" : " (" + year + ")");
                    String parsedYear = year;
                    int parsedYearCorrect = 0;
                    if (!year.isEmpty()) {
                        parsedYearCorrect = Integer.parseInt(year);
                    }
                    List<String> genres = new ArrayList<>();

                    if (!genreJson.isEmpty() && genreJson.startsWith("[")) {
                        JSONArray genresArray = new JSONArray(genreJson);
                        for (int i = 0; i < genresArray.length(); i++) {
                            JSONObject genreObj = genresArray.getJSONObject(i);
                            String genreName = genreObj.optString("name");
                            if (!genreName.isEmpty()) {
                                genres.add(genreName);
                            }
                        }
                    }
                    IMovie movie = new Movie(fullTitle, parsedYearCorrect, genres);
                    movieMap.put(id, movie);
                    // movieTitles.put(id, fullTitle);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return movieMap;
    }

//    public void loadCast(String creditFile, Map<Integer, IMovie> movieMap) {
//      //  Map<Integer, List<String>> cast = new HashMap<>();
//        int badRowCount = 0;
//        int skippedCrew = 0;
//        int skippedCast = 0;
//        try {
//            CSVParser parser = new CSVParserBuilder()
//                    .withSeparator(',')
//                    .withQuoteChar('"')
//                    .withEscapeChar('\\')
//                    .build();
//
//            CSVReader reader = new CSVReaderBuilder(new FileReader(creditFile))
//                    .withCSVParser(parser)
//                    .build();
//            String[] header = reader.readNext();
//            String[] row;
//
//            while ((row = reader.readNext()) != null) {
//                if (row.length < 4) {
//                    badRowCount++;
//                    System.err.println("Skipping malformed row: " + Arrays.toString(row));
//                    continue;
//                }
//                    int id;
//                    try {
//                        id = Integer.parseInt(row[0].trim());
//                    } catch (NumberFormatException e) {
//                        badRowCount++;
//                        System.err.println("Skipping row with invalid ID: " + Arrays.toString(row));
//                        continue;
//                    }
//
//                    if (!movieMap.containsKey(id)) {
//                        continue;
//                    } else {
//                    Movie movie = (Movie) movieMap.get(id);
//                        String casting = row[2];
//                        String crew = row[3];
//
//try {
//                    JSONArray castArray = new JSONArray(casting);
//                    for (int i = 0; i < castArray.length(); i++ ) {
//                        String name = castArray.getJSONObject(i).optString("name");
//                        if (!name.isEmpty()) {
//                            movie.addActor(name);
//                        }
//                    }
//                } catch (Exception e) {
//                skippedCast++;
//    System.err.println("Skipping invalid cast JSON for movie ID: " + id);
//}
//
//
//
//                    try {
//                    JSONArray crewArray = new JSONArray(crew);
//                    for (int i = 0; i < crewArray.length(); i++ ) {
//                        String nameCrew = crewArray.getJSONObject(i).optString("name");
//                       if (!nameCrew.isEmpty()) {
//                           movie.addContributor(nameCrew);
//                       }
//
//                    }
//                    } catch (Exception e) {
//                        skippedCrew++;
//                        System.err.println("Skipping invalid cast JSON for movie ID: " + id);
//                    }
//                }
//                System.out.println("Finished loading cast and crew.");
//                System.out.println("Skipped " + skippedCast + " invalid cast entries.");
//                System.out.println("Skipped " + skippedCrew + " invalid crew entries.");
//                System.out.println("Skipped " + badRowCount + " malformed or incomplete CSV rows.");
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (CsvValidationException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void loadCast(String creditFile, Map<Integer, IMovie> movieMap) {
        int skippedLines = 0;
        int castErrors = 0;
        int crewErrors = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(creditFile))) {
            String header = br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                try {
                    int firstComma = line.indexOf(',');
                    int secondComma = line.indexOf(',', firstComma + 1);

                    String idStr = line.substring(0, firstComma).trim();
                    int id = Integer.parseInt(idStr);

// Skip if ID not found in map
                    if (!movieMap.containsKey(id)) return;
                    Movie movie = (Movie) movieMap.get(id);

// Find end of cast JSON: first "]}" after second comma
                    int castStart = secondComma + 1;
                    int castEnd = line.indexOf("}]", castStart) + 2;
                    if (castEnd <= 1) throw new Exception("Cast JSON malformed");

                    String castJson = line.substring(castStart, castEnd);

                    int crewStart = line.indexOf("[{", castEnd);
                    int crewEnd = line.indexOf("}]", crewStart) + 2;
                    if (crewStart == -1 || crewEnd <= 1) throw new Exception("Crew JSON malformed");

                    String crewJson = line.substring(crewStart, crewEnd);

                    System.out.println("ID: " + id);
                    System.out.println("CAST JSON RAW: " + castJson);
                    System.out.println("CREW JSON RAW: " + crewJson);
                    // Parse cast
                    castJson = castJson.replaceAll("\"\"", "\"");
                    crewJson = crewJson.replaceAll("\"\"", "\"");
                    try {
                        JSONArray castArray = new JSONArray(castJson);
                        for (int i = 0; i < castArray.length(); i++) {
                            String name = castArray.getJSONObject(i).optString("name");
                            System.out.println("Adding actor: " + name + " to " + movie.getTitle());
                            if (!name.isEmpty()) movie.addActor(name);
                        }
                    } catch (Exception e) {
                        castErrors++;
                    }

                    try {
                        JSONArray crewArray = new JSONArray(crewJson);
                        for (int i = 0; i < crewArray.length(); i++) {
                            String name = crewArray.getJSONObject(i).optString("name");
                            System.out.println("Adding crew member: " + name + " to " + movie.getTitle());
                            if (!name.isEmpty()) movie.addContributor(name);
                        }
                    } catch (Exception e) {
                        crewErrors++;
                    }

                } catch (Exception e) {
                    skippedLines++;
                }
            }

            System.out.println("Finished loading cast and crew.");
            System.out.println("Skipped rows: " + skippedLines);
            System.out.println("Cast JSON errors: " + castErrors);
            System.out.println("Crew JSON errors: " + crewErrors);

        } catch (Exception e) {
            throw new RuntimeException("Failed to process cast file", e);
        }
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
