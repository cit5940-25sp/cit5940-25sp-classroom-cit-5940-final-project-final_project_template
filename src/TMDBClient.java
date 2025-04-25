import java.net.http.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.IOException;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

/**
 * Client for accessing TMDB (The Movie Database) API
 */
public class TMDBClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public TMDBClient() {
        this.apiKey = ConfigLoader.get("tmdb.api.key");
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public Movie fetchMovieByTitle(String title) {
        try {
            String encoded = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String url = BASE_URL + "/search/movie?query=" + encoded + "&api_key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            JsonNode results = root.path("results");

            if (results.isArray() && results.size() > 0) {
                long id = results.get(0).get("id").asLong();
                return fetchMovieDetailsById(id);
            }
        } catch (Exception e) {
            System.err.println("fetchMovieByTitle error: " + e.getMessage());
        }
        return null;
    }

    private Movie fetchMovieDetailsById(long id) {
        try {
            String url = BASE_URL + "/movie/" + id + "?api_key=" + apiKey + "&append_to_response=credits";
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            String title = root.path("title").asText();
            int year = Integer.parseInt(root.path("release_date").asText().split("-")[0]);

            Set<String> genres = new HashSet<>();
            for (JsonNode genre : root.path("genres")) {
                genres.add(genre.path("name").asText());
            }

            Set<String> actors = new HashSet<>();
            for (JsonNode cast : root.path("credits").path("cast")) {
                if (actors.size() >= 5) break;
                actors.add(cast.path("name").asText());
            }

            Set<String> directors = new HashSet<>();
            for (JsonNode crew : root.path("credits").path("crew")) {
                if ("Director".equals(crew.path("job").asText())) {
                    directors.add(crew.path("name").asText());
                }
            }

            // ⚠️ 根據你的 Movie 類別建構子來調整這裡
            return new Movie(id, title, year, genres, actors, directors, Set.of(), Set.of(), Set.of());
        } catch (Exception e) {
            System.err.println("fetchMovieDetailsById error: " + e.getMessage());
        }
        return null;
    }

}

