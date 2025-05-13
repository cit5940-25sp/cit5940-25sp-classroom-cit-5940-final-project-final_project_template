import java.util.*;

// Tracks game state (model)
public class GameModel implements IGameModel {

    private List<IPlayer> players;
    private int currentPlayerIndex;
    private IMovie currentMovie;
    private List<IMovie> recentHistory;
    private Set<String> usedMovies;
    private IPlayer winner;
    private int roundCount;
    private ConnectionValidator connectionValidator;
    private IPlayer player1;
    private IPlayer player2;
    private Map<Integer, IMovie> movies;

    public GameModel() {
        this.recentHistory = new ArrayList<>();
        this.usedMovies = new HashSet<>();
        this.roundCount = 0;
        this.connectionValidator = new ConnectionValidator();
    }

    @Override
    public void initializePlayers() {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        this.player1 = player1;
        this.player2 = player2;
        this.players = Arrays.asList(player1, player2);

        Set<String> allActors = new HashSet<>();
        Set<String> allCrew = new HashSet<>();

        Map<Integer, IMovie> movies = loadMovieData("tmdb_5000_movies.csv", "tmdb_5000_credits.csv");
        this.movies = movies;

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
            String selectedCrewMember = crewList.get(ran.nextInt(crewList.size()));
            player1.setWinConditionStrategy(new CrewMemWinCondition(selectedCrewMember));
        }

        boolean p2usingActor = ran.nextBoolean();
        if (p2usingActor && !allActors.isEmpty()) {
            String selectedActor = actorList.get(ran.nextInt(actorList.size()));
            player2.setWinConditionStrategy(new ActorWinCondition(selectedActor));
        } else if (!allCrew.isEmpty()) {
            String selectedCrew = crewList.get(ran.nextInt(crewList.size()));
            player2.setWinConditionStrategy(new CrewMemWinCondition(selectedCrew));
        }

        this.currentPlayerIndex = 0;
    }

    @Override
    public Map<Integer, IMovie> loadMovieData(String moviesCsvFile, String creditsCsvFile) {
        MovieIndex movieIndex = new MovieIndex();
        Map<Integer, IMovie> movies = movieIndex.loadMovies(moviesCsvFile); // loads the movies
        movieIndex.loadCast(creditsCsvFile, movies);
        return movies;
    }

    @Override
    public void setStartingMovie(IMovie movie) {
        this.currentMovie = movie;
    }

    @Override
    public List<IPlayer> getPlayers() {
        return players;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public void switchToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        roundCount++;
    }

    @Override
    public IMovie getCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(IMovie currentMovie) {
        this.currentMovie = currentMovie;
    }

    @Override
    public boolean isValidMove(String movieTitle) {
        MovieIndex movieIndex = new MovieIndex();
        Map<Integer, IMovie> movies = movieIndex.loadMovies("tmdb_5000_movies.csv");
        movieIndex.loadCast("tmdb_5000_credits.csv", movies);
        IMovie candidate = movieIndex.getMovieByTitle(movieTitle);

        if (candidate == null) {
            return false;
        }
        if (usedMovies.contains(candidate.getTitle().toLowerCase())) {
            return false;
        }
        List<String> shared = getSharedConnections(currentMovie, candidate);
        return !shared.isEmpty();
    }

    @Override
    public void makeMove(String movieTitle) {
        MovieIndex movieIndex = new MovieIndex();
        Map<Integer, IMovie> loadedMovies = movieIndex.loadMovies("tmdb_5000_movies.csv");
        movieIndex.loadCast("tmdb_5000_credits.csv", loadedMovies);

        IMovie nextMovie = movieIndex.getMovieByTitle(movieTitle);
        if (nextMovie == null || usedMovies.contains(nextMovie.getTitle().toLowerCase())) {
            return;
        }

        List<String> sharedConnections = connectionValidator.getSharedConnections(currentMovie, nextMovie);
        connectionValidator.recordConnectionUse(sharedConnections);

        // Add the movie that was just connected FROM to the history
        if (currentMovie != null) {
            recentHistory.add(currentMovie);
            if (recentHistory.size() > 5) {
                recentHistory.remove(0);
            }
            usedMovies.add(currentMovie.getTitle().toLowerCase());
        }

        currentMovie = nextMovie;
        usedMovies.add(nextMovie.getTitle().toLowerCase());
        getCurrentPlayer().addPlayedMovie(nextMovie);

        int currentPlayerScore = getCurrentPlayer().getScore();
        currentPlayerScore++;
        getCurrentPlayer().setScore(currentPlayerScore);
    }

    @Override
    public boolean checkWinCondition(IPlayer player) {
        boolean won = player.hasWon();
        if (won) {
            winner = player;
        }
        return won;
    }

    @Override
    public boolean isGameOver() {
        return winner != null;
    }

    @Override
    public IPlayer getWinner() {
        return winner;
    }

    @Override
    public List<IMovie> getRecentHistory() {
        return new ArrayList<>(recentHistory);
    }

    @Override
    public int getRoundCount() {
        return roundCount;
    }

    private List<String> getSharedConnections(IMovie a, IMovie b) {
        Set<String> contributorsA = a.getAllContributors();
        Set<String> contributorsB = b.getAllContributors();
        List<String> shared = new ArrayList<>();

        for (String contributor : contributorsA) {
            if (contributorsB.contains(contributor)) {
                shared.add(contributor);
            }
        }
        return shared;
    }

    @Override
    public IPlayer getPlayer1() {
        return player1;
    }

    @Override
    public IPlayer getPlayer2() {
        return player2;
    }

    @Override
    public Map<Integer, IMovie> getMovies() {
        return movies;
    }

    @Override
    public List<IMovie> convertMapToListOfMovies(Map<Integer, IMovie> movies) {
        List<IMovie> movieList = new ArrayList<>();
        for (IMovie movie : movies.values()) {
            movieList.add(movie);
        }

        return movieList;
    }
}
