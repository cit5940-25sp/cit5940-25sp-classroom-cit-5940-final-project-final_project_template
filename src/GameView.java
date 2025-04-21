import java.util.List;

// Displays current round status, player progress, recent plays, etc. Also shows autocomplete suggestions during input. (View)
public class GameView implements IGameView {
    @Override
    public void showWelcomeMessage() {

    }

    @Override
    public void showWinConditions(List<IPlayer> players) {

    }

    @Override
    public void showGameStart(IPlayer currentPlayer) {

    }

    @Override
    public void promptForMovie(IPlayer currentPlayer) {

    }

    @Override
    public void showMoveSuccess(String movieTitle, IPlayer currentPlayer) {

    }

    @Override
    public void showInvalidMove(String movieTitle) {

    }

    @Override
    public void showTimeout(IPlayer currentPlayer) {

    }

    @Override
    public void showWinner(IPlayer winner) {

    }

    @Override
    public void showDrawOrTimeout() {

    }

    @Override
    public void showNextTurn(IPlayer currentPlayer) {

    }

    @Override
    public void showMovieHistory(List<IMovie> recentMovies) {

    }

    @Override
    public void showPlayerStats(List<IPlayer> players, int roundCount) {

    }
}
