import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Game {
    private Movies movies;
    private Player player1;
    private Player player2;
    private String prevMovie;
    private HashSet<String> moviesPlayed;
    private int roundsPlayed;
    // Movie and the links to the last movie played
    private LinkedList<AbstractMap.Entry<String, List<String>>> lastFivePlayed;

    public Game(String fileName, String player1Name, String player2Name,
                String objectiveGenre1, String objectiveGenre2) {
        this.movies = new Movies(fileName);

        // initialize objectives based on level
        this.player1 = new Player(player1Name, objectiveGenre1, 1);
        this.player2 = new Player(player2Name, objectiveGenre2, 1);

        prevMovie = movies.getRandomMovie();
    }

    /*
    player should be 1 or 2
    returns true if movie was valid, false otherwise
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





    public String gameConditionPlayer1(){
        return player1.getObjectiveGenre();
    }
    public String gameConditionPlayer2(){
        return player2.getObjectiveGenre();
    }
    public double progressPlayer1(){
        return player1.progressSoFar();
    }
    public double progressPlayer2(){
        return player1.progressSoFar();
    }
    public String usernamePlayer1(){
        return player1.getUsername();
    }
    public String usernamePlayer2(){
        return player2.getUsername();
    }
    public LinkedList<AbstractMap.Entry<String, List<String>>> getLastFivePlayed() {
        return lastFivePlayed;
    }
    public int getRoundsPlayed() {
        return roundsPlayed;
    }







}
