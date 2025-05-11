import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {
    String username;
    int progress;
    String objectiveGenre;
    int objectiveAmount;
    //(e.g. actor name) and how many times (max 3)
    HashMap<String, Integer> linksUsed;


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

    public boolean handleMovie (List<String> connections, String genre) {
        boolean hasConnection = false;
        for (String connection : connections) {
            if (linksUsed.containsKey(connection)) {
                if (linksUsed.get(connection) < 3) {
                    hasConnection = true;
                }
            } else {
                linksUsed.put(connection, 1);
            }
        }
        if (hasConnection && genre.equals(objectiveGenre)) {
            progress++;
        }
    return hasConnection;

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

    //added
    //player status
    public String getStatus() {
        return username + " - " + progress + "/" + objectiveAmount + " " + objectiveGenre;
    }
}

