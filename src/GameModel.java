import java.util.List;

// Tracks game state (model)
public class GameModel implements IGameModel {

    @Override
    public void initializePlayers(List<IPlayer> players) {

    }

    @Override
    public void loadMovieData(List<IMovie> movieList) {

    }

    @Override
    public List<IPlayer> getPlayers() {
        return List.of();
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return null;
    }

    @Override
    public void switchToNextPlayer() {

    }

    @Override
    public IMovie getCurrentMovie() {
        return null;
    }

    @Override
    public boolean isValidMove(String movieTitle) {
        return false;
    }

    @Override
    public void makeMove(String movieTitle) {

    }

    @Override
    public boolean checkWinCondition(IPlayer player) {
        return false;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public IPlayer getWinner() {
        return null;
    }

    @Override
    public List<IMovie> getRecentHistory() {
        return List.of();
    }

    @Override
    public int getRoundCount() {
        return 0;
    }
}
