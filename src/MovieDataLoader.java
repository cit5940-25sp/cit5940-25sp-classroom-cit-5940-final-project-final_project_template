import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for loading movie data from TMDB-style CSV files containing metadata and credits.
 * It parses cast, crew, genres, and other relevant fields into structured {@link Movie} objects.
 *
 * The credits file should include `movie_id`, `title`, `cast`, and `crew`.
 * The movies file should include `id`, `genres`, `vote_count`, and `release_date`.
 *
 * @author Jianing Yin
 */
public class MovieDataLoader {

    /**
     * Loads movies from the given credits and movies metadata CSV files.
     *
     * @param creditsFile Path to the credits CSV file
     * @param moviesFile Path to the movies metadata CSV file
     * @return A list of {@link Movie} objects populated with information from both files
     */
    public static List<Movie> loadMovies(String creditsFile, String moviesFile) {
        Map<String, Movie> movieMap = new HashMap<>();
        Gson gson = new Gson();

        // Step 1: Load credits
        try (BufferedReader br = new BufferedReader(new FileReader(creditsFile));
             CSVParser parser = new CSVParser(br, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : parser) {
                String id = record.get("movie_id");
                String title = record.get("title");
                String castJson = record.get("cast");
                String crewJson = record.get("crew");

                JsonArray castArray = gson.fromJson(castJson, JsonArray.class);
                JsonArray crewArray = gson.fromJson(crewJson, JsonArray.class);

                Set<String> cast = new HashSet<>();
                for (int i = 0; i < castArray.size(); i++) {
                    cast.add(castArray.get(i).getAsJsonObject().get("name").getAsString());
                }

                Set<String> directors = new HashSet<>();
                Set<String> composers = new HashSet<>();
                Set<String> writers = new HashSet<>();
                Set<String> cinematographers = new HashSet<>();
                for (int i = 0; i < crewArray.size(); i++) {
                    JsonObject crew = crewArray.get(i).getAsJsonObject();
                    String job = crew.get("job").getAsString();
                    String name = crew.get("name").getAsString();
                    switch (job) {
                        case "Director" -> directors.add(name);
                        case "Original Music Composer" -> composers.add(name);
                        case "Writer" -> writers.add(name);
                        case "Director of Photography" -> cinematographers.add(name);
                    }
                }

                Movie movie = new Movie();
                movie.setTitle(title);
                movie.setActors(cast);
                movie.setDirectors(directors);
                movie.setComposers(composers);
                movie.setWriters(writers);
                movie.setCinematographers(cinematographers);

                movieMap.put(id, movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 2: Load movie metadata
        try (BufferedReader br = new BufferedReader(new FileReader(moviesFile));
             CSVParser parser = new CSVParser(br, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : parser) {
                String id = record.get("id");
                if (!movieMap.containsKey(id)) continue;

                Movie movie = movieMap.get(id);
                movie.setVoteCount(Integer.parseInt(record.get("vote_count")));

                JsonArray genresArray = gson.fromJson(record.get("genres"), JsonArray.class);
                Set<String> genres = new HashSet<>();
                for (int i = 0; i < genresArray.size(); i++) {
                    genres.add(genresArray.get(i).getAsJsonObject().get("name").getAsString());
                }
                movie.setGenres(genres);

                String releaseDate = record.get("release_date");
                if (releaseDate != null && releaseDate.length() >= 4) {
                    movie.setReleaseYear(Integer.parseInt(releaseDate.substring(0, 4)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(movieMap.values());
    }
}
