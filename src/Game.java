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
    private boolean turn; // true for player1, false for player2
    private String winner;
    private String autocompleteFile;
    private LinkedList<AbstractMap.Entry<String, List<String>>> lastFivePlayed;

    public Game(String fileName, String player1Name, String player2Name,
                String objectiveGenre1, String objectiveGenre2) {
        this.movies = new Movies(fileName);
        this.autocompleteFile = "src/autocomplete.txt";
        this.player1 = new Player(player1Name, objectiveGenre1, 5);
        this.player2 = new Player(player2Name, objectiveGenre2, 5);
        this.moviesPlayed = new HashSet<>();
        this.lastFivePlayed = new LinkedList<>();
        this.turn = true;
        this.roundsPlayed = 0;
        this.winner = null;
        this.prevMovie = movies.getRandomMovie();
        moviesPlayed.add(prevMovie);
    }

    /**
     * Updates the game state with the movie played by the current player.
     * Returns true if move was valid and game continues, false if invalid.
     */
    public boolean update(String moviePlayed, String player) {
        // Check that movie hasn't already been used
        if (moviesPlayed.contains(moviePlayed)) {
            return false;
        }

        List<String> links = movies.getConnection(prevMovie, moviePlayed);
        if (links.isEmpty()) {
            return false;
        }

        List<String> genres = movies.getMovieGenres(moviePlayed);
        boolean valid = false;

        if (player.equals(player1.getUsername())) {
            valid = player1.handleMovie(links, genres);
            if (player1.hasMetObjective()) {
                winner = player1.getUsername();
            }
        } else if (player.equals(player2.getUsername())) {
            valid = player2.handleMovie(links, genres);
            if (player2.hasMetObjective()) {
                winner = player2.getUsername();
            }
        }

        if (valid) {
            moviesPlayed.add(moviePlayed);
            lastFivePlayed.addFirst(new AbstractMap.SimpleEntry<>(moviePlayed, links));
            if (lastFivePlayed.size() > 5) {
                lastFivePlayed.removeLast();
            }

            prevMovie = moviePlayed;
            roundsPlayed++;
            turn = !turn;
        }

        return valid;
    }

    public boolean isGameOver() {
        return winner != null;
    }

    public String getWinner() {
        return winner;
    }

    public String getWhosTurn() {
        return turn ? player1.getUsername() : player2.getUsername();
    }

    public String gameConditionPlayer1() {
        return player1.getObjectiveGenre();
    }

    public String gameConditionPlayer2() {
        return player2.getObjectiveGenre();
    }

    public double progressPlayer1() {
        return player1.progressSoFar();
    }

    public double progressPlayer2() {
        return player2.progressSoFar();
    }

    public String usernamePlayer1() {
        return player1.getUsername();
    }

    public String usernamePlayer2() {
        return player2.getUsername();
    }

    public LinkedList<AbstractMap.Entry<String, List<String>>> getLastFivePlayed() {
        return lastFivePlayed;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public String getAutocompleteFileName() {
        return autocompleteFile;
    }

    public String getCurrentMovie() {
        return prevMovie;
    }
}