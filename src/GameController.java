import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Orchestrates the game loop: player input -> validation -> update state -> update view (controller)
public class GameController implements IGameController {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private List<IMovie> movieList;
    private GameModel gameModel;
    private GameView gameView;
    private TerminalWithSuggestions terminal;
    private boolean gameOver;

    public GameController(Player p1, Player p2, List<IMovie> movies, TerminalWithSuggestions terminal) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = p1;
        this.movieList = movies;
        this.terminal = terminal;
        this.gameModel = new GameModel(new MovieIndex());
        this.gameModel.loadMovieData(movies);
        this.gameView = new GameView(terminal);
        this.gameOver = false;
    }

    @Override
    public void initializeGame() {

        Random rand = new Random();
        IMovie start = movieList.get(rand.nextInt(movieList.size()));

        MovieIndex movieIndex = new MovieIndex();
        Map<Integer, IMovie> movies = movieIndex.loadMovies("tmdb_5000_movies.csv"); // loads the movies
        movieIndex.loadCast("tmdb_5000_credits.csv", movies);


        player1.setWinConditionStrategy(new ActorWinCondition());
        player2.setWinConditionStrategy(new DirectorWinCondition("Steven Spielberg"));

        gameModel.initializePlayers(Arrays.asList(player1, player2));
        gameModel.makeMove(start.getTitle()); // start movie

        gameView.showWelcomeMessage();
        gameView.showWinConditions(Arrays.asList(player1, player2));
    }

    @Override
    public void startGame() {
        while (!gameOver) {
            gameView.showGameStart(currentPlayer);
            gameView.showMovieHistory(gameModel.getRecentHistory());
            gameView.showPlayerStats(gameModel.getPlayers(), gameModel.getRoundCount());
            gameView.promptForMovie(currentPlayer);

            String input = terminal.getInputWithSuggestions(movieList, 30);
            if (input == null || input.trim().isEmpty()) {
                handleTimeout();
                break;
            }

            handlePlayerInput(input);
            if (gameModel.checkWinCondition(currentPlayer)) {
                gameView.showWinner(currentPlayer);
                gameOver = true;
                break;
            }

            nextTurn();
        }
    }

    @Override
    public void handlePlayerInput(String input) {
        if (!gameModel.isValidMove(input)) {
            gameView.showInvalidMove(input);
        } else {
            gameModel.makeMove(input);
            gameView.showMoveSuccess(input, currentPlayer);
        }
    }

    @Override
    public void nextTurn() {
        gameModel.switchToNextPlayer();
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public void endGame() {
        gameOver = true;
    }

    @Override
    public void handleTimeout() {
        gameView.showTimeout(currentPlayer);
        Player other;
        if (currentPlayer == player2) {
            other = player2;
        } else {
            other = player1;
        }
        gameView.showWinner(other);
    }

}


    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

