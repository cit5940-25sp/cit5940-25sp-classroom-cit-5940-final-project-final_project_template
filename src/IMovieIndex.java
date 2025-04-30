import java.util.List;
import java.util.Map;

public interface IMovieIndex {



    /*
    retrieves movie by exact title
    (case insenstive match)
     */
    public IMovie getMovieByTitle(String title);

    /*
    returns a list of movie titles that match the movie prefix
    (used for real-time autocomplete when player types)
     */
    public List<String> autocomplete(String input);

    /*
    returns a list of movie that share a contributor with given movie
    (use to find valid connections)
     */
    public List<IMovie> getConnectedMovies(IMovie movie);

    /*
    returns whether a movie exists in the index or not
     */
    public boolean containsMovie(String title);

}
