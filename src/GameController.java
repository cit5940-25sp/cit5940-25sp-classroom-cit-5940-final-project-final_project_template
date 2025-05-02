import java.io.IOException;
import java.time.Clock;
import java.util.*;

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
    private MovieIndex movieIndex;

    public GameController(Player p1, Player p2, Clock clock, List<IMovie> movies, TerminalWithSuggestions terminal) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = p1;
        this.movieList = movies;
        this.terminal = terminal;
        this.gameModel = new GameModel();
        this.gameModel.loadMovieData("tmdb_5000_movies.csv", "tmdb_5000_credits.csv");
        this.gameView = new GameView(terminal);
        this.gameOver = false;
        this.movieIndex = new MovieIndex();
        Map<Integer, IMovie> loadedMovies = movieIndex.loadMovies("tmdb_5000_movies.csv");
        movieIndex.loadCast("tmdb_5000_credits.csv", loadedMovies);
    }

    public GameController(Player player1, Player player2, Clock clock, List<IMovie> movies, GameView view) {
    }

    @Override
    public void initializeGame(List<IMovie> movieList) {

        Random rand = new Random();
        IMovie start = movieList.get(rand.nextInt(movieList.size()));


        gameModel.initializePlayers();

        gameView.showWelcomeMessage();
        gameView.showWinConditions(Arrays.asList(player1, player2));

        gameModel.setCurrentMovie(start);
        gameModel.setStartingMovie(start);
    }

    @Override
    public void startGame() throws IOException {
        while (!gameOver) {
            terminal.clearScreen(); // Reset screen each round
            gameView.showGameStart(currentPlayer);
            gameView.showMovieHistory(gameModel.getRecentHistory());
            gameView.showPlayerStats(gameModel.getPlayers(), gameModel.getRoundCount());
            gameView.promptForMovie(currentPlayer);

            long start = System.currentTimeMillis();
            int timeLimit = 30;
            String input = "";
            boolean moveAccepted = false;

            while ((System.currentTimeMillis() - start) / 1000 < timeLimit) {
                input = terminal.getInputWithSuggestions(movieList, gameModel.getCurrentMovie(), timeLimit - (int)((System.currentTimeMillis() - start) / 1000));

                if (input == null || input.trim().isEmpty()) {
                    continue; // Ignore empty input, continue countdown
                }

                if (!movieExists(input)) {
                    gameView.showInvalidMove("Movie not found.");
                } else if (!gameModel.isValidMove(input)) {
                    gameView.showInvalidMove("Invalid connection.");
                } else {
                    gameModel.makeMove(input);
                    gameView.showMoveSuccess(input, currentPlayer);
                    moveAccepted = true;
                    if (gameModel.checkWinCondition(currentPlayer)) {
                        gameView.showWinner(currentPlayer);
                        gameOver = true;
                    }
                    break;
                }
            }

            if (!moveAccepted && !gameOver) {
                handleTimeout();
                gameOver = true;
            }

            if (!gameOver) {
                nextTurn();
            }
        }
    }

    private boolean movieExists(String title) {
        for (IMovie movie : movieList) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
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
            other = player1;
        } else {
            other = player2;
        }
        gameView.showWinner(other);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<IMovie> getMovieList() {
        return movieList;
    }
}


