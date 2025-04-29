import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        MovieIndex parser = new MovieIndex();
        Map<Integer, String> movies = parser.loadMovies("/Users/melodyhashemi/IdeaProjects/Final Project -594/tmdb_5000_movies.csv");
        System.out.println("Loaded " + movies.size() + " movies.");
        for (Map.Entry<Integer, String> entry : movies.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

}
