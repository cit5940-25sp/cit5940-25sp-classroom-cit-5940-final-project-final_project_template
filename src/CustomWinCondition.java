import java.util.List;
import java.util.function.Function;

public class CustomWinCondition implements WinCondition {
    private Function<List<Movie>, Boolean> condition;

    public CustomWinCondition(Function<List<Movie>, Boolean> condition) {
        this.condition = condition;
    }

    @Override
    public boolean checkWin(Player player) {
        return condition.apply(player.getMoviesPlayed());
    }
}
