package src;

import AutoComplete.Autocomplete;
import AutoComplete.IAutocomplete;
import AutoComplete.ITerm;

import java.util.*;

public class GameModel extends Model implements Observable {
    private List<Observer> views;
    private GameStatus gameStatus;
    private MovieDate movieData;
    private Set<Movie> movies;
    private IAutocomplete autocomplete;
    final int SUGGESTION = 10;

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