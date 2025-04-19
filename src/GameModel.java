import java.util.*;

// Tracks game state (model)
public class GameModel implements IGameModel {

    private List<IPlayer> players;
    private int currentPlayerIndex;
    private IMovie currentMovie;
    private List<IMovie> recentHistory;
    private IMovieIndex movieIndex;
    private Set<String> usedMovies;
    private IPlayer winner;
    private int roundCount;

    public GameModel(IMovieIndex movieIndex) {
        this.movieIndex = movieIndex;
        this.recentHistory = new ArrayList<>();
        this.usedMovies = new HashSet<>();
        this.roundCount = 0;
    }

    @Override
    public void initializePlayers(List<IPlayer> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    @Override
    public void loadMovieData(List<IMovie> movieList) {
        movieIndex.loadMovies(movieList);
        this.currentMovie = movieList.get(new Random().nextInt(movieList.size()));
        recentHistory.add(currentMovie);
        usedMovies.add(currentMovie.getTitle().toLowerCase());
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

    @Override
    public boolean isValidMove(String movieTitle) {
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
        IMovie movie = movieIndex.getMovieByTitle(movieTitle);
        if (movie == null) {
            return;
        }

        usedMovies.add(movie.getTitle().toLowerCase());
        recentHistory.add(movie);
        if (recentHistory.size() > 5) {
            recentHistory.remove(0);
        }

        currentMovie = movie;
        getCurrentPlayer().addPlayedMovie(movie);
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
}
