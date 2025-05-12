import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {
    private String username;
    private int progress;
    private String objectiveGenre;
    private int objectiveAmount;
    //(e.g. actor name) and how many times (max 3)
    private HashMap<String, Integer> linksUsed;


    public Player (String username, String objectiveGenre, int objectiveNumber) {
        this.username = username;
        this.objectiveGenre = objectiveGenre;
        this.objectiveAmount = objectiveNumber;
        this.linksUsed = new HashMap<>();
        this.progress = 0;
    }

    /*
    return true if user still plays, false if user has lost
     */
    // add connection to connection map or increase number
    // of times that connection was used.
    // return false if connection was used more than 3 times

    // update progress if genre matches objectivegenre

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

    public boolean hasMetObjective () {
        return (objectiveAmount - progress) == 0;
    }

    public double progressSoFar () {
        return (progress * 100.0) / objectiveAmount ;
    }

    public String getUsername() {
        return username;
    }

    public String getObjectiveGenre() {
        return objectiveGenre;
    }

    public String getStatus() {
        return username + " - " + progress + "/" + objectiveAmount + " " + objectiveGenre;
    }
}