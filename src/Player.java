import java.util.ArrayList;
import java.util.List;

// Tracks info on each player (scores + movies already played) (model)
public class Player implements IPlayer {
    private String name;
    private List<IMovie> playedMovies;
    private IWinConditionStrategy winConditionStrategy;
    private int score;

    public Player(String name) {
        this.name = name;
        this.playedMovies = new ArrayList<>();
        this.score = 0;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<IMovie> getPlayedMovies() {
        return this.playedMovies;
    }

    @Override
    public void addPlayedMovie(IMovie movie) {
        this.playedMovies.add(movie);
    }

    @Override
    public boolean hasWon() {
        return this.winConditionStrategy.checkWin(this.playedMovies);
    }

    @Override
    public String getWinConditionDescription() {
        return this.winConditionStrategy.getDescription();
    }

    @Override
    public IWinConditionStrategy getWinConditionStrategy() {
        return this.winConditionStrategy;
    }

    @Override
    public int getScore() {
        return this.score;
    }
}
