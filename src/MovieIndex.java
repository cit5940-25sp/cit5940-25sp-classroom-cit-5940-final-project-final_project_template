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

    public Set<Movie> getConnectedMovies(Movie movie) {
        Set<Movie> connectedMovies = new HashSet<>();
        for (String actor : movie.getActors()) {
            connectedMovies.addAll(actorMap.getOrDefault(actor, new HashSet<>()));
        }
        for (String director : movie.getDirectors()) {
            connectedMovies.addAll(directorMap.getOrDefault(director, new HashSet<>()));
        }
        for (String composer : movie.getComposers()) {
            connectedMovies.addAll(composerMap.getOrDefault(composer, new HashSet<>()));
        }
        for (String writer : movie.getWriters()) {
            connectedMovies.addAll(writerMap.getOrDefault(writer, new HashSet<>()));
        }
        for (String cinematographer : movie.getCinematographers()) {
            connectedMovies.addAll(
                    cinematographerMap.getOrDefault(cinematographer, new HashSet<>()));
        }
        // Remove the movie itself from the connected movies
        connectedMovies.remove(movie);
        return connectedMovies;
    }

    public List<String> getConnectedMovieTitles(Movie movie) {
        Set<Movie> connected = getConnectedMovies(movie);
        List<String> titles = new ArrayList<>();
        for (Movie m : connected) {
            titles.add(m.getTitle() + " (" + m.getReleaseYear() + ") - " + String.join(", ", m.getGenres()));
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
        if (getConnectedMovies(randomMovie).isEmpty()) {
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
