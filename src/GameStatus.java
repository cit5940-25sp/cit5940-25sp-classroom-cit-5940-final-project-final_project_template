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

    /**
     * Retrieves the winner of the game.
     *
     * @return The winning player if the game is over and a winner exists,
     *         {@code null} if the game is not over or no winner is found.
     *         Note: It's assumed that there should always be a winner if the game is over.
     */
    public Player getWinner() {
        // Check if the game is not over. If so, return null as there's no winner yet.
        if(!isGameOver()) {
            return null;
        }
        // Iterate through all players to find the winner.
        for(Player player : players) {
            // If a player is marked as the winner, return that player.
            if(player.isWinner()) {
                return player;
            }
        }
        // This line should never be reached if the game logic is correct and there's always a winner when the game is over.
        return null;
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
