import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class Main {
    public static void main(String[] args) {
        try {
            WinCondition winCondition1 = new GenreWinCondition("Action");
            WinCondition winCondition2 = new GenreWinCondition("Action");

            Player player1 = new Player("Alice", winCondition1);
            Player player2 = new Player("Bob", winCondition2);

            MovieIndex movieIndex = new MovieIndex();
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();

            GameView view = new GameView(screen, movieIndex.getMovieTrie());

            GameController controller = new GameController(player1, player2, movieIndex, view);
            controller.startGame();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
