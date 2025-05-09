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

    /**
     * Constructor for the GameModel class.
     * Initializes the list of observers and sets the timeout flag to false.
     */
    public GameModel(){
        // Initialize the views list as an ArrayList
        views = new ArrayList<>();
        // Set the initial timeout state to false
        timeOut = false;
        startMovie = "Mission: Impossible";
        gameStatusString = "The start movie is '" + startMovie + "'";
    }


    /**
     * Initializes the game data required for the game model.
     * This method creates instances of GameStatus, MovieDate, and Autocomplete,
     * and populates the autocomplete system with movie titles from the movie data.
     */
    public void initialData(){
        // Create a new instance of GameStatus to manage the game state
        gameStatus = new GameStatus();
        // Create a new instance of MovieDate to handle movie-related data
        movieData = new MovieDate();
        // Create a new instance of Autocomplete with the specified number of suggestions
        autocomplete = new Autocomplete(SUGGESTION);
        // Iterate through all movies in the movie data
        for(Movie movie: movieData.getMovies()){
            // Add each movie title to the autocomplete system with a weight of 1
            autocomplete.addWord(movie.getTitle(), 1);
        }
    }

    /**
     * Sets the timeout state of the game.
     *
     * @param timeOut A boolean value indicating whether the game has timed out.
     */
    public void setTimeOut(boolean timeOut) {
        // Update the timeout state
        this.timeOut = timeOut;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public String getGameStatusString() {
        return gameStatusString;
    }

    public boolean isGameOver(){
        if (isTimeOut()){
            gameStatusString = gameStatus.timeOutString();
            notifyUI();
        }
        return gameStatus.isGameOver() || isTimeOut();
    }

    /**
     * Checks if the provided movie title is valid. A title is considered valid if it exists in the movie data
     * and the corresponding movie has not been used in the game yet.
     *
     * @param title The title of the movie to be validated.
     * @return {@code true} if the title is valid, {@code false} otherwise.
     */
    public boolean isValidTitle(String title){
        // Check if the movie data contains the provided title
        if(movieData.contains(title)){
            // Retrieve the Movie object from the movie data using the provided title
            Movie movie = movieData.getMovieByTitle(title);
            // Check if the retrieved movie has not been used in the game
            if(!gameStatus.isUsed(movie)){
                // If the movie exists and is not used, the title is valid
                return true;
            }
        }
        // If the movie does not exist or has already been used, the title is invalid
        return false;
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