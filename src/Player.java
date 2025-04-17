import java.util.List;

// Tracks info on each player (scores + movies already played) (model)
public class Player implements IPlayer{

    private String name;

    public Player() {
        this.name = name;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<IMovie> getPlayedMovies() {
        return List.of();
    }

    @Override
    public void addPlayedMovie(IMovie movie) {

    }

    @Override
    public boolean hasWon() {
        return false;
    }

    @Override
    public String getWinConditionDescription() {
        return "";
    }

    @Override
    public IWinConditionStrategy getWinConditionStrategy() {
        return null;
    }
}
