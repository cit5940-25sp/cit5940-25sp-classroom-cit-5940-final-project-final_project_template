import java.util.ArrayList;
import java.util.List;
public class GameStatus {
    private List<Player> players; // 2
    private int currentPlayer;
    private int round;
    private String gameStatusString;

public class GameStatus {
    private String status;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private int round;

    public GameStatus() {
        this.status = "Not Started";
        this.player1 = new Player();
        this.player2 = new Player(); 
    }

    public boolean isGameOver() {
        return false; 
    }
}
