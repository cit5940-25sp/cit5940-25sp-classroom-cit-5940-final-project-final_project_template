import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectorWinCondition implements IWinConditionStrategy {
    private String selectedDirector;

    public DirectorWinCondition(String selectedDirector) {
        this.selectedDirector = selectedDirector;
    }

    @Override
    public boolean checkWin(List<IMovie> playedMovies) {
        Map<String, Integer> directorCount = new HashMap<String, Integer>();
        for (IMovie movie : playedMovies) {
            List<String> directors = movie.getDirectors();
            for (String director : directors) {
                if (!directorCount.containsKey(director)) {
                    directorCount.put(director, 1);
                } else {
                    directorCount.put(director, directorCount.get(director) + 1);
                }
                if (directorCount.get(director) == 5) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "All of the movies played must have" + selectedDirector + "as the director of the movie";
    }
}
