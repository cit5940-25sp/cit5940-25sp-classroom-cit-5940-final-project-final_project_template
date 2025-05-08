public class TurnResult {
    private final boolean success;
    private final boolean gameOver;
    private final String message;

    public TurnResult(boolean success, boolean gameOver, String message) {
        this.success = success;
        this.gameOver = gameOver;
        this.message = message;
    }

    // Keep original constructor for backward compatibility
    public TurnResult(boolean success, String message) {
        this(success, false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getMessage() {
        return message;
    }
}
