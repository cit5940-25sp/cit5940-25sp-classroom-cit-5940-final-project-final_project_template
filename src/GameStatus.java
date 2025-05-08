import java.util.ArrayList;
import java.util.List;
public class GameStatus {
    private List<Player> players; // 2
    private int currentPlayer;
    private int round;
    private String gameStatusString;

    // Set up initial game state: empty player list, starting with player 0 and round 0
    public GameStatus() {
        players = new ArrayList<>(); // Assuming 2 players for simplicity
        currentPlayer = 0;
        round = 0;
    }

    public boolean isGameOver() {
        return false;
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
