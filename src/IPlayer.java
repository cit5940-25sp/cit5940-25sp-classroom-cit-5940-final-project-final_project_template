import java.util.List;

public interface IPlayer {

    /*
    returns the player's name
     */
    public String getName();

    /*
    returns the list of movies the player has used
     */
    public List<IMovie> getPlayedMovies();

    /*
    adds the movie to the player's history
    parameter: movie played this term
     */
    public void addPlayedMovie(IMovie movie);

    /*
    returns true if player has met their win condition
     */
    public boolean hasWon();

    /*
    returns a description of the win condition
     */
    public String getWinConditionDescription();

    /*
    returns the player's win condition strategy object
     */
    public IWinConditionStrategy getWinConditionStrategy();

    /*
    returns the player's score
     */

    public int getScore();

    public void setScore(int score);
}
