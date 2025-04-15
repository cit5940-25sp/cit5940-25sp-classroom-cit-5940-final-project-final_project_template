import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Movie> moviesPlayed;
    private WinCondition winCondition;

    public Player(String name, WinCondition winCondition) {
        this.name = name;
        moviesPlayed = new ArrayList<>();
        this.winCondition = winCondition;
    }

    public String getName() {
        return name;
    }

    public List<Movie> getMoviesPlayed() {
        return moviesPlayed;
    }

    public void addMovie(Movie movie) {
        moviesPlayed.add(movie);
    }

    public WinCondition getWinCondition() {
        return winCondition;
    }
}
