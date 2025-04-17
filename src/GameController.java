import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Orchestrates the game loop: player input -> validation -> update state -> update view (controller)
public class GameController implements IGameController {

    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private List<IMovie> usedMovies;
    private List<IMovie> movieList;
    private Clock clock;
    private static final long ROUND_DURATION_MS = 30000;
    private long turnStartTime;
    private boolean gameOver;
    private GameView gameView;

    public GameController(Player player1, Player player2, Clock clock, List<IMovie> movieList, GameView gameView) {
        this.player1 = player1;
        this.player2 = player2;
        this.clock = clock;
        this.usedMovies = new ArrayList<>();
        this.currentPlayer = player1;
        this.movieList = movieList;
        this.gameView = gameView;
    }

    @Override
    public void initializeGame() {

    }

    @Override
    public void startGame() {
        turnStartTime = clock.millis();
        gameOver = false;
        gameView.showWelcomeMessage();
        gameView.showWinConditions(List.of(player1, player2));
    }

    @Override
    public void handlePlayerInput(String input) {
        long elapsedTime = clock.millis() - turnStartTime;
        if (elapsedTime > ROUND_DURATION_MS) {
            handleTimeout();
        }
    }

    @Override
    public void nextTurn() {
        turnStartTime = clock.millis();

    }

    @Override
    public boolean isGameOver() {
        return this.gameOver;
    }

    @Override
    public void endGame() {
        if (currentPlayer.equals(player1)) {
            gameView.showWinner(player2);
        } else {
            gameView.showWinner(player1);
        }
    }

    @Override
    public void handleTimeout() {
        this.gameOver = true;
        gameView.showTimeout(currentPlayer);
        endGame();
    }
}
