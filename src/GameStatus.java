import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
public class GameStatus {
    private List<Player> players;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private int round;
    private TreeSet<Movie> usedMovies;


    // Set up initial game state: empty player list, starting with player 0 and round 0
    public GameStatus() {
        usedMovies = new TreeSet<>();
        players = new ArrayList<>(); // Assuming 2 players for simplicity
        currentPlayerIndex = 0;
        currentPlayer = null;
        round = 0;
    }

    public boolean isGameOver() {
        for(Player player : players) {
            if(!player.isFinished()) {
                return false;
            }
        }
        // If all players have finished their turns, the game is over.
        return true;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    // Return the current player, then advance to the next one for the next turn
    public Player getCurrentPlayer() {
        if(players.isEmpty())
            return null;
        Player player = players.get(currentPlayer);
        currentPlayer = (currentPlayer + 1) % players.size();
        return player;
    }

    // play round and let the current player try to play the given movie
    public void playRound(Movie movie) {
        round++;
        Player player = getCurrentPlayer();
        if (player != null) {
            player.play(movie);
        }
    }
}
