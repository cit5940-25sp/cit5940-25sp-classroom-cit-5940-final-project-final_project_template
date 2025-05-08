import com.google.gson.*;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

/**
 * Loads movie metadata from the TMDB API instead of CSV files.
 * This includes titles, vote counts, genres, cast, crew, and release year.
 */
public class MovieDataLoaderAPI {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0OGZmN2VmODgxMzc0MTJhN2EwODgwNjY5YjQ3NDhkMiIsIm5iZiI6MS43NDY3Mjg5NzU4ODk5OTk5ZSs5LCJzdWIiOiI2ODFjZjgwZjA3OTc3YWE2YWEzZWNhMzAiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.lbNKY-qA6QdqejO5OWT8Ac5TJHplvKelOaZCog_nzdA";

    /**
     * Fetches full movie metadata (title, cast, crew, genres, etc.) from TMDB API for the given number of pages.
     * Each page contains 20 movies from the "popular" category.
     *
     * @param numPages number of pages to load from the popular movie list
     * @return list of Movie objects with populated fields
     */
    public static List<Movie> loadMoviesFromAPI(int numPages) {
        List<Movie> movies = new ArrayList<>();

        for (int page = 1; page <= numPages; page++) {
            try {
                String url = BASE_URL + "/movie/popular?page=" + page;

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer " + BEARER_TOKEN)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        System.err.println("Failed to fetch page " + page + ": " + response.code());
                        continue;
                    }

                    JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                    JsonArray results = json.getAsJsonArray("results");

                    for (JsonElement element : results) {
                        JsonObject obj = element.getAsJsonObject();
                        int movieId = obj.get("id").getAsInt();
                        Movie detailedMovie = fetchMovieDetails(movieId);
                        if (detailedMovie != null) {
                            movies.add(detailedMovie);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error fetching page " + page + ": " + e.getMessage());
            }
        }

        return movies;
    }

    /**
     * Fetches detailed metadata of a single movie using its TMDB movie ID.
     *
     * @param movieId TMDB ID of the movie
     * @return Movie object with detailed fields filled
     * @throws IOException if API call fails
     */
    private static Movie fetchMovieDetails(int movieId) throws IOException {
        String url = BASE_URL + "/movie/" + movieId + "?append_to_response=credits";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + BEARER_TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return null;
            }

            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();

            Movie movie = new Movie();
            movie.setId(movieId);
            movie.setTitle(json.get("title").getAsString());

            if (json.has("vote_count")) {
                movie.setVoteCount(json.get("vote_count").getAsInt());
            }

            // Parse release year
            String releaseDate = json.has("release_date") ? json.get("release_date").getAsString() : "";
            if (releaseDate.length() >= 4) {
                movie.setReleaseYear(Integer.parseInt(releaseDate.substring(0, 4)));
            }

            // Parse genres
            Set<String> genres = new HashSet<>();
            JsonArray genresArray = json.getAsJsonArray("genres");
            for (JsonElement genreEl : genresArray) {
                genres.add(genreEl.getAsJsonObject().get("name").getAsString());
            }
            movie.setGenres(genres);

            // Parse cast
            Set<String> cast = new HashSet<>();
            JsonArray castArray = json.getAsJsonObject("credits").getAsJsonArray("cast");
            for (JsonElement el : castArray) {
                cast.add(el.getAsJsonObject().get("name").getAsString());
            }
            movie.setActors(cast);

            // Parse crew
            Set<String> directors = new HashSet<>();
            Set<String> writers = new HashSet<>();
            Set<String> composers = new HashSet<>();
            Set<String> cinematographers = new HashSet<>();
            JsonArray crewArray = json.getAsJsonObject("credits").getAsJsonArray("crew");

            for (JsonElement el : crewArray) {
                JsonObject crewMember = el.getAsJsonObject();
                String job = crewMember.get("job").getAsString();
                String name = crewMember.get("name").getAsString();

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
                    default:
                        break;
                }
            }

            movie.setDirectors(directors);
            movie.setWriters(writers);
            movie.setComposers(composers);
            movie.setCinematographers(cinematographers);

            return movie;
        }
    }
}
