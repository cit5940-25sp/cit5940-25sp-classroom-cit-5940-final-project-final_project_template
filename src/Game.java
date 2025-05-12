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
    //true for player1 false for player2
    private boolean turn;
    private String winner;
    // Movie and the links to the last movie played
    private LinkedList<AbstractMap.Entry<String, List<String>>> lastFivePlayed;

    public Game (String fileName, String player1Name, String player2Name,
                     String objectiveGenre1, String objectiveGenre2) {
        this.movies = new Movies(fileName);

        // initialize objectives based on level
        this.player1 = new Player(player1Name, objectiveGenre1, 1);
        this.player2 = new Player(player2Name, objectiveGenre2, 1);
        this.turn = true;
        prevMovie = movies.getRandomMovie();




        //TODO: get file from Movies and create an autocomplete


    }

    /*
    player should be 1 or 2
    returns true if movie was valid, false otherwise
     */
    public boolean update(String moviePlayed, String player){
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
        if (player.equals(player1.getUsername())) {
            valid = player1.handleMovie(links, genres);
        } else {
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
            turn = !turn;

        }
        return valid;
    }




    public String getWhosTurn() {
        if (turn) {
            return player1.getUsername();
        } else {
            return player2.getUsername();
        }
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
    // TODO: do this
    public String getAutocompleteFileName() {
        return null;
    }







}
