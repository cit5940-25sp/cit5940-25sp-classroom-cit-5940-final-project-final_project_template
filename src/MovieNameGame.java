import controller.GameController;
import java.io.IOException;
import java.util.List;
import model.Movie;
import model.MovieDataLoader;
import model.MovieIndex;
import model.Player;
import strategy.ActorLinkStrategy;
import strategy.GenreWinCondition;
import strategy.ILinkStrategy;
import strategy.IWinCondition;
import view.GameView;

public class MovieNameGame {

    public static void main(String[] args) {
        try {
            List<Movie> movies = MovieDataLoader.loadMovies("data/movies.csv", "data/credits.csv");
            MovieIndex index = new MovieIndex(movies);

            Player p1 = new Player("Player 1");
            Player p2 = new Player("Player 2");

            ILinkStrategy linkStrategy = new ActorLinkStrategy();
            IWinCondition winCondition = new GenreWinCondition("Action");

            GameView view = new GameView();

            GameController controller = new GameController(index, linkStrategy, winCondition, p1, p2, view);
            controller.runGame();

        } catch (IOException e) {
            System.err.println("Error loading movie data: " + e.getMessage());
        }
    }
}