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
