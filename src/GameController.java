import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        MovieIndex movieIndex = new MovieIndex();
        movieIndex.loadMovies(movieList); // loads the movies

        Random random = new Random();
        IMovie startingMovie = movieList.get(random.nextInt(movieList.size())); // randomly generate movie

        player1 = new Player(" ");
        player2 = new Player(" ");

        currentPlayer = player1;

        usedMovies.add(startingMovie);
        // what else is there to initialize game?
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
        ConnectionValidator connectionValidator = new ConnectionValidator();
        String movieTitle = input.trim().toLowerCase();
        IMovie selectedMovie = null;

        // checks input by finding it in the list of movies
        for (IMovie movie : movieList) {
            if (movie.getTitle().toLowerCase().equals(movieTitle)) {
                selectedMovie = movie;
                break;
            }
        }
        // ensures input exists in movie list and has value
        if (selectedMovie == null) {
            System.out.println("Movie not found");
            return;
        }
        // ensures movie is not repeated
        if (usedMovies.contains(selectedMovie)) {
            System.out.println("Movie already used");
            return;
        }
        IMovie lastMovie = usedMovies.get(usedMovies.size() - 1);
        if (!connectionValidator.isValidConnection(lastMovie, selectedMovie)) {
            System.out.println("not valid connection");
            return;
        }
        // if we made it here, input is valid and counts as a point for the player

        usedMovies.add(selectedMovie); // add it to used movies
        currentPlayer.addPlayedMovie(selectedMovie); // add to list of movies specific player played
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
