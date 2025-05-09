import AutoComplete.Autocomplete;
import AutoComplete.IAutocomplete;
import AutoComplete.ITerm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * GameModel class represents the model component in the MVC architecture for the game.
 * It manages the game state, data, and business logic.
 * Implements the Observable interface to notify observers (views) of state changes.
 */
public class GameModel extends Model implements Observable {
    // List to store all the observers (views) that need to be notified of model changes
    private List<Observer> views;
    // GameStatus object to manage the current status of the game
    private GameStatus gameStatus;
    // MovieDate object to handle all movie-related data
    private MovieDate movieData;
    // IAutocomplete object to provide autocomplete suggestions for movie titles
    private IAutocomplete autocomplete;
    // Constant representing the maximum number of suggestions to be provided by the autocomplete feature
    final int SUGGESTION = 20;
    // String to store the current game status message
    private String gameStatusString;
    // Boolean flag indicating whether the game has timed out
    private boolean timeOut;
    private String startMovie;

    public GameModel() {
        views = new ArrayList<>();
    }

    public void initialData() {
        gameStatus = new GameStatus();
        movieData = new MovieDate();
        autocomplete = new Autocomplete(SUGGESTION);
        movies = movieData.getMovies();
        for (Movie movie : movies) {
            autocomplete.addWord(movie.getTitle(), 1);
        }
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public List<String> getsuggestions(String prefix) {
        List<ITerm> list = autocomplete.getSuggestions(prefix);
        List<String> strings = new LinkedList<>();
        for (ITerm term : list) {
            strings.add(term.getTerm());
        }
        return strings;
    }

    public boolean inputMovie(String title) {
        return false;
    }

    public void notifyUI() {
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        views.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : views) {
            observer.update();
        }
    }


}