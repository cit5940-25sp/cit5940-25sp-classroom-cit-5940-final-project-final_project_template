import java.util.List;

public interface IWinConditionStrategy {

    /*
    returns true if given played movies satisfies the win condition
    (played movies referred to the list of movies that player has played)
     */
    public boolean checkWin(List<IMovie> playedMovies);

    /*
    returns a user friendly description of the win condition
     */
    public String getDescription();

}
