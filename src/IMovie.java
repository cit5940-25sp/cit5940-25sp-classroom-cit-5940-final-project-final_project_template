import java.util.List;
import java.util.Set;

public interface IMovie {

    /*
    returns the title of the movie
     */
    public String getTitle();

    /*
    returns the year the movie was made
     */
    public int getYear();

    /*
    returns the list of genres the movie belongs to
     */
    public List<String> getGenres();

    /*
    returns the list of actors in movie
     */
    public List<String> getActors();

    /*
   returns the list of crew in movie
    */
    public List<String> getCrew();

    public void addActor(String name);

    public void addCrew(String name);

    public void addDirector(String name);

    public void addWriter(String name);

    public void addComposer(String name);

    public void addCinematographer(String name);

    /*
    returns the director of the movie
     */
    public List<String> getDirectors();

    /*
    returns the list of writers
     */
    public List<String> getWriters();

    public List<String> getComposers();

    /*
    returns list of cinematographers
     */
    public List<String> getCinematographers();

    public void addContributor(String name);

    /*
    returns all people associated with the movie
    used to validate connections between movies
     */
    public Set<String> getAllContributors();
}
