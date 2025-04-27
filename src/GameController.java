import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private GameModel gameModel;
    private ScheduledExecutorService scheduler;
    private boolean timerRunning;
    private int secondsRemaining;

    public GameController(Player player1, Player player2, Clock clock, List<IMovie> movieList, GameView gameView) {
        this.player1 = player1;
        this.player2 = player2;
        this.clock = clock;
        this.usedMovies = new ArrayList<>();
        this.currentPlayer = player1;
        this.movieList = movieList;
        this.gameView = gameView;
        this.gameModel = new GameModel(new MovieIndex());
        gameModel.loadMovieData(movieList);
    }

    @Override
    public void initializeGame() {
        MovieIndex movieIndex = new MovieIndex();
        movieIndex.loadMovies(movieList); // loads the movies

        Random random = new Random();
        IMovie startingMovie = movieList.get(random.nextInt(movieList.size())); // randomly generate movie

        currentPlayer = player1;
        player1.setWinConditionStrategy(new ActorWinCondition()); // how to populate
        player2.setWinConditionStrategy(new DirectorWinCondition("Steven Spielberg")); // how to populate
        usedMovies.add(startingMovie);
        // what else is there to initialize game?
    }

    @Override
    public void startGame() {
        turnStartTime = clock.millis();
        gameOver = false;
        gameView.showWelcomeMessage();
        gameView.showWinConditions(List.of(player1, player2));

        secondsRemaining = 30;
        timerRunning = true;
        startTimer();
    }

    private void startTimer() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (timerRunning && secondsRemaining > 0) {
                secondsRemaining--;
                gameView.updateScreen(secondsRemaining); // need method to show timer
            }
            if (secondsRemaining == 0) {
                timerRunning = false;
                handleTimeout();
            }
        }, 1, 1, TimeUnit.SECONDS);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    @Override
    public void handlePlayerInput(String input) {
        long elapsedTime = clock.millis() - turnStartTime;
        if (elapsedTime > ROUND_DURATION_MS) {
            handleTimeout();
            return;
        }

        if (!gameModel.isValidMove(input)) {
            gameView.showInvalidMove(input);
            return;
        }

        gameModel.makeMove(input);
        gameView.showMoveSuccess(input, currentPlayer);
    }

    @Override
    public void nextTurn() {
        turnStartTime = clock.millis();
        // this switches the current player
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }

    }

    @Override
    public boolean isGameOver() {
        return this.gameOver;
    }

    @Override
    public void endGame() {
        timerRunning = false;
        if (scheduler != null) {
            scheduler.shutdown();
        }
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
