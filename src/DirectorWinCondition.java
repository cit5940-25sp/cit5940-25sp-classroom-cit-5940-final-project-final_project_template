import java.util.List;

public class DirectorWinCondition implements IWinConditionStrategy {
    @Override
    public boolean checkWin(List<IMovie> playedMovies) {
        return false;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
