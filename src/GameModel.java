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
    public GameModel(){
        views = new ArrayList<>();
    }

