import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

/**
 * Entry point of the movie connection game.
 * <p>
 * Initializes players, win conditions, movie index, terminal screen, and launches the game controller.
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
            WinCondition winCondition1 = new GenreWinCondition("Action");
            WinCondition winCondition2 = new CustomWinCondition(movies -> movies.size() >= 3, "3 Movies");

            Player player1 = new Player("Alice", winCondition1);
            Player player2 = new Player("Bob", winCondition2);

            MovieIndex movieIndex = new MovieIndex();
            TerminalSize size = new TerminalSize(125, 30);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(size);
            Screen screen = terminalFactory.createScreen();

//            Screen screen = new DefaultTerminalFactory().createScreen();
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
