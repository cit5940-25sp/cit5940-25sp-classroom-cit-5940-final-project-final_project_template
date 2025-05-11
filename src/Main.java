import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

/**
 * Entry point of the movie connection game.
 * <p>
 * Initializes players, win conditions, movie index, terminal screen,
 * and launches the game controller.
 * </p>
 *
 * @author Jianing Yin
 */
public class Main {
    /**
     * The main method to start the movie game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            Player player1 = PlayerFactory.createPlayerWithGenre("Alice", "Action");
            Player player2 = PlayerFactory.createPlayerWithCustomCondition(
                "Bob",
                movies -> movies.stream().anyMatch(
                    m -> m.getDirectors() != null &&
                         m.getDirectors().stream().anyMatch(
                             d -> d.toLowerCase().contains("nolan")
                         )
                ),
                "Directed by Nolan"
            );
//            Player player2 = PlayerFactory.createPlayerWithCustomCondition(
//                "Bob",
//                movies -> movies.size() > 3,
//                "Collected more than 3 movies"
//            );


            MovieIndex movieIndex = new MovieIndex();
            TerminalSize size = new TerminalSize(125, 30);
            DefaultTerminalFactory terminalFactory =
                    new DefaultTerminalFactory().setInitialTerminalSize(size);
            Screen screen = terminalFactory.createScreen();

            screen.startScreen();

            GameView view = new GameView(screen);
            view.setMovieTrie(movieIndex.getMovieTrie());

            GameController controller = new GameController(player1, player2, movieIndex, view);
            controller.startGame();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
