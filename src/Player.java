import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    String username;
    int progress;
    String objectiveGenre;
    int objectiveAmount;

    //(e.g. actor name) and how many times (max 3)
    HashMap<String, Integer> linksUsed;

    public Player (String username, String objectiveGenre, int objectiveNumber) {

    }

    /*
    return true if user still plays, false if user has lost
     */

    public boolean handleMovie (ArrayList<String> connections, String genre) {

        // add connection to connection map or increase number
        // of times that connection was used.
        // return false if connection was used more than 3 times

        // update progress if genre matches objectivegenre



    }

    public boolean hasMetObjective () {
        return false;
    }
    public double progressSoFar () {

        return (progress * 100.0) / objectiveAmount ;
    }

    //added
    // to add connection and for progress??
    public boolean addConnection(String connection, String genre) {
        linksUsed.put(connection, linksUsed.getOrDefault(connection, 0) + 1);

        // Check if connection has been used more than 3 times
        if (linksUsed.get(connection) > 3) {
            return false;
        }

        // If genre matches objective, update progress
        if (genre.equalsIgnoreCase(objectiveGenre)) {
            progress++;
        }
        return true;
    }

    //added
    //player status
    public String getStatus() {
        return username + " - " + progress + "/" + objectiveAmount + " " + objectiveGenre;
    }
}

