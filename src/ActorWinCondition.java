import java.util.List;

public class ActorWinCondition implements IWinConditionStrategy {
    @Override
    public boolean checkWin(List<IMovie> playedMovies) {
        return false;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
