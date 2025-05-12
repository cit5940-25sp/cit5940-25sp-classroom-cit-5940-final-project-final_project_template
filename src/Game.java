import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Game class that manages the main game logic, player turns, and game progress.
 */
public class Game {
    private Movies movies;     // The Movies instance to manage movie data
    private Player player1;    // Player 1 in the game
    private Player player2;    // Player 2 in the game
    private String prevMovie;  // The previously played movie
    private HashSet<String> moviesPlayed;  // Set of movies that have been played
    private int roundsPlayed;   // Count of the total rounds played

    // Movie and the links to the last movie played
    private LinkedList<AbstractMap.Entry<String, List<String>>> lastFivePlayed;

    /**
     * Initializes the Game object with two players and a movie database file.
     * @param fileName Path to the file containing movie data.
     * @param player1Name Name of Player 1.
     * @param player2Name Name of Player 2.
     * @param objectiveGenre1 Objective genre for Player 1.
     * @param objectiveGenre2 Objective genre for Player 2.
     */
    public void Game(String fileName, String player1Name, String player2Name,
                     String objectiveGenre1, String objectiveGenre2) {
        this.movies = new Movies(fileName);

        // initialize objectives based on level
        this.player1 = new Player(player1Name, objectiveGenre1, 1);
        this.player2 = new Player(player2Name, objectiveGenre2, 1);

        // Set the initial movie as a random movie
        prevMovie = movies.getRandomMovie();
    }

    /**
     * Updates the game state after a player plays a movie.
     * @param moviePlayed The movie chosen by the player.
     * @param player The player number (1 or 2).
     * @return True if the move was valid, false otherwise.
     */
    public boolean update(String moviePlayed, int player){
        //check that movie hasn't been used before
        if (moviesPlayed.contains(moviePlayed)) {
            return false;
        }
        moviesPlayed.add(moviePlayed);
        // get links with last movie played
        List<String> links = movies.getConnection(prevMovie, moviePlayed);
        if (links.isEmpty()) {
            return false;
        }

        // update corresponding player
        List<String> genres = movies.getMovieGenres(moviePlayed);
        boolean valid = false;
        if (player == 1) {
            valid = player1.handleMovie(links, genres);
        }
        if (player == 2) {
            valid = player2.handleMovie(links, genres);
        }
        // update prevMovie
        // update lastFivePlayed
        if (valid) {
            lastFivePlayed.add(new AbstractMap.SimpleEntry<>(moviePlayed, links));
            if (lastFivePlayed.size() > 5) {
                lastFivePlayed.removeLast();
            }
            prevMovie = moviePlayed;
            roundsPlayed++;

        }
        return valid;
    }

    /**
     * Returns the objective genre for Player 1.
     * @return Player 1's objective genre.
     */
    public String gameConditionPlayer1(){
        return player1.getObjectiveGenre();
    }

    /**
     * Returns the objective genre for Player 2.
     * @return Player 2's objective genre.
     */
    public String gameConditionPlayer2(){
        return player2.getObjectiveGenre();
    }

    /**
     * Returns the progress of Player 1 as a percentage.
     * @return Player 1's progress percentage.
     */
    public double progressPlayer1(){
        return player1.progressSoFar();
    }

    /**
     * Returns the progress of Player 2 as a percentage.
     * @return Player 2's progress percentage.
     */
    public double progressPlayer2(){
        return player1.progressSoFar();
    }

    /**
     * Returns the username of Player 1.
     * @return Player 1's username.
     */
    public String usernamePlayer1(){
        return player1.getUsername();
    }

    /**
     * Returns the username of Player 2.
     * @return Player 2's username.
     */
    public String usernamePlayer2(){
        return player2.getUsername();
    }

    /**
     * Gets the last five played movies with their connections.
     * @return A list of the last five movies and their connections.
     */
    public LinkedList<AbstractMap.Entry<String, List<String>>> getLastFivePlayed() {
        return lastFivePlayed;
    }

    /**
     * Gets the total number of rounds played.
     * @return The number of rounds played.
     */
    public int getRoundsPlayed() {
        return roundsPlayed;
    }
}
