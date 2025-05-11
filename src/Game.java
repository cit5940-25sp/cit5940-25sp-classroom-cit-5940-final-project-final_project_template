import java.util.HashSet;

public class Game {
    Movies movies;
    Player player1;
    Player player2;
    String currentMovie;
    HashSet<String> moviesPlayed;

    public void Game(String fileName, String player1Name, String player2Name, String level) {
        this.movies = new Movies(fileName);

        // initialize objectives based on level
        this.player1 = new Player(player1Name, "someGenre", 1);
        this.player2 = new Player(player2Name, "someGenre", 1);

        currentMovie = movies.getRandomMovie();
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





}
