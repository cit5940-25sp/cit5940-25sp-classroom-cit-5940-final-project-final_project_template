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

    public GameController(Player player1, Player player2, Clock clock, List<IMovie> movies, GameView view) {
    }

    @Override
    public void initializeGame() {

        Random rand = new Random();
        IMovie start = movieList.get(rand.nextInt(movieList.size()));

        MovieIndex movieIndex = new MovieIndex();
        Map<Integer, IMovie> movies = movieIndex.loadMovies("tmdb_5000_movies.csv"); // loads the movies
        movieIndex.loadCast("tmdb_5000_credits.csv", movies);


        Set<String> allActors = new HashSet<>();
        Set<String> allCrew = new HashSet<>();

        for (IMovie movie : movies.values()) {
            allActors.addAll(movie.getActors());
            allCrew.addAll(movie.getCrew());
        }

        List<String> actorList = new ArrayList<>(allActors);
        List<String> crewList = new ArrayList<>(allCrew);
        Random ran = new Random();


        boolean p1usingActor = ran.nextBoolean(); // choosing between win condition is actor or crew

        if (p1usingActor && !allActors.isEmpty()) {
            String selectedActor = actorList.get(ran.nextInt(actorList.size()));
            player1.setWinConditionStrategy(new ActorWinCondition(selectedActor));
        } else if (!allCrew.isEmpty()) {
            String selectedCrew = crewList.get(ran.nextInt(crewList.size()));
            player1.setWinConditionStrategy(new CrewMemWinCondition(selectedCrew));
        }

        boolean p2usingActor = ran.nextBoolean();
        if (p2usingActor && !allActors.isEmpty()) {
            String selectedActor = actorList.get(ran.nextInt(actorList.size()));
            player2.setWinConditionStrategy(new ActorWinCondition(selectedActor));
        } else if (!allCrew.isEmpty()) {
            String selectedCrew = crewList.get(ran.nextInt(crewList.size()));
            player2.setWinConditionStrategy(new CrewMemWinCondition(selectedCrew));
        }


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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}


