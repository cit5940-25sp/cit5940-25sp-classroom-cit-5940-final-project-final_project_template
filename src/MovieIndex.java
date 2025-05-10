import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MovieIndex {
    private Map<String, Set<Movie>> actorMap;
    private Map<String, Set<Movie>> directorMap;
    private Map<String, Set<Movie>> composerMap;
    private Map<String, Set<Movie>> writerMap;
    private Map<String, Set<Movie>> cinematographerMap;
    private MovieTrie movieTrie;
    private List<Movie> allMovies = new ArrayList<>();

    public MovieIndex() {
        // Initialize the maps
        actorMap = new HashMap<>();
        directorMap = new HashMap<>();
        composerMap = new HashMap<>();
        writerMap = new HashMap<>();
        cinematographerMap = new HashMap<>();
        movieTrie = new MovieTrie();
        movieTrie.buildTrie();
        allMovies = movieTrie.getAllMovies();
        for (Movie movie : allMovies) {
            addMovie(movie);
        }
    }

    private void addToMap(Map<String, Set<Movie>> map, String key, Movie movie) {
        map.computeIfAbsent(key, k -> new HashSet<>()).add(movie);
    }

    public void addMovie(Movie movie) {
        // Add to actor map
        for (String actor : movie.getActors()) {
            addToMap(actorMap, actor, movie);
        }

        // Add to director map
        for (String director : movie.getDirectors()) {
            addToMap(directorMap, director, movie);
        }

        // Add to composer map
        for (String composer : movie.getComposers()) {
            addToMap(composerMap, composer, movie);
        }

        // Add to writer map
        for (String writer : movie.getWriters()) {
            addToMap(writerMap, writer, movie);
        }

        // Add to cinematographer map
        for (String cinematographer : movie.getCinematographers()) {
            addToMap(cinematographerMap, cinematographer, movie);
        }
    }

    public Map<Movie, String> getConnectedMoviesWithReason(Movie movie) {
        Map<Movie, String> connected = new HashMap<>();

        for (String actor : movie.getActors()) {
            for (Movie m : actorMap.getOrDefault(actor, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "actor: " + actor);
                }
            }
        }
        for (String director : movie.getDirectors()) {
            for (Movie m : directorMap.getOrDefault(director, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "director: " + director);
                }
            }
        }
        for (String composer : movie.getComposers()) {
            for (Movie m : composerMap.getOrDefault(composer, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "composer: " + composer);
                }
            }
        }
        for (String writer : movie.getWriters()) {
            for (Movie m : writerMap.getOrDefault(writer, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "writer: " + writer);
                }
            }
        }
        for (String cinematographer : movie.getCinematographers()) {
            for (Movie m : cinematographerMap.getOrDefault(cinematographer, Set.of())) {
                if (!m.equals(movie) && !connected.containsKey(m)) {
                    connected.put(m, "cinematographer: " + cinematographer);
                }
            }
        }

        return connected;
    }


    public List<String> getConnectedMovieTitlesWithReason(Movie movie) {
        Map<Movie, String> connected = getConnectedMoviesWithReason(movie);  // ✅ 使用新方法
        List<String> titles = new ArrayList<>();
        for (Map.Entry<Movie, String> entry : connected.entrySet()) {
            Movie m = entry.getKey();
            String reason = entry.getValue();
            titles.add(m.getTitle() + " (" + reason + ")");
        }
        return titles;
    }

    public List<String> getSuggestions(String partialTitle) {
        List<String> suggestions = new ArrayList<>();
        // Use the movieTrie to get suggestions based on the partial title
        suggestions = movieTrie.getSuggestions(partialTitle);
        return suggestions;
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }

    public Movie getRandomMovie() {
        if (allMovies.isEmpty()) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(allMovies.size());
        Movie randomMovie = allMovies.get(index);
        if (getConnectedMoviesWithReason(randomMovie).isEmpty()) {
            return getRandomMovie();
        }
        return randomMovie;
    }

    public Movie getMovieByTitle(String title) {
        // iterate through all movies, find the one that matches the title
        for (Movie movie : allMovies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }

        // if no movie is found, return null
        return null;
    }

    public MovieTrie getMovieTrie() {
        return movieTrie;
    }

}
