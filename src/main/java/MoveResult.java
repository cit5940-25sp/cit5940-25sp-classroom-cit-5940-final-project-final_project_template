import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MoveResult {
    private final boolean success;
    private final String message;
    private final Set<Language> languageOptions;
    private final GameMove move;

    public MoveResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.languageOptions = Collections.emptySet();
        this.move = null;
    }

    public MoveResult(boolean success, String message, Set<Language> languageOptions) {
        this.success = success;
        this.message = message;
        this.languageOptions = Collections.unmodifiableSet(new HashSet<>(languageOptions));
        this.move = null;
    }

    public MoveResult(boolean success, String message, GameMove move) {
        this.success = success;
        this.message = message;
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

    public boolean requiresLanguageSelection() {
        return !languageOptions.isEmpty();
    }

    public GameMove getMove() {
        return move;
    }
}
