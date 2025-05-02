import java.util.List;

public interface IConnectionValidator {

    /*
    checks for actor, genre, director, writer, cinematographer, composer
    parameters: movie1 is previous valid movie named, movie2 is suggested connection
     */
    public boolean isValidConnection(IMovie movie1, IMovie movie2);

    /*
    Returns list of specific connections that link the two movies
    Returned once the movie2 is validated
     */
    public List<String> getSharedConnections(IMovie movie1, IMovie movie2);

    /*
    keeps track of how many times a specific connection was used
    param: connection is the specific actor, writer, director, cinematographer, composer
     */
    public int getUsageCount(String connection);

    /*
    updates the number of times a specific connection has been used
     */
    public void recordConnectionUse(List<String> connections);

    /*
    resets all tracked connections for new game
     */
    public void resetUsageCount();
}
