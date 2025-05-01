import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        MovieIndex parser = new MovieIndex();
        Map<Integer, IMovie> movies = parser.loadMovies("tmdb_5000_movies.csv");
        parser.loadCast("tmdb_5000_credits.csv", movies);
        System.out.println("Loaded " + movies.size() + " movies.");
////        for (Map.Entry<Integer, IMovie> entry : movies.entrySet()) {
//            System.out.println(entry.getKey() + " -> " + entry.getValue());
//        }
//    }
        for (Map.Entry<Integer, IMovie> entry : movies.entrySet()) {
            System.out.println(entry.getValue());  // this uses your toString()
        }
    }
}
