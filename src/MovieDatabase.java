import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MovieDatabase {
    private final TMDBClient tmdb;
    private final Map<String, Movie> movieCache = new HashMap<>();
    private final Map<String, List<Movie>> actorCache = new HashMap<>();
    private final Map<Long, List<Movie>> similarCache = new HashMap<>();

    public MovieDatabase(String apiKey) {
        this.tmdb = new TMDBClient();
    }

    public Movie findByTitle(String title) {
        if (movieCache.containsKey(title.toLowerCase().trim())) {
            return movieCache.get(title);
        }

        Movie movie = tmdb.fetchMovieByTitle(title);
        if (movie != null) {
            movieCache.put(title, movie);
        }

        return movie;
    }

    public List<Movie> findConnections(Movie movie) {
        if (similarCache.containsKey(movie.getMovieId())) {
            return similarCache.get(movie.getMovieId());
        }

        List<Movie> similar = tmdb.fetchSimilarMovies(movie);
        if (similar != null) {
            similarCache.put(movie.getMovieId(), similar);
        }

        return similar;
    }


    public List<Movie> findByActor(String actor) {
        if (actorCache.containsKey(actor)) {
            return actorCache.get(actor);
        }

        List<Movie> movies = tmdb.fetchMoviesByActor(actor);
        if (movies != null) {
            actorCache.put(actor, movies);
        }

        return movies;
    }


    public void clearCache() {
        movieCache.clear();
        actorCache.clear();
        similarCache.clear();
    }

    public Set<String> getAllTitles() {
        System.out.println("Total movie titles: " + movieCache.size());

        return movieCache.keySet();
    }

    public List<String> getMatchingTitles(String prefix) {
        String lower = prefix.toLowerCase();
        return movieCache.keySet().stream()
            .filter(title -> title.toLowerCase().startsWith(lower))
            .limit(5)
            .toList();
    }

    public String preloadPopularMovies() {
        File cacheFile = new File("movie_cache.json");
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        List<Movie> popular;

        if (cacheFile.exists()) {
            // Load from JSON cache
            try {
                Movie[] cached = mapper.readValue(cacheFile, Movie[].class);
                popular = Arrays.asList(cached);
                for (Movie movie : popular) {
                    movieCache.put(movie.getTitle(), movie);
                }
                return "Loaded " + popular.size() + " movies from local cache.";
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to load cache file. " + e.getMessage();
            }
        } else {
            // Fetch from TMDB and write to cache
            int maxPages = 25;
            popular = tmdb.fetchPopularMovies(maxPages);
            for (Movie movie : popular) {
                movieCache.put(movie.getTitle(), movie);
            }

            try {
                mapper.writeValue(cacheFile, popular);
            } catch (IOException e) {
                e.printStackTrace();
                return "Fetched movies, but failed to write cache. " + e.getMessage();
            }

            return "Fetched and cached " + popular.size() + " movies.";
        }
    }

    public Movie getRandomMovie() {
        if (movieCache.isEmpty()) {
            preloadPopularMovies();
        }

        if (movieCache.isEmpty()) return null;

        List<Movie> all = new ArrayList<>(movieCache.values());
        return all.get(new Random().nextInt(all.size()));
    }
}
