import java.util.Collections;
import java.util.Set;

public class MoveResult {
    private final boolean success;
    private final String message;
    private final Set<Language> languageOptions;
    private final GameMove move;
    private final boolean languageOverused;

    public MoveResult(boolean success, String message) {
        this(success, message, false);
    }

    public MoveResult(boolean success, String message, boolean languageOverused) {
        this(success, message, null, languageOverused);
    }

    public MoveResult(boolean success, String message, GameMove move) {
        this(success, message, move, false);
    }

    public MoveResult(boolean success, String message, GameMove move, boolean languageOverused) {
        this.success = success;
        this.message = message;
        this.languageOverused = false;
        this.languageOptions = Collections.emptySet();
        this.move = move;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Set<Language> getLanguageOptions() {
        return languageOptions;
    }

    public GameMove getMove() {
        return move;
    }

    public boolean isLanguageOverused() {
        return languageOverused;
    }
}
