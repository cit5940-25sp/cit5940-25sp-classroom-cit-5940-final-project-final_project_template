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


}
