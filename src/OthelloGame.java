public class OthelloGame {
    private Board board;
    private Player currentPlayer;

    public OthelloGame() {
        board = new Board();
        currentPlayer = Player.BLACK;  // black moves first in Othello
    }

    public void playGame() {
        while (!board.isGameOver()) {
            System.out.println("Current Player: " + currentPlayer);

            // TODO: Connect to GUI or AI to obtain move input
            // simulate move at (2,3) for testing
            // Move move = new Move(2, 3, currentPlayer);
            // if (board.isValidMove(move, currentPlayer)) {
            //     board.applyMove(move);
            // }

            switchPlayer();
        }
        System.out.println("Game Over. Final Score: " + board.getScore());
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.BLACK) ? Player.WHITE : Player.BLACK;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }
}
