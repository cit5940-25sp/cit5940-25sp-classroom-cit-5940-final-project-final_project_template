import java.util.ArrayList;
import java.util.List;

public class Board {
    private BoardSpace[][] grid;
    public static final int SIZE = 8;

    public Board() {
        grid = new BoardSpace[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new BoardSpace(i, j, Player.NONE);
            }
        }

        // Initialize with the standard Othello opening: 2 black and 2 white in the center
        grid[3][3].setOccupant(Player.WHITE);
        grid[3][4].setOccupant(Player.BLACK);
        grid[4][3].setOccupant(Player.BLACK);
        grid[4][4].setOccupant(Player.WHITE);
    }

    public boolean isValidMove(Move move, Player player) {
        // Basic check: whether the move is in the list of legal moves
        return getLegalMoves(player).contains(move);
    }

    public void applyMove(Move move) {
        // Basic implementation: place the piece on the board (does NOT flip opponent pieces yet)
        grid[move.getRow()][move.getCol()].setOccupant(move.getPlayer());
        // TODO: Implement flipping of opponent pieces according to Othello rules
    }

    public List<Move> getLegalMoves(Player player) {
        List<Move> legalMoves = new ArrayList<>();

        // Currently simplified: adds all empty spaces (no outflank checking yet)
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].getOccupant() == Player.NONE) {
                    // TODO: Check if placing here would outflank opponent pieces
                    legalMoves.add(new Move(i, j, player));
                }
            }
        }
        return legalMoves;
    }

    public boolean isGameOver() {
        return getLegalMoves(Player.BLACK).isEmpty() && getLegalMoves(Player.WHITE).isEmpty();
    }

    public String getScore() {
        int black = 0, white = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].getOccupant() == Player.BLACK) black++;
                if (grid[i][j].getOccupant() == Player.WHITE) white++;
            }
        }
        return "Black: " + black + " | White: " + white;
    }


    public BoardSpace[][] getGrid() {
        return grid;
    }
}