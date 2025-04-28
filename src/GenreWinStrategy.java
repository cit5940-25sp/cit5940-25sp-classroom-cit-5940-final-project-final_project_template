import java.util.List;

public class GenreWinStrategy implements IWinStrategy {
    private String targetGenre;
    private int requiredCount;

    public GenreWinStrategy(String targetGenre, int requiredCount) {
        this.targetGenre = targetGenre;
        this.requiredCount = requiredCount;
    }

    public boolean checkWinCondition(List<Movie> playedMovies) {
        return false;
    }

    public void updateProgress(Movie movie) {
    }
}
