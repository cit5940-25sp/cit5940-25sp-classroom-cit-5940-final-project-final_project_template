import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        TerminalWithSuggestions terminal;
        try {
            terminal = new TerminalWithSuggestions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        GameModel model = new GameModel();
        model.initializePlayers();

        List<IMovie> movieList = model.convertMapToListOfMovies(model.loadMovieData("tmdb_5000_movies.csv", "tmdb_5000_credits.csv"));

        GameController controller = new GameController((Player) model.getPlayer1(), (Player) model.getPlayer2(), clock, movieList, terminal);

        controller.initializeGame(movieList);
        controller.startGame();

    }
}
