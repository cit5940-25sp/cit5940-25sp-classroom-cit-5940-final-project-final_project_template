import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {
    private String username;       // The username of the player
    private int progress;          // Progress towards the objective
    private String objectiveGenre; // The target genre for the player
    private int objectiveAmount;   // Number of movies needed to meet the objective

    // Tracks connections (e.g., actor names) and their usage count (max 3)
    //(e.g. actor name) and how many times (max 3)
    private HashMap<String, Integer> linksUsed;


    /**
     * Constructor that initializes a new Player with specified username and objective.
     * @param username The username of the player.
     * @param objectiveGenre The target genre for the player's objective.
     * @param objectiveNumber The number of movies to complete the objective.
     */
    public Player (String username, String objectiveGenre, int objectiveNumber) {
        this.username = username;
        this.objectiveGenre = objectiveGenre;
        this.objectiveAmount = objectiveNumber;
        this.linksUsed = new HashMap<>();
        this.progress = 0;
    }

    /**
     * Handles a movie play by recording connections and checking genre match.
     * @param connections List of connections (e.g., actors) between movies.
     * @param genres List of genres for the current movie.
     * @return True if a connection was successfully used, false otherwise.
     */
    public boolean handleMovie(List<String> connections, List<String> genres) {
        boolean usedConnectionThisTurn = false;

        for (String connection : connections) {
            int count = linksUsed.getOrDefault(connection, 0);
            if (count < 3) {
                linksUsed.put(connection, count + 1);
                usedConnectionThisTurn = true;
            }
        }

        if (usedConnectionThisTurn &&
                genres.stream().anyMatch(genre -> genre.equalsIgnoreCase(objectiveGenre))) {
            progress++;
        }

        return usedConnectionThisTurn;
    }

    /**
     * Checks if the player has met their objective.
     * @return True if the player has completed their objective, false otherwise.
     */
    public boolean hasMetObjective() {
        return (objectiveAmount - progress) == 0;
    }

    /**
     * Calculates the player’s progress as a percentage.
     * @return The progress percentage towards the objective.
     */
    public double progressSoFar() {
        return (progress * 100.0) / objectiveAmount ;
    }

    /**
     * Gets the player's username.
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the player's objective genre.
     * @return The objective genre of the player.
     */
    public String getObjectiveGenre() {
        return objectiveGenre;
    }

    /**
     * Provides a status string for the player.
     * @return The status of the player including progress.
     */
    public String getStatus() {
        return username + " - " + progress + "/" + objectiveAmount + " " + objectiveGenre;
    }
}