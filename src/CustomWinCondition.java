import java.util.List;
import java.util.function.Function;

public class CustomWinCondition implements WinCondition {
    private Function<List<Movie>, Boolean> condition;
    private String description;

    public CustomWinCondition(Function<List<Movie>, Boolean> condition, String description) {
        this.condition = condition;
        this.description = description;
    }

    @Override
    public boolean checkWin(Player player) {
        return condition.apply(player.getMoviesPlayed());
    }

    @Override
    public String getDescription() {
        return description;
    }
}
