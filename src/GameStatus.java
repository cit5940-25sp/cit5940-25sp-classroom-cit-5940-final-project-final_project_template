import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
public class GameStatus {
    private List<Player> players;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private int round;
    private TreeSet<Movie> usedMovies;


    public GameStatus() {
        usedMovies = new TreeSet<>();
        players = new ArrayList<>(); // Assuming 2 players for simplicity
        currentPlayerIndex = 0;
        currentPlayer = null;
        round = 0;
    }

    public boolean isGameOver() {
        for(Player player : players) {
            if(!player.isFinished()) {
                return false;
            }
        }
        // If all players have finished their turns, the game is over.
        return true;
    }

    /**
     * Retrieves the winner of the game.
     *
     * @return The winning player if the game is over and a winner exists,
     *         {@code null} if the game is not over or no winner is found.
     *         Note: It's assumed that there should always be a winner if the game is over.
     */
    public Player getWinner() {
        // Check if the game is not over. If so, return null as there's no winner yet.
        if(!isGameOver()) {
            return null;
        }
        // Iterate through all players to find the winner.
        for(Player player : players) {
            // If a player is marked as the winner, return that player.
            if(player.isWinner()) {
                return player;
            }
        }
        // This line should never be reached if the game logic is correct and there's always a winner when the game is over.
        return null;
    }

    /**
     * Adds a player to the game.
     *
     * @param player The player to be added to the game.
     */
    public void addPLayer(Player player) {
        players.add(player);
    }

    /**
     * Retrieves the next player in the turn order and updates the current player index.
     *
     * @return The next player in the turn order, or {@code null} if there are no players.
     */
    public Player getNextPlayer() {
        // Check if the list of players is empty. If so, return null.
        if(players.isEmpty())
            return null;
        // Get the current player based on the current index.
        Player player = players.get(currentPlayerIndex);
        // Update the current player index to point to the next player in a circular manner.
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        return player;
    }

    /**
     * Retrieves the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        // Return the current player.
        return currentPlayer;
    }

    public boolean isUsed(Movie movie) {
        return usedMovies.contains(movie);
    }

    public String timeOutString(){
        String message = "Time out!";
        message += "\n" + currentPlayer.getName() + " has lost the game.";
        Player player = getNextPlayer();
        message += "\n" + player.getName() + " is the winner!";
        return message;
    }

    /**
     * Plays a round of the game with the given movie.
     *
     * @param movie The movie to be played in this round.
     * @return {@code true} if the player successfully plays the movie, {@code false} otherwise.
     */
    public boolean inputMovie(Movie movie) {
        boolean r = currentPlayer.play(movie);
        if (r) {
            usedMovies.add(movie);
        }
        return r;
    }
    public void nextRound(){
        round++;
        currentPlayer = getNextPlayer();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Round: ").append(round).append("\n");
        sb.append("Current Player: ").append(currentPlayer.toString());
        sb.append("wait control input...\n");
        return sb.toString();
    }
}
