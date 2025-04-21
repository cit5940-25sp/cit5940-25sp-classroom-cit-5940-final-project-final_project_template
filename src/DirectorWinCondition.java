import java.util.List;

public class DirectorWinCondition implements IWinConditionStrategy {
    private String selectedDirector;

    public DirectorWinCondition(String selectedDirector) {
        this.selectedDirector = selectedDirector;
    }
    @Override
    public boolean checkWin(List<IMovie> playedMovies) {
            for (IMovie movie : playedMovies) {
                if (movie.getDirectors().contains(selectedDirector)) {
                    return true;
                }
            }
        return false;
    }

    @Override
    public String getDescription() {
        return "All of the movies played must have" + selectedDirector + "as the director of the movie";
    }
}
