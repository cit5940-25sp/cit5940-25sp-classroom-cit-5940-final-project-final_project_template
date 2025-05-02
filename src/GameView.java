import java.util.List;

// Displays current round status, player progress, recent plays, etc. Also shows autocomplete suggestions during input. (View)
public class GameView implements IGameView {

    private TerminalWithSuggestions terminal;

    public GameView(TerminalWithSuggestions terminal) {
        this.terminal = terminal;
    }

    public GameView() {

    }

    @Override
    public void showWelcomeMessage() {
        terminal.displayMessage("Welcome to the Movie Name Game!");
    }

    public void updateScreen (int secondsRemaining) {
        System.out.println("Seconds remaining: " + secondsRemaining);
    }

    @Override
    public void showWinConditions(List<IPlayer> players) {
        for (IPlayer player : players) {
            terminal.displayMessage(player.getName() + " win condition: " + player.getWinConditionDescription());
        }
    }

    @Override
    public void showGameStart(IPlayer currentPlayer) {
        terminal.displayMessage("Round start: " + currentPlayer.getName());
    }

    @Override
    public void promptForMovie(IPlayer currentPlayer) {
        terminal.displayMessage(currentPlayer.getName() + ", enter a movie title.");
    }

    @Override
    public void showMoveSuccess(String movieTitle, IPlayer currentPlayer) {
        terminal.displayMessage(currentPlayer.getName() + " played " + movieTitle);
    }

    @Override
    public void showInvalidMove(String movieTitle) {
        terminal.displayMessage("Invalid movie title.");
    }

    @Override
    public void showTimeout(IPlayer currentPlayer) {
        terminal.displayMessage(currentPlayer.getName() + " ran out of time.");
    }

    @Override
    public void showWinner(IPlayer winner) {
        terminal.displayMessage(winner.getName() + " is the winner!");

    }

    @Override
    public void showNextTurn(IPlayer currentPlayer) {
        terminal.displayMessage("Next: " + currentPlayer.getName());
    }

    @Override
    public void showMovieHistory(List<IMovie> recentMovies) {
        terminal.displayMessage("Last 5 movies: ");
        for (IMovie movie : recentMovies) {
            terminal.displayMessage("- " + movie.getTitle());
        }
    }

    @Override
    public void showPlayerStats(List<IPlayer> players, int roundCount) {
        terminal.displayMessage("Player stats after round " + roundCount);
        for (IPlayer player : players) {
            terminal.displayMessage(player.getName() + " | Score: " + player.getScore());
        }
    }

    public void showCurrentMovie(IMovie currentMovie) {
        terminal.displayMessage("Current Movie: " + currentMovie.getTitle());
        terminal.displayMessage("Genres: " + currentMovie.getGenres());
    }
}
